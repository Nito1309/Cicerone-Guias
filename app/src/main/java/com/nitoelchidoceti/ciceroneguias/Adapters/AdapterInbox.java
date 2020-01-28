package com.nitoelchidoceti.ciceroneguias.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nitoelchidoceti.ciceroneguias.Fragments.MessagesFragment;
import com.nitoelchidoceti.ciceroneguias.Global.Global;
import com.nitoelchidoceti.ciceroneguias.POJOS.MensajeRecibir;
import com.nitoelchidoceti.ciceroneguias.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterInbox extends RecyclerView.Adapter<AdapterInbox.HolderMensajes> {

    private List<MensajeRecibir> mensajes = new ArrayList<>();
    private Context context;
    private AdapterInbox.OnItemClickListener listener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public AdapterInbox(MessagesFragment context, OnItemClickListener listener) {
        this.context = context.getContext();
        this.listener = listener;
    }

    public void addMensaje(MensajeRecibir mensaje) {
        mensajes.add(mensaje);
        notifyItemInserted(mensajes.size());
    }

    public void clearList() {
        int size = mensajes.size();
        mensajes.clear();
        notifyItemRangeRemoved(0, size);
    }

    @NonNull
    @Override
    public HolderMensajes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.recycle_inbox, parent, false);
        return new HolderMensajes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensajes holder, int position) {
        if(mensajes.get(position).getIdUsuario().equals("guia"+ Global.getObject().getId())){
            holder.getNombre().setText(mensajes.get(position).getNombreDestinatario());
        }else {
            holder.getNombre().setText(mensajes.get(position).getNombre());
        }

        holder.getMensaje().setText(mensajes.get(position).getMensaje());
        Long codigoHora = mensajes.get(position).getHora();
        Date date = new Date(codigoHora);
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        holder.getHora().setText(prettyTime.format(date));
        holder.onClickFake(position, listener);
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public class HolderMensajes extends RecyclerView.ViewHolder {
        private TextView nombre, hora, mensaje;
        private ImageView imgMensaje;


        public HolderMensajes(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombreInbox);
            hora = itemView.findViewById(R.id.txtFechaInbox);
            mensaje = itemView.findViewById(R.id.txtMsgInbox);
            imgMensaje= itemView.findViewById(R.id.imgPerfilInbox);
        }

        public void onClickFake(final int posicion, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(posicion);
                }
            });
        }

        public TextView getNombre() {
            return nombre;
        }

        public void setNombre(TextView nombre) {
            this.nombre = nombre;
        }

        public TextView getHora() {
            return hora;
        }

        public void setHora(TextView hora) {
            this.hora = hora;
        }

        public TextView getMensaje() {
            return mensaje;
        }

        public void setMensaje(TextView mensaje) {
            this.mensaje = mensaje;
        }

        public ImageView getImgMensaje() {
            return imgMensaje;
        }

        public void setImgMensaje(ImageView imgMensaje) {
            this.imgMensaje = imgMensaje;
        }
    }
}
