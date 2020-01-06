package com.nitoelchidoceti.ciceroneguias.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nitoelchidoceti.ciceroneguias.POJOS.PojoComentario;
import com.nitoelchidoceti.ciceroneguias.R;

import java.util.ArrayList;

public class AdapterDeComentarios extends RecyclerView.Adapter<AdapterDeComentarios.FichaHolder> {
    public ArrayList<PojoComentario> datos;

    public AdapterDeComentarios(ArrayList<PojoComentario> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public AdapterDeComentarios.FichaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = View.inflate(parent.getContext(), R.layout.recycle_comentarios, null);
        FichaHolder respect = new FichaHolder(vista);
        return respect;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDeComentarios.FichaHolder holder, int position) {
        holder.asignarDatos(datos.get(position));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class FichaHolder extends RecyclerView.ViewHolder {
        TextView nombreUser, comentario, fecha;

        public FichaHolder(@NonNull View itemView) {
            super(itemView);
            nombreUser = itemView.findViewById(R.id.txtUserComentario);
            comentario = itemView.findViewById(R.id.txtComentario);
            fecha = itemView.findViewById(R.id.txtFechaComentario);
        }

        public void asignarDatos(PojoComentario pojoComentario) {
            nombreUser.setText(pojoComentario.getUserName());
            fecha.setText(pojoComentario.getFecha()+" ");
            comentario.setText(pojoComentario.getComentario());
        }
    }
}
