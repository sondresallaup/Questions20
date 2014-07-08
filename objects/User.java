package no.appfabrikken.valpolicella.objects;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import no.appfabrikken.valpolicella.R;
import no.appfabrikken.valpolicella.functions.GuiFunctions;

/**
 * Created by sondresallaup on 14.06.14.
 */
@ParseClassName("_User")
public class User extends ParseUser {
    ArrayList<Game> gamesYourTurn = new ArrayList<Game>();
    ArrayList<Game> gamesWaitingTurn = new ArrayList<Game>();

    public User(){
    }

    public void login(String username, String password){
        this.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // TODO: logget inn
                } else {
                    // TODO: feil med innlogging
                }
            }
        });
    }

    public void facebookLogin(Activity context){
        ParseFacebookUtils.logIn(context, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    System.out.print("Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    System.out.print("User signed up and logged in through Facebook!");
                } else {
                    System.out.print("User logged in through Facebook!");
                }
            }
        });
    }

    public void loadGamesFromParseYourTurn(final GuiFunctions guiFunctions){
        ParseQuery<Game> queryUser1 = ParseQuery.getQuery(Game.class);
        queryUser1.whereEqualTo("user1", this.getUsername());
        queryUser1.whereEqualTo("isUser1sTurn", true);
        queryUser1.findInBackground(new FindCallback<Game>() {
            @Override
            public void done(List<Game> parseGames, ParseException e) {
                if(e == null){
                    guiFunctions.createYourTurnHeader(25);
                    for(Game game : parseGames){
                        Log.d("Den lille app-fabrikken", game.getUser2());
                        guiFunctions.createGameButton(game.getUser2());
                        gamesYourTurn.add(game);
                    }
                }
                else {
                    Log.d("Den lille app-fabrikken", e.getMessage());
                }
            }
        });

        Log.d("Den lille app-fabrikken loadGamesFromParseYourTurn", String.valueOf(gamesYourTurn.size()));

        ParseQuery<Game> queryUser2 = ParseQuery.getQuery(Game.class);
        queryUser2.whereEqualTo("user2", this.getUsername());
        queryUser2.whereEqualTo("isUser1sTurn", false);
        queryUser2.findInBackground(new FindCallback<Game>() {
            @Override
            public void done(List<Game> parseGames, ParseException e) {
                if(e == null){
                    for(Game game : parseGames){
                        Log.d("Den lille app-fabrikken", game.getUser1());
                        guiFunctions.createGameButton(game.getUser1());
                        gamesYourTurn.add(game);
                    }
                }
                else{
                    Log.d("Den lille app-fabrikken", e.getMessage());
                }
            }
        });

        guiFunctions.createNewGameHeader(25);
    }

    public void loadGamesFromParseWaitingTurn(final GuiFunctions guiFunctions){
        ParseQuery<Game> queryUser1 = ParseQuery.getQuery(Game.class);
        queryUser1.whereEqualTo("user1", this.getUsername());
        queryUser1.whereEqualTo("isUser1sTurn", false);
        queryUser1.findInBackground(new FindCallback<Game>() {
            @Override
            public void done(List<Game> parseGames, ParseException e) {
                if(e == null){
                    guiFunctions.createWaitingGameHeader(25);
                    for(Game game : parseGames){
                        guiFunctions.createGameButton(game.getUser2());
                        gamesWaitingTurn.add(game);
                    }
                }
                else {
                    Log.d("Den lille app-fabrikken", e.getMessage());
                }
            }
        });

        ParseQuery<Game> queryUser2 = ParseQuery.getQuery(Game.class);
        queryUser2.whereEqualTo("user2", this.getUsername());
        queryUser2.whereEqualTo("isUser1sTurn", true);
        queryUser2.findInBackground(new FindCallback<Game>() {
            @Override
            public void done(List<Game> parseGames, ParseException e) {
                if(e == null){
                    for(Game game : parseGames){
                        guiFunctions.createGameButton(game.getUser1());
                        gamesWaitingTurn.add(game);
                    }
                }
                else{
                    Log.d("Den lille app-fabrikken", e.getMessage());
                }
            }
        });
    }

    public boolean isLoggedIn(){
        this.getCurrentUser();
        return this != null;
    }

    public String getObjectId(){
        return getString("objectId");
    }

    public String getUsername(){
        return getString("username");
    }

    public String getEmail(){
        return getString("email");
    }

    public ArrayList<Game> getGamesYourTurn(){
        return gamesYourTurn;
    }

    public ArrayList<Game> getGamesWaitingTurn() {return gamesWaitingTurn;}
}
