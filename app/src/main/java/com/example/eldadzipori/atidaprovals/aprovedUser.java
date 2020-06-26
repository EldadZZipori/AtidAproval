package com.example.eldadzipori.atidaprovals;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eldadzipori on 3/5/16.
 */
public class aprovedUser extends ParseUser {
    public boolean isTeacher()
    {
        return this.getBoolean("IsTeacher");
    }
    public String getGrade()
    {
        return this.getString("Grade");
    }
    public String getFullName()
    {
        try{
            this.fetchIfNeeded();
        }
        catch (Exception e)
        {

        }
        StringBuilder s = new StringBuilder();
        s.append(this.getString("FirstName"));
        s.append(" ");
        s.append(this.getString("LastName"));
        return s.toString();
    }
    public boolean sendMessage(aprovedUser Teacher,String Content,Date Time)
    {
        aprovedMessage message = new aprovedMessage();
        message.put("Message",Content);
        message.put("Teacher",Teacher);
        message.put("Student",this);
        message.put("TimeOfLeaving",Time);
        try{
            message.save();
            return true;
        }
        catch (ParseException e){
            return false;
        }
    }
    public ArrayList<aprovedMessage> getAllMessages()
    {
        if(this.isTeacher())
        {
            ParseQuery<aprovedMessage> query = new ParseQuery<aprovedMessage>(aprovedMessage.class);
            query.whereEqualTo("Teacher",this);
            try
            {
               ArrayList<aprovedMessage> messages = (ArrayList<aprovedMessage>) query.find();
                return messages;
            }
            catch (ParseException e) {}
            return null;

        }
        ParseQuery<aprovedMessage> query = new ParseQuery<aprovedMessage>(aprovedMessage.class);
        query.whereEqualTo("Student", this);
        try
        {
            ArrayList<aprovedMessage> messages =(ArrayList<aprovedMessage>)  query.find();
            return messages;
        }
        catch (ParseException e) {}
        return null;

    }
    public static List<aprovedUser> getTeacherForClass(String grade)
    {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TeacherForClass");
        query.whereEqualTo("Grade",grade);
        List<ParseObject> list;
        try{
            list = query.find();
            List<aprovedUser> returnList = new ArrayList<>();
            for(ParseObject o : list)
            {
                returnList.add((aprovedUser)o.get("Teacher"));
            }
            return returnList;
        }
        catch (ParseException e){ return null;}


    }

    public static boolean createUser(String firstName,String lastName,String email,String password,String grade)
    {

        aprovedUser newUser = null ;
        try {
             newUser = new aprovedUser();
        }
        catch (Exception e){
            String s = e.getMessage();
        }
        newUser.put("Grade",grade);
        newUser.put("password",password);
        newUser.put("email",email);
        newUser.put("username",email);
        newUser.put("IsTeacher",false);
        newUser.put("FirstName",firstName);
        newUser.put("LastName",lastName);

        try{
            newUser.signUp();

        }
        catch (ParseException e){
            String s = e.getMessage();
            return false;
        }
        return true;
    }


}
