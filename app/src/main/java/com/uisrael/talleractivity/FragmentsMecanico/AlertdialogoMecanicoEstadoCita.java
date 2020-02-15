package my.jviracocha.talleractivity.FragmentsMecanico;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import my.jviracocha.talleractivity.FragmentsCliente.AlertDialogoClienteIngresoKilometraje;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class AlertdialogoMecanicoEstadoCita extends DialogFragment {

    private TextView mensajeDialogo;
    private TextView guardar, cancelar;

    private String datos;
    private int idcita, idMecanico;

    onSendMessageCitas onSendMessageCitasListener;
    public interface onSendMessageCitas{

        public  void onsendmensaje(String mensaje);
        public void  refreshListaTrabajo(int idMecanico);
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            onSendMessageCitasListener=(onSendMessageCitas)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.alertdialogo_mecanico_estadocitachange,container,false);

        mensajeDialogo=(TextView)vista.findViewById(R.id.txtcontenidocitaChange);
        guardar=(TextView)vista.findViewById(R.id.txtGuardarEstadoCita);
        cancelar=(TextView)vista.findViewById(R.id.txtCancelarEstadoCita);

        Bundle bundle=getArguments();
        datos=bundle.getString("mensaje");
        idcita=bundle.getInt("idCita");
        idMecanico=bundle.getInt("idMecanico");

        mensajeDialogo.setText(datos);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String estado="CERRADO";
                updateEstadoCita(idcita,estado);
                onSendMessageCitasListener.refreshListaTrabajo(idMecanico);
                getDialog().dismiss();
        }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        return  vista;
    }

    private void  updateEstadoCita(int idCita,String estado){
        String ip=getString(R.string.ipWebService);
        String url=ip+"/updateObserCitas.php?id_citas="+idCita+"&observaciones="+estado;
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
                                            String mensaje1=response.getString("dato");
                                            onSendMessageCitasListener.onsendmensaje(mensaje1);
                                            break;
                                        case "2":
                                            String mensaje=response.getString("dato");
                                            onSendMessageCitasListener.onsendmensaje(mensaje);
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
                                //int duracion= Toast.LENGTH_SHORT;
                                //Toast toast=Toast.makeText(context,text,duracion);
                                //toast.show();
                                onSendMessageCitasListener.onsendmensaje(text.toString());

                            }
                        }
                ));



    }

}
