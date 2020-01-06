package com.nitoelchidoceti.ciceroneguias.Fragments;

import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nitoelchidoceti.ciceroneguias.Adapters.AdapterInbox;
import com.nitoelchidoceti.ciceroneguias.ChatActivity;
import com.nitoelchidoceti.ciceroneguias.Global.Global;
import com.nitoelchidoceti.ciceroneguias.POJOS.MensajeRecibir;
import com.nitoelchidoceti.ciceroneguias.R;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {

    private View view;

    private RecyclerView recyclerViewMensajes;
    private AdapterInbox adapterInbox;

    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private MensajeRecibir mensaje;

    private ArrayList<String> idsTurista = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_messages,container,false);
        recyclerViewMensajes = view.findViewById(R.id.recycle_inbox);

        return view;
    }

    private void databaseConfiguration() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {//recorro todas las conversaciones
                    String[] idGuia = ds.getKey().split("_");//divido el child name por "_"
                    idsTurista.add(idGuia[1]);
                    if (ds.getKey().indexOf("Guia_" + Global.getObject().getId()) != -1) {//filtro las conversaciones del turista
                        Query lastQuery = databaseReference.child(ds.getKey()).orderByChild(ds.getKey()).limitToLast(1);//obtengo el ultimo mensaje enviado de cada conversacion
                        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    mensaje = child.getValue(MensajeRecibir.class);
                                    adapterInbox.addMensaje(mensaje);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(view.getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recycleConfiguration() {
        adapterInbox = new AdapterInbox(this, new AdapterInbox.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                launchChat(position);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewMensajes.setLayoutManager(linearLayoutManager);
        recyclerViewMensajes.setAdapter(adapterInbox);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapterInbox!=null){
            adapterInbox.clearList();
        }
        databaseConfiguration();
        recycleConfiguration();
    }

    private void launchChat(int pos) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);

        if (mensaje.getIdUsuario().equals("guia"+Global.getObject().getId())){//enviar nombre del turista (si el ultimo mensaje lo envio el turista o el guia)
            intent.putExtra("Turista", mensaje.getNombreDestinatario());
        }else {
            intent.putExtra("Turista", mensaje.getNombre());
        }
        intent.putExtra("ID", idsTurista.get(pos));
        startActivity(intent);
    }
}
