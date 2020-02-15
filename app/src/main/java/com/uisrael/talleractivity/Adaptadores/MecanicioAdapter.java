package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Mecanico;
import my.jviracocha.talleractivity.R;

public class MecanicioAdapter extends RecyclerView.Adapter<MecanicioAdapter.MecanicoViewHolder> {

    private  ArrayList<Mecanico> mecanicoArrayList;
    //************************2doPaso***************************************
    private OnItemClickListenerM onItemClickListener;
    //***************************************************************

    public  MecanicioAdapter(ArrayList<Mecanico> mecanicoArrayList){

    this.mecanicoArrayList=mecanicoArrayList;

    }

    //**********************2doPaso*****************************************
    //interfaz creada para la llamada al onclick de la lista

    public  interface OnItemClickListenerM{
        void onItemClick(int position);
        //*******************nuevo metodo para la imagen borrar**************

        void ooDeliteClick(int position);
        //*******************************************************************

        //****************metodo para mostar los trabajos pendientes agendados*********

        void  onShowMantenimientos(int position);
        //*****************************************************************************

    }

    public  void setOnItemClickListener(OnItemClickListenerM listener){

        onItemClickListener=listener;
    }
    //****************************************************************



    @NonNull
    @Override
    public MecanicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mecanico,parent,false);
        //MecanicoViewHolder mvh=new MecanicoViewHolder(vista) antes de ser cambiado
        MecanicoViewHolder mvh=new MecanicoViewHolder(vista,onItemClickListener);
        return  mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MecanicoViewHolder holder, int position) {
        Mecanico mecanico=mecanicoArrayList.get(position);

        holder.nombreMecanico.setText(mecanicoArrayList.get(position).getNombre());
        holder.correoMecanico.setText(mecanicoArrayList.get(position).getCorreo());
        holder.telefonoMecanico.setText(Integer.toString(mecanicoArrayList.get(position).getTelefono()));

    }

    @Override
    public int getItemCount() {

        return  mecanicoArrayList.size();
    }

    public  static class MecanicoViewHolder extends RecyclerView.ViewHolder{
        public TextView nombreMecanico;
        public TextView correoMecanico;
        public TextView telefonoMecanico;
        public ImageView iconoBorrarMecanico;
        public ImageView iconoTrabajosMecanico;

        //public MecanicoViewHolder(@NonNull View itemView)  esto estaba antes
        public MecanicoViewHolder(@NonNull View itemView,final OnItemClickListenerM listenerM) {
                super(itemView);

            nombreMecanico=(TextView)itemView.findViewById(R.id.txtNombreMecanico);
            correoMecanico=(TextView)itemView.findViewById(R.id.txtCorreoMecanico);
            telefonoMecanico=(TextView)itemView.findViewById(R.id.txtTelefonoMecanico);
            iconoBorrarMecanico=(ImageView)itemView.findViewById(R.id.icono_borrarMecanico);
            iconoTrabajosMecanico=(ImageView)itemView.findViewById(R.id.icono_TrabajosMecanico);

            //*****************2daParte****************************************
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenerM!=null){
                        int position=getAdapterPosition();
                            if(position!= RecyclerView.NO_POSITION){
                                listenerM.onItemClick(position);
                            }

                    }

                }
            });
            //******************************************************************

            //****************uso del boton borrar*******************************

            iconoBorrarMecanico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenerM!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            //***********cambio del llamado al metodo
                            listenerM.ooDeliteClick(position);
                            //***************************************
                        }

                    }
                }
            });
            //*******************************************************************

            //************************uso del boton mostar trabajos****************
            iconoTrabajosMecanico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenerM!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            //***********cambio del llamado al metodo
                            listenerM.onShowMantenimientos(position);
                            //***************************************
                        }

                    }
                }
            });

            //*********************************************************************
        }
    }
}

