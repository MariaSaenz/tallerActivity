package my.jviracocha.talleractivity.FragmentsCliente;

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

public class AlertDialogoClienteIngresoKilometraje extends DialogFragment {

    private TextInputLayout InputIngresoKilometraje;
    private TextInputEditText editTextIngresoKilometraje;

    private TextView txtGuardar, txtCancelar;


    private int idCliente,idAuto;


    onSendMessageKM onSendMessageKMListener;
    public  interface onSendMessageKM{

        public  void onsendmensaje(String mensaje);

        public  void  updateListKilometrajeIngresado(int idClient, int idAuto);
    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            onSendMessageKMListener=(onSendMessageKM)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.alertdialogo_cliente_ingresokilometraje,container,false);

        InputIngresoKilometraje=(TextInputLayout)vista.findViewById(R.id.txtInputIngresoKilometraje);
        editTextIngresoKilometraje=(TextInputEditText)vista.findViewById(R.id.editTxtIngresoKilometreje);

        txtGuardar=(TextView)vista.findViewById(R.id.txtGuardar);
        txtCancelar=(TextView)vista.findViewById(R.id.txtCancelar);

        //datos que lleagn al alert dialogo

        Bundle bundle=getArguments();
        idCliente=bundle.getInt("idCliente");
        idAuto=bundle.getInt("idAuto");



        //validamos campos
        editTextIngresoKilometraje.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    validarKilometraje();
                }
            }
        });


        //bonton cancelar
        txtCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        //boton guardar
        txtGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kilometraje = InputIngresoKilometraje.getEditText().getText().toString().trim();


                if(!kilometraje.isEmpty()){
                    //ingresamos los datos  a la BDD
                    ingresarKilometraje(Integer.parseInt(kilometraje),idCliente,idAuto);

                    onSendMessageKMListener.updateListKilometrajeIngresado(idCliente,idAuto);
                }else{

                    Toast.makeText(getContext(),"IngreseKilometraje",Toast.LENGTH_LONG).show();
                }

                getDialog().dismiss();


            }

        });

        return  vista;


    }
    private boolean validarKilometraje () {
        String modelo = InputIngresoKilometraje.getEditText().getText().toString().trim();
        if (modelo.isEmpty()) {
            InputIngresoKilometraje.setError("No se ha ingresado campo");
            return false;
        } else if (modelo.length() > 4) {
            InputIngresoKilometraje.setError("Kilometraje extenso");
            return false;
        } else {
            InputIngresoKilometraje.setError(null);
            return true;
        }
    }

    public void ingresarKilometraje(int kilometraje, int idCliente, int idAuto){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/insertKilometraje.php?kilometraje="+kilometraje+"&id_cliente="+idCliente+"&id_auto="+idAuto;
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

                                            String mensaje=response.getString("dato");

                                            //Toast.makeText(getContext(),mensaje,Toast.LENGTH_LONG).show();

                                            onSendMessageKMListener.onsendmensaje(mensaje);


                                            break;
                                        case "2":

                                            String mensaje2=response.getString("dato");
                                            // Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
                                            onSendMessageKMListener.onsendmensaje(mensaje2);
                                            break;
                                        case "3":

                                            String mensaje3=response.getString("dato");
                                            // Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
                                            onSendMessageKMListener.onsendmensaje(mensaje3);
                                            break;
                                        case "4":

                                            String mensaje4=response.getString("dato");
                                            // Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
                                            onSendMessageKMListener.onsendmensaje(mensaje4);
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
                                int duracion= Toast.LENGTH_SHORT;
                                // Toast toast=Toast.makeText(context,text,duracion);
                                //toast.show();
                                onSendMessageKMListener.onsendmensaje(text.toString());
                            }
                        }
                ));


    }

}
