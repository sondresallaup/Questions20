package no.appfabrikken.valpolicella.functions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import no.appfabrikken.valpolicella.GameActivity;
import no.appfabrikken.valpolicella.R;
import no.appfabrikken.valpolicella.objects.Game;

/**
 * Created by sondresallaup on 26.06.14.
 */
public class GuiFunctions {
    private Activity context;
    private LinearLayout layout;
    private Typeface funFont;

    public GuiFunctions(Activity context, LinearLayout layout){
        this.context = context;
        this.layout = layout;
        funFont = Typeface.createFromAsset(context.getAssets(), "fonts/jandamanateesolid.ttf");
    }

    public void createYourTurnHeader(int textSize){
        TextView yourTurnText = new TextView(context);
        yourTurnText.setText(R.string.your_turn);
        yourTurnText.setTextSize(textSize);
        yourTurnText.setTypeface(funFont);
        layout.addView(yourTurnText);
    }

    public void createNewGameHeader(int textSize){
        TextView newGameText = new TextView(context);
        newGameText.setText(R.string.new_game);
        newGameText.setTextSize(textSize);
        newGameText.setTypeface(funFont);
        layout.addView(newGameText);

        final Button friendButton = new Button(context);
        friendButton.setText(R.string.new_game_friend);
        friendButton.setTextSize(20);
        friendButton.setTypeface(funFont);
        layout.addView(friendButton);

        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendButton.setText(R.string.loading);
                friendButton.setClickable(false);
                openNewGameDialog();
            }
        });

        final Button randomFriendButton = new Button(context);
        randomFriendButton.setText(R.string.new_game_random);
        randomFriendButton.setTextSize(20);
        randomFriendButton.setTypeface(funFont);
        layout.addView(randomFriendButton);

        randomFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomFriendButton.setText(R.string.loading);
                randomFriendButton.setBackgroundColor((R.color.actionbar_color));
                randomFriendButton.setClickable(false);
                Toast notAvailableToast = Toast.makeText(context, R.string.service_not_available, Toast.LENGTH_SHORT);
                notAvailableToast.show();
                randomFriendButton.setText(R.string.new_game_random);
                randomFriendButton.setClickable(true);
            }
        });
    }

    public void createWaitingGameHeader(int textSize){
        TextView waitingGameText = new TextView(context);
        waitingGameText.setText(R.string.waiting_game);
        waitingGameText.setTextSize(textSize);
        waitingGameText.setTypeface(funFont);
        layout.addView(waitingGameText);
    }

    public void createGameButton(final String txt){
        final Button gameButton = new Button(context);
        gameButton.setText(txt);
        gameButton.setTextSize(20);
        gameButton.setTypeface(funFont);
        layout.addView(gameButton);

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameButton.setText(R.string.loading);
                gameButton.setClickable(false);
                startGameActivity(txt);
                gameButton.setText(txt);
                gameButton.setClickable(true);
            }
        });

    }

    private void openNewGameDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(R.string.user_you_want_to_play);

        //input
        final EditText usernameInput = new EditText(context);
        usernameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setView(usernameInput);

        //buttons
        alertDialogBuilder.setPositiveButton(R.string.OK_string, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserFunctions userFunctions = new UserFunctions();
                if(userFunctions.isUserExisting(usernameInput.getText().toString())){
                    startGameActivity(usernameInput.getText().toString());
                }
                else{
                    Toast usernameNotExistingToast = Toast.makeText(context, R.string.username_not_existing, Toast.LENGTH_SHORT);
                    usernameNotExistingToast.show();
                }

            }
        });

        alertDialogBuilder.setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.show();
    }


    private void startGameActivity(String username){
        Intent gameIntent = new Intent(context, GameActivity.class);
        gameIntent.putExtra("username", username);
        context.startActivity(gameIntent);
    }
}
