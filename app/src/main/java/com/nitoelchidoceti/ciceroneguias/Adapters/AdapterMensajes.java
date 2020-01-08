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
import com.nitoelchidoceti.ciceroneguias.Global.Global;
import com.nitoelchidoceti.ciceroneguias.POJOS.MensajeRecibir;
import com.nitoelchidoceti.ciceroneguias.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterMensajes extends RecyclerView.Adapter<AdapterMensajes.HolderMensajes> {

    private List<MensajeRecibir> mensajes = new ArrayList<>();
    private Context context;

    public AdapterMensajes(Context context) {
        this.context = context;
    }

    public void addMensaje(MensajeRecibir mensaje) {
        mensajes.add(mensaje);
        notifyItemInserted(mensajes.size());
    }

    @NonNull
    @Override
    public HolderMensajes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {//si el mensaje es nuestro usa esa card view
            view = LayoutInflater.from(context).inflate(R.layout.card_view_emisor, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.card_view_receptor, parent, false);
        }
        return new HolderMensajes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensajes holder, int position) {
        holder.getNombre().setText(mensajes.get(position).getNombre());
        holder.getMensaje().setText(mensajes.get(position).getMensaje());
        if (mensajes.get(position).getType_mensaje().equals("2")) {
            Glide.with(context).load(mensajes.get(position).getUrlFoto()).into(holder.imgMensaje);
        }
        Long codigoHora = mensajes.get(position).getHora();
        Date date = new Date(codigoHora);
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd HH:mm");
        holder.getHora().setText(sdf.format(date));*/
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        holder.getHora().setText(prettyTime.format(date));
}

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    @Override
    public int getItemViewType(int position) {//conocer si el mensaje es nuestro de aqui se va al on create view holder
        //return super.getItemViewType(position);
        if (mensajes.get(position).getNombre() != null) {//por si todavia no cargan los datos
            if (mensajes.get(position).getIdUsuario().equals("guia" + Global.getObject().getId())) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public class HolderMensajes extends RecyclerView.ViewHolder {
        private TextView nombre, hora, mensaje;
        public ImageView imgMensaje;


        public HolderMensajes(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombreCardView);
            hora = itemView.findViewById(R.id.txtFechaCardView);
            mensaje = itemView.findViewById(R.id.txtMensajeCardView);
            imgMensaje = itemView.findViewById(R.id.imagenChat);
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
    }
}
