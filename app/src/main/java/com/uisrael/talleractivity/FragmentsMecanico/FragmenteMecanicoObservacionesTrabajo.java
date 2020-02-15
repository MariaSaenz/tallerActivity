package my.jviracocha.talleractivity.FragmentsMecanico;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import my.jviracocha.talleractivity.Adaptadores.AdapterTrabajo;
import my.jviracocha.talleractivity.Adaptadores.ParteAdapterListaMant;
import my.jviracocha.talleractivity.Entidades.Mantenimiento;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmenteMecanicoObservacionesTrabajo extends Fragment {
    private  int idCita,idMecanico;
    private String placa;
    private TextView placaT;

    private ArrayList<Mantenimiento> mantenimientoArrayList;
    private RecyclerView recyclerView;
    private AdapterTrabajo adapterTrabajo;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAnalytics mFirebaseAnalytics;

    FloatingActionsMenu grupoBontoresFlotantes;
    FloatingActionButton CerraCita;
    FloatingActionButton Cancelar,salir;

    sendDataMecanicoCitas sendDataMecanicoCitasListener;
    public interface sendDataMecanicoCitas {

        public void opendilogochangeestadocita(String datoCita,int idCita,int idMecanico);
    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            sendDataMecanicoCitasListener=(sendDataMecanicoCitas) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragmente_mecanico_observaciones_trabajo, container, false);

        idCita=getArguments().getInt("idCita");
        placa=getArguments().getString("placaAuto");
        idMecanico=getArguments().getInt("idMecanico");
        Log.d("salida", ":"+idCita+"/"+placa );
        placaT=(TextView)vista.findViewById(R.id.txtplacaobstrabajoMecanico);
        placaT.setText(placa);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        grupoBontoresFlotantes=(FloatingActionsMenu)vista.findViewById(R.id.grupoFabObservTrabaojo);
        CerraCita=(FloatingActionButton)vista.findViewById(R.id.idFabCerrarCita);
        Cancelar=(FloatingActionButton)vista.findViewById(R.id.idFabCancelarCita);



        mantenimientoArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewObsTrabajos);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        cargarListaMantenimientoAgendadod(idCita);
         //boton flotante que permite cambiar el esado de la cita.
        CerraCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataMecanicoCitasListener.opendilogochangeestadocita("Se cerrar el mantenimiento \n para el vehiculo:"+placa,idCita,idMecanico);
                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "CerraCita");
                params10.putInt("id_mecanico", idMecanico);
                mFirebaseAnalytics.logEvent("click_evento", params10);
                // [END custom_event]
            }
        });

        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSupportFragmentManager().beginTransaction().remove(this).commit();
                //getFragmentManager().beginTransaction().remove().commit();
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "Cancelar");
                params10.putInt("id_mecanico", idMecanico);
                mFirebaseAnalytics.logEvent("click_evento", params10);
                // [END custom_event]
            }
        });


        return  vista;
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


                                                    mantenimiento.setId_mantenimiento(jsonObject.getInt("id_mantenimiento"));
                                                    mantenimiento.setNombre(jsonObject.getString("nombre"));
                                                    mantenimiento.setObservacionesMecanico(jsonObject.getString("observacion"));
                                                    mantenimiento.setId_mecanico(jsonObject.getInt("id_mecanico"));

                                                    //mantenimiento.setPrecio(jsonObject.getInt("precio"));
                                                    // mantenimiento.setId_mecanico(jsonObject.getInt("id_mecanico"));




                                                    mantenimientoArrayList.add(mantenimiento);
                                                }
                                                adapterTrabajo=new AdapterTrabajo(mantenimientoArrayList);
                                                adapterTrabajo.setOnClickListener(new AdapterTrabajo.OnItemClickListenerTrabajo() {
                                                    @Override
                                                    public void onSaveObsSwitch(int position, String observacion) {
                                                        Log.d("salida", "desbloque desde el adapter"+observacion+"////"+mantenimientoArrayList.get(position).getId_mantenimiento());

                                                        updateObservacionesMantenimiento(mantenimientoArrayList.get(position).getId_mantenimiento(),observacion);
                                                        // [START custom_event]
                                                        Bundle params10 = new Bundle();
                                                        params10.putString("button", "upsateObservaciones");
                                                        params10.putInt("id_usuario", idMecanico);
                                                        mFirebaseAnalytics.logEvent("click_evento", params10);
                                                        // [END custom_event]
                                                    }
                                                });


                                                recyclerView.setAdapter(adapterTrabajo);


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

    private void updateObservacionesMantenimiento(int idMantenimiento, String Observacion){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/updateObservacionesMantenimiento.php?id_mantenimiento="+idMantenimiento+"&observacion="+Observacion;
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
                                            Toast.makeText(getActivity(),mensaje1,Toast.LENGTH_SHORT).show();

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
