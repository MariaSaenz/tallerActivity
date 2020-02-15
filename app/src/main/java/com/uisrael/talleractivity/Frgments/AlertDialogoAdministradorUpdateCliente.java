package my.jviracocha.talleractivity.Frgments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class AlertDialogoAdministradorUpdateCliente extends DialogFragment {

    private  int idcliente,telefono;
    private String nombre,contrasena,direccion,correo;

    private EditText editNombre,editTelefono,editDireccion,editContrasena;
    private TextView textViewcorreo, guardar, cancelar;

   onSendMenssageUpdateCliente onSendMenssageUpdateClienteListener;
    public interface onSendMenssageUpdateCliente{

        public  void onsendmensaje(String mensaje);
        public  void  updateFragmentGestionCliente();

    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            onSendMenssageUpdateClienteListener=(onSendMenssageUpdateCliente)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.alertdialogo_administrador_update_cliente,container,false);

        Bundle bundle=getArguments();
        idcliente=bundle.getInt("idCliente");
        telefono=bundle.getInt("telefono");
        nombre=bundle.getString("nombre");
        contrasena=bundle.getString("contrasena");
        direccion=bundle.getString("direccion");
        correo=bundle.getString("correo");

        editNombre=(EditText)vista.findViewById(R.id.lblNombreClienteGestionUpdate);
        editTelefono=(EditText)vista.findViewById(R.id.lblTelefonoClienteGestionUpdate);
        editDireccion=(EditText)vista.findViewById(R.id.lblDireccionClienteGestionUpdate);
        editContrasena=(EditText)vista.findViewById(R.id.lblContrasenaClienteGestionUpdate);
        textViewcorreo=(TextView)vista.findViewById(R.id.lblCorreoClienteGestionUpdate);
        guardar=(TextView)vista.findViewById(R.id.txtGuardarUpdateCliente);
        cancelar=(TextView)vista.findViewById(R.id.txtCancelarUpdateCliente);

        Log.d("salida", ":"+idcliente+telefono+nombre+contrasena+direccion+correo);


        editNombre.setText(nombre);
        editTelefono.setText(Integer.toString(telefono));
        editDireccion.setText(direccion);
        editContrasena.setText(contrasena);
        textViewcorreo.setText(correo);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarwebserviceActualizar(idcliente,editNombre.getText().toString(),editContrasena.getText().toString(),editDireccion.getText().toString(),
                        Integer.parseInt(editTelefono.getText().toString()),textViewcorreo.getText().toString());
                onSendMenssageUpdateClienteListener.updateFragmentGestionCliente();
                getDialog().dismiss();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendMenssageUpdateClienteListener.updateFragmentGestionCliente();
                getDialog().dismiss();
            }
        });


    return vista;
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
                                onSendMenssageUpdateClienteListener.onsendmensaje(text.toString());
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
                    onSendMenssageUpdateClienteListener.onsendmensaje(mensaje4);

                    break;
                case "2":
                    String mensaje=response.getString("dato");
                    onSendMenssageUpdateClienteListener.onsendmensaje(mensaje);
                    break;
                case "3":
                    String mensaje3=response.getString("dato");
                    onSendMenssageUpdateClienteListener.onsendmensaje(mensaje3);
                    break;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
