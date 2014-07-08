package no.appfabrikken.valpolicella;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import no.appfabrikken.valpolicella.functions.GameFunctions;
import no.appfabrikken.valpolicella.functions.PushFunctions;
import no.appfabrikken.valpolicella.functions.UserFunctions;
import no.appfabrikken.valpolicella.lib.TypefaceSpan;
import no.appfabrikken.valpolicella.objects.Game;
import no.appfabrikken.valpolicella.objects.Question;
import no.appfabrikken.valpolicella.objects.User;
import no.appfabrikken.valpolicella.resources.Strings;

public class GameActivity extends ActionBarActivity {
    LinearLayout layout;
    String username;
    Game game;
    User currentUser = (User) ParseUser.getCurrentUser();
    ArrayList<Question> questions;
    PushFunctions pushFunctions = new PushFunctions();
    Typeface funFont;
    ActionBar actionBar;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        funFont = Typeface.createFromAsset(getAssets(), "fonts/jandamanateesolid.ttf");

        layout = (LinearLayout) findViewById(R.id.gameLayout);
        username = getUsername();


        String youTurnString = getString(R.string.push_your_turn);
        username = getUsername();
        String titleString = youTurnString + " " + username;
        setTitleWithFont(titleString);

        GameFunctions gameFunctions = new GameFunctions(username);
        game = gameFunctions.getGame();

        Resources res = getResources();
        int colorActionBar = res.getColor(R.color.actionbar_color);
        actionBar.setBackgroundDrawable(new ColorDrawable(colorActionBar));


        startGame();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            refreshActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getUsername(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            return extras.getString("username");
        }
        return "";
    }

    private void startGame(){
        if(game == null) {
            createNewGame();
        }

        if(game.isWon()){
            String youTurnString = getString(R.string.your_finished_game_with);
            username = getUsername();
            String titleString = youTurnString + " " + username;
            setTitleWithFont(titleString);

            TextView gameWonText = new TextView(this);
            gameWonText.setTypeface(funFont);
            gameWonText.setTextSize(25);
            if(!game.isUser1(currentUser.getUsername())){
                String resourcesString = getString(R.string.congratulations_you_won);
                gameWonText.setText(resourcesString + " " + username);
            }
            else{
                String resourcesString = getString(R.string.contestant_won);
                gameWonText.setText(username + " " + resourcesString);
            }
            layout.addView(gameWonText);

            printWordDescription();
            printGameWord(game.getWord());
        }

        else {

            if (!game.isUser1(currentUser.getUsername())) {
                printWordDescription();
            }

            if (!isCurrentUsersTurn()) {
                if (game.isUser1(currentUser.getUsername())) {
                    printGameWord(game.getWord());
                }

                printWaitingForPlayer();
            } else {
                if (game.getCurrentRound() == 0 && game.getUser1().equals(currentUser.getUsername()) && game.getLastQuestion() == null) {
                    createNewWordInput();
                } else if (game.getUser1().equals(username)) {
                    createNewQuestionInput();
                } else {
                    createAnswerButtons();
                }
            }
        }

        TextView newLine = new TextView(this);
        newLine.setLines(3);
        layout.addView(newLine);

        game.printQuestionsFromParse(this, layout);


    }

    private void createNewWordInput(){
        final EditText newWordInput = new EditText(this);
        newWordInput.setInputType(InputType.TYPE_CLASS_TEXT);
        newWordInput.setText(R.string.your_word);
        layout.addView(newWordInput);

        final Spinner concretenessSpinner = new Spinner(this);
        final ArrayAdapter<CharSequence> concretenessArray = ArrayAdapter.createFromResource(this, R.array.concreteness, R.layout.support_simple_spinner_dropdown_item);
        concretenessSpinner.setAdapter(concretenessArray);
        layout.addView(concretenessSpinner);


        Button newWordSubmit = new Button(this);
        newWordSubmit.setText(R.string.send);
        newWordSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewWord(newWordInput.getText().toString(), concretenessSpinner.getSelectedItemPosition());
            }
        });
        layout.addView(newWordSubmit);
    }

    private void createNewQuestionInput(){
        final EditText newQuestionInput = new EditText(this);
        newQuestionInput.setInputType(InputType.TYPE_CLASS_TEXT);
        newQuestionInput.setText(R.string.your_question);
        layout.addView(newQuestionInput);

        Button newQuestionButton = new Button(this);
        newQuestionButton.setText(R.string.send);
        newQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewQuestion(newQuestionInput.getText().toString());
            }
        });
        layout.addView(newQuestionButton);

        final Button iKnowTheAnswerButton = new Button(this);
        iKnowTheAnswerButton.setText(R.string.i_know_the_answer);
        iKnowTheAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIKnowTheAnswerDialog();
            }
        });
        layout.addView(iKnowTheAnswerButton);
    }

    private void createAnswerButtons(){
        printLastQuestion();

        Button yesButton = new Button(this);
        yesButton.setText(R.string.yes);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerQuestion(Strings.trueString);
            }
        });
        layout.addView(yesButton);
        Button noButton = new Button(this);
        noButton.setText(R.string.no);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerQuestion(Strings.falseString);
            }
        });
        layout.addView(noButton);
        Button doNotKnowButton  = new Button(this);
        doNotKnowButton.setText(R.string.do_not_know);
        doNotKnowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerQuestion(Strings.doNotKnowString);
            }
        });
        layout.addView(doNotKnowButton);

        Button moreButton = new Button(this);
        moreButton.setText(R.string.more);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAnswerMoreDialog();
            }
        });
        layout.addView(moreButton);
    }

    private void createNewGame(){
        game = new Game();
        game.setUser1(currentUser.getUsername());
        game.setUser2(username);
        game.setIsUser1sTurn(true);
        game.setCurrentRound(0);
        game.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    e.printStackTrace();
                }
            }
        });
    }

    private void submitNewWord(String word, int concreteness){
        game.setWord(word);
        game.setConcreteness(concreteness);
        game.setIsUser1sTurn(false);
        game.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    //TODO: informere om suksess
                    sendPushNotification();
                    refreshActivity();
                }
                else{
                    //TODO: skrive feilmelding
                    e.printStackTrace();
                }
            }
        });
    }

    private void submitNewQuestion(String questionString){
        Question question = new Question();
        Question lastQuestion = game.getLastQuestionWithAnswer();
        boolean doThisRoundCount = true;
        if(lastQuestion != null){
            doThisRoundCount = !lastQuestion.getAnswer().equals(Strings.doNotKnowString);
        }
        final boolean doThisRoundCountFinal = doThisRoundCount;
        question.setGame(game.getObjectId());
        question.setQuestion(questionString);
        question.setIndex(game.getCurrentRound());
        question.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    //TODO: suksess
                    game.setIsUser1sTurn(true);
                    Log.d("Den lille app-fabrikken", String.valueOf(doThisRoundCountFinal));
                    if(doThisRoundCountFinal) {
                        Log.d("Den lille app-fabrikken", String.valueOf(game.getCurrentRound() + 1));
                        game.setCurrentRound(game.getCurrentRound() + 1);
                    }
                    game.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                sendPushNotification();
                                refreshActivity();
                            }
                            else{
                                //TODO: skrive feilmelding
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
                    //TODO: skrive feilmelding
                    e.printStackTrace();
                }
            }
        });
    }

    private void printWaitingForPlayer(){
        String waitingForString = getString(R.string.waiting_for);
        username = getUsername();
        String titleString = waitingForString + " " + username;
        setTitleWithFont(titleString);
    }

    private void printGameWord(String word){
        TextView gameWordText = new TextView(this);
        String resourcesString = getString(R.string.your_word_is);
        gameWordText.setText(resourcesString + " '" + word + "'");
        gameWordText.setTextSize(25);
        gameWordText.setTypeface(funFont);
        layout.addView(gameWordText);
    }

    private void printWordDescription(){
        TextView wordDescriptionText = new TextView(this);
        wordDescriptionText.setText(game.getConcreteness());
        wordDescriptionText.setTextSize(25);
        wordDescriptionText.setTypeface(funFont);
        layout.addView(wordDescriptionText);
    }

    private void printLastQuestion(){
        TextView firstQuestionText = new TextView(this);
        firstQuestionText.setText(game.getLastQuestion().getQuestion());
        firstQuestionText.setTextSize(25);
        firstQuestionText.setTypeface(funFont);
        layout.addView(firstQuestionText);
    }

    private void answerQuestion(final String answer){
        Question question = game.getLastQuestion();
        question.setAnswer(answer);
        question.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    //TODO: vellykket
                    game.setIsUser1sTurn(false);
                    game.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                sendPushNotificationNewAnswer(answer);
                                refreshActivity();
                            }
                            else{
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else {
                    //TODO: skrive feilmelding
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendPushNotification(){
        pushFunctions.sendPush(username, currentUser.getUsername(), this);
    }

    private void sendPushNotificationNewAnswer(String answer){
        pushFunctions.sendPushNewAnswer(username, currentUser.getUsername(), answer, this);
    }

    private void refreshActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private boolean isCurrentUsersTurn(){
        return game.isUser1sTurn() && game.isUser1(currentUser.getUsername()) || !game.isUser1sTurn() && !game.isUser1(currentUser.getUsername());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setTitleWithFont(String title){
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this, "jandamanateesolid.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        actionBar = getActionBar();
        actionBar.setTitle(s);
    }

    private void openAnswerMoreDialog(){
        AlertDialog.Builder answerMoreDialogBuilder = new AlertDialog.Builder(this);
        answerMoreDialogBuilder.setTitle(R.string.write_custom_answer);

        final EditText customAnswerInput = new EditText(this);

        customAnswerInput.setInputType(InputType.TYPE_CLASS_TEXT);
        answerMoreDialogBuilder.setView(customAnswerInput);

        //buttons
        answerMoreDialogBuilder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                answerQuestion(customAnswerInput.getText().toString());

            }
        });

        answerMoreDialogBuilder.setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        answerMoreDialogBuilder.show();
    }

    private void setInputBoxText(String text, EditText inputBox){
        inputBox.setText(text);
    }

    private void openIKnowTheAnswerDialog(){
        AlertDialog.Builder iKnowTheAnswerDialogBuilder = new AlertDialog.Builder(this);
        iKnowTheAnswerDialogBuilder.setTitle(R.string.i_know_the_answer);

        final EditText answerInput = new EditText(this);

        answerInput.setInputType(InputType.TYPE_CLASS_TEXT);
        iKnowTheAnswerDialogBuilder.setView(answerInput);

        //buttons
        iKnowTheAnswerDialogBuilder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isCorrectWord(answerInput.getText().toString())){
                    Log.d("Den lille app-fabrikken", "yeah!");
                    game.setIsWon(true);
                    game.saveEventually();
                }
                else{
                    //TODO: informere om at er feil ord
                }

            }
        });

        iKnowTheAnswerDialogBuilder.setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        iKnowTheAnswerDialogBuilder.show();
    }

    private Boolean isCorrectWord(String inputWord){
        return game.getWord().equals(inputWord);
    }
}
