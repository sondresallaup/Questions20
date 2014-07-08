package no.appfabrikken.valpolicella;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SignUpCallback;

import no.appfabrikken.valpolicella.lib.TypefaceSpan;
import no.appfabrikken.valpolicella.objects.User;

public class RegisterActivity extends ActionBarActivity {
    Button registerButton;
    EditText usernameInput, passwordInput, repeatPasswordInput;
    TextView registerMsg, usernameTxt, passwordTxt, repeatPasswordTxt;
    Typeface funFont;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        funFont = Typeface.createFromAsset(getAssets(), "fonts/jandamanateesolid.ttf");

        String title = getString(R.string.title_activity_register);
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this, "jandamanateesolid.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);

        Resources res = getResources();
        int colorActionBar = res.getColor(R.color.actionbar_color);
        actionBar.setBackgroundDrawable(new ColorDrawable(colorActionBar));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);

        registerButton = (Button) findViewById(R.id.registerUserButton);
        registerButton.setTypeface(funFont);
        usernameInput = (EditText) findViewById(R.id.usernameRegisterInput);
        passwordInput = (EditText) findViewById(R.id.passwordRegisterInput);
        repeatPasswordInput = (EditText) findViewById(R.id.repeatPasswordInput);
        registerMsg = (TextView) findViewById(R.id.registerMsg);
        registerMsg.setTypeface(funFont);
        usernameTxt = (TextView) findViewById(R.id.usernameTxt);
        usernameTxt.setTypeface(funFont);
        passwordTxt = (TextView) findViewById(R.id.passwordTxt);
        passwordTxt.setTypeface(funFont);
        repeatPasswordTxt = (TextView) findViewById(R.id.repeatPasswordTxt);
        repeatPasswordTxt.setTypeface(funFont);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setText(R.string.loading);
                registerButton.setClickable(false);
                if(isUsernameInput() && isPasswordInput() && isRepeatPasswordInput()){
                    if(isPasswordAndRepeatPasswordTheSame(passwordInput.getText().toString(), repeatPasswordInput.getText().toString())) {
                        if(isPasswordGood(passwordInput.getText().toString())){
                            //User user = new User(usernameInput.getText().toString(), passwordInput.getText().toString());
                            User user = new User();
                            user.setUsername(usernameInput.getText().toString());
                            user.setPassword(passwordInput.getText().toString());
                            user.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        startMainActivity();
                                        //TODO: Velkomsthilsen
                                    }
                                    else{
                                        //TODO: feilmelding basert på Parses feilkode
                                        writeMessageToUser(R.string.error_register);
                                    }
                                }
                            });
                        }
                        else{
                            registerMsg.setText(R.string.password_to_short);
                        }
                    }
                    else{
                        writeMessageToUser(R.string.passwords_not_matching);
                    }
                }
                else{
                    writeMessageToUser(R.string.fill_every_input_register);
                }

                registerButton.setText(R.string.register);
                registerButton.setClickable(true);
            }
        });



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startMainActivity(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    private boolean isUsernameInput(){
        return usernameInput.getText().length() != 0;
    }

    private boolean isPasswordInput(){
        return passwordInput.getText().length() != 0;
    }

    private boolean isRepeatPasswordInput(){
        return repeatPasswordInput.getText().length() != 0;
    }

    private void writeMessageToUser(int message){
        registerMsg.setText(message);
    }

    private boolean isPasswordAndRepeatPasswordTheSame(String password, String repeatPassword){
        return password.equals(repeatPassword);
    }

    private boolean isPasswordGood(String password){
        int minPasswordLength = 6;
        if(password.length() >= minPasswordLength )
            return true;

        return false;
    }
}
