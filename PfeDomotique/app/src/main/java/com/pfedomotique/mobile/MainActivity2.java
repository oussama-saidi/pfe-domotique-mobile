package com.pfedomotique.mobile;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pfedomotique.mobile.classes.ArduinoData;
import com.pfedomotique.mobile.classes.Commande;
import com.pfedomotique.mobile.classes.Equipement;
import com.pfedomotique.mobile.classes.Humidite;
import com.pfedomotique.mobile.classes.IndiceQualiteAir;
import com.pfedomotique.mobile.classes.Temperature;
import com.pfedomotique.mobile.listviewitems.ChartItem;
import com.pfedomotique.mobile.listviewitems.LineChartItem;
import com.pfedomotique.mobile.utils.JSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity2 extends AppCompatActivity {
    JSONObject jsonObject = new JSONObject();
    boolean isAirCondAutoActivated = false;
    private GetPiecesInfos getPiecesInfos= new GetPiecesInfos();
    private EnvoyerCommande envoyerCommande= new EnvoyerCommande();
    private ObtenirListeTemperature obtenirListeTemperature= new ObtenirListeTemperature();
    private ObtenirListeHumidite obtenirListeHumidite= new ObtenirListeHumidite();
    private ObtenirListeIqa obtenirListeIqa= new ObtenirListeIqa();
    private ArduinoData arduinoData = new ArduinoData();
    private Commande commande =  new Commande();
    private  TextView temperature ;
    private TextView humidite ;
    private LineChart lineChartTemperature;
    private LineChart lineChartHumidite;
    private LineChart lineChartIqa;
    private TextView iqa ;
    public JSONUtil jsonUtil = new JSONUtil();
    List<Entry> lineEntriesTemerature = new ArrayList<>();
    List<Entry> lineEntriesHumidite = new ArrayList<>();
    List<Entry> lineEntriesIqa = new ArrayList<>();

    public String adresseIp = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ProgressDialog progressDialog = new ProgressDialog(MainActivity2.this);
        SeekBar tempthresholdseekBar = findViewById(R.id.tempthresholdseekBar);
        final TextView tempthresholdValue = (TextView)findViewById(R.id.tempthresholdValue);
         temperature = (TextView)findViewById(R.id.valeurTemperature);
         humidite = (TextView)findViewById(R.id.valeurHumidite);
        iqa = (TextView)findViewById(R.id.valeurIQA);
        Switch climOnOff = (Switch) findViewById(R.id.climOnOff);
        Switch lightOnOff = (Switch) findViewById(R.id.lightOnOff);

        Intent intent = getIntent();
        adresseIp = intent.getStringExtra("adresseIp");
        String type = intent.getStringExtra("type");
        commande.AdresseIP = adresseIp;

        progressDialog.setTitle("Chargement en cours");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            jsonObject.put("cible",type );
            jsonObject.put("commande", "update-all");
            jsonObject.put("adresseIP", "192.168.1.180");
            jsonObject.put("adresseIPEsclave", adresseIp);
            jsonObject.put("port", 10002);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String jsonString = jsonObject.toString();
        getPiecesInfos.execute(jsonString);
        obtenirListeTemperature.execute();
        obtenirListeHumidite.execute();
        obtenirListeIqa.execute();
        progressDialog.dismiss();
        climOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(climOnOff.isChecked())
                {
                    try {
                        jsonObject.put("cible",type );
                        jsonObject.put("commande", "set-ac-on");
                        jsonObject.put("adresseIP", "192.168.1.180");
                        jsonObject.put("adresseIPEsclave", adresseIp);
                        jsonObject.put("port", 10002);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                else {
                    try {
                        jsonObject.put("cible",type );
                        jsonObject.put("commande", "set-ac-off");
                        jsonObject.put("adresseIP", "192.168.1.180");
                        jsonObject.put("adresseIPEsclave", adresseIp);
                        jsonObject.put("port", 10002);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                String jsonString = jsonObject.toString();
                new EnvoyerCommande().execute(jsonString);
            }
        });

        lightOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(lightOnOff.isChecked())
                {
                    try {
                        jsonObject.put("cible",type );
                        jsonObject.put("commande", "set-lampe-on");
                        jsonObject.put("adresseIP", "192.168.1.180");
                        jsonObject.put("adresseIPEsclave", adresseIp);
                        jsonObject.put("port", 10002);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                else {
                    try {
                        jsonObject.put("cible",type );
                        jsonObject.put("commande", "set-lampe-off");
                        jsonObject.put("adresseIP", "192.168.1.180");
                        jsonObject.put("adresseIPEsclave", adresseIp);
                        jsonObject.put("port", 10002);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                String jsonString = jsonObject.toString();
                new EnvoyerCommande().execute(jsonString);
            }
        });

        /*
        tempthresholdseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if(airCondAutoOnOff.isChecked())
                {
                    tempthresholdValue.setText(String.valueOf(progress)+" °C");
                }

                else {
                    Toast.makeText(getApplicationContext(), "La climatisation automatique est désactivée", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
*/
        ArrayList<ChartItem> list = new ArrayList<>();
        lineChartTemperature = findViewById(R.id.tempChart);
        lineChartHumidite = findViewById(R.id.humiditeChart);
        lineChartIqa = findViewById(R.id.iqaChart);


    }

    private void drawLineChart(LineChart lineChart,String label, List<Entry> dataSet) {
        Typeface mTf  = Typeface.createFromAsset(getApplicationContext().getAssets(), "OpenSans-Regular.ttf");
        List<Entry> lineEntries = dataSet;
        LineDataSet lineDataSet = new LineDataSet(lineEntries, label);

        LineData lineData = new LineData(lineDataSet);

        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);


        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.getXAxis().setTypeface(mTf);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(true);




        ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Valeur");
        xAxisLabel.add("Heure");



        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        lineChart.setData(lineData);
        lineChart.animateX(750);

    }

    private List<Entry> getDataSetHumidite() {
        List<Entry> lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(1,55));
        lineEntries.add(new Entry(2,55));
        lineEntries.add(new Entry(3,60));
        lineEntries.add(new Entry(4,60));
        lineEntries.add(new Entry(5,65));
        lineEntries.add(new Entry(6,65));
        lineEntries.add(new Entry(7,65));
        lineEntries.add(new Entry(8,70));
        lineEntries.add(new Entry(9,70));
        lineEntries.add(new Entry(10,70));
        lineEntries.add(new Entry(11,55));
        lineEntries.add(new Entry(12,40));
        lineEntries.add(new Entry(13,40));
        lineEntries.add(new Entry(14,40));
        lineEntries.add(new Entry(15,45));
        lineEntries.add(new Entry(16,50));
        lineEntries.add(new Entry(17,50));
        lineEntries.add(new Entry(18,50));
        lineEntries.add(new Entry(19,58));
        lineEntries.add(new Entry(20,64));
        lineEntries.add(new Entry(21,60));
        lineEntries.add(new Entry(22,60));
        lineEntries.add(new Entry(23,60));

        return lineEntries;
    }
    public  class EnvoyerCommande extends AsyncTask<String, Void, String> {
        private  String _urlApi = "";
        // on below line creating a variable for response line.
        String responseLine = null;
        // on below line creating a string builder.
        StringBuilder response = new StringBuilder();

        @Override
        protected String doInBackground(String... strings) {
            try {

                // on below line creating a url to post the data.
                URL url = new URL("http://192.168.1.15:8081/api/Arduino/EnvoyerCommande");

                // on below line opening the connection.
                HttpURLConnection client = (HttpURLConnection) url.openConnection();

                // on below line setting method as post.
                client.setRequestMethod("POST");

                // on below line setting content type and accept type.
                client.setRequestProperty("Content-Type", "application/json");
                client.setRequestProperty("Accept", "application/json");

                // on below line setting client.
                client.setDoOutput(true);

                // on below line we are creating an output stream and posting the data.
                try (OutputStream os = client.getOutputStream()) {
                    byte[] input = strings[0].getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

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

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    public  class GetPiecesInfos extends AsyncTask<String, Void, String> {
        private  String _urlApi = "";
        // on below line creating a variable for response line.
        String responseLine = null;
        // on below line creating a string builder.
        StringBuilder response = new StringBuilder();

        @Override
        protected String doInBackground(String... strings) {
            try {

                // on below line creating a url to post the data.
                URL url = new URL("http://192.168.1.15:8081/api/Arduino/EnvoyerCommande");

                // on below line opening the connection.
                HttpURLConnection client = (HttpURLConnection) url.openConnection();

                // on below line setting method as post.
                client.setRequestMethod("POST");

                // on below line setting content type and accept type.
                client.setRequestProperty("Content-Type", "application/json");
                client.setRequestProperty("Accept", "application/json");

                // on below line setting client.
                client.setDoOutput(true);

                // on below line we are creating an output stream and posting the data.
                try (OutputStream os = client.getOutputStream()) {
                    byte[] input = strings[0].getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

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

                String json = s.replaceAll("\\\\", "").replaceAll("^\"|\"$", "");
                JSONObject jsonObj = new JSONObject(json);

                temperature.setText(jsonObj.getString("Temperature")+" °C");
                humidite.setText(jsonObj.getString("Humidite")+" °%");
                iqa.setText(jsonObj.getString("indiceQualiteAir"));

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    public  class ObtenirListeTemperature extends AsyncTask<String, Void, String> {
        private  String _urlApi = "";
        // on below line creating a variable for response line.
        String responseLine = null;
        // on below line creating a string builder.
        StringBuilder response = new StringBuilder();

        @Override
        protected String doInBackground(String... strings) {
            try {

                // on below line creating a url to post the data.
                URL url = new URL("http://192.168.1.15:8081/api/Temperatures/ObtenirListeTemperature");

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

                List<Temperature> finalTempList =null;
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                Type founderListType = new TypeToken<ArrayList<Temperature>>(){}.getType();
                List<Temperature>listeTemperature = gson.fromJson(s,founderListType);
                if(listeTemperature.size()>0)
                {
                   listeTemperature.forEach(temperature -> {
                       if (temperature.adresseIp.equals(adresseIp))
                       {
                           lineEntriesTemerature.add(new Entry(temperature.dateAjout.getHours(),(float) temperature.valeur));
                           drawLineChart(lineChartTemperature,"Variation de la température du J-1",lineEntriesTemerature);
                       }


                   });
                }
               // Collection<Temperature> dd = listeTemperature.stream().filter(p->p.AdresseIp ==adresseIp).collect(Collectors.toList());

                String opp = "";
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    public  class ObtenirListeHumidite extends AsyncTask<String, Void, String> {
        private  String _urlApi = "";
        // on below line creating a variable for response line.
        String responseLine = null;
        // on below line creating a string builder.
        StringBuilder response = new StringBuilder();

        @Override
        protected String doInBackground(String... strings) {
            try {

                // on below line creating a url to post the data.
                URL url = new URL("http://192.168.1.15:8081/api/Humidite/ObtenirListeHumidite");

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

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                Type humiditeListType = new TypeToken<ArrayList<Humidite>>(){}.getType();
                List<Humidite>listeHumidite = gson.fromJson(s,humiditeListType);
                if(listeHumidite.size()>0)
                {
                    listeHumidite.forEach(humidite -> {
                        if (humidite.adresseIp.equals(adresseIp))
                        {
                            lineEntriesHumidite.add(new Entry(humidite.dateAjout.getHours(),(float) humidite.taux));
                            drawLineChart(lineChartHumidite,"Variation de l'humidite du J-1",lineEntriesHumidite);
                        }


                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public  class ObtenirListeIqa extends AsyncTask<String, Void, String> {
        private  String _urlApi = "";
        // on below line creating a variable for response line.
        String responseLine = null;
        // on below line creating a string builder.
        StringBuilder response = new StringBuilder();

        @Override
        protected String doInBackground(String... strings) {
            try {

                // on below line creating a url to post the data.
                URL url = new URL("http://192.168.1.15:8081/api/IndiceQualiteAir/ObtenirListeIndiceQualiteAir");

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

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                Type iqaListType = new TypeToken<ArrayList<IndiceQualiteAir>>(){}.getType();
                List<IndiceQualiteAir>listeIndiceQualiteAir = gson.fromJson(s,iqaListType);
                if(listeIndiceQualiteAir.size()>0)
                {
                    listeIndiceQualiteAir.forEach(indiceQualiteAir -> {
                        if (indiceQualiteAir.adresseIp.equals(adresseIp))
                        {
                            lineEntriesIqa.add(new Entry(indiceQualiteAir.dateAjout.getHours(),(float) indiceQualiteAir.valeur));
                            drawLineChart(lineChartIqa,"Variation de la qualité de l'air du J-1",lineEntriesIqa);
                        }


                    });
                }

                String opp = "";
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}