package my.jviracocha.talleractivity.FragmentsCliente;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import my.jviracocha.talleractivity.Adaptadores.AutoAdapter;
import my.jviracocha.talleractivity.Adaptadores.AutoAdapterRW;
import my.jviracocha.talleractivity.Entidades.Auto;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentListaAutoCliente extends Fragment {
    ArrayList<Auto> autoArrayList;
    private RecyclerView recyclerView;
    private  AutoAdapterRW adapter;
    private RecyclerView.LayoutManager layoutManager;

    int idCliente;
    onUpdateGestAutoListener updateGestAuto;

    public  interface onUpdateGestAutoListener{

        public  void onUpdateGesAutofromListaAuto(int idCliente);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            updateGestAuto=(onUpdateGestAutoListener)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_cliente_lista_autos,container,false);

        autoArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewAutosCliente);
        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle=getArguments();
        idCliente=bundle.getInt("idCliente2");
        cargarListaAutos(idCliente);
        return  vista;
    }
    public void cargarListaAutos(final int idCliente){


        String ip=getString(R.string.ipWebService);
        String url=ip+"/obtenerAutos.php?id_cliente="+idCliente;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try{
                                    String estado=response.getString("estado");
                                    switch (estado){

                                        case "1":

                                            Auto auto=null;
                                            JSONArray json=response.optJSONArray("autos");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    auto=new Auto();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    auto.setId_auto(jsonObject.optInt("id_auto"));
                                                    auto.setColor(jsonObject.optString("color"));
                                                    auto.setCilindraje(jsonObject.getInt("cilindraje"));
                                                    auto.setPlaca(jsonObject.getString("placa"));
                                                    auto.setId_cliente(jsonObject.getInt("id_cliente"));
                                                    auto.setModelo(jsonObject.getString("modelo"));
                                                    auto.setMarca(jsonObject.getString("marca"));

                                                    autoArrayList.add(auto);
                                                }
                                                adapter=new AutoAdapterRW(autoArrayList);

                                                adapter.setOnItemClickListener(new AutoAdapterRW.OnClickListenerImg() {
                                                    @Override
                                                    public void clicBorrar(int position) {
                                                        System.out.println("podemos borrar desde aqui...."+position);

                                                        int idAuto=autoArrayList.get(position).getId_auto();
                                                        System.out.println("Este es el id cliente que se pasa: "+idCliente);
                                                        webserBorrarAuto(idAuto,idCliente);
                                                        //llamamos a la interfaz para que actulice el spinner
                                                        updateGestAuto.onUpdateGesAutofromListaAuto(idCliente);
                                                        autoArrayList.clear();
                                                    }
                                                });
                                                recyclerView.setAdapter(adapter);

                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "2":
                                            String mensaje=response.getString("mensaje");
                                            Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }catch(JSONException e){
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
    private void webserBorrarAuto(int idAuto, final int idCliente2){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/deliteAuto.php?id_auto="+idAuto;
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
                                    cargarListaAutos(idCliente2);
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
