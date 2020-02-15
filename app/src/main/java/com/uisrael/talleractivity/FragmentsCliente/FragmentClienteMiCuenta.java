package my.jviracocha.talleractivity.FragmentsCliente;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import my.jviracocha.talleractivity.Adaptadores.AdapterKilometraje;
import my.jviracocha.talleractivity.Entidades.Cliente;
import my.jviracocha.talleractivity.Entidades.Kilometraje;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;



public class FragmentClienteMiCuenta extends Fragment {

    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaGuardar,btnFabActulizar,btnFabSalir;
    private String evento,correo,rol;
    private TextView idCliente;

    private TextInputEditText txtCorreoRegistro;

    private TextInputLayout txtInputLayotNombreClienteRegistro,
            txtInputLayotCorreoRegistro,txtInputLayotTelefonoClienteRegistro,
            txtInputLayotDireccionClienteRegistro,txtInputLayotContrasenaClienteRegistro;

    sedDatafragmentMiCuentainterface sedDatafragmentMiCuentainterfaceListener;
    public interface sedDatafragmentMiCuentainterface{
        void onsendmensaje(String mensaje);
    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            sedDatafragmentMiCuentainterfaceListener=(sedDatafragmentMiCuentainterface)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_registrocliente,container,false);

        txtInputLayotNombreClienteRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotNombreClienteRegistro);
        txtInputLayotCorreoRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotCorreoRegistro);
        txtInputLayotTelefonoClienteRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotTelefonoClienteRegistro);
        txtInputLayotDireccionClienteRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotDireccionClienteRegistro);
        txtInputLayotContrasenaClienteRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotContrasenaClienteRegistro);
        idCliente=(TextView)vista.findViewById(R.id.txtIDclienteActulizar);
        txtCorreoRegistro=(TextInputEditText)vista.findViewById(R.id.txtCorreoRegistro);

        correo=getArguments().getString("correo");
        rol=getArguments().getString("rol");
        obtenrIDUsuario(correo,rol);

        gruupoBotones=vista.findViewById(R.id.grupofloatRegCliente);
        btnFabaGuardar=vista.findViewById(R.id.btnFlot_guardar);
        btnFabActulizar=vista.findViewById(R.id.btnFlot_actualizarCliente);
        btnFabSalir=vista.findViewById(R.id.btnFlot_salirCliente);

        btnFabaGuardar.setVisibility(vista.INVISIBLE);
        btnFabActulizar.setVisibility(vista.VISIBLE);
        btnFabSalir.setVisibility(vista.VISIBLE);

        txtCorreoRegistro.setEnabled(false);

        btnFabActulizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarImput(v,Integer.parseInt(idCliente.getText().toString().trim()));
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
            }
        });

        btnFabSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
            }
        });


    return vista;
    }

    private boolean validarnombre(){
        Pattern patron =Pattern.compile("^[a-zA-Z ]+$");
        String nombre=txtInputLayotNombreClienteRegistro.getEditText().getText().toString().trim();
        if(nombre.isEmpty()){
            txtInputLayotNombreClienteRegistro.setError("Debe ingresar su nombre");
            return false;
        }else if(!patron.matcher(nombre).matches()||nombre.length()>30){
            txtInputLayotNombreClienteRegistro.setError("Nombre Invalido");
            return false;
        }else {
            txtInputLayotNombreClienteRegistro.setError(null);
            return true;
        }
    }
    private boolean validarcorreo(){

        String mail=txtInputLayotCorreoRegistro.getEditText().getText().toString().trim();
        if(mail.isEmpty()){
            txtInputLayotCorreoRegistro.setError("Necesita correo");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            txtInputLayotCorreoRegistro.setError("Correo incorrecto");
            return false;

        }else  {
            txtInputLayotCorreoRegistro.setError(null);
            //textInputEditTextCorreo.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validartelefono(){
        String telefono=txtInputLayotTelefonoClienteRegistro.getEditText().getText().toString().trim();
        if(telefono.isEmpty()){
            txtInputLayotTelefonoClienteRegistro.setError("Debe ingresar una teléfono");
            return false;
        }
        else if(!Patterns.PHONE.matcher(telefono).matches()){
            txtInputLayotTelefonoClienteRegistro.setError("Telefono Invalido");
            return false;
        }else {
            txtInputLayotTelefonoClienteRegistro.setError(null);
            return true;
        }
    }
    private boolean valiardierccion(){

        String direccion=txtInputLayotDireccionClienteRegistro.getEditText().getText().toString().trim();
        if(direccion.isEmpty()){
            txtInputLayotDireccionClienteRegistro.setError("Debe ingresar su dirección");
            return false;
        }else {

            txtInputLayotDireccionClienteRegistro.setError(null);
            return true;
        }
    }
    private boolean validarcontrasena(){

        String clave=txtInputLayotContrasenaClienteRegistro.getEditText().getText().toString().trim();
        if(clave.isEmpty()){
            txtInputLayotContrasenaClienteRegistro.setError("Debe escribir su password");
            return false;
        }else {
            txtInputLayotContrasenaClienteRegistro.setError("");
            //textInputEditTextContrasena.setErrorEnabled(false);
            return true;
        }
    }

    public void confirmarImput(View view, int idCliente){
        if(validarnombre()&& validarcorreo()&&validartelefono()&& valiardierccion()&& validarcontrasena()){

            String nombre =txtInputLayotNombreClienteRegistro.getEditText().getText().toString().trim();
            String correo=txtInputLayotCorreoRegistro.getEditText().getText().toString().trim();
            String contrasena=txtInputLayotContrasenaClienteRegistro.getEditText().getText().toString().trim();
            String direccion=txtInputLayotDireccionClienteRegistro.getEditText().getText().toString().trim();
            String telefono=txtInputLayotTelefonoClienteRegistro.getEditText().getText().toString().trim();
            cargarwebserviceActualizar(idCliente,nombre,contrasena,direccion,Integer.parseInt(telefono),correo);

        }
        else {
            String mensaje="Datos ingresador incorrectos";
            Context context = getContext();
            CharSequence text=mensaje ;
            int duracion= Toast.LENGTH_SHORT;
            Toast toast=Toast.makeText(context,text,duracion);
            toast.show();
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
                                //onSendMenssageUpdateClienteListener.onsendmensaje(text.toString());
                            }
                        }
                ));
    }
    private void procesoActualizar(JSONObject response) {
        try{
            String estado=response.getString("estado");

            switch (estado){

                case "1":
                    String mensaje4=response.getString("dato");
                    sedDatafragmentMiCuentainterfaceListener.onsendmensaje(mensaje4);
                    break;
                case "2":
                    String mensaje=response.getString("dato");
                    sedDatafragmentMiCuentainterfaceListener.onsendmensaje(mensaje);

                break;
                case "3":
                    String mensaje3=response.getString("dato");
                    sedDatafragmentMiCuentainterfaceListener.onsendmensaje(mensaje3);

                break;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

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

                                            int idClinte=Integer.parseInt(idClienteStr);

                                            idCliente.setText(idClienteStr);

                                            cargardatosCliente(idClinte);


                                            break;
                                        case "2":
                                            String mensaje=response.getString("mensaje");
                                            sedDatafragmentMiCuentainterfaceListener.onsendmensaje(mensaje);

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

                                sedDatafragmentMiCuentainterfaceListener.onsendmensaje(text.toString());
                            }
                        }
                ));


    }

    private void cargardatosCliente(int idCliente){

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

                                                    cliente.setNombre(jsonObject.optString("nombre"));
                                                    cliente.setCorreo(jsonObject.optString("correo"));
                                                    cliente.setTelefono(jsonObject.optInt("telefono"));
                                                    cliente.setDireccion(jsonObject.optString("direccion"));
                                                    cliente.setContrasena(jsonObject.optString("contrasena"));

                                                }

                                                txtInputLayotNombreClienteRegistro.getEditText().setText(cliente.getNombre());
                                                txtInputLayotCorreoRegistro.getEditText().setText(cliente.getCorreo());
                                                String telefono=Integer.toString(cliente.getTelefono());
                                                txtInputLayotTelefonoClienteRegistro.getEditText().setText(telefono);
                                                txtInputLayotDireccionClienteRegistro.getEditText().setText(cliente.getDireccion());
                                                txtInputLayotContrasenaClienteRegistro.getEditText().setText(cliente.getContrasena());

                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }

                                            break;
                                        case "2":

                                            String mensaje2=response.getString("dato");
                                            sedDatafragmentMiCuentainterfaceListener.onsendmensaje(mensaje2);
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
                                sedDatafragmentMiCuentainterfaceListener.onsendmensaje(text.toString());

                            }
                        }
                ));

    }

}
