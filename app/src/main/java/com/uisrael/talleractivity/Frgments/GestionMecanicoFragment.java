package my.jviracocha.talleractivity.Frgments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import my.jviracocha.talleractivity.Adaptadores.ClienteAdapter;
import my.jviracocha.talleractivity.Adaptadores.MecanicioAdapter;
import my.jviracocha.talleractivity.Entidades.Mecanico;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;


public class GestionMecanicoFragment extends Fragment {
    ArrayList<Mecanico> mecanicoArrayList;


    private RecyclerView recyclerView;

    //private  RecyclerView.Adapter adapter; esto es cambiado para el click listner de la lista
    //*******************2doParte*************************************
    private MecanicioAdapter adapter;
    //****************************************************************

    private RecyclerView.LayoutManager layoutManager;

    //*****************enviar datos al otro fragmento*****************************

    OnRegistroMecanicoSedListener registroMecanicoSedListener;

    public  interface OnRegistroMecanicoSedListener{

        public void onRegistroMecanicoSend(String datosMecanico,String correo);
        public  void openDialogoCreateMecanico(int idAdministrador);
        public void openDialogoUpdateMecanico(int idMecanico,String correoMec,int telefonoMec,String contrasenaMec, String nombreMec,int idAdmin);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            registroMecanicoSedListener=(OnRegistroMecanicoSedListener)activity;
        }catch (ClassCastException e){

           throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    //aqui puedo limpiar los datos

    }

    //*****************************************************************************

    //******************menu flotante*********************************************

    //elementos del menu flotante
    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaNuevo;
    FloatingActionButton btnFabActualizar;
    FloatingActionButton btnSalirMecanico;

    private EditText lblNombreMecanico;
    private EditText lblCorreoMecanico;
    private  EditText lblTelefonoMecanico;
    private  EditText lblContrasenaMecanico;
    private Button btnGuardarMecanico;

    private int idMecanico;

    //*****************************************************************************
    private TextView idAdm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       final View vista=inflater.inflate(R.layout.fragment_gestionmecanicos,container,false);

        mecanicoArrayList=new ArrayList<>();

        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewMecanicos);
        recyclerView.setHasFixedSize(true);//esta debo tener cuidado para que sirve
        layoutManager =new LinearLayoutManager(this.getContext());
        //adapter=new MecanicioAdapter(mecanicoArrayList);

        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(adapter);
        cargarwebserviceMecanico();

        //************************Menu flotante*******************************

        lblNombreMecanico=(EditText)vista.findViewById(R.id.lblNombreMecanicoRegistro);
        lblCorreoMecanico=(EditText) vista.findViewById(R.id.lblCorreoMecanicioRegistro);
        lblTelefonoMecanico=(EditText)vista.findViewById(R.id.lblTelefonoMecanicoRegistro);
        lblContrasenaMecanico=(EditText)vista.findViewById(R.id.lblContrasenaMecanicoRegistro);
        btnGuardarMecanico=(Button)vista.findViewById(R.id.btGuardarMecanico);

        gruupoBotones= vista.findViewById(R.id.grupoFabMecanico);
        btnFabaNuevo=vista.findViewById(R.id.idFabNuevoMecanico);
        btnFabActualizar=vista.findViewById(R.id.idFabActualizarMecanico);
        btnSalirMecanico=vista.findViewById(R.id.idFabSalirMecanico);

        btnFabActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String nombreMec = lblNombreMecanico.getText().toString();
                    String correoMec = lblCorreoMecanico.getText().toString();
                    int telefonoMec = Integer.parseInt(lblTelefonoMecanico.getText().toString());
                    String contrasenaMec = lblContrasenaMecanico.getText().toString();
                    int idAdmin = Integer.parseInt(idAdm.getText().toString());

                   registroMecanicoSedListener.openDialogoUpdateMecanico(idMecanico, correoMec, telefonoMec, contrasenaMec, nombreMec, idAdmin);
                    mecanicoArrayList.clear();
                    gruupoBotones.collapse();
                }
                catch (Exception a){
                    Toast.makeText(getContext(), "Debe escoger un mecanico", Toast.LENGTH_LONG).show();

                }

            }
        });

        btnFabaNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int idAdmin = Integer.parseInt(idAdm.getText().toString());
                registroMecanicoSedListener.openDialogoCreateMecanico(idAdmin);


                //lblCorreoMecanico.setEnabled(true);

                lblNombreMecanico.setText("");
                lblCorreoMecanico.setText("");
                lblTelefonoMecanico.setText("");
                lblContrasenaMecanico.setText("");

                btnGuardarMecanico.setVisibility(View.VISIBLE);





            }
        });

        btnGuardarMecanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (lblNombreMecanico.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Campo NOMBRE esta vacio", Toast.LENGTH_LONG).show();
                } else {
                    if (lblCorreoMecanico.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Campo CORREO esta vacio", Toast.LENGTH_LONG).show();
                    } else {
                        if (lblTelefonoMecanico.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "Campo TELEFONO esta vacio", Toast.LENGTH_LONG).show();
                        } else {
                            if (lblContrasenaMecanico.getText().toString().isEmpty()) {
                                Toast.makeText(getContext(), "Campo CONTRASENA esta vacio", Toast.LENGTH_LONG).show();
                            } else {
                                //cargar el webservice
                                System.out.println("guardamos el nuevo cliente");
                                String nombreMec = lblNombreMecanico.getText().toString();
                                String correoMec = lblCorreoMecanico.getText().toString();
                                int telefonoMec = Integer.parseInt(lblTelefonoMecanico.getText().toString());
                                String contrasenaMec = lblContrasenaMecanico.getText().toString();
                                int idAdmin = Integer.parseInt(idAdm.getText().toString());

                                //webserviceNuevoMecanico(nombreMec, correoMec, contrasenaMec, telefonoMec, idAdmin);
                            }
                        }

                    }
                }

                mecanicoArrayList.clear();

            }
        });

        btnSalirMecanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
            }
        });


        //***************************************************************************




        //*********************ESTE CODIGO NO SE TOCA************************//
        idAdm=(TextView)vista.findViewById(R.id.lblIdAdministrador);
        String correo=getArguments().getString("correo");
        String rol=getArguments().getString("rol");
        obtenrIDUsuario(correo,rol);
        //System.out.println(idAdm.getText());
        //*******************************************************************//







       return  vista;

    }

    private void webserviceNuevoMecanico(String nombreMec,String correoMec, String contrasenaMec, int telefonoMec, int idAdmin) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/insertarMecanico.php?nombre="+nombreMec+"&correo="+correoMec+"&contrasena="+contrasenaMec+"&telefono="+telefonoMec+"&id_administrador="+idAdmin;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json
                                procesoNuevoCliente(response);


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

    private void procesoNuevoCliente(JSONObject response) {
        try{
            String estado=response.getString("estado");

            switch (estado){

                case "1":
                    String mensaje1=response.getString("dato");
                    Toast.makeText(getContext(),mensaje1,Toast.LENGTH_LONG).show();
                    break;
                case "2":
                    String mensaje=response.getString("dato");
                    Toast.makeText(getContext(),mensaje,Toast.LENGTH_LONG).show();
                    break;
                case "3":
                    String mensaje3=response.getString("dato");
                    Toast.makeText(getContext(),mensaje3,Toast.LENGTH_LONG).show();

                    lblNombreMecanico.setText("");
                    lblCorreoMecanico.setText("");
                    lblTelefonoMecanico.setText("");
                    lblContrasenaMecanico.setText("");
                    break;
                case "4":
                    String mensaje4=response.getString("dato");
                    Toast.makeText(getContext(),mensaje4,Toast.LENGTH_LONG).show();
                    break;
                case "5":
                    String mensaje5=response.getString("dato");
                    Toast.makeText(getContext(),mensaje5,Toast.LENGTH_LONG).show();
                    break;
            }
            cargarwebserviceMecanico();
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void webserviceActualizarMecanico(int idMecanico, String correoMec, int telefonoMec, String contrasenaMec, String nombreMec, int idAdmin) {

        int id;
        String ip=getString(R.string.ipWebService);
        String url=ip+"/ActualizarMecanico.php?id_mecanico="+idMecanico+"&correo="+correoMec+"" +
                "&telefono="+telefonoMec+"&contrasena="+contrasenaMec+"&nombre="+nombreMec+"&id_administrador="+idAdmin;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json
                                procesoActualizar(response);


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

    private void procesoActualizar(JSONObject response) {
        try{
            String estado=response.getString("estado");

            switch (estado){

                case "1":
                    String mensaje1=response.getString("dato");
                    Toast.makeText(getContext(),mensaje1,Toast.LENGTH_SHORT).show();

                    lblNombreMecanico.setText("");
                    lblCorreoMecanico.setText("");
                    lblTelefonoMecanico.setText("");
                    lblContrasenaMecanico.setText("");


                    break;
                case "2":
                    String mensaje=response.getString("dato");
                    Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                    break;
                case "3":
                    String mensaje3=response.getString("dato");
                    Toast.makeText(getContext(),mensaje3,Toast.LENGTH_SHORT).show();

                    lblNombreMecanico.setText("");
                    lblCorreoMecanico.setText("");
                    lblTelefonoMecanico.setText("");
                    lblContrasenaMecanico.setText("");
                    break;
            }
            cargarwebserviceMecanico();
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void cargarwebserviceMecanico() {
        String ip=getString(R.string.ipWebService);
        String url=ip+"/obtenerMecanicos.php";
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json
                                procesarlistadoMecanicos(response);


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

    private  void  procesarlistadoMecanicos(JSONObject response){
        try{
            String estado=response.getString("estado");

            switch (estado){
                case "1":
                    Mecanico mecanico=null;
                    JSONArray json=response.optJSONArray("dato");
                    try{
                        for (int i=0;i<json.length();i++ )
                        {
                            mecanico=new Mecanico();
                            JSONObject jsonObject=null;
                            jsonObject=json.getJSONObject(i);

                            mecanico.setId_mecanico(jsonObject.optInt("id_mecanico"));
                            mecanico.setNombre(jsonObject.optString("nombre"));
                            mecanico.setCorreo(jsonObject.optString("correo"));
                            mecanico.setTelefono(jsonObject.optInt("telefono"));
                            mecanico.setContrasena(jsonObject.optString("contrasena"));
                            mecanico.setRol(jsonObject.optString("rol"));

                            mecanicoArrayList.add(mecanico);

                        }
                        adapter=new MecanicioAdapter(mecanicoArrayList);

                        //**********************2parte MOSTRAT EL MECANICO DENTRO DEL FRAGMENT*******************

                        //adapter.setOnItemClickListener(new MecanicioAdapter.OnItemClickListenerM() { antes de llamar al metodo borrar
                        adapter.setOnItemClickListener(new MecanicioAdapter.OnItemClickListenerM() {
                            @Override
                            public void onItemClick(int position) {
                                //System.out.println(mecanicoArrayList.get(position).getId_mecanico());
                                //envio de datos a otro fragment
                                // registroMecanicoSedListener.onRegistroMecanicoSend(mecanicoArrayList.get(position).getNombre());
                                // registroMecanicoSedListener.onRegistroMecanicoSend(mecanicoArrayList.get(position).getNombre(),mecanicoArrayList.get(position).getCorreo());

                                Toast.makeText(getContext(),"Se ha seleccionado: "+mecanicoArrayList.get(position).getNombre(),Toast.LENGTH_SHORT).show();

                                //**************************************MENU FLOTANTE **************************************
                                lblCorreoMecanico.setEnabled(false);
                                idMecanico=mecanicoArrayList.get(position).getId_mecanico();
                                lblNombreMecanico.setText(mecanicoArrayList.get(position).getNombre());
                                lblCorreoMecanico.setText(mecanicoArrayList.get(position).getCorreo());
                                lblTelefonoMecanico.setText(Integer.toString(mecanicoArrayList.get(position).getTelefono()));
                                lblContrasenaMecanico.setText(mecanicoArrayList.get(position).getContrasena());

                                //******************************************************************************************

                            }

                            //***********************************imagen borrar*********************************
                            @Override
                            public void ooDeliteClick(int position) {
                                int id_mecanico=mecanicoArrayList.get(position).getId_mecanico();
                                cargarwebserviceBorrarMecanico(id_mecanico);

                                lblNombreMecanico.setText("");
                                lblCorreoMecanico.setText("");
                                lblTelefonoMecanico.setText("");
                                lblContrasenaMecanico.setText("");

                                mecanicoArrayList.clear();
                            }
                            //*********************************************************************************

                            //***********************************mantenimientos Imagen***************************

                            @Override
                            public void onShowMantenimientos(int position) {

                                registroMecanicoSedListener.onRegistroMecanicoSend(mecanicoArrayList.get(position).getRol(),mecanicoArrayList.get(position).getCorreo());

                            }


                            //***********************************************************************************
                        });

                        //****************************************************************************************

                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

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

    private void cargarwebserviceBorrarMecanico(int id_mecanico) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/EliminarMecanico.php?id_mecanico="+id_mecanico;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json
                                procesarBorradoMecanico(response);


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

    private void procesarBorradoMecanico(JSONObject response) {
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
                case "4":
                    String mensaje4=response.getString("dato");
                    Toast.makeText(getContext(),mensaje4,Toast.LENGTH_SHORT).show();
                    break;

            }
            cargarwebserviceMecanico();
        }catch (JSONException e){
            e.printStackTrace();
        }
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
                                 idAdm.setText(procesarRespuesta(response));

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
                    //usuario.setCorreo(jsonObjectDato.getString("correo"));
                    //usuario.setRol(jsonObjectDato.getString("rol"));

                    idAdministrador=jsonObjectDato.getString("id_administrador");

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


}
