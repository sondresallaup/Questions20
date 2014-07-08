package no.appfabrikken.valpolicella;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import no.appfabrikken.valpolicella.functions.PushFunctions;
import no.appfabrikken.valpolicella.lib.TypefaceSpan;
import no.appfabrikken.valpolicella.objects.User;

public class SettingsActivity extends ActionBarActivity {
    PushFunctions pushFunctions = new PushFunctions();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        String title = getString(R.string.title_activity_settings);
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
        final User user = new User();
        user.getCurrentUser();

        final Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutButton.setText(R.string.loading);
                logoutButton.setClickable(false);
                pushFunctions.removePushNotifications(getContext());
                user.logOut();
                startLoginActivity();
                logoutButton.setText(R.string.log_out);
                logoutButton.setClickable(true);
            }
        });


        Resources res = getResources();
        int colorActionBar = res.getColor(R.color.actionbar_color);
        logoutButton.setBackgroundColor(colorActionBar);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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

    private void startLoginActivity(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private Activity getContext(){
        return this;
    }
}
