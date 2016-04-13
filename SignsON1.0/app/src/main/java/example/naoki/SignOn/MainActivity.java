package example.naoki.SignOn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import example.naoki.ble_myo.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("signson", MODE_PRIVATE);
        String jaLogou = prefs.getString("logado","");

        if(jaLogou.equals("")) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }else {
            startActivity(new Intent(getApplicationContext(), MyoListActivity.class));
        }
    }

}
