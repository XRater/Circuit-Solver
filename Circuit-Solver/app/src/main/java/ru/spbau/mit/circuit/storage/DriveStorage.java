package ru.spbau.mit.circuit.storage;


import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveResourceClient;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import ru.spbau.mit.circuit.MainActivity;

public class DriveStorage implements Storage {

    private final MainActivity activity;

    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;

    public DriveStorage(Activity mainActivity) {
        this.activity = (MainActivity) mainActivity;
        activity.initDrive(this);
//        initializeDriveClient(activity.getSignedInAccount());
    }

    public void initializeDriveClient(GoogleSignInAccount signInAccount) {
        mDriveClient = Drive.getDriveClient(activity.getApplicationContext(), signInAccount);
        mDriveResourceClient = Drive.getDriveResourceClient(activity.getApplicationContext(),
                signInAccount);
    }

    @Override
    public void saveToNewFile(byte[] stream, String filename) {

    }

    @Override
    public Collection<String> getFiles() {
        return null;
    }

    @Override
    public ByteArrayInputStream loadFromFile(String filename) {
        return null;
    }
}
