package my.jviracocha.talleractivity.Frgments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import my.jviracocha.talleractivity.Adaptadores.MecanicioAdapter;
import my.jviracocha.talleractivity.Adaptadores.ParteAdapter;
import my.jviracocha.talleractivity.Entidades.Mecanico;
import my.jviracocha.talleractivity.Entidades.Parte;
import my.jviracocha.talleractivity.FragmentsCliente.DatePickerFragment;
import my.jviracocha.talleractivity.MainActivity;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.TemplatePDF;
import my.jviracocha.talleractivity.VolleySingleton;

public class GestionReporteFragment extends Fragment {
    private   EditText fechaInicio, fechaFin, mecanico, estadoCita;
    private static final int STORAGE_CODE=1000;
    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaGenerarReporte,btnFabaSalir;
    private FirebaseAnalytics mFirebaseAnalytics;

    String[] listItemsDos;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    String[] listaEstadosCita;
    boolean[] chechedEstado;
    ArrayList<Integer> mEstadoCita =new ArrayList<>();

    private ArrayList<String> listaCitasReport;

    private DatePickerDialog.OnDateSetListener onDateSetListener, onDateSetListenerF;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View vista=inflater.inflate(R.layout.fragment_administrador_reportes,container,false);

        fechaInicio=(EditText)vista.findViewById(R.id.txtfechaInicioReportes);
        fechaFin=(EditText)vista.findViewById(R.id.txtfechaFinReportes);
        mecanico=(EditText)vista.findViewById(R.id.txtMecanicosReportes);
        estadoCita=(EditText)vista.findViewById(R.id.txtCitaReportes);

        gruupoBotones=vista.findViewById(R.id.grupoFabReportes);
        btnFabaSalir=vista.findViewById(R.id.idFabSalirReporte);
        btnFabaGenerarReporte=vista.findViewById(R.id.idFabGenerarReporte);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());


        cargarwebserviceMecanico();
        listaEstadosCita=getResources().getStringArray(R.array.estados_cita);
        chechedEstado=new boolean[listaEstadosCita.length];

        listaCitasReport=new ArrayList<>();


        fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int mouth=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener,year,mouth,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        fechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int mouth=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListenerF,year,mouth,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        mecanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Mecanicos");
                builder.setMultiChoiceItems(listItemsDos, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item="";
                        for (int i=0;i<mUserItems.size();i++){
                            item=item +"'"+ listItemsDos[mUserItems.get(i)]+"'";
                            if(i!=mUserItems.size()-1){
                                item=item+",";
                            }
                        }
                        mecanico.setText(item);
                    }
                });
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkedItems.length;i++){
                            checkedItems[i]=false;
                            mUserItems.clear();
                            mecanico.setText(" ");
                        }
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

        estadoCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("Estados cita");
                builder.setMultiChoiceItems(listaEstadosCita, chechedEstado, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            mEstadoCita.add(position);
                        }else{
                            mEstadoCita.remove((Integer.valueOf(position)));
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item=" ";
                        for (int i=0;i<mEstadoCita.size();i++){
                            item=item +"'"+listaEstadosCita[mEstadoCita.get(i)]+"'";
                            if(i!=mEstadoCita.size()-1){
                                item=item+",";
                            }
                        }
                        if(item==" "){
                            estadoCita.setText("TODOS");
                        }else {
                            estadoCita.setText(item);
                        }

                    }
                });
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<chechedEstado.length;i++){
                            checkedItems[i]=false;
                            mEstadoCita.clear();
                            estadoCita.setText("TODOS");
                        }
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });




        onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.d("salida","dato: "+ year+"-"+month+"-"+dayOfMonth);
                String data= year+"-"+twoDigits(month)+"-"+twoDigits(dayOfMonth);
                fechaInicio.setText(data);
            }
        };
        onDateSetListenerF=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.d("salida","dato: "+ year+"-"+month+"-"+dayOfMonth);
                String data= year+"-"+twoDigits(month)+"-"+twoDigits(dayOfMonth);
                fechaFin.setText(data);
            }
        };


        btnFabaSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
                gruupoBotones.collapse();
            }
        });
        btnFabaGenerarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaCitasReport.clear();
                String dateStringInicio = fechaInicio.getText().toString().trim();//"03/26/2012 11:49:00 AM";
                String dataStringFin=fechaFin.getText().toString().trim();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date convertedDateInicio = new Date();
                Date convertedDateFin=new Date();
                try {
                    convertedDateInicio = dateFormat.parse(dateStringInicio);
                    convertedDateFin=dateFormat.parse(dataStringFin);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(!fechaInicio.getText().toString().isEmpty()&&!fechaFin.getText().toString().isEmpty()){

                    if(convertedDateInicio.compareTo(convertedDateFin)<=0 ){

                        if(!mecanico.getText().toString().isEmpty() && !estadoCita.getText().toString().isEmpty()){
                            Log.d("salida","campo mecanico y estado no estan vacios ");
                            getlistaCitasReport(fechaInicio.getText().toString(),
                                    fechaFin.getText().toString(),
                                    mecanico.getText().toString(),
                                    estadoCita.getText().toString());
                        }else if(!mecanico.getText().toString().isEmpty() && estadoCita.getText().toString().isEmpty()){
                            Log.d("salida","solo campo mecanicio no esta vacio ");
                            getlistaCitasReport(fechaInicio.getText().toString(),
                                    fechaFin.getText().toString(),
                                    mecanico.getText().toString(),
                                    "TODOS");

                        }else if (mecanico.getText().toString().isEmpty() && !estadoCita.getText().toString().isEmpty()){
                            Log.d("salida","solo campo estado cita no esta vacio");
                            getlistaCitasReport(fechaInicio.getText().toString(),
                                    fechaFin.getText().toString(),
                                    "TODOS",
                                    estadoCita.getText().toString());
                        } else
                        {
                            Log.d("salida","campos estado y mecanico estan vacios");
                            getlistaCitasReport(fechaInicio.getText().toString(),
                                    fechaFin.getText().toString(),
                                    "TODOS","TODOS");

                        }

                    }else {
                        Context context = getContext();
                        int duracion= Toast.LENGTH_SHORT;
                        Toast toast=Toast.makeText(context,"Fecha inicio mayor a fecha fin ",duracion);
                        toast.show();

                    }

                }else {
                    Context context = getContext();
                    int duracion= Toast.LENGTH_SHORT;
                    Toast toast=Toast.makeText(context,"Ingrese las fechas por favor ",duracion);
                    toast.show();
                }

                gruupoBotones.collapse();
                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("button", "btnFabaGenerarReporte");
                params10.putString("id_usuario", "admin");
                mFirebaseAnalytics.logEvent("click_evento", params10);
// [END custom_event]
            }
        });

        return vista;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //perission was granted from popu
                    Log.d("salida","guardar pdfTRES");
                    savePDF();
                }else {
                    //permission was denied from popu, show error message
                    Toast.makeText(getContext(),"Permiso denegado.. !",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
    private void cargarwebserviceMecanico() {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/obtenerMecanicos.php";
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
                                            JSONArray json=response.optJSONArray("dato");
                                            try{
                                                listItemsDos=new String[json.length()];
                                                for (int i=0;i<json.length();i++ )
                                                {

                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);
                                                    // mecanico.setId_mecanico(jsonObject.optInt("id_mecanico"));
                                                    listItemsDos[i]=jsonObject.optString("nombre");
                                                }
                                                checkedItems=new boolean[listItemsDos.length];
                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "2":
                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
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
    private void getlistaCitasReport(String fechainicio, String fechafin,String mecanico, String estadocita){
        String ip=getString(R.string.ipWebService);
        String url=ip+"/getlistaCitasReporteAll.php?fechainicio="+fechainicio+"&fechafin="+fechafin+"&mecanico="+mecanico+"&estadocita="+estadocita;
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
                                                JSONArray json=response.optJSONArray("dato");
                                                for (int i=0;i<json.length();i++ )
                                                {

                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    listaCitasReport.add(jsonObject.optString( "fecha")+
                                                            ","+jsonObject.optString("hora")+
                                                            ","+jsonObject.optString("estado")+
                                                            ","+jsonObject.optString("nombreCliente").trim()+
                                                            ","+jsonObject.optString("telefono")+
                                                            ","+jsonObject.optString("nombre").trim()+
                                                            ","+jsonObject.optString("marca")+
                                                            ","+jsonObject.optString("placa"));
                                                }

                                                //<---permisos cuando el dispositivo con marshmallow
                                                if(Build.VERSION.SDK_INT >Build.VERSION_CODES.M){
                                                    //systema OS >= marshmallow(6.0), chequea si los permisos esta habilitado o no
                                                    if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                                            PackageManager.PERMISSION_DENIED){
                                                        //permission was not granted, require it
                                                        String[]permisson={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                                        requestPermissions(permisson,STORAGE_CODE);

                                                    }else{
                                                        //permison already granted, call save pdf method
                                                        Log.d("salida","guardar pdfUNO");

                                                        savePDF();
                                                    }
                                                }else {
                                                    //system OS< marshmallow ,call savepdf method
                                                    Log.d("salida","guardar pdfDOS");
                                                    savePDF();
                                                }
                                                //savePDF();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "2":

                                            String mensaje2=response.getString("dato");
                                            Toast.makeText(getContext(),mensaje2,Toast.LENGTH_SHORT).show();

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
    private ArrayList<String[]>getCitas(){
        ArrayList<String[]> rows=new ArrayList<>();
        String[] array = null;
        try {

            for (int i=0;i<=listaCitasReport.size();i++){
                array = listaCitasReport.get(i).split(",");
                rows.add(array);
                Log.d("salida",""+rows.get(i).toString());
            }
        }catch (Exception e){
            String mensaje2="Servicio no disponible.....!";
            Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
        }


        return  rows;
    }
    private void savePDF(){
        Date d = new Date();
        CharSequence s = DateFormat.format("MMMM d, yyyy ", d.getTime());
        String[]headers={"Fecha","Hora","Estado","Cliente","Telefono","Mecanico","Marca","Placa"};
        TemplatePDF templatePDF=new TemplatePDF(getContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("Reporte Mantenimiento", "Mantenimiento","Administardor");
        templatePDF.addTitulo("Reporte Citas","Mantenimientos",d.toString());
        templatePDF.addPagraph("Reporte a la fecha:");
        templatePDF.createTable(headers,getCitas());
        templatePDF.clseDocument();
        templatePDF.viewPDF(getContext());
    }

}
