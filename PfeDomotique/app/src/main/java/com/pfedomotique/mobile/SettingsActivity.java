package com.pfedomotique.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    EditText adresseIpEditText;
    Button sauvegarderBtn;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String IotIpKey = "iotIpKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        adresseIpEditText=(EditText)findViewById(R.id.editText);

        sauvegarderBtn=(Button)findViewById(R.id.buttonSauvegarder);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sauvegarderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n  = adresseIpEditText.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(IotIpKey, n);
                editor.commit();
                Toast.makeText(SettingsActivity.this,"Adresse sauvegardeé",Toast.LENGTH_LONG).show();
            }
        });
    }
}