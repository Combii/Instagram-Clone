package com.combii.instagram;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    EditText username, password;
    Button loginButton;
    boolean isInSignUpMode;
    TextView login;
    ConstraintLayout background;
    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isInSignUpMode = true;

        loginButton = (Button) findViewById(R.id.signIn);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.passWord);
        login = (TextView) findViewById(R.id.login);
        background = (ConstraintLayout) findViewById(R.id.background);
        logo = (ImageView) findViewById(R.id.logo);

        background.setOnClickListener(this);
        logo.setOnClickListener(this);


        //Setup textView login button click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInSignUpMode) {
                    isInSignUpMode = false;
                    login.setText("or Sign Up");
                    loginButton.setText("Log in");
                } else {
                    isInSignUpMode = true;
                    login.setText("or Log In");
                    loginButton.setText("Sign Up");
                }
            }
        });

        password.setOnKeyListener(this);

    }

    public void clickedSignIn(View view) {

        String enteredUsername = username.getText().toString();
        String enteredPassword = password.getText().toString();

        if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show();
        } else {

            if (!isInSignUpMode) {


                ParseUser.logInInBackground(enteredUsername, enteredPassword, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            Log.i("Login", "Successful");

                            Intent intent = new Intent(getApplicationContext(), ListUsersActivity.class);
                            startActivity(intent);

                        } else {
                            Log.i("Login", "Failed. Error: " + e.toString());
                            Toast.makeText(MainActivity.this, "Invalid activeUsername/password", Toast.LENGTH_SHORT).show();
                        }
                    }


                });

                //Is in log in
            } else {
              ParseUser user = new ParseUser();

                user.setUsername(enteredUsername);
                user.setPassword(enteredPassword);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Sign Up", "Successful");
                        } else {
                            Log.i("Sign Up", "Failed. Error: " + e.toString());
                            Toast.makeText(MainActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN) {
            clickedSignIn(view);
        }


        return false;
    }

    @Override
    public void onClick(View view) {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

}
