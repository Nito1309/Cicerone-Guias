package com.nitoelchidoceti.ciceroneguias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.nitoelchidoceti.ciceroneguias.JavaMail.JavaMailAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class RecoverPasswordActivity extends AppCompatActivity {

    private TextInputLayout verificationCode;
    private Button btnVerificationCode;
    private static String code , email;
    private JavaMailAPI javaMailAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        email=getIntent().getSerializableExtra("email").toString();
        verificationCode = findViewById(R.id.etxtVerificationCode);
        code = getIntent().getSerializableExtra("code").toString();
        btnVerificationCode = findViewById(R.id.btnVerificationCode);
        btnVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarCodigo();
            }
        });
    }

    private void verificarCodigo() {
        if (verificationCode.getEditText().getText().toString().equals(code)){
            obtenerContraseña();
        }else {
            verificationCode.setError("El código ingresado es incorrecto");
        }
    }

    private void obtenerContraseña() {
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/" +
                "Cicerone/PHP/obtenerContrasena.php?correo="+email;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            sendEmail(jsonObject.getString("Contrasena"));

                        } catch (JSONException e) {
                            Toast.makeText(RecoverPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(RecoverPasswordActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }

    private void sendEmail(String contraseña) throws ExecutionException, InterruptedException {
         javaMailAPI = new JavaMailAPI(RecoverPasswordActivity.this ,
                email,
                "Recuperacion de contraseña de Cicerone",
                "La contraseña de tu cuenta cicerone es:\n"+
                        contraseña);
        javaMailAPI.execute().get();
        launchLogin2();
    }



    public void launchLogin2() {
        Intent iToLogin = new Intent(this, LoginActivity.class);
        iToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iToLogin);
    }
}
