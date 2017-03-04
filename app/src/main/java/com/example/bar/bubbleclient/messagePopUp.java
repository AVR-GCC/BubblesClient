package com.example.bar.bubbleclient;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;

/**
 * Created by Bar on 3/4/2017.
 */
public class messagePopUp extends Activity {

    private static HttpURLConnection urlConnection;
    private static String url_str;
    private static BroadcastReceiver reciever;
    private static String response;
    private static TextView responseTextView;
    private static RelativeLayout top;
    private static String UserName;
    private static String Token;
    private static String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_pop_up);
        Button sendButton = (Button) findViewById(R.id.submitMessage);
        //top = (RelativeLayout) findViewById(R.id.massageWrapper);
        UserName = getIntent().getStringExtra("userName");
        Token = getIntent().getStringExtra("token");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();

            }
        });
    }
    public void SendMessage(){
        EditText messageBox = (EditText) findViewById(R.id.messageText);
        message = String.valueOf(messageBox.getText());
        //TODO - get username & current token


        publish(UserName, Token, message);
        Intent intent=new Intent();
        intent.putExtra("RESULT_STRING", response);
        setResult(RESULT_OK, intent);
        try {
            wait(1000);
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
        finish();


    }
    public String publish(String UserName, final String Token, String message){
        url_str = getString(R.string.ip) + ":" + getString(R.string.port);

        resThread thread = new resThread();
        thread.start();

        /*try {
            thread.join();
            Log.v("thread joiner", "thread joined");
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }*/
        return "this should work";
    }

    private class resThread extends Thread{
        public resThread(){
            super(new Runnable() {
                @Override
                public void run() {
                    try {

                        URL url = new URL(url_str + "/publish/" + UserName + "/" + Token + "/" + message);
                        Log.v("The url is", url.toString());
                        //URL url = new URL(url_str + "/read_all");
                        urlConnection = (HttpURLConnection) url.openConnection();
                        try {
                            Log.v("urlConnection is: ", urlConnection.toString());
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            StringBuilder stringBuilder = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                stringBuilder.append(line).append("\n");
                            }
                            bufferedReader.close();
                            response = stringBuilder.toString();
                            Log.v("urlThread:", "response text is: " + response);
                            //Toast.makeText(getApplicationContext(), "response: " + response, Toast.LENGTH_LONG).show();
                            //c.unregisterReceiver(reciever);
                        }
                        catch (Exception e){
                            Log.v("in the catch", "response text is: " + response);
                        }
                        finally{
                            urlConnection.disconnect();
                        }
                    }
                    catch(Exception e) {
                        Log.e("ERROR", e.getMessage(), e);
                    }
                }
            });
        }
    }
}
