package my.jviracocha.talleractivity.Frgments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class AlertDialogoAdministradorCreateMecanico extends DialogFragment {

    private EditText nombreMecanico,correoMecanico,telefonoMecanico,contrasenaMecanico;
    private TextView guardarMecanico, cancelarMecanico;
    private int  idAdministrador;

    onsendDataCreateMecanico onsendDataCreateMecanicoListener;
    public interface onsendDataCreateMecanico{
        public  void onsendmensaje(String mensaje);
        public void updateFragmentGestionMecanico();

    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            onsendDataCreateMecanicoListener=(onsendDataCreateMecanico)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.alertdialogo_administrador_update_mecanico, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Bundle bundle=getArguments();
        idAdministrador=bundle.getInt("idAdministrador");

        nombreMecanico=(EditText)vista.findViewById(R.id.lblNombreMecanicoRegistroUpdate);
        correoMecanico=(EditText)vista.findViewById(R.id.lblCorreoMecanicioRegistroUpdate);
        telefonoMecanico=(EditText)vista.findViewById(R.id.lblTelefonoMecanicoRegistro);
        contrasenaMecanico=(EditText)vista.findViewById(R.id.lblContrasenaMecanicoRegistro);

        guardarMecanico=(TextView)vista.findViewById(R.id.txtGuardarUpdateMecanico);
        cancelarMecanico=(TextView)vista.findViewById(R.id.txtCancelarUpdateMecanico);

        guardarMecanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nombreMecanico.getText().toString().isEmpty()) {
                    String mensaje1="Campo NOMBRE esta vacio";
                    onsendDataCreateMecanicoListener.onsendmensaje(mensaje1);
                } else {
                    if (correoMecanico.getText().toString().isEmpty()) {
                        String mensaje2="Campo CORREO esta vacio";
                        onsendDataCreateMecanicoListener.onsendmensaje(mensaje2);
                    } else {
                        if ( telefonoMecanico.getText().toString().isEmpty()) {
                            String mensaje3="Campo TELEFONO esta vacio";
                            onsendDataCreateMecanicoListener.onsendmensaje(mensaje3);
                        } else {
                            if (contrasenaMecanico.getText().toString().isEmpty()) {
                                String mensaje4="Campo CONTRASENA esta vacio";
                                onsendDataCreateMecanicoListener.onsendmensaje(mensaje4);
                            } else {

                                webserviceNuevoMecanico(nombreMecanico.getText().toString(), correoMecanico.getText().toString(), contrasenaMecanico.getText().toString(),Integer.parseInt( telefonoMecanico.getText().toString()), idAdministrador);
                                onsendDataCreateMecanicoListener.updateFragmentGestionMecanico();
                            }
                        }

                    }
                }
                getDialog().dismiss();


            }
        });

        cancelarMecanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                onsendDataCreateMecanicoListener.updateFragmentGestionMecanico();

            }
        });

        return vista;

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
                                onsendDataCreateMecanicoListener.onsendmensaje(text.toString());
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
                    onsendDataCreateMecanicoListener.onsendmensaje(mensaje1);
                    break;
                case "2":
                    String mensaje=response.getString("dato");
                    onsendDataCreateMecanicoListener.onsendmensaje(mensaje);
                    break;
                case "3":
                    String mensaje3=response.getString("dato");
                    onsendDataCreateMecanicoListener.onsendmensaje(mensaje3);
                    break;
                case "4":
                    String mensaje4=response.getString("dato");
                    onsendDataCreateMecanicoListener.onsendmensaje(mensaje4);
                    break;
                case "5":
                    String mensaje5=response.getString("dato");
                    onsendDataCreateMecanicoListener.onsendmensaje(mensaje5);
                    break;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
