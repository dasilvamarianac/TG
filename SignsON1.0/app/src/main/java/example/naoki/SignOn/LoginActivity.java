package example.naoki.SignOn;

import android.app.Activity;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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


public class LoginActivity extends Activity {

    private EditText login;
    private EditText password;
    private TextView status;
    private RequestQueue requestQueue;
    private static final String URL = "http://signson.orgfree.com/php/login.php";
    private static final String URL2 = "http://signson.orgfree.com/php/email.php";
    private StringRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //layout
        setContentView(R.layout.activity_login);
        //layout items
        login = (EditText) findViewById(R.id.emailEdt);
        password = (EditText) findViewById(R.id.passEdt);

        final Button loginb = (Button) findViewById(R.id.createBtn);
        final Button create = (Button) findViewById(R.id.cancelBtn);
        final Button pass = (Button) findViewById(R.id.resenhaBtn);

        create.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("login", login.getText().toString());
                intent.putExtra("password", password.getText().toString());
                intent.putExtra("type", "0");
                LoginActivity.this.startActivity(intent);
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        loginb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        pass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptEmail();
            }
        });
    }

    private void attemptLogin() {
        // Store values at the time of the login attempt.
        String email = login.getText().toString();
        String passwords = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
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
        }


        if (cancel) {
            focusView.requestFocus();
        } else {

            request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.names().get(0).equals("success")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                            SharedPreferences prefs = getSharedPreferences("signson", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("logado", jsonObject.getString("id"));
                            editor.putString("status", jsonObject.getString("status"));
                            editor.commit();



                            startActivity(new Intent(getApplicationContext(), MyoListActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("email", login.getText().toString());
                    hashMap.put("password", password.getText().toString());

                    return hashMap;
                }
            };
            requestQueue.add(request);
        }

    }

    private void attemptEmail() {
        // Store values at the time of the login attempt.
        String email = login.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            login.setError(getString(R.string.error_field_required));
            focusView = login;
            cancel = true;
        } else if (!isEmailValid(email)) {
            login.setError(getString(R.string.error_invalid_email));
            focusView = login;
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
                        if (jsonObject.names().get(0).equals("success")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Erro" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
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
            }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("email", login.getText().toString());
                        return hashMap;
                    }
                };
                requestQueue.add(request);
            }
    }


    private boolean isEmailValid(String email) {

        return email.contains("@") && email.contains(".");
    }


}

