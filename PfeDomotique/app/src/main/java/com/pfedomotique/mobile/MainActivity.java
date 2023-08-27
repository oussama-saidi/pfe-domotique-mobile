package com.pfedomotique.mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.gson.Gson;
import com.pfedomotique.mobile.classes.Equipement;
import com.pfedomotique.mobile.databinding.ActivityMainBinding;
import com.pfedomotique.mobile.utils.PostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GetEquipements listeEquipements = new GetEquipements();
    Equipement[] quipements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_settings, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        LinearLayout layoutSalon=(LinearLayout) findViewById(R.id.layoutSalon);
        LinearLayout layoutChambreParent=(LinearLayout) findViewById(R.id.layoutBedroom);
        LinearLayout layoutChambreEnfant=(LinearLayout) findViewById(R.id.layoutKidRoom);
        listeEquipements.execute();
        layoutSalon.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("adresseIp",quipements[0].adresseIp);
                intent.putExtra("type",quipements[0].type);
                startActivity(intent);
            }
        });
        layoutChambreParent.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("adresseIp",quipements[1].adresseIp);
                intent.putExtra("type",quipements[1].type);
                startActivity(intent);
            }
        });

        layoutChambreEnfant.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("adresseIp",quipements[2].adresseIp);
                intent.putExtra("type",quipements[2].type);
                startActivity(intent);
            }
        });
    }

    public  class GetEquipements extends AsyncTask<String, Void, String> {
        private  String _urlApi = "";
        // on below line creating a variable for response line.
        String responseLine = null;
        // on below line creating a string builder.
        StringBuilder response = new StringBuilder();

        @Override
        protected String doInBackground(String... strings) {
            try {

                // on below line creating a url to post the data.
                //URL url = new URL("http://192.168.1.28:8081/api/Compte/AjouterCompte");
                URL url = new URL("http://192.168.1.15:8081/api/Arduino/ObtenirListeEquipements");

                // on below line opening the connection.
                HttpURLConnection client = (HttpURLConnection) url.openConnection();

                // on below line setting method as post.
                client.setRequestMethod("GET");

                // on below line setting content type and accept type.
                client.setRequestProperty("Content-Type", "application/json");
                client.setRequestProperty("Accept", "application/json");

                // on below line creating and initializing buffer reader.
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(client.getInputStream(), "utf-8"))) {

                    // on below line writing the response
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return  response.toString();
                }

            } catch (Exception e) {

                // on below line handling the exception.
                e.printStackTrace();
                // Toast.makeText(SignUpActivity.this, "Fail to post the data : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {


            // dismiss the progress dialog after receiving data from API
            //progressDialog.dismiss();
            try {
                Gson gson = new Gson();
               quipements = gson.fromJson(s, Equipement[].class);
                // JSON Parsing of data
                //JSONArray jsonArray = new JSONArray(s);

                //JSONObject oneObject = jsonArray.getJSONObject(0);
                // Pulling items from the array
                //title = oneObject.getString("title");



            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}