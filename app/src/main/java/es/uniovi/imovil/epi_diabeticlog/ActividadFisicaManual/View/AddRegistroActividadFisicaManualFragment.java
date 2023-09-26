package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.View;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManual;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentAddRegistroActividadFisicaManualBinding;

public class AddRegistroActividadFisicaManualFragment extends Fragment implements View.OnClickListener{

    private FragmentAddRegistroActividadFisicaManualBinding binding;

    private Spinner spinner_tipo_actividad;
    private Spinner spinner_intensidad;
    private TextView hora_inicio;
    private TextView hora_final;
    private EditText kcal_quemadas_input;
    private  Button boton_agregar_registro;

    private Calendar fecha_inicio;
    private Calendar fecha_fin;

    private static final int OPTION=1;

    //constructor por defecto
    public AddRegistroActividadFisicaManualFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentAddRegistroActividadFisicaManualBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_add_registroactividadfisicamanual));

        spinner_tipo_actividad=binding.spinnerTipoActividad;
        ArrayAdapter<CharSequence> arrayAdapter_tipo_aplicacion = ArrayAdapter.createFromResource(this.getActivity(), R.array.tipos_actividad, android.R.layout.simple_spinner_item);
        arrayAdapter_tipo_aplicacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_actividad.setAdapter(arrayAdapter_tipo_aplicacion);

        spinner_intensidad=binding.spinnerIntensidad;
        ArrayAdapter<CharSequence> arrayAdapter_tipo_insulina = ArrayAdapter.createFromResource(this.getActivity(), R.array.tipos_intensidad, android.R.layout.simple_spinner_item);
        arrayAdapter_tipo_insulina.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_intensidad.setAdapter(arrayAdapter_tipo_insulina);

        kcal_quemadas_input=binding.kcalQuemadasInput;
        boton_agregar_registro=binding.botonAgregarRegistro;

        hora_inicio=binding.horaInicio;
        hora_inicio.setOnClickListener(this);

        hora_final=binding.horaFinal;
        hora_final.setOnClickListener(this);

        boton_agregar_registro.setOnClickListener(this);

        return view;
    }

    //metodo onclick implementado en el selectro de las horas inicio y final y el boton de guardado
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.hora_inicio:
                Calendar calendar= Calendar.getInstance();
                int hours=calendar.get(Calendar.HOUR_OF_DAY);
                int minute=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.setTimeZone(TimeZone.getDefault());
                        fecha_inicio=c;

                        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
                        String  time = format.format(c.getTime());
                        hora_inicio.setText(time);
                    }
                },hours, minute, false);
                timePickerDialog.show();
                break;

            case R.id.hora_final:
                Calendar calendar2= Calendar.getInstance();
                int hours2=calendar2.get(Calendar.HOUR_OF_DAY);
                int minute2=calendar2.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog2=new TimePickerDialog(this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.setTimeZone(TimeZone.getDefault());
                        fecha_fin=c;

                        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
                        String  time = format.format(c.getTime());
                        hora_final.setText(time);
                    }
                },hours2, minute2, false);
                timePickerDialog2.show();
                break;

            case  R.id.boton_agregar_registro:
                try {

                    RegistroActividadFisicaManual creada = new RegistroActividadFisicaManual();

                    creada.setTipo_actividad(this.spinner_tipo_actividad.getSelectedItem().toString());
                    creada.setIntensidad(this.spinner_intensidad.getSelectedItem().toString());
                    creada.setKcal_quemadas(Float.valueOf(this.kcal_quemadas_input.getText().toString()));
                    creada.setFecha_inicio(fecha_inicio.getTimeInMillis());
                    creada.setFecha_fin(fecha_fin.getTimeInMillis());

                    Calendar inicio=Calendar.getInstance();
                    inicio.setTimeInMillis(creada.getFecha_inicio());

                    Calendar fin=Calendar.getInstance();
                    fin.setTimeInMillis(creada.getFecha_fin());

                    if(inicio.before(fin)){

                        Bundle bundle=new Bundle();
                        bundle.putInt("option",this.OPTION);
                        bundle.putParcelable("registro_creado",creada);
                        bundle.putBoolean("clicked_add",true);
                        Navigation.findNavController(v).navigate(R.id.nav_registroactividadfisicamanual, bundle);

                    }
                    else {

                        Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorHorasRegistro), Toast.LENGTH_SHORT).show();

                    }
                    InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }catch(Exception e){
                        Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorDataRegistro), Toast.LENGTH_SHORT).show();
                }
                finally {
                    break;
                }
        }
    }
}