package fatec.tg.SignsOn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

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

        }, 1500);

    }

    protected void Login (){
        SharedPreferences prefs = getSharedPreferences("signson", MODE_PRIVATE);
        String jaLogou = prefs.getString("logado", "x");
        String status = prefs.getString("status", "x");
        Log.i("Login", "[" + jaLogou + "]");
        Log.i("Status", "[" + status + "]");

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("myo", "NONMYO");
        editor.commit();

        if (jaLogou.equals("x")) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            Intent intent;
            intent = new Intent(getApplicationContext(), MyoListActivity.class);
            startActivity(intent);
        }
    }

}
