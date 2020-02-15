package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Mantenimiento;
import my.jviracocha.talleractivity.Entidades.Parte;
import my.jviracocha.talleractivity.R;

public class ParteAdapterListaMant extends RecyclerView.Adapter<ParteAdapterListaMant.ParteAdapterListMantViewHolder> {
    private ArrayList<Mantenimiento> parteArrayList;

    public ParteAdapterListaMant(ArrayList<Mantenimiento> parteArrayList){

        this.parteArrayList=parteArrayList;
    }

    @NonNull
    @Override
    public ParteAdapterListMantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.carview_lista_parte,parent,false);
        ParteAdapterListMantViewHolder mvh=new ParteAdapterListMantViewHolder(vista) ;

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ParteAdapterListMantViewHolder holder, int position) {

        holder.nombreParte.setText(parteArrayList.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return parteArrayList.size();
    }

    public  static  class  ParteAdapterListMantViewHolder extends RecyclerView.ViewHolder{

        public TextView nombreParte;


        public ParteAdapterListMantViewHolder(View itemView){
            super(itemView);
            //txtNombrePartelistamantenimiento

            nombreParte=(TextView)itemView.findViewById(R.id.txtNombrePartelistamantenimiento);

        }
    }


}
