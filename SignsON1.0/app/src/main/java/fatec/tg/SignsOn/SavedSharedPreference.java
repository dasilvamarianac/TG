package fatec.tg.SignsOn;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Mariana on 4/12/2016.
 */
public class SavedSharedPreference {

    static final String PREF_USER_ID = "idUser";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setidUser(Context ctx, String idUser)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, idUser);
        editor.commit();
    }

    public static String getidUser(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }
}