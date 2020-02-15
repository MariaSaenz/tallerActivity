package my.jviracocha.talleractivity.Frgments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;
import my.jviracocha.talleractivity.loginActivity;

public class RegistroClienteFragment extends Fragment {



    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaGuardar,btnFabActulizar,btnFabSalir;
    private String evento;

    private TextInputLayout txtInputLayotNombreClienteRegistro,
            txtInputLayotCorreoRegistro,txtInputLayotTelefonoClienteRegistro,
            txtInputLayotDireccionClienteRegistro,txtInputLayotContrasenaClienteRegistro;

    private EditText nombreCliente;
    private EditText correoCliente;
    private EditText telefonoCliente;
    private EditText direccionCliente;
    private EditText contrasenaCliente;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_registrocliente, container, false);



        txtInputLayotNombreClienteRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotNombreClienteRegistro);
        txtInputLayotCorreoRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotCorreoRegistro);
        txtInputLayotTelefonoClienteRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotTelefonoClienteRegistro);
        txtInputLayotDireccionClienteRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotDireccionClienteRegistro);
        txtInputLayotContrasenaClienteRegistro=(TextInputLayout)vista.findViewById(R.id.txtInputLayotContrasenaClienteRegistro);



        Bundle bundle=getArguments();
        evento=bundle.getString("evento");

        gruupoBotones=vista.findViewById(R.id.grupofloatRegCliente);
        btnFabaGuardar=vista.findViewById(R.id.btnFlot_guardar);
        btnFabActulizar=vista.findViewById(R.id.btnFlot_actualizarCliente);
        btnFabSalir=vista.findViewById(R.id.btnFlot_salirCliente);

        btnFabaGuardar.setVisibility(vista.VISIBLE);
        btnFabActulizar.setVisibility(vista.INVISIBLE);
        btnFabSalir.setVisibility(vista.VISIBLE);

        btnFabaGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmarImput(v);
                Log.i("salida", "onClick:"+evento);

                if(evento.equals("nuevoUser")){
                    Intent intent = new Intent(getActivity(), loginActivity.class);
                    getActivity().startActivity(intent);

                }else if(evento.equals("nuevoUserAdmin")) {

                    txtInputLayotNombreClienteRegistro.getEditText().setText("");
                    txtInputLayotCorreoRegistro.getEditText().setText("");
                    txtInputLayotContrasenaClienteRegistro.getEditText().setText("");
                    txtInputLayotDireccionClienteRegistro.getEditText().setText("");
                    txtInputLayotTelefonoClienteRegistro.getEditText().setText("");
               }

            }
        });

        btnFabSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                       remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
                //getActivity().finish();
            }
        });

        return vista;
    }

    private void cargarwebservice(String nombreCliente,String correoCliente,String contrasenaCliente,String direccionCliente, String telefonoCliente) {
        String ip=getString(R.string.ipWebService);
        String url=ip+"/insertarCliente.php?nombre="+nombreCliente+"&correo="+correoCliente+"&contrasena="+contrasenaCliente+"&direccion="+direccionCliente+"&txtoken=&telefono="+telefonoCliente;
        url=url.replace(" ","%20");
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
            System.out.println(estado);
            switch (estado){
                case "1":
                    Toast.makeText(getContext(),"Y existe el cliente con este correo",Toast.LENGTH_SHORT).show();

                    break;
                case "2":
                    Toast.makeText(getContext(),"Otro Usuario ocupa este correo",Toast.LENGTH_SHORT).show();
                    break;

                case "3":
                    Toast.makeText(getContext(),"Cliente creado exitosamente",Toast.LENGTH_SHORT).show();


                    break;
                case "4":
                    String mensaje=response.getString("dato");
                    Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                    break;
                case "5":
                    System.out.println("datos incompletos");
                    break;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    //metodos que permite validar cada campor del formuario.

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

   public void confirmarImput(View view){
        if(validarnombre()&& validarcorreo()&&validartelefono()&& valiardierccion()&& validarcontrasena()){

            String nombre =txtInputLayotNombreClienteRegistro.getEditText().getText().toString().trim();
            String correo=txtInputLayotCorreoRegistro.getEditText().getText().toString().trim();
            String contrasena=txtInputLayotContrasenaClienteRegistro.getEditText().getText().toString().trim();
            String direccion=txtInputLayotDireccionClienteRegistro.getEditText().getText().toString().trim();
            String telefono=txtInputLayotTelefonoClienteRegistro.getEditText().getText().toString().trim();

            cargarwebservice(nombre,correo,contrasena,direccion,telefono);

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





}