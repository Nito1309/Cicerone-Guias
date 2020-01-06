package com.nitoelchidoceti.ciceroneguias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputLayout;
import com.nitoelchidoceti.ciceroneguias.Global.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputEmail, textInputPassword;
    private LoginButton loginButtonFb;
    private CallbackManager callbackManager;
    private EditText txtEmail, txtName, txtPass;
    public String ID;
    private ImageView imgPortada;
    private Button btnIniciarSesion;
    private TextView olvidoDeContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputPassword = findViewById(R.id.text_input_password);
        textInputPassword.setCounterMaxLength(14);
        textInputPassword.setCounterEnabled(true);
        textInputPassword.setNextFocusDownId(R.id.btnLogin);
        imgPortada = findViewById(R.id.imglogin);
        loginButtonFb = findViewById(R.id.login_button_fb);
        btnIniciarSesion = findViewById(R.id.btnLogin);
        txtName = findViewById(R.id.etxt_contraseña_login);
        txtEmail = findViewById(R.id.etxt_correo_login);
        olvidoDeContra = findViewById(R.id.txtOlvidoDeContra);
        olvidoDeContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_get_email();
            }
        });
        txtPass = findViewById(R.id.etxt_contraseña_login);
        callbackManager = CallbackManager.Factory.create();
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput();
            }
        });
        checkLoginStatus();

        loginButtonFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void comprobarCredenciales() {
        final String url = "http://ec2-54-245-18-174.us-west-2.compute.amazonaws.com/Cicerone/PHP/Guia/login.php?correo=" + txtEmail.getText().toString().trim() +
                "&contraseña=" + txtPass.getText().toString().trim();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject loginTurista = response.getJSONObject(0);
                            if (loginTurista.getString("success").equals("false")) {
                                Toast.makeText(LoginActivity.this, "Verifique sus credenciales.", LENGTH_SHORT).show();
                            } else {
                                ID = loginTurista.getString("id");
                                Global.getObject().setId(ID);
                                Global.getObject().setNombre(loginTurista.getString("Nombre"));
                                iniciarSesion();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Si estas concectado a la red? Error: " + error.getMessage(), LENGTH_SHORT).show();
                        System.out.println("Checa: "+ error.getMessage());
                    }
                });
        RequestQueue ejecuta = Volley.newRequestQueue(LoginActivity.this);
        ejecuta.add(jsonArrayRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {                  //SI NO FUNCA*******

                Toast.makeText(LoginActivity.this, "User Loged Out", Toast.LENGTH_SHORT).show();
            } else {
                loadUserProfile(currentAccessToken);
                Toast.makeText(LoginActivity.this, "Entró aquí", LENGTH_SHORT).show();
            }
        }
    };


    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) { //OBTENIENDO LOS STRINGS PREVIAMENTE DEFINIDOS
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");

                    Toast.makeText(LoginActivity.this, "Bienvenido: " + first_name, LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.getMessage(), LENGTH_SHORT).show();
                }
            }
        });
        Bundle paraemters = new Bundle();
        paraemters.putString("fields", "first_name, last_name");//LO QUE SE VA A OBTENER DE FACEBOOK
        request.setParameters(paraemters);
        request.executeAsync();
    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }



    private void iniciarSesion() {
        Intent intent = new Intent(LoginActivity.this,BottomNav.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            textInputEmail.setError("El correo no puede estar vacío");
            return false;
        } else {
            textInputEmail.setError(null);
            textInputEmail.setErrorEnabled(false);//DESABILITA LOS POSIBLES ERRORES
            return true;
        }

    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("La contraseña no puede estar vacía");
            return false;
        } else if (passwordInput.length() > 13) {
            textInputPassword.setError("Contraseña demasiado Larga");
            return false;
        } else {
            textInputPassword.setError(null);
            textInputPassword.setErrorEnabled(false);//DESABILITA LOS POSIBLES ERRORES
            return true;
        }

    }

    public void confirmInput() {//METODO ONCLICK DEL BOTON INICIAR SESIÓN
        if (!validateEmail() | !validatePassword()) {
            return;
        }
        comprobarCredenciales();
    }

    public void launch_get_email() {
        Intent getEMailAct = new Intent(this, GetMailForgotPassword.class);
        startActivity(getEMailAct);
    }
}