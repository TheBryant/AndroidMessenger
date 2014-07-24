package com.sinch.messagingtutorialskeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.example.messagingtutorialskeleton.R;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;


public class LoginActivity extends Activity {
    private Button signUpButton;
    private Button loginButton;
    private EditText usernameField;
    private EditText passwordField;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "CUSvRfGOkKbtqRCUvyeWJueCosdm8J27OZh92blz", "UV0Upul7lieWhd1fgvVZAoFeEMCVVO3LlTlKHd8h");
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(getApplicationContext(), ListUserActivity.class);
            startActivity(i);
        }

        setContentView(R.layout.activity_login);

        usernameField = (EditText)findViewById(R.id.loginUsername);
        passwordField = (EditText)findViewById(R.id.loginPassword);
        signUpButton = (Button)findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Signup", Toast.LENGTH_SHORT).show();
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null){
                            Intent i = new Intent(getApplicationContext(), ListUserActivity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.signup_error, Toast.LENGTH_SHORT ).show();
                        }
                    }
                });
            }
        });
        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, com.parse.ParseException e) {
                        if (parseUser != null){
                            Intent i = new Intent(getApplicationContext(), ListUserActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(),R.string.login_error_password, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });






    }

}
