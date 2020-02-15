package my.jviracocha.talleractivity.FragmentsCliente;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Adaptadores.AdapterClienteNotificacion;
import my.jviracocha.talleractivity.Adaptadores.AdapterNotificacion;
import my.jviracocha.talleractivity.Entidades.Notificaciones;
import my.jviracocha.talleractivity.Preferences;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentClienteListaNotificaciones extends Fragment {
    private ArrayList<Notificaciones> notificacionesArrayList;
    private RecyclerView recyclerView;
    private AdapterClienteNotificacion adapterNotificacion;
    private RecyclerView.LayoutManager layoutManager;

    String idCliente;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_cliente_lista_notificaciones,container,false);
        idCliente=Preferences.obtenerPreferenceString(getActivity(), Preferences.USUARIO_ID);
        Log.d("salida","idCliente: "+idCliente);

        notificacionesArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewListaNotificacionesCliente);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        getlistaNotificacionesServicio(Integer.parseInt(idCliente));
         return vista;
    }

    private void getlistaNotificacionesServicio(int idCliente){
        String ip=getString(R.string.ipWebService);
        String url=ip+"/getallNotificacionesListServicio.php?id_cliente="+idCliente;
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
                                            Notificaciones notificaciones=null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    notificaciones=new Notificaciones();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    notificaciones.setIdNotificacion(jsonObject.optInt("id_notificacionS"));
                                                    notificaciones.setCuerpo(jsonObject.optString("cuerpo"));
                                                    notificaciones.setFecha(jsonObject.optString("fecha"));



                                                    notificacionesArrayList.add(notificaciones);
                                                }
                                                adapterNotificacion=new AdapterClienteNotificacion(notificacionesArrayList);
                                                recyclerView.setAdapter(adapterNotificacion);

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
