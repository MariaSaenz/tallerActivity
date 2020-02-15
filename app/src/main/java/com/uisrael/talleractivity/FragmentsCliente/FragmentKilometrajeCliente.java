package my.jviracocha.talleractivity.FragmentsCliente;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import my.jviracocha.talleractivity.Adaptadores.AdapterKilometraje;
import my.jviracocha.talleractivity.Adaptadores.MecanicioAdapter;
import my.jviracocha.talleractivity.Entidades.Kilometraje;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentKilometrajeCliente extends Fragment {
    ArrayList<Kilometraje> kilometrajeArrayList;
    private RecyclerView recyclerView;
    private AdapterKilometraje adapterKilometraje;
    private RecyclerView.LayoutManager layoutManager;

    int idCliente;
    int idAuto;

    onUpdateListKM onUpdateListKM;
    public  interface onUpdateListKM{

        public void onUpdateListKilometraje(int idCliente, int idAuto);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            onUpdateListKM=(onUpdateListKM)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_cliente_kilometraje,container,false);

        Bundle bundle=getArguments();
        idCliente=bundle.getInt("idCliente");
        idAuto=bundle.getInt("idAuto");

        kilometrajeArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewkilometrajeIngresado);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        cargarlistaKilometraje(idCliente,idAuto);


        return  vista;
    }

    public void cargarlistaKilometraje(final int idCliente,final int idAuto){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getallKilometraje.php?id_cliente="+idCliente+"&id_auto="+idAuto;
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
                                            Kilometraje kilometraje=null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    kilometraje=new Kilometraje();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    kilometraje.setId_kilometraje(jsonObject.optInt("id_kilometraje"));
                                                    kilometraje.setId_auto(jsonObject.optInt("id_auto"));
                                                    kilometraje.setId_cliente(jsonObject.optInt("id_cliente"));
                                                    kilometraje.setFecha(jsonObject.optString("fecha"));
                                                    kilometraje.setKilometraje(jsonObject.optInt("kilometraje"));



                                                    kilometrajeArrayList.add(kilometraje);
                                                }
                                                adapterKilometraje=new AdapterKilometraje(kilometrajeArrayList);


                                                adapterKilometraje.setOnItemClickListener(new AdapterKilometraje.OnItemClikListenerKM() {
                                                    @Override
                                                    public void onDeliteKM(int position) {

                                                        System.out.println("podemos borrar ");
                                                        int idKilometraje=kilometrajeArrayList.get(position).getId_kilometraje();

                                                        System.out.println(idKilometraje);
                                                        borrarKilometraje(idKilometraje,idAuto,idCliente);
                                                        onUpdateListKM.onUpdateListKilometraje(idCliente,idAuto);

                                                        kilometrajeArrayList.clear();
                                                    }
                                                });

                                                recyclerView.setAdapter(adapterKilometraje);

                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }





                                            ////
                                            break;
                                        case "2":

                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();

                                            break;
                                        case "3":

                                            String mensaje3=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje3,Toast.LENGTH_SHORT).show();

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

    public  void borrarKilometraje(int idkilometraje,final  int idAuto, final  int idCliente){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/deliteKilometraje.php?id_kilometraje="+idkilometraje;
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
                                            String mensaje1=response.getString("dato");
                                            Toast.makeText(getContext(),mensaje1,Toast.LENGTH_SHORT).show();

                                            break;
                                        case "2":
                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getContext(),mensaje2,Toast.LENGTH_SHORT).show();
                                            break;
                                        case "3":
                                            String mensaje3=response.getString("dato");
                                            Toast.makeText(getContext(),mensaje3,Toast.LENGTH_SHORT).show();
                                            break;


                                    }
                                    //cargarwebserviceMecanico();
                                    cargarlistaKilometraje(idCliente,idAuto);

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
