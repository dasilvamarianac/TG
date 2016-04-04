package example.naoki.SignOn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import java.util.prefs.Preferences;

import example.naoki.ble_myo.R;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    private PopupWindow pwindo;
    private View layout;



    public boolean initiatePopupWindow(final Context c, View v) {
        try {
// We need to get the instance of the LayoutInflater

            int w = v.getDisplay().getWidth();  // deprecated
            int h = v.getDisplay().getHeight();

            LayoutInflater inflater = (LayoutInflater) c
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_menu,
                    (ViewGroup) v.findViewById(R.id.popup));
            pwindo = new PopupWindow(layout, w, h, true);
            pwindo.setAnimationStyle(R.style.popwin_anim_style);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            Button Sobre = (Button) layout.findViewById(R.id.sobreBtn);
            Sobre.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c, Help.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    c.startActivity(intent);
                }
            });

            Button Conexao = (Button) layout.findViewById(R.id.conectarBtn);
            Conexao.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c,MyoListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    c.startActivity(intent);

                }
            });
            Button Sinais = (Button) layout.findViewById(R.id.sinaisBtn);
            Sinais.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c,SignalListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    c.startActivity(intent);

                }
            });



            Button close = (Button) layout.findViewById(R.id.voltarBtn);
            close.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pwindo.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return(true);
    }

}
