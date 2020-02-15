package my.jviracocha.talleractivity.Frgments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.jviracocha.talleractivity.R;
import my.jviracocha.talleractivity.VolleySingleton;

public class FragmentAdministradorReportesEstadistica extends Fragment {

    private PieChart pieChart;
    private BarChart barChart;

    private String[]dias;//=new String[]{"Lunes","Martes","Miércoles","Jueves","Viernes"};
    private String[]nombres;
    private Integer[]mantenimientos;
    private int[]colors=new int[]{Color.BLACK,Color.RED,Color.GREEN,Color.BLUE,Color.LTGRAY,Color.RED};
    private Integer[]citas;//=new int[]{3,4,5,10,5};


    private ArrayList<String> listaDiasReport;
    private ArrayList<Integer> listaCantidadCitas;
    private  ArrayList<Integer> listaMantenimientos;
    private  ArrayList<String> listaNombreMantenimiento;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_administrador_reportes_estadistica,container,false);

        pieChart=(PieChart)vista.findViewById(R.id.graficaPastel);
        barChart=(BarChart)vista.findViewById(R.id.graficaBarras);
        listaDiasReport=new ArrayList<>();
        listaCantidadCitas=new ArrayList<>();
        listaMantenimientos=new ArrayList<>();
        listaNombreMantenimiento=new ArrayList<>();



        getlistaCitasReport();
        getlistaManteReport();

        //crearChart();


        return vista;
    }
    private Chart getSameChart(Chart chart,String description,int textColor,int background, int animation){
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(background);
        chart.animateY(animation);
        legend(chart);
        return chart;

    }
    private Chart getSameChartpie(Chart chart,String description,int textColor,int background, int animation){
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(background);
        chart.animateY(animation);
        legendpie(chart);
        return chart;

    }
    private void legendpie(Chart chart){
        Legend legend=chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry>entries=new ArrayList<>();
        for (int i=0;i<nombres.length;i++){
            LegendEntry entry= new LegendEntry();
            entry.formColor=colors[i];
            entry.label=nombres[i];
            entries.add(entry);
        }
        legend.setCustom(entries);

    }
    private void legend(Chart chart){
        Legend legend=chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry>entries=new ArrayList<>();
        for (int i=0;i<dias.length;i++){
            LegendEntry entry= new LegendEntry();
            entry.formColor=colors[i];
            entry.label=dias[i];
            entries.add(entry);
        }
        legend.setCustom(entries);

    }
    //datos que van dentro de las gráficas
    private  ArrayList<BarEntry> getBarEntries(){
        ArrayList<BarEntry> entries=new ArrayList<>();
        for (int i=0;i<citas.length;i++){
            entries.add(new BarEntry(i,citas[i]));
        }
        return  entries;
    }
    private  ArrayList<PieEntry> getPieEntries(){
        ArrayList<PieEntry> entries=new ArrayList<>();
        for (int i=0;i<mantenimientos.length;i++){
            entries.add(new PieEntry(mantenimientos[i]));
        }
        return  entries;
    }
    private void axisX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(dias));
        axis.setEnabled(false);//desactivamos los titulos en el eje x
    }
    // valores al costado izquierdo
    private void axisLeft(YAxis axis){
       axis.setSpaceTop(20);
       axis.setAxisMinimum(0);
    }
    //para que no aparesca en la parte derecha
    private void axisRight(YAxis axis){
        axis.setEnabled(false);
    }

    //creamos las graficas solo en las graficas xy
    public void crearChart(){
        barChart=(BarChart)getSameChart(barChart,"CITAS",Color.RED,Color.CYAN,3000);
        barChart.setDrawGridBackground(true);//lineado en la grafica
        barChart.setDrawBarShadow(true);//sombreado en la gráfica
        barChart.setData(getBarData());
        barChart.invalidate();

        axisX(barChart.getXAxis());
        axisLeft(barChart.getAxisLeft());
        axisRight(barChart.getAxisRight());


       /* pieChart=(PieChart)getSameChartpie(pieChart,"MANTENIMIENTO",Color.GRAY,Color.MAGENTA,3000);
        pieChart.setHoleRadius(10);
        pieChart.setTransparentCircleRadius(12);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setData(getPieData());
        pieChart.invalidate();*/



    }
    public void createChartPie(){
        pieChart=(PieChart)getSameChartpie(pieChart,"MANTENIMIENTO",Color.GRAY,Color.MAGENTA,3000);
        pieChart.setHoleRadius(10);
        pieChart.setTransparentCircleRadius(12);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setData(getPieData());
        pieChart.invalidate();
    }
    //agregar datos específicos para cada tipo de gráfica
    private DataSet getData(DataSet dataSet){
        dataSet.setColors(colors);
        dataSet.setValueTextSize(Color.WHITE);
        dataSet.setValueTextSize(10);

        return dataSet;
    }
    //personalizar el contenido de cada una de las graficas
    private BarData getBarData(){
        BarDataSet barDataSet=(BarDataSet)getData(new BarDataSet(getBarEntries(),""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData= new BarData(barDataSet);
        //cambiamos el ancho de las barras
        barData.setBarWidth(0.45f);
        return barData;
    }
    private PieData getPieData(){
        PieDataSet pieDataSet=(PieDataSet) getData(new PieDataSet(getPieEntries(),""));
        pieDataSet.setSliceSpace(2);//espacio de la linea que seprar las grafica depastel
        pieDataSet.setValueFormatter(new PercentFormatter());
        return new PieData(pieDataSet);
    }

    private void getlistaCitasReport(){
        String ip=getString(R.string.ipWebService);
        String url=ip+"/getlistaCitasCantidadReportEstadistico.php";
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

                                                    listaDiasReport.add(jsonObject.optString("DIA_SEMANA")+"-"+jsonObject.optString("dia"));
                                                    listaCantidadCitas.add(jsonObject.optInt("totalcitas"));
                                                }
                                                dias=new String[listaDiasReport.size()];
                                                citas=new Integer[listaCantidadCitas.size()];

                                                dias=listaDiasReport.toArray(dias);
                                                citas=listaCantidadCitas.toArray(citas);
                                                //getlistaManteReport();
                                                crearChart();




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

    private void  getlistaManteReport(){
        String ip=getString(R.string.ipWebService);
        String url=ip+"/getlistaMantenimientosReportesEstadisticos.php";
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


                                                    listaMantenimientos.add(jsonObject.optInt("total_mante"));
                                                    listaNombreMantenimiento.add(jsonObject.optString("nombre"));
                                                }
                                                mantenimientos=new Integer[listaMantenimientos.size()];
                                                nombres=new String[listaNombreMantenimiento.size()];
                                                mantenimientos=listaMantenimientos.toArray(mantenimientos);
                                                nombres=listaNombreMantenimiento.toArray(nombres);

                                                createChartPie();

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




}
