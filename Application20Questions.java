package no.appfabrikken.valpolicella;

import android.app.Application;
import android.content.Context;

/**
 * Created by sondresallaup on 03.07.14.
 */
public class Application20Questions extends Application {
    private static Application20Questions instance = new Application20Questions();

    public Application20Questions() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
    @Override
    public void onCreate(){
        ParseFunctions parseFunctions = new ParseFunctions();
        parseFunctions.registerSubclasses();
        parseFunctions.initializeParse(this);
        super.onCreate();
    }
}
