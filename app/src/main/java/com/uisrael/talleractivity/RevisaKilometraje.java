package my.jviracocha.talleractivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RevisaKilometraje extends Service {

    private Timer timer = new Timer();
    JSONArray kilometrajeVariosAutos;
    JSONArray partes;
    String idCliente = null;
    String deviceId= null;

    private ArrayList<String> listaautos;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startService();

        //idCliente = Preferences.obtenerPreferenceString(RevisaKilometraje.this,Preferences.USUARIO_ID);; //<--------------------------Recibir el id del cliente con los bundles
       //idCliente="17";
        Log.i("salida","idClienteAlRevisarKilometraje: "+idCliente);
        verificarKilometraje( idCliente );
        //reviva el proceso
        return Service.START_STICKY;
    }


    //ESTE SERVICIO PERMITE REALIZAR LA BÚQUEDA DE LAS CITAS CADA CIERTO TIEMPO.
    private void startService()
    {
        timer.scheduleAtFixedRate(new mainTask(),0,1000*60*15);  //  <--------------------------Tiempo de actualización
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
           // verificarKilometraje( idCliente ); //<-------------la identificacion del usuario que se quiere consultar
            String listaIdAutos = "";

            if(kilometrajeVariosAutos != null){
                try {

                    String paraMostrarKm ="";

                    for(int i =0; i<kilometrajeVariosAutos.length(); i++){
                        JSONObject km = kilometrajeVariosAutos.getJSONObject(i);

                        if(!km.getString("fechaKm").isEmpty()){
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date fechaFinal = new Date();
                            Date fechaInicial=dateFormat.parse(km.getString("fechaKm"));
                            Log.d("salida","fecha slaida "+fechaInicial.toString());

                            int dias=(int)((fechaFinal.getTime()-fechaInicial.getTime())/86400000);
                            Log.d("salida","resultado dia "+dias);
                            if(dias > 1){  //<-----------------------------------------------------------------CAMBIAR DIA
                                paraMostrarKm += "INGRESE KM:"+km.getString("placa");

                                //listaIdAutos += km.getString("id_auto") + ",";//<---------------------------REVISAR (18) ingresar un array
                                //listaautos.add(km.getString("id_auto"));

                            }
                        }else{
                            paraMostrarKm += "INGRESE KM:"+km.getString("placa")+"*";
                            Log.d("salida","ifkilometrajevariosautos"+paraMostrarKm);
                        }
                       // listaIdAutos += km.getString("id_auto") + ",";//<---------------------------REVISAR (18) ingresar un array
                        listaautos.add(km.getString("id_auto"));
                    }

                    Log.d("salida","para mostrar"+paraMostrarKm);
                    //Log.d("salida","lista autos:"+listaIdAutos);

                    if(!paraMostrarKm.isEmpty()){
                    notificationKm(paraMostrarKm);}

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                    Log.e("error",e.toString());
                }

            }

            /*if(!listaIdAutos.isEmpty()){
                verificarPartes(listaIdAutos);
                Log.d("salida","enviando el id del auto"+ listaIdAutos);
            }*/

            //metodo paralelo que prueba la existencia de datos en el string
            if(listaautos.size()>0){
                for(int i=0;i<listaautos.size();i++){

                    listaIdAutos=listaIdAutos+listaautos.get(i);
                    if(i!=listaautos.size()-1){
                        listaIdAutos=listaIdAutos+",";
                    }
                   // verificarPartes(listaautos.get(i));
                }

                verificarPartes(listaIdAutos);
            }else {
                Log.d("salida","no se esta cargando nada en la listaautos");
            }

            String paraMostrarParte ="";

            if(partes != null && kilometrajeVariosAutos != null){
                try{
                    Log.d("salida","llegando el valor de partes: "+partes);
                    Log.d("salida","llegando el valor de variosautos: "+kilometrajeVariosAutos);

                    for (int i=0; i< partes.length();i++){
                        JSONObject parte = partes.getJSONObject(i);

                        //Log.e("aa",parte.getString("nombre"));
                        for(int j=0; j<kilometrajeVariosAutos.length();j++){
                            JSONObject km = kilometrajeVariosAutos.getJSONObject(j);

                            Log.d("salida",String.valueOf(j)+km.getString("placa"));

                            if(parte.getString("id_auto").equals(km.getString("id_auto"))){

                                int start=Integer.parseInt(parte.getString("vencimientoKM"));
                                int end=Integer.parseInt(Integer.valueOf(km.getString("kilometraje")).toString());

                                int result=start-end;

                                Log.d("salida","start and : "+result);

                                if(Integer.valueOf(parte.getString("vencimientoKM")) < Integer.valueOf(km.getString("kilometraje")) || result<=200){
                                    paraMostrarParte += parte.getString("nombre") +"-"+ km.getString("placa")+"  ";
                                }
                            }
                        }

                    }


                }catch (Exception e){
                    Log.e("errorXX",e.toString());
                }
            }

            if(!paraMostrarParte.isEmpty()){
                Log.i("partes",paraMostrarParte);
                notificationPartes(paraMostrarParte);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private void verificarKilometraje(final String id_cliente)
    {




        String url="http://18.217.156.145/consultarKm.php";

        RequestQueue request;
        StringRequest stringRequest;

        request= Volley.newRequestQueue(getApplicationContext());
        stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject respuesta = new JSONObject(response);
                    kilometrajeVariosAutos = respuesta.getJSONArray("kilometraje");


                }catch (Exception e){
                    Log.i("RSPnotif",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("RSPnotif",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros= new HashMap<>();
                parametros.put("id_cliente", id_cliente);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void verificarPartes(final String listaIdAutos)
    {


        String url="http://18.217.156.145/consultarPartes.php";

        RequestQueue request;
        StringRequest stringRequest;

        request= Volley.newRequestQueue(getApplicationContext());
        stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    //Log.e("lasPartes",response);
                    JSONObject respuesta = new JSONObject(response);
                    partes = respuesta.getJSONArray("partes");


                }catch (Exception e){
                    Log.e("RSPnotif",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RSPnotif**",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros= new HashMap<>();
                parametros.put("listaIdAutos", listaIdAutos);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    //notificacion estatica
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setContentTitle("Applicacion is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);

    }

    //es la notificaion del kilometraje
    private void notificationKm(String text) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intentKM =new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pendingIntentKM=PendingIntent.getActivity(getApplicationContext(),100,intentKM,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Taller")
                .setContentIntent(pendingIntentKM)
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Taller de autos")
                .setContentText(text)
                .setContentInfo("Kilometraje");
        notificationManager.notify(1, notificationBuilder.build());

        insertNotificacionService(text,Integer.parseInt(idCliente),deviceId);
    }

    //notificaion de las partes
    private void notificationPartes(String text) {

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("idFragment", "partes");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Taller")
                .setContentIntent(pendingIntent)
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Taller de autos")
                .setContentText(text)
                .setContentInfo("Kilometraje");
        notificationManager.notify(2, notificationBuilder.build());
        if(!text.isEmpty()){
        insertNotificacionService(text,Integer.parseInt(idCliente),deviceId);
        }
    }

    private void insertNotificacionService(String cuerpo,int id_cliente, String deviceId){
        String ip=getString(R.string.ipWebService);
        String url=ip+"/insertNotificacionServicio.php?cuerpo="+cuerpo+"&id_cliente="+id_cliente+"&deviceId="+deviceId;
        url=url.replace(" ","%20");
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getBaseContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String estado=response.getString("estado");
                                    switch (estado){
                                        case "1":
                                            String dato1=response.getString("dato");
                                            Log.d("salida",dato1);
                                            break;
                                        case "2":
                                            String dato2=response.getString("dato");
                                            Log.d("salida",dato2);
                                            break;
                                    }

                                }catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }


                            }




                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //gerar mensaje de error
                                Log.e("error",error.toString());

                            }
                        }
                ));


    }
}
