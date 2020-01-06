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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nitoelchidoceti.ciceroneguias.JavaMail.JavaMailAPI;
import com.nitoelchidoceti.ciceroneguias.JavaMail.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetMailForgotPassword extends AppCompatActivity {

    private Button sendMail;
    private TextInputLayout textInputLayoutEmail;
    private TextInputEditText etxtEmail;
    private String emailInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_mail_forgot_password);
        sendMail = findViewById(R.id.btnRegister);
        etxtEmail = findViewById(R.id.etxtCorreo_RecPass);
        textInputLayoutEmail = findViewById(R.id.txt_Correo);
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInput = textInputLayoutEmail.getEditText().getText().toString().trim();
                if (emailInput.isEmpty()){
                    textInputLayoutEmail.setError("Este campo no puede esta vacío");
                }else {
                    comprobarCorreo(emailInput);
                }
            }
        });
    }

    private void comprobarCorreo(String correOngas) {
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/" +
                "Cicerone/PHP/Guia/comprobarCorreo.php?correo="+ correOngas;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            if (jsonObject.getString("success").equals("true")){
                                sendEmail();
                            }else {
                                Toast.makeText(GetMailForgotPassword.this,
                                        "Su correo no esta registrado en Cicerone", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(GetMailForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(GetMailForgotPassword.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }

    private void sendEmail() {

        String verificationCode = Utils.getRandomString(8);//obtiene el codigo de verificacion
        JavaMailAPI javaMailAPI = new JavaMailAPI(GetMailForgotPassword.this ,
                etxtEmail.getText().toString().trim(),
                "Código de verificación Cicerone",
                "Tu código de verificación para recuperar tu contraseña es:\n"+
                        verificationCode);
        javaMailAPI.execute();
        launchRecoverPassword(verificationCode);
    }

    public void launchLogin(View view) {
        Intent launchLog = new Intent(this,LoginActivity.class);
        launchLog.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(launchLog);
    }

    public void launchRecoverPassword(String code) {
        Intent launchRecPass = new Intent(this, RecoverPasswordActivity.class);
        launchRecPass.putExtra("code",code);
        launchRecPass.putExtra("email",emailInput);
        startActivity(launchRecPass);
    }
}
