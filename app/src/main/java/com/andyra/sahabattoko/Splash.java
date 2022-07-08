package com.andyra.sahabattoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tvversi = findViewById(R.id.ss_versi);
        tvversi.setText("v."+BuildConfig.VERSION_NAME);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // cek koneksi
                ConnectivityManager mconnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                while (mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                        mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
                    Toast.makeText(Splash.this, "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startActivity(new Intent(Splash.this, Login.class));
                finish();
            }
        }, 2000);
    }
}