package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Servicio;
import my.jviracocha.talleractivity.R;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    private final ArrayList<Servicio> servicioArrayList;
    private OnItemClickListenerServ onItemClickListener;

    public  interface OnItemClickListenerServ{
        void onItemClick(int position);
        void onDeliteItem(int position);
    }
    public  void setOnItemClickListener(OnItemClickListenerServ onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    public ServicioAdapter(ArrayList<Servicio> servicioArrayList) {
        this.servicioArrayList = servicioArrayList;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_servicio,parent,false);
        ServicioViewHolder mvh=new ServicioViewHolder(vista,onItemClickListener); //antes de ser cambiado


        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {

        Servicio servicio=servicioArrayList.get(position);

        holder.nombreServicio.setText(servicioArrayList.get(position).getNombreServicio());
        holder.costoServicio.setText("COSTO: "+Integer.toString(servicioArrayList.get(position).getCostoServicio()));
        holder.kilometrajeTope.setText("KM: "+Integer.toString(servicioArrayList.get(position).getKilometrajeTope()));

    }

    @Override
    public int getItemCount() {
        return servicioArrayList.size();
    }

    public  static class ServicioViewHolder extends RecyclerView.ViewHolder{

        public TextView nombreServicio;
        public TextView costoServicio;
        public TextView kilometrajeTope;
        public  ImageView iconoBorrarSer;

        public ServicioViewHolder(@NonNull View itemView,final OnItemClickListenerServ listenerServ) {
            super(itemView);

            nombreServicio=(TextView)itemView.findViewById(R.id.txtNombreServicio);
            costoServicio=(TextView)itemView.findViewById(R.id.txtCostoServicio);
            kilometrajeTope=(TextView)itemView.findViewById(R.id.txtTopeKilometro);
            iconoBorrarSer=(ImageView)itemView.findViewById(R.id.icono_borrarServicio);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenerServ!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listenerServ.onItemClick(position);
                        }

                    }
                }
            });

            iconoBorrarSer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenerServ!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listenerServ.onDeliteItem(position);
                        }

                    }
                }
            });

        }
    }
}
