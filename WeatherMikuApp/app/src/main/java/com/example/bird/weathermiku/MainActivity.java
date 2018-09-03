package com.example.bird.weathermiku;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static volatile String mainDes, des, humid, temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button next = (Button) findViewById(R.id.button);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.lookup);

                final EditText input = (EditText) findViewById(R.id.editText);
                Button next = (Button) findViewById(R.id.button4);

                input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        input.setText("");
                    }
                });

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        String place = input.getText().toString();
                        String rsp_msg = "";

                        HttpClient client = new DefaultHttpClient();
                        HttpGet get = new HttpGet("http://1.174.225.231:8097/weatherByMap/" + place);
                        HttpResponse response;

                        try {
                            response = client.execute(get);
                            HttpEntity entity = response.getEntity();
                            rsp_msg = EntityUtils.toString(entity, "UTF-8");
                        } catch (IOException e) {
                            System.out.println("fail");
                        }


                        if(rsp_msg.contains("bad999")) {
                            setContentView(R.layout.bad);
                            Button next = (Button) findViewById(R.id.button200);
                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setContentView(R.layout.miku);
                                }
                            });
                        } else {
                            String[] data = rsp_msg.split(",");
                            mainDes = (data[0].split(":"))[1].replace("\"", "").replace(",", "");
                            des = (data[1].split(":"))[1].replace("\"", "").replace(",", "");
                            humid = (data[3].split(":"))[1].replace("\"", "").replace(",", "").replace("}", "");
                            temp = (data[2].split(":"))[1].replace("\"", "").replace(",", "");

                            setContentView(R.layout.good);
                            TextView ans = (TextView) findViewById(R.id.textView2);

                            ans.setText("現在" + place + "的天氣：\n\n" + mainDes + "\n" + des + "\n溫度：" + temp + "C\n濕度：" + humid);

                            Button next = (Button) findViewById(R.id.button2);
                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setContentView(R.layout.miku);
                                }
                            });
                        }


                    }
                });
            }
        });

    }
}
