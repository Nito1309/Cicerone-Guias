package com.nitoelchidoceti.ciceroneguias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nitoelchidoceti.ciceroneguias.Adapters.AdapterDeTextCollapseView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserHelpActivity extends AppCompatActivity {

    private ArrayList<String> titulosProblemas, descripcionesProblemas = new ArrayList<>();
    private AdapterDeTextCollapseView adapterDeTextCollapseView;
    private ExpandableListView expandableListView;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {//desactivo item de ayuda en el toolbar
        menu.getItem(1).setEnabled(false);
        menu.getItem(1).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_help);
        Toolbar toolbar = findViewById(R.id.toolbarUserHelp);
        setSupportActionBar(toolbar);
        titulosProblemas = new ArrayList<>();
        expandableListView=findViewById(R.id.expandableTextViewUserHelp);

        consultarProblemas();
    }
    private void consultarProblemas() {
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/Cicerone/PHP/Guia/problemasRecurrentes.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            mostrarProblemas(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserHelpActivity.this,""+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(UserHelpActivity.this);
        queue.add(jsonArrayRequest);
    }

    private void mostrarProblemas(JSONArray response) throws JSONException {
        titulosProblemas.clear();
        for (int i = 0; i < response.length(); i++) {
            String problema;
            JSONObject objeto;
            objeto = response.getJSONObject(i);
            problema=objeto.getString("Titulo");
            titulosProblemas.add(problema);
            String problema2;
            problema2 = objeto.getString("Descripcion");
            descripcionesProblemas.add(problema2);
            //collapseview
        }
        adapterDeTextCollapseView=new AdapterDeTextCollapseView(titulosProblemas,descripcionesProblemas,UserHelpActivity.this);
        expandableListView.setAdapter(adapterDeTextCollapseView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.panic_button:
                launchPanicActivity();
                break;
        }
        return true;
    }
    private void launchPanicActivity() {
        Intent intent = new Intent(this,PanicButtonActivity.class);
        startActivity(intent);
    }
}
