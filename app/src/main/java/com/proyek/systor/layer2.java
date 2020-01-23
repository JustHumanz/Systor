package com.proyek.Systor;

import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.util.*;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class layer2 extends AppCompatActivity {
    private static final String TAG = "layer2";
    private TextView hasil1;
    private TextView hasil2;
    private TextView hasil3;
    private TextView hostname;
    private TextView status_ser;
    private TextView last;


    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        final String user = bundle.getString("user");
        final String password = bundle.getString("pass");
        final String host = bundle.getString("host");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer2);
        hasil1 = (TextView) findViewById(R.id.hasil1);
        hasil2 = (TextView) findViewById(R.id.hasil2);
        hasil3 = (TextView) findViewById(R.id.hasil3);
        hostname = (TextView) findViewById(R.id.wel);
        status_ser = (TextView) findViewById(R.id.status);
        last = (TextView) findViewById(R.id.last);

        ImageView imgClick = (ImageView) findViewById(R.id.nginx);
        ImageView imgClick2 = (ImageView) findViewById(R.id.apache);
        ImageView imgClick3 = (ImageView) findViewById(R.id.nginx_stop);
        ImageView imgClick4 = (ImageView) findViewById(R.id.nginx_start);
        ImageView imgClick5 = (ImageView) findViewById(R.id.apache_stop);
        ImageView imgClick6 = (ImageView) findViewById(R.id.apache_start);
        ImageView imgClick7 = (ImageView) findViewById(R.id.php_fpm);
        ImageView imgClick8 = (ImageView) findViewById(R.id.php_fpm_stop);
        ImageView imgClick9 = (ImageView) findViewById(R.id.php_fpm_start);

        final Button status = (Button) findViewById(R.id.res_stat);


        new Thread() {
            public void run() {
                Integer i = 0;
                while (i++ < 1000) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                status.performClick();
                            }
                        });
                        Thread.sleep(30000);
                    } catch (InterruptedException ee) {
                        ee.printStackTrace();
                    }
                }
            }
        }.start();
        /*
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        // This schedule a runnable task every 1 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                status.performClick();
            }
        }, 0, 1, TimeUnit.MINUTES);

         */

        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    hostname(user,password,host);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(1);

        imgClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            nginx(user,password,host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
            private void nginx(String user, String password, String host) {
                int port=22;
                JSch jsch = new JSch();
                try {
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.connect();
                    }catch (Exception ee){
                        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                    }

                    ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                    channelssh.setCommand("systemctl is-active nginx");
                    InputStream commandOutput = channelssh.getExtInputStream();
                    final StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder errorBuffer = new StringBuilder();
                    InputStream in = channelssh.getInputStream();
                    InputStream err = channelssh.getExtInputStream();
                    channelssh.connect();
                    //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;
                            outputBuffer.append(new String(tmp, 0, i));
                        }
                        while (err.available() > 0) {
                            int i = err.read(tmp, 0, 1024);
                            if (i < 0) break;
                            errorBuffer.append(new String(tmp, 0, i));
                        }
                        if (channelssh.isClosed()) {
                            if ((in.available() > 0) || (err.available() > 0)) continue;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    hasil1.setText(outputBuffer.toString());
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }

                }catch (Exception ee){
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                }

            }
        });

        imgClick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            apache(user,password,host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
            private void apache(String user, String password, String host) {
                int port=22;
                JSch jsch = new JSch();
                try {
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.connect();
                    }catch (Exception ee){
                        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                    }

                    ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                    channelssh.setCommand("systemctl is-active httpd");
                    InputStream commandOutput = channelssh.getExtInputStream();
                    final StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder errorBuffer = new StringBuilder();
                    InputStream in = channelssh.getInputStream();
                    InputStream err = channelssh.getExtInputStream();
                    channelssh.connect();
                    //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;
                            outputBuffer.append(new String(tmp, 0, i));
                        }
                        while (err.available() > 0) {
                            int i = err.read(tmp, 0, 1024);
                            if (i < 0) break;
                            errorBuffer.append(new String(tmp, 0, i));
                        }
                        if (channelssh.isClosed()) {
                            if ((in.available() > 0) || (err.available() > 0)) continue;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    hasil2.setText(outputBuffer.toString());
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }

                }catch (Exception ee){
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                }

            }
        });

        imgClick3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            nginx_stop(user,password,host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
            private void nginx_stop(String user, String password, String host) {
                int port=22;
                JSch jsch = new JSch();
                try {
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.connect();
                    }catch (Exception ee){
                        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                    }

                    ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                    channelssh.setCommand("systemctl stop nginx && systemctl is-active nginx");
                    InputStream commandOutput = channelssh.getExtInputStream();
                    final StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder errorBuffer = new StringBuilder();
                    InputStream in = channelssh.getInputStream();
                    InputStream err = channelssh.getExtInputStream();
                    channelssh.connect();
                    //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;
                            outputBuffer.append(new String(tmp, 0, i));
                        }
                        while (err.available() > 0) {
                            int i = err.read(tmp, 0, 1024);
                            if (i < 0) break;
                            errorBuffer.append(new String(tmp, 0, i));
                        }
                        if (channelssh.isClosed()) {
                            if ((in.available() > 0) || (err.available() > 0)) continue;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    hasil1.setText(outputBuffer.toString());
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }

                }catch (Exception ee){
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                }

            }
        });
        imgClick4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            nginx_start(user,password,host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
            private void nginx_start(String user, String password, String host) {
                int port=22;
                JSch jsch = new JSch();
                try {
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.connect();
                    }catch (Exception ee){
                        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                    }

                    ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                    channelssh.setCommand("systemctl start nginx && systemctl is-active nginx");
                    InputStream commandOutput = channelssh.getExtInputStream();
                    final StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder errorBuffer = new StringBuilder();
                    InputStream in = channelssh.getInputStream();
                    InputStream err = channelssh.getExtInputStream();
                    channelssh.connect();
                    //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;
                            outputBuffer.append(new String(tmp, 0, i));
                        }
                        while (err.available() > 0) {
                            int i = err.read(tmp, 0, 1024);
                            if (i < 0) break;
                            errorBuffer.append(new String(tmp, 0, i));
                        }
                        if (channelssh.isClosed()) {
                            if ((in.available() > 0) || (err.available() > 0)) continue;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    hasil1.setText(outputBuffer.toString());
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }

                }catch (Exception ee){
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                }

            }
        });
        imgClick5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            apache_stop(user,password,host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
            private void apache_stop(String user, String password, String host) {
                int port=22;
                JSch jsch = new JSch();
                try {
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.connect();
                    }catch (Exception ee){
                        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                    }

                    ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                    channelssh.setCommand("systemctl stop httpd && systemctl is-active httpd");
                    InputStream commandOutput = channelssh.getExtInputStream();
                    final StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder errorBuffer = new StringBuilder();
                    InputStream in = channelssh.getInputStream();
                    InputStream err = channelssh.getExtInputStream();
                    channelssh.connect();
                    //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;
                            outputBuffer.append(new String(tmp, 0, i));
                        }
                        while (err.available() > 0) {
                            int i = err.read(tmp, 0, 1024);
                            if (i < 0) break;
                            errorBuffer.append(new String(tmp, 0, i));
                        }
                        if (channelssh.isClosed()) {
                            if ((in.available() > 0) || (err.available() > 0)) continue;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    hasil2.setText(outputBuffer.toString());
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }

                }catch (Exception ee){
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                }

            }
        });
        imgClick6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            apache_start(user,password,host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
            private void apache_start(String user, String password, String host) {
                int port=22;
                JSch jsch = new JSch();
                try {
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.connect();
                    }catch (Exception ee){
                        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                    }

                    ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                    channelssh.setCommand("systemctl start httpd && systemctl is-active httpd");
                    InputStream commandOutput = channelssh.getExtInputStream();
                    final StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder errorBuffer = new StringBuilder();
                    InputStream in = channelssh.getInputStream();
                    InputStream err = channelssh.getExtInputStream();
                    channelssh.connect();
                    //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;
                            outputBuffer.append(new String(tmp, 0, i));
                        }
                        while (err.available() > 0) {
                            int i = err.read(tmp, 0, 1024);
                            if (i < 0) break;
                            errorBuffer.append(new String(tmp, 0, i));
                        }
                        if (channelssh.isClosed()) {
                            if ((in.available() > 0) || (err.available() > 0)) continue;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    hasil2.setText(outputBuffer.toString());
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }

                }catch (Exception ee){
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                }

            }
        });
        imgClick7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            php_fpm(user,password,host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
            private void php_fpm(String user, String password, String host) {
                int port=22;
                JSch jsch = new JSch();
                try {
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.connect();
                    }catch (Exception ee){
                        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                    }

                    ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                    channelssh.setCommand("systemctl is-active php-fpm.service");
                    InputStream commandOutput = channelssh.getExtInputStream();
                    final StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder errorBuffer = new StringBuilder();
                    InputStream in = channelssh.getInputStream();
                    InputStream err = channelssh.getExtInputStream();
                    channelssh.connect();
                    //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;
                            outputBuffer.append(new String(tmp, 0, i));
                        }
                        while (err.available() > 0) {
                            int i = err.read(tmp, 0, 1024);
                            if (i < 0) break;
                            errorBuffer.append(new String(tmp, 0, i));
                        }
                        if (channelssh.isClosed()) {
                            if ((in.available() > 0) || (err.available() > 0)) continue;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    hasil3.setText(outputBuffer.toString());
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }

                }catch (Exception ee){
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                }

            }
        });
        imgClick9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            php_fpm_start(user,password,host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
            private void php_fpm_start(String user, String password, String host) {
                int port=22;
                JSch jsch = new JSch();
                try {
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.connect();
                    }catch (Exception ee){
                        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                    }

                    ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                    channelssh.setCommand("systemctl start php-fpm.service");
                    InputStream commandOutput = channelssh.getExtInputStream();
                    final StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder errorBuffer = new StringBuilder();
                    InputStream in = channelssh.getInputStream();
                    InputStream err = channelssh.getExtInputStream();
                    channelssh.connect();
                    //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;
                            outputBuffer.append(new String(tmp, 0, i));
                        }
                        while (err.available() > 0) {
                            int i = err.read(tmp, 0, 1024);
                            if (i < 0) break;
                            errorBuffer.append(new String(tmp, 0, i));
                        }
                        if (channelssh.isClosed()) {
                            if ((in.available() > 0) || (err.available() > 0)) continue;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    hasil3.setText(outputBuffer.toString());
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }

                }catch (Exception ee){
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                }

            }
        });
        imgClick8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            php_fpm_stop(user,password,host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
            private void php_fpm_stop(String user, String password, String host) {
                int port=22;
                JSch jsch = new JSch();
                try {
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.connect();
                    }catch (Exception ee){
                        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                    }

                    ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                    channelssh.setCommand("systemctl stop php-fpm.service");
                    InputStream commandOutput = channelssh.getExtInputStream();
                    final StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder errorBuffer = new StringBuilder();
                    InputStream in = channelssh.getInputStream();
                    InputStream err = channelssh.getExtInputStream();
                    channelssh.connect();
                    //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;
                            outputBuffer.append(new String(tmp, 0, i));
                        }
                        while (err.available() > 0) {
                            int i = err.read(tmp, 0, 1024);
                            if (i < 0) break;
                            errorBuffer.append(new String(tmp, 0, i));
                        }
                        if (channelssh.isClosed()) {
                            if ((in.available() > 0) || (err.available() > 0)) continue;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    hasil3.setText(outputBuffer.toString());
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }

                }catch (Exception ee){
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                }

            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            status_server(user,password,host);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
            private void status_server(String user, String password, String host) {
                int port=22;
                JSch jsch = new JSch();
                try {
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    try {
                        session.connect();
                    }catch (Exception ee){
                        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                    }

                    ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
                    channelssh.setCommand("echo -e 'OS\\t : '$(cat /etc/*-release | grep \"PRETTY_NAME\" | sed 's/PRETTY_NAME=//g' | sed 's/\"//g')'\\nKernel\\t : '$(uname -r)'\\nUptime\\t : '$(uptime -p)'\\nMemory\\t : '$(free | awk '/Mem/{printf(\"Used, %.2f%\"), $3/$2*100} /buffers\\/cache/{printf(\", buffers: %.2f%\"), $4/($3+$4)*100}')'\\n\\t   '$(free | awk '/Swap/{printf(\"Swap, %.2f%\"), $3/$2*100}')'\\nDisk\\t : 'Size, $(df -Ph . | tail -1 | awk '{print $2}')'\\n\\t   'Used, $(df -Ph . | tail -1 | awk '{print $3}')'\\n\\t   'Avail, $(df -Ph . | tail -1 | awk '{print $4}')");
                    InputStream commandOutput = channelssh.getExtInputStream();
                    final StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder errorBuffer = new StringBuilder();
                    InputStream in = channelssh.getInputStream();
                    InputStream err = channelssh.getExtInputStream();
                    channelssh.connect();
                    //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0) break;
                            outputBuffer.append(new String(tmp, 0, i));
                        }
                        while (err.available() > 0) {
                            int i = err.read(tmp, 0, 1024);
                            if (i < 0) break;
                            errorBuffer.append(new String(tmp, 0, i));
                        }
                        if (channelssh.isClosed()) {
                            if ((in.available() > 0) || (err.available() > 0)) continue;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    status_ser.setText(outputBuffer.toString());
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }

                }catch (Exception ee){
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
                }

            }
        });
    }

    private void hostname(final String user, final String password, final String host) {
        int port=22;
        JSch jsch = new JSch();
        try {
            final Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            try {
                session.connect();
            }catch (Exception ee){
                Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
            }

            ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
            channelssh.setCommand("echo $(whoami)'@'$(hostname)");
            InputStream commandOutput = channelssh.getExtInputStream();
            final StringBuilder outputBuffer = new StringBuilder();
            StringBuilder errorBuffer = new StringBuilder();
            InputStream in = channelssh.getInputStream();
            InputStream err = channelssh.getExtInputStream();
            channelssh.connect();
            //Log.d(TAG, "testtt: "+channelssh.getOutputStream().toString());
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    outputBuffer.append(new String(tmp, 0, i));
                }
                while (err.available() > 0) {
                    int i = err.read(tmp, 0, 1024);
                    if (i < 0) break;
                    errorBuffer.append(new String(tmp, 0, i));
                }
                if (channelssh.isClosed()) {
                    if ((in.available() > 0) || (err.available() > 0)) continue;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hostname.setText(outputBuffer.toString()+" Services");

                        }
                    });
                    lazt(user,password,host);
                    break;
                }
            }

        }catch (Exception ee){
            Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
        }

    }

    private void lazt(String user, String password, String host) {
        int port=22;
        JSch jsch = new JSch();
        try {
            final Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            try {
                session.connect();
            }catch (Exception ee){
                Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
            }

            ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
            channelssh.setCommand("last | head -n5 |  awk '{print $1\" \" $3\" \"$5\" \"$6\" \"$7}'");
            InputStream commandOutput = channelssh.getExtInputStream();
            final StringBuilder outputBuffer = new StringBuilder();
            StringBuilder errorBuffer = new StringBuilder();
            InputStream in = channelssh.getInputStream();
            InputStream err = channelssh.getExtInputStream();
            channelssh.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    outputBuffer.append(new String(tmp, 0, i));
                }
                while (err.available() > 0) {
                    int i = err.read(tmp, 0, 1024);
                    if (i < 0) break;
                    errorBuffer.append(new String(tmp, 0, i));
                }
                if (channelssh.isClosed()) {
                    if ((in.available() > 0) || (err.available() > 0)) continue;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            last.setText("Last Login : \n"+outputBuffer.toString());
                        }
                    });
                    break;
                }
            }

        }catch (Exception ee){
        Log.e(TAG, "Exception: "+Log.getStackTraceString(ee));
    }

    }
}
