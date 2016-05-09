package example.naoki.SignOn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import example.naoki.ble_myo.R;


public class UserActivity extends Activity {

    private String type;
    private String idUser;
    private EditText login;
    private EditText password;
    private EditText conf;
    private ImageView imge;
    private TextView status;
    private MenuActivity menui;
    private RequestQueue requestQueue;
    private static final String URL = "http://signson.orgfree.com/php/novo.php";
    private static final String URL2 = "http://signson.orgfree.com/php/edit.php";
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        SharedPreferences prefs = getSharedPreferences("signson", MODE_PRIVATE);
        idUser = prefs.getString("logado","");

        login = (EditText) findViewById(R.id.loginEdt);
        password = (EditText) findViewById(R.id.passEdt);
        conf = (EditText) findViewById(R.id.confEdt);
        imge = (ImageView) findViewById(R.id.emailImg);

        login.setText(getIntent().getStringExtra("login"));
        password.setText(getIntent().getStringExtra("password"));
        type = getIntent().getStringExtra("type");

        Button cancel = (Button) findViewById(R.id.cancelBtn);
        Button create = (Button) findViewById(R.id.createBtn);
        Button menu = (Button) findViewById(R.id.menuBtn);

        if (type.equals("0")){
            menu.setVisibility(View.INVISIBLE);
        }else{
            login.setVisibility(View.INVISIBLE);
            imge.setVisibility(View.INVISIBLE);
            password.setBackgroundResource(R.drawable.senha2);
        }


        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menui = new MenuActivity();
                menui.initiatePopupWindow(UserActivity.this, v);
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        create.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type.equals("0")){
                    attemptCreate();
                }else{
                    attemptEdit();
                }
            }
        });
    }
    private void attemptCreate() {
        String email = login.getText().toString();
        String passwords = password.getText().toString();
        String confp = conf.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            login.setError(getString(R.string.error_field_required));
            focusView = login;
            cancel = true;
        } else if (!isEmailValid(email)) {
            login.setError(getString(R.string.error_invalid_email));
            focusView = login;
            cancel = true;
        } else if(TextUtils.isEmpty(passwords)){
            password.setError(getString(R.string.error_field_required));
            focusView = password;
            cancel = true;
        } else if(!isPasswordValid(passwords)){
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        } else if(TextUtils.isEmpty(confp)) {
            conf.setError(getString(R.string.error_field_required));
            focusView = conf;
            cancel = true;
        } else if (! confp.equals(passwords)) {
            conf.setError(getString(R.string.error_incorrect_password));
            focusView = conf;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.names().get(0).equals("success")){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                        }else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        }

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
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> hashMap = new HashMap<String, String>();
                    hashMap.put("email",login.getText().toString());
                    hashMap.put("password",password.getText().toString());

                    return hashMap;
                }
            };
            requestQueue.add(request);
        }

    }

    private void attemptEdit() {
        String passwords = password.getText().toString();
        String confp = conf.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(passwords)){
            password.setError(getString(R.string.error_field_required));
            focusView = password;
            cancel = true;
        } else if(TextUtils.isEmpty(confp)) {
           conf.setError(getString(R.string.error_field_required));
           focusView = conf;
           cancel = true;
        } else if(!isPasswordValid(confp)){
            conf.setError(getString(R.string.error_invalid_password));
            focusView = conf;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            request = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.names().get(0).equals("success")){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MyoListActivity.class));
                        }else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        }
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
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> hashMap = new HashMap<String, String>();
                    hashMap.put("id",idUser);
                    hashMap.put("newpassword",conf.getText().toString());
                    hashMap.put("password",password.getText().toString());

                    return hashMap;
                }
            };
            requestQueue.add(request);
        }

    }

    private boolean isEmailValid(String email) {

        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {

        return password.length() >= 8;
    }
}

