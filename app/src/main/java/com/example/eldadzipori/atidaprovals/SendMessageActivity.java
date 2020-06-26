package com.example.eldadzipori.atidaprovals;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.onesignal.OneSignal;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SendMessageActivity extends AppCompatActivity {

    TextView timePiker;
    AutoCompleteTextView txtTeachers;
    TextView txtMessage;
    Button btnSend;
    String Date = "";
    aprovedUser Student;
    aprovedUser Teacher;

    void onFinish()
    {

        ///Push natifcation here!!!!!

        /*ParsePush push = new ParsePush();
        push.setChannel(Teacher.getString("FirstName") + Teacher.getString("LastName"));
        push.setMessage("A new request from:" + ((aprovedUser) ParseUser.getCurrentUser()).getFullName());
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SendMessageActivity.this, R.style.AppTheme_Dark_Dialog);
                dialog.setTitle("Succses!");
                dialog.setMessage("Request has been diliverd");
                dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });*/

        try{
            Student.fetchIfNeeded();
            OneSignal.postNotification(new JSONObject("{'contents': {'en':'You Have a new request from:"+Student.getFullName()+"'}, 'include_player_ids': ['" + Teacher.getString("oneSignalId") + "']}"), null);

            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        txtTeachers = (AutoCompleteTextView)findViewById(R.id.txtTeacherName);
        btnSend = (Button)findViewById(R.id.btnSendRequest);
        timePiker = (TextView)findViewById(R.id.timePicker);
        txtMessage =(TextView)findViewById(R.id.txtMessage);
        ArrayList<aprovedUser> objects  = (ArrayList<aprovedUser>)aprovedUser.getTeacherForClass(ParseUser.getCurrentUser().getString("Grade"));
        teachersCorrectionAdapter adapter = new teachersCorrectionAdapter(this,0,objects);
        txtTeachers.setAdapter(adapter);

        Student = (aprovedUser)ParseUser.getCurrentUser();
        timePiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                int year = mcurrentTime.get(Calendar.YEAR);
                int month = mcurrentTime.get(Calendar.MONTH);
                final int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SendMessageActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Date = year + "-" + monthOfYear + "-" + dayOfMonth;

                        TimePickerDialog dialog1 = new TimePickerDialog(SendMessageActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Date += " " + hourOfDay + ":" + minute;
                                timePiker.setText(Date);
                            }
                        }, hour, day, true);
                        dialog1.setTitle("Select Date");
                        dialog1.show();
                    }
                }, year, month, day);
                dialog.setTitle("Select Time");
                dialog.show();
            }
        });

        txtTeachers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Teacher = (aprovedUser) parent.getAdapter().getItem(position);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean b = false;
                if (txtMessage.getText().length() < 5) {
                    txtMessage.setError("Must write a message!");
                    b = true;
                }
                if (Teacher == null || !txtTeachers.getText().toString().equals(Teacher.getFullName())) {
                    txtTeachers.setError("Must pick a teacher from the list");
                    b = true;
                }
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
                java.util.Date d = new Date();
                try {
                    d = dt.parse(timePiker.getText().toString());
                } catch (Exception e) {
                    timePiker.setError("Must pick a date");
                    return;
                }
                if (b) return;
                timePiker.setError(null);
                ((aprovedUser) ParseUser.getCurrentUser()).sendMessage(Teacher, txtMessage.getText().toString(), d);
                onFinish();
            }


        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}
