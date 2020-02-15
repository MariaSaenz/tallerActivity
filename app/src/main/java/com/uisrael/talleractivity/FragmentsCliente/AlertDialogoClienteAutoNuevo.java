package my.jviracocha.talleractivity.FragmentsCliente;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
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


public class AlertDialogoClienteAutoNuevo extends DialogFragment{

   private  static  final  String TAG="miFragment";
    private TextInputLayout txtInserMarca;
    private TextInputLayout txtInserPlaca;
    private TextInputLayout txtInserModelo;
    private TextInputLayout txtCilindraje;

    private TextInputEditText editTextMarca;
    private TextInputEditText editTextPlaca;
    private TextInputEditText editTextModelo;
    private TextInputEditText editTextCilindraje;

    private TextView guardarAuto, cancelarAuto;

    private Spinner spinnerColor;
    private String color;

    private  int idCliente;

    onUpdateGestAutoListener updateGestAuto;
    public  interface onUpdateGestAutoListener{

        public  void onUpdateGesAuto();
        void onsendmensaje(String mensaje);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            updateGestAuto=(onUpdateGestAutoListener)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.alertdialogo_cliente_auto_nuevo, container, false);
        txtInserMarca = (TextInputLayout)vista.findViewById(R.id.txtInsertMarca);
        txtInserPlaca = (TextInputLayout)vista.findViewById(R.id.txtInsertPlaca);
        txtInserModelo = (TextInputLayout)vista.findViewById(R.id.txtInsertModelo);
        txtCilindraje = (TextInputLayout)vista.findViewById(R.id.txtInsertCilindarje);
        spinnerColor = (Spinner)vista.findViewById(R.id.spinnerColorAutoNuevo);

        guardarAuto=(TextView)vista.findViewById(R.id.txtGuardarNuevoAuto);
        cancelarAuto=(TextView)vista.findViewById(R.id.txtCancelarNuevoAuto);

        editTextMarca = (TextInputEditText)vista.findViewById(R.id.editTxtMarca);
        editTextModelo = (TextInputEditText)vista.findViewById(R.id.editTxtModelo);
        editTextPlaca = (TextInputEditText)vista.findViewById(R.id.editTxtPlaca);
        editTextCilindraje = (TextInputEditText)vista.findViewById(R.id.editTxtCilindraje);

        editTextMarca.addTextChangedListener(validTexWatcher);
        editTextModelo.addTextChangedListener(validTexWatcher);
        editTextPlaca.addTextChangedListener(validTexWatcher);
        editTextCilindraje.addTextChangedListener(validTexWatcher);

        Bundle bundle=getArguments();
        idCliente=bundle.getInt("idCliente");

        editTextPlaca.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        //generamos un adaptador para poder agregar la lista de colores
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_color,android.R.layout.simple_spinner_item);
        spinnerColor.setAdapter(adapter);
        //creamos el metodo listener al seleccionar un elemento del combo
        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //llenamos la variable de color para poder crear el nuevo auto o pasarle al fragment
                color=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        guardarAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarImput(v);
                getDialog().dismiss();
            }
        });
        cancelarAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return  vista;
    }

        private void webInsertarAuto(String marca,String color, String placa, int idCliente,float cilindraje, int modelo) {
        String ip=getString(R.string.ipWebService);
        String url=ip+"/insertarAuto.php?color="+color+"&modelo="+modelo+"&marca="+marca+"&cilindraje="+cilindraje+"&placa="+placa+"&id_cliente="+idCliente;
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
                                            updateGestAuto.onsendmensaje(mensaje);
                                           // Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                                            break;
                                        case "2":

                                            String mensaje2=response.getString("dato");
                                            updateGestAuto.onsendmensaje(mensaje2);
                                           // Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
                                            break;
                                        case "3":

                                            String mensaje3=response.getString("dato");
                                            updateGestAuto.onsendmensaje(mensaje3);
                                            // Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
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
                                updateGestAuto.onsendmensaje(text.toString());
                            }
                        }
                ));
    }
        private boolean validarMarca () {
        String marca = txtInserMarca.getEditText().getText().toString().trim();
        if (marca.isEmpty()) {
            txtInserMarca.setError("No se ha ingresado campo");
            return false;
        } else {
            txtInserMarca.setError(null);
            return true;
        }
    }
        private boolean validarPlaca () {
        String placa = txtInserPlaca.getEditText().getText().toString().trim();
        if (placa.isEmpty()) {
            txtInserPlaca.setError("No se ha ingresado campo");
            return false;
        } else if (placa.length() > 8) {
            txtInserPlaca.setError("Placa muy larga");
            return false;
        } else {
            txtInserPlaca.setError(null);
            return true;
        }
    }
        private boolean validarModelo () {
        String modelo = txtInserModelo.getEditText().getText().toString().trim();
        if (modelo.isEmpty()) {
            txtInserModelo.setError("No se ha ingresado campo");
            return false;
        } else if (modelo.length() > 4) {
            txtInserModelo.setError("Placa muy larga");
            return false;
        } else {
            txtInserModelo.setError(null);
            return true;
        }
    }
        private boolean validarCilindraje () {
        String modelo = txtCilindraje.getEditText().getText().toString().trim();
        if (modelo.isEmpty()) {
            txtCilindraje.setError("No se ha ingresado campo");
            return false;
        } else if (modelo.length() > 4) {
            txtCilindraje.setError("Placa muy larga");
            return false;
        } else {
            txtCilindraje.setError(null);
            return true;
        }
    }
        public void validarImput(View view){
            if(validarMarca()&&validarPlaca()&&validarModelo()&&validarCilindraje()){
                String marca = txtInserMarca.getEditText().getText().toString().trim();
                String placa = txtInserPlaca.getEditText().getText().toString().trim();
                int modelo = Integer.parseInt(txtInserModelo.getEditText().getText().toString().trim());
                float cilindraje = Float.parseFloat(txtCilindraje.getEditText().getText().toString().trim()) ;
                //System.out.println("datos a ingresar:"+marca+""+placa+""+modelo+""+cilindraje+""+color+""+idCliente);
                Log.i("salida","\"datos a ingresar:\"+marca+\"\"+placa+\"\"+modelo+\"\"+cilindraje+\"\"+color+\"\"+idCliente");
                //<Llamado al metodo que permite guardar un nuevo auto >String marca,String color, String placa, int idCliente,float cilindraje, int modelo

                webInsertarAuto(marca,color,placa,idCliente,cilindraje,modelo);
                updateGestAuto.onUpdateGesAuto();
            }else {
                updateGestAuto.onsendmensaje("falta el ingreso de datos");
            }
        }
        private TextWatcher validTexWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


                String marca= editTextMarca.getText().toString().trim();
                String modelo=editTextModelo.getText().toString().trim();
                String placa=editTextPlaca.getText().toString().trim();
                String cilindraje=editTextCilindraje.getText().toString().trim();
            guardarAuto.setEnabled(!marca.isEmpty()&&!modelo.isEmpty()&&!placa.isEmpty()&&!cilindraje.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };





}
