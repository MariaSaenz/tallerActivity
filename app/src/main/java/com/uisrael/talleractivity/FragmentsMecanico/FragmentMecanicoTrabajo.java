package my.jviracocha.talleractivity.FragmentsMecanico;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Adaptadores.AdapterTrabajoMecanico;
import my.jviracocha.talleractivity.Entidades.Cita;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentMecanicoTrabajo extends Fragment {
    private  String correo, rol;

    private ArrayList<Cita> citaArrayList;
    private RecyclerView recyclerView;
    private AdapterTrabajoMecanico adapterTrabajoMecanico;
    private RecyclerView.LayoutManager layoutManager;
    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaSalir;
    private FirebaseAnalytics mFirebaseAnalytics;


    sendDataMecanicoTrabajo sendDataMecanicoTrabajoListener;
    public interface sendDataMecanicoTrabajo{

        public void openFragTrabajo(int idCita, String placaAuto, int idMecanico);
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            sendDataMecanicoTrabajoListener=(sendDataMecanicoTrabajo) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_mecanico_trabajo, container, false);


        correo=getArguments().getString("correo");
        rol=getArguments().getString("rol");

        citaArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewlistadetrabajomecanico);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        obtenrIDUsuario(correo,rol);

        gruupoBotones=vista.findViewById(R.id.grupoFabMantenimientoMecanicotrabajos);
        btnFabaSalir=vista.findViewById(R.id.idFabMantenimientoSalirtrabajos);

        btnFabaSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
                gruupoBotones.collapse();
            }
        });


        return vista;
    }

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

                                            Log.d("salida", ":"+idClienteStr);

                                            obtenrLiltadeTrabajos(idClinte);



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

    private void obtenrLiltadeTrabajos( final int idMecanico){
        String ip=getString(R.string.ipWebService);
        String url=ip+"/getlistWork.php?id_mecanico="+idMecanico;
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
                                            Cita cita=null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    cita=new Cita();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    cita.setFecha(jsonObject.getString("fecha"));
                                                    cita.setHora(jsonObject.getString("hora"));
                                                    cita.setNombre(jsonObject.getString("nombre"));
                                                    cita.setTelefono(jsonObject.getInt("telefono"));
                                                    cita.setPlacaAuto(jsonObject.getString("placa"));
                                                    cita.setIdCita(jsonObject.getInt("id_citas"));
                                                    cita.setMarcaAuto(jsonObject.getString("marca"));
                                                    cita.setEstado(jsonObject.getString("estado"));


                                                    citaArrayList.add(cita);
                                                }
                                                adapterTrabajoMecanico=new AdapterTrabajoMecanico(citaArrayList);
                                                adapterTrabajoMecanico.setClickButtonListener(new AdapterTrabajoMecanico.workClickButtonListener() {
                                                    @Override
                                                    public void onShowPartUpkeep(int position) {
                                                        Log.d("salida", ":"+citaArrayList.get(position).getIdCita());
                                                        updateEstadoCita(citaArrayList.get(position).getIdCita(), "PROGRESS" );
                                                        sendDataMecanicoTrabajoListener.openFragTrabajo(citaArrayList.get(position).getIdCita(),citaArrayList.get(position).getPlacaAuto(),idMecanico);
                                                        // [START custom_event]
                                                        Bundle params10 = new Bundle();
                                                        params10.putString("button", "trabajos");
                                                        params10.putString("id_usuario", correo);
                                                        mFirebaseAnalytics.logEvent("click_evento", params10);
                                                        // [END custom_event]
                                                        // [START custom_event]
                                                        Bundle params = new Bundle();
                                                        params.putString("fragmente", "openFragTrabajo");
                                                        params.putString("usuario", correo);
                                                        mFirebaseAnalytics.logEvent("navigation", params);
                                                        // [END custom_event]

                                                    }
                                                });

                                                recyclerView.setAdapter(adapterTrabajoMecanico);

                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
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

    private void  updateEstadoCita(int idCita,String estado){
        String ip=getString(R.string.ipWebService);
        String url=ip+"/updateObserCitas.php?id_citas="+idCita+"&observaciones="+estado;
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
                                            String mensaje1=response.getString("dato");
                                            Toast.makeText(getContext(),mensaje1,Toast.LENGTH_SHORT).show();
                                            break;
                                        case "2":
                                            String mensaje=response.getString("dato");
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



}
