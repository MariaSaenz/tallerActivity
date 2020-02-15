package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Auto;
import my.jviracocha.talleractivity.R;

public class AutoAdapterRW extends RecyclerView.Adapter<AutoAdapterRW.AutoViewHolder> {
    private ArrayList<Auto> autoArrayList;

    private OnClickListenerImg onClickListenerImg;

    public  interface OnClickListenerImg{

        public  void clicBorrar(int position);


    }
    public  void setOnItemClickListener(OnClickListenerImg listener){

        onClickListenerImg=listener;
    }




    public AutoAdapterRW(ArrayList<Auto> autoArrayList){

        this.autoArrayList=autoArrayList;
    }




    @NonNull
    @Override
    public AutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_auto,parent,false);

        AutoViewHolder autoViewHolder=new AutoViewHolder(vista,onClickListenerImg);

        return  autoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AutoViewHolder holder, int position) {

        Auto auto=autoArrayList.get(position);

        holder.MarcaAuto.setText("MARCA:      "+autoArrayList.get(position).getMarca());
        holder.PlacaAuto.setText("PLACA:        "+autoArrayList.get(position).getPlaca());
        holder.MoleloAuto.setText("AÑO:            "+autoArrayList.get(position).getModelo());
        holder.CilindrajeAuto.setText("CILINDRAJE: "+Float.toString(autoArrayList.get(position).getCilindraje()));

    }

    @Override
    public int getItemCount() {
        return autoArrayList.size();
    }

    public static class AutoViewHolder extends RecyclerView.ViewHolder{

        public TextView MarcaAuto;
        public  TextView PlacaAuto;
        public  TextView MoleloAuto;
        public  TextView CilindrajeAuto;
        public ImageView ImgBorrar;



        public AutoViewHolder(@NonNull View itemView,final OnClickListenerImg onClickListenerImg) {
            super(itemView);

            MarcaAuto=(TextView)itemView.findViewById(R.id.txtMarcaAuto);
            PlacaAuto=(TextView)itemView.findViewById(R.id.txtPlacaAuto);
            MoleloAuto=(TextView)itemView.findViewById(R.id.txtModeloAuto);
            CilindrajeAuto=(TextView)itemView.findViewById(R.id.txtCilindrajeAuto);
            ImgBorrar=(ImageView)itemView.findViewById(R.id.icono_borrar_auto2);

            ImgBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListenerImg!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            //***********cambio del llamado al metodo
                            onClickListenerImg.clicBorrar(position);
                            //***************************************
                        }

                    }
                }
            });

        }
    }
}
