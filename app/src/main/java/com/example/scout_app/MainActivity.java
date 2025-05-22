package com.example.weather_app;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ListView;
import android.widget.Button;
import android.widget.*;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    Button button;
    Toast toast;
    EditText citta;
    ListView listarichieste;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =

                            insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top,

                            systemBars.right, systemBars.bottom);

                    return insets;
                });
        button=findViewById(R.id.button);
        listarichieste=findViewById(R.id.listView);

        citta=findViewById(R.id.editTextText2);
        citta.setText("");
        arrayList = new ArrayList<String>();
// Adapter: You need three parameters 'the context, id of the layout (it

        will be where the data is shown),
// and the array that contains the data
        adapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_spinner_item,
                        arrayList);

// Here, you set the data in your ListView
        listarichieste.setAdapter(adapter);
    }
    public void effettuaRichiesta(View view){
        String city = citta.getText().toString().trim();
        if(city.isEmpty())
        {
            toast = Toast.makeText(this,"Inserisci una

                    città", Toast.LENGTH_LONG);
                    toast.show();
        }
        else {
            getWeather(city);
        }
    }
    private void getWeather(String city) {
        new Thread(() -> {
            try {

                String urlString = "https://wheather-
                webapp-emanuelepallot1.replit.app/weather?city=" +

                URLEncoder.encode(city, "UTF-8");

                URL url = new URL(urlString);
                HttpURLConnection conn =
                        (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(

                        new

                                InputStreamReader(conn.getInputStream())

                );
                StringBuilder response = new

                        StringBuilder();

                String line;

                while ((line = reader.readLine()) != null)

                {

                    response.append(line);
                }
                reader.close();
                conn.disconnect();
                JSONObject json = new
                        JSONObject(response.toString());

                String luogo = json.getString("luogo");
                String temperatura =

                        json.getString("temperatura");

                runOnUiThread(() -> {
                    arrayList.add("luogo: " + luogo + " |

                            temperatura: " + temperatura);

                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this,
                            "Errore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "Errore: " +

                            e.getMessage());
                });
            }
        }).start();
    }
}