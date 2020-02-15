package my.jviracocha.talleractivity.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Entidades.Cliente;
import my.jviracocha.talleractivity.R;

//ingreso del implement filtreable

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> implements Filterable {
    private ArrayList<Cliente> clienteArrayList;
    private OnItemClickListener mListener;

    private int row_index=-1;
    //cambios por el buscar
    private ArrayList<Cliente> clienteArrayListfull;
    public  interface OnItemClickListener{
        void  onItemClick(int position);
        void  onDeliteClick(int position);
    }

   public void  setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
   }


    public static  class ClienteViewHolder extends  RecyclerView.ViewHolder{
        public TextView nombreCliente;
        public TextView correoCliente;
        public TextView telefonoCliente;
        public ImageView iconoBorrar;
        public CardView cardView;
        int row_index2=-1;

        public ClienteViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            nombreCliente=(TextView)itemView.findViewById(R.id.txtNombreCliente);
            correoCliente=(TextView)itemView.findViewById(R.id.txtCorreoCliente);
            telefonoCliente=(TextView)itemView.findViewById(R.id.txtTelefonoCliente);
            iconoBorrar=(ImageView)itemView.findViewById(R.id.icono_borrar);

            cardView=(CardView)itemView.findViewById(R.id.cardViewListaClientes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        row_index2=position;
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                            Log.d("salida", "position :"+position);
                            //cardView.setBackgroundColor(Color.parseColor("#567845"));



                        }

                    }


                }
            });





            iconoBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onDeliteClick(position);
                        }
                    }
                }
            });


        }
    }

    //contructor cambiado por el boton buscar " ,"
    public ClienteAdapter(ArrayList<Cliente> clienteArrayList){

        this.clienteArrayList=clienteArrayList;
        //nuevo agrumento
        this.clienteArrayListfull=new ArrayList<>(clienteArrayList);
    }

    @NonNull
    @Override
    public ClienteAdapter.ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewcliente,parent,false);
        ClienteViewHolder evy=new ClienteViewHolder(vista,mListener);

        return evy;
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteAdapter.ClienteViewHolder holder, final int position) {


        holder.nombreCliente.setText(clienteArrayList.get(position).getNombre());
        holder.correoCliente.setText(clienteArrayList.get(position).getCorreo());
        holder.telefonoCliente.setText(Integer.toString(clienteArrayList.get(position).getTelefono()) );











    }

    @Override
    public int getItemCount() {

        return clienteArrayList.size();
    }

    //nuevo metodo por el filtro
    @Override
    public Filter getFilter() {
        return clienteFiltro;
    }
    //cargamos el valor a devolver
    private Filter clienteFiltro=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Cliente> clienteArrayListFilter=new ArrayList<>();

            if(constraint==null || constraint.length()==0){
                clienteArrayListFilter.addAll(clienteArrayListfull);
            }else {

                String filterPatter=constraint.toString().toLowerCase().trim();

                for (Cliente item: clienteArrayListfull){
                    if (item.getNombre().toLowerCase().contains(filterPatter)){

                        clienteArrayListFilter.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=clienteArrayListFilter;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            clienteArrayList.clear();
            //ojo aqui creo que va a generar un conflicto
            clienteArrayList.addAll((ArrayList<Cliente>)results.values);
            notifyDataSetChanged();
        }
    };




}
