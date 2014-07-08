package no.appfabrikken.valpolicella.objects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by sondresallaup on 16.06.14.
 */

@ParseClassName("Question")
public class Question extends ParseObject{

    public Question(){

    }

    public String getGame(){
        return getString("game");
    }

    public String getQuestion(){
        return getString("question");
    }

    public String getAnswer(){
        return getString("answer");
    }

    public int getIndex(){
        return getInt("roundNumber");
    }

    public void setGame(String game){
        put("game", game);
    }

    public void setQuestion(String question){
        put("question", question);
    }

    public void setAnswer(String answer){
        put("answer", answer);
    }

    public void setIndex(int index){
        put("roundNumber", index);
    }
}
