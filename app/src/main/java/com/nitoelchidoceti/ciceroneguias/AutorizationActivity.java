package com.nitoelchidoceti.ciceroneguias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioRouting;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.nitoelchidoceti.ciceroneguias.Global.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

public class AutorizationActivity extends AppCompatActivity {

    private TextInputLayout codigo;
    private Button btnVerificar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorization);
        codigo = findViewById(R.id.etxtCodigoAut);
        btnVerificar = findViewById(R.id.btnVerificar);
        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codigo.getEditText().getText().toString().isEmpty()){
                    codigo.setError("Por favor no deje campos vacíos");
                }else {
                    verificarCodigo(codigo.getEditText().getText().toString().trim());
                }
            }
        });
    }

    private void verificarCodigo(String codigo) {
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/" +
                "Cicerone/PHP/Guia/comprobarCodigoDeAut.php?codigo="+codigo+
                "&id="+ Global.getObject().getId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            if (jsonObject.getString("success").equals("true")) {
                                iniciarSesion();
                            }else {
                                Toast.makeText(AutorizationActivity.this, "El " +
                                        "código ingresado es incorrecto.", LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AutorizationActivity.this, " Error: " + error.getMessage(), LENGTH_SHORT).show();
                    }
                });
        RequestQueue ejecuta = Volley.newRequestQueue(AutorizationActivity.this);
        ejecuta.add(jsonArrayRequest);
    }

    private void iniciarSesion() {
        Intent intent = new Intent(AutorizationActivity.this,BottomNav.class);
        finish();
        startActivity(intent);
    }
}
