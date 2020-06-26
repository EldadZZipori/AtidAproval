package com.example.eldadzipori.atidaprovals;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    //Controllers for the view
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    TextView btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseUser.getCurrentUser().logOut();

        //Inzalting Contorlers to the layout
        this.etEmail = (EditText)findViewById(R.id.input_email);
        this.etPassword = (EditText)findViewById(R.id.input_password);
        this.btnLogin = (Button)findViewById(R.id.btn_login);
        this.btnCreate = (TextView)findViewById(R.id.link_signup);

        SharedPreferences file = getSharedPreferences("User",0);

        etEmail.setText(file.getString("User",""));

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validate()) {
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();
                    aprovedUser.logInInBackground(etEmail.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {

                            progressDialog.dismiss();

                            if (parseUser != null) {
                                SharedPreferences preferences = getSharedPreferences("User",0);
                                preferences.edit().putString("User",parseUser.getEmail()).commit();

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            } else {

                                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
                                dialog.setMessage("Unable to login, If you do no have a user connect your teacher to get a code and sign-up!");
                                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }

                        }
                    });

                }
            }
        });
    }

    public boolean Validate()
    {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
             etEmail.setError("enter a valid email address");
            valid = false;
        }
        else{
            etEmail.setError(null);
        }
        if(password.isEmpty() || password.length() < 3) {
            etPassword.setError("at least 3 characters");
            valid = false;
        }
        else {
            etPassword.setError(null);
        }
        return valid;
    }
}
