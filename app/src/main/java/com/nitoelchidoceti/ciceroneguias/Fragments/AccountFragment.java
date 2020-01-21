package com.nitoelchidoceti.ciceroneguias.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.nitoelchidoceti.ciceroneguias.Adapters.AdapterDeComentarios;
import com.nitoelchidoceti.ciceroneguias.Adapters.AdapterDeViewPager;
import com.nitoelchidoceti.ciceroneguias.Global.Global;
import com.nitoelchidoceti.ciceroneguias.LoginActivity;
import com.nitoelchidoceti.ciceroneguias.POJOS.PojoComentario;
import com.nitoelchidoceti.ciceroneguias.POJOS.PojoGuia;
import com.nitoelchidoceti.ciceroneguias.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AccountFragment extends Fragment {

    PojoGuia pojoGuia;
    ArrayList<PojoComentario> comentarios;
    TextView nombreDelGuia, descripcionDeGuiaCompleto, idiomasGuia, infAcademicaGuia,
            duracionTourGuia, horarioGuia, costosGuia, calificacionGuia;
    AdapterDeComentarios adapterDeComentarioGuia;
    RecyclerView recycleComentarioGuia;
    ImageView fotoPerfil;
    ArrayList<String> imagenes;
    ViewPager viewPager;
    Button btnCerrarSesion;
    ImageButton oneStar, twoStar, threeStar, fourStar, fiveStar;

    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account,container,false);

        inicializacion();

        return view;
    }
    /**
     * inicializa todas la variables de la actividad
     * y los configura
     */
    private void inicializacion() {
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
        calificacionGuia = view.findViewById(R.id.txtCalificacionNumeroGuia);
        comentarios = new ArrayList<>();
        recycleComentarioGuia = view.findViewById(R.id.recycleComentariosGuia);
        recycleComentarioGuia.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false));
        nombreDelGuia = view.findViewById(R.id.txtNombreGuia);
        descripcionDeGuiaCompleto = view.findViewById(R.id.txtDescripcionGuia);
        infAcademicaGuia = view.findViewById(R.id.txtInfAcademicaGuia);
        duracionTourGuia = view.findViewById(R.id.txtDuracionTourGuia);
        horarioGuia = view.findViewById(R.id.txtHorarioGuia);
        costosGuia = view.findViewById(R.id.txtCostosTourGuia);
        idiomasGuia=view.findViewById(R.id.txtIdiomasGuia);
        oneStar = view.findViewById(R.id.UnaEstrellaGuia);
        twoStar = view.findViewById(R.id.DosEstrellasGuia);
        threeStar = view.findViewById(R.id.TresEstrellasGuia);
        fourStar = view.findViewById(R.id.CuatroEstrellasGuia);
        fiveStar = view.findViewById(R.id.CincoEstrellasGuia);
        viewPager = view.findViewById(R.id.viewPagerGuia);
        fotoPerfil = view.findViewById(R.id.imgFotoPerfilGuia);
        imagenes = new ArrayList<>();
        pojoGuia = new PojoGuia();
        //POJO GUIA
        pojoGuia.setId(String.valueOf(Global.getObject().getId()));

        consultaTitulos();

    }

    private void cerrarSesion() {
        if (AccessToken.getCurrentAccessToken() == null) {
            Intent launchLoginFromAccount = new Intent(view.getContext(),LoginActivity.class);
            getActivity().finish();
            startActivity(launchLoginFromAccount);
            // already logged out
        }else {
            disconnectFromFacebook(view.getContext());
        }
    }

    public void disconnectFromFacebook(final Context context) {

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
                Intent launchLoginFromAccount = new Intent(context,LoginActivity.class);
                launchLoginFromAccount.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchLoginFromAccount);
            }
        }).executeAsync();
    }

    private void obtenerInfGuia() {
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/Cicerone/PHP/Guia/infDeCuenta.php?id=" +
                pojoGuia.getId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            obtenerImagenes();
                            consultaComentarios();
                            calcularCalificacion();
                            agregarInfGuia(response);
                        } catch (JSONException e) {
                            Toast.makeText(viewPager.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ImageView imageView = view.findViewById(R.id.viewPagerGuia);
                imageView.setImageResource(R.drawable.img_catedral_premium);
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(jsonArrayRequest);
    }

    private void agregarInfGuia(JSONArray response) throws JSONException {
        JSONObject objeto;
        objeto = response.getJSONObject(0);
        pojoGuia.setNombre(objeto.getString("Nombre"));
        pojoGuia.setTelefono(objeto.getString("Telefono"));
        pojoGuia.setDuracion(objeto.getString("Duracion"));
        pojoGuia.setCorreo(objeto.getString("Correo"));
        pojoGuia.setFotografia(objeto.getString("Fotografia"));
        pojoGuia.setNombreDelSitio(objeto.getString("Sitio"));
        pojoGuia.setHorario("De "+objeto.getString("Horario_Inicio")+"hrs a "+objeto.getString("Horario_Final")+"hrs");
        pojoGuia.setFK_Sitio(objeto.getString("FK_Sitio"));
        Double[] aux = new Double[3];
        aux[0] = Double.valueOf(objeto.getString("Ninos"));
        aux[1] = Double.valueOf(objeto.getString("Especial"));
        aux[2] = Double.valueOf(objeto.getString("Adultos"));
        pojoGuia.setCostos(aux);
        llenarInformacion();
    }

    private void obtenerImagenes( ) {
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/Cicerone/PHP/consultaMultimedia.php?esGuia=true&id=" +
                pojoGuia.getId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            llenarImagenes(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ImageView imageView = view.findViewById(R.id.viewPagerGuia);
                imageView.setImageResource(R.drawable.img_catedral_premium);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(jsonArrayRequest);
    }

    private void consultaTitulos() {

        String url2 = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/Cicerone/PHP/titulosGuia.php?FK_guia="+ Global.getObject().getId();
        System.out.println(url2);
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET,
                url2,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            agregarTitulosAlPojo(response);
                            consultaIdiomas();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(view.getContext());
        requestQueue2.add(jsonArrayRequest2);
    }

    private void consultaIdiomas() {
        String url2 = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/Cicerone/PHP/idiomasGuias.php?FK_guia="+Global.getObject().getId();
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET,
                url2,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            agregarIdiomasAlPojo(response);
                            obtenerInfGuia();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(view.getContext());
        requestQueue2.add(jsonArrayRequest2);
    }

    private void agregarIdiomasAlPojo(JSONArray response) throws JSONException {
        final ArrayList<String> idiomas = new ArrayList<>();
        for(int j=0;j<response.length();j++){
            JSONObject jsonObject = response.getJSONObject(j);
            idiomas.add(jsonObject.getString("Idioma"));
        }
        pojoGuia.setIdiomas(idiomas);
    }

    private void agregarTitulosAlPojo(JSONArray response) throws JSONException {
        final ArrayList<String> titulos = new ArrayList<>();
        for(int i=0;i<response.length();i++){
            JSONObject jsonObject = response.getJSONObject(i);
            titulos.add(jsonObject.getString("Especialidad")+" en " +
                    jsonObject.getString("Carrera")+" en el "+
                    jsonObject.getString("Universidad"));
        }
        pojoGuia.setTitulos(titulos);
    }

    private void llenarImagenes(JSONArray response) throws JSONException {
        imagenes.clear();
        for (int i = 0; i < response.length(); i++) {
            JSONObject objeto;
            objeto = response.getJSONObject(i);
            imagenes.add(objeto.getString("Imagen"));
        }
        AdapterDeViewPager adapterDeViewPager = new AdapterDeViewPager(view.getContext(),imagenes);
        viewPager.setAdapter(adapterDeViewPager);
    }

    /**
     * calcula el promedio del Guia
     */
    private void calcularCalificacion() {
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/Cicerone/PHP/promedioCalificacion.php?lugar=" + pojoGuia.getId()+
                "&esSitio=false";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject;
                            jsonObject = response.getJSONObject(0);
                            calificacionGuia.setText(jsonObject.getString("Calificacion"));
                            String s = jsonObject.getString("Calificacion").substring(1,3);
                            Boolean mediaEstrella = esMediaEstrella(s);

                            stars(jsonObject.getString("Calificacion").substring(0,1),mediaEstrella);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(jsonArrayRequest);
    }
    private void stars(String numero,Boolean mediaEstrella) {
        switch (numero) {
            case "1":
                oneStar.setBackgroundResource(R.drawable.ic_one_star);
                if (mediaEstrella==true){
                    twoStar.setBackgroundResource(R.drawable.ic_half_star);
                }else {
                    twoStar.setBackgroundResource(R.drawable.ic__empty_star);
                }

                threeStar.setBackgroundResource(R.drawable.ic__empty_star);
                fourStar.setBackgroundResource(R.drawable.ic__empty_star);
                fiveStar.setBackgroundResource(R.drawable.ic__empty_star);
                break;
            case "2":
                oneStar.setBackgroundResource(R.drawable.ic_one_star);
                twoStar.setBackgroundResource(R.drawable.ic_one_star);
                if (mediaEstrella==true){
                    threeStar.setBackgroundResource(R.drawable.ic_half_star);
                }else {
                    threeStar.setBackgroundResource(R.drawable.ic__empty_star);
                }
                fourStar.setBackgroundResource(R.drawable.ic__empty_star);
                fiveStar.setBackgroundResource(R.drawable.ic__empty_star);
                break;
            case "3":
                oneStar.setBackgroundResource(R.drawable.ic_one_star);
                twoStar.setBackgroundResource(R.drawable.ic_one_star);
                threeStar.setBackgroundResource(R.drawable.ic_one_star);
                if (mediaEstrella==true){
                    fourStar.setBackgroundResource(R.drawable.ic_half_star);
                }else {
                    fourStar.setBackgroundResource(R.drawable.ic__empty_star);
                }
                fiveStar.setBackgroundResource(R.drawable.ic__empty_star);
                break;
            case "4":
                oneStar.setBackgroundResource(R.drawable.ic_one_star);
                twoStar.setBackgroundResource(R.drawable.ic_one_star);
                threeStar.setBackgroundResource(R.drawable.ic_one_star);
                fourStar.setBackgroundResource(R.drawable.ic_one_star);
                if (mediaEstrella==true){
                    fiveStar.setBackgroundResource(R.drawable.ic_half_star);
                }else {
                    fiveStar.setBackgroundResource(R.drawable.ic__empty_star);
                }
                break;
            case "5":
                oneStar.setBackgroundResource(R.drawable.ic_one_star);
                twoStar.setBackgroundResource(R.drawable.ic_one_star);
                threeStar.setBackgroundResource(R.drawable.ic_one_star);
                fourStar.setBackgroundResource(R.drawable.ic_one_star);
                fiveStar.setBackgroundResource(R.drawable.ic_one_star);
                break;
        }
    }
    private Boolean esMediaEstrella(String s) {
        switch (s){
            case ".3":
            case ".4":
            case ".7":
            case ".6":
            case ".5":
                return true;
        }
        return false;
    }

    /**
     * obtiene los comentarios de la DB
     */
    private void consultaComentarios() {
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/Cicerone/PHP/comentariosLugar.php?id_place=" + pojoGuia.getId()+
                "&esSitio=false";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            mostrarComentarios(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),""+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(jsonArrayRequest);
    }


    /**
     * Obtiene los datos y los agrega al arreglo de comentarios
     * para despues mandarles el array
     *
     * @param response
     * @throws JSONException
     */
    private void mostrarComentarios(JSONArray response) throws JSONException {
        comentarios.clear();
        for (int i = 0; i < response.length(); i++) {
            PojoComentario comentario = new PojoComentario();
            JSONObject objeto;
            objeto = response.getJSONObject(i);
            comentario.setComentario(objeto.getString("Descripcion"));
            comentario.setFecha(objeto.getString("Fecha"));
            comentario.setUserName(objeto.getString("Nombre"));
            comentario.setFK_lugar(objeto.getString("FK_Sitios"));
            comentarios.add(comentario);
        }
        adapterDeComentarioGuia = new AdapterDeComentarios(comentarios);
        recycleComentarioGuia.setAdapter(adapterDeComentarioGuia);
    }


    /**
     * Llena la inf de los lugares
     */
    private void llenarInformacion() {
        nombreDelGuia.setText(pojoGuia.getNombre());
        //descripcionDeGuiaCompleto.setText(pojoGuia.getDescripcion());
        duracionTourGuia.setText(pojoGuia.getDuracion());
        String aux = new String();

        for (int i=0;i<pojoGuia.getTitulos().size();i++){
            aux+="* "+pojoGuia.getTitulos().get(i)+"\n";
        }
        infAcademicaGuia.setText(aux);

        String aux2 = new String();

        for (int i=0;i<pojoGuia.getIdiomas().size();i++){
            aux2+="* "+pojoGuia.getIdiomas().get(i)+"\n";
        }
        idiomasGuia.setText(aux2);
        horarioGuia.setText(pojoGuia.getHorario());
        duracionTourGuia.setText("Aproximadamente "+pojoGuia.getDuracion()+ " Hrs.");
        Double[] array;
        array = pojoGuia.getCostos();
        costosGuia.setText("Niños: " + array[0] + " MXN" + "\n" +
                "Estudiantes o 3ra Edad: " + array[1] + " MXN" + "\n" +
                "Adultos: " + array[2] + " MXN");
        fotoPerfil.setBackground(null);
        if (Global.getObject().getImagen() != null){
            Glide.with(view.getContext()).load(Global.getObject().getImagen()).into(fotoPerfil);
        }else {
            Glide.with(view.getContext()).load(pojoGuia.getFotografia()).into(fotoPerfil);
        }
    }

}
