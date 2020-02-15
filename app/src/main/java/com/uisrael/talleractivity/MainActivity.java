package my.jviracocha.talleractivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.jviracocha.talleractivity.FragmentsCliente.AlertDialogoClienteAutoNuevo;
import my.jviracocha.talleractivity.FragmentsCliente.AlertDialogoClienteIngresoKilometraje;
import my.jviracocha.talleractivity.FragmentsCliente.AlertDialogoClienteModificarFecha;
import my.jviracocha.talleractivity.FragmentsCliente.AlertDialogoListaPartesMantenimiento;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentAgendamientoCita;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentClienteGestionCitas;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentClienteGestionAuto;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentClienteGestionNotificaciones;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentClienteGestionReportes;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentClienteListaMantenimientosAgendados;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentClienteListaNotificaciones;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentClienteMiCuenta;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentKilometrajeCliente;
import my.jviracocha.talleractivity.FragmentsCliente.FragmentListaAutoCliente;
import my.jviracocha.talleractivity.FragmentsCliente.FragmenteResumenAuto;
import my.jviracocha.talleractivity.FragmentsMecanico.AlertdialogoMecanicoEstadoCita;
import my.jviracocha.talleractivity.FragmentsMecanico.FragmentMecanicoGestionMantenimientos;
import my.jviracocha.talleractivity.FragmentsMecanico.FragmentMecanicoMiCuenta;
import my.jviracocha.talleractivity.FragmentsMecanico.FragmentMecanicoTrabajo;
import my.jviracocha.talleractivity.FragmentsMecanico.FragmentMecanicoVisualizaciones;
import my.jviracocha.talleractivity.FragmentsMecanico.FragmenteMecanicoObservacionesTrabajo;
import my.jviracocha.talleractivity.Frgments.AlertDialogoAdministradorCreateMecanico;
import my.jviracocha.talleractivity.Frgments.AlertDialogoAdministradorCreateNotificacion;
import my.jviracocha.talleractivity.Frgments.AlertDialogoAdministradorNuevoServicio;
import my.jviracocha.talleractivity.Frgments.AlertDialogoAdministradorUpdateCliente;
import my.jviracocha.talleractivity.Frgments.AlertDialogoAdministradorUpdateMecanico;
import my.jviracocha.talleractivity.Frgments.AlertDialogoAdministradorUpdateServicio;
import my.jviracocha.talleractivity.Frgments.FragmentAdministradorGestionReportes;
import my.jviracocha.talleractivity.Frgments.FragmentAdministradorReportesEstadistica;
import my.jviracocha.talleractivity.Frgments.GestionClienteFragment;
import my.jviracocha.talleractivity.Frgments.GestionMecanicoFragment;
import my.jviracocha.talleractivity.Frgments.GestionReporteFragment;
import my.jviracocha.talleractivity.Frgments.GestionServiciosFragment;
import my.jviracocha.talleractivity.Frgments.GestionServiciosNotificaciones;
import my.jviracocha.talleractivity.Frgments.MantenimientosMecanicoFragment;
import my.jviracocha.talleractivity.Frgments.RegistroClienteFragment;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        GestionMecanicoFragment.OnRegistroMecanicoSedListener,FragmentClienteGestionAuto.sedIdClienteListener,
        AlertDialogoClienteAutoNuevo.onUpdateGestAutoListener,FragmentListaAutoCliente.onUpdateGestAutoListener,
        AlertDialogoClienteIngresoKilometraje.onSendMessageKM,FragmentKilometrajeCliente.onUpdateListKM,
        FragmentClienteGestionCitas.sendDataGestion,FragmentAgendamientoCita.agendamientoCitaInterface,
        FragmentClienteListaMantenimientosAgendados.sendataListMante,AlertDialogoClienteModificarFecha .upsateListCitas,
        FragmentMecanicoGestionMantenimientos.sendDataforView,FragmentMecanicoTrabajo.sendDataMecanicoTrabajo,
        FragmenteMecanicoObservacionesTrabajo.sendDataMecanicoCitas,AlertdialogoMecanicoEstadoCita.onSendMessageCitas,
        GestionClienteFragment.sedDataClienteAdmin,AlertDialogoAdministradorUpdateCliente.onSendMenssageUpdateCliente,
        AlertDialogoAdministradorUpdateMecanico.onsendDataUpdateMecanico,AlertDialogoAdministradorCreateMecanico.onsendDataCreateMecanico,
        AlertDialogoAdministradorNuevoServicio.sendDataNuevoServicio,GestionServiciosFragment.gestionServicio,
        AlertDialogoAdministradorUpdateServicio.sendDataUpdateServicio,GestionServiciosNotificaciones.gestionNotificacionesInterface,
        AlertDialogoAdministradorCreateNotificacion.onSendDataCreateNotificacion,FragmentClienteMiCuenta.sedDatafragmentMiCuentainterface,
        FragmentMecanicoMiCuenta.sedDatafragmentMiCuentainterfaceMecanico,FragmentClienteGestionNotificaciones.sendDataGestionNotificaciones,
        FragmentClienteGestionReportes.interfaceReporteCliente,FragmentAdministradorGestionReportes.sendDataGestionReportes{

    private DrawerLayout drawer;
    private Menu menu;
    private String rolUsuairo;

    static TextView correoUsuario;
    String correoUsuarioLogin;
    String rolUsuarioLogin;
    String id_usuarioLogin;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        /*
        * Poner aqui lo de las notificaciones que estan en el login
        * */


        //lees de los puntExtras
        String idFragment = "partes";
        if(idFragment.equals("partes")){
            //abres el fragmet de las partes
        }else if(idFragment.equals("kilometraje")){
            //abres el fragmet del kilometraje
        }

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        correoUsuario=header.findViewById(R.id.txtCorreo);

        //Intent intentLogin=getIntent();
        //correoUsuarioLogin=intentLogin.getStringExtra(USUARIO_CORREO);
        //rolUsuarioLogin=intentLogin.getStringExtra(USUARIO_ROL);
        correoUsuarioLogin=Preferences.obtenerPreferenceString(MainActivity.this,Preferences.USUARIO_CORREO);
        rolUsuarioLogin=Preferences.obtenerPreferenceString(MainActivity.this,Preferences.USUARIO_ROL);
        id_usuarioLogin=Preferences.obtenerPreferenceString(MainActivity.this,Preferences.USUARIO_ID);

        Log.i("salida","resultado del preference : "+id_usuarioLogin);
        correoUsuario.setText(correoUsuarioLogin);


        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        menu=navigationView.getMenu();
        rolUsuairo=rolUsuarioLogin;

        // [START custom_event]
        Bundle params = new Bundle();
        params.putString("usuario", correoUsuarioLogin);
        params.putString("rolusuario", rolUsuarioLogin);
        mFirebaseAnalytics.logEvent("user_in_aplication", params);
        // [END custom_event]
        switch (rolUsuairo){

            case "Administrador":


                menu.setGroupVisible(R.id.grup_administrador,true);
                menu.setGroupVisible(R.id.grupo_cliente,false);
                menu.setGroupVisible(R.id.grupo_mecanico,false);
                break;
            case "Cliente":

                menu.setGroupVisible(R.id.grup_administrador,false);
                menu.setGroupVisible(R.id.grupo_cliente,true);
                menu.setGroupVisible(R.id.grupo_mecanico,false);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel =
                    new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
                    channel.enableLights(true);
                    channel.setLightColor(Color.RED);
                    channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                    channel.enableVibration(true);
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



        this.startService(service);
        //this.stopService(service);


                break;
            case "Mecanico":
                menu.setGroupVisible(R.id.grup_administrador,false);
                menu.setGroupVisible(R.id.grupo_cliente,false);
                menu.setGroupVisible(R.id.grupo_mecanico,true);
                break;
        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        getSupportActionBar().setTitle(menuItem.getTitle());

        switch (menuItem.getItemId()){
            case R.id.nav_gestionclientes:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,new GestionClienteFragment()).commit();

                // [START custom_event]
                Bundle params = new Bundle();
                params.putString("fragmente", "nav_gestionclientes");
                params.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params);
                // [END custom_event]
                break;

            case R.id.nav_gestionmecanicos:

                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,new GestionMecanicoFragment()).commit();
                Bundle bundle=new Bundle();
                bundle.putString("correo",correoUsuarioLogin);
                bundle.putString("rol",rolUsuarioLogin);
                FragmentManager fragmentManager=getSupportFragmentManager();
                GestionMecanicoFragment gestionMecanicoFragment= new GestionMecanicoFragment();
                gestionMecanicoFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_continer,gestionMecanicoFragment).commit();
                // [START custom_event]
                Bundle params1 = new Bundle();
                params1.putString("fragmente", "nav_gestionmecanicos");
                params1.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params1);
                // [END custom_event]
                break;
            case R.id.nav_gestionservicio:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,new GestionServiciosFragment()).commit();

                // [START custom_event]
                Bundle params2 = new Bundle();
                params2.putString("fragmente", "nav_gestionservicio");
                params2.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params2);
                // [END custom_event]
                break;
            case R.id.nav_gestioninsumos:
                AlertDialog.Builder mensaje=new AlertDialog.Builder(this);
                mensaje.setTitle("¿Desea Cerrar la sesión?");
                mensaje.setCancelable(false);
                mensaje.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Preferences.savePreferenceBoolean(MainActivity.this,false,Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                        Preferences.clearPreference(MainActivity.this,Preferences.USUARIO_ID);
                        Intent intent2= new Intent( MainActivity.this,loginActivity.class);
                        startActivity(intent2);
                        // [START custom_event]
                        Bundle params2 = new Bundle();
                        params2.putString("fragmente", "nav_gestioninsumos");
                        params2.putString("usuario", correoUsuarioLogin);
                        mFirebaseAnalytics.logEvent("navigation", params2);
                        // [END custom_event]
                        finish();
                    }
                });
                mensaje.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                mensaje.show();
                break;

                ///<Summary>
                //case que llama al fragment de gestion autos, esto ya pertenece al cliente
                ///<Summary>
            case R.id.nav_gestionAuto:

                Bundle bundle2=new Bundle();
                bundle2.putString("correo",correoUsuarioLogin);
                bundle2.putString("rol",rolUsuarioLogin);
                FragmentManager fragmentManager2=getSupportFragmentManager();
                FragmentClienteGestionAuto fragmentClienteGestionAuto= new FragmentClienteGestionAuto();
                fragmentClienteGestionAuto.setArguments(bundle2);
                fragmentManager2.beginTransaction().replace(R.id.fragment_continer,fragmentClienteGestionAuto,"fragmentClienteGestionAuto").commit();
                // [START custom_event]
                Bundle params3 = new Bundle();
                params3.putString("fragmente", "nav_gestionAuto");
                params3.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params3);
                // [END custom_event]
                break;
            case R.id.nav_agendaMantenimiento:

                Bundle bundle3=new Bundle();
                bundle3.putString("correo",correoUsuarioLogin);
                bundle3.putString("rol",rolUsuarioLogin);
                FragmentManager fragmentManager3=getSupportFragmentManager();

                FragmentClienteGestionCitas agendamientoCitas=new FragmentClienteGestionCitas();
                agendamientoCitas.setArguments(bundle3);
                fragmentManager3. beginTransaction().replace(R.id.fragment_continer,agendamientoCitas,null).commit();
                // [START custom_event]
                Bundle params4 = new Bundle();
                params4.putString("fragmente", "nav_agendaMantenimiento");
                params4.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params4);
                // [END custom_event]

                break;
            case R.id.nav_historicos:

                Bundle bundle4=new Bundle();
                bundle4.putString("correo",correoUsuarioLogin);
                bundle4.putString("rol",rolUsuarioLogin);

                FragmentManager fragmentManager4=getSupportFragmentManager();
                FragmentMecanicoGestionMantenimientos fragmentMecanicoGestionMantenimientos=new FragmentMecanicoGestionMantenimientos();
                fragmentMecanicoGestionMantenimientos.setArguments(bundle4);
                fragmentManager4.beginTransaction().replace(R.id.fragment_continer,fragmentMecanicoGestionMantenimientos,null).commit();
                // [START custom_event]
                Bundle params5 = new Bundle();
                params5.putString("fragmente", "nav_historicos");
                params5.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params5);
                // [END custom_event]
                break;

            case R.id.nav_mantenimientosAgednados:
                Bundle bundle1=new Bundle();
                bundle1.putString("correo",correoUsuarioLogin);
                bundle1.putString("rol",rolUsuarioLogin);

                FragmentManager fragmentManager1=getSupportFragmentManager();
                FragmentMecanicoTrabajo fragmentMecanicoTrabajo=new FragmentMecanicoTrabajo();
                fragmentMecanicoTrabajo.setArguments(bundle1);
                fragmentManager1.beginTransaction().replace(R.id.fragment_continer,fragmentMecanicoTrabajo,null).commit();
                // [START custom_event]
                Bundle params6 = new Bundle();
                params6.putString("fragmente", "nav_mantenimientosAgednados");
                params6.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params6);
                // [END custom_event]
                break;

            case R.id.nav_gestionnotificaciones:

                Bundle bundle5=new Bundle();
                bundle5.putString("correo",correoUsuarioLogin);
                bundle5.putString("rol",rolUsuarioLogin);
                FragmentManager fragmentManager5=getSupportFragmentManager();
                GestionServiciosNotificaciones gestionServiciosNotificaciones=new GestionServiciosNotificaciones();
                gestionServiciosNotificaciones.setArguments(bundle5);
                fragmentManager5.beginTransaction().replace(R.id.fragment_continer,gestionServiciosNotificaciones,null).commit();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,new GestionServiciosNotificaciones()).commit();
                // [START custom_event]
                Bundle params7 = new Bundle();
                params7.putString("fragmente", "nav_gestionnotificaciones");
                params7.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params7);
                // [END custom_event]
                break;
            case R.id.nav_miCuentaCliente:

                Bundle bundle6=new Bundle();
                bundle6.putString("correo",correoUsuarioLogin);
                bundle6.putString("rol",rolUsuarioLogin);
                FragmentManager fragmentManager6=getSupportFragmentManager();
                FragmentClienteMiCuenta fragmentClienteMiCuenta= new FragmentClienteMiCuenta();
                fragmentClienteMiCuenta.setArguments(bundle6);
                fragmentManager6.beginTransaction().replace(R.id.fragment_continer,fragmentClienteMiCuenta,null).commit();
                // [START custom_event]
                Bundle params8 = new Bundle();
                params8.putString("fragmente", "nav_miCuentaCliente");
                params8.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params8);
                // [END custom_event]
                break;

            case R.id.nav_salirApp:


                //<---------------------------------------------------------
                AlertDialog.Builder mensaje2=new AlertDialog.Builder(this);
                mensaje2.setTitle("¿Desea Cerrar la sesión?");
                mensaje2.setCancelable(false);
                mensaje2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Preferences.savePreferenceBoolean(MainActivity.this,false,Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                        Preferences.clearPreference(MainActivity.this,Preferences.USUARIO_ID);
                        Intent intent2= new Intent( MainActivity.this,loginActivity.class);
                        startActivity(intent2);
                        // [START custom_event]
                        Bundle params8 = new Bundle();
                        params8.putString("fragmente", "nav_salirApp");
                        params8.putString("usuario", correoUsuarioLogin);
                        mFirebaseAnalytics.logEvent("navigation", params8);
                        // [END custom_event]
                        finish();
                    }
                });
                mensaje2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                mensaje2.show();
                //<---------------------------------------------------------

                break;

            case R.id.nav_miCuentaMecanico:
                Bundle bundle7=new Bundle();
                bundle7.putString("correo",correoUsuarioLogin);
                bundle7.putString("rol",rolUsuarioLogin);
                FragmentManager fragmentManager7=getSupportFragmentManager();
                FragmentMecanicoMiCuenta fragmentMecanicoMiCuenta =new FragmentMecanicoMiCuenta();
                fragmentMecanicoMiCuenta.setArguments(bundle7);
                fragmentManager7.beginTransaction().replace(R.id.fragment_continer,fragmentMecanicoMiCuenta,null).commit();
                // [START custom_event]
                Bundle params9 = new Bundle();
                params9.putString("fragmente", "nav_miCuentaMecanico");
                params9.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params9);
                // [END custom_event]
                break;
            case R.id.nav_salirAppMecanico:
                AlertDialog.Builder mensaje3=new AlertDialog.Builder(this);
                mensaje3.setTitle("¿Desea Cerrar la sesión?");
                mensaje3.setCancelable(false);
                mensaje3.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Preferences.savePreferenceBoolean(MainActivity.this,false,Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                        Preferences.clearPreference(MainActivity.this,Preferences.USUARIO_ID);
                        Intent intent2= new Intent( MainActivity.this,loginActivity.class);
                        startActivity(intent2);
                        // [START custom_event]
                        Bundle params9 = new Bundle();
                        params9.putString("fragmente", "nav_salirAppMecanico");
                        params9.putString("usuario", correoUsuarioLogin);
                        mFirebaseAnalytics.logEvent("navigation", params9);
                        // [END custom_event]
                        finish();
                    }
                });
                mensaje3.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                mensaje3.show();
                break;

            case R.id.nav_misNotificaciones:
                FragmentManager fragmentManager8=getSupportFragmentManager();
                FragmentClienteGestionNotificaciones fragmentClienteGestionNotificaciones=new FragmentClienteGestionNotificaciones();
                fragmentManager8.beginTransaction().replace(R.id.fragment_continer,fragmentClienteGestionNotificaciones,null).commit();
                // [START custom_event]
                Bundle params10 = new Bundle();
                params10.putString("fragmente", "nav_misNotificaciones");
                params10.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params10);
                // [END custom_event]
                break;
            case R.id.nav_misReportes:
                FragmentManager fragmentManager9=getSupportFragmentManager();
                FragmentClienteGestionReportes fragmentClienteGestionReportes=new FragmentClienteGestionReportes();
                fragmentManager9.beginTransaction().replace(R.id.fragment_continer,fragmentClienteGestionReportes,null).commit();
                // [START custom_event]
                Bundle params11 = new Bundle();
                params11.putString("fragmente", "nav_misReportes");
                params11.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params11);
                // [END custom_event]
                break;

            case R.id.nav_misReportesAdmin:
                FragmentManager fragmentManager10=getSupportFragmentManager();
                //GestionReporteFragment gestionReporteFragment= new GestionReporteFragment();
                FragmentAdministradorGestionReportes fragmentAdministradorGestionReportes = new FragmentAdministradorGestionReportes();
                fragmentManager10.beginTransaction().replace(R.id.fragment_continer,fragmentAdministradorGestionReportes, null).commit();

                // [START custom_event]
                Bundle params12 = new Bundle();
                params12.putString("fragmente", "nav_misReportesAdmin");
                params12.putString("usuario", correoUsuarioLogin);
                mFirebaseAnalytics.logEvent("navigation", params12);
                // [END custom_event]
                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {

        /*if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);

        }else {
            super.onBackPressed();
            this.finish();
        }*/
        AlertDialog.Builder mensaje=new AlertDialog.Builder(this);
        mensaje.setTitle("¿Desea Salir de la Aplicacion?");
        mensaje.setCancelable(false);
        mensaje.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        mensaje.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mensaje.show();
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        menu.setGroupVisible(R.id.grup_administrador,false);
        return super.onPrepareOptionsMenu (menu);


    }
    //***********************interfaz para pasar datos****************************
    @Override
    public void onRegistroMecanicoSend(String datosMecanico,String correo) {

        FragmentManager fragmentManager4=getSupportFragmentManager();
        Bundle bundle =new Bundle();
        bundle.putString("rol",datosMecanico);
        bundle.putString("correo",correo);
        FragmentMecanicoGestionMantenimientos fragmentMecanicoGestionMantenimientos=new FragmentMecanicoGestionMantenimientos();
        fragmentMecanicoGestionMantenimientos.setArguments(bundle);
        fragmentManager4.beginTransaction().replace(R.id.fragment_continer,fragmentMecanicoGestionMantenimientos,null).commit();


    }

    @Override
    public void openDialogoCreateMecanico(int idAdministrador) {
        AlertDialogoAdministradorCreateMecanico alertDialogoAdministradorCreateMecanico=new AlertDialogoAdministradorCreateMecanico();
        Bundle bundle=new Bundle();
        bundle.putInt("idAdministrador",idAdministrador);
        alertDialogoAdministradorCreateMecanico.setArguments(bundle);
        alertDialogoAdministradorCreateMecanico.show(getSupportFragmentManager(),null);
    }

    @Override
    public void openDialogoUpdateMecanico(int idMecanico, String correoMec, int telefonoMec, String contrasenaMec, String nombreMec, int idAdmin) {

        AlertDialogoAdministradorUpdateMecanico alertDialogoAdministradorUpdateMecanico=new AlertDialogoAdministradorUpdateMecanico();
        Bundle bundle=new Bundle();
        bundle.putInt("idMecanico",idMecanico);
        bundle.putInt("telefonoMec",telefonoMec);
        bundle.putInt("idAdmin",idAdmin);
        bundle.putString("correoMec",correoMec);
        bundle.putString("contrasenaMec",contrasenaMec);
        bundle.putString("nombreMec",nombreMec);
        alertDialogoAdministradorUpdateMecanico.setArguments(bundle);
        alertDialogoAdministradorUpdateMecanico.show(getSupportFragmentManager(),null);
    }

    @Override
    public void sedIdCliente(int idCliente) {
        AlertDialogoClienteAutoNuevo alertDialogoClienteAutoNuevo=new AlertDialogoClienteAutoNuevo();
        Bundle bundle =new Bundle();
        bundle.putInt("idCliente",idCliente);
        alertDialogoClienteAutoNuevo.setArguments(bundle);

        alertDialogoClienteAutoNuevo.show(getSupportFragmentManager(),"Crear auto");
    }
    //metodo para enviar los datos idAuto idCliente al alter dialogo para poderingresar los datos
    @Override
    public void sendDataKMInsert(int idAuto, int idCliente) {
        AlertDialogoClienteIngresoKilometraje alertDialogoClienteIngresoKilometraje=new AlertDialogoClienteIngresoKilometraje();
        Bundle bundle=new Bundle();
        bundle.putInt("idCliente",idCliente);
        bundle.putInt("idAuto",idAuto);

        alertDialogoClienteIngresoKilometraje.setArguments(bundle);
        alertDialogoClienteIngresoKilometraje.show(getSupportFragmentManager(),"ingresoKilometraje");
    }

    //metodo para enviar datos al fragmento de lista de autos
    @Override
    public void sendDataKMList(int idAuto, int idCliente) {


        FragmentKilometrajeCliente fragmentKilometrajeCliente=new FragmentKilometrajeCliente();
        Bundle bundle=new Bundle();
        bundle.putInt("idCliente",idCliente);
        bundle.putInt("idAuto",idAuto);
        fragmentKilometrajeCliente.setArguments(bundle);

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerCliente, fragmentKilometrajeCliente,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //metodo de interfaz que permite cargar la lista de autos que ha creado un cliente
    @Override
    public void sedIdClienteAuto(int idCliente) {
        FragmentListaAutoCliente fragmentListaAutoCliente= new FragmentListaAutoCliente();
        Bundle bundle= new Bundle();
        bundle.putInt("idCliente2",idCliente);
        fragmentListaAutoCliente.setArguments(bundle);
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerCliente,fragmentSelected).addToBackStack(null).commit();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerCliente,fragmentListaAutoCliente,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void sedIdAuto(int idAutoP) {
        FragmenteResumenAuto framenteResumenAuto=new FragmenteResumenAuto();
        Bundle bundle= new Bundle();
        bundle.putInt("idAutoP",idAutoP);
        framenteResumenAuto.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerCliente,framenteResumenAuto,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //metodo que permite actulizar el fragmento luego de haberse creado un auto, el objetivo es que el spinner cargue nuevamente los autos desde la BDD
    @Override
    public void onUpdateGesAuto() {

        Bundle bundle2=new Bundle();
        bundle2.putString("correo",correoUsuarioLogin);
        bundle2.putString("rol",rolUsuarioLogin);
        FragmentManager fragmentManager2=getSupportFragmentManager();
        FragmentClienteGestionAuto fragmentClienteGestionAuto= new FragmentClienteGestionAuto();
        fragmentClienteGestionAuto.setArguments(bundle2);
        fragmentManager2.beginTransaction().replace(R.id.fragment_continer,fragmentClienteGestionAuto,"fragmentClienteGestionAuto").commit();
    }

    @Override
    public void onUpdateGesAutofromListaAuto(int idCliente) {
        onUpdateGesAuto();
        ///////
        sedIdClienteAuto(idCliente);
    }

    //metodo que llamado desde ingreso kilometraje del flotin aciton button
    @Override
    public void onsendmensaje(String mensaje) {

        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateFragmentGetionNotificaciones(String correo, String rol) {

        Bundle bundle5=new Bundle();
        bundle5.putString("correo",correoUsuarioLogin);
        bundle5.putString("rol",rolUsuarioLogin);
        FragmentManager fragmentManager5=getSupportFragmentManager();
        GestionServiciosNotificaciones gestionServiciosNotificaciones=new GestionServiciosNotificaciones();
        gestionServiciosNotificaciones.setArguments(bundle5);
        fragmentManager5.beginTransaction().replace(R.id.fragment_continer,gestionServiciosNotificaciones,null).commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,new GestionServiciosNotificaciones()).commit();

    }

    @Override
    public void resetListServicios() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,new GestionServiciosFragment()).commit();
    }

    @Override
    public void updateFragmentGestionMecanico() {
        Bundle bundle=new Bundle();
        bundle.putString("correo",correoUsuarioLogin);
        bundle.putString("rol",rolUsuarioLogin);
        FragmentManager fragmentManager=getSupportFragmentManager();
        GestionMecanicoFragment gestionMecanicoFragment= new GestionMecanicoFragment();
        gestionMecanicoFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_continer,gestionMecanicoFragment).commit();

    }

    @Override
    public void updateFragmentGestionCliente() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,new GestionClienteFragment()).commit();
    }

    @Override
    public void refreshListaTrabajo(int idMecanico) {

        Bundle bundle1=new Bundle();
        bundle1.putString("correo",correoUsuarioLogin);
        bundle1.putString("rol",rolUsuarioLogin);

        FragmentManager fragmentManager1=getSupportFragmentManager();
        FragmentMecanicoTrabajo fragmentMecanicoTrabajo=new FragmentMecanicoTrabajo();
        fragmentMecanicoTrabajo.setArguments(bundle1);
        fragmentManager1.beginTransaction().replace(R.id.fragment_continer,fragmentMecanicoTrabajo,null).commit();


    }

    @Override
    public void updateListKilometrajeIngresado(int idClient, int idAuto) {
        sendDataKMList(idAuto,idClient);
    }

    @Override
    public void onUpdateListKilometraje(int idCliente, int idAuto) {
        sendDataKMList(idAuto,idCliente);
    }

    //metodo para pasar datos al fragment de agendamiento de citas
    @Override
    public void sendDataCitas(int idCliente) {

        Bundle bundle= new Bundle();
        bundle.putInt("idCliente",idCliente);
        FragmentAgendamientoCita agendamientoCita= new FragmentAgendamientoCita();
        agendamientoCita.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerAgendaMantenimiento,agendamientoCita,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void sendDataCitasListAgenda(int idCliente) {
        Bundle bundle= new Bundle();
        bundle.putInt("idCliente",idCliente);
        FragmentClienteListaMantenimientosAgendados  agendados= new FragmentClienteListaMantenimientosAgendados();
        agendados.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerAgendaMantenimiento,agendados,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //metodo que permite actualizar el fragment de agendamiento de citas
    @Override
    public void updateFragmentAgenCita(int idCliente) {
        sendDataCitas(idCliente);
    }

    //método para enviar datos al alert dialogo que muestra una lista
    @Override
    public void sendIdClinteLisMan(int idCita) {
        //AlertDialogoClienteIngresoKilometraje alertDialogoClienteIngresoKilometraje=new AlertDialogoClienteIngresoKilometraje();
        AlertDialogoListaPartesMantenimiento alertDialogoListaPartesMantenimiento=new AlertDialogoListaPartesMantenimiento();
        Bundle bundle=new Bundle();
        bundle.putInt("idCita",idCita);
        alertDialogoListaPartesMantenimiento.setArguments(bundle);
        alertDialogoListaPartesMantenimiento.show(getSupportFragmentManager(),"listadepartesmantenimiento");



    }

    @Override
    public void onUpdateListaManteAgendado(int idCliente) {
        Bundle bundle= new Bundle();
        bundle.putInt("idCliente",idCliente);
        FragmentClienteListaMantenimientosAgendados  agendados= new FragmentClienteListaMantenimientosAgendados();
        agendados.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerAgendaMantenimiento,agendados,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void sendDataUpdateCita( int idCliente,int idCita,String fecha, String hora, int idAuto) {
        AlertDialogoClienteModificarFecha alertDialogoClienteModificarFecha=new AlertDialogoClienteModificarFecha();
        Bundle bundle= new Bundle();
        bundle.putInt("idCitas",idCita);
        bundle.putInt("idCliente",idCliente);
        bundle.putInt("idAuto",idAuto);
        bundle.putString("hora",hora);
        bundle.putString("fecha",fecha);
        alertDialogoClienteModificarFecha.setArguments(bundle);

        alertDialogoClienteModificarFecha.show(getSupportFragmentManager(),"modificarfechacita");

    }

    @Override
    public void sendDtaListaCitas(int idCliente) {
        sendDataCitasListAgenda(idCliente);
    }

    @Override
    public void sendMenajeUpdateCita(String mensaje) {
        onsendmensaje(mensaje);
    }

    @Override
    public void dataforView(int idcliente, int idAuto, int idCita) {
        Bundle bundle= new Bundle();
        bundle.putInt("idCliente",idcliente);
        bundle.putInt("idAuto",idAuto);
        bundle.putInt("idCita",idCita);
        FragmentMecanicoVisualizaciones visualizaciones= new FragmentMecanicoVisualizaciones();
        visualizaciones.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,visualizaciones,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void openFragTrabajo(int idCita, String placaAuto, int idMecanico) {

        Bundle bundle =new Bundle();
        bundle.putInt("idCita",idCita);
        bundle.putString("placaAuto",placaAuto);
        bundle.putInt("idMecanico",idMecanico);
        FragmenteMecanicoObservacionesTrabajo fragmenteMecanicoObservacionesTrabajo=new FragmenteMecanicoObservacionesTrabajo();
        fragmenteMecanicoObservacionesTrabajo.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,fragmenteMecanicoObservacionesTrabajo,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void opendilogochangeestadocita(String datoCita,int idCita,int idMecanico) {

        AlertdialogoMecanicoEstadoCita alertdialogoMecanicoEstadoCita=new AlertdialogoMecanicoEstadoCita();
        Bundle bundle=new Bundle();
        bundle.putString("mensaje",datoCita);
        bundle.putInt("idCita",idCita);
        bundle.putInt("idMecanico",idMecanico);
        alertdialogoMecanicoEstadoCita.setArguments(bundle);
        alertdialogoMecanicoEstadoCita.show(getSupportFragmentManager(),"cambiocita");



    }

    @Override
    public void openAlerDialogUpdateCliente(int idCliente, String nombre, String contrasena, String direccion, int telefono, String correo) {
        AlertDialogoAdministradorUpdateCliente alertDialogoAdministradorUpdateCliente=new AlertDialogoAdministradorUpdateCliente();
        Bundle bundle=new Bundle();
        bundle.putInt("idCliente",idCliente);
        bundle.putInt("telefono",telefono);
        bundle.putString("nombre",nombre);
        bundle.putString("contrasena",contrasena);
        bundle.putString("direccion",direccion);
        bundle.putString("correo",correo);
        alertDialogoAdministradorUpdateCliente.setArguments(bundle);
        alertDialogoAdministradorUpdateCliente.show(getSupportFragmentManager(),null);
    }

    @Override
    public void openAlertDialogoNewServicio() {
        AlertDialogoAdministradorNuevoServicio alertDialogoAdministradorNuevoServicio=new AlertDialogoAdministradorNuevoServicio();
        alertDialogoAdministradorNuevoServicio.show(getSupportFragmentManager(),null);
    }

    @Override
    public void openAlertDialgoUpdateServicio(String nomreServicio, int costoServicio, int kilometrajeServicio,int idServicio) {

        AlertDialogoAdministradorUpdateServicio alertDialogoAdministradorUpdateServicio=new AlertDialogoAdministradorUpdateServicio();
        Bundle bundle =new Bundle();
        bundle.putString("nombreServicio",nomreServicio);
        bundle.putInt("costoServicio",costoServicio);
        bundle.putInt("kilometrajeServicio",kilometrajeServicio);
        bundle.putInt("idServicio",idServicio);
        alertDialogoAdministradorUpdateServicio.setArguments(bundle);
        alertDialogoAdministradorUpdateServicio.show(getSupportFragmentManager(),null);




    }
    @Override
    public void openDilogoNuevaNotificaion(int idadministrador,String correo, String rol) {
        AlertDialogoAdministradorCreateNotificacion alertDialogoAdministradorCreateNotificacion=new AlertDialogoAdministradorCreateNotificacion();
        Bundle bundle =new Bundle();
        bundle.putInt("idadministrador",idadministrador);
        bundle.putString("correo",correo);
        bundle.putString("rol",rol);
        alertDialogoAdministradorCreateNotificacion.setArguments(bundle);
        alertDialogoAdministradorCreateNotificacion.show(getSupportFragmentManager(),null);
    }

    @Override
    public void openListaNotificacionesServicios() {

        FragmentClienteListaNotificaciones fragmentClienteGestionNotificaciones= new FragmentClienteListaNotificaciones();
        //agendados.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerClienteNootificaciones,fragmentClienteGestionNotificaciones,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    @Override
    public void openListaNotificacionesPromociones() {
        Bundle bundle5=new Bundle();
        bundle5.putString("correo",correoUsuarioLogin);
        bundle5.putString("rol",rolUsuarioLogin);
        GestionServiciosNotificaciones gestionServiciosNotificaciones=new GestionServiciosNotificaciones();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continerClienteNootificaciones,gestionServiciosNotificaciones,null);
        gestionServiciosNotificaciones.setArguments(bundle5);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        }

    @Override
    public void openreportPDFCliente() {
        Log.d("PDf","cracion del pdf ");


    }

    @Override
    public void opengestionreportesPDF() {
        FragmentManager fragmentManager10=getSupportFragmentManager();
        GestionReporteFragment gestionReporteFragment= new GestionReporteFragment();
        //FragmentAdministradorGestionReportes fragmentAdministradorGestionReportes = new FragmentAdministradorGestionReportes();
        fragmentManager10.beginTransaction().replace(R.id.fragment_continerGererarReportes,gestionReporteFragment, null).commit();
    }

    @Override
    public void opengestionreportesEstadisticas() {

        FragmentManager fragmentManager10=getSupportFragmentManager();
        FragmentAdministradorReportesEstadistica gestionReporteFragment= new FragmentAdministradorReportesEstadistica();
        //FragmentAdministradorGestionReportes fragmentAdministradorGestionReportes = new FragmentAdministradorGestionReportes();
        fragmentManager10.beginTransaction().replace(R.id.fragment_continerGererarReportes,gestionReporteFragment, null).commit();
    }


    //****************************************************************************




}
