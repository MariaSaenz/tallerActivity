package my.jviracocha.talleractivity.Frgments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class AlertDialogoAdministradorCreateNotificacion extends DialogFragment {

    private TextInputLayout tituloNotificacion,cuerpoNotificacion;
    private TextInputEditText txttituloNotificacion, txtcuerpoNotificacion;
    private TextView enviar, cancelar;
    private FirebaseAnalytics mFirebaseAnalytics;

    private String correo,rol;
    private int idadministrador;

    onSendDataCreateNotificacion onSendDataCreateNotificacionListener;
    public interface onSendDataCreateNotificacion{
        void onsendmensaje(String mensaje);
        void updateFragmentGetionNotificaciones(String correo, String rol);
    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            onSendDataCreateNotificacionListener=(onSendDataCreateNotificacion)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.alertdialogo_administrador_nueva_notificacion, container, false);

        correo=getArguments().getString("correo");
        rol=getArguments().getString("rol");
        idadministrador=getArguments().getInt("idadministrador");
        Log.i("salida","datos que llegan al alertdialogoNotificaiconesNuevas: "+correo+" "+rol+" "+idadministrador);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        tituloNotificacion=(TextInputLayout)vista.findViewById(R.id.txtInputTituloNotificacion);
        cuerpoNotificacion=(TextInputLayout)vista.findViewById(R.id.txtInputCuerpoNotificacion);

        txttituloNotificacion=(TextInputEditText)vista.findViewById(R.id.editTxtTituloNotificacion);
        txtcuerpoNotificacion=(TextInputEditText)vista.findViewById(R.id.editTxtCuerpoNotificacion);

        txttituloNotificacion.addTextChangedListener(validTexWatcher);
        txtcuerpoNotificacion.addTextChangedListener(validTexWatcher);

        enviar=(TextView)vista.findViewById(R.id.txtEnviarNotificacion);
        cancelar=(TextView)vista.findViewById(R.id.txtCancelarNotificacion);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarInput(v);
                Log.d("salida","se creo una nueva notificacion");
                onSendDataCreateNotificacionListener.updateFragmentGetionNotificaciones(correo,rol);
                getDialog().dismiss();
                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "enviar_notificacion");
                params10.putString("id_usuario", "admin");
                mFirebaseAnalytics.logEvent("click_evento", params10);
// [END custom_event]
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("salida","se abrio el formulario  para nueva notificacion pero se lo cerro ");
                getDialog().dismiss();
            }
        });



        return vista;
    }

    private TextWatcher validTexWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String titulo=tituloNotificacion.getEditText().getText().toString().trim();
            String cuerpo=tituloNotificacion.getEditText().getText().toString().trim();

            enviar.setEnabled(!titulo.isEmpty()&&!cuerpo.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private boolean validarTituloNotificacion(){
        String titulo=tituloNotificacion.getEditText().getText().toString().trim();
        if(titulo.isEmpty()){
            tituloNotificacion.setError("Necesita titulo de la notificacion");
            return false;
        }else if(titulo.length()> 40){
            tituloNotificacion.setError("Titulo muy largo");
            return false;
        }else {
            tituloNotificacion.setError(null);
            //textInputEditTextCorreo.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validarCuerpoNotificacion(){
        String cuerpo=tituloNotificacion.getEditText().getText().toString().trim();
        if(cuerpo.isEmpty()){
            tituloNotificacion.setError("Necesita cuerpo de la notificacion");
            return false;
        }else if(cuerpo.length()> 40){
            tituloNotificacion.setError("Cuerpo muy largo");
            return false;
        }else {
            tituloNotificacion.setError(null);
            //textInputEditTextCorreo.setErrorEnabled(false);
            return true;
        }
    }
    public void confirmarInput (View view){

        if(validarTituloNotificacion()&& validarCuerpoNotificacion()){
            String tema="alerts";
            createNotificacion(txttituloNotificacion.getText().toString(),txtcuerpoNotificacion.getText().toString(),tema,idadministrador);
            onSendDataCreateNotificacionListener.updateFragmentGetionNotificaciones(correo,rol);
        } else{
            String mensaje="Datos ingresador incorrectos";
            onSendDataCreateNotificacionListener.onsendmensaje(mensaje);
        }

    }

    private void  createNotificacion(String titulo, String cuerpo, String tema, int idAdministrador){
        String ip=getString(R.string.ipWebService);
        String url=ip+"/notificardispositivos.php?titulo="+titulo+"&cuerpo="+cuerpo+"&tema="+tema+"&id_administrador="+idAdministrador;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json

                                //String idClienteStr=null;
                                try{
                                    String estado=response.getString("estado");

                                    switch (estado){

                                        case "1":
                                            String mensaje1=response.getString("dato");
                                            onSendDataCreateNotificacionListener.onsendmensaje(mensaje1);
                                            break;
                                        case "2":
                                            String mensaje=response.getString("dato");
                                            onSendDataCreateNotificacionListener.onsendmensaje(mensaje);
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
                                onSendDataCreateNotificacionListener.onsendmensaje(text.toString());

                            }
                        }
                ));



    }
}
