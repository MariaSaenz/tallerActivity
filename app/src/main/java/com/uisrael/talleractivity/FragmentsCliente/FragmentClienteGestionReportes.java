package my.jviracocha.talleractivity.FragmentsCliente;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import my.jviracocha.talleractivity.Adaptadores.AdapterClienteNotificacion;
import my.jviracocha.talleractivity.Adaptadores.AdapterClienteReporte;
import my.jviracocha.talleractivity.Entidades.Cita;
import my.jviracocha.talleractivity.Entidades.Kilometraje;
import my.jviracocha.talleractivity.Entidades.Mantenimiento;
import my.jviracocha.talleractivity.Entidades.Notificaciones;
import my.jviracocha.talleractivity.Preferences;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.TemplatePDF;
import my.jviracocha.talleractivity.VolleySingleton;

import static android.content.Context.MODE_PRIVATE;

public class FragmentClienteGestionReportes extends Fragment {

    private static final int STORAGE_CODE=1000;
    private ArrayList<Cita> citaArrayList;
    private ArrayList<Kilometraje> kilometrajeArrayList;
    private FirebaseAnalytics mFirebaseAnalytics;

    private ArrayList<String> arrPackage;
    private ArrayList<String>notificacionesArrayList;
    private ArrayList<String>mantenimientoArrayList;


    private RecyclerView recyclerView;
    private AdapterClienteReporte adapterClienteReporte;
    private RecyclerView.LayoutManager layoutManager;

    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaSalir;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    private String idCliente,correoUsuarioLogin;


    interfaceReporteCliente interfaceReporteClienteListener;
    public interface interfaceReporteCliente{

        void openreportPDFCliente();
    }
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            interfaceReporteClienteListener=(interfaceReporteCliente) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_cliente_gestion_reportes,container,false);

        idCliente= Preferences.obtenerPreferenceString(getActivity(), Preferences.USUARIO_ID);
        correoUsuarioLogin=Preferences.obtenerPreferenceString(getActivity(),Preferences.USUARIO_CORREO);

        pref = getContext().getSharedPreferences("BuyyaPref", MODE_PRIVATE);
        editor = pref.edit();

        arrPackage = new ArrayList<>();

        notificacionesArrayList=new ArrayList<>();

        citaArrayList=new ArrayList<>();
        kilometrajeArrayList=new ArrayList<>();
        mantenimientoArrayList=new ArrayList<>();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewReportesClientePDF);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        gruupoBotones=vista.findViewById(R.id.grupoFabClienteReportes);
        btnFabaSalir=vista.findViewById(R.id.idFabClienteReportesSalir);

        btnFabaSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
                gruupoBotones.collapse();
            }
        });

        getListaCitasCerradas(Integer.parseInt(idCliente));
        return vista;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:
                if(grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
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
    private void getListaCitasCerradas(int idCliente){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getlistCitasinClose.php?id_cliente="+idCliente;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json

                                try{
                                    final String estado=response.getString("estado");

                                    switch (estado){

                                        case "1":
                                            //cargamos el recicle view
                                            Cita cita=null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    cita=new Cita();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    cita.setIdCita(jsonObject.optInt("id_citas"));
                                                    cita.setFecha(jsonObject.optString("fecha"));
                                                    cita.setHora(jsonObject.optString("hora"));
                                                    cita.setPlacaAuto(jsonObject.optString("placa"));
                                                    cita.setIdMecanico(jsonObject.optInt("id_mecanico"));
                                                    cita.setIdCliente(jsonObject.optInt("id_cliente"));
                                                    cita.setIdAuto(jsonObject.getInt("id_auto"));
                                                    cita.setMarcaAuto(jsonObject.getString("marca"));


                                                    citaArrayList.add(cita);
                                                }
                                                adapterClienteReporte=new AdapterClienteReporte(citaArrayList);
                                                adapterClienteReporte.setOnItemClickListener(new AdapterClienteReporte.OnItemClickListenerReporte() {
                                                    @Override
                                                    public void onShowReportPDF(int idCita) {


                                                       Log.d("salida","idCita al que se le saca el jugo: "+citaArrayList.get(idCita).getPlacaAuto());

                                                        editor.putInt("idmecanico",citaArrayList.get(idCita).getIdMecanico());
                                                        editor.putString("fecha", citaArrayList.get(idCita).getFecha()+"-"+citaArrayList.get(idCita).getHora());  // Saving string
                                                        editor.putString("marca",citaArrayList.get(idCita).getMarcaAuto());
                                                        editor.putString("placa",citaArrayList.get(idCita).getPlacaAuto());
                                                        // Save the changes in SharedPreferences
                                                        editor.commit(); // commit changes

                                                        int idCitaC=citaArrayList.get(idCita).getIdCita();
                                                        int idAutoC=citaArrayList.get(idCita).getIdAuto();
                                                        int idClienteC=citaArrayList.get(idCita).getIdCliente();

                                                        cargarlistaKilometraje(idClienteC,idCitaC,idAutoC);


                                                    }
                                                });
                                                recyclerView.setAdapter(adapterClienteReporte);
                                                adapterClienteReporte.notifyDataSetChanged();

                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                            ////
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
    public void cargarlistaKilometraje(final int idClienteC, final int idCita, int idAuto){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getkilometrajeReport.php?id_cita="+idCita+"&id_auto="+idAuto;
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
                                            //cargamos el recicle view
                                            Kilometraje kilometraje=null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    kilometraje=new Kilometraje();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);
                                                    kilometraje.setFecha(jsonObject.optString("fecha"));
                                                    kilometraje.setKilometraje(jsonObject.optInt("kilometraje"));

                                                    arrPackage.add( jsonObject.optString("fecha")+","+jsonObject.optInt("kilometraje"));

                                                }
                                                getlistaNotificacionesServicio(idClienteC,idCita);
                                               // saveArrayList(arrPackage, "lista_kilometraje");

                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }

                                            ////
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
                                Toast toast=Toast.makeText(context,text,duracion);
                                toast.show();

                            }
                        }
                ));


    }
    private void getlistaNotificacionesServicio(int idCliente,final int idCitaC){

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getallNotificacionesListServicio.php?id_cliente="+idCliente;
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
                                            //cargamos el recicle view
                                            Notificaciones notificaciones=null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    notificaciones=new Notificaciones();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);

                                                    notificaciones.setCuerpo(jsonObject.optString("cuerpo"));
                                                    notificaciones.setFecha(jsonObject.optString("fecha"));
                                                    notificacionesArrayList.add(jsonObject.optString("fecha")+","+"M:"+jsonObject.optString("cuerpo"));
                                                }
                                                cargarListaMantenimientoAgendadod(idCitaC);
                                                //saveArrayList(notificacionesArrayList, "lista_notificaciones");


                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                            ////
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
    private void cargarListaMantenimientoAgendadod(int idCitas) {

        String ip=getString(R.string.ipWebService);
        String url=ip+"/getlistMantenimientoByidCita.php?id_citas="+idCitas;
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
                                            //cargamos el recicle view
                                            Mantenimiento mantenimiento=null;
                                            JSONArray json=response.optJSONArray("dato");

                                            try {
                                                for (int i=0;i<json.length();i++ )
                                                {
                                                    mantenimiento=new Mantenimiento();
                                                    JSONObject jsonObject=null;
                                                    jsonObject=json.getJSONObject(i);


                                                    mantenimiento.setId_mantenimiento(jsonObject.getInt("id_mantenimiento"));
                                                    mantenimiento.setNombre(jsonObject.getString("nombre"));
                                                    mantenimiento.setObservacionesMecanico(jsonObject.getString("observacion"));
                                                    mantenimiento.setId_mecanico(jsonObject.getInt("id_mecanico"));

                                                    mantenimientoArrayList.add(jsonObject.getString("nombre")+","+jsonObject.getString("observacion"));
                                                }


                                                //saveArrayList(mantenimientoArrayList,"mantenimiento_lista");

                                                //<---permisos cuando el dispositivo con marshmallow
                                                if(Build.VERSION.SDK_INT >Build.VERSION_CODES.M){
                                                    //systema OS >= marshmallow(6.0), chequea si los permisos esta habilitado o no
                                                    if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)==
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


                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                            ////
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

    private ArrayList<String[]>getNotificaciones(){
        ArrayList<String[]> rows=new ArrayList<>();
        String[] array = null;
        try {
            //sopNoti= getArrayList("lista_notificaciones");
            for (int i=0;i<=notificacionesArrayList.size();i++){
                array = notificacionesArrayList.get(i).split(",");
                rows.add(array);
                Log.d("salida",""+rows.get(i).toString());
            }
        }catch (Exception e){
            String mensaje2="Servicio no disponible.....!";
            Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
        }


        return  rows;
    }
    private ArrayList<String[]>getKilometraje(){
        ArrayList<String[]> rows=new ArrayList<>();
        String[] array = null;
        try{
            //sop= getArrayList("lista_kilometraje");
            for (int i=0;i<arrPackage.size();i++){
                array =arrPackage.get(i).split(",");
                rows.add(array);
                Log.d("salida",""+rows.get(i).toString());
            }
        }
        catch (Exception e){
            String mensaje2="Servicio no disponible.....!";
            Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
        }



        return  rows;
    }
    private ArrayList<String[]>getMantenimientos(){
        ArrayList<String[]> rows=new ArrayList<>();
        String[] array = null;

        try{
           // sopMantenimiento= getArrayList("mantenimiento_lista");
            for (int i=0;i<mantenimientoArrayList.size();i++){

                array =mantenimientoArrayList.get(i).split(",");
                rows.add(array);

                Log.d("salida",""+rows.get(i).toString());
            }
        }
        catch (Exception e){
            String mensaje2="Servicio no disponible.....!";
            Toast.makeText(getActivity(),mensaje2,Toast.LENGTH_SHORT).show();
        }



        return  rows;
    }

    private void savePDF(){


        Date d = new Date();
        CharSequence s = DateFormat.format("MMMM d, yyyy ", d.getTime());


        int idMecanico=pref.getInt("idmecanico",0);

        String fechaT= pref.getString("fecha", null);
        String marcaAuto= pref.getString("marca", null);
        String placaAuto= pref.getString("placa", null);


        String[]headers={"Fecha","Kilometraje"};
        String[]hederesNoti={"Fecha","Detalle"};
        String[]hedersMante={"Nombre","Observaciones"};
        TemplatePDF templatePDF=new TemplatePDF(getContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("Reporte Mantenimiento", "Mantenimiento",correoUsuarioLogin);
        templatePDF.addTitulo("Reporte Mantenimiento",marcaAuto+" "+placaAuto,s.toString());
        templatePDF.addPagraph("El presente reporte muestra informacion del vehículo, como se detalla acontinuación.");
        templatePDF.addPagraph("Cita Programada :"+fechaT);
        templatePDF.addPagraph("Mecanico :"+idMecanico);
        templatePDF.addPagraph("Kilometrajes ingresados:");
        templatePDF.createTable(headers,getKilometraje());
        templatePDF.addPagraph("Alertas sujeridas por la APP:");
        templatePDF.createTable(hederesNoti,getNotificaciones());
        templatePDF.addPagraph("Trabajos realizados:");
        templatePDF.createTable(hedersMante,getMantenimientos());
        templatePDF.clseDocument();

        editor.remove("idmecanico");
        editor.remove("fecha");
        editor.remove("marca");
        editor.remove("placa");

        editor.clear();
        editor.commit();
        templatePDF.viewPDF(getContext());

        arrPackage.clear();
        notificacionesArrayList.clear();
        mantenimientoArrayList.clear();

        // [START custom_event]
        Bundle params = new Bundle();
        params.putString("button", "generarPDF");
        params.putString("usuario", correoUsuarioLogin);
        mFirebaseAnalytics.logEvent("report", params);
        // [END custom_event]
    }
    public void saveArrayList(ArrayList<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }
    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(json, type);
    }



}
