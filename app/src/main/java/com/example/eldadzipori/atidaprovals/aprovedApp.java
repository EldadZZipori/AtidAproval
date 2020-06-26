package com.example.eldadzipori.atidaprovals;

import android.app.Application;

import com.onesignal.OneSignal;
import com.parse.Parse;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by eldadzipori on 3/7/16.
 */
public class aprovedApp extends Application {
     static final String TIME_FORMAT = "yyyy-MM-dd' 'h:mm a";
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(aprovedMessage.class);
        ParseObject.registerSubclass(aprovedCode.class);
        ParseObject.registerSubclass(aprovedUser.class);

        Parse.enableLocalDatastore(this);


        Parse.initialize(this, "kFnvKwYhNIx7hFO2vWolPWqAEU7WYnIOzrN1kqXD", "ma1Ufu0jL29HhiPGnsFgBvY9vl5SGeMv2loLShdU");

        OneSignal.startInit(this).init();


    }
}
