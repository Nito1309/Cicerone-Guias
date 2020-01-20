package com.nitoelchidoceti.ciceroneguias.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.nitoelchidoceti.ciceroneguias.POJOS.PojoReservacion;
import com.nitoelchidoceti.ciceroneguias.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterDeReservaciones extends RecyclerView.Adapter<AdapterDeReservaciones.FichaHolder> {
    private ArrayList<PojoReservacion> reservaciones;
    private Context context;
    private AdapterDeReservaciones.OnItemClickListener listener;
    private AdapterDeReservaciones.OnMenuItemClickListener menuItemClickListener;

    public AdapterDeReservaciones(ArrayList<PojoReservacion> reservaciones, Context context,
                                  AdapterDeReservaciones.OnItemClickListener listener,
                                  AdapterDeReservaciones.OnMenuItemClickListener menuItemClickListener) {
        this.reservaciones = reservaciones;
        this.context = context;
        this.listener = listener;
        this.menuItemClickListener = menuItemClickListener;
    }
    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public interface OnMenuItemClickListener {
        void OnMenuItemClick(int popMenuPosition, int itemPosition);
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
        holder.asignarDatos(reservaciones.get(position));
        holder.onClickFake(position, listener);
        holder.onMenuItemClick(position, menuItemClickListener);
    }

    @Override
    public int getItemCount() {
        return reservaciones.size();
    }

    public class FichaHolder extends RecyclerView.ViewHolder {
        TextView Titulo, Fecha;
        private ImageButton threePoints;

        public FichaHolder(@NonNull View itemView) {
            super(itemView);
            Titulo = itemView.findViewById(R.id.txtTituloReservacion);
            Fecha = itemView.findViewById(R.id.txtFechaReservacion);
        }

        public void asignarDatos(PojoReservacion reservacion) {
            String fecha = reservacion.getFecha().substring(0,10);
            String hora = reservacion.getFecha().substring(10,16);
            String finalDate = darFormatoFecha(fecha);
            Fecha.setText(finalDate +" at "+ hora+" hrs ");
            Titulo.setText("Reservación con "+ reservacion.getNombre());
            threePoints = itemView.findViewById(R.id.btnThreePoints);

        }

        public void onClickFake(final int posicion, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(posicion);
                }
            });
        }

        public void onMenuItemClick(final int itemPosition, final OnMenuItemClickListener listener) {
            threePoints.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listItemPositionForPopupMenu = listItemPosition;
                    PopupMenu popup = new PopupMenu(context, v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_three_points, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.itemLlamada:
                                    listener.OnMenuItemClick(1, itemPosition);
                                    return true;

                                case R.id.itemMensaje:
                                    listener.OnMenuItemClick(2, itemPosition);
                                    return true;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
        }
        private String darFormatoFecha(String fecha) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd",Locale.getDefault());
            SimpleDateFormat format = new SimpleDateFormat("EEEE MMM dd",Locale.getDefault());

            Date myDate = null;
            try {
                myDate = dateFormat.parse(fecha);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return format.format(myDate);
        }

    }

}
