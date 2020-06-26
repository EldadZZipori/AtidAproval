package com.example.eldadzipori.atidaprovals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.onesignal.OneSignal;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView messagesListView ;
    ArrayList<aprovedMessage> items;
    boolean isTeacher = false;
    String grade ;
    aprovedUser user ;
    int limit = 5;
    Button btnChangeCode;
    ProgressDialog pd;


    void Refresh(boolean b )
    {
        pd.show();
        if(!isTeacher){
            ParseQuery<aprovedMessage> query = new ParseQuery<aprovedMessage>(aprovedMessage.class);
            query.whereEqualTo("Student", user);
            query.orderByDescending("createdAt");
            if(b) {
                query.setLimit(limit);
            }

            query.findInBackground(new FindCallback<aprovedMessage>() {
                @Override
                public void done(List<aprovedMessage> list, ParseException e) {
                    items = (ArrayList)list;
                    messageAdapter adapter = new messageAdapter(MainActivity.this,0,items);
                    messagesListView.setAdapter(adapter);
                    pd.dismiss();
                }
            });
        }
        else {
            isTeacher = true;
            ParseQuery<aprovedMessage> query = new ParseQuery<aprovedMessage>(aprovedMessage.class);
            query.whereEqualTo("Teacher", user);
            query.orderByDescending("createdAt");
            if(b){
                query.setLimit(limit);
            }

            query.findInBackground(new FindCallback<aprovedMessage>() {
                @Override
                public void done(List<aprovedMessage> list, ParseException e) {
                    items = (ArrayList)list;
                    messageAdapter adapter = new messageAdapter(MainActivity.this,0,items);
                    messagesListView.setAdapter(adapter);
                    pd.dismiss();
                }
            });


        }
    }

    @Override
    public void onBackPressed() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this,R.style.AppTheme_Dark_Dialog);
        dialog.setTitle("Warning!!!");
        dialog.setMessage("Are you sure you want to logout?");
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ParseUser.logOut();
                finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pd = new ProgressDialog(MainActivity.this,R.style.AppTheme_Dark_Dialog);
        pd.setMessage("data is coming from server...");
        pd.setIndeterminate(true);

        btnChangeCode = (Button)findViewById(R.id.btnChangeCode);
        messagesListView = (ListView)findViewById(R.id.lvMessages);


        user = (aprovedUser) ParseUser.getCurrentUser();


        try {
            user.fetchIfNeeded();
            grade = user.getGrade();
        }
        catch (Exception e)
        {

        }
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                user.put("oneSignalId",userId);
                try{
                    user.save();
                }
                catch (Exception e){}
            }
        });

        if(user.isTeacher()) {
            ParsePush.subscribeInBackground(user.getString("FirstName")+user.getString("LastName"));
            isTeacher = true;

            messagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(MainActivity.this,AppvrovalActivity.class);
                    i.putExtra("name",items.get(position).getStudent().getFullName());
                    i.putExtra("message",items.get(position).getMessage());
                    i.putExtra("time",items.get(position).getTimeOfLeaving().toString());
                    i.putExtra("is",items.get(position).isAprroved());
                    i.putExtra("id",items.get(position).getObjectId());
                    startActivity(i);

                }
            });
        }
        else {
            isTeacher = false;
            btnChangeCode.setVisibility(View.INVISIBLE);
         }

        Refresh(true);


        btnChangeCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prompt();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(!isTeacher) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, SendMessageActivity.class));
                }
            });
        }
        else {
            fab.hide();
        }
    }

    void Prompt(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View promptView = inflater.inflate(R.layout.eddittext_dialog_layout,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this,R.style.AppTheme_Dark_Dialog);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText userInput = (EditText) promptView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                if (userInput.getText().length() == 5) {
                                    userInput.setError(null);
                                    if (aprovedCode.changeCodeForGrade(grade, userInput.getText().toString())) {
                                        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                                        b.setTitle("Succses!!!");
                                        b.setMessage("Code Was Changed Succsesfuly");
                                        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.dismiss();
                                        b.show();
                                    } else {
                                        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                                        b.setTitle("Opps!!!");
                                        b.setMessage("Something went wrong");
                                        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.dismiss();
                                        b.show();
                                    }
                                } else {
                                    userInput.setError("only 5 charecters");
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Change Grade Code");
        // show it
        alertDialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
       Refresh(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ParseUser.getCurrentUser().logOut();
            finish();
            return true;
        }
        else if(id == R.id.action_limitFive)
        {
            limit = 5;
            Refresh(true);
        }
        else if(id == R.id.action_limitOne){
            limit = 1;
            Refresh(true);
        }
        else if(id == R.id.action_Refresh){
            Refresh(true);
        }
        else if(id == R.id.action_limitall){

            Refresh(false);
        }


        return super.onOptionsItemSelected(item);
    }
}
