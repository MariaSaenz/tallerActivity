package my.jviracocha.talleractivity.FragmentsCliente;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AlertDialogoClienteModificarFecha extends DialogFragment {
    private EditText fechaModificar;
    private TextView guradar, cancelar;
    private Spinner spinerhoraModificar;
    String horario;
    String fecha;
    String horaSet;
    int idCita;
    int idCliente;
    int idAuto;


    upsateListCitas upsateListCitasListener;
    public  interface  upsateListCitas{
        public  void sendDtaListaCitas(int idCliente);
        public  void  sendMenajeUpdateCita(String mensaje);

    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;
        try {
            upsateListCitasListener=(upsateListCitas)activity;

        }catch (ClassCastException e){

            //revisar si no provoca error de compilacion
            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.alertdialogo_cliente_modificarfecha_cita,container,false);


        Bundle bundle=getArguments();
        horaSet=bundle.getString("hora");
        fecha=bundle.getString("fecha");
        idCita=bundle.getInt("idCitas");
        idCliente=bundle.getInt("idCliente");
        idAuto=bundle.getInt("idAuto");


        fechaModificar=(EditText)vista.findViewById(R.id.txtfechaMantenimientoCorrecion);
        spinerhoraModificar=(Spinner)vista.findViewById(R.id.spinnerHorarios);
        guradar=(TextView)vista.findViewById(R.id.txtGuardarCitaModificada);
        cancelar=(TextView)vista.findViewById(R.id.txtCancelarCitaModificada);

        fechaModificar.setText(fecha);


        //carga del spiner con los horasio a dispocicion.
        final ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_horarios,android.R.layout.simple_spinner_item);
        spinerhoraModificar.setAdapter(adapter);
        //obtener el valor que nos llega desde la clase fragmentclientelistamantenimiento
        spinerhoraModificar.setSelection(obterPosicionItem(spinerhoraModificar,horaSet));

        spinerhoraModificar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //llenamos la variable de horario escogido para pasarle  a la cita
                horario=parent.getItemAtPosition(position).toString();
                System.out.println("seleccionado:"+horario);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //evento cuando no se ha selecciando nada
                horario=horaSet;
             }
        });


        fechaModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.txtfechaMantenimientoCorrecion:
                        showDatePickerDialog();
                        break;

                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        guradar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!fechaModificar.getText().toString().equals(fecha) && horario!=horaSet){

                    System.out.println(fechaModificar.getText().toString()+"/"+horario+"/"+idCita+"/"+idCliente);
                    updateCitaMantenimientoFechaHora(fechaModificar.getText().toString(),horario,idCita,idCliente,idAuto);



                }else if(fechaModificar.getText().toString().equals(fecha)){
                    updateCitaMantenimiento(fecha,horario,idCita,idCliente);
                    //llamada a un metodo que permita actualizar la lista.
                    System.out.println(fecha+"/"+horario+"/"+idCita+"/"+idCliente);

                    upsateListCitasListener.sendDtaListaCitas(idCliente);
                }
                getDialog().dismiss();

            }
        });



        return  vista;
    }

    private static int obterPosicionItem(Spinner spinerhoraModificar, String horaSet) {

        int position=0;
        for(int i=0;i<spinerhoraModificar.getCount();i++){

            if(spinerhoraModificar.getItemAtPosition(i).toString().equalsIgnoreCase(horaSet)){
                position=i;
            }
        }

        return  position;
    }

    private void showDatePickerDialog() {

        //DatePickerFragment newFragment = new DatePickerFragment();
        DatePickerFragment newFragment=DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                //twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                final  String selectedDate=year+"-"+twoDigits((month+1))+"-"+twoDigits(dayOfMonth);
                fechaModificar.setText(selectedDate);

            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");


    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    private  void  updateCitaMantenimiento(String fecha, String hora, int id_citas, final int idCliente){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/updateHorarioCitas.php?hora="+hora+"&id_citas="+id_citas+"&fecha="+fecha;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json

                                System.out.println(response);
                                try{
                                    String estado=response.getString("estado");

                                    switch (estado){

                                        case "1":

                                            String mensaje=response.getString("dato");
                                            System.out.println(mensaje);
                                            //Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                                            upsateListCitasListener.sendMenajeUpdateCita(mensaje);
                                            break;
                                        case "2":
                                            String mensaje2=response.getString("dato");
                                            //Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
                                            //llamara a un metodo para actualizar la lista
                                            upsateListCitasListener.sendMenajeUpdateCita(mensaje2);
                                            upsateListCitasListener.sendDtaListaCitas(idCliente);


                                            break;
                                        case "3":

                                            String mensaje3=response.getString("dato");
                                            //Toast.makeText(getActivity(),mensaje3,Toast.LENGTH_SHORT).show();
                                            upsateListCitasListener.sendMenajeUpdateCita(mensaje3);
                                            break;
                                        case "4":

                                            String mensaje4=response.getString("dato");
                                            //Toast.makeText(getActivity(),mensaje4,Toast.LENGTH_SHORT).show();
                                            upsateListCitasListener.sendMenajeUpdateCita(mensaje4);
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
                            }
                        }
                ));

    }

    private  void  updateCitaMantenimientoFechaHora(String fecha, String hora, int id_citas, final int idCliente,int idAuto){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/updateCitaHoraFecha.php?fecha="+fecha+"&hora="+hora+"+&id_cliente="+idCliente+"&id_auto="+idAuto+"&id_citas="+id_citas;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json

                                System.out.println(response);
                                try{
                                    String estado=response.getString("estado");

                                    switch (estado){

                                        case "1":

                                            String mensaje=response.getString("dato");
                                            System.out.println(mensaje);
                                            //Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                                            upsateListCitasListener.sendMenajeUpdateCita(mensaje);
                                            break;
                                        case "2":
                                            String mensaje2=response.getString("dato");
                                            //Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
                                            //llamara a un metodo para actualizar la lista
                                            upsateListCitasListener.sendMenajeUpdateCita(mensaje2);
                                            upsateListCitasListener.sendDtaListaCitas(idCliente);


                                            break;
                                        case "3":

                                            String mensaje3=response.getString("dato");
                                            //Toast.makeText(getActivity(),mensaje3,Toast.LENGTH_SHORT).show();
                                            upsateListCitasListener.sendMenajeUpdateCita(mensaje3);
                                            break;
                                        case "4":

                                            String mensaje4=response.getString("dato");
                                            //Toast.makeText(getActivity(),mensaje4,Toast.LENGTH_SHORT).show();
                                            upsateListCitasListener.sendMenajeUpdateCita(mensaje4);
                                            break;
                                        case "5":

                                            String mensaje5=response.getString("dato");
                                            //Toast.makeText(getActivity(),mensaje4,Toast.LENGTH_SHORT).show();
                                            upsateListCitasListener.sendMenajeUpdateCita(mensaje5);
                                            break;
                                        case "6":

                                            String mensaje6=response.getString("dato");
                                            //Toast.makeText(getActivity(),mensaje4,Toast.LENGTH_SHORT).show();
                                            upsateListCitasListener.sendMenajeUpdateCita(mensaje6);
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
                                upsateListCitasListener.sendMenajeUpdateCita(text.toString());
                            }
                        }
                ));

    }
}
