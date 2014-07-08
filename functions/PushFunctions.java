package no.appfabrikken.valpolicella.functions;

import android.app.Activity;

import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;

import no.appfabrikken.valpolicella.MainActivity;
import no.appfabrikken.valpolicella.R;
import no.appfabrikken.valpolicella.objects.User;


/**
 * Created by sondresallaup on 30.06.14.
 */
public class PushFunctions {
    ParsePush push;

    public void sendPush(String toUser, String fromUser, Activity context){
        push = new ParsePush();
        push.setChannel(toUser);
        String pushMessage = context.getString(R.string.push_your_turn) + " " + fromUser + "!";
        push.setMessage(pushMessage);
        push.sendInBackground();
    }

    public void sendPushNewAnswer(String toUser, String fromUser, String answer , Activity context){
        String answerString = answer;
        if (answer.equals("true")){
            answerString = context.getString(R.string.yes);
        }
        else if(answer.equals("false")){
            answerString = context.getString(R.string.no);
        }
        else if(answer.equals("doNotKnow")){
            answerString = context.getString(R.string.do_not_know);
        }

        push = new ParsePush();
        push.setChannel(toUser);
        String pushMessage = fromUser + " " + context.getString(R.string.contestant_has_answered_question_beginning) + " " + answerString + " " + context.getString(R.string.contestant_has_answered_question_end) + "!";
        push.setMessage(pushMessage);
        push.sendInBackground();
    }

    public void removePushNotifications(Activity context){
        if(context != null){
            User currentUser = (User) ParseUser.getCurrentUser();
            PushService.setDefaultPushCallback(context, MainActivity.class);
            ParseAnalytics.trackAppOpened(context.getIntent());
            PushService.unsubscribe(context, currentUser.getUsername());
            ParseInstallation.getCurrentInstallation().saveEventually();
        }
    }
}
