package my.jviracocha.talleractivity.FragmentsCliente;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Adaptadores.AutoAdapter;
import my.jviracocha.talleractivity.Adaptadores.ParteAdapterAgendaMantenimiento;
import my.jviracocha.talleractivity.Entidades.Auto;
import my.jviracocha.talleractivity.Entidades.Cita;
import my.jviracocha.talleractivity.Entidades.Parte;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentAgendamientoCita extends Fragment {

    ArrayList<Auto> autoArrayList;
    AutoAdapter autoAdapter;
    StringBuilder stringBuilder;
    ArrayList<Integer> idPartes;
    int idCliente;
    //int idAuto;
    String horario;
    private Spinner spinnerHorarios,spinnerAutos;
    private EditText fechaMantenimiento;
    private Button btnAgendar;

    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaSalir,btnAgendarCita;

    private TextView idAutoAgendaMantenimiento;
    private  TextView idMecanicoAsignado;

    ArrayList<Parte> parteArrayList;
    private RecyclerView recyclerView;
    private ParteAdapterAgendaMantenimiento adapterAgendaMantenimiento;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAnalytics mFirebaseAnalytics;
    agendamientoCitaInterface agendamientoCitaListener;
    public  interface agendamientoCitaInterface{

        public void updateFragmentAgenCita(int idCliente);
    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            agendamientoCitaListener=(agendamientoCitaInterface) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_cliente_agendarcitamantenimiento,container,false);

        parteArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewPartesAutoAgendaMantenimiento);
        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        gruupoBotones=vista.findViewById(R.id.grupoFabClienteMantenimientoAgendar);
        btnFabaSalir=vista.findViewById(R.id.idFabSalirMantenimientoAgendar);
        btnAgendarCita=vista.findViewById(R.id.idFabAgendarMantenimiento);

        idAutoAgendaMantenimiento=(TextView)vista.findViewById(R.id.txtIDAutoAgendaMantenimiento);
        idMecanicoAsignado=(TextView)vista.findViewById(R.id.txtIDMecanicoAsigneado);

        idCliente=getArguments().getInt("idCliente");
        //System.out.println(idCliente);
        autoArrayList=new ArrayList<>();


        spinnerHorarios=(Spinner)vista.findViewById(R.id.spinnerHoraAutos);
        spinnerAutos=(Spinner)vista.findViewById(R.id.spinnerListaAutosAgenda);

        fechaMantenimiento=(EditText)vista.findViewById(R.id.txtfechaMantenimiento);
        //btnAgendar=(Button)vista.findViewById(R.id.btnAgendarCita);


        cargarListaAutos(idCliente);
        final ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_horarios,android.R.layout.simple_spinner_item);
        spinnerHorarios.setAdapter(adapter);
        spinnerHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //llenamos la variable de horario escogido para pasarle  a la cita
                horario=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //evento cuando no se ha selecciando nada

            }
        });


        fechaMantenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.txtfechaMantenimiento:
                        showDatePickerDialog();
                        // [START custom_event]
                        Bundle params10 = new Bundle();
                        params10.putString("button", "fechaMantenimiento");
                        params10.putInt("id_usuario", idCliente);
                        mFirebaseAnalytics.logEvent("click_evento", params10);
                        // [END custom_event]
                        break;

                }
            }
        });

       /* btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuilder=new StringBuilder();
                idPartes=new ArrayList<>();
                int i=0;
                    //parte=adapterAgendaMantenimiento.parteArrayListSelected.get(i);
                    if(adapterAgendaMantenimiento.parteArrayListSelected.size()>0){
                        Log.i("salida","numero seleccionado"+adapterAgendaMantenimiento.parteArrayListSelected.size());
                        do{

                            Parte parte=adapterAgendaMantenimiento.parteArrayListSelected.get(i);
                            stringBuilder.append(parte.getId_parte());
                            idPartes.add(parte.getId_parte());
                            Log.i("salida","partes a ejecutar"+parte.getNombre());
                            if(i !=adapterAgendaMantenimiento.parteArrayListSelected.size()-1){
                                stringBuilder.append("\n");

                            }
                            i++;

                        }while (i<adapterAgendaMantenimiento.parteArrayListSelected.size());
                        //Toast.makeText(getContext(),stringBuilder.toString(),Toast.LENGTH_LONG).show();
                        Log.i("salida","partes a ejecutar: "+fechaMantenimiento.getText().toString()+" "+horario+" "+idCliente+" "+Integer.parseInt(idAutoAgendaMantenimiento.getText().toString()));
                        //metodo que permite insertar citas
                        webInsertarCita(fechaMantenimiento.getText().toString(),horario,idCliente,Integer.parseInt(idAutoAgendaMantenimiento.getText().toString()));
                        //metodo que permite realizar una actulización del fragment
                        agendamientoCitaListener.updateFragmentAgenCita(idCliente);

                        // [START custom_event]
                        Bundle params10 = new Bundle();
                        params10.putString("button", "btnAgendar");
                        params10.putInt("id_usuario", idCliente);
                        mFirebaseAnalytics.logEvent("click_evento", params10);
                        // [END custom_event]
                    }else {
                        Toast.makeText(getContext(),"primero selccione los mantenimientos.",Toast.LENGTH_LONG).show();
                    }

                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "btnAgendar");
                params10.putInt("id_usuario", idCliente);
                mFirebaseAnalytics.logEvent("click_evento", params10);
                // [END custom_event]

            }
        });*/

        btnFabaSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
                gruupoBotones.collapse();
            }
        });
        btnAgendarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuilder=new StringBuilder();
                idPartes=new ArrayList<>();
                int i=0;
                //parte=adapterAgendaMantenimiento.parteArrayListSelected.get(i);
                if(adapterAgendaMantenimiento.parteArrayListSelected.size()>0){
                    Log.i("salida","numero seleccionado"+adapterAgendaMantenimiento.parteArrayListSelected.size());
                    do{

                        Parte parte=adapterAgendaMantenimiento.parteArrayListSelected.get(i);
                        stringBuilder.append(parte.getId_parte());
                        idPartes.add(parte.getId_parte());
                        Log.i("salida","partes a ejecutar"+parte.getNombre());
                        if(i !=adapterAgendaMantenimiento.parteArrayListSelected.size()-1){
                            stringBuilder.append("\n");

                        }
                        i++;

                    }while (i<adapterAgendaMantenimiento.parteArrayListSelected.size());
                    //Toast.makeText(getContext(),stringBuilder.toString(),Toast.LENGTH_LONG).show();
                    Log.i("salida","partes a ejecutar: "+fechaMantenimiento.getText().toString()+" "+horario+" "+idCliente+" "+Integer.parseInt(idAutoAgendaMantenimiento.getText().toString()));
                    //metodo que permite insertar citas
                    webInsertarCita(fechaMantenimiento.getText().toString(),horario,idCliente,Integer.parseInt(idAutoAgendaMantenimiento.getText().toString()));
                    //metodo que permite realizar una actulización del fragment
                    agendamientoCitaListener.updateFragmentAgenCita(idCliente);

                    // [START custom_event]
                    Bundle params10 = new Bundle();
                    params10.putString("button", "btnAgendar");
                    params10.putInt("id_usuario", idCliente);
                    mFirebaseAnalytics.logEvent("click_evento", params10);
                    // [END custom_event]
                }else {
                    Toast.makeText(getContext(),"primero selccione los mantenimientos.",Toast.LENGTH_LONG).show();
                }

                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "btnAgendar");
                params10.putInt("id_usuario", idCliente);
                mFirebaseAnalytics.logEvent("click_evento", params10);
                // [END custom_event]
            }
        });

        return vista;
    }

    private void showDatePickerDialog() {

        //DatePickerFragment newFragment = new DatePickerFragment();
        DatePickerFragment newFragment=DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                //twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                final  String selectedDate=year+"-"+twoDigits((month+1))+"-"+twoDigits(dayOfMonth);
                fechaMantenimiento.setText(selectedDate);

            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");


    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    //método que llena el spinner de autos a partir de idCliente
    public void cargarListaAutos(int idCliente){


        String ip=getString(R.string.ipWebService);
        String url=ip+"/obtenerAutos.php?id_cliente="+idCliente;
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
                                            Auto auto=null;
                                            JSONArray json=response.optJSONArray("autos");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    auto=new Auto();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    auto.setId_auto(jsonObject.optInt("id_auto"));
                                                    auto.setColor(jsonObject.optString("color"));
                                                    auto.setCilindraje(jsonObject.getInt("cilindraje"));
                                                    auto.setPlaca(jsonObject.getString("placa"));
                                                    auto.setId_cliente(jsonObject.getInt("id_cliente"));
                                                    auto.setModelo(jsonObject.getString("modelo"));
                                                    auto.setMarca(jsonObject.getString("marca"));

                                                    autoArrayList.add(auto);
                                                }
                                                autoAdapter=new AutoAdapter(getContext(),autoArrayList);

                                                spinnerAutos.setAdapter(autoAdapter);
                                                spinnerAutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        //escogemos el auto a gestionar de momento

                                                        int idAuto=autoArrayList.get(position).getId_auto();

                                                        idAutoAgendaMantenimiento.setText(Integer.toString(idAuto));

                                                        System.out.println("Id del auto a evaluar:"+idAuto);
                                                        cargarlistaPartes(idAuto);

                                                        parteArrayList.clear();


                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });


                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "2":
                                            String mensaje=response.getString("mensaje");
                                            Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
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
                                Toast toast=Toast.makeText(context,text,duracion);
                                toast.show();
                            }
                        }
                ));


    }

    //metodo para llenar el recicle view
    private void cargarlistaPartes(int idAuto) {


        String ip=getString(R.string.ipWebService);
        String url=ip+"/getallPartesbyIDAuto.php?id_auto="+idAuto;
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
                                            try {

                                                Parte parte=null;
                                                JSONArray json=response.optJSONArray("dato");
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    parte=new Parte();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    parte.setId_auto(jsonObject.optInt("id_auto"));
                                                    parte.setId_parte(jsonObject.optInt("id_parte"));
                                                    parte.setNombre(jsonObject.optString("nombre"));
                                                    parte.setCosto(jsonObject.getInt("consto"));
                                                    parte.setVencimientoKM(jsonObject.getInt("vencimientoKM"));



                                                    parteArrayList.add(parte);
                                                }
                                                adapterAgendaMantenimiento=new ParteAdapterAgendaMantenimiento(parteArrayList);
                                                recyclerView.setAdapter(adapterAgendaMantenimiento);



                                                /*adapterAgendaMantenimiento.setOnItemClickListener(new ParteAdapterAgendaMantenimiento.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(int position) {
                                                        System.out.println(parteArrayList.get(position).getId_parte());
                                                    }
                                                });*/


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "2":

                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getContext(),mensaje2,Toast.LENGTH_SHORT).show();

                                            break;
                                        case "3":
                                            String mensaje3=response.getString("dato");
                                            Toast.makeText(getContext(),mensaje3,Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                                catch(JSONException e){
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
                                Toast toast=Toast.makeText(context,text,duracion);
                                toast.show();
                            }
                        }
                ));


    }

    //metodo para insertar citas
    private void webInsertarCita(final String fecha,final String hora,final int idCliente, final int idauto) {
        String ip=getString(R.string.ipWebService);
        String url=ip+"/insertCita.php?fecha="+fecha+"&hora="+hora+"&observaciones=PENDING"+"&id_cliente="+idCliente+"&id_auto="+idauto;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json

                                Log.i("salida","Esto se obtiene del insertCita:"+response);
                                try{
                                    String estado=response.getString("estado");

                                    switch (estado){

                                        case "1":

                                            String mensaje=response.getString("dato");
                                            System.out.println(mensaje);
                                            Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                                            break;
                                        case "2":


                                            int IDmecanicoSelecionado=response.getInt("idMecanico");
                                            //System.out.println(IDmecanicoSelecionado);
                                            idMecanicoAsignado.setText(Integer.toString(IDmecanicoSelecionado) );

                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
                                            //obtene el id del mecanico
                                            webGetIDCita(fecha,hora,idCliente,idauto,IDmecanicoSelecionado );



                                            break;
                                        case "3":

                                            String mensaje3=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje3,Toast.LENGTH_SHORT).show();
                                            break;
                                        case "4":

                                            String mensaje4=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje4,Toast.LENGTH_SHORT).show();
                                            break;
                                        case "5":

                                            String mensaje5=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje5,Toast.LENGTH_SHORT).show();
                                            break;
                                        case "6":

                                            String mensaje6=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje6,Toast.LENGTH_SHORT).show();
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

    //metodo para obtener el id de la cita
    private void  webGetIDCita(String fecha, String hora, final int idCliente, final int idauto,final int idMecanico){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getIDCita.php?fecha="+fecha+"&hora="+hora+"&id_cliente="+idCliente+"&id_auto="+idauto+"&id_mecanico="+idMecanico;
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

                                            Cita cita=null;
                                            JSONArray jsonArray=response.optJSONArray("dato");
                                            for(int i=0;i<jsonArray.length();i++){
                                                cita=new Cita();
                                                JSONObject jsonObject=null;
                                                jsonObject=jsonArray.getJSONObject(i);

                                                cita.setIdCita(jsonObject.optInt("id_citas"));
                                            }



                                            for (int i=0;i<idPartes.size();i++){

                                                int idParteMantenimiento =idPartes.get(i);
                                                webInsertMantenimiento(idParteMantenimiento, idMecanico, cita.getIdCita());


                                            }


                                            break;
                                        case "2":


                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
                                            //obtene el id del mecanico

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

    //metodo qeu permite ingresar el mantenimiento.
    private  void  webInsertMantenimiento(int idParte, int idMecanico, int idCita){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/insertMantenimiento.php?id_parte="+idParte+"&id_mecanico="+idMecanico+"&id_citas="+idCita;
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
                                            Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
                                            break;
                                        case "2":
                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();

                                            break;
                                        case "3":

                                            String mensaje3=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje3,Toast.LENGTH_SHORT).show();
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







}
