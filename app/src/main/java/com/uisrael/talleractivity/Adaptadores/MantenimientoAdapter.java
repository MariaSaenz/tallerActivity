package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Cita;
import my.jviracocha.talleractivity.R;

public class MantenimientoAdapter extends RecyclerView.Adapter<MantenimientoAdapter.MantenimientoViewHolder> {

    private  setOnClikImgListener setOnClikImgListener;
    public interface setOnClikImgListener{

        public  void  okClikListPartes(int position);
        public  void  onClickDeliteCitaAdendada(int position);
        public  void  onClickUpdateCitaMantenimiento(int position);
    }

    public void  setOnItemClickListener(setOnClikImgListener listener){
        this.setOnClikImgListener=listener;

    }

    private ArrayList<Cita> citaArrayList;
    public  MantenimientoAdapter(ArrayList<Cita> citaArrayList){
        this.citaArrayList=citaArrayList;
    }
    @NonNull
    @Override
    public MantenimientoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mantenimiento,parent,false);
        //MecanicoViewHolder mvh=new MecanicoViewHolder(vista) antes de ser cambiado

        MantenimientoViewHolder mvh=new MantenimientoViewHolder(vista,setOnClikImgListener);
        return  mvh;

    }

    @Override
    public void onBindViewHolder(@NonNull MantenimientoViewHolder holder, int position) {

        holder.fechaMantenimiento.setText(citaArrayList.get(position).getFecha());
        holder.horaMantenimiento.setText(citaArrayList.get(position).getHora());
        holder.mecanicoAsignado.setText(citaArrayList.get(position).getNombreMecanico());
        holder.marcaAuto.setText(citaArrayList.get(position).getMarcaAuto());
        holder.placaAuto.setText(citaArrayList.get(position).getPlacaAuto());



    }

    @Override
    public int getItemCount() {
        return citaArrayList.size();
    }

    public  static class MantenimientoViewHolder extends RecyclerView.ViewHolder{

        private TextView fechaMantenimiento;
        private TextView horaMantenimiento;
        private TextView mecanicoAsignado;
        private TextView marcaAuto;
        private TextView placaAuto;

        private ImageView listaPartesMantenimiento, cambiarFechaHora, eliminarCitaAgendada;


        public MantenimientoViewHolder(@NonNull View itemView, final setOnClikImgListener listener) {
            super(itemView);

            fechaMantenimiento=(TextView)itemView.findViewById(R.id.lblFechaCitaMantenimiento);
            horaMantenimiento=(TextView)itemView.findViewById(R.id.lblhoraCitaMantenimiento);
            mecanicoAsignado=(TextView)itemView.findViewById(R.id.lblmecanicoAsignadoMantenimiento);
            marcaAuto=(TextView)itemView.findViewById(R.id.lblAutoMantenimiento);
            placaAuto=(TextView)itemView.findViewById(R.id.lblPlacaAutoMantenimiento);

            listaPartesMantenimiento=(ImageView)itemView.findViewById(R.id.icono_listarMantenimientoPartes);
            cambiarFechaHora=(ImageView)itemView.findViewById(R.id.icono_ModificarMantenimiento);
            eliminarCitaAgendada=(ImageView)itemView.findViewById(R.id.icono_BorrarMantenimiento);

            //sobrescribimos los metodos de las imagenesClick
            listaPartesMantenimiento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.okClikListPartes(position);
                        }

                    }
                }
            });

            //metodo que utiliza la imagen para borrar la cita agendada
            eliminarCitaAgendada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onClickDeliteCitaAdendada(position);
                        }

                    }
                }
            });
            //método que poermite mostrar un alert dialogo en el que se puede modificar la fecha y hora de la cita.
            cambiarFechaHora.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onClickUpdateCitaMantenimiento(position);
                        }

                    }
                }
            });

        }
    }
}
