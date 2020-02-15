package my.jviracocha.talleractivity.FragmentsMecanico;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Adaptadores.HistorialAdapter;
import my.jviracocha.talleractivity.Entidades.Cita;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentMecanicoGestionMantenimientos extends Fragment {

    private TextView idMecanico;
    private  String correo, rol;

    private ArrayList<Cita> citaArrayList;
    private RecyclerView recyclerView;
    private HistorialAdapter historialAdapter;
    private RecyclerView.LayoutManager layoutManager;

    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaSalir;
    private FirebaseAnalytics mFirebaseAnalytics;

    sendDataforView sedDataforViewListener;
    public interface  sendDataforView{

        public  void dataforView(int idcliente, int idAuto, int idCita);

    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            sedDataforViewListener=(sendDataforView) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }

    //private  int idMecanico;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_mecanico_gestion_mantenimientos, container, false);

        citaArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewlistaHistoricos);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        //idMecanico=(TextView)vista.findViewById(R.id.txtIDmecanicoIngresado);
        correo=getArguments().getString("correo");
        rol=getArguments().getString("rol");

        Log.i("salida", "elementos enviados" + correo+""+rol);
        obtenrIDUsuario(correo,rol);
        gruupoBotones=vista.findViewById(R.id.grupoFabMantenimientoMecanico);
        btnFabaSalir=vista.findViewById(R.id.idFabMantenimientoSalir);

        btnFabaSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
                gruupoBotones.collapse();
            }
        });


        return  vista;
    }
    //método que permite obtener el id de los mecanicos
    private void obtenrIDUsuario(String correo, String rol) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getIDUser.php?correo="+correo+"&rol="+rol;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json

                                String idClienteStr=null;
                                try{
                                    String estado=response.getString("estado");

                                    switch (estado){

                                        case "1":
                                            JSONObject jsonObjectDato=response.getJSONObject("dato");

                                            idClienteStr=jsonObjectDato.getString("id_mecanico");

                                            int idClinte=Integer.parseInt(idClienteStr);
                                            Log.i("salida", "id de busqueda" + idClinte);

                                            cargarHistoricosMecanico(idClinte);
                                            break;
                                        case "2":
                                            String mensaje=response.getString("mensaje");
                                            Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
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
    //metodo para permitir obter la lista de los mantenimientos agendados
    public void cargarHistoricosMecanico(final int idMecanico){


        String ip=getString(R.string.ipWebService);
        String url=ip+"/getlistCitasofMecanico.php?id_mecanico="+idMecanico;
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
                                            Cita cita=null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    cita=new Cita();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    cita.setFecha(jsonObject.optString("fecha"));
                                                    cita.setHora(jsonObject.optString("hora"));
                                                    cita.setMarcaAuto(jsonObject.getString("marca"));

                                                    //datos necesarios para pasrle al fragment de visulización
                                                    cita.setIdCliente(jsonObject.getInt("id_cliente"));
                                                    cita.setIdAuto(jsonObject.getInt("id_auto"));
                                                    cita.setIdCita(jsonObject.getInt("id_citas"));

                                                    citaArrayList.add(cita);
                                                }
                                                historialAdapter=new HistorialAdapter(citaArrayList);
                                                historialAdapter.setOnItemClickListener(new HistorialAdapter.onItemClickListenerHistory() {
                                                    @Override
                                                    public void onItemClik(int position) {
                                                        System.out.println("el idCliente es: "+citaArrayList.get(position).getIdCliente());

                                                        sedDataforViewListener.dataforView(citaArrayList.get(position).getIdCliente(),citaArrayList.get(position).getIdAuto(),citaArrayList.get(position).getIdCita());
                                                        // [START custom_event]
                                                        Bundle params10 = new Bundle();
                                                        params10.putString("button", "mantenimiento");
                                                        params10.putInt("id_usuario", idMecanico);
                                                        mFirebaseAnalytics.logEvent("click_evento", params10);
                                                        // [END custom_event]
                                                    }
                                                });


                                                recyclerView.setAdapter(historialAdapter);

                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "2":
                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getContext(),mensaje2,Toast.LENGTH_SHORT).show();
                                            break;

                                    }

                                }
                                catch (Exception e){
                                    String mensaje="Usuario no tiene  citas";
                                    Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
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
