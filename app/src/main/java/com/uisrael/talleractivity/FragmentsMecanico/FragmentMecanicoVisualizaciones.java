package my.jviracocha.talleractivity.FragmentsMecanico;

import android.content.Context;
import android.os.Bundle;
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

import my.jviracocha.talleractivity.Adaptadores.AdapterMantenimientoVisualizacion;
import my.jviracocha.talleractivity.Entidades.Auto;
import my.jviracocha.talleractivity.Entidades.Cliente;
import my.jviracocha.talleractivity.Entidades.Mantenimiento;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentMecanicoVisualizaciones extends Fragment {

    private ArrayList<Mantenimiento> mantenimientoArrayList;
    private RecyclerView recyclerView;
    private AdapterMantenimientoVisualizacion mantenimientoVisualizacionadapter;
    private RecyclerView.LayoutManager layoutManager;

    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaSalir;

    private  int idCliente, idAuto, idCita;

    private TextView nombre,telefono,correo,marca,placa,modelo,color;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_mecanico_visualizacion, container, false);



        idCliente=getArguments().getInt("idCliente");
        idCita=getArguments().getInt("idCita");
        idAuto=getArguments().getInt("idAuto");


        mantenimientoArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewMantenimentoVisualizacion);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        gruupoBotones=vista.findViewById(R.id.grupoFabMantenimientoVisualizacionMecanico);
        btnFabaSalir=vista.findViewById(R.id.idFabMantenimientoVisualizacionSalir);


        nombre=(TextView)vista.findViewById(R.id.txtNombreClienteVisualizacion);
        telefono=(TextView)vista.findViewById(R.id.txtTelefonoClienteVisualizacion);
        correo=(TextView)vista.findViewById(R.id.txtCorreoClienteVisualizacion);
        marca=(TextView)vista.findViewById(R.id.txtMarcaAutoVisualizacion);
        placa=(TextView)vista.findViewById(R.id.txtPlacaAutoVisualizacion);
        modelo=(TextView)vista.findViewById(R.id.txtModeloAutoVisualizacion);
        color=(TextView)vista.findViewById(R.id.txtColorAutoVisualizacion);

        cargarDataCliente(idCliente);
        cargarDataAuto(idAuto);
        cargarMantenimientosVisualizacion(idCita);

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

    private void cargarDataCliente(int idCliente) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getClientebyID.php?id_cliente="+idCliente;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json

                                Cliente cliente = new Cliente();
                                try{
                                    String estado=response.getString("estado");

                                    switch (estado){

                                        case "1":
                                            //JSONObject jsonObjectDato=response.getJSONObject("dato");
                                            JSONArray json=response.optJSONArray("dato");
                                            JSONObject jsonObjectDato=null;
                                            jsonObjectDato=json.getJSONObject(0);

                                            cliente.setNombre(jsonObjectDato.getString("nombre"));
                                            cliente.setCorreo(jsonObjectDato.getString("correo"));
                                            cliente.setTelefono(jsonObjectDato.getInt("telefono"));

                                            nombre.setText(cliente.getNombre());
                                            correo.setText(cliente.getCorreo());
                                            telefono.setText(Integer.toString(cliente.getTelefono()));





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

    private void cargarDataAuto(int idAuto){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/obtenerAutoID.php?id_auto="+idAuto;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json

                                Auto auto = new Auto();
                                try{
                                    String estado=response.getString("estado");

                                    switch (estado){

                                        case "1":
                                            JSONObject jsonObjectDato=response.getJSONObject("dato");


                                            auto.setMarca(jsonObjectDato.getString("marca"));
                                            auto.setPlaca(jsonObjectDato.getString("placa"));
                                            auto.setColor(jsonObjectDato.getString("color"));
                                            auto.setModelo(jsonObjectDato.getString("modelo"));


                                           marca.setText(auto.getMarca());
                                           placa.setText(auto.getPlaca());
                                           modelo.setText(auto.getModelo());
                                           color.setText(auto.getColor());

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

    private void cargarMantenimientosVisualizacion(int idCita){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getlistMantenimientoByidCita.php?id_citas="+idCita;
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

                                                    mantenimiento.setNombre(jsonObject.getString("nombre"));
                                                    mantenimiento.setObservacionesMecanico(jsonObject.getString("observacion"));

                                                    mantenimientoArrayList.add(mantenimiento);
                                                }
                                                mantenimientoVisualizacionadapter=new AdapterMantenimientoVisualizacion(mantenimientoArrayList);
                                                recyclerView.setAdapter(mantenimientoVisualizacionadapter);

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
