package com.rotimijohnson.notetakingapp.view.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rotimijohnson.notetakingapp.R;
import com.rotimijohnson.notetakingapp.database.DataBaseHandler;
import com.google.android.material.textfield.TextInputLayout;
import com.rotimijohnson.notetakingapp.model.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    DataBaseHandler db;
    Button next, login;
    TextView title;

    TextInputLayout fullname, username, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DataBaseHandler(this);

        next = findViewById(R.id.signup_next_btn);
        login = findViewById(R.id.signup_login_btn);
        title = findViewById(R.id.signup_title_text);

        //validate variables
        fullname = findViewById(R.id.sign_up_full_name);
        username = findViewById(R.id.sign_up_user_name);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        confirmPassword = findViewById(R.id.sign_up_confirm_password);
    }

    public void Register(View view) {
        if (!validateFullName() | !validateUserName() | !validateEmail() | !validatePassword()) {
            return;
        }

        try {
            User user = new User();
            user.setFullname(fullname.getEditText().getText().toString());
            user.setUsername(username.getEditText().getText().toString());
            user.setEmail(email.getEditText().getText().toString());
            user.setPassword(password.getEditText().getText().toString());
            User temp = db.checkUn(username.getEditText().getText().toString());
            if (temp == null) {
                if (db.create(user)) {
                    String _fullname = fullname.getEditText().getText().toString().trim();
                    String _username = username.getEditText().getText().toString().trim();
                    String _email = email.getEditText().getText().toString().trim();
                    String _password = password.getEditText().getText().toString().trim();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("fullname", _fullname);
                    intent.putExtra("username", _username);
                    intent.putExtra("email", _email);
                    intent.putExtra("password", _password);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Error");
                    builder.setMessage("cannot create new account");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Error");
                builder.setMessage("Username Exists");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Error");
            builder.setMessage(e.getMessage());
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }
//        String Usr = username.getEditText().getText().toString().trim();
//        String Pwd = password.getEditText().getText().toString().trim();
//        String Cnpwd = confirmPassword.getEditText().getText().toString().trim();
//
//        if (Pwd.equals(Cnpwd)){
//            Boolean checkUser;
//            checkUser = db.checkUsername(Usr);
//            if (checkUser){
//                boolean insert = db.insert(Usr, Pwd);
//                if (insert){
//                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
//                    String _fullname = fullname.getEditText().getText().toString().trim();
//                    String _username = username.getEditText().getText().toString().trim();
//                    String _email = email.getEditText().getText().toString().trim();
//                    String _password = password.getEditText().getText().toString().trim();
//
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.putExtra("fullname", _fullname);
//                    intent.putExtra("username", _username);
//                    intent.putExtra("email", _email);
//                    intent.putExtra("password", _password);
//                    startActivity(intent);
//                }
//            }else {
//                Toast.makeText(this, "Username Already Exists", Toast.LENGTH_SHORT).show();
//            }
//        }else {
//            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
//        }
    }

    public void loginScreen(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    //Validate methods

    private boolean validateFullName() {
        String valfullname = fullname.getEditText().getText().toString().trim();

        if (valfullname.isEmpty()) {
            fullname.setError("Field can't be empty");
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName() {
        String valfullname = username.getEditText().getText().toString().trim();
        String noWhiteSpace = "\\A\\W{4,20}\\Z";

        if (valfullname.isEmpty()) {
            username.setError("Field can't be empty");
            return false;
        } else if (valfullname.length() >= 25) {
            username.setError("Full name too long");
            return false;
        } else if (valfullname.matches(noWhiteSpace)) {
            username.setError("White spaces not allowed");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        String passwordval = "^(?:(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*)[^\\s]{8,}$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordval)) {
            password.setError("Password must contain at least 8 letters, one lowercase, one uppercase, one number, one special character");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}