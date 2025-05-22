package com.example.scout_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText citta;
    private ListView listRicerche;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button = findViewById(R.id.button);
        listRicerche = findViewById(R.id.listView);
        citta = findViewById(R.id.editTextText2);
        citta.setText("");

        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listRicerche.setAdapter(adapter);

        button.setOnClickListener(v -> effettuaRichiesta());
    }

    private void effettuaRichiesta() {
        String city = citta.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(this, "Inserisci una città", Toast.LENGTH_LONG).show();
        } else {
            getWeather(city);
        }
    }

    private void getWeather(String city) {
        new Thread(() -> {
            try {
                String encodedCity = URLEncoder.encode(city, "UTF-8");
                String urlString = "https://weather-webapp-emanuelepallot1.replit.app/weather?city=" + encodedCity;

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();

                JSONObject json = new JSONObject(response.toString());
                String luogo = json.optString("luogo", "-");
                String temperatura = json.optString("temperatura", "-");

                runOnUiThread(() -> {
                    arrayList.add("Luogo: " + luogo + " | Temperatura: " + temperatura);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                Log.e("MainActivity", "Errore: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        "Errore: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
