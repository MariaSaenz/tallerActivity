package com.uisrael.talleractivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import  android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import my.jviracocha.talleractivity.Entidades.Usuario;
import my.jviracocha.talleractivity.Frgments.RegistroClienteFragment;

public class loginActivity extends AppCompatActivity {

    JsonObjectRequest jsonObjectRequest;
    //objetos a crear dentro del login
    private EditText txtCorreo;
    private EditText txtContrasena;

    private TextInputLayout textInputEditTextCorreo, textInputEditTextContrasena;
    private TextInputEditText InputEditTexttxtCorreo, InputEditTexttxtContrasena;
    private Button btnLogin;
    private TextView lblCrearCuenta;
    private CheckBox checkBoxlogin;
    private boolean isActivateCheckBox;

    private FirebaseAnalytics mFirebaseAnalytics;

    //claves para el paso de datos al main activity

    /*private static final String STRING_PREFERENCE="jviracocha.talleractivity";
    private static final String PREFERENCE_ESTADO_BUTTON_SESION="estado.button.sesion";
    public static  final  String USUARIO_CORREO="usuario_correo";
    public static  final  String USUARIO_ROL="usuario_rol";*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(Preferences.obtenerPreferenceBoolean(loginActivity.this,Preferences.PREFERENCE_ESTADO_BUTTON_SESION)){//<-------------------------------------------ANTES DE QUE INICIE

            Intent miIntent = new Intent(loginActivity.this,MainActivity.class);
           // miIntent.putExtra(USUARIO_CORREO, "javier_12-9will@hotmail.com");//<----------datos momentaneos
            //miIntent.putExtra(USUARIO_ROL,"Administrador");//<----------------------------datos momentaneos

            //<-------------------------------GUARDAMOS EL ESTADO DE NUESTRO BOTON
            startActivity(miIntent);
        }
        runtimeEnableAutoInit();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
       //-------------------------------Poner en el main activity

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("alerts")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "ok";
                        if (!task.isSuccessful()) {
                            msg = "nook";
                        }
                        Log.d("LOGMain", msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        //inicia el proceso en segundo plano el daemon
        Intent service = new Intent(this, RevisaKilometraje.class);
        //this.startService(service);
        this.stopService(service);*/

        //-------------------------------Poner en el main activity

        textInputEditTextCorreo=(TextInputLayout)findViewById(R.id.txtInputLayotCorreo);
        textInputEditTextContrasena=(TextInputLayout)findViewById(R.id.txtInputLayotContrasena);
        InputEditTexttxtCorreo=(TextInputEditText)findViewById(R.id.txtCorreo);
        InputEditTexttxtContrasena=(TextInputEditText)findViewById(R.id.txtContrasena) ;



        btnLogin=(Button)findViewById(R.id.btnlogin);
        lblCrearCuenta=(TextView)findViewById(R.id.lblCrearCuenta);
        checkBoxlogin=(CheckBox)findViewById(R.id.checkBoxlogin);
        isActivateCheckBox=checkBoxlogin.isChecked();//CHEKBOC DESACTIVADO
        Log.i("salida","valor del chekbox:"+isActivateCheckBox);
        checkBoxlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActivateCheckBox){
                    checkBoxlogin.setChecked(false);
                }
                isActivateCheckBox=checkBoxlogin.isChecked();
                Log.i("salida","valor del chekbox:"+isActivateCheckBox);
            }
        });

        //creamos el metodo onClick del boton para tener acceso a la siguiente actiivdad que deriva en un fragment
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmImput(v);


            }


        });
        lblCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnLogin.setVisibility(View.INVISIBLE);
                String evento="nuevoUser";
                Bundle bundle=new Bundle();
                bundle.putString("evento",evento);
                // [START custom_event]
                Bundle params = new Bundle();
                params.putString("usuario","CrearUsuario" );
                mFirebaseAnalytics.logEvent("nuevoUser", params);
                // [END custom_event]
                FragmentManager fragmentManager=getSupportFragmentManager();
                RegistroClienteFragment registroClienteFragment= new RegistroClienteFragment();
                registroClienteFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.loginContainer,registroClienteFragment).commit();

            }
        });


    }

    private void cargrWebservice( String correo, String contrasena) {
        String ip=getString(R.string.ipWebService);
        String url=ip+"/appstart.php?correo="+correo+"&contrasena="+contrasena;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getApplicationContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json
                                procesarRespuesta(response);
                            }


                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //gerar mensaje de error
                                Context context = getApplicationContext();
                                CharSequence text= "No se encontro registro"+error.toString();
                                int duracion= Toast.LENGTH_SHORT;
                                Toast toast=Toast.makeText(context,text,duracion);
                                toast.show();
                            }
                        }
                ));

    }
    private void procesarRespuesta(JSONObject response) {
        //creo un objeto del tipo usuario para poder grabar los datos del json
        Usuario usuario= new Usuario();

        try {
            String estado= response.getString("estado");
            switch(estado){
                case "1":
                    //de ser 1 extraigo los datos del json
                    JSONObject jsonObjectDato=response.getJSONObject("dato");
                    usuario.setCorreo(jsonObjectDato.getString("correo"));
                    usuario.setRol(jsonObjectDato.getString("rol"));

                    //System.out.println(usuario.getRol());

                    Intent miIntent = new Intent(loginActivity.this,MainActivity.class);

                    // miIntent.putExtra("usuario_correo", usuario.getCorreo());<---- ya no es necesario
                    //miIntent.putExtra("rol_usuario",usuario.getRol());<------------ ya no es necesario

                   // guardarEstadoButton();//<-------------------------------GUARDAMOS EL ESTADO DE NUESTRO BOTON
                    Preferences.savePreferenceBoolean(loginActivity.this,checkBoxlogin.isChecked(),Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                    Preferences.savePreferenceString(loginActivity.this,usuario.getCorreo(),Preferences.USUARIO_CORREO);
                    Preferences.savePreferenceString(loginActivity.this,usuario.getRol(),Preferences.USUARIO_ROL);
                    obtenrIDUsuario(usuario.getCorreo(),usuario.getRol());
                    // [START custom_event]
                    Bundle params = new Bundle();
                    params.putString("usuario", usuario.getCorreo());
                    mFirebaseAnalytics.logEvent("user_login", params);
                    // [END custom_event]

                    startActivity(miIntent);
                    finish();//<----------------------------------------------ACTIVIDAD SE DESTRUYE POR COMPLETO

                    break;
                case "2":
                    String mensaje=response.getString("dato");
                    Context context = getApplicationContext();
                    CharSequence text=mensaje ;
                    int duracion= Toast.LENGTH_SHORT;
                    Toast toast=Toast.makeText(context,text,duracion);
                    toast.show();

                    break;
                case "3":
                    //System.out.println(response);

                    break;

            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    private boolean validarMail(){
        String mail=textInputEditTextCorreo.getEditText().getText().toString().trim();
        if(mail.isEmpty()){
            textInputEditTextCorreo.setError("Necesita correo");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            textInputEditTextCorreo.setError("Correo incorrecto");
            return false;

        }else  {
            textInputEditTextCorreo.setError(null);
            //textInputEditTextCorreo.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validarClave(){

        String clave=textInputEditTextContrasena.getEditText().getText().toString().trim();
        if(clave.isEmpty()){
            textInputEditTextContrasena.setError("Debe escribir su password");
            return false;

        }else {
            textInputEditTextContrasena.setError("");
            //textInputEditTextContrasena.setErrorEnabled(false);
            return true;
        }
    }
    public void confirmImput(View view){
        if(validarMail()&& validarClave()){

                cargrWebservice(textInputEditTextCorreo.getEditText().getText().toString().trim(),textInputEditTextContrasena.getEditText().getText().toString().trim());

                return;
        }else {
            String mensaje="Datos ingresador incorrectos";
            Context context = getApplicationContext();
            CharSequence text=mensaje ;
            int duracion= Toast.LENGTH_SHORT;
            Toast toast=Toast.makeText(context,text,duracion);
            toast.show();

        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    //sharPreference
    public void runtimeEnableAutoInit() {
        // [START fcm_runtime_enable_auto_init]
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        // [END fcm_runtime_enable_auto_init]
    }


    //metodos de busqueda en el webservice
    public void obtenrIDUsuario(String correo, String rol) {


        String ip=getString(R.string.ipWebService);
        String url=ip+"/getIDUser.php?correo="+correo+"&rol="+rol;
        //peticion get con sigelton
        VolleySingleton.getInstanceVolley(getApplicationContext()).
                addToRequestQueue(new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //procesar la respuesta json

                                String idClienteStr=null;
                                try{
                                    String estado=response.getString("estado");

                                    switch (estado){

                                        case "1":
                                            JSONObject jsonObjectDato=response.getJSONObject("dato");

                                            idClienteStr=jsonObjectDato.getString("id_cliente");

                                            int idClinte=Integer.parseInt(idClienteStr);

                                            //idClienteTrue.setText(idClienteStr);
                                            Preferences.savePreferenceString(loginActivity.this,idClienteStr,Preferences.USUARIO_ID);
                                            Log.d("salida","id apasarse: "+idClienteStr);

                                            break;
                                        case "2":
                                            String mensaje=response.getString("mensaje");
                                            Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
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
                                Context context = getApplicationContext();
                                CharSequence text= "No se encontro registro"+error.toString();
                                int duracion= Toast.LENGTH_SHORT;
                                Toast toast=Toast.makeText(context,text,duracion);
                                toast.show();
                            }
                        }
                ));

    }

}
