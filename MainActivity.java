package no.appfabrikken.valpolicella;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import no.appfabrikken.valpolicella.functions.GuiFunctions;
import no.appfabrikken.valpolicella.lib.TypefaceSpan;
import no.appfabrikken.valpolicella.objects.User;

public class MainActivity extends ActionBarActivity {
    LinearLayout layout;
    GuiFunctions guiFunctions;
    ParseFunctions parseFunctions = new ParseFunctions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseFunctions.initializePushNotifications(this);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        User user = new User();
        user.getCurrentUser();

        if(!user.isLoggedIn()){
            startLoginActivity();
        }

        createLayout();

        testParseClasses();



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


        return true;
    }

    private void testParseClasses() {
        User user = (User)ParseUser.getCurrentUser();

        Log.d("Den lille app-fabrikken", user.getUsername());


        user.loadGamesFromParseYourTurn(guiFunctions);

        user.loadGamesFromParseWaitingTurn(guiFunctions);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startSettingsActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSettingsActivity(){
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void startLoginActivity(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void createLayout(){
        layout = (LinearLayout) findViewById(R.id.mainLayout);

        guiFunctions = new GuiFunctions(this, layout);

    }

    @Override
    public void onBackPressed() {
        //TODO: lukke appen
    }
}
