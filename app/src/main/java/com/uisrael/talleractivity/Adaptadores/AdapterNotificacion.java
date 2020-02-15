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

public class AdapterNotificacion extends RecyclerView.Adapter<AdapterNotificacion.NotificacionViewHolder> {


    OnItemClicListenerNotification onItemClicListenerNotificationListener;
    public interface OnItemClicListenerNotification{
        void onDeliteClickNotificacion(int position);
    }

    private ArrayList <Notificaciones> notificacionesArrayList;
    public AdapterNotificacion(ArrayList <Notificaciones> notificacionesArrayList){
        this.notificacionesArrayList=notificacionesArrayList;
    }

    public void setOnItemClicListenerNotificationListener(OnItemClicListenerNotification listener){

        onItemClicListenerNotificationListener=listener;
    }

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_administrador_notificaciones,parent,false);
        NotificacionViewHolder nvh= new NotificacionViewHolder(vista,onItemClicListenerNotificationListener);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {

        holder.titulo.setText(notificacionesArrayList.get(position).getTitulo());
        holder.cuerpo.setText(notificacionesArrayList.get(position).getCuerpo());
        holder.fecha_.setText(notificacionesArrayList.get(position).getFecha());
    }

    @Override
    public int getItemCount() {
        return notificacionesArrayList.size();
    }

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder{
        public TextView titulo,cuerpo,fecha_;
        public ImageView iconoBorrar;

        public NotificacionViewHolder(@NonNull View itemView,final OnItemClicListenerNotification listenerNotification){
            super(itemView);
            titulo=(TextView)itemView.findViewById(R.id.txtTituloNotificacion);
            cuerpo=(TextView)itemView.findViewById(R.id.txtCuerpoNotificacion);
            fecha_=(TextView)itemView.findViewById(R.id.txtfechanotificacion);
            iconoBorrar=(ImageView)itemView.findViewById(R.id.icono_borrarNotificacion);

            iconoBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenerNotification!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            //***********cambio del llamado al metodo
                            listenerNotification.onDeliteClickNotificacion(position);
                            //***************************************
                        }

                    }
                }
            });

        }
    }
}
