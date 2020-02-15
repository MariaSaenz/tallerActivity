package my.jviracocha.talleractivity.Adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Cita;
import my.jviracocha.talleractivity.R;

public class AdapterClienteReporte extends RecyclerView.Adapter<AdapterClienteReporte.ClienteReporteViewHolder> {

    private ArrayList<Cita> citaArrayList;
    public AdapterClienteReporte(ArrayList<Cita> citaArrayList){
        this.citaArrayList=citaArrayList;
    }
    private OnItemClickListenerReporte onItemClickListenerReporte;
    public interface OnItemClickListenerReporte{
        void onShowReportPDF(int idCita);
    }
    public  void setOnItemClickListener( OnItemClickListenerReporte listener){

        onItemClickListenerReporte=listener;
    }

    @NonNull
    @Override
    public ClienteReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_cliente_gestion_reportes,parent,false);
        ClienteReporteViewHolder mvh=new ClienteReporteViewHolder(vista,onItemClickListenerReporte);
        return  mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteReporteViewHolder holder, int position) {

        holder.fechaCita.setText("FECHA:"+citaArrayList.get(position).getFecha() +" "+citaArrayList.get(position).getHora());
        holder.datoAuto.setText("PLACA:"+citaArrayList.get(position).getPlacaAuto());



    }

    @Override
    public int getItemCount() {
        return citaArrayList.size();
    }

    public static class ClienteReporteViewHolder extends RecyclerView.ViewHolder{
        public TextView fechaCita,datoAuto;
        public Button verReporte;
        public ClienteReporteViewHolder(@NonNull View itemView,final OnItemClickListenerReporte listener){
            super(itemView);

            fechaCita=(TextView)itemView.findViewById(R.id.txtfechaMantenimientoClienteReporte);
            datoAuto=(TextView)itemView.findViewById(R.id.txtPlacaAutoClienteReporte);
            verReporte=(Button)itemView.findViewById(R.id.btnHoraCitaClienteTranajoMecanico);

            verReporte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onShowReportPDF(position);
                        }

                    }
                }
            });
        }
    }
}
