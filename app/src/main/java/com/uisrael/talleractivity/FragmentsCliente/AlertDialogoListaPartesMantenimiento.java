package my.jviracocha.talleractivity.FragmentsCliente;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Adaptadores.ParteAdapterListaMant;
import my.jviracocha.talleractivity.Entidades.Mantenimiento;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class AlertDialogoListaPartesMantenimiento extends DialogFragment {

    int idCita;

    ArrayList<Mantenimiento> mantenimientoArrayList;
    private RecyclerView recyclerView;
    private ParteAdapterListaMant parteAdapter;
    private RecyclerView.LayoutManager layoutManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.alertdialogo_cliente_listapartesmantenimiento,container,false);

        mantenimientoArrayList=new ArrayList<>();

        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewPreteslistaMantenimiento);
        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);


        Bundle bundle=getArguments();
        idCita=bundle.getInt("idCita");
        cargarListaMantenimientoAgendadod(idCita);


        return vista;


    }



    private void cargarListaMantenimientoAgendadod(int idCitas) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getlistMantenimientoByidCita.php?id_citas="+idCitas;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json

                                try{
                                    String estado=response.getString("estado");

                                    switch (estado){

                                        case "1":
                                            //cargamos el recicle view
                                            Mantenimiento mantenimiento=null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    mantenimiento=new Mantenimiento();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);


                                                    //mantenimiento.setId_mantenimiento(jsonObject.getInt("id_mantenimiento"));
                                                    mantenimiento.setNombre(jsonObject.getString("nombre"));
                                                    //mantenimiento.setPrecio(jsonObject.getInt("precio"));
                                                   // mantenimiento.setId_mecanico(jsonObject.getInt("id_mecanico"));



                                                    mantenimientoArrayList.add(mantenimiento);
                                                }
                                                parteAdapter=new ParteAdapterListaMant(mantenimientoArrayList);

                                                recyclerView.setAdapter(parteAdapter);


                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }





                                            ////
                                            break;
                                        case "2":

                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();

                                            break;


                                    }

                                }catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }




                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //gerar mensaje de error
                                Context context = getContext();
                                CharSequence text= "No se encontro registro"+error.toString();
                                int duracion= Toast.LENGTH_SHORT;
                                Toast toast=Toast.makeText(context,text,duracion);
                                toast.show();

                            }
                        }
                ));

    }

}
