package my.jviracocha.talleractivity.FragmentsMecanico;

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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import my.jviracocha.talleractivity.Entidades.Cliente;
import my.jviracocha.talleractivity.Entidades.Mecanico;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentClienteMiCuenta;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentMecanicoMiCuenta extends Fragment {

    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaGuardar,btnFabActulizar,btnFabSalir;
    private String evento,correo,rol;
    private TextView idMecanico,idAdministrador;

    private TextInputLayout txtInputLayotNombreMecanicoRegistro,
            txtInputLayotCorreoMecanicoRegistro,txtInputLayotTelefonoMecanicoRegistro,
            txtInputLayotDireccionMecanicoRegistro,txtInputLayotContrasenaMecanicoRegistro;

    sedDatafragmentMiCuentainterfaceMecanico sedDatafragmentMiCuentainterfaceMecanicoListener;
    public interface sedDatafragmentMiCuentainterfaceMecanico{
        void onsendmensaje(String mensaje);
    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            sedDatafragmentMiCuentainterfaceMecanicoListener=(sedDatafragmentMiCuentainterfaceMecanico)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_registrocliente,container,false);

        txtInputLayotNombreMecanicoRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotNombreClienteRegistro);
        txtInputLayotCorreoMecanicoRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotCorreoRegistro);
        txtInputLayotTelefonoMecanicoRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotTelefonoClienteRegistro);
        txtInputLayotDireccionMecanicoRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotDireccionClienteRegistro);
        txtInputLayotContrasenaMecanicoRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotContrasenaClienteRegistro);
        idMecanico=(TextView)vista.findViewById(R.id.txtIDclienteActulizar);
        idAdministrador=(TextView)vista.findViewById(R.id.txtIDmecanicoActulizar);

        gruupoBotones=vista.findViewById(R.id.grupofloatRegCliente);
        btnFabaGuardar=vista.findViewById(R.id.btnFlot_guardar);
        btnFabActulizar=vista.findViewById(R.id.btnFlot_actualizarCliente);
        btnFabSalir=vista.findViewById(R.id.btnFlot_salirCliente);

        btnFabaGuardar.setVisibility(vista.INVISIBLE);
        btnFabActulizar.setVisibility(vista.VISIBLE);
        btnFabSalir.setVisibility(vista.VISIBLE);

        correo=getArguments().getString("correo");
        rol=getArguments().getString("rol");
        obtenrIDUsuario(correo,rol);

        btnFabActulizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarImput(v,Integer.parseInt(idMecanico.getText().toString().trim()),Integer.parseInt(idAdministrador.getText().toString().trim()));
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

        return  vista;
    }

    private boolean validarnombre(){
        Pattern patron =Pattern.compile("^[a-zA-Z ]+$");
        String nombre=txtInputLayotNombreMecanicoRegistro.getEditText().getText().toString().trim();
        if(nombre.isEmpty()){
            txtInputLayotNombreMecanicoRegistro.setError("Debe ingresar su nombre");
            return false;
        }else if(!patron.matcher(nombre).matches()||nombre.length()>30){
            txtInputLayotNombreMecanicoRegistro.setError("Nombre Invalido");
            return false;
        }else {
            txtInputLayotNombreMecanicoRegistro.setError(null);
            return true;
        }
    }
    private boolean validarcorreo(){

        String mail=txtInputLayotCorreoMecanicoRegistro.getEditText().getText().toString().trim();
        if(mail.isEmpty()){
            txtInputLayotCorreoMecanicoRegistro.setError("Necesita correo");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            txtInputLayotCorreoMecanicoRegistro.setError("Correo incorrecto");
            return false;

        }else  {
            txtInputLayotCorreoMecanicoRegistro.setError(null);
            //textInputEditTextCorreo.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validartelefono(){
        String telefono=txtInputLayotTelefonoMecanicoRegistro.getEditText().getText().toString().trim();
        if(telefono.isEmpty()){
            txtInputLayotTelefonoMecanicoRegistro.setError("Debe ingresar una teléfono");
            return false;
        }
        else if(!Patterns.PHONE.matcher(telefono).matches()){
            txtInputLayotTelefonoMecanicoRegistro.setError("Telefono Invalido");
            return false;
        }else {
            txtInputLayotTelefonoMecanicoRegistro.setError(null);
            return true;
        }
    }
    private boolean valiardierccion(){

        String direccion=txtInputLayotDireccionMecanicoRegistro.getEditText().getText().toString().trim();
        if(direccion.isEmpty()){
            txtInputLayotDireccionMecanicoRegistro.setError("Debe ingresar su dirección");
            return false;
        }else {

            txtInputLayotDireccionMecanicoRegistro.setError(null);
            return true;
        }
    }
    private boolean validarcontrasena(){

        String clave=txtInputLayotContrasenaMecanicoRegistro.getEditText().getText().toString().trim();
        if(clave.isEmpty()){
            txtInputLayotContrasenaMecanicoRegistro.setError("Debe escribir su password");
            return false;
        }else {
            txtInputLayotContrasenaMecanicoRegistro.setError("");
            //textInputEditTextContrasena.setErrorEnabled(false);
            return true;
        }
    }

    public void confirmarImput(View view, int idCliente,int idadmin){
        if(validarnombre()&& validarcorreo()&&validartelefono()&& valiardierccion()&& validarcontrasena()){

            String nombre =txtInputLayotNombreMecanicoRegistro.getEditText().getText().toString().trim();
            String correo=txtInputLayotCorreoMecanicoRegistro.getEditText().getText().toString().trim();
            String contrasena=txtInputLayotContrasenaMecanicoRegistro.getEditText().getText().toString().trim();
            String direccion=txtInputLayotDireccionMecanicoRegistro.getEditText().getText().toString().trim();
            String telefono=txtInputLayotTelefonoMecanicoRegistro.getEditText().getText().toString().trim();
            webserviceActualizarMecanico(idCliente,correo,Integer.parseInt(telefono),contrasena,nombre,idadmin);

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

                                            idClienteStr=jsonObjectDato.getString("id_mecanico");

                                            int idClinte=Integer.parseInt(idClienteStr);

                                            idMecanico.setText(idClienteStr);

                                            cargardatosCliente(idClinte);


                                            break;
                                        case "2":
                                            String mensaje=response.getString("mensaje");
                                            sedDatafragmentMiCuentainterfaceMecanicoListener.onsendmensaje(mensaje);

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

                                sedDatafragmentMiCuentainterfaceMecanicoListener.onsendmensaje(text.toString());
                            }
                        }
                ));


    }
    private void cargardatosCliente(int idCliente){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/obtenerMecanico.php?id_mecanico="+idCliente;
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

                                            Mecanico mecanico =null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    mecanico=new Mecanico();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    mecanico.setNombre(jsonObject.optString("nombre"));
                                                    mecanico.setCorreo(jsonObject.optString("correo"));
                                                    mecanico.setTelefono(jsonObject.optInt("telefono"));
                                                    mecanico.setId_administrador(jsonObject.optInt("id_administrador"));
                                                    mecanico.setContrasena(jsonObject.optString("contrasena"));

                                                }

                                                txtInputLayotNombreMecanicoRegistro.getEditText().setText(mecanico.getNombre());
                                                txtInputLayotCorreoMecanicoRegistro.getEditText().setText(mecanico.getCorreo());
                                                String telefono=Integer.toString(mecanico.getTelefono());
                                                txtInputLayotTelefonoMecanicoRegistro.getEditText().setText(telefono);
                                                txtInputLayotDireccionMecanicoRegistro.getEditText().setText("no presenta direccion");
                                                txtInputLayotContrasenaMecanicoRegistro.getEditText().setText(mecanico.getContrasena());
                                                idAdministrador.setText(Integer.toString(mecanico.getId_administrador()));

                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }

                                            break;
                                        case "2":

                                            String mensaje2=response.getString("dato");
                                            sedDatafragmentMiCuentainterfaceMecanicoListener.onsendmensaje(mensaje2);
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
                                sedDatafragmentMiCuentainterfaceMecanicoListener.onsendmensaje(text.toString());

                            }
                        }
                ));

    }

    private void webserviceActualizarMecanico(int idMecanico, String correoMec, int telefonoMec, String contrasenaMec, String nombreMec, int idAdmin) {


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
                                sedDatafragmentMiCuentainterfaceMecanicoListener.onsendmensaje(text.toString());
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
                    sedDatafragmentMiCuentainterfaceMecanicoListener.onsendmensaje(mensaje1);

                    break;
                case "2":
                    String mensaje=response.getString("dato");
                    sedDatafragmentMiCuentainterfaceMecanicoListener.onsendmensaje(mensaje);
                    break;
                case "3":
                    String mensaje3=response.getString("dato");
                    sedDatafragmentMiCuentainterfaceMecanicoListener.onsendmensaje(mensaje3);

                    break;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}
