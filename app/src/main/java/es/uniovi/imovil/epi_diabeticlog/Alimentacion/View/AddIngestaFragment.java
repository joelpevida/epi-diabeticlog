package es.uniovi.imovil.epi_diabeticlog.Alimentacion.View;

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

import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.Ingesta;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentAddIngestaBinding;

public class AddIngestaFragment extends Fragment implements View.OnClickListener{

    private FragmentAddIngestaBinding binding;

    Spinner spinner_tipos_ingesta;
    Button boton_agregar_ingesta;
    EditText num_raciones;
    TextView hora_ingesta;

    Calendar fecha_ingesta;

    private static final int OPTION=1;

    public AddIngestaFragment() {
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

        binding= FragmentAddIngestaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_add_alimentacion));

        spinner_tipos_ingesta=binding.spinnerTiposIngesta;
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.tipos_ingesta, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipos_ingesta.setAdapter(arrayAdapter);

        hora_ingesta= binding.horaIngesta;
        hora_ingesta.setOnClickListener(this);

        num_raciones=binding.numRaciones;
        boton_agregar_ingesta=binding.botonAgregarIngesta;

        boton_agregar_ingesta.setOnClickListener(this);

        return view;
    }

    //metodo onclick del selector de la hora de la ingesta y el boton de guardar la ingesta
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.boton_agregar_ingesta:
                try{
                    Ingesta created = new Ingesta(spinner_tipos_ingesta.getSelectedItem().toString(), Float.parseFloat(num_raciones.getText().toString()), fecha_ingesta.getTimeInMillis());

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("ingesta_creada", created);
                    bundle.putInt("option",this.OPTION);

                    bundle.putBoolean("clicked_add",true);
                    Navigation.findNavController(v).navigate(R.id.nav_alimentacion, bundle);

                    InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }catch(Exception e){
                    Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorDataIngesta), Toast.LENGTH_SHORT).show();
                }finally {
                    break;
                }

            case R.id.hora_ingesta:
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
                        fecha_ingesta=c;

                        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
                        String  time = format.format(c.getTime());
                        hora_ingesta.setText(time);
                    }
                },hours, minute, false);
                timePickerDialog.show();
                break;
        }
    }
}
