package example.naoki.SignOn;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

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
                    Intent intent = new Intent(c, HelpActivity.class);
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
            Button Senha = (Button) layout.findViewById(R.id.asenhaBtn);
            Senha.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c, UserActivity.class);
                    intent.putExtra("type", "1");
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    c.startActivity(intent);

                }
            });

            Button Logout = (Button) layout.findViewById(R.id.logoutBtn);
            Logout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                  SharedPreferences prefs = getSharedPreferences("signson", 0);
                  prefs.edit().clear().commit();


                  Intent mStartActivity = new Intent(c, MainActivity.class);
                  int mPendingIntentId = 123456;
                  PendingIntent mPendingIntent = PendingIntent.getActivity(c, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                  AlarmManager mgr = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
                  mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                  System.exit(0);


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
