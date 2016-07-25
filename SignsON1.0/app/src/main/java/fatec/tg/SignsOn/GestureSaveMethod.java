package fatec.tg.SignsOn;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
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

public class GestureSaveMethod {
    private final static String TAG = "SignsOn";
    private final static String FileName = "userdata.dat";

    private final static int SAVE_DATA_LENGTH = 3;
    private final static int AVERAGING_LENGTH = 10;

    private ArrayList<EmgCharacteristicData> rawDataList = new ArrayList<>();
    private ArrayList<EmgData> maxDataList = new ArrayList<>();
    private ArrayList<EmgData> compareGesture = new ArrayList<>();

    private SaveState saveState = SaveState.Ready;

    private int dataCounter = 0;
    private int gestureCounter = 0;

    private RequestQueue requestQueue;
    private static final String URLL = "http://signsonapp.com/php/listug.php";
    private static final String URL =  "http://signsonapp.com/php/novoS.php";
    private static final String URLC = "http://signsonapp.com/php/completed.php";
    private StringRequest request;
    private String signal;

    public void Charge() {
        compareGesture = new ArrayList<>();
        MyoDataFileReader dataFileReader = new MyoDataFileReader(TAG,FileName);
        compareGesture = dataFileReader.load();
        saveState = SaveState.Have_Saved;
    }

    public GestureSaveMethod() {
        MyoDataFileReader dataFileReader = new MyoDataFileReader(TAG,FileName);
        if (dataFileReader.load().size() == 150) {
            compareGesture = dataFileReader.load();
            saveState = SaveState.Have_Saved;
        }
    }

    public enum SaveState {
        Ready,
        Not_Saved,
        Now_Saving,
        Have_Saved,
    }

    public void addData(byte[] data) {
        rawDataList.add(new EmgCharacteristicData(data));
        dataCounter++;
        if (dataCounter % SAVE_DATA_LENGTH == 0) {
            EmgData dataMax = new EmgData();
            int count = 0;
            for (EmgCharacteristicData emg16Temp : rawDataList) {
                EmgData emg8Temp = emg16Temp.getEmg8Data_abs();
                if (count == 0) {
                    dataMax = emg8Temp;
                } else {
                    for (int i_element = 0; i_element < 8; i_element++) {
                        if (emg8Temp.getElement(i_element) > dataMax.getElement(i_element)) {
                            dataMax.setElement(i_element, emg8Temp.getElement(i_element));
                        }
                    }
                }
                count++;
            }
            if (rawDataList.size() < SAVE_DATA_LENGTH) {
                Log.e("GestureDetect", "Small rawData : " + rawDataList.size());
            }
            maxDataList.add(dataMax);
            rawDataList = new ArrayList<>();
        }
        if (dataCounter == SAVE_DATA_LENGTH * AVERAGING_LENGTH) {
            saveState = SaveState.Not_Saved;
            makeCompareData();
            Completed();
            dataCounter = 0;
        }
    }



    private void makeCompareData() {
        EmgData tempData  = new EmgData();

        // Get each Max EMG-elements of maxDataList
        int count = 0;
        for (EmgData emg8Temp : maxDataList) {
            if (count == 0) {
                tempData = emg8Temp;
            } else {
                for (int i_element = 0; i_element < 8; i_element++) {
                    if (emg8Temp.getElement(i_element) > tempData.getElement(i_element)) {
                        tempData.setElement(i_element, emg8Temp.getElement(i_element));
                    }
                }
            }
            count++;
        }

        if (maxDataList.size() < AVERAGING_LENGTH) {
            Log.e("GestureDetect", "Small aveData : " + maxDataList.size());
        }
        InsertData(tempData, signal);
        maxDataList = new ArrayList<>();
    }



    public void Listug(){
        SharedPreferences prefs = MyoActivity.getContext().getSharedPreferences("signson", 0);
        final String user = prefs.getString("logado", "x");
        requestQueue = Volley.newRequestQueue(MyoActivity.getContext());
        compareGesture = new ArrayList<>();
        request = new StringRequest(Request.Method.POST, URLL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String line;
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        line = row.getString("line");
                        EmgData tempData  = new EmgData();
                        tempData.setLine(line);
                        compareGesture.add(tempData);
                        compareGesture.get(i).setLine(line);
                        Log.i("LINHA", compareGesture.get(i).getLine());
                    }
                    for (int i = 0; i < array.length(); i++) {
                        Log.i("LINHA", compareGesture.get(i).getLine());
                    }
                    MyoDataFileReader dataFileReader = new MyoDataFileReader(TAG, FileName);
                    dataFileReader.saveMAX(getCompareDataList());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyoActivity.getContext(), "Erro de conexão " + error, Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user", user);
                return hashMap;
            }
        };
        requestQueue.add(request);

    }

    public void InsertData(EmgData temp, final String sinal) {
        final EmgData tempData = temp;
        SharedPreferences prefs = MyoActivity.getContext().getSharedPreferences("signson", 0);
        final String user = prefs.getString("logado", "");

        requestQueue = Volley.newRequestQueue(MyoActivity.getContext());
        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success")){

                    }else {
                        Toast.makeText(MyoActivity.getContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyoActivity.getContext(), "Erro de conexão " + error, Toast.LENGTH_SHORT).show();
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put("user",user);
                hashMap.put("gesture",sinal);
                hashMap.put("sensor1",tempData.getElement(0).toString()+"00000");
                hashMap.put("sensor2",tempData.getElement(1).toString()+"00000");
                hashMap.put("sensor3",tempData.getElement(2).toString()+"00000");
                hashMap.put("sensor4",tempData.getElement(3).toString()+"00000");
                hashMap.put("sensor5",tempData.getElement(4).toString()+"00000");
                hashMap.put("sensor6",tempData.getElement(5).toString()+"00000");
                hashMap.put("sensor7",tempData.getElement(6).toString()+"00000");
                hashMap.put("sensor8",tempData.getElement(7).toString()+"00000");

                return hashMap;
            }
        };
        requestQueue.add(request);

    }
    public void Completed(){
        SharedPreferences prefs = MyoActivity.getContext().getSharedPreferences("signson", 0);
        final String user = prefs.getString("logado", "x");


        requestQueue = Volley.newRequestQueue(MyoActivity.getContext());
        request = new StringRequest(Request.Method.POST, URLC, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("completed")){

                        SharedPreferences prefs = MyoActivity.getContext().getSharedPreferences("signson",0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("status", "1");
                        editor.commit();

                        saveState = SaveState.Have_Saved;
                        Listug();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyoActivity.getContext(), "Erro de conexão " + error, Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user", user);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }
    public SaveState getSaveState() {
        return saveState;
    }

    public void setState(SaveState state) {
        saveState = state;
    }

    public int getGestureCounter() {
        return gestureCounter;
    }

    public ArrayList<EmgData> getCompareDataList() {
        return compareGesture;
    }

    public void setSignal(String s) {
        signal = s ;
    }



}