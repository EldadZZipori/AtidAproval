package com.example.eldadzipori.atidaprovals;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.parse.ParseException;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eldadzipori on 3/6/16.
 */
public class messageAdapter extends ArrayAdapter<aprovedMessage> {

    Context context;
    List<aprovedMessage> messageList;

    public messageAdapter(Context context, int resource, List<aprovedMessage> objects) {
        super(context, resource, objects);

        this.context = context;
        this.messageList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(R.layout.message_item_layout, parent, false);


        TextView name = (TextView)v.findViewById(R.id.txtName);
        TextView txt  = (TextView)v.findViewById(R.id.txtTime);
        CheckBox cb = (CheckBox)v.findViewById(R.id.cbIsApproved);

        aprovedMessage message = messageList.get(position);
        try {
            message.fetch();
        }
        catch (ParseException e){
            txt.setText("Problem Connecting to Server");
            return  v;
        }

        cb.setChecked(messageList.get(position).isAprroved());

        name.setText(message.getStudent().getFullName());
        Date date = messageList.get(position).getTimeOfLeaving();
        SimpleDateFormat format = new SimpleDateFormat(aprovedApp.TIME_FORMAT);
        String time = format.format(date);

        txt.setText(time);


        return v;
    }
}
