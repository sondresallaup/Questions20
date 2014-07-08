package no.appfabrikken.valpolicella;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import no.appfabrikken.valpolicella.functions.GameFunctions;
import no.appfabrikken.valpolicella.lib.TypefaceSpan;
import no.appfabrikken.valpolicella.objects.User;

public class LoginActivity extends ActionBarActivity {
    Button loginButton;
    EditText usernameInput, passwordInput;
    TextView loginMsg, userNameText, passwordText;
    Typeface funFont;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParseFunctions parseFunctions = new ParseFunctions();
        parseFunctions.registerSubclasses();
        parseFunctions.initializeFacebook();

        funFont = Typeface.createFromAsset(getAssets(), "fonts/jandamanateesolid.ttf");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        String title = getString(R.string.app_name);

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
        getMenuInflater().inflate(R.menu.main, menu);

        loginButton = (Button) findViewById(R.id.loginButton);
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        loginMsg = (TextView) findViewById(R.id.loginMsg);

        loginButton.setTypeface(funFont);
        loginMsg.setTypeface(funFont);

        userNameText = (TextView) findViewById(R.id.usernameText);
        userNameText.setTypeface(funFont);

        passwordText = (TextView) findViewById(R.id.passwordText);
        passwordText.setTypeface(funFont);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText(R.string.loading);
                loginButton.setClickable(false);
                if(isUsernameInput() && isPasswordInput()){
                    login();
                }
                else {
                    writeMessageToUser(R.string.fill_every_input_register);
                }
                loginButton.setText(R.string.login);
                loginButton.setClickable(true);
            }

        });

        ParseUser parseUser = ParseUser.getCurrentUser();
        if(parseUser != null){
            startMainActivity();
        }

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterActivity();
            }
        });
        registerButton.setTypeface(funFont);

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    private void startMainActivity(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void startRegisterActivity(){
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void login(){
        ParseUser.logInInBackground(usernameInput.getText().toString(), passwordInput.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser == null) {
                    writeMessageToUser(R.string.login_error);
                }
                else {
                    startMainActivity();
                }
            }
        });
    }

    private Boolean isUsernameInput(){
        return usernameInput.getText().length() != 0;
    }

    private Boolean isPasswordInput(){
        return passwordInput.getText().length() != 0;
    }

    private void writeMessageToUser(int message){
        loginMsg.setText(message);
    }

}
