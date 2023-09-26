package es.uniovi.imovil.epi_diabeticlog.Insulina.View;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentEditAplicacionInsulinaBinding;

public class EditAplicacionInsulinaFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FragmentEditAplicacionInsulinaBinding binding;

    private Spinner spinner_tipo_aplicacion_edit;
    private Spinner spinner_tipo_insulina_edit;
    private EditText num_unidades_edit;
    private TextView hora_aplicacion_edit;
    private Button boton_aceptar_cambios_aplicacion;

    private Calendar fecha_aplicacion;
    int id;

    private Insulina seleccionada;
    private static final int OPTION=2;


    public EditAplicacionInsulinaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            seleccionada = getArguments().getParcelable("seleccionada");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding= FragmentEditAplicacionInsulinaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_edit_insulina));

        if(seleccionada!=null) {

            id = seleccionada.getId();

            //primero seleccionamos el tipo de insulina aplicada
            spinner_tipo_insulina_edit = binding.spinnerTipoInsulinaEdit;
            ArrayAdapter<CharSequence> arrayAdapter_tipo_insulina = ArrayAdapter.createFromResource(this.getActivity(), R.array.tipos_insulina, android.R.layout.simple_spinner_item);
            arrayAdapter_tipo_insulina.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_tipo_insulina_edit.setAdapter(arrayAdapter_tipo_insulina);

            String Lenta=getResources().getString(R.string.Lenta);
            String Intermedia=getResources().getString(R.string.Intermedia);
            String Rapida=getResources().getString(R.string.Rapida);

            String Normal=getResources().getString(R.string.Normal);
            String Extra=getResources().getString(R.string.Extra);

            if(seleccionada.getTipo_insulina().equals(Lenta)){
                spinner_tipo_insulina_edit.setSelection(0);
            }
            else{
                if(seleccionada.getTipo_insulina().equals(Intermedia)){
                    spinner_tipo_insulina_edit.setSelection(1);
                }
                else{
                    if(seleccionada.getTipo_insulina().equals(Rapida)){
                        spinner_tipo_insulina_edit.setSelection(2);
                    }
                }
            }

            //tras esto establecemos el tipo de aplicacion
            spinner_tipo_aplicacion_edit = binding.spinnerTipoAplicacionEdit;
            ArrayAdapter<CharSequence> arrayAdapter_tipo_aplicacion = ArrayAdapter.createFromResource(this.getActivity(), R.array.tipos_aplicacion, android.R.layout.simple_spinner_item);
            arrayAdapter_tipo_aplicacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_tipo_aplicacion_edit.setAdapter(arrayAdapter_tipo_aplicacion);

            if(seleccionada.getTipo_aplicacion().equals(Normal)){
                this.spinner_tipo_aplicacion_edit.setSelection(0);
            }
            else{
                if(seleccionada.getTipo_aplicacion().equals(Extra)){
                    this.spinner_tipo_aplicacion_edit.setSelection(1);
                }
            }

            //ahora el numero de unidades aplicadas
            num_unidades_edit = binding.numUnidadesEdit;
            num_unidades_edit.setText(String.valueOf(seleccionada.getNum_unidades()));

            //inicializamos el boton de aceptar los cambios
            boton_aceptar_cambios_aplicacion = binding.botonAceptarCambiosAplicacion;

            //definimos la fecha en la que se produjo la aplicacion cogiendola de la seleccionada
            fecha_aplicacion = Calendar.getInstance();
            fecha_aplicacion.setTimeInMillis(seleccionada.getFecha_aplicacion());

            //le damos un formato para mostrarla en la etiqueta correspondiente
            SimpleDateFormat format = new SimpleDateFormat("k:mm a");
            String time = format.format(fecha_aplicacion.getTime());

            hora_aplicacion_edit = binding.horaAplicacionEdit;
            hora_aplicacion_edit.setText(time);


            boton_aceptar_cambios_aplicacion.setOnClickListener(this);
            hora_aplicacion_edit.setOnClickListener(this);
            spinner_tipo_insulina_edit.setOnItemSelectedListener(this);
        }
        return view;
    }

    //metodo que se ejecuta al pulsar el boton para guardar los cambios relizados en la insulina o cuando se pulsa el selector de la hora
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case (R.id.boton_aceptar_cambios_aplicacion):

                Bundle bundle = new Bundle();

                bundle.putString("tipo_aplicacion", this.spinner_tipo_aplicacion_edit.getSelectedItem().toString());
                bundle.putString("tipo_insulina", this.spinner_tipo_insulina_edit.getSelectedItem().toString());
                bundle.putInt("num_unidades", Integer.valueOf(this.num_unidades_edit.getText().toString()));
                bundle.putLong("fecha_aplicacion", this.fecha_aplicacion.getTimeInMillis());
                bundle.putInt("id_aplicacion",this.id);

                bundle.putInt("option",this.OPTION);

                bundle.putBoolean("clicked_edit",true);

                InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                Navigation.findNavController(v).navigate(R.id.nav_insulina, bundle);

                break;

            case (R.id.hora_aplicacion_edit):
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
                        fecha_aplicacion=c;

                        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
                        String  time = format.format(c.getTime());
                        hora_aplicacion_edit.setText(time);
                    }
                },hours, minute, false);
                timePickerDialog.show();
                break;
        }
    }

    //metodo que habilita y deshabilita el spinner del tipo de aplicación en base al tipo de insulina seleccionado
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String Lenta=getResources().getString(R.string.Lenta);
        String Intermedia=getResources().getString(R.string.Intermedia);

        if(parent.getSelectedItem().equals(Lenta)||parent.getSelectedItem().equals(Intermedia)){
            this.spinner_tipo_aplicacion_edit.setSelection(0);
            this.spinner_tipo_aplicacion_edit.setEnabled(false);

        }
        else{
            this.spinner_tipo_aplicacion_edit.setEnabled(true);
        }
    }

    //no es necesario establecer nada por defecto pues este fragmento se carga con información de antemano
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}