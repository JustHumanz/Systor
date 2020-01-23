package com.proyek.Systor;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.*;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private TextView hasil1;
    private EditText username1;
    private EditText password1;
    private EditText host1;
    private static final String TAG = "MainActivity";
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username1 = (EditText) findViewById(R.id.user);
        password1 = (EditText) findViewById(R.id.pass);
        host1 = (EditText) findViewById(R.id.host);
        hasil1 = (TextView) findViewById(R.id.hasil1);


        Button Gas = (Button) findViewById(R.id.Gas);
        Button info = (Button) findViewById(R.id.info);

        ImageView infoclick = (ImageView) findViewById(R.id.infoapl);
        ImageView helpclick = (ImageView) findViewById(R.id.help);

        Gas.setOnClickListener(new has());

        infoclick.setOnClickListener(new in());

        helpclick.setOnClickListener(new he());
    }
    class he implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.help);
            dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
            dialog.setTitle("Help");

            dialog.show();
        }
    }
    class in implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.info);
            dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
            dialog.setTitle("Info Aplikasi");

            dialog.show();
        }
    }
    class has implements Button.OnClickListener {
        public void onClick (View v){
            new AsyncTask<Integer, Void, Void>(){
                @Override
                protected Void doInBackground(Integer... params) {
                    try {
                        testtt(username1.getText().toString(),password1.getText().toString(),host1.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(1);

        }

        private void testtt(String user,String password,String host) {
            int port=22;
            JSch jsch = new JSch();
            try{
                final Session session = jsch.getSession(user, host, port);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                try {
                    session.connect();
                    runOnUiThread(new Runnable(){
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Successful login",Toast.LENGTH_LONG).show();
                            session.disconnect();
                            openlayer2();
                        }
                    });
                } catch (Exception ee){
                    runOnUiThread(new Runnable(){
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error,cek network username & pass",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            catch(Exception e){
            }
        }
        public void openlayer2(){

            Intent intent = new Intent(MainActivity.this, com.proyek.Systor.layer2.class);
            Bundle extras = new Bundle();
            intent.putExtra("user",username1.getText().toString());
            intent.putExtra("pass",password1.getText().toString());
            intent.putExtra("host",host1.getText().toString());
            startActivity(intent);
        }

    }
}