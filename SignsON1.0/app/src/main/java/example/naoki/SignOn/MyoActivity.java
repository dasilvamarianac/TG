package example.naoki.SignOn;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import android.widget.Button;
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
import com.echo.holographlibrary.LineGraph;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import example.naoki.ble_myo.R;

public class MyoActivity extends ActionBarActivity implements BluetoothAdapter.LeScanCallback {
    public static final int MENU_LIST = 0;
    public static final int MENU_BYE = 1;
    private MenuActivity menui;

    /** Device Scanning Time (ms) */
    private static final long SCAN_PERIOD = 5000;

    /** Intent code for requesting Bluetooth enable */
    private static final int REQUEST_ENABLE_BT = 1;

    private static final String TAG = "BLE_Myo";

    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt    mBluetoothGatt;
    private TextView         emgDataText;
    private TextView         gestureText;

    private MyoGattCallback mMyoCallback;
    private MyoCommandList commandList = new MyoCommandList();

    private String deviceName;
    private String type ;
    private String img;
    public String sinal;

    private GestureSaveModel saveModel;
    private GestureSaveMethod saveMethod;
    private GestureDetectModel detectModel;
    private GestureDetectMethod detectMethod;

    private LineGraph graph;
    private TextView tTranslate;
    private ImageView iSignal;
    private Button graphButton1;
    private Button graphButton2;
    private Button graphButton3;
    private Button graphButton4;
    private Button graphButton5;
    private Button graphButton6;
    private Button graphButton7;
    private Button graphButton8;
    private Button bStopEMG;
    private Button bEMG;
    private Button bDetect;
    private Button bSave;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);
        mContext = getApplicationContext();

        //ready
        graph = (LineGraph) findViewById(R.id.holo_graph_view);
        bStopEMG = (Button) findViewById(R.id.bStopEmg);
        bEMG = (Button) findViewById(R.id.bEMG);
        bDetect = (Button) findViewById(R.id.bDetect);
        bSave = (Button) findViewById(R.id.bSave);
        tTranslate = (TextView) findViewById(R.id.tTranslate);

        graphButton1 = (Button) findViewById(R.id.btn_emg1);
        graphButton2 = (Button) findViewById(R.id.btn_emg2);
        graphButton3 = (Button) findViewById(R.id.btn_emg3);
        graphButton4 = (Button) findViewById(R.id.btn_emg4);
        graphButton5 = (Button) findViewById(R.id.btn_emg5);
        graphButton6 = (Button) findViewById(R.id.btn_emg6);
        graphButton7 = (Button) findViewById(R.id.btn_emg7);
        graphButton8 = (Button) findViewById(R.id.btn_emg8);


        //set color
        graphButton1.setBackgroundColor(Color.argb(0x66, 0xff, 0, 0xff));
        graphButton2.setBackgroundColor(Color.argb(0x66, 0xff, 0x00, 0x00));
        graphButton3.setBackgroundColor(Color.argb(0x66, 0x66, 0x33, 0xff));
        graphButton4.setBackgroundColor(Color.argb(0x66, 0xff, 0x66, 0x33));
        graphButton5.setBackgroundColor(Color.argb(0x66, 0xff, 0x33, 0x66));
        graphButton6.setBackgroundColor(Color.argb(0x66, 0x00, 0x33, 0xff));
        graphButton7.setBackgroundColor(Color.argb(0x66, 0x00, 0x33, 0x33));
        graphButton8.setBackgroundColor(Color.argb(0x66, 0x66, 0xcc, 0x66));

        emgDataText = (TextView)findViewById(R.id.emgDataTextView);
        gestureText = (TextView)findViewById(R.id.gestureTextView);
        mHandler = new Handler();

        startNopModel();

        BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        //Intent intent = getIntent();
        //deviceName = intent.getStringExtra(MyoListActivity.TAG);


        SharedPreferences prefs = getSharedPreferences("signson", MODE_PRIVATE);
        deviceName = prefs.getString("myo", "");
        Log.i("Myo: ", "[" + deviceName + "]");


        if (deviceName != null) {
            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                // Scanning Time out by Handler.
                // The device scanning needs high energy.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBluetoothAdapter.stopLeScan(MyoActivity.this);
                    }
                }, SCAN_PERIOD);
                mBluetoothAdapter.startLeScan(this);
            }
        }

        Button menu = (Button) findViewById(R.id.menuBtn);
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menui = new MenuActivity();
                menui.initiatePopupWindow(MyoActivity.this, v);
            }
        });

        graph = (LineGraph) findViewById(R.id.holo_graph_view);
        bStopEMG = (Button) findViewById(R.id.bStopEmg);
        bEMG = (Button) findViewById(R.id.bEMG);
        bDetect = (Button) findViewById(R.id.bDetect);
        bSave = (Button) findViewById(R.id.bSave);
        tTranslate = (TextView) findViewById(R.id.tTranslate);
        iSignal = (ImageView) findViewById(R.id.iSignal);

        Redesign();
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, MENU_LIST, 0, "Find Myo");
        menu.add(0, MENU_BYE, 0, "Good Bye");
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {

        super.onResume();
        Redesign();
    }

    public void Redesign(){
        //parameters

        SharedPreferences prefs = getSharedPreferences("signson", MODE_PRIVATE);
        deviceName = prefs.getString("myo", "");
        Log.i("Myo: ", "[" + deviceName + "]");

        type = getIntent().getStringExtra("type");
        img = getIntent().getStringExtra("img");
        sinal = getIntent().getStringExtra("sinal");

        Log.i("Log - type", type);

        if (type.equals("0")){ // Signal



            Log.i("Log - type", type);
            Log.i("Log - img", img);
            Log.i("Log - sinal", sinal);


            bStopEMG.setVisibility(View.INVISIBLE);
            bDetect.setVisibility(View.INVISIBLE);
            tTranslate.setVisibility(View.INVISIBLE);
            iSignal.setVisibility(View.VISIBLE);
            bSave.setVisibility(View.VISIBLE);
            bEMG.setVisibility(View.VISIBLE);
            graph.setVisibility(View.VISIBLE);

            Picasso.with(iSignal.getContext()).load(img).into(iSignal);

        }else{ //Translation
            iSignal.setVisibility(View.INVISIBLE);
            bSave.setVisibility(View.INVISIBLE);
            bEMG.setVisibility(View.INVISIBLE);
            graph.setVisibility(View.INVISIBLE);
            bStopEMG.setVisibility(View.VISIBLE);
            bDetect.setVisibility(View.VISIBLE);
            tTranslate.setVisibility(View.VISIBLE);
        }

        startNopModel();

        BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        prefs = getSharedPreferences("signson", MODE_PRIVATE);
        deviceName = prefs.getString("myo", "");
        Log.i("Myo: ", "[" + deviceName + "]");


        if (deviceName != null) {
            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                // Scanning Time out by Handler.
                // The device scanning needs high energy.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBluetoothAdapter.stopLeScan(MyoActivity.this);
                    }
                }, SCAN_PERIOD);
                mBluetoothAdapter.startLeScan(this);
            }
        }

    }

    @Override
    public void onStop(){
        super.onStop();
        this.closeBLEGatt();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case MENU_LIST:
//                Log.d("Menu","Select Menu A");
                Intent intent = new Intent(this,MyoListActivity.class);
                startActivity(intent);
                return true;

            case MENU_BYE:
//                Log.d("Menu","Select Menu B");
                closeBLEGatt();
                Toast.makeText(getApplicationContext(), "Close GATT", Toast.LENGTH_SHORT).show();
                startNopModel();
                return true;

        }
        return false;
    }

    /** Define of BLE Callback */
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (deviceName.equals(device.getName())) {
            mBluetoothAdapter.stopLeScan(this);
            // Trying to connect GATT
            HashMap<String,View> views = new HashMap<String,View>();
            //put GraphView
            views.put("graph",graph);
            //put Button1?8

            views.put("btn1",graphButton1);
            views.put("btn2",graphButton2);
            views.put("btn3",graphButton3);
            views.put("btn4",graphButton4);
            views.put("btn5",graphButton5);
            views.put("btn6",graphButton6);
            views.put("btn7",graphButton7);
            views.put("btn8",graphButton8);

            mMyoCallback = new MyoGattCallback(mHandler, emgDataText, views);
            mBluetoothGatt = device.connectGatt(this, false, mMyoCallback);
            mMyoCallback.setBluetoothGatt(mBluetoothGatt);
        }
    }

    public void onClickVibration(View v){
        if (mBluetoothGatt == null || !mMyoCallback.setMyoControlCommand(commandList.sendVibration3())) {
            Log.d(TAG, "False Vibrate");
        }
    }

    public void onClickEMG(View v) {
        if (mBluetoothGatt == null || !mMyoCallback.setMyoControlCommand(commandList.sendEmgOnly())) {
            Log.d(TAG,"False EMG");
        } else {
            saveMethod  = new GestureSaveMethod();
            if (saveMethod.getSaveState() == GestureSaveMethod.SaveState.Have_Saved) {
                gestureText.setText("DETECT Ready");
            } else {
                gestureText.setText("Teach me \'Gesture\'");
            }
        }
    }

    public void onClickNoEMG(View v) {
        if (mBluetoothGatt == null
                || !mMyoCallback.setMyoControlCommand(commandList.sendUnsetData())
                || !mMyoCallback.setMyoControlCommand(commandList.sendNormalSleep())) {
            Log.d(TAG,"False Data Stop");
        }
    }

    public void onClickSave(View v) {

        if (saveMethod.getSaveState() == GestureSaveMethod.SaveState.Ready ||
                saveMethod.getSaveState() == GestureSaveMethod.SaveState.Have_Saved) {
            saveModel   = new GestureSaveModel(saveMethod, sinal);
            startSaveModel();
        } else if (saveMethod.getSaveState() == GestureSaveMethod.SaveState.Not_Saved) {
            startSaveModel();
        }

        saveMethod.setState(GestureSaveMethod.SaveState.Now_Saving);
        gestureText.setText("Saving ; " + (saveMethod.getGestureCounter() + 1));



    }

    public void onClickDetect(View v) {
        if (mBluetoothGatt == null || !mMyoCallback.setMyoControlCommand(commandList.sendEmgOnly())) {
            Log.d(TAG, "False EMG");
        } else {
            saveMethod  = new GestureSaveMethod();
            if (saveMethod.getSaveState() == GestureSaveMethod.SaveState.Have_Saved) {
                gestureText.setText("DETECT Ready");
            } else {
                gestureText.setText("Teach me \'Gesture\'");
            }
        }
        if (saveMethod.getSaveState() == GestureSaveMethod.SaveState.Have_Saved) {
            gestureText.setText("Let's Go !!");
            detectMethod = new GestureDetectMethod(saveMethod.getCompareDataList());
            detectModel = new GestureDetectModel(detectMethod);
            startDetectModel();
        }
    }

    public void closeBLEGatt() {
        if (mBluetoothGatt == null) {
            return;
        }
        mMyoCallback.stopCallback();
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    public void startSaveModel() {
        IGestureDetectModel model = saveModel;
        model.setAction(new GestureDetectSendResultAction(this));
        GestureDetectModelManager.setCurrentModel(model);
    }

    public void startDetectModel() {
        IGestureDetectModel model = detectModel;
        model.setAction(new GestureDetectSendResultAction(this));
        GestureDetectModelManager.setCurrentModel(model);
    }

    public void startNopModel() {
        GestureDetectModelManager.setCurrentModel(new NopModel());
    }

    public void setGestureText(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                gestureText.setText(message);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(MyoActivity.this);
                }
            }, SCAN_PERIOD);
            mBluetoothAdapter.startLeScan(this);
        }
    }


}

