package es.uniovi.imovil.epi_diabeticlog.PantallaInicio.View;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.ViewModel.ActividadFisicaViewModel;
import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManual;
import es.uniovi.imovil.epi_diabeticlog.Alimentacion.ViewModel.AlimentacionViewModel;
import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.Ingesta;
import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;
import es.uniovi.imovil.epi_diabeticlog.Glucosa.ViewModel.GlucosaViewModel;
import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;
import es.uniovi.imovil.epi_diabeticlog.Insulina.ViewModel.InsulinaViewModel;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentPantallaInicioBinding;

public class PantallaInicioFragment extends Fragment {

    private FragmentPantallaInicioBinding binding;

    private PantallaInicioRecyclerViewAdapter adapter;

    private GlucosaViewModel viewModelGlucosa;
    private InsulinaViewModel viewModelInsulina;
    private ActividadFisicaViewModel viewModelActFisManual;
    private AlimentacionViewModel viewModelAlimentacion;

    private LineChart chart;
    private XAxis xAxis;

    private LinkedList<ILineDataSet> dataSets;
    private LineData lineData;

    private LineDataSet lineDataSet;
    private LineDataSet lineDataSet2;
    private LineDataSet lineDataSet3;
    private LineDataSet lineDataSet4;

    public PantallaInicioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelGlucosa = new ViewModelProvider(this).get(GlucosaViewModel.class);
        viewModelInsulina = new ViewModelProvider(this).get(InsulinaViewModel.class);
        viewModelActFisManual = new ViewModelProvider(this).get(ActividadFisicaViewModel.class);
        viewModelAlimentacion = new ViewModelProvider(this).get(AlimentacionViewModel.class);
        adapter = new PantallaInicioRecyclerViewAdapter(this.getActivity(), this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding= FragmentPantallaInicioBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_pantalla_inicio));

        RecyclerView recyclerView = (RecyclerView)binding.recyclerPantallaInicio;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        //inicializamos el grafico
        chart = (LineChart) binding.chart;

        //si no hay datos que se muestre este texto en este color
        chart.setNoDataText(getResources().getString(R.string.NoDatos));

        chart.setNoDataTextColor(Color.GRAY);
        Paint paint = chart.getPaint(Chart.PAINT_INFO);
        paint.setTextSize(70);

        //ponemos una descripcion vacia al grafico
        Description desc=new Description();
        desc.setText("");
        chart.setDescription(desc);

        //permitimos que sepuedan hacer gestos
        chart.setTouchEnabled(true);

        // premeticmos escalado y desplazamiento
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // forzamos al hacer zoom sea sobre los dos ejes
        chart.setPinchZoom(true);

        //definimos que el eje Y sea el izquierdo
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawLimitLinesBehindData(true);
        //anulamos el eje y derecho
        chart.getAxisRight().setEnabled(false);

        //definimos el rango de valores que tendrá el eje Y
        leftAxis.setAxisMinimum(30f);
        leftAxis.setAxisMaximum(250f);
        leftAxis.setLabelCount(12, true);


        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setEnabled(true);
        l.setFormSize(10f);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setTextSize(10f);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(8f);
        l.setYEntrySpace(5f);

        int colores[]= {Color.GREEN, Color.RED};

        String []labels=new String[] { getResources().getString(R.string.GlucosaEstable), getResources().getString(R.string.GlucosaPeligroso)};

        LegendEntry [] legendEntries=new LegendEntry[2];

        for(int i=0;i<legendEntries.length;i++){
            LegendEntry aux=new LegendEntry();
            aux.formColor= colores[i];
            aux.label=labels[i];
            legendEntries[i]=aux;
        }

        l.setCustom(legendEntries);

        //definimos la limitline superior apra indicar la hiperglucemia
        LimitLine limitLineSuperior=new LimitLine(180, "");
        limitLineSuperior.setLineColor(Color.RED);
        limitLineSuperior.setLineWidth(2f);
        limitLineSuperior.setTextColor(Color.BLACK);
        limitLineSuperior.setTextSize(12f);

        //definimos la limitline inferior para indicar la hipoglucemia
        LimitLine limitLineInferior=new LimitLine(70, "");
        limitLineInferior.setLineColor(Color.RED);
        limitLineInferior.setLineWidth(2f);
        limitLineInferior.setTextColor(Color.BLACK);
        limitLineInferior.setTextSize(12f);

        //añadimos las limitlines al eje y
        leftAxis.addLimitLine(limitLineInferior);
        leftAxis.addLimitLine(limitLineSuperior);

        //definimos el eje x
        xAxis = chart.getXAxis();
        xAxis.setDrawLimitLinesBehindData(true);

        //establecemos el rango de valores del eje x
        //el valor máximo es el numero de segundos que hay en el dia
        xAxis.setAxisMaximum(86399);
        xAxis.setAxisMinimum(0);

        // aqui se establece el numero de elementos en el eje X
        xAxis.setLabelCount(13, true);

        //indicamos la posicion del eje X, mejor abajo que es más visual
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //hacemos ahora un formateador para los valores del eje X
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
            @Override
            public String getFormattedValue(float value) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                long startDay=calendar.getTimeInMillis();

                long millis=(long)value*1000 + startDay;

                calendar.setTimeInMillis(millis);
                Date date = calendar.getTime();

                String formateada=mFormat.format(date);

                if(!formateada.equals("00:00")&&!formateada.equals("23:59")){
                    millis=millis+(1*1000);
                    calendar.setTimeInMillis(millis);
                    date = calendar.getTime();
                    formateada=mFormat.format(date);
                }

                return formateada;
            }
        });

        this.removeElementos();
        this.establecerElementos();

        return view;
    }

    //metodo para eliminar los elementos del adaptador
    private void removeElementos() {
        adapter.removeElementos();
    }

    //metodo para ordenar los elementos del adaptador y retornar una lista con estos items
    public List<ItemPantallaInicio> ordenarElementos(List<ItemPantallaInicio> itemPantallaInicios){

            long valorFechaMin = this.valorfechaMax(itemPantallaInicios);
            int pos = -1;
            ItemPantallaInicio aux = null;

            if(itemPantallaInicios.size()!=1) {

                for (int i = 0; i < itemPantallaInicios.size(); i++) {
                    for (int j = i; j < itemPantallaInicios.size(); j++) {
                        if (itemPantallaInicios.get(j).getFechaItem() <= valorFechaMin) {

                            valorFechaMin = itemPantallaInicios.get(j).getFechaItem();
                            aux = itemPantallaInicios.get(j);
                            pos = j;
                        }
                    }
                    itemPantallaInicios.set(pos, itemPantallaInicios.get(i));
                    itemPantallaInicios.set(i, aux);
                    valorFechaMin = this.valorfechaMax(itemPantallaInicios);
                }
            }
        return itemPantallaInicios;
    }

    //metodo que retorna el valor de la fecha mas reciente de los items
    public long valorfechaMax(List<ItemPantallaInicio> itemPantallaInicios){
        long maxFecha=-1;
        for (int i=0;i<itemPantallaInicios.size();i++){
            if(itemPantallaInicios.get(i).getFechaItem()>maxFecha){
                maxFecha=itemPantallaInicios.get(i).getFechaItem();
            }
        }
        return maxFecha;
    }

    //metodo que se encarga de obtener todos los elementos de las bases de datos cuya fecha coincide con la del dia actual
    //una vez obtenidos los elementos, se definen diferentes datasets que se dibujarán en el gráfica y en el recyclerview con el que cuenta el fragmento
    //este método se ejecuta cada vez que se navega hasta el fragmento
    public void establecerElementos(){
        viewModelGlucosa.getGlucosas().observe(getViewLifecycleOwner(), new Observer<List<Glucosa>>() {
            @Override
            public void onChanged(@Nullable List<Glucosa> glucosas) {
                if(glucosas!=null && glucosas.size()>0) {

                    adapter.removeGlucosas();

                    adapter.addGlucosas(glucosas);

                    List<ItemPantallaInicio> glucosasAux = new LinkedList<>(glucosas);

                    glucosasAux = ordenarElementos(glucosasAux);

                    Calendar calendarIniDia = Calendar.getInstance();
                    calendarIniDia.set(Calendar.DAY_OF_MONTH, (calendarIniDia.get(Calendar.DAY_OF_MONTH)));
                    calendarIniDia.set(Calendar.HOUR_OF_DAY, 0);
                    calendarIniDia.set(Calendar.MINUTE, 0);
                    calendarIniDia.set(Calendar.SECOND, 0);

                    List<ItemPantallaInicio> listitems = new LinkedList<ItemPantallaInicio>();

                    for (int i = 0; i < glucosasAux.size(); i++) {
                        listitems.add(glucosasAux.get(i));
                    }

                    List<Entry> entries = new LinkedList<Entry>();
                    List<Integer> colors = new LinkedList<Integer>();

                    for (ItemPantallaInicio data : listitems) {
                        if (data.getTipo().equals("Glucosa")) {
                            Glucosa aux = (Glucosa) data;
                            Entry entryAux = new Entry((data.getFechaItem() - calendarIniDia.getTimeInMillis()) / 1000, aux.getMgDl());
                            entries.add(entryAux);
                            float value = aux.getMgDl();
                            if (value <= 70 || value >= 180) {
                                colors.add(ResourcesCompat.getColor(getResources(), R.color.red, null));
                            } else {
                                colors.add(ResourcesCompat.getColor(getResources(), R.color.green, null));
                            }
                        }
                    }

                    if (lineDataSet == null) {

                        lineDataSet = new LineDataSet(entries, getResources().getString(R.string.GlucosaEnSangre));
                        lineDataSet.setCircleColors(colors);
                        lineDataSet.setCircleHoleColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                        lineDataSet.setColors(ResourcesCompat.getColor(getResources(), R.color.black, null));

                        lineDataSet.setCircleHoleRadius(4f);
                        lineDataSet.setCircleRadius(6f);
                        lineDataSet.setValueTextSize(10f);
                    }

                    if (dataSets == null) {
                        dataSets = new LinkedList<>();
                    }
                    dataSets.add(lineDataSet);

                    //esto deberia de ir al final, despues de todo
                    if (lineData == null) {
                        lineData = new LineData(dataSets);
                        chart.setData(lineData);
                        chart.invalidate();
                    } else {
                        lineData.addDataSet(lineDataSet);
                        chart.setData(lineData);
                        chart.invalidate();
                    }
                }
            }
        });

        viewModelInsulina.getInsulinas().observe(getViewLifecycleOwner(), new Observer<List<Insulina>>() {
            @Override
            public void onChanged(@Nullable List<Insulina> insulinas) {
                if (insulinas != null && insulinas.size()>0) {

                    adapter.removeInsulinas();

                    adapter.addInsulinas(insulinas);

                    List<ItemPantallaInicio> insulinasAux = new LinkedList<>(insulinas);

                    insulinasAux = ordenarElementos(insulinasAux);

                    Calendar calendarIniDia = Calendar.getInstance();
                    calendarIniDia.set(Calendar.DAY_OF_MONTH, (calendarIniDia.get(Calendar.DAY_OF_MONTH)));
                    calendarIniDia.set(Calendar.HOUR_OF_DAY, 0);
                    calendarIniDia.set(Calendar.MINUTE, 0);
                    calendarIniDia.set(Calendar.SECOND, 0);

                    List<ItemPantallaInicio> listitems = new LinkedList<ItemPantallaInicio>();

                    for (int i = 0; i < insulinasAux.size(); i++) {
                        listitems.add(insulinasAux.get(i));
                    }

                    List<Entry> entries = new LinkedList<Entry>();
                    List<Integer> colors = new LinkedList<Integer>();

                    colors.add(ResourcesCompat.getColor(getResources(), R.color.black, null));


                    for (ItemPantallaInicio data : listitems) {
                        Entry entryAux = new Entry((data.getFechaItem() - calendarIniDia.getTimeInMillis()) / 1000, 250, ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_insulina_inicio));
                        entries.add(entryAux);

                    }

                    if (lineDataSet2 == null) {
                        lineDataSet2 = new LineDataSet(entries, getResources().getString(R.string.AplicacionesInsulina));
                        lineDataSet2.setDrawIcons(true);
                        lineDataSet2.setCircleColors(colors);
                        lineDataSet2.setColors(ResourcesCompat.getColor(getResources(), R.color.gris, null));

                        lineDataSet2.setDrawValues(false);
                        lineDataSet2.setDrawCircleHole(false);
                        lineDataSet2.setCircleRadius(8f);
                    }
                    else{
                        lineDataSet2.setLabel("");
                    }

                    if (dataSets == null) {
                        dataSets = new LinkedList<>();
                    }
                    dataSets.add(lineDataSet2);


                    //esto deberia de ir al final, despues de todo
                    if (lineData == null) {
                        lineData = new LineData(dataSets);
                        chart.setData(lineData);
                        chart.invalidate();
                    } else {
                        lineData.addDataSet(lineDataSet2);
                        chart.setData(lineData);
                        chart.invalidate();

                    }
                }
            }
        });

        viewModelActFisManual.getRegistros().observe(getViewLifecycleOwner(), new Observer<List<RegistroActividadFisicaManual>>() {
            @Override
            public void onChanged(@Nullable List<RegistroActividadFisicaManual> registroActividadFisicaManuals) {
                if(registroActividadFisicaManuals!=null && registroActividadFisicaManuals.size()>0) {

                    adapter.removeRegistros();

                    adapter.addRegistros(registroActividadFisicaManuals);

                    List<ItemPantallaInicio> registroActividadFisicaManualsAux = new LinkedList<>(registroActividadFisicaManuals);

                    registroActividadFisicaManualsAux = ordenarElementos(registroActividadFisicaManualsAux);

                    Calendar calendarIniDia = Calendar.getInstance();
                    calendarIniDia.set(Calendar.DAY_OF_MONTH, (calendarIniDia.get(Calendar.DAY_OF_MONTH)));
                    calendarIniDia.set(Calendar.HOUR_OF_DAY, 0);
                    calendarIniDia.set(Calendar.MINUTE, 0);
                    calendarIniDia.set(Calendar.SECOND, 0);

                    List<ItemPantallaInicio> listitems = new LinkedList<ItemPantallaInicio>();

                    List<Integer> colors = new LinkedList<Integer>();
                    colors.add(ResourcesCompat.getColor(getResources(), R.color.black, null));

                    for (int i = 0; i < registroActividadFisicaManualsAux.size(); i++) {
                        listitems.add(registroActividadFisicaManualsAux.get(i));
                    }

                    List<Entry> entries = new LinkedList<Entry>();

                    for (ItemPantallaInicio data : listitems) {
                        Entry entryAux = new Entry((data.getFechaItem() - calendarIniDia.getTimeInMillis()) / 1000, 250, ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_acti_fisi_inicio));
                        entries.add(entryAux);

                    }

                    if (lineDataSet3 == null) {
                        lineDataSet3 = new LineDataSet(entries, getResources().getString(R.string.ActividadFisica));
                        lineDataSet3.setDrawIcons(true);
                        lineDataSet3.setCircleColors(colors);

                        lineDataSet3.setColors(ResourcesCompat.getColor(getResources(), R.color.gris, null));

                        lineDataSet3.setDrawValues(false);
                        lineDataSet3.setDrawCircleHole(false);
                        lineDataSet3.setCircleRadius(8f);
                    }
                    else{
                        lineDataSet3.setLabel("");
                    }

                    if (dataSets == null) {
                        dataSets = new LinkedList<>();
                    }
                    dataSets.add(lineDataSet3);

                    //esto deberia de ir al final, despues de todo
                    if (lineData == null) {
                        lineData = new LineData(dataSets);
                        chart.setData(lineData);
                        chart.invalidate();
                    } else {
                        lineData.addDataSet(lineDataSet3);
                        chart.setData(lineData);
                        chart.invalidate();
                    }
                }
            }
        });

        viewModelAlimentacion.getIngestas().observe(getViewLifecycleOwner(), new Observer<List<Ingesta>>() {
            @Override
            public void onChanged(@Nullable List<Ingesta> ingestas) {
                if(ingestas!=null && ingestas.size()>0) {

                    adapter.removeIngestas();

                    adapter.addIngestas(ingestas);

                    List<ItemPantallaInicio> ingestasAux = new LinkedList<>(ingestas);

                    ingestasAux = ordenarElementos(ingestasAux);

                    Calendar calendarIniDia = Calendar.getInstance();
                    calendarIniDia.set(Calendar.DAY_OF_MONTH, (calendarIniDia.get(Calendar.DAY_OF_MONTH)));
                    calendarIniDia.set(Calendar.HOUR_OF_DAY, 0);
                    calendarIniDia.set(Calendar.MINUTE, 0);
                    calendarIniDia.set(Calendar.SECOND, 0);

                    List<ItemPantallaInicio> listitems = new LinkedList<ItemPantallaInicio>();

                    List<Integer> colors = new LinkedList<Integer>();
                    colors.add(ResourcesCompat.getColor(getResources(), R.color.black, null));

                    for (int i = 0; i < ingestasAux.size(); i++) {
                        listitems.add(ingestasAux.get(i));

                    }

                    List<Entry> entries = new LinkedList<Entry>();

                    for (ItemPantallaInicio data : listitems) {
                        Entry entryAux = new Entry((data.getFechaItem() - calendarIniDia.getTimeInMillis()) / 1000, 250, ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_alimentacion_inicio));
                        entries.add(entryAux);
                    }

                    if (lineDataSet4 == null) {
                        lineDataSet4 = new LineDataSet(entries, getResources().getString(R.string.ingestas));
                        lineDataSet4.setDrawIcons(true);
                        lineDataSet4.setCircleColors(colors);

                        lineDataSet4.setColors(ResourcesCompat.getColor(getResources(), R.color.gris, null));

                        lineDataSet4.setDrawValues(false);
                        lineDataSet4.setDrawCircleHole(false);
                        lineDataSet4.setCircleRadius(8f);
                    }
                    else{
                        lineDataSet4.setLabel("");
                    }
                    if (dataSets == null) {
                        dataSets = new LinkedList<>();
                    }
                    dataSets.add(lineDataSet4);

                    if (lineData == null) {
                        lineData = new LineData(dataSets);
                        chart.setData(lineData);
                        chart.invalidate();
                    } else {
                        lineData.addDataSet(lineDataSet4);
                        chart.setData(lineData);
                        chart.invalidate();
                    }
                }
            }
        });

    }

}