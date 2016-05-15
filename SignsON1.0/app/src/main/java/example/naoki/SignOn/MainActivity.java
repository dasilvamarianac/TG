package example.naoki.SignOn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import example.naoki.ble_myo.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {

               Login();

            }

        }, 3000);

    }

    @Override
    protected void onResume() {

        super.onResume();
        Login();
    }

    protected void Login (){
        SharedPreferences prefs = getSharedPreferences("signson", MODE_PRIVATE);
        String jaLogou = prefs.getString("logado", "x");
        Log.i("Login", "[" + jaLogou + "]");

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("myo", "NONMYO");
        editor.commit();

        if (jaLogou.equals("x")) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            Intent intent;
            intent = new Intent(getApplicationContext(), MyoActivity.class);
            intent.putExtra("type", "1");
            startActivity(intent);
        }
    }

}
