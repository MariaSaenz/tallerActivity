package my.jviracocha.talleractivity.FragmentsCliente;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.jviracocha.talleractivity.Adaptadores.ParteAdapter;
import my.jviracocha.talleractivity.Entidades.Parte;
import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmenteResumenAuto extends Fragment {

    ArrayList<Parte> parteArrayList;
    private RecyclerView recyclerView;
    private ParteAdapter parteAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_cliente_resumen,container,false);

        parteArrayList=new ArrayList<>();
        recyclerView=(RecyclerView)vista.findViewById(R.id.reclieviewPartesAuto);
        recyclerView.hasFixedSize();
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);



        Bundle bundle=getArguments();
        int idAuto=bundle.getInt("idAutoP");
        cargarlistaPartes(idAuto);



        return  vista;
    }

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
                                                parteAdapter=new ParteAdapter(parteArrayList);
                                                recyclerView.setAdapter(parteAdapter);


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

}
