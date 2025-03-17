package com.example.plaka_eslesme;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> numberAdapter;
    private final List<String> cities = new ArrayList<>();
    private final List<Integer> numbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView cityListView = findViewById(R.id.cityListView);
        ListView numberListView = findViewById(R.id.numberListView);

        // Şehir isimlerini listeye ekleyin
        cities.add("İstanbul");
        cities.add("Ankara");
        cities.add("İzmir");
        cities.add("Bursa");
        cities.add("Antalya");
        // 10 şehir daha ekleyin...

        // Başlangıçta sayılar için rastgele değerler oluşturun
        for (int i = 0; i < cities.size(); i++) {
            numbers.add(generateRandomNumber());
        }

        // Şehir isimlerini ve sayıları birer adaptörle bağlayın
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        numberAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, convertNumbersToString(numbers));

        cityListView.setAdapter(cityAdapter);
        numberListView.setAdapter(numberAdapter);

        // Butona tıklayınca sayıları rastgele güncelle
        Button randomButton = findViewById(R.id.randomButton);
        randomButton.setOnClickListener(v -> updateNumbers());

        // ListView item tıklama işlemi
        cityListView.setOnItemClickListener((parent, view, position, id) -> {
            String city = cities.get(position);
            int number = numbers.get(position);
            checkCityPlate(city, number);
        });
    }

    // Sayıları rastgele 0 ile 81 arasında güncelleme
    private void updateNumbers() {
        numbers.replaceAll(ignored -> {
            return generateRandomNumber(); // Sayıyı günceller
        });

        // Yeni sayıları listeye yansıt
        numberAdapter.clear();
        numberAdapter.addAll(convertNumbersToString(numbers));
        numberAdapter.notifyDataSetChanged();
    }

    // 0 ile 81 arasında rastgele bir sayı üretme
    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(82); // 0-81 arası
    }

    // Sayıları String listesine dönüştürme
    private List<String> convertNumbersToString(List<Integer> numbers) {
        List<String> stringList = new ArrayList<>();
        for (Integer number : numbers) {
            stringList.add(String.valueOf(number));
        }
        return stringList;
    }

    // Şehre tıklanınca plaka doğrulama
    private void checkCityPlate(String city, int number) {
        String correctPlate = getCorrectPlateForCity(city);
        if (correctPlate != null) {
            if (String.valueOf(number).equals(correctPlate)) {
                // Doğru plaka
                Toast.makeText(this, city + " için plaka numarası doğru!", Toast.LENGTH_SHORT).show();
            } else {
                // Yanlış plaka
                Toast.makeText(this, city + " için doğru plaka numarası: " + correctPlate, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Bu şehir için geçerli bir plaka yok.", Toast.LENGTH_SHORT).show();
        }
    }

    // Şehire göre doğru plaka numarasını döndürme
    private String getCorrectPlateForCity(String city) {
        switch (city) {
            case "İstanbul":
                return "34";
            case "Ankara":
                return "06";
            case "İzmir":
                return "35";
            case "Bursa":
                return "16";
            case "Antalya":
                return "07";
            // Diğer şehirlerin plaka numaralarını buraya ekleyin
            default:
                return null;
        }
    }
}
