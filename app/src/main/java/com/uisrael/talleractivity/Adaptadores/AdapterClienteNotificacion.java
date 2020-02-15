package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Notificaciones;
import my.jviracocha.talleractivity.R;

public class AdapterClienteNotificacion extends RecyclerView.Adapter<AdapterClienteNotificacion.ClienteNotificacionViewHolder> {


    private ArrayList<Notificaciones> notificacionesArrayList;
    public AdapterClienteNotificacion(ArrayList<Notificaciones> notificacionesArrayList){
        this.notificacionesArrayList=notificacionesArrayList;
    }

    @NonNull
    @Override
    public ClienteNotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_administrador_notificaciones,parent,false);
        ClienteNotificacionViewHolder nvh= new ClienteNotificacionViewHolder(vista);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteNotificacionViewHolder holder, int position) {
        holder.titulo.setText("Alerta Cliente");
        holder.cuerpo.setText(notificacionesArrayList.get(position).getCuerpo());
        holder.fecha_.setText(notificacionesArrayList.get(position).getFecha());


    }

    @Override
    public int getItemCount() {
        return notificacionesArrayList.size();
    }

    public static class ClienteNotificacionViewHolder extends RecyclerView.ViewHolder{
        public TextView titulo,cuerpo,fecha_;
        public ImageView iconoBorrar;

        public ClienteNotificacionViewHolder(@NonNull View itemView){
            super(itemView);
            titulo=(TextView)itemView.findViewById(R.id.txtTituloNotificacion);
            cuerpo=(TextView)itemView.findViewById(R.id.txtCuerpoNotificacion);
            fecha_=(TextView)itemView.findViewById(R.id.txtfechanotificacion);
            iconoBorrar=(ImageView)itemView.findViewById(R.id.icono_borrarNotificacion);
        }
    }
}
