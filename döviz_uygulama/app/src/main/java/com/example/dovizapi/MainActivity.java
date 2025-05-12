package com.example.dovizapi;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView chfText;
    TextView usdText;
    TextView jpyText;
    TextView tryText;
    TextView cadText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chfText = findViewById(R.id.chfText);
        usdText = findViewById(R.id.usdText);
        jpyText = findViewById(R.id.jpyText);
        tryText = findViewById(R.id.tryText);
        cadText = findViewById(R.id.cadText);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRates();  // Butona tıklandığında döviz kurlarını çekmek için çağrılıyor
            }
        });
    }

    public void getRates() {
        String accessKey = "dfafd8b6aa8b8c3a9ba10992b5ddb146";  // API anahtarınızı burada belirttik
        DownloadData downloadData = new DownloadData();
        downloadData.execute("https://data.fixer.io/api/latest?access_key=" + accessKey);  // API isteğini bu şekilde yapıyoruz
    }

    public class DownloadData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(strings[0]);  // API URL'sini alıyoruz
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);  // API yanıtını StringBuilder'a ekliyoruz
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("DownloadData", "Error downloading data: " + e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();  // Bağlantıyı kesiyoruz
                }
            }

            return result.toString();  // JSON verisini döndürüyoruz
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                Log.d("API Response", s);  // Gelen JSON yanıtını logla

                JSONObject jsonObject = new JSONObject(s);
                JSONObject rates = jsonObject.getJSONObject("rates");  // Kurları almak için rates objesine bakıyoruz

                // Döviz kurlarını JSON'dan alıp TextView'lere yazdırıyoruz
                String chf = rates.getString("CHF");
                chfText.setText("CHF: " + chf);

                String usd = rates.getString("USD");
                usdText.setText("USD: " + usd);

                String jpy = rates.getString("JPY");
                jpyText.setText("JPY: " + jpy);

                String turkishLira = rates.getString("TRY");
                tryText.setText("TRY: " + turkishLira);

                String cad = rates.getString("CAD");
                cadText.setText("CAD: " + cad);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DownloadData", "Error parsing JSON: " + e.getMessage());
            }
        }

    }
}
