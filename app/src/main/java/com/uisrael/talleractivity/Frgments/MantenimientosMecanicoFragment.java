package my.jviracocha.talleractivity.Frgments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import my.jviracocha.talleractivity.R;

public class MantenimientosMecanicoFragment extends Fragment {

    private EditText nombreMecanico;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista= inflater.inflate(R.layout.fragment_mantenimientos_mecanico,container,false);

        nombreMecanico=(EditText)vista.findViewById(R.id.txtNombreMecanicoRegistro);

        Bundle bundle=getArguments();
        String datoLlegada=bundle.getString("nombre");
        String correollegada=bundle.getString("correo");
        nombreMecanico.setText(datoLlegada+","+correollegada);
        return vista;
    }
}
