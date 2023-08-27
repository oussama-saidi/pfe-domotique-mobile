package com.pfedomotique.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pfedomotique.mobile.utils.PostData;

import org.json.JSONObject;
public class SignUpActivity extends AppCompatActivity {
private Button createAccountButton;
private EditText nomTxt;
private EditText prenomTxt;
private EditText emailTxt;
private EditText motPasseTxt;
private PostData postData = new PostData("http://192.168.1.15:8081/api/Compte/AjouterCompte");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        createAccountButton = findViewById(R.id.createAccount);
        nomTxt = findViewById(R.id.nomTxt);
        prenomTxt = findViewById(R.id.prenomTxt);
        emailTxt = findViewById(R.id.emailTxt);
        motPasseTxt = findViewById(R.id.motPasseTxt);

        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        findViewById(R.id.textSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),loginActivity.class));
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("nom", nomTxt.getText().toString());
                        jsonObject.put("prenom", prenomTxt.getText().toString());
                        jsonObject.put("email", emailTxt.getText().toString());
                        jsonObject.put("motDePasse", motPasseTxt.getText().toString());
                        String jsonString = jsonObject.toString();
                        postData.execute(jsonString);
                        Toast.makeText(SignUpActivity.this, "Votre compte a bien été créé.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this,loginActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, "Erreur de création du compte.", Toast.LENGTH_SHORT).show();

                    }


            }
        });
    }

}