package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Mantenimiento;
import my.jviracocha.talleractivity.R;

public class AdapterMantenimientoVisualizacion extends RecyclerView.Adapter<AdapterMantenimientoVisualizacion.MantenimientoVisualizacionViewHolder> {

    private ArrayList<Mantenimiento> mantenimientoArrayList;
    public AdapterMantenimientoVisualizacion(ArrayList<Mantenimiento> mantenimientos){

        this.mantenimientoArrayList=mantenimientos;
    }

    @NonNull
    @Override
    public MantenimientoVisualizacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mecanico_viewmantenimiento,parent,false);
        MantenimientoVisualizacionViewHolder  mvh=new MantenimientoVisualizacionViewHolder (vista);
        return  mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MantenimientoVisualizacionViewHolder holder, int position) {
        holder.nombreMantenimiento.setText(mantenimientoArrayList.get(position).getNombre());
        holder.observacionesMantenimiento.setText(mantenimientoArrayList.get(position).getObservacionesMecanico());

    }

    @Override
    public int getItemCount() {
        return mantenimientoArrayList.size();
    }

    public  static class MantenimientoVisualizacionViewHolder extends RecyclerView.ViewHolder{
        public TextView nombreMantenimiento;
        public TextView observacionesMantenimiento;

        public MantenimientoVisualizacionViewHolder(@NonNull View itemView){
            super(itemView);

            nombreMantenimiento=(TextView)itemView.findViewById(R.id.txtnombreMantenimientoVisualizacion);
            observacionesMantenimiento=(TextView)itemView.findViewById(R.id.txtObservacionesMantenimientoVisualizacion);

        }
    }
}
