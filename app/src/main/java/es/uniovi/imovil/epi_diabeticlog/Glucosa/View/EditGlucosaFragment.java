package es.uniovi.imovil.epi_diabeticlog.Glucosa.View;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentEditGlucosaBinding;

public class EditGlucosaFragment extends Fragment implements View.OnClickListener{

    FragmentEditGlucosaBinding binding;

    private TextView horaMedicion_input_edit;
    private EditText mgDL_input_edit;
    private Button boton_editar_glucosa;

    private Calendar fechaMedicion_edit;
    int id;

    private Glucosa seleccionada;
    private static final int OPTION=2;

    public EditGlucosaFragment() {
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

        binding= FragmentEditGlucosaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_edit_glucosa));

        if(seleccionada!=null) {
            id = seleccionada.getId();

            mgDL_input_edit = binding.mgDLInputEdit;
            mgDL_input_edit.setText(String.valueOf(seleccionada.getMgDl()));

            //inicializamos el boton de aceptar los cambios
            boton_editar_glucosa = binding.botonEditarGlucosa;

            //definimos la fecha en la que se produjo la aplicacion cogiendola de la seleccionada
            fechaMedicion_edit = Calendar.getInstance();
            fechaMedicion_edit.setTimeInMillis(seleccionada.getFecha_medicion());

            //le damos un formato para mostrarla en la etiqueta correspondiente
            SimpleDateFormat format = new SimpleDateFormat("k:mm a");
            String time_inicio = format.format(fechaMedicion_edit.getTime());

            horaMedicion_input_edit = binding.horaMedicionInputEdit;
            horaMedicion_input_edit.setText(time_inicio);
            horaMedicion_input_edit.setOnClickListener(this);

            boton_editar_glucosa.setOnClickListener(this);
            horaMedicion_input_edit.setOnClickListener(this);

        }
        return view;
    }

    //metodo que se ejecuta al pulsar el boton para almacenar los cambios y al pulsar el selector para indicar la hora de la medicion de glucosa
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.boton_editar_glucosa):
                try{

                    Bundle bundle = new Bundle();

                    bundle.putFloat("mgDL", Float.valueOf(this.mgDL_input_edit.getText().toString()));
                    bundle.putLong("fechaMedicion", this.fechaMedicion_edit.getTimeInMillis());
                    bundle.putInt("id", this.id);
                    bundle.putInt("option", this.OPTION);
                    bundle.putBoolean("clicked_edit", true);

                    InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    Navigation.findNavController(v).navigate(R.id.nav_glucosa, bundle);

                }catch(Exception e){
                    Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorDataGlucosa), Toast.LENGTH_SHORT).show();
                }
                finally {
                    break;
                }

            case (R.id.horaMedicion_input_edit):
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
                        fechaMedicion_edit = c;
                        SimpleDateFormat format = new SimpleDateFormat("k:mm a");
                        String time_inicio = format.format(c.getTime());
                        horaMedicion_input_edit.setText(time_inicio);
                    }
                }, hours, minute, false);
                timePickerDialog.show();
                break;
        }
    }
}