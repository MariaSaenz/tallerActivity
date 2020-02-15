package my.jviracocha.talleractivity.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import my.jviracocha.talleractivity.Entidades.Auto;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentClienteGestionAuto;
import my.jviracocha.talleractivity.R;

public class AutoAdapter extends ArrayAdapter<Auto> {


    public AutoAdapter(Context context, ArrayList<Auto> autoArrayList) {
        super(context,0,autoArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return autoView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return autoView(position, convertView, parent);
    }

    public View autoView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.vista_auto,parent,false);
        }

        Auto auto=getItem(position);
        //ImageView imageView=convertView.findViewById(R.id.imgSpinnerCliente);
        TextView nombre=convertView.findViewById(R.id.tvNombreAuto);

        if(auto!=null){
           // imageView.setImageResource(auto.getImagen());
            nombre.setText(auto.getMarca());
        }
        return convertView;
    }
}
