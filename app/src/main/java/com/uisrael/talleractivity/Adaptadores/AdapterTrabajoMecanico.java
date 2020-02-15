package my.jviracocha.talleractivity.Adaptadores;

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

public class AdapterTrabajoMecanico extends RecyclerView.Adapter<AdapterTrabajoMecanico.TrabajoViewHolder> {

    private ArrayList<Cita> citaArrayList;
    public workClickButtonListener clickButtonListener;

    public AdapterTrabajoMecanico (ArrayList<Cita> citaArrayList){

        this.citaArrayList=citaArrayList;
    }

    public interface workClickButtonListener{

        public void onShowPartUpkeep(int position);
    }

    public void setClickButtonListener(workClickButtonListener listener){

        clickButtonListener=listener;
    }
    @NonNull
    @Override
    public TrabajoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mecanico_trabajo,parent,false);
        //MecanicoViewHolder mvh=new MecanicoViewHolder(vista) antes de ser cambiado

        TrabajoViewHolder mvh=new TrabajoViewHolder(vista,clickButtonListener);
        return  mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TrabajoViewHolder holder, int position) {

        holder.fechaT.setText(citaArrayList.get(position).getFecha());
        holder.nombreClienteT.setText(citaArrayList.get(position).getNombre());
        holder.telefonoT.setText(Integer.toString(citaArrayList.get(position).getTelefono()));
        holder.infoAutoT.setText(citaArrayList.get(position).getMarcaAuto()+"-"+citaArrayList.get(position).getPlacaAuto());
        holder.estadoCita.setText(citaArrayList.get(position).getEstado());

        holder.btnhora.setText(citaArrayList.get(position).getHora());

    }

    @Override
    public int getItemCount() {
        return citaArrayList.size();
    }

    public  static  class TrabajoViewHolder extends  RecyclerView.ViewHolder{
        public TextView fechaT, nombreClienteT, telefonoT, infoAutoT, estadoCita;
        public Button btnhora;

         public  TrabajoViewHolder (@NonNull View itemView,final  workClickButtonListener clickButtonListener){
             super(itemView);
             fechaT=(TextView)itemView.findViewById(R.id.txtfechaMantenimientoTrabajoMecanico);
             nombreClienteT=(TextView)itemView.findViewById(R.id.txtNombreClienteTrabajoMecanico);
             telefonoT=(TextView)itemView.findViewById(R.id.txtTelefonoClienteTrabajoMecanico);
             infoAutoT=(TextView)itemView.findViewById(R.id.txtInfoAutoClienteTrabajoMecanico);
             estadoCita=(TextView)itemView.findViewById(R.id.txtInfoestaocita);

             btnhora=(Button)itemView.findViewById(R.id.btnHoraCitaClienteTranajoMecanico);

             btnhora.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(clickButtonListener!=null){
                         int position=getAdapterPosition();
                         if(position!= RecyclerView.NO_POSITION){
                             clickButtonListener.onShowPartUpkeep(position);
                         }

                     }
                 }
             });

         }


    }
}
