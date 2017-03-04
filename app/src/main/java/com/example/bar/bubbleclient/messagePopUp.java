package com.example.bar.bubbleclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Bar on 3/4/2017.
 */
public class messagePopUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_pop_up);
        Button sendButton = (Button) findViewById(R.id.submitMessage);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });
    }
    public void SendMessage(){
        EditText message = (EditText) findViewById(R.id.messageText);
        String message_str = message.toString();
        //TODO - get username & current token
        String UserName = "";
        String Token = "";
        publish(UserName, Token, message_str);



    }
    public String publish(String UserName, String Token, String message){
        String url_str = getString(R.string.ip) + ":" + getString(R.string.port);

        try {
            URL url = new URL(url_str + "/publish/" + UserName + "/" + Token + "/" + message);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

}
