package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Parte;
import my.jviracocha.talleractivity.R;

public class ParteAdapterAgendaMantenimiento extends RecyclerView.Adapter<ParteAdapterAgendaMantenimiento.ParteAdapterViewHolder> {

    private ArrayList<Parte> parteArrayList;

    public ArrayList<Parte> parteArrayListSelected=new ArrayList<>();

    public ParteAdapterAgendaMantenimiento(ArrayList<Parte> parteArrayList){

        this.parteArrayList=parteArrayList;
    }



    @NonNull
    @Override
    public ParteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.carview_partes_agendamantenimiento,parent,false);
        ParteAdapterViewHolder mvh=new ParteAdapterViewHolder(vista) ;

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ParteAdapterViewHolder holder, final int position) {
        holder.nombreParte.setText(parteArrayList.get(position).getNombre());
        holder.KMtope.setText(Integer.toString(parteArrayList.get(position).getVencimientoKM()));
        holder.checkBox.setChecked(parteArrayList.get(position).isSelect());

        holder.setItemClickListener(new ParteAdapterViewHolder.ItemClickListener() {

            @Override
             public  void onItemClick(View v, int position){

                CheckBox checkBox=(CheckBox)v;
                Parte parte= parteArrayList.get(position);
                if(checkBox.isChecked()){

                    parte.setSelect(true);
                    parteArrayListSelected.add(parte);

                }else if(!checkBox.isChecked()){
                    parte.setSelect(false);
                    parteArrayListSelected.remove(parte);


                }


            }

        });





    }

    @Override
    public int getItemCount() {
        return parteArrayList.size();
    }

    public  static  class  ParteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nombreParte;
        public TextView KMtope;
        public TextView sumaKM;
        public CheckBox checkBox;


        ItemClickListener itemClickListener;

        public ParteAdapterViewHolder(View itemView){
            super(itemView);

            nombreParte=(TextView)itemView.findViewById(R.id.txtNombreParteAgendamiento);
            KMtope=(TextView)itemView.findViewById(R.id.txtKilometrajeTopeAgendamiento);
            sumaKM=(TextView)itemView.findViewById(R.id.txtSumaKilometrajeAutoAgendamiento);
            checkBox=(CheckBox)itemView.findViewById(R.id.checkboxAgendaMantenimiento);

            checkBox.setOnClickListener(this);

           /* if(checkBox.isChecked()){
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener!=null){
                            int position=getAdapterPosition();
                            if(position!= RecyclerView.NO_POSITION){
                                listener.onItemClick(position);

                            }

                        }
                    }
                });

            }*/


        }
        public void  setItemClickListener(ItemClickListener itemClickListener){

            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v){
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }
        interface  ItemClickListener{
            void onItemClick(View v, int position);


        }

    }

}
