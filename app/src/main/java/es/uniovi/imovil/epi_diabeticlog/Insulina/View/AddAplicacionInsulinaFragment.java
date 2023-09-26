package es.uniovi.imovil.epi_diabeticlog.Insulina.View;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentAddAplicacionInsulinaBinding;

public class AddAplicacionInsulinaFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private FragmentAddAplicacionInsulinaBinding binding;

    Spinner spinner_tipo_aplicacion;
    Spinner spinner_tipo_insulina;
    Button boton_agregar_aplicacion;
    EditText num_unidades;
    TextView hora_aplicacion;

    Calendar fecha_aplicacion;

    private static final int OPTION=1;

    public AddAplicacionInsulinaFragment() {
        // Required empty public constructor
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

        binding= FragmentAddAplicacionInsulinaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_add_insulina));

        spinner_tipo_aplicacion = binding.spinnerTipoAplicacion;
        ArrayAdapter<CharSequence> arrayAdapter_tipo_aplicacion = ArrayAdapter.createFromResource(this.getActivity(), R.array.tipos_aplicacion, android.R.layout.simple_spinner_item);
        arrayAdapter_tipo_aplicacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_aplicacion.setAdapter(arrayAdapter_tipo_aplicacion);

        spinner_tipo_insulina = binding.spinnerTipoInsulina;
        ArrayAdapter<CharSequence> arrayAdapter_tipo_insulina = ArrayAdapter.createFromResource(this.getActivity(), R.array.tipos_insulina, android.R.layout.simple_spinner_item);
        arrayAdapter_tipo_insulina.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_insulina.setAdapter(arrayAdapter_tipo_insulina);

        num_unidades = binding.numUnidadesItem;
        boton_agregar_aplicacion = binding.botonAgregarAplicacion;

        hora_aplicacion = binding.horaAplicacion;
        hora_aplicacion.setOnClickListener(this);

        boton_agregar_aplicacion.setOnClickListener(this);

        spinner_tipo_insulina.setOnItemSelectedListener(this);

        return view;
    }

    //metodo que se ejecuta al pulsar el boton para registrar la insulina o cuando se pulsa el selector de la hora
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hora_aplicacion:
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.setTimeZone(TimeZone.getDefault());
                        fecha_aplicacion = c;

                        SimpleDateFormat format = new SimpleDateFormat("k:mm a");
                        String time = format.format(c.getTime());
                        hora_aplicacion.setText(time);
                    }
                }, hours, minute, false);
                timePickerDialog.show();
                break;

            case R.id.boton_agregar_aplicacion:
                try {

                    Insulina creada = new Insulina();

                    creada.setTipo_aplicacion(this.spinner_tipo_aplicacion.getSelectedItem().toString());
                    creada.setTipo_insulina(this.spinner_tipo_insulina.getSelectedItem().toString());
                    creada.setNum_unidades(Integer.valueOf(this.num_unidades.getText().toString()));
                    creada.setFecha_aplicacion(fecha_aplicacion.getTimeInMillis());

                    Bundle bundle=new Bundle();
                    bundle.putInt("option",this.OPTION);
                    bundle.putParcelable("insulina_creada",creada);
                    bundle.putBoolean("clicked_add",true);
                    Navigation.findNavController(v).navigate(R.id.nav_insulina, bundle);

                    InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                } catch (Exception e) {
                    Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorDataInsulina), Toast.LENGTH_SHORT).show();
                } finally {
                    break;
                }
        }
    }

    //metodo que habilita y deshabilita el spinner del tipo de aplicación en base al tipo de insulina seleccionado
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String Lenta=getResources().getString(R.string.Lenta);
        String Intermedia=getResources().getString(R.string.Intermedia);

        if (parent.getSelectedItem().equals(Lenta) || parent.getSelectedItem().equals(Intermedia)) {
            this.spinner_tipo_aplicacion.setSelection(0);
            this.spinner_tipo_aplicacion.setEnabled(false);

        } else {
            this.spinner_tipo_aplicacion.setEnabled(true);
        }
    }

    //se establece el valor por defecto de spinner del tipo de aplicacion
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        this.spinner_tipo_aplicacion.setEnabled(false);
    }
}