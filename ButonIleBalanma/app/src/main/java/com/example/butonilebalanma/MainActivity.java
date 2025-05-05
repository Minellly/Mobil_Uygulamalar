package com.example.butonilebalanma;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button buttonB = (Button) findViewById(R.id.buttonB);
        Button buttonW = (Button) findViewById(R.id.buttonW);
        Button buttonC = (Button) findViewById(R.id.buttonC);

        buttonB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent gecisB = new Intent(MainActivity.this, com.example.butonilebalanma.bluetooth.class);
                startActivity(gecisB);
            }
        });

        buttonW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent gecisW = new Intent(MainActivity.this, wifi.class);
                startActivity(gecisW);
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent gecisC = new Intent(MainActivity.this, camera.class);
                startActivity(gecisC);
            }
        });
    }
}