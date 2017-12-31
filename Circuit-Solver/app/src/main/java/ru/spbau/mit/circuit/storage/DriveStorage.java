package ru.spbau.mit.circuit.storage;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.spbau.mit.circuit.MainActivity;

public class DriveStorage implements Storage {

    private static final int BUFFER_SIZE = 1024;
    @NonNull
    private final MainActivity activity;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private DriveClient driveClient;
    private DriveResourceClient driveResourceClient;

    public DriveStorage(Activity mainActivity) {
        this.activity = (MainActivity) mainActivity;
        activity.initDrive(this);
    }

    public void initializeDriveClient(@NonNull GoogleSignInAccount signInAccount) {
        driveClient = Drive.getDriveClient(activity.getApplicationContext(), signInAccount);
        driveResourceClient = Drive.getDriveResourceClient(activity.getApplicationContext(),
                signInAccount);
    }

    @Override
    public void save(@NonNull byte[] bytes, @NonNull String filename) {
        final Task<DriveFolder> rootFolderTask = driveResourceClient.getAppFolder();
        final Task<DriveContents> createContentsTask = driveResourceClient.createContents();
        Tasks.whenAll(rootFolderTask, createContentsTask)
                .continueWithTask(task -> {
                    DriveFolder parent = rootFolderTask.getResult();
                    DriveContents contents = createContentsTask.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    outputStream.write(bytes);

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(filename)
                            .setMimeType("text/plain")
                            .setStarred(true)
                            .build();

                    return driveResourceClient.createFile(parent, changeSet, contents);
                });
        System.out.println("done " + filename);
    }

    @NonNull
    @Override
    public List<String> getCircuits() {
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                .build();

        Task<MetadataBuffer> queryTask = driveResourceClient.query(query);
        List<String> circuitsNames = new ArrayList<>();

        try {
            Tasks.await(queryTask);
            System.out.println("QueryTasks finished");
            for (Metadata metadata : queryTask.getResult()) {
                circuitsNames.add(metadata.getTitle());
            }
            System.out.println(circuitsNames.toString());
        } catch (@NonNull ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return circuitsNames;
    }

    @Nullable
    @Override
    public ByteArrayInputStream load(String filename) {
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                .build();

        Task<MetadataBuffer> queryTask = driveResourceClient.query(query);

        DriveFile driveFile = null;
        try {
            Tasks.await(queryTask);
            for (Metadata metadata : queryTask.getResult()) {
                if (metadata.getTitle().equals(filename)) {
                    DriveId driveId = metadata.getDriveId();
                    driveFile = driveId.asDriveFile();
                    break;
                }
            }
        } catch (@NonNull ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Task<DriveContents> driveContentsTask = driveResourceClient.openFile(driveFile,
                DriveFile.MODE_READ_ONLY);
        ByteArrayInputStream stream = null;

        try {
            Tasks.await(driveContentsTask);
            InputStream inputStream = driveContentsTask.getResult().getInputStream();

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            byte[] buff = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buff)) != -1) {
                bao.write(buff, 0, bytesRead);
            }

            byte[] data = bao.toByteArray();

            stream = new ByteArrayInputStream(data);
        } catch (@NonNull InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        return stream;
    }

    @Override
    public void delete(String filename) throws LoadingException {
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                .build();

        Task<MetadataBuffer> queryTask = driveResourceClient.query(query);

        DriveFile driveFile = null;
        try {
            Tasks.await(queryTask);
            for (Metadata metadata : queryTask.getResult()) {
                if (metadata.getTitle().equals(filename)) {
                    DriveId driveId = metadata.getDriveId();
                    driveFile = driveId.asDriveFile();
                    break;
                }
            }
        } catch (@NonNull InterruptedException | ExecutionException e) {
            throw new LoadingException(e);
        }
        if (driveFile != null) {
            driveResourceClient.delete(driveFile);
        }
    }
}
