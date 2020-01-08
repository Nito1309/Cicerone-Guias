package com.nitoelchidoceti.ciceroneguias.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nitoelchidoceti.ciceroneguias.Adapters.AdapterDeReservaciones;
import com.nitoelchidoceti.ciceroneguias.Global.Global;
import com.nitoelchidoceti.ciceroneguias.POJOS.PojoReservacion;
import com.nitoelchidoceti.ciceroneguias.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleFragment extends Fragment {
    private View view;
    private ArrayList<PojoReservacion> reservaciones = new ArrayList<>();
    RecyclerView recycleReservaciones ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule,container,false);
        recycleReservaciones = view.findViewById(R.id.recycle_schedule);
        recycleReservaciones.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false));
        consultarReservaciones();
        return view;
    }

    private void consultarReservaciones() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(new Date());
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/" +
                "Cicerone/PHP/Guia/consultaReservaciones.php?guia=" + Global.getObject().getId() +
                "&fecha="+dateString;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            agregarReservaciones(response);

                        } catch (JSONException e) {
                            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(jsonArrayRequest);
    }

    private void agregarReservaciones(JSONArray response) throws JSONException {
        AdapterDeReservaciones adapterDeReservaciones;

        if (reservaciones.size()!=0){
            reservaciones.clear();
        }
        for (int i = 0; i < response.length(); i++) {

            JSONObject objeto;
            objeto = response.getJSONObject(i);
            String fecha,titulo,telefono,registro;
            fecha = objeto.getString("Fecha");
            titulo = "Reservación con "+objeto.getString("Nombre");
            telefono = objeto.getString("Telefono");
            registro= objeto.getString("PK_Registro");
            PojoReservacion reservacion = new PojoReservacion(fecha,titulo,registro,telefono) ;
            reservaciones.add(reservacion);
        }
        adapterDeReservaciones = new AdapterDeReservaciones(reservaciones,view.getContext());
        recycleReservaciones.setAdapter(adapterDeReservaciones);
    }
}
