package my.jviracocha.talleractivity.Frgments;

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
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class AlertDialogoAdministradorUpdateServicio extends DialogFragment {
    private TextInputLayout nombreServicio,costoServicio,kilometrajeTope;
    private TextInputEditText txtnombreServicio,txtcostoServicio,txtkilometrajetopeServicio;
    private TextView guardar, cancelar;

    private String nombreServicio_;
    private  int costosSericio_, kilometrajeTopeServicio_,idServicio_;



    sendDataUpdateServicio sendDataUpdateServicioListener;
    public interface sendDataUpdateServicio{
        public void onsendmensaje(String mensaje);
        public void  resetListServicios();
    }

   public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            sendDataUpdateServicioListener=(sendDataUpdateServicio)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.alertdialogo_administrador_update_servicio, container, false);

        Bundle bundle=getArguments();
        nombreServicio_=bundle.getString("nombreServicio");
        costosSericio_=bundle.getInt("costoServicio");
        kilometrajeTopeServicio_=bundle.getInt("kilometrajeServicio");
        idServicio_=bundle.getInt("idServicio");

        nombreServicio=(TextInputLayout)vista.findViewById(R.id.txtInputNombreServicioUpdate);
        costoServicio=(TextInputLayout)vista.findViewById(R.id.txtInputCostoServicioUpdate);
        kilometrajeTope=(TextInputLayout)vista.findViewById(R.id.txtInputInsertarKilometrajeServicioUpdate);

        txtnombreServicio=(TextInputEditText)vista.findViewById(R.id.editTxtNombreServicioUpdate);
        txtcostoServicio=(TextInputEditText)vista.findViewById(R.id.editTxtCostoServicioUpdate);
        txtkilometrajetopeServicio=(TextInputEditText)vista.findViewById(R.id.editTxtInsertatKilometrajeServicioUpdate);




       nombreServicio.getEditText().setText(nombreServicio_);
       costoServicio.getEditText().setText(Integer.toString(costosSericio_) );
       kilometrajeTope.getEditText().setText(Integer.toString(kilometrajeTopeServicio_) );


        guardar=(TextView)vista.findViewById(R.id.txtGuardarNuevoServicioUpdate);
        cancelar=(TextView)vista.findViewById(R.id.txtCancelarNuevoServicioUpdate);

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

            webserviceActualizarServicio(nombreServicio.getEditText().getText().toString().trim(),Integer.parseInt(costoServicio.getEditText().getText().toString()),Integer.parseInt(kilometrajeTope.getEditText().getText().toString()),idServicio_);
            sendDataUpdateServicioListener.resetListServicios();
        } else{
            String mensaje="Datos ingresador incorrectos";
            sendDataUpdateServicioListener.onsendmensaje(mensaje);
        }

    }

    private void webserviceActualizarServicio(String nombre, int costo, int kilometraje, int idServicioFull) {
        String ip=getString(R.string.ipWebService);
        String url=ip+"/updateServicio.php?nombre="+nombre+"&costo="+costo+"&kilometrajeTope="+kilometraje+"&id_servicio="+idServicioFull;
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
                                            sendDataUpdateServicioListener.onsendmensaje(mensaje1);

                                            break;
                                        case "2":
                                            String mensaje2=response.getString("dato");
                                            sendDataUpdateServicioListener.onsendmensaje(mensaje2);
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
                                sendDataUpdateServicioListener.onsendmensaje(text.toString());
                            }
                        }
                ));
    }
}
