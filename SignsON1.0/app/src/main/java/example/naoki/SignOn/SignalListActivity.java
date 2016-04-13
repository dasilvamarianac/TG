package example.naoki.SignOn;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import example.naoki.ble_myo.R;

/**
 * Created by Mariana on 4/3/2016.
 */
public class SignalListActivity extends Activity {
    private MenuActivity menui;
    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.activity_signal_list);

        ListView lista = (ListView) findViewById(R.id.sinaisLvi);

        String[] n = {"Felipe", "Joao", "Zé"};
        List<String> nomes = Arrays.asList(n);

        SinalAdapter adapter = new SinalAdapter(this, nomes);
        lista.setAdapter(adapter);


        Button menu = (Button) findViewById(R.id.menuBtn);
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menui = new MenuActivity();
                menui.initiatePopupWindow(SignalListActivity.this, v);
            }
        });

    }
}
