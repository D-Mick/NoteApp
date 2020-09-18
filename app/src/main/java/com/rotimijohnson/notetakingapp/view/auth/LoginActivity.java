package com.rotimijohnson.notetakingapp.view.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rotimijohnson.notetakingapp.R;
import com.rotimijohnson.notetakingapp.database.DataBaseHandler;
import com.rotimijohnson.notetakingapp.model.User;
import com.rotimijohnson.notetakingapp.util.SessionManagement;
import com.rotimijohnson.notetakingapp.view.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    DataBaseHandler db;
    TextInputLayout loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = findViewById(R.id.usernameEmail);
        loginPassword = findViewById(R.id.passwordLogin);
        db  = new DataBaseHandler(this);
        String _username = getIntent().getStringExtra("username");
        loginEmail.getEditText().setText(_username);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private boolean validateUserName() {
        String valfullname = loginEmail.getEditText().getText().toString().trim();
        String noWhiteSpace = "\\A\\W{4,20}\\Z";

        if (valfullname.isEmpty()) {
            loginEmail.setError("Field can't be empty");
            return false;
        } else if (valfullname.length() >= 25) {
            loginEmail.setError("Full name too long");
            return false;
        } else if (valfullname.matches(noWhiteSpace)) {
            loginEmail.setError("White spaces not allowed");
            return false;
        } else {
            loginEmail.setError(null);
            loginEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = loginPassword.getEditText().getText().toString().trim();
        String passwordval = "^(?:(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*)[^\\s]{8,}$";

        if (val.isEmpty()) {
            loginPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordval)) {
            loginPassword.setError("Password must contain at least 8 letters, one lowercase, one uppercase, one number, one special character");
            return false;
        } else {
            loginPassword.setError(null);
            loginPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void checkSession() {
        //check if user is logged in
        //if user is logged in --> move to mainActivity
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        int userID = sessionManagement.getSession();
        if (userID != -1){
            //user id logged in and so move to mainActivity
        }else {
            //do nothing
        }
    }

    private void moveToMainActivity(){
        String _loginEmail = loginEmail.getEditText().getText().toString().trim();
        String _loginPassword = loginPassword.getEditText().getText().toString().trim();
        User user = db.login(_loginEmail,_loginPassword);
        if (user == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Invalid account");
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("username", _loginEmail);
            intent.putExtra("password", _loginPassword);
            intent.putExtra("account",user);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void MainActivityScreen(View view) {
        if (!validatePassword() |!validateUserName()){
            return;
        }

        User user = new User();
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        sessionManagement.saveSession(user);
        moveToMainActivity();
    }

    public void createAccount(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}