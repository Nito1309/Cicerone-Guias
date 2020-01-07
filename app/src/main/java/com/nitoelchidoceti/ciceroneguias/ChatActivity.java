package com.nitoelchidoceti.ciceroneguias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nitoelchidoceti.ciceroneguias.Adapters.AdapterMensajes;
import com.nitoelchidoceti.ciceroneguias.Global.Global;
import com.nitoelchidoceti.ciceroneguias.POJOS.MensajeEnviar;
import com.nitoelchidoceti.ciceroneguias.POJOS.MensajeRecibir;
import com.nitoelchidoceti.ciceroneguias.POJOS.PojoMensaje;

public class ChatActivity extends AppCompatActivity {

    private com.google.android.material.textfield.TextInputEditText etxtMensaje;
    private RecyclerView recyclerViewMensajes;
    private ImageButton btnEnviarMensaje, btnEnviarImagen;
    private AdapterMensajes adapterMensajes;
    private TextView txtNombreChat;
    private String nombreDestinatario,idDestinatario;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private static final int PHOTO_SEND = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        nombreDestinatario = (String) getIntent().getSerializableExtra("Turista");
        idDestinatario = (String) getIntent().getSerializableExtra("ID");
        Toolbar toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        instancias();
    }

    private void instancias() {

        etxtMensaje=findViewById(R.id.etxtMensaje);
        recyclerViewMensajes=findViewById(R.id.recycleChat);
        btnEnviarMensaje=findViewById(R.id.btnEnviarMsgChat);
        btnEnviarImagen = findViewById(R.id.imgEnviarImagen);
        txtNombreChat=findViewById(R.id.txtNombreChat);
        txtNombreChat.setText(nombreDestinatario);

        databaseConfiguration();
        recycleConfiguration();
        onClickEnviarMensaje();
        onClickEnviarImagen();
    }

    private void onClickEnviarImagen() {
        btnEnviarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//pedir obtener todos archivos
                intent.setType("image/jpeg");//configurar para que solo sean fotos
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);//lo agregas al intent para enviar
                //evaluar el resultado del intent (si se realizo correctamente)
                startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PHOTO_SEND);//1 = imagen
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //cuando la imagen se selecciona correctamente y cuando el resultado se reciba bien
        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {
            Uri uri = data.getData();//subir la img a la db
            storageReference = storage.getReference("imagenes_chat");
            final StorageReference imagenReferencia = storageReference.child(uri.getLastPathSegment());//key de nuestra foto
            UploadTask uploadTask = imagenReferencia.putFile(uri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imagenReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        PojoMensaje mensaje = new MensajeEnviar(
                                "",
                                Global.getObject().getNombre(),"2",
                                downloadUri.toString(),
                                "guia"+Global.getObject().getId(),
                                nombreDestinatario, ServerValue.TIMESTAMP);
                        databaseReference.push().setValue(mensaje);
                    }else {
                        Toast.makeText(ChatActivity.this, "No se ha podido subir la imagen correctamente.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void onClickEnviarMensaje() {
        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etxtMensaje.getText().length()!=0){
                    databaseReference.push().setValue(new MensajeEnviar(etxtMensaje.getText().toString()
                            , Global.getObject().getNombre(), "1",
                            "guia"+Global.getObject().getId(),nombreDestinatario,ServerValue.TIMESTAMP));
                    etxtMensaje.setText("");
                }
            }
        });
    }

    private void recycleConfiguration() {
        adapterMensajes=new AdapterMensajes(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewMensajes.setLayoutManager(linearLayoutManager);
        recyclerViewMensajes.setAdapter(adapterMensajes);
        adapterMensajes.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {//cuando se inserta un nuevo objeto al recycle
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollBar();
            }
        });
    }

    private void databaseConfiguration() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chatTurista_"+ idDestinatario+"_Guia_"+Global.getObject().getId()+"_");//Sala de chat(nombre)
        storage = FirebaseStorage.getInstance();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MensajeRecibir mensaje = dataSnapshot.getValue(MensajeRecibir.class);
                adapterMensajes.addMensaje(mensaje);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setScrollBar() {//scroll la posicion del recycle view al ultimo item agregado
        recyclerViewMensajes.scrollToPosition(adapterMensajes.getItemCount() - 1);
    }

    public static boolean verifyStoragePermissions(ChatActivity activity) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,

        };
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.panic_button){
            //launchPanicActivity();
        }
        if (id == R.id.faqs){
            launchFaqsActivity();
        }
        return true;
    }

    private void launchFaqsActivity() {
        Intent intent = new Intent(this,UserHelpActivity.class);
        startActivity(intent);
    }
}
