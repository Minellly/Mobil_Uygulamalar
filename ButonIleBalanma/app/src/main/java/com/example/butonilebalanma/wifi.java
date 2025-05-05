package com.example.butonilebalanma;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class wifi extends AppCompatActivity {

    private WifiManager modem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        modem = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        final ToggleButton btn = (ToggleButton) findViewById(R.id.toggleButton2);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (btn.isChecked()) {
                    wifiAc();
                } else {
                    wifiKapat();
                }
            }
        });
    }

    private void wifiKapat() {
        if (modem.isWifiEnabled()) {
            modem.setWifiEnabled(false);
            Toast.makeText(wifi.this, "Wifi Kapalı", Toast.LENGTH_SHORT).show();
        }
    }

    private void wifiAc() {
        if (!modem.isWifiEnabled()) { // Wifi açık değilse açıyoruz
            modem.setWifiEnabled(true);
            Toast.makeText(wifi.this, "Wifi Açık", Toast.LENGTH_SHORT).show();
        }
    }
}