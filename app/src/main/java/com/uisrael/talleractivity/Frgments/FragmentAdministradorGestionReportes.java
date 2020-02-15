package my.jviracocha.talleractivity.Frgments;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import my.jviracocha.talleractivity.R;

public class FragmentAdministradorGestionReportes extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAnalytics mFirebaseAnalytics;

    sendDataGestionReportes sendDataGestionReportesListener;
    public  interface sendDataGestionReportes{
        public void opengestionreportesPDF();
        public  void  opengestionreportesEstadisticas();
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=(Activity) context;

        try{
            sendDataGestionReportesListener=(sendDataGestionReportes) activity;
        }catch (ClassCastException e){

            throw  new ClassCastException(activity.toString()+"mus implement on send");
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_administrador_gestion_reporte,container,false);
        bottomNavigationView=(BottomNavigationView)vista.findViewById(R.id.botton_navigation_administrador_reportes);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
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
                        case R.id.nav_reportePDF:

                            try{
                                //sendDataGestionNotificacioneslister.openListaNotificacionesServicios();
                                sendDataGestionReportesListener.opengestionreportesPDF();
                            }catch (Exception a){
                                Toast.makeText(getContext(),"no se han registrado datos",Toast.LENGTH_SHORT).show();
                            }
                            // [START custom_event]
                            Bundle params = new Bundle();
                            params.putString("fragmente", "nav_reportePDF");
                            //params.putString("usuario", correo);
                            mFirebaseAnalytics.logEvent("navigation", params);
                            // [END custom_event]
                            break;
                        case R.id.nav_reportEstadistic:
                            //sendDataGestionNotificacioneslister.openListaNotificacionesPromociones();
                            sendDataGestionReportesListener.opengestionreportesEstadisticas();
                            // [START custom_event]
                            Bundle params1 = new Bundle();
                            params1.putString("fragmente", "nav_reportEstadistic");
                            //params.putString("usuario", correo);
                            mFirebaseAnalytics.logEvent("navigation", params1);
                            // [END custom_event]
                            break;


                    }

                    return true;
                }
            };
}
