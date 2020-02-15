package my.jviracocha.talleractivity.Frgments;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Adaptadores.AdapterKilometraje;
import my.jviracocha.talleractivity.Adaptadores.AdapterNotificacion;
import my.jviracocha.talleractivity.Entidades.Kilometraje;
import my.jviracocha.talleractivity.Entidades.Notificaciones;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class GestionServiciosNotificaciones extends Fragment {
    private ArrayList<Notificaciones> notificacionesArrayList;
    private RecyclerView recyclerView;
    private AdapterNotificacion adapterNotificacion;
    private RecyclerView.LayoutManager layoutManager;
    //private int idAdministrador;
   private String correo, rol;

    private TextView idadministrador;

    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaNuevo;
    FloatingActionButton btnSalirNoti;

    gestionNotificacionesInterface gestionNotificacionesInterfaceListener;
    public interface gestionNotificacionesInterface{
        void openDilogoNuevaNotificaion(int idAdministrador, String correo, String rol);
    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            gestionNotificacionesInterfaceListener=(gestionNotificacionesInterface)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragmente_gestionotificaciones,container,false);

        idadministrador=(TextView)vista.findViewById(R.id.idadministradorNotificacionesAdministrador);


        notificacionesArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewListaNotificacionesEnviadas);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);


        gruupoBotones= vista.findViewById(R.id.grupoFabNotificaciones);
        btnFabaNuevo=vista.findViewById(R.id.idFabNuevaNotificacion);
        btnSalirNoti=vista.findViewById(R.id.idFabSalirNotificacion);

         correo=getArguments().getString("correo");
         rol=getArguments().getString("rol");
        Log.i("salida","datos que se envia desde el main"+correo+" "+rol);
        obtenrIDUsuario(correo, rol);


        cargarListaNotificaciones();

        btnFabaNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("salida","Se creara nueva notificaione abriendo el alert dialogo");
                Log.i("salida","se enviaran los siguientes valores:"+idadministrador.getText().toString()+" "+correo+"" +rol);
                int idadmin=Integer.parseInt(idadministrador.getText().toString());
                gestionNotificacionesInterfaceListener.openDilogoNuevaNotificaion(idadmin,correo,rol);
                gruupoBotones.collapse();


            }
        });
        btnSalirNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
            }
        });



        return  vista;
    }

    public void obtenrIDUsuario(String correo, String rol) {
        int id;
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
                                idadministrador.setText(procesarRespuesta(response));
                                Log.i("salida","dato en el texview:"+idadministrador.getText().toString().trim());

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

    public  String  procesarRespuesta(JSONObject response) {
        String idAdministrador=null;
        try{
            String estado=response.getString("estado");

            switch (estado){

                case "1":
                    JSONObject jsonObjectDato=response.getJSONObject("dato");
                    idAdministrador=jsonObjectDato.getString("id_administrador");
                    Log.i("salida","busqueda del IDADM en el webservice:"+idAdministrador);

                    break;
                case "2":
                    String mensaje=response.getString("mensaje");
                    Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                    break;

            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return idAdministrador;
    }

    public void cargarListaNotificaciones(){
        String ip=getString(R.string.ipWebService);
        String url=ip+"/getListNotificaciones.php";
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
                                            Notificaciones notificacion=null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    notificacion=new Notificaciones();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    notificacion.setIdNotificacion(jsonObject.optInt("id_notificacionP"));
                                                    notificacion.setTitulo(jsonObject.optString("titulo"));
                                                    notificacion.setCuerpo(jsonObject.optString("cuerpo"));
                                                    notificacion.setTema(jsonObject.optString("tema"));
                                                    notificacion.setFecha(jsonObject.optString("fecha"));
                                                    notificacion.setIdMensage(jsonObject.getString("idmenssage"));

                                                    notificacionesArrayList.add(notificacion);
                                                }
                                                adapterNotificacion=new AdapterNotificacion(notificacionesArrayList);
                                                adapterNotificacion.setOnItemClicListenerNotificationListener(new AdapterNotificacion.OnItemClicListenerNotification() {
                                                    @Override
                                                    public void onDeliteClickNotificacion(int position) {
                                                        Log.i("salida","se ha eliminado:"+position);
                                                    }
                                                });

                                                recyclerView.setAdapter(adapterNotificacion);

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



}
