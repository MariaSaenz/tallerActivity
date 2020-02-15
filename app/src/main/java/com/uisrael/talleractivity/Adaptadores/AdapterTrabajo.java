package my.jviracocha.talleractivity.Adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Mantenimiento;
import my.jviracocha.talleractivity.R;

public class AdapterTrabajo extends RecyclerView.Adapter<AdapterTrabajo.TrabajoViewHolder> {


    private ArrayList<Mantenimiento> mantenimientoArrayList;
    public  AdapterTrabajo(ArrayList<Mantenimiento> mantenimientoArrayList){
        this.mantenimientoArrayList=mantenimientoArrayList;
    }

    OnItemClickListenerTrabajo listenerTrabajo;
    public interface OnItemClickListenerTrabajo{
        void  onSaveObsSwitch(int position,String observacion);
    }

    public void setOnClickListener(OnItemClickListenerTrabajo listenerTrabajo){

        this.listenerTrabajo=listenerTrabajo;
    }

    @NonNull
    @Override
    public TrabajoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mecanico_obs_trabajo,parent,false);
        TrabajoViewHolder mvh=new TrabajoViewHolder(vista,listenerTrabajo);
        return  mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrabajoViewHolder holder, final int position) {
        holder.nombreMantenimiento.setText(mantenimientoArrayList.get(position).getNombre());
        holder.observaciones.setText(mantenimientoArrayList.get(position).getObservacionesMecanico());

       /* holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("salida", "fue cambiando el estado"+position );
                    holder.observaciones.setEnabled(false);

                } else {

                    Log.d("salida", "no ha sido cambiado el estado" );
                }

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mantenimientoArrayList.size();
    }

    public static class TrabajoViewHolder extends RecyclerView.ViewHolder{

        public TextView nombreMantenimiento;
        public EditText observaciones;
        public Switch aSwitch;

        public TrabajoViewHolder(@NonNull View itemView,final OnItemClickListenerTrabajo listenerTrabajo){
            super(itemView);
            nombreMantenimiento=(TextView)itemView.findViewById(R.id.txtnombreMantenimientoTrabajo);
            observaciones=(EditText)itemView.findViewById(R.id.txtObseracionesMantenimientoTrabajo);
            aSwitch=(Switch)itemView.findViewById(R.id.switchTrabajoMecanico);

            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if(listenerTrabajo!=null){
                            int position=getAdapterPosition();
                            if(position!= RecyclerView.NO_POSITION){
                                listenerTrabajo.onSaveObsSwitch(position,observaciones.getText().toString() );
                                //Log.d("salida", "desbloque desde el adapter"+observaciones.getText().toString() );
                                observaciones.setEnabled(false);
                            }

                        }

                   }else {

                        Log.d("salida", "desbloque desde el adapter" );
                        observaciones.setEnabled(true);
                    }


                }
            });
        }
    }
}
