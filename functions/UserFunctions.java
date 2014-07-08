package no.appfabrikken.valpolicella.functions;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import no.appfabrikken.valpolicella.objects.User;

/**
 * Created by sondresallaup on 27.06.14.
 */
public class UserFunctions {
    ParseQuery<User> parseQuery = ParseQuery.getQuery(User.class);

    public boolean isUserExisting(String username){

        parseQuery.whereEqualTo("username", username);
        try {
            User user = parseQuery.getFirst();
            if(user != null) {
                return true;
            }

        } catch (ParseException e) {

        }

        return false;
    }



}
