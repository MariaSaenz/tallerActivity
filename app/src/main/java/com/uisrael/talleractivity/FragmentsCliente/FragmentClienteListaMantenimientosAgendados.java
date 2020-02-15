package my.jviracocha.talleractivity.FragmentsCliente;


import android.app.Activity;
import android.content.Context;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import my.jviracocha.talleractivity.Adaptadores.MantenimientoAdapter;
import my.jviracocha.talleractivity.Entidades.Cita;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentClienteListaMantenimientosAgendados extends Fragment {

    ArrayList<Cita> citaArrayList;
    private RecyclerView recyclerView;
    private MantenimientoAdapter mantenimientoAdapter;
    private RecyclerView.LayoutManager layoutManager;
    int idCliente;
    private FirebaseAnalytics mFirebaseAnalytics;
    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaSalir;

    sendataListMante sendataListMantelistener;
    public interface sendataListMante{
        public  void sendIdClinteLisMan(int idCliente);
        public  void  onUpdateListaManteAgendado(int idCliente);
        public  void  sendDataUpdateCita(int idCliente,int idCita,String fecha,String hora, int idAuto);
    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            sendataListMantelistener=(sendataListMante) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_cliente_lista_mantenimientos_agendados,container,false);

        idCliente=getArguments().getInt("idCliente");
        System.out.println(idCliente);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        gruupoBotones=vista.findViewById(R.id.grupoFabClienteMantenimientoListaM);
        btnFabaSalir=vista.findViewById(R.id.idFabSalirMantenimientoListaM);

        citaArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewListaMantenimientosAgendados);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        citaArrayList.clear();



        cargarListaMantenimientoAgendadod(idCliente);


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

    private void cargarListaMantenimientoAgendadod(final int idCliente) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getListCitas.php?id_cliente="+idCliente;
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


                                                    cita.setIdCita(jsonObject.optInt("id_citas"));
                                                    cita.setFecha(jsonObject.optString("fecha"));
                                                    cita.setHora(jsonObject.getString("hora"));
                                                    cita.setNombreMecanico(jsonObject.getString("nombre"));
                                                    cita.setMarcaAuto(jsonObject.getString("marca"));
                                                    cita.setPlacaAuto(jsonObject.getString("placa"));
                                                    cita.setIdAuto(jsonObject.getInt("id_auto"));



                                                   citaArrayList.add(cita);
                                                }
                                                mantenimientoAdapter=new MantenimientoAdapter(citaArrayList);
                                                //espacio para colocar el llamado a los iconos
                                                mantenimientoAdapter.setOnItemClickListener(new MantenimientoAdapter.setOnClikImgListener() {
                                                    @Override
                                                    public void okClikListPartes(int position) {
                                                        //llamdao a un alterdialogo enviandolo el idCitas
                                                        System.out.println(citaArrayList.get(position).getIdCita());
                                                       // createSingleListDialog();
                                                        sendataListMantelistener.sendIdClinteLisMan(citaArrayList.get(position).getIdCita());
                                                        // [START custom_event]
                                                        Bundle params10 = new Bundle();
                                                        params10.putString("button", "okClikListPartes");
                                                        params10.putInt("id_usuario", idCliente);
                                                        params10.putInt("id_cita",citaArrayList.get(position).getIdCita());
                                                        mFirebaseAnalytics.logEvent("click_evento", params10);
                                                        // [END custom_event]
                                                    }

                                                    //borar de la lista el elemento
                                                    @Override
                                                    public void onClickDeliteCitaAdendada(int position) {
                                                        System.out.println("idcliente para actualizar "+idCliente);

                                                        borrarCitaAgendada(citaArrayList.get(position).getIdCita(),idCliente);
                                                        citaArrayList.clear();
                                                        mantenimientoAdapter.notifyDataSetChanged();

                                                        sendataListMantelistener.onUpdateListaManteAgendado(idCliente);
                                                        // [START custom_event]
                                                        Bundle params10 = new Bundle();
                                                        params10.putString("button", "onClickDeliteCitaAdendada");
                                                        params10.putInt("id_usuario", idCliente);
                                                        params10.putInt("id_cita",citaArrayList.get(position).getIdCita());
                                                        mFirebaseAnalytics.logEvent("click_evento", params10);
                                                        // [END custom_event]

                                                    }

                                                    //modificar la fecha del mantenimiemto.
                                                    @Override
                                                    public void onClickUpdateCitaMantenimiento(int position) {
                                                        sendataListMantelistener.sendDataUpdateCita(idCliente,citaArrayList.get(position).getIdCita(),citaArrayList.get(position).getFecha(),citaArrayList.get(position).getHora(),citaArrayList.get(position).getIdAuto());
                                                        // [START custom_event]
                                                        Bundle params10 = new Bundle();
                                                        params10.putString("button", "onClickUpdateCitaMantenimiento");
                                                        params10.putInt("id_usuario", idCliente);
                                                        params10.putInt("id_cita",citaArrayList.get(position).getIdCita());
                                                        mFirebaseAnalytics.logEvent("click_evento", params10);
                                                        // [END custom_event]
                                                    }


                                                });



                                                recyclerView.setAdapter(mantenimientoAdapter);

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

    public  void borrarCitaAgendada(int idCitas, final int idCliente){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/deliteCitas.php?id_citas="+idCitas;
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
                                    cargarListaMantenimientoAgendadod(idCliente);

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
