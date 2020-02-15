package my.jviracocha.talleractivity.Frgments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Adaptadores.ClienteAdapter;
import my.jviracocha.talleractivity.Entidades.Cliente;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class GestionClienteFragment extends Fragment {

    private int idCliente;

    private ArrayList<Cliente> ListaClientes;
    private RecyclerView recyclerView;
    //private RecyclerView.Adapter adapter;
    private  ClienteAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAnalytics mFirebaseAnalytics;
    //elementos del menu flotante
    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaNuevo;
    FloatingActionButton btnFabActualizar;
    FloatingActionButton btnSalirCliente;

    //elememtos dela vista de datos
    private EditText nombreCliente;
    private  TextView correoCliente;
    private  EditText telefonoCliente;
    private  EditText direccionCliente;
    private  EditText contrasenaCliente;


    sedDataClienteAdmin sedDataClienteAdminListener;
    public interface sedDataClienteAdmin{
        public void openAlerDialogUpdateCliente(int idCliente, String nombre,String contrasena,String direccion, int telefono,String correo);

    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            sedDataClienteAdminListener=(sedDataClienteAdmin) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_gestionclientes,container,false);

        ListaClientes=new ArrayList<>();
        gruupoBotones= vista.findViewById(R.id.grupoFab);
        btnFabaNuevo=vista.findViewById(R.id.idFabNuevo);
        btnFabActualizar=vista.findViewById(R.id.idFabActualizar);
        btnSalirCliente=vista.findViewById(R.id.idFabSalirCliente);
        nombreCliente=(EditText)vista.findViewById(R.id.lblNombreClienteGestion);
        correoCliente=(TextView) vista.findViewById(R.id.lblCorreoClienteGestion);
        telefonoCliente=(EditText)vista.findViewById(R.id.lblTelefonoClienteGestion);
        direccionCliente=(EditText)vista.findViewById(R.id.lblDireccionClienteGestion);
        contrasenaCliente=(EditText)vista.findViewById(R.id.lblContrasenaClienteGestion);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewClientes);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);

        cargarwebservice();

        btnFabActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    String nombre=nombreCliente.getText().toString();
                    String contrasena=contrasenaCliente.getText().toString();
                    String direccion=direccionCliente.getText().toString();
                    int telefono =Integer.parseInt(telefonoCliente.getText().toString());
                    String correo=correoCliente.getText().toString();

                    sedDataClienteAdminListener.openAlerDialogUpdateCliente(idCliente,nombre,contrasena,direccion,telefono,correo);
                    ListaClientes.clear();
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e){

                    Toast.makeText(getContext(),"Debe seleccionar un cliente",Toast.LENGTH_SHORT).show();

                }


                gruupoBotones.collapse();
                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "btnFabActualizar");
                params10.putInt("id_usuario", idCliente);
                mFirebaseAnalytics.logEvent("click_evento", params10);
                // [END custom_event]
            }
        });

        btnFabaNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gruupoBotones.collapse();
                RegistroClienteFragment registroClienteFragment= new RegistroClienteFragment();
                String evento="nuevoUserAdmin";
                Bundle bundle=new Bundle();
                bundle.putString("evento",evento);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                registroClienteFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_continer, registroClienteFragment);
                fragmentTransaction.commit();

                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "btnFabaNuevo");
                //params10.putInt("id_usuario", idCliente);
                mFirebaseAnalytics.logEvent("click_evento", params10);
                // [END custom_event]
                // [START custom_event]
                Bundle params = new Bundle();
                params.putString("fragmente", "registroClienteFragment");
                params.putString("usuario", "admin");
                mFirebaseAnalytics.logEvent("navigation", params);
                // [END custom_event]




            }
        });
        btnSalirCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();

            }
        });
        return  vista;

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.toolabar_menu,menu);

        //aqui debo agregar el codiog para la busqueda
        MenuItem item =menu.findItem(R.id.spinnerToolbar);
        item.setVisible(false);
        MenuItem serchItem=menu.findItem(R.id.menu_buscar);
        SearchView searchView=(SearchView)serchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id == R.id.menu_buscar){

            System.out.println("menu buscar, ejecutar el codigo de busqueda");
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargarwebservice() {
        String ip=getString(R.string.ipWebService);
        String url=ip+"/obtenerClientes.php";
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json
                                procesarRespuesta(response);


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

    private void procesarRespuesta(JSONObject response) {
        try{
            String estado=response.getString("estado");

            switch (estado){
                case "1":
                    Cliente cliente=null;
                    JSONArray json=response.optJSONArray("dato");
                    try {
                        for (int i=0;i<json.length();i++ )
                        {
                            cliente=new Cliente();
                            JSONObject jsonObject=null;
                            jsonObject=json.getJSONObject(i);



                            cliente.setId_cliente(jsonObject.optInt("id_cliente"));
                            cliente.setNombre(jsonObject.optString("nombre"));
                            cliente.setCorreo(jsonObject.optString("correo"));
                            cliente.setTelefono(jsonObject.optInt("telefono"));
                            cliente.setDireccion(jsonObject.optString("direccion"));
                            cliente.setContrasena(jsonObject.optString("contrasena"));

                            ListaClientes.add(cliente);

                        }

                        adapter=new ClienteAdapter(ListaClientes);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClickListener(new ClienteAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                // ListaClientes.get(position).getNombre();
                                idCliente=ListaClientes.get(position).getId_cliente();
                                nombreCliente.setText(ListaClientes.get(position).getNombre());
                                correoCliente.setText(ListaClientes.get(position).getCorreo());
                                direccionCliente.setText(ListaClientes.get(position).getDireccion());
                                telefonoCliente.setText(Integer.toString(ListaClientes.get(position).getTelefono()));
                                contrasenaCliente.setText(ListaClientes.get(position).getContrasena());
                                Log.d("salida", "Clinte seleccionado :"+idCliente);
                                Toast.makeText(getContext(),"Se ha seleccionado: "+nombreCliente.getText().toString(),Toast.LENGTH_SHORT).show();




                            }

                            @Override
                            public void onDeliteClick(int position) {

                                System.out.println(ListaClientes.get(position).getDireccion());
                                int id_Cliente=ListaClientes.get(position).getId_cliente();
                                cargarwebserviceBorrar(id_Cliente);

                                ListaClientes.clear();
                                adapter.notifyDataSetChanged();
                                //adapter=new ClienteAdapter(ListaClientes);

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

    private void cargarwebserviceBorrar(int id_cliente) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/deliteCliente.php?id_cliente="+id_cliente;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json
                                procesarBorrado(response);


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

    private void procesarBorrado(JSONObject response) {

        try{
            String estado=response.getString("estado");

            switch (estado){

                case "1":
                    Toast.makeText(getContext(),"Se ha eliminado correctamente",Toast.LENGTH_SHORT).show();

                    break;
                case "2":
                    String mensaje=response.getString("mensaje");
                    Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                    break;
                case "3":
                    String mensaje3=response.getString("mensaje");
                    Toast.makeText(getContext(),mensaje3,Toast.LENGTH_SHORT).show();
                    break;
            }
            cargarwebservice();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void cargarwebserviceActualizar(int idclienteA, String nombre, String contrasena, String direccion, int telefono, String correo) {
        String ip=getString(R.string.ipWebService);
        String url=ip+"/updateCliente.php?id_cliente="+idclienteA+"&nombre="+nombre+"&contrasena="+contrasena+"" +
                "&direccion="+direccion+"&txtoken&telefono="+telefono+"&correo="+correo;
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
                    Toast.makeText(getContext(),"Se ha actualizado correctamente",Toast.LENGTH_SHORT).show();

                    nombreCliente.setText("");
                    correoCliente.setText("");
                    direccionCliente.setText("");
                    telefonoCliente.setText("");
                    contrasenaCliente.setText("");

                    break;
                case "2":
                    String mensaje=response.getString("mensaje");
                    Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                    break;
                case "3":
                    String mensaje3=response.getString("mensaje");
                    Toast.makeText(getContext(),mensaje3,Toast.LENGTH_SHORT).show();
                    break;
            }
            cargarwebservice();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


}
