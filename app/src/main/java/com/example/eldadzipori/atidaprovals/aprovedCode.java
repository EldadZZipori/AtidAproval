package com.example.eldadzipori.atidaprovals;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

import java.util.List;

/**
 * Created by eldadzipori on 3/7/16.
 */
@ParseClassName("Codes")
public class aprovedCode extends ParseObject {

    public String getCode(){return this.getString("Code");}
    public  String getGrade(){return this.getString("Grade");}
    public void setGrade(String grade){this.put("Grade",grade);}
    public boolean setCode(String code){
        if(thereIsCode(code)!=null){
            return false;
        }
        this.put("Code",code);
        return true;

    }

    public  static  boolean changeCodeForGrade(String grade,String code)
    {
        if(thereIsCode(code)!=null){
            return false;
        }
        ParseQuery<aprovedCode> query = new ParseQuery<aprovedCode>(aprovedCode.class);
        query.whereEqualTo("Grade",grade);
        try{
            aprovedCode ac = query.getFirst();
            ac.setCode(code);
            ac.save();

        }
        catch (Exception e)
        {
            return false;
        }
        return  true;
    }
    public static boolean makeNewCode(String grade,String code)
    {
        if(thereIsCode(code)!=null){
            return false;
        }
        aprovedCode aprovedCode = new aprovedCode();
        aprovedCode.put("Code",code);
        aprovedCode.put("Grade",grade);

        try{
            aprovedCode.save();
            return true;
        }
        catch (ParseException e){
            return  false;
        }

    }
    public static List<aprovedCode> thereIsCode(String code)
    {
        ParseQuery<aprovedCode> query = new ParseQuery<aprovedCode>(aprovedCode.class);
        query.whereEqualTo("Code",code);
        try{
            List<aprovedCode> l = query.find();
          if( l.size() ==0)
                return null;
            else {
              return l;
          }
        }
        catch (ParseException e){
            String s = e.getMessage();
        }
        return null;
    }
}
