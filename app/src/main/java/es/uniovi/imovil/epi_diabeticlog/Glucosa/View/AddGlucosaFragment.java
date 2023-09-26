package es.uniovi.imovil.epi_diabeticlog.Glucosa.View;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentAddGlucosaBinding;


public class AddGlucosaFragment extends Fragment implements View.OnClickListener{

    private FragmentAddGlucosaBinding binding;

    private TextView horaMedicion_input;
    private EditText mgDL_input;
    private Button boton_agregar_glucosa;

    private Calendar fecha_medicion;

    private static final int OPTION=1;

    public AddGlucosaFragment() {
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
        binding= FragmentAddGlucosaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_add_glucosa));

        mgDL_input=binding.mgDLInput;
        boton_agregar_glucosa=binding.botonAgregarGlucosa;

        horaMedicion_input=binding.horaMedicionInput;
        horaMedicion_input.setOnClickListener(this);

        boton_agregar_glucosa.setOnClickListener(this);

        return view;
    }

    //metodo onclick que se ejecuta al pulsar el selector de la hora de la medición de la glucosa y al pulsar el boton para agregar la medicion de glucosa
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.horaMedicion_input:
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
                        fecha_medicion=c;

                        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
                        String  time = format.format(c.getTime());
                        horaMedicion_input.setText(time);
                    }
                },hours, minute, false);
                timePickerDialog.show();
                break;

            case  R.id.boton_agregar_glucosa:
                try {

                    Glucosa creada = new Glucosa();

                    creada.setMgDl(Float.valueOf(this.mgDL_input.getText().toString()));
                    creada.setFecha_medicion(fecha_medicion.getTimeInMillis());

                    Calendar inicio=Calendar.getInstance();
                    inicio.setTimeInMillis(creada.getFecha_medicion());

                    Bundle bundle=new Bundle();
                    bundle.putInt("option",this.OPTION);
                    bundle.putParcelable("glucosa_creada",creada);
                    bundle.putBoolean("clicked_add",true);
                    Navigation.findNavController(v).navigate(R.id.nav_glucosa, bundle);

                    InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }catch(Exception e){
                    Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorDataGlucosa), Toast.LENGTH_SHORT).show();
                }
                finally {
                    break;
                }
        }
    }
}