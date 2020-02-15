package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Cita;
import my.jviracocha.talleractivity.R;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialAdapterViewHolder>  {

    ArrayList<Cita> citaArrayList;
    public HistorialAdapter(ArrayList<Cita> citaArrayList){
        this.citaArrayList=citaArrayList;

    }

    private  onItemClickListenerHistory listenerHistory;
    public interface onItemClickListenerHistory{
        public  void  onItemClik(int position);
    }
    public  void setonItemClickListenerHistory(onItemClickListenerHistory listenerHistory){
        this.listenerHistory=listenerHistory;
    }
    public  void setOnItemClickListener(onItemClickListenerHistory listener){

       listenerHistory=listener;
    }
    @NonNull
    @Override
    public HistorialAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mantenimientos_agendados,parent,false);
        //MecanicoViewHolder mvh=new MecanicoViewHolder(vista) antes de ser cambiado

        HistorialAdapterViewHolder mvh=new HistorialAdapterViewHolder(vista,listenerHistory);
        return  mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialAdapterViewHolder holder, int position) {

        holder.fecha.setText(citaArrayList.get(position).getFecha());
        holder.hora.setText(citaArrayList.get(position).getHora());
        holder.auto.setText(citaArrayList.get(position).getMarcaAuto());


    }

    @Override
    public int getItemCount() {
        return citaArrayList.size();
    }

    public static class HistorialAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView fecha,hora,auto;

        public HistorialAdapterViewHolder(@NonNull View itemView,final onItemClickListenerHistory listenerHistory){
            super(itemView);

            fecha=(TextView) itemView.findViewById(R.id.lblFechaMantenimientoAgendado);
            hora=(TextView)itemView.findViewById(R.id.lblhoraMantenimientoAgendado);
            auto=(TextView)itemView.findViewById(R.id.lblAutoMantenimientoAgendado);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenerHistory!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listenerHistory.onItemClik(position);
                        }

                    }
                }
            });

        }
    }
}
