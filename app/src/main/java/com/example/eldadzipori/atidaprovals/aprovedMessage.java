package com.example.eldadzipori.atidaprovals;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by eldadzipori on 3/5/16.
 */
@ParseClassName("Message")
public class aprovedMessage extends ParseObject {
    public aprovedUser getTeacher()
    {
        return (aprovedUser) this.get("Teacher");
    }
    public aprovedUser getStudent()
    {
        return (aprovedUser) this.get("Student");
    }
    public String getMessage()
    {
        return this.getString("Message");
    }
    public boolean isAprroved()
    {
        return this.getBoolean("IsApproved");
    }
    public Date getTimeOfLeaving()
    {
        return this.getDate("TimeOfLeaving");
    }



}
