package ru.spbau.mit.circuit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.tasks.Task;

import java.util.HashSet;
import java.util.Set;

import ru.spbau.mit.circuit.controler.Controller;
import ru.spbau.mit.circuit.storage.DriveStorage;
import ru.spbau.mit.circuit.ui.NewCircuitActivity;
import ru.spbau.mit.circuit.ui.UI;

/**
 * Initialization of main parts of the program is here. Two buttons
 * for activities.
 */
public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_SIGN_IN = 0;

    public static UI ui;
    private static Controller controller;
    private DriveStorage driverStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new Controller(this);
        ui = controller.getUi();
    }

    public void onNewCircuit(View view) {
        controller.clearModel();
        ui.setCircuitWasNotLoaded();
        Intent intent = new Intent(MainActivity.this, NewCircuitActivity.class);
        startActivity(intent);
    }

    public void onLoadCircuit(View view) {
        Intent intent = new Intent(MainActivity.this, LoadActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode != RESULT_OK) {
                    //TODO
                    finish(); // No way without sign-it
                    return;
                }

                Task<GoogleSignInAccount> getAccountTask =
                        GoogleSignIn.getSignedInAccountFromIntent(data);
                if (getAccountTask.isSuccessful()) {
                    driverStorage.initializeDriveClient(getAccountTask.getResult());
                } else {
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // This methods initializes drive work
    public void initDrive(DriveStorage storage) {
        this.driverStorage = storage;
        signInForDrive();
    }

    private void signInForDrive() {
        Set<Scope> requiredScopes = new HashSet<>();
        requiredScopes.add(Drive.SCOPE_FILE);
        requiredScopes.add(Drive.SCOPE_APPFOLDER);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null && signInAccount.getGrantedScopes().containsAll(requiredScopes)) {
            driverStorage.initializeDriveClient(signInAccount);
        } else {
            GoogleSignInOptions signInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(Drive.SCOPE_FILE)
                            .requestScopes(Drive.SCOPE_APPFOLDER)
                            .build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
            startActivityForResult(googleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
        }
    }
}
