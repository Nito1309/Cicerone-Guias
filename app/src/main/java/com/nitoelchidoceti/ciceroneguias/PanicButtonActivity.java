package com.nitoelchidoceti.ciceroneguias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PanicButtonActivity extends AppCompatActivity {

    private TextView fila1Columna1,fila1Columna2,fila2Columna1,fila2Columna2,fila3Columna1,fila3Columna2;
    private ArrayList<Boton> botones = new ArrayList<>();

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {//desactivo item de panico en el toolbar
        menu.getItem(0).setEnabled(false);
        menu.getItem(0).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_button);
        inicializaciones();
        obtenerBotones();
        onClicks();
    }

    private void onClicks() {
        fila1Columna1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fila1Columna2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fila2Columna1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fila2Columna2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fila3Columna1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fila3Columna2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void inicializaciones() {
        Toolbar toolbar = findViewById(R.id.toolbar_panic_button);
        setSupportActionBar(toolbar);
        fila1Columna1= findViewById(R.id.columna1Fila1);
        fila1Columna2= findViewById(R.id.columna2Fila1);
        fila2Columna1 = findViewById(R.id.columna1Fila2);
        fila2Columna2 = findViewById(R.id.columna2Fila2);
        fila3Columna1 = findViewById(R.id.columna1Fila3);
        fila3Columna2 = findViewById(R.id.columna2Fila3);
    }

    private void obtenerBotones() {
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/Cicerone/PHP/Guia/panicButtons.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            agregarBotones(response);
                        } catch (JSONException e) {
                            Toast.makeText(PanicButtonActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PanicButtonActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }

    private void agregarBotones(JSONArray response) throws JSONException {
        JSONObject objeto;
        for (int i=0;i<response.length();i++){
            objeto = response.getJSONObject(i);
            Boton boton = new Boton(objeto.getString("Nombre"),
                    objeto.getString("Telefono"));
            botones.add(boton);
        }
        objeto = response.getJSONObject(0);
        fila1Columna1.setText(objeto.getString("Nombre"));
        objeto = response.getJSONObject(1);
        fila1Columna2.setText(objeto.getString("Nombre"));
        objeto = response.getJSONObject(2);
        fila2Columna1.setText(objeto.getString("Nombre"));
        objeto = response.getJSONObject(3);
        fila2Columna2.setText(objeto.getString("Nombre"));
        objeto = response.getJSONObject(4);
        fila3Columna1.setText(objeto.getString("Nombre"));
        objeto = response.getJSONObject(5);
        fila3Columna2.setText(objeto.getString("Nombre"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.faqs){
            launchFaqsActivity();
        }
        return true;
    }

    private void launchFaqsActivity() {
        Intent intent = new Intent(this,UserHelpActivity.class);
        startActivity(intent);
    }

    private class Boton{
        private String Nombre,Telefono;

        private Boton(String nombre, String telefono) {
            Nombre = nombre;
            Telefono = telefono;
        }

        public String getNombre() {
            return Nombre;
        }

        public void setNombre(String nombre) {
            Nombre = nombre;
        }

        public String getTelefono() {
            return Telefono;
        }

        public void setTelefono(String telefono) {
            Telefono = telefono;
        }
    }
}
