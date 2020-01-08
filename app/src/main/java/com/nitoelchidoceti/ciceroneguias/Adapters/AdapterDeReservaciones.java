package com.nitoelchidoceti.ciceroneguias.Adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nitoelchidoceti.ciceroneguias.POJOS.PojoReservacion;
import com.nitoelchidoceti.ciceroneguias.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterDeReservaciones extends RecyclerView.Adapter<AdapterDeReservaciones.FichaHolder> {
    private ArrayList<PojoReservacion> reservaciones;
    private Context context;
    public AdapterDeReservaciones(ArrayList<PojoReservacion> reservaciones, Context context ){
        this.reservaciones = reservaciones;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterDeReservaciones.FichaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.card_view_schedule,parent,false);
        FichaHolder respecto = new FichaHolder(vista);
        return respecto;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDeReservaciones.FichaHolder holder, int position) {
        try {
            holder.asignarDatos(reservaciones.get(position));
        } catch (ParseException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return reservaciones.size();
    }

    public class FichaHolder extends RecyclerView.ViewHolder {
        TextView Titulo, Fecha;

        public FichaHolder(@NonNull View itemView) {
            super(itemView);
            Titulo = itemView.findViewById(R.id.txtTituloReservacion);
            Fecha = itemView.findViewById(R.id.txtFechaReservacion);
        }

        public void asignarDatos(PojoReservacion reservacion) throws ParseException {
            String fecha = "El "+ reservacion.getFecha();
            //Date date = java.text.DateFormat.getDateInstance().parse(fecha);
            //PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
            //Fecha.setText(prettyTime.format(date));
            Fecha.setText(fecha);
            Titulo.setText(reservacion.getTitulo());
            System.out.println("popo: "+reservacion.getTitulo()+"  "+ fecha+"\n");
        }
    }
}
