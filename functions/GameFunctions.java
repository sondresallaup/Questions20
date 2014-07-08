package no.appfabrikken.valpolicella.functions;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import no.appfabrikken.valpolicella.objects.Game;
import no.appfabrikken.valpolicella.objects.User;

/**
 * Created by sondresallaup on 27.06.14.
 */
public class GameFunctions {
    String username;
    String currentUser;

    public GameFunctions(String username){
        this.username = username;
        User user = (User) ParseUser.getCurrentUser();
        this.currentUser = user.getUsername();
    }

    public Game getGame(){
        ParseQuery<Game> parseQuery1 = ParseQuery.getQuery(Game.class);
        parseQuery1.whereEqualTo("user1", currentUser);
        parseQuery1.whereEqualTo("user2", username);

        Log.d("Den lille app-fabrikken, getGame()", currentUser);
        try {
            Game game = parseQuery1.getFirst();
            if(game != null){
                return game;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<Game> parseQuery2 = ParseQuery.getQuery(Game.class);
        parseQuery2.whereEqualTo("user2", currentUser);
        parseQuery2.whereEqualTo("user1", username);
        try {
            Game game = parseQuery2.getFirst();
            if(game != null){
                return game;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
