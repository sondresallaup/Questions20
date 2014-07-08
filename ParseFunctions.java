package no.appfabrikken.valpolicella;

import android.app.Activity;
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

import no.appfabrikken.valpolicella.objects.Game;
import no.appfabrikken.valpolicella.objects.Question;
import no.appfabrikken.valpolicella.objects.User;

/**
 * Created by sondresallaup on 14.06.14.
 */
public class ParseFunctions {
    private String parseApplicationId = "0vQV4BK631nO8eKgAdaXuOYy3PbZh38u1nuEv7Fe";
    private String parseClientKey = "i5aVWcbsqouG1J1WnbO2KsDdGxQDF2Q2jYqoXh6R";
    private String facebookAppId = "232837086927657";

    public void initializeParse(Application context){
        if(context != null)
            Parse.initialize(context, parseApplicationId, parseClientKey);
    }

    public void initializeFacebook(){
        ParseFacebookUtils.initialize(facebookAppId);
    }

    public void registerSubclasses(){
        ParseUser.registerSubclass(User.class);
        ParseObject.registerSubclass(Question.class);
        ParseObject.registerSubclass(Game.class);
    }

    public void initializePushNotifications(Activity context){
        if(context != null) {
            User currentUser = (User) ParseUser.getCurrentUser();
            PushService.setDefaultPushCallback(context, MainActivity.class);
            ParseAnalytics.trackAppOpened(context.getIntent());
            PushService.subscribe(context, "everybody", MainActivity.class);
            PushService.subscribe(context, currentUser.getUsername(), MainActivity.class);
            ParseInstallation.getCurrentInstallation().saveEventually();
        }
    }

    public String getParseApplicationId(){
        return parseApplicationId;
    }

    public String getParseClientKey(){
        return parseClientKey;
    }

    public String getFacebookAppId(){
        return facebookAppId;
    }
}
