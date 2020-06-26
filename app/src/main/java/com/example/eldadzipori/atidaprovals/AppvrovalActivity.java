package com.example.eldadzipori.atidaprovals;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.onesignal.OneSignal;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppvrovalActivity extends AppCompatActivity {

    ScrollView svMessage;
    TextView tvMessage;
    TextView tvStudentName;
    TextView tvDate;
    CheckBox cbIsAproved;
    Button btnSend;
    Intent i;
    ProgressDialog pd;
    Boolean notYet;

    AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appvroval);

        setTitle("Approve Requsets");


        pd = new ProgressDialog(AppvrovalActivity.this,R.style.AppTheme_Dark_Dialog);
        pd.setIndeterminate(true);
        pd.setMessage("Saving....");

        ad = new AlertDialog.Builder(AppvrovalActivity.this,R.style.AppTheme_Dark_Dialog);
        ad.setTitle("All most there");
        ad.setMessage("Are you sure you want to make these changes?");
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                pd.show();
                ParseQuery<aprovedMessage> query = new ParseQuery<aprovedMessage>(aprovedMessage.class);
                query.whereEqualTo("objectId", i.getStringExtra("id"));


                final aprovedMessage message;
                try{
                   message = query.getFirst();
                    message.put("IsApproved", cbIsAproved.isChecked());
                    message.save();
                    AlertDialog.Builder b = new AlertDialog.Builder(AppvrovalActivity.this,R.style.AppTheme_Dark_Dialog);
                    b.setMessage("Saved");
                    b.setTitle("Yay");
                    b.setCancelable(false);
                    b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!notYet&&notYet!=cbIsAproved.isChecked())
                            {
                                try{
                                    message.fetchIfNeeded();
                                    OneSignal.postNotification(new JSONObject("{'contents': {'en':'Your request is approved!:" + message.getStudent().getFullName() + "'}, 'include_player_ids': ['" + message.getStudent().getString("oneSignalId") + "']}"), null);

                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            dialog.dismiss();
                            finish();


                        }
                    });
                    pd.dismiss();
                    b.show();

                }catch (Exception e){
                    AlertDialog.Builder b = new AlertDialog.Builder(AppvrovalActivity.this,R.style.AppTheme_Dark_Dialog);
                    b.setMessage("Problem in Saving");
                    b.setTitle("Opps");
                    b.setCancelable(false);
                    b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            dialog.dismiss();

                        }
                    });
                    b.show();
                }



            }
        });

        i = getIntent();

        svMessage =(ScrollView)findViewById(R.id.svStudentMessage);
        tvDate =(TextView)findViewById(R.id.txtDate);
        tvMessage =(TextView)findViewById(R.id.txtStudentMessage);
        tvStudentName =(TextView)findViewById(R.id.txtNameOfStudent);
        cbIsAproved =(CheckBox)findViewById(R.id.cbAprov);
        btnSend =(Button)findViewById(R.id.btnSaveChanges);
        notYet = cbIsAproved.isChecked();


        tvDate.setText(i.getStringExtra("time"));
        tvMessage.setText(i.getStringExtra("message"));
        tvStudentName.setText(i.getStringExtra("name"));
        cbIsAproved.setChecked(i.getBooleanExtra("is", false));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.show();
            }
        });


    }
}
