package my.jviracocha.talleractivity.Frgments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.util.ArrayList;

import my.jviracocha.talleractivity.Adaptadores.ServicioAdapter;
import my.jviracocha.talleractivity.Entidades.Servicio;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class GestionServiciosFragment extends Fragment {
    private ArrayList<Servicio> servicioArrayList;

    private RecyclerView recyclerView;
    private  ServicioAdapter adapter;
    private  RecyclerView.LayoutManager layoutManager;
    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText txtNombreServ,txtCostoServ,txtKilometrajeTope;

    FloatingActionsMenu gruupoBotonesServ;
    FloatingActionButton btnFabaNuevoServ;
    FloatingActionButton btnFabActualizarServ;
    FloatingActionButton btnSalirServicio;
    private Button guardarServicio;

    int idServicioFull;
    String boton=null;

    gestionServicio gestionServicioListener;
    public interface gestionServicio{
        public void openAlertDialogoNewServicio();
        public void openAlertDialgoUpdateServicio(String nomreServicio, int costoServicio, int kilometrajeServicio,int idServicio);

    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            gestionServicioListener=(gestionServicio)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista= inflater.inflate(R.layout.fragment_gestionservicios,container,false);

        txtNombreServ=(EditText)vista.findViewById(R.id.txtNombreRegistroServicio);
        txtCostoServ=(EditText)vista.findViewById(R.id.txtCostoRegistroServicio);
        txtKilometrajeTope=(EditText)vista.findViewById(R.id.txtKilometrajeTopeRegistroServicio);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());


        gruupoBotonesServ= vista.findViewById(R.id.grupoFabServicio);
        btnFabaNuevoServ=vista.findViewById(R.id.idFabNuevoServicio);
        btnFabActualizarServ=vista.findViewById(R.id.idFabActualizarServicio);
        guardarServicio=(Button)vista.findViewById(R.id.btGuardarServicio);
        btnSalirServicio=vista.findViewById(R.id.idFabSalirServicio);

        servicioArrayList=new ArrayList<>();

         recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewServicios);
         recyclerView.setHasFixedSize(true);
         layoutManager=new LinearLayoutManager(this.getContext());
         recyclerView.setLayoutManager(layoutManager);

         cargarwebServicios();

         btnFabaNuevoServ.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 gestionServicioListener.openAlertDialogoNewServicio();
                 gruupoBotonesServ.collapse();

                 // [START custom_event]
                 Bundle params10 = new Bundle();
                 params10.putString("button", "btnFabaNuevoServ");
                 params10.putString("id_usuario", "admin");
                 mFirebaseAnalytics.logEvent("click_evento", params10);
                // [END custom_event]
             }
         });

        btnFabActualizarServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtNombreServ.setEnabled(true);
                txtKilometrajeTope.setEnabled(true);
                txtCostoServ.setEnabled(true);

                try{
                    String nombre=txtNombreServ.getText().toString();
                    int costo=Integer.parseInt(txtCostoServ.getText().toString());
                    int kilometraje=Integer.parseInt(txtKilometrajeTope.getText().toString());
                    int idServicio=idServicioFull;
                    Log.d("salida","Datos a enviar:"+nombre+" "+costo+" "+kilometraje+" "+idServicio);
                    gestionServicioListener.openAlertDialgoUpdateServicio(nombre,costo,kilometraje,idServicio);

                }catch (Exception e){
                    String mensaje="Debe seleccionar un servicio";
                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();

                }


                gruupoBotonesServ.collapse();
                adapter.notifyDataSetChanged();
                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "btnFabActualizarServ");
                params10.putString("id_usuario", "admin");
                mFirebaseAnalytics.logEvent("click_evento", params10);
                // [END custom_event]

            }
        });


        guardarServicio.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 switch (boton){

                     case "nuevo":
                         if (txtNombreServ.getText().toString().isEmpty()) {
                             Toast.makeText(getContext(), "Campo NOMBRE esta vacio", Toast.LENGTH_LONG).show();
                         } else {
                             if (txtCostoServ.getText().toString().isEmpty()) {
                                 Toast.makeText(getContext(), "Campo COSTO esta vacio", Toast.LENGTH_LONG).show();
                             } else {
                                 if (txtKilometrajeTope.getText().toString().isEmpty()) {
                                     Toast.makeText(getContext(), "Campo KILOMETRAJE TOPE esta vacio", Toast.LENGTH_LONG).show();
                                 } else {
                                     //cargar el webservice

                                     String nombre=txtNombreServ.getText().toString();
                                     int costo=Integer.parseInt(txtCostoServ.getText().toString());
                                     int kilometraje=Integer.parseInt(txtKilometrajeTope.getText().toString());

                                     webserviceNuevoServicio(nombre, costo, kilometraje);
                                     txtNombreServ.setText("");
                                     txtCostoServ.setText("");
                                     txtKilometrajeTope.setText("");
                                     servicioArrayList.clear();

                                 }

                             }
                         }
                         break;
                     case "actualizar":
                         if (txtNombreServ.getText().toString().isEmpty()) {
                             Toast.makeText(getContext(), "Campo NOMBRE esta vacio", Toast.LENGTH_LONG).show();
                         } else {
                             if (txtCostoServ.getText().toString().isEmpty()) {
                                 Toast.makeText(getContext(), "Campo COSTO esta vacio", Toast.LENGTH_LONG).show();
                             } else {
                                 if (txtKilometrajeTope.getText().toString().isEmpty()) {
                                     Toast.makeText(getContext(), "Campo KILOMETRAJE TOPE esta vacio", Toast.LENGTH_LONG).show();
                                 } else {
                                     String nombre=txtNombreServ.getText().toString();
                                     int costo=Integer.parseInt(txtCostoServ.getText().toString());
                                     int kilometraje=Integer.parseInt(txtKilometrajeTope.getText().toString());

                                     webserviceActualizarServicio(nombre, costo, kilometraje,idServicioFull);

                                     txtNombreServ.setText("");
                                     txtCostoServ.setText("");
                                     txtKilometrajeTope.setText("");
                                     servicioArrayList.clear();

                                 }

                             }
                         }

                         break;

                 }

                guardarServicio.setVisibility(View.INVISIBLE);
             }
         });

        btnSalirServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
            }
        });


          return  vista;
    }

    private void webserviceActualizarServicio(String nombre, int costo, int kilometraje, int idServicioFull) {
        String ip=getString(R.string.ipWebService);
        String url=ip+"/updateServicio.php?nombre="+nombre+"&costo="+costo+"&kilometrajeTope="+kilometraje+"&id_servicio="+idServicioFull;
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
                                            String mensaje1=response.getString("dato");
                                            Toast.makeText(getContext(),mensaje1,Toast.LENGTH_SHORT).show();

                                            break;
                                        case "2":
                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getContext(),mensaje2,Toast.LENGTH_SHORT).show();
                                            break;



                                    }
                                    cargarwebServicios();
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

    private void webserviceNuevoServicio(String nombre, int costo, int kilometraje) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/insertServicio.php?nombre="+nombre+"&costo="+costo+"&kilometrajeTope="+kilometraje;
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
                                    cargarwebServicios();
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

    private void cargarwebServicios() {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getallServicios.php";
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                procesarlistadoServicios(response);
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

    private void procesarlistadoServicios(JSONObject response) {
        try{
            String estado=response.getString("estado");

            switch (estado){
                case "1":
                    Servicio servicio=null;
                    JSONArray json=response.optJSONArray("dato");
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            servicio=new Servicio();
                            JSONObject jsonObject=null;
                            jsonObject=json.getJSONObject(i);

                            servicio.setId_Servicio(jsonObject.optInt("id_servicio"));
                            servicio.setNombreServicio(jsonObject.optString("nombre"));
                            servicio.setCostoServicio(jsonObject.optInt("costo"));
                            servicio.setKilometrajeTope(jsonObject.optInt("kilometrajeTope"));

                            servicioArrayList.add(servicio);
                        }
                        adapter=new ServicioAdapter(servicioArrayList);
                        adapter.setOnItemClickListener(new ServicioAdapter.OnItemClickListenerServ() {
                            @Override
                            public void onItemClick(int position) {
                                txtNombreServ.setText(servicioArrayList.get(position).getNombreServicio());
                                txtCostoServ.setText(Integer.toString(servicioArrayList.get(position).getCostoServicio()));
                                txtKilometrajeTope.setText(Integer.toString(servicioArrayList.get(position).getKilometrajeTope()));

                                idServicioFull=servicioArrayList.get(position).getId_Servicio();
                                // txtNombreServ.setEnabled(false);
                                //txtKilometrajeTope.setEnabled(false);
                                //txtCostoServ.setEnabled(false);
                                Toast.makeText(getContext(),"Se ha seleccionado: "+servicioArrayList.get(position).getNombreServicio(),Toast.LENGTH_SHORT).show();

                            }
                            @Override
                            public void onDeliteItem(int position) {
                                int idServicio=servicioArrayList.get(position).getId_Servicio();
                                webserviceBorrarServicio(idServicio);
                                servicioArrayList.clear();
                                adapter.notifyDataSetChanged();

                            }
                        });
                        recyclerView.setAdapter(adapter);
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

    private void webserviceBorrarServicio(int idServicio) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/deliteServicio.php?id_servicio="+idServicio;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json
                               // procesarBorradoServicio(response);
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
                                    cargarwebServicios();
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
