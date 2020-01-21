package com.nitoelchidoceti.ciceroneguias.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.nitoelchidoceti.ciceroneguias.ChatActivity;
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
    private RecyclerView recycleReservaciones ;

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
                            //Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(view.getContext(), "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
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
            String fecha,nombre,telefono,registro;
            fecha = objeto.getString("Fecha");
            nombre = objeto.getString("Nombre");
            telefono = objeto.getString("Telefono");
            registro= objeto.getString("PK_Registro");
            PojoReservacion reservacion = new PojoReservacion(fecha,nombre,registro,telefono) ;
            reservaciones.add(reservacion);
        }
        adapterDeReservaciones = new AdapterDeReservaciones(reservaciones, view.getContext(), new AdapterDeReservaciones.OnItemClickListener() {
            @Override
            public void OnItemClick(final int position) {
                final PojoReservacion reservacion = reservaciones.get(position);
            }
        }, new AdapterDeReservaciones.OnMenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int popMenuPosition,int itemPosition) {
                switch (popMenuPosition){
                    case 1://LLAMADA
                        if (ContextCompat.checkSelfPermission(view.getContext(),
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CALL_PHONE}, 1);

                            if (ContextCompat.checkSelfPermission(view.getContext(),
                                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(view.getContext(), "Tienes que habilitar el permiso para poder llamar", Toast.LENGTH_SHORT).show();
                            }else {
                                String dial = "tel:" + reservaciones.get(itemPosition).getTelefono();
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                            }
                        }else {
                            String dial = "tel:" + reservaciones.get(itemPosition).getTelefono();
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                        }
                        break;
                    case 2://MENSAJE
                        launchSendMessage(reservaciones.get(itemPosition).getRegistroTurista(),
                                reservaciones.get(itemPosition).getNombre());
                        break;
                }



            }
        });
        recycleReservaciones.setAdapter(adapterDeReservaciones);
    }

    private void launchSendMessage(String registroTurista, String nombre) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        intent.putExtra("Turista",nombre);
        intent.putExtra("ID", registroTurista);
        startActivity(intent);
    }
}
