package com.b_noble.android.networktest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.b_noble.android.networktest.util.HttpCallbackListener;
import com.b_noble.android.networktest.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,HttpCallbackListener{

    private final int SHOW_RESPONSE = 0;

    private Button sendRequestBtn;
    private TextView contextTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.networklayout);

        sendRequestBtn = (Button) findViewById(R.id.sendRequest);
        contextTextView = (TextView) findViewById(R.id.response);

        sendRequestBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendRequest :
                sendRequest();
                //sendRequestWithHttpClient();
                break;
            default:
                break;
        }
    }

    //httpClient 被废弃
    private void sendRequestWithHttpClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    private void sendRequest(){

        String address = "http://192.168.2.100:8080/examples/test_json.json";

        HttpUtil.sendHttpRequest(address,this);
        /**
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL("http://192.168.2.100:8080/examples/test_json.json");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder responseStr = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine()) != null){
                        responseStr.append(line);
                    }

                    //解析xml

                    //paseXmlWithPull(responseStr.toString());

                    parseJsonWithJsonObject(responseStr.toString());
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = responseStr.toString();
                    handler.sendMessage(message);
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
         **/


    }

    private void parseJsonWithJsonObject(String jsonStr){
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.d("MainActivity","id is:" + id);
                Log.d("MainActivity","name is:" + name);
                Log.d("MainActivity","version is:" + version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void paseXmlWithPull(String xml){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();

            String id = "";
            String name = "";
            String version = "";
            while (eventType != parser.END_DOCUMENT){
                String nodeName = parser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG :{
                        if("id".equals(nodeName)){
                            id = parser.nextText();
                        }else if("name".equals(nodeName)){
                            name = parser.nextText();
                        }else if("version".equals(nodeName)){
                            version = parser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG : {
                        if("app".equals(nodeName)){

                            Log.d("MainActivity","id:" + id);
                            Log.d("MainActivity","name:" + name);
                            Log.d("MainActivity","version:" + version);

                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case SHOW_RESPONSE :
                    String resStr = (String)message.obj;
                    contextTextView.setText(resStr);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onFinish(String responseStr) {
        parseJsonWithJsonObject(responseStr.toString());
        Message message = new Message();
        message.what = SHOW_RESPONSE;
        message.obj = responseStr.toString();
        handler.sendMessage(message);
    }

    @Override
    public void onError(Exception e) {
        Log.e("MainAcitivity",e.getMessage());
        e.printStackTrace();
    }
}
