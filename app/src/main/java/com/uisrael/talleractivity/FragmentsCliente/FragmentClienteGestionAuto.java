package my.jviracocha.talleractivity.FragmentsCliente;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

import my.jviracocha.talleractivity.Adaptadores.AutoAdapter;
import my.jviracocha.talleractivity.Entidades.Auto;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentClienteGestionAuto extends Fragment  {

   private  static final String TAG="miFragmente";
    private String correo;
    private String rol;

    private Spinner spinner;
    private BottomNavigationView bottomNavigationView;

    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaNuevoAuto;
    FloatingActionButton btnFabNuevoKilometraje;
    FloatingActionButton btnSalirAutoCliente;

    private TextView idClienteTrue;
    private  TextView idAutoP;
    private FirebaseAnalytics mFirebaseAnalytics;


    public  ArrayList<Auto> autoArrayList;
    AutoAdapter autoAdapter;

    sedIdClienteListener envioIDSedlistener;



    public  interface sedIdClienteListener{

        public  void sedIdCliente(int idCliente);
        public void  sedIdClienteAuto(int idCliente);
        public  void  sedIdAuto(int idAutoP);
        public  void sendDataKMInsert(int idAuto,int idCliente);
        public  void sendDataKMList(int idAuto, int idCliente);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            envioIDSedlistener=(sedIdClienteListener) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragement_cliente_gestionauto,container,false);

        idClienteTrue=(TextView)vista.findViewById(R.id.txtIDcliente);
        idAutoP=(TextView)vista.findViewById(R.id.txtIDAutoP);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        bottomNavigationView=(BottomNavigationView)vista.findViewById(R.id.botton_navigation_cliente);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        ///<Sumary>
        //gargamso los datos que nos vienen desde el mainactivity como es el correo y el rol para usar un metodo que permita obtener el ID de Cliente
        correo=getArguments().getString("correo");
        rol=getArguments().getString("rol");
        obtenrIDUsuario(correo,rol);
        ///<Sumary>
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerCliente,new FragmenteResumenAuto()).addToBackStack(null).commit();


        gruupoBotones= vista.findViewById(R.id.grupoFabCliente);
        btnFabaNuevoAuto=vista.findViewById(R.id.idFabNuevoAuto);
        btnFabNuevoKilometraje=vista.findViewById(R.id.idFabNuevaLecturaKM);
        btnSalirAutoCliente=vista.findViewById(R.id.idFabSalirLecturaKM);

        btnFabaNuevoAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uso interfaz para pasar datos al alert dialogo


                envioIDSedlistener.sedIdCliente(Integer.parseInt(idClienteTrue.getText().toString()));

                gruupoBotones.collapse();
                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "btnFabaNuevoAuto");
                params10.putString("id_usuario", correo);
                mFirebaseAnalytics.logEvent("click_evento", params10);
                // [END custom_event]
            }
        });

        btnFabNuevoKilometraje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uso de interfaz para enviar idAuto, idCliente al alert dialogo
                try{
                    envioIDSedlistener.sendDataKMInsert(Integer.parseInt(idAutoP.getText().toString()),Integer.parseInt(idClienteTrue.getText().toString()));
                }catch(Exception a){
                    Toast.makeText(getContext(),"No ha seleccionado ningún auto",Toast.LENGTH_SHORT).show();
                }

                gruupoBotones.collapse();
                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "btnFabNuevoKilometraje");
                params10.putString("id_usuario", correo);
                mFirebaseAnalytics.logEvent("click_evento", params10);
                // [END custom_event]
            }
        });

        btnSalirAutoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
            }
        });

        return  vista;

    }




    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragmentSelected=null;
                    switch (menuItem.getItemId()){
                        case R.id.nav_resume:
                            //envioIDSedlistener.sedIdAuto();
                            try{
                                envioIDSedlistener.sedIdAuto(Integer.parseInt(idAutoP.getText().toString()));
                            }catch (Exception a){
                                Toast.makeText(getContext(),"no se han registrado datos",Toast.LENGTH_SHORT).show();
                            }
                            // [START custom_event]
                            Bundle params = new Bundle();
                            params.putString("fragmente", "nav_resume");
                            params.putString("usuario", correo);
                            mFirebaseAnalytics.logEvent("navigation", params);
                            // [END custom_event]

                            break;
                        case R.id.nav_listaAutos:

                            envioIDSedlistener.sedIdClienteAuto(Integer.parseInt(idClienteTrue.getText().toString()));

                            // [START custom_event]
                            Bundle params1 = new Bundle();
                            params1.putString("fragmente", "nav_listaAutos");
                            params1.putString("usuario", correo);
                            mFirebaseAnalytics.logEvent("navigation", params1);
                            // [END custom_event]
                            break;
                        case R.id.nav_kilometrejes:
                            //fragmentSelected=new FragmentKilometrajeCliente();
                            try{
                                envioIDSedlistener.sendDataKMList(Integer.parseInt(idAutoP.getText().toString()),Integer.parseInt(idClienteTrue.getText().toString()));
                            }catch (Exception a){
                                Toast.makeText(getContext(),"no se han registrado datos",Toast.LENGTH_SHORT).show();
                            }
                            // [START custom_event]
                            Bundle params2 = new Bundle();
                            params2.putString("fragmente", "nav_listaAutos");
                            params2.putString("usuario", correo);
                            mFirebaseAnalytics.logEvent("navigation", params2);
                            // [END custom_event]
                            //envioIDSedlistener.sendDataKMList(Integer.parseInt(idAutoP.getText().toString()),Integer.parseInt(idClienteTrue.getText().toString()));
                            break;

                    }

//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerCliente,fragmentSelected).addToBackStack(null).commit();

                return true;
                }
            };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        autoArrayList=new ArrayList<>();
        inflater.inflate(R.menu.toolabar_menu,menu);
        MenuItem item =menu.findItem(R.id.spinnerToolbar);
        MenuItem serchItem=menu.findItem(R.id.menu_buscar);
        serchItem.setVisible(false);

        spinner=(Spinner)item.getActionView();


        //cargarListaAutos(1);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void cargarListaAutos(int idCliente){


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
                                                autoAdapter=new AutoAdapter(getContext(),autoArrayList);

                                                spinner.setAdapter(autoAdapter);
                                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        //escogemos el auto a gestionar de momento

                                                        int idAuto=autoArrayList.get(position).getId_auto();
                                                        idAutoP.setText(Integer.toString(idAuto));

                                                        System.out.println("Id del auto a evaluar:"+idAuto);


                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });


                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
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

    public void obtenrIDUsuario(String correo, String rol) {


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

                                            idClienteStr=jsonObjectDato.getString("id_cliente");

                                           int idClinte=Integer.parseInt(idClienteStr);

                                            idClienteTrue.setText(idClienteStr);

                                            cargarListaAutos(idClinte);


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






}
