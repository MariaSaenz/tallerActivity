package my.jviracocha.talleractivity.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Parte;
import my.jviracocha.talleractivity.R;

public class ParteAdapter extends RecyclerView.Adapter<ParteAdapter.ParteAdapterViewHolder> {

    private ArrayList<Parte> parteArrayList;

    public ParteAdapter(ArrayList<Parte> parteArrayList){

        this.parteArrayList=parteArrayList;
    }

    @NonNull
    @Override
    public ParteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_parte,parent,false);
        ParteAdapterViewHolder mvh=new ParteAdapterViewHolder(vista) ;

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ParteAdapterViewHolder holder, int position) {

        holder.nombreParte.setText(parteArrayList.get(position).getNombre());
        holder.KMtope.setText(Integer.toString(parteArrayList.get(position).getVencimientoKM()));
    }

    @Override
    public int getItemCount() {
        return parteArrayList.size();
    }

    public  static  class  ParteAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView nombreParte;
        public TextView KMtope;
        public TextView sumaKM;

        public ParteAdapterViewHolder(View itemView){
            super(itemView);

            nombreParte=(TextView)itemView.findViewById(R.id.txtNombreParte);
            KMtope=(TextView)itemView.findViewById(R.id.txtKilometrajeTope);
            sumaKM=(TextView)itemView.findViewById(R.id.txtSumaKilometrajeAuto);
        }
    }
}
