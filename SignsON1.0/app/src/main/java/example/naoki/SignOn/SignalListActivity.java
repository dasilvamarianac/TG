package example.naoki.SignOn;

        import android.app.Activity;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.os.StrictMode;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

        import example.naoki.ble_myo.R;

/**
 * Created by Mariana on 4/3/2016.
 */
public class SignalListActivity extends Activity implements AdapterView.OnItemClickListener {
    private MenuActivity menui;
    private ListView listView;
    private SignalAdapter adapterListView;
    private ArrayList<SignalListView> itens;
    private RequestQueue requestQueue;
    private static final String URL = "http://signson.orgfree.com/php/signalslist.php";
    private StringRequest request;
    @Override
    protected void onCreate(Bundle instance) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        requestQueue = Volley.newRequestQueue(this);


        super.onCreate(instance);
        setContentView(R.layout.activity_signal_list);

        Button menu = (Button) findViewById(R.id.menuBtn);
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menui = new MenuActivity();
                menui.initiatePopupWindow(SignalListActivity.this, v);
            }
        });

        listView = (ListView) findViewById(R.id.sinaisLvi);
        listView.setOnItemClickListener(this);

        createListView();

    }

    private void createListView()
    {
        SharedPreferences prefs = getSharedPreferences("signson", MODE_PRIVATE);
        final String user = prefs.getString("logado", "x");

        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    String id;
                    String image;
                    String status;
                    SignalListView item;
                    JSONArray array = new JSONArray(response);
                    itens = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject row = array.getJSONObject(i);

                        id = row.getString("idgesture");
                        image = row.getString("image");
                        status = row.getString("status");

                        if (status.equals("S")) {
                            item = new SignalListView(id, image, R.drawable.correto);
                        }else{
                            item = new SignalListView(id, image, R.drawable.not);
                        }

                        itens.add(item);
                    }

                    adapterListView = new SignalAdapter(getApplication(), itens);

                    listView.setAdapter(adapterListView);
                    listView.setCacheColorHint(Color.TRANSPARENT);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro de conexão " + error, Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", user);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        SignalListView item = adapterListView.getItem(arg2);
        Intent intent = new Intent(SignalListActivity.this,MyoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("sinal", item.getTexto());
        intent.putExtra("img", item.getIconeRid());
        intent.putExtra("type", "0");
        SignalListActivity.this.startActivity(intent);
    }
}