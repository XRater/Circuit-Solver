package ru.spbau.mit.circuit.storage;


import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.spbau.mit.circuit.MainActivity;

public class DriveStorage implements Storage {

    private final MainActivity activity;

    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;

    public DriveStorage(Activity mainActivity) {
        this.activity = (MainActivity) mainActivity;
        activity.initDrive(this);
    }

    public void initializeDriveClient(GoogleSignInAccount signInAccount) {
        mDriveClient = Drive.getDriveClient(activity.getApplicationContext(), signInAccount);
        mDriveResourceClient = Drive.getDriveResourceClient(activity.getApplicationContext(),
                signInAccount);
    }

    @Override
    public void save(byte[] bytes, String filename) {
        final Task<DriveFolder> rootFolderTask = mDriveResourceClient.getAppFolder();
        final Task<DriveContents> createContentsTask = mDriveResourceClient.createContents();
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

                    return mDriveResourceClient.createFile(parent, changeSet, contents);
                });
    }

    @Override
    public List<String> getCircuits() {
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                .build();

        Task<MetadataBuffer> queryTask = mDriveResourceClient.query(query);
        List<String> circuitsNames = new ArrayList<>();

        try {
            Tasks.await(queryTask);
            for (Metadata metadata : queryTask.getResult()) {
                circuitsNames.add(metadata.getTitle());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return circuitsNames;
    }

    @Override
    public ByteArrayInputStream load(String filename) {
//        mDriveClient.newOpenFileActivityIntentSender(openOptions);
//        Task<DriveContents> openFileTask =
//                mDriveResourceClient.openFile(filename, DriveFile.MODE_READ_ONLY);
        return null;
    }
}
