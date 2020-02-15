package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Kilometraje;
import my.jviracocha.talleractivity.R;

public class AdapterKilometraje extends RecyclerView.Adapter<AdapterKilometraje.KilometrajeViewHolder> {

    private OnItemClikListenerKM onItemClikListenerKM;

    public  interface OnItemClikListenerKM{

        void onDeliteKM(int position);
    }

    public  void setOnItemClickListener(OnItemClikListenerKM listener){
        onItemClikListenerKM=listener;

    }


    private ArrayList<Kilometraje> kilometrajeArrayList;

    public  AdapterKilometraje(ArrayList<Kilometraje> kilometrajeArrayList){
        this.kilometrajeArrayList=kilometrajeArrayList;

    }

    @NonNull
    @Override
    public KilometrajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_kilometraje,parent,false);
        KilometrajeViewHolder viewHolder= new KilometrajeViewHolder(vista, onItemClikListenerKM);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KilometrajeViewHolder holder, int position) {

        holder.IngresoKilometraje.setText(Integer.toString(kilometrajeArrayList.get(position).getKilometraje()));
        holder.fecha.setText(kilometrajeArrayList.get(position).getFecha());
    }

    @Override
    public int getItemCount() {
        return kilometrajeArrayList.size();
    }


    public  static  class  KilometrajeViewHolder extends  RecyclerView.ViewHolder{

        public TextView IngresoKilometraje, fecha;
        public ImageView iconoBorrarKilometraje;

        public KilometrajeViewHolder(@NonNull View itemView, final OnItemClikListenerKM onItemClikListenerKM){
            super(itemView);
            IngresoKilometraje=(TextView)itemView.findViewById(R.id.txtkilometrajeIngresado);
            fecha=(TextView)itemView.findViewById(R.id.txtFechaKilometraje);
            iconoBorrarKilometraje=(ImageView)itemView.findViewById(R.id.icono_borrarKilometraje);


            iconoBorrarKilometraje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClikListenerKM!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            //***********cambio del llamado al metodo
                            onItemClikListenerKM.onDeliteKM(position);
                            //***************************************
                        }

                    }
                }
            });

        }

    }
}
