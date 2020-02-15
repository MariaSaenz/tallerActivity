package my.jviracocha.talleractivity.FragmentsCliente;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import my.jviracocha.talleractivity.R;

public class FragmentClienteGestionNotificaciones extends Fragment {

    private BottomNavigationView bottomNavigationView;

    FloatingActionsMenu gruupoBotones;
    FloatingActionButton btnFabaSalir;
    private FirebaseAnalytics mFirebaseAnalytics;

    sendDataGestionNotificaciones sendDataGestionNotificacioneslister;
    public  interface sendDataGestionNotificaciones{

        public void openListaNotificacionesServicios();
        public  void  openListaNotificacionesPromociones();
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            sendDataGestionNotificacioneslister=(sendDataGestionNotificaciones) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_cliente_gestion_notificaciones,container,false);


        bottomNavigationView=(BottomNavigationView)vista.findViewById(R.id.botton_navigation_cliente_notificaciones);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        gruupoBotones=vista.findViewById(R.id.grupoFabClienteNotificacionesList);
        btnFabaSalir=vista.findViewById(R.id.idFabClienteNotificacionSalir);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        btnFabaSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.fragment_continer)).commit();
                gruupoBotones.collapse();
            }
        });

        return vista;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragmentSelected=null;
                    switch (menuItem.getItemId()){
                        case R.id.nav_notificacionesServicio:

                            try{
                                sendDataGestionNotificacioneslister.openListaNotificacionesServicios();
                            }catch (Exception a){
                                Toast.makeText(getContext(),"no se han registrado datos",Toast.LENGTH_SHORT).show();
                            }
                            // [START custom_event]
                            Bundle params = new Bundle();
                            params.putString("fragmente", "nav_notificacionesServicio");
                           // params.putString("usuario", correo);
                            mFirebaseAnalytics.logEvent("navigation", params);
                            // [END custom_event]
                            break;
                        case R.id.nav_notificacionesPromociones:
                            sendDataGestionNotificacioneslister.openListaNotificacionesPromociones();
                            // [START custom_event]
                            Bundle params2 = new Bundle();
                            params2.putString("fragmente", "nav_notificacionesPromociones");
                            // params.putString("usuario", correo);
                            mFirebaseAnalytics.logEvent("navigation", params2);
                            // [END custom_event]
                            break;


                    }

                    return true;
                }
            };
}
