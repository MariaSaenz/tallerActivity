package my.jviracocha.talleractivity.FragmentsCliente;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentClienteGestionCitas extends Fragment {
    private String correo;
    private String rol;
    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaSalir;



    private TextView idCliente,idAuto;
    private BottomNavigationView bottomNavigationView;

    sendDataGestion sendDataGestionlister;
    public  interface sendDataGestion{

        public void sendDataCitas(int idCliente);
        public  void  sendDataCitasListAgenda(int idCliente);
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            sendDataGestionlister=(sendDataGestion) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_cliente_gestion_agendamiento,container,false);

        idCliente=(TextView)vista.findViewById(R.id.txtIDclienteCita);
        //idAuto=(TextView)vista.findViewById(R.id.txtIDAutoCita);

        correo=getArguments().getString("correo");
        rol=getArguments().getString("rol");
        obtenrIDUsuario(correo,rol);

        bottomNavigationView=(BottomNavigationView)vista.findViewById(R.id.botton_navigation_cliente_agendamiento);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        /*gruupoBotones=vista.findViewById(R.id.grupoFabClienteMantenimiento);
        btnFabaSalir=vista.findViewById(R.id.idFabSalirMantenimiento);

        btnFabaSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
                gruupoBotones.collapse();
            }
        });*/



        return  vista;
    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_AgendarMantenimiento:
                            sendDataGestionlister.sendDataCitas(Integer.parseInt(idCliente.getText().toString()));
                            break;
                        case R.id.nav_listaMantenimientosAgendados:
                            sendDataGestionlister.sendDataCitasListAgenda(Integer.parseInt(idCliente.getText().toString()));

                            break;


                    }
                    return true;
                }
            };

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

                                            idClienteStr=jsonObjectDato.getString("id_cliente");
                                            Log.i("salida","idBuscadaCliente:"+idCliente.toString());

                                            int idClinte=Integer.parseInt(idClienteStr);

                                            idCliente.setText(idClienteStr);


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
