package my.jviracocha.talleractivity.Frgments;

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
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import my.jviracocha.talleractivity.FragmentsCliente.AlertDialogoClienteIngresoKilometraje;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class AlertDialogoAdministradorNuevoServicio extends DialogFragment {

    private TextInputLayout nombreServicio,costoServicio,kilometrajeTope;
    private TextInputEditText txtnombreServicio,txtcostoServicio,txtkilometrajetopeServicio;
    private TextView guardar, cancelar;

    sendDataNuevoServicio sendDataNuevoServicioListener;
    public interface sendDataNuevoServicio{
        public void onsendmensaje(String mensaje);
        public void  resetListServicios();
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            sendDataNuevoServicioListener=(sendDataNuevoServicio)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.alertdialogo_administrador_nuevo_servicio, container, false);

        nombreServicio=(TextInputLayout)vista.findViewById(R.id.txtInputNombreServicio);
        costoServicio=(TextInputLayout)vista.findViewById(R.id.txtInputCostoServicio);
        kilometrajeTope=(TextInputLayout)vista.findViewById(R.id.txtInputInsertarKilometrajeServicio);

        txtnombreServicio=(TextInputEditText)vista.findViewById(R.id.editTxtNombreServicio);
        txtcostoServicio=(TextInputEditText)vista.findViewById(R.id.editTxtCostoServicio);
        txtkilometrajetopeServicio=(TextInputEditText)vista.findViewById(R.id.editTxtInsertatKilometrajeServicio);

        guardar=(TextView)vista.findViewById(R.id.txtGuardarNuevoServicio);
        cancelar=(TextView)vista.findViewById(R.id.txtCancelarNuevoServicio);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarInput(v);
                getDialog().dismiss();

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });



        return vista;
    }

    private boolean validarnombreSercicio(){
        String mail=nombreServicio.getEditText().getText().toString().trim();
        if(mail.isEmpty()){
            nombreServicio.setError("Necesita nombre del servicio");
            return false;
        }
        else  {
            nombreServicio.setError(null);
            //textInputEditTextCorreo.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validarcostoservicio(){
        String costo=costoServicio.getEditText().getText().toString().trim();
        if(costo.isEmpty()){
            costoServicio.setError("Ingrese un costo ");
            return false;
        }
        else {
            costoServicio.setError("null");
            return true;
        }
    }
    private boolean validarkilometrajetope(){

        String kilometraje=kilometrajeTope.getEditText().getText().toString().trim();
        if(kilometraje.isEmpty()){
            kilometrajeTope.setError("Debe ingresar el kilometraje");
            return false;

        }else {
            kilometrajeTope.setError("null");
            return true;
        }
    }

    public void confirmarInput (View view){
       if(validarnombreSercicio()&&validarcostoservicio()&&validarkilometrajetope()){
           webserviceNuevoServicio(txtnombreServicio.getText().toString(),Integer.parseInt(txtcostoServicio.getText().toString()),Integer.parseInt(txtkilometrajetopeServicio.getText().toString()));
           sendDataNuevoServicioListener.resetListServicios();
       } else{
           String mensaje="Datos ingresador incorrectos";
           sendDataNuevoServicioListener.onsendmensaje(mensaje);
       }

    }

    private void webserviceNuevoServicio(String nombre, int costo, int kilometraje) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/insertServicio.php?nombre="+nombre+"&costo="+costo+"&kilometrajeTope="+kilometraje;
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
                                            String mensaje1=response.getString("dato");
                                            sendDataNuevoServicioListener.onsendmensaje(mensaje1);

                                            break;
                                        case "2":
                                            String mensaje2=response.getString("dato");
                                            sendDataNuevoServicioListener.onsendmensaje(mensaje2);
                                            break;
                                        case "3":
                                            String mensaje3=response.getString("dato");
                                            sendDataNuevoServicioListener.onsendmensaje(mensaje3);
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
                                sendDataNuevoServicioListener.onsendmensaje(text.toString());
                            }
                        }
                ));
    }
}
