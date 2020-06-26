package com.example.eldadzipori.atidaprovals;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.ParseException;

import java.util.List;

public class SignupActivity extends AppCompatActivity {

    public static final int SIGNUP_REQUEST = 63245;

    EditText etEmail;
    EditText etFirstName;
    EditText etLastName;
    EditText etCode;
    EditText etPassword;
    Button btnSignUp;
    TextView linkLogIn;
    SharedPreferences file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        etEmail = (EditText)findViewById(R.id.etUpEmail);
        etFirstName = (EditText)findViewById(R.id.etUpFirstName);
        etLastName = (EditText)findViewById(R.id.etUpLastName);
        etCode = (EditText)findViewById(R.id.etUpCode);
        etPassword = (EditText)findViewById(R.id.etUpPassword);
        btnSignUp = (Button)findViewById(R.id.btn_signup);
        linkLogIn = (TextView)findViewById(R.id.link_login);

        file =  getSharedPreferences("UsedCodes",0);

        linkLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validate())
                {
                    final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();

                    if(file.getString(etCode.getText().toString(),"") != "1") {
                        List<aprovedCode> codes = aprovedCode.thereIsCode(etCode.getText().toString());
                        if (codes != null && codes.size()!=0) {


                            AlertDialog.Builder dialog = new AlertDialog.Builder(SignupActivity.this, R.style.AppTheme_Dark_Dialog);


                            if (aprovedUser.createUser(etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), codes.get(0).getGrade())) {

                               boolean b = file.edit().putString(codes.get(0).getCode(), "1").commit();
                                progressDialog.dismiss();
                                dialog.setMessage("User was created successfully");
                                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                dialog.show();
                            } else {
                                progressDialog.dismiss();
                                dialog.setMessage("Error in creating user");
                                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        } else {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
                            builder.setMessage("Code is invalid");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }
                    else
                    {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
                        builder.setMessage("Code have already been used on this device");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }

            }
        });

    }


    public boolean Validate()
    {
        boolean valid = true;

        String firstname = etFirstName.getText().toString();
        String lastname = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String code = etCode.getText().toString();
        String password = etPassword.getText().toString();

        if(firstname.isEmpty() || firstname.length() < 3){
            etFirstName.setError("at least 3 characters");
            valid = false;
        }
        else {
            etFirstName.setError(null);
        }

        if(lastname.isEmpty() || lastname.length() < 3){
            etLastName.setError("at least 3 characters");
            valid = false;
        }
        else {
            etLastName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 ) {
            etPassword.setError("at least 4 characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        if(code.length() != 5) {
            etCode.setError("only 5 characters");
            valid = false;
        } else {
            etCode.setError(null);
        }






        return valid;

    }

}
