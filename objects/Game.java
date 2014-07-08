package no.appfabrikken.valpolicella.objects;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.appfabrikken.valpolicella.R;
import no.appfabrikken.valpolicella.resources.Strings;

/**
 * Created by sondresallaup on 16.06.14.
 */

@ParseClassName("Game")
public class Game extends ParseObject{
    private ArrayList<Question> questions = new ArrayList<Question>();

    public Game(){
    }

    public void printQuestionsFromParse(final Activity context, final LinearLayout layout){
        ParseQuery<Question> query = ParseQuery.getQuery(Question.class);
        query.whereEqualTo("game", this.getObjectId());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Question>() {
            @Override
            public void done(List<Question> parseQuestions, ParseException e) {
                if(e == null){
                    for(Question question : parseQuestions){
                        printQuestionLog(context, layout, question);
                    }
                }
                else{
                    Log.d("Den lille app-fabrikken", "ædijfæslkjfølkldkahfkajfh");
                }
            }
        });
    }

    public Question getLastQuestion(){
        ParseQuery<Question> query = ParseQuery.getQuery(Question.class);
        query.whereEqualTo("game", this.getObjectId());
        query.addDescendingOrder("createdAt");
        try {
            return query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Question getLastQuestionWithAnswer(){
        ParseQuery<Question> query = ParseQuery.getQuery(Question.class);
        query.whereEqualTo("game", this.getObjectId());
        query.whereNotEqualTo("answer", null);
        query.addDescendingOrder("createdAt");
        try {
            return query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setWord(String word){
        put("word", word);
    }

    public void setUser1(String user1) {
        put("user1", user1);
    }

    public void setUser2(String user2) {
        put("user2", user2);
    }

    public void setIsUser1sTurn(Boolean isUser1sTurn){
        put("isUser1sTurn", isUser1sTurn);
    }

    public void setCurrentRound(int currentRound){
        put("currentRound", currentRound);
    }

    public void setConcreteness(int concreteness){
        put("concreteness", concreteness);
    }

    public void setNumberWords(int numberWords){
        put("numberWords", numberWords);
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

    public String getWord(){
        return getString("word");
    }

    public String getUser1(){
        return getString("user1");
    }

    public String getUser2(){
        return getString("user2");
    }

    public int getConcreteness(){
        int concretenessStrings[] = new int[3];
        concretenessStrings[0] = R.string.concrete_string;
        concretenessStrings[1] = R.string.abstract_string;
        concretenessStrings[2] = R.string.both_concrete_abstract;
        return concretenessStrings[getInt("concreteness")];

    }

    public int getNumberWords(){
        return getInt("numberWords");
    }

    public int getCurrentRound(){
        return getInt("currentRound");
    }

    public ArrayList<Question> getQuestions(){
        return questions;
    }

    public Boolean isUser1sTurn(){
        return getBoolean("isUser1sTurn");
    }

    public Boolean isUser1(String user){
        return getString("user1").equals(user);
    }

    public Boolean isWon(){
        return getBoolean("isWon") == true;
    }

    public void setIsWon(Boolean isWon){
        put("isWon", isWon);
    }

    private void printQuestionLog(Activity context, LinearLayout layout, Question question){
        Resources resources = context.getResources();
        Typeface funFont = Typeface.createFromAsset(context.getAssets(), "fonts/jandamanateesolid.ttf");
        TextView wordDescriptionText = new TextView(context);
        wordDescriptionText.setTextSize(25);
        wordDescriptionText.setTypeface(funFont);
        String answer  = "";
        if(question.getAnswer() != null) {
            if (question.getAnswer().equals(Strings.trueString)) {
                wordDescriptionText.setTextColor(resources.getColor(R.color.green_yes_color));
                answer = context.getString(R.string.yes);
            } else if (question.getAnswer().equals(Strings.falseString)) {
                wordDescriptionText.setTextColor(resources.getColor(R.color.red_no_color));
                answer = context.getString(R.string.no);
            } else if (question.getAnswer().equals(Strings.doNotKnowString)) {
                answer = context.getString(R.string.do_not_know);
                wordDescriptionText.setTextColor(resources.getColor(R.color.yellow_do_not_know_color));
            }
            else{
                wordDescriptionText.setTextColor(resources.getColor(R.color.custom_answer_color));
                answer = question.getAnswer();
            }
        }
            wordDescriptionText.setText(question.getIndex() + 1 + ": " + question.getQuestion() + ":  " + answer);
            layout.addView(wordDescriptionText);
    }
}
