package com.example.listeleme;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ListView list1=(ListView) findViewById(R.id.list1);
        TextView text1=(TextView) findViewById((R.id.text1);

        String [] sehirler={"istanbul","ankara","izmir","kocaeli"};
        ArrayAdapter<String> verilistesi= new ArrayAdapter<>(context MainActivity.this,
                android.R.layout.simple_list_item_1,sehirler);
        list1.setAdapter(verilistesi);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this,verilistesi.getItem(i),
                        Toast.LENGTH_LONG).show();
                text1.setText(verilistesi(i));
            }
        });
    }
}