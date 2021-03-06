package com.example.bar.bubbleclient;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Curreir extends AppCompatActivity {
    private static final String TAG = "BubbleClient";
    private static WifiManager wm;
    private static Curreir self;
    private static List<android.net.wifi.ScanResult> netlist;
    private static RadioGroup rg;
    private static RadioButton curButton;
    private static BroadcastReceiver reciever;
    private static String Token;
    private static String UserName;
    private static String[] dets;

    WifiManager wifi;
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<android.net.wifi.ScanResult> results;

    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curreir);


    }
    @Override
    protected void onStart(){
        super.onStart();
        // Start Wifi
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        //results = wifi.getScanResults();






        //When Scan is done
        reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                //-TODO - request THE CORRECT permissions online:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {
                    results = wifi.getScanResults();
                    //do something, permission was previously granted; or legacy device
                }
                Map<String, String> map = new HashMap<String, String>();
                results = wifi.getScanResults();
                Toast.makeText(self, "Scanning...", Toast.LENGTH_SHORT).show();
                size = results.size();
                rg = (RadioGroup) findViewById(R.id.RBWrapper);
                rg.removeAllViews();
                for (int i = 0; i < size; i++) {

                    if(results.get(i).SSID.endsWith(".bubbles.one")) {
                        String ssidSTR = results.get(i).SSID;
                        Log.v("SSID:", ssidSTR);
                        dets = ssidSTR.split("\\.");
                        Log.v("username:", dets[1]);
                        Log.v("token:", dets[0]);
                        if(map.containsKey(dets[1])) continue;
                        map.put(dets[1], dets[0]);

                        //dets = new String[]{"123", "bar"};




                        int time = (int) (System.currentTimeMillis());
                        Timestamp tsTemp = new Timestamp(time);
                        String ts =  tsTemp.toString();
                        curButton = new RadioButton(self);
                        curButton.setText(dets[1]);
                        curButton.setTextSize(40);
                        curButton.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             self.unregisterReceiver(reciever);
                                                             Toast.makeText(self, curButton.getText(), Toast.LENGTH_SHORT).show();
                                                             Intent messageBubble = new Intent(Curreir.this, messagePopUp.class);
                                                             messageBubble.putExtra("token", dets[0]);
                                                             messageBubble.putExtra("userName", dets[1]);
                                                             startActivityForResult(messageBubble, 0);

                                                         }
                                                     }
                        );
                        Log.v("radio button", "adding and breaking");
                        rg.addView(curButton);

                    }
                }


                rg.invalidate();
            }
        };
        registerReceiver(reciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        self = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //RadioGroup rg = (RadioGroup) findViewById(R.id.RBWrapper);
        //WifiInfo wfi = wifi.getConnectionInfo();

        arraylist.clear();

        autoScanner();
        Toast.makeText(this, "Scanning... Please wait", Toast.LENGTH_SHORT).show();

        try {
            while(size == 0){
                wait(100);
            }
            size = size - 1;
            while (size >= 0) {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put(ITEM_KEY, results.get(size).SSID + "  " + results.get(size).capabilities);
                Log.v(TAG, results.get(size).SSID);
                arraylist.add(item);
                size--;
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String string = data.getStringExtra("RESULT_STRING");


        rg.clearCheck();

        Toast.makeText(getApplicationContext(), "message sent", Toast.LENGTH_LONG).show();
    }
    public String publish(String UserName, String Token, String message){
        return "all is well";
    }

    public String read(String UserName, String Password){
        return "all is well";
    }

    private void autoScanner() {
        try{
            while(wifi != null){
                wifi.startScan();
                wait(1000);
            }
        } catch (Exception e) {}
    }

    public void StartPopUp(){

    }

}
        //android.support.design.widget.CollapsingToolbarLayout toolbar2 = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //setSupportActionBar(toolbar);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Log.v(TAG, "button Cricked!!!!!");
            }

        wm = (WifiManager) this.getSystemService(this.WIFI_SERVICE);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.registerReceiver(new MyReceiver(), new IntentFilter("android.net.wifi."));
        Log.v(TAG, "super onCreated!!!!!");
        //LinearLayout wrapper = (LinearLayout) inflater.inflate(R.layout.content_currier, null);
        //LinearLayout wrapper = (LinearLayout) findViewById(R.layout.content_currier);
        rg = (RadioGroup) findViewById(R.id.RBWrapper);
        //RadioButton button;
        //WifiInfo wfinfor = wm.getConnectionInfo();
        wm.startScan();




        this.invalidateOptionsMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_curreir, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public class MyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            netlist = wm.getScanResults();

            for(int i = 0; i < 3; i++) {
                RadioButton button = new RadioButton(self);
                button.setText("RB: " + i);
                rg.addView(button);

            }
            rg.invalidate();
        }
    }
}
public class WiFiDemo extends Activity implements OnClickListener
{
    WifiManager wifi;
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<android.net.wifi.ScanResult> results;

    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;

    /* Called when the activity is first created. *//*
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        //textStatus = (TextView) findViewById(R.id.textStatus);
        //buttonScan = (Button) findViewById(R.id.buttonScan);
        //buttonScan.setOnClickListener(this);
        //lv = (ListView)findViewById(R.id.list);


    }

    public void onClick(View view)
    {

    }
}*/