package fatec.tg.SignsOn;

import android.app.Application;
import android.content.Context;

/**
 * Created by Mariana on 5/15/2016.
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}