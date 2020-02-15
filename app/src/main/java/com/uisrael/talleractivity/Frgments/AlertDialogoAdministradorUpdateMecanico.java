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

public class AlertDialogoAdministradorUpdateMecanico extends DialogFragment {

    private EditText nombreMecanico,correoMecanico,telefonoMecanico,contrasenaMecanico;
    private TextView guardarMecanico, cancelarMecanico;

    private String nombreMec,correoMec,contrasenaMec;
    private int idMecanico, telefono, idAdministrador;


    onsendDataUpdateMecanico onsendDataUpdateMecanicoListener;
    public interface onsendDataUpdateMecanico{
        public  void onsendmensaje(String mensaje);
        public void updateFragmentGestionMecanico();

    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            onsendDataUpdateMecanicoListener=(onsendDataUpdateMecanico)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.alertdialogo_administrador_update_mecanico, container, false);

        nombreMecanico=(EditText)vista.findViewById(R.id.lblNombreMecanicoRegistroUpdate);
        correoMecanico=(EditText)vista.findViewById(R.id.lblCorreoMecanicioRegistroUpdate);
        telefonoMecanico=(EditText)vista.findViewById(R.id.lblTelefonoMecanicoRegistro);
        contrasenaMecanico=(EditText)vista.findViewById(R.id.lblContrasenaMecanicoRegistro);

        guardarMecanico=(TextView)vista.findViewById(R.id.txtGuardarUpdateMecanico);
        cancelarMecanico=(TextView)vista.findViewById(R.id.txtCancelarUpdateMecanico);

        Bundle bundle=getArguments();
        idMecanico=bundle.getInt("idMecanico");
        telefono=bundle.getInt("telefonoMec");
        idAdministrador=bundle.getInt("idAdmin");
        correoMec=bundle.getString("correoMec");
        contrasenaMec=bundle.getString("contrasenaMec");
        nombreMec=bundle.getString("nombreMec");

        Log.d("salida", ":"+idMecanico+" "+telefono+" "+idAdministrador+" "+correoMec+" "+contrasenaMec+" "+nombreMec);

        nombreMecanico.setText(nombreMec);
        correoMecanico.setText(correoMec);
        telefonoMecanico.setText(Integer.toString(telefono));
        contrasenaMecanico.setText(contrasenaMec);

        correoMecanico.setEnabled(false);

        guardarMecanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webserviceActualizarMecanico(idMecanico,correoMecanico.getText().toString(),Integer.parseInt(telefonoMecanico.getText().toString()),contrasenaMecanico.getText().toString(), nombreMecanico.getText().toString(),idAdministrador);
                onsendDataUpdateMecanicoListener.updateFragmentGestionMecanico();
                getDialog().dismiss();

            }
        });

        cancelarMecanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onsendDataUpdateMecanicoListener.updateFragmentGestionMecanico();
                getDialog().dismiss();

            }
        });
        return vista;
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
                                onsendDataUpdateMecanicoListener.onsendmensaje(text.toString());
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
                    onsendDataUpdateMecanicoListener.onsendmensaje(mensaje1);

                    break;
                case "2":
                    String mensaje=response.getString("dato");
                    onsendDataUpdateMecanicoListener.onsendmensaje(mensaje);
                    break;
                case "3":
                    String mensaje3=response.getString("dato");
                    onsendDataUpdateMecanicoListener.onsendmensaje(mensaje3);

                    break;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
