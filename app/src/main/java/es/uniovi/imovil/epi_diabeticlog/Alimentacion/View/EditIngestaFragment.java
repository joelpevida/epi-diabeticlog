package es.uniovi.imovil.epi_diabeticlog.Alimentacion.View;

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
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.Ingesta;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentEditIngestaBinding;

public class EditIngestaFragment extends Fragment implements View.OnClickListener{

    private FragmentEditIngestaBinding binding;

    private EditText etiqueta_nombre_ingesta_edit;
    private EditText etiqueta_num_raciones_ingesta_edit;
    private Button boton_guardar_cambios_ingesta_edit;

    private TextView etiqueta_hora_ingesta_edit;
    private String nombre_viejo;
    float num_raciones_viejo;

    private Calendar fecha_ingesta;

    int id;
    private Ingesta seleccionada;

    private static final int OPTION=2;

    public EditIngestaFragment() {
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
        binding = FragmentEditIngestaBinding.inflate(getLayoutInflater());

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_edit_alimentacion));

        if(this.seleccionada!=null) {
            this.id = seleccionada.getId();

            this.etiqueta_nombre_ingesta_edit=binding.etiquetaNombreIngestaEdit;
            etiqueta_num_raciones_ingesta_edit = binding.etiquetaNumRacionesIngestaEdit;
            boton_guardar_cambios_ingesta_edit = binding.botonGuardarCambiosIngesta;
            etiqueta_hora_ingesta_edit = binding.etiquetaHoraIngestaEdit;

            nombre_viejo = seleccionada.getTipo_ingesta();
            num_raciones_viejo = seleccionada.getNum_raciones();

            this.etiqueta_nombre_ingesta_edit.setText(this.nombre_viejo);
            etiqueta_num_raciones_ingesta_edit.setText(String.valueOf(this.num_raciones_viejo));

            fecha_ingesta = Calendar.getInstance();
            fecha_ingesta.setTimeInMillis(seleccionada.getFecha());

            SimpleDateFormat format = new SimpleDateFormat("k:mm a");
            String time = format.format(fecha_ingesta.getTime());
            etiqueta_hora_ingesta_edit.setText(time);

            this.invalidarCambioNombre(seleccionada.getTipo_ingesta());

            etiqueta_hora_ingesta_edit.setOnClickListener(this);
            boton_guardar_cambios_ingesta_edit.setOnClickListener(this);
        }
        return binding.getRoot();
    }

    //metodo que no permite modificar el nombre de la ingesta si es una principal
    private void invalidarCambioNombre(String nombre) {

        if(nombre.toUpperCase().equals(getResources().getString(R.string.Desayuno).toUpperCase()) ||
                nombre.toUpperCase().equals(getResources().getString(R.string.Media_manana).toUpperCase()) ||
                nombre.toUpperCase().equals(getResources().getString(R.string.Comida).toUpperCase()) ||
                nombre.toUpperCase().equals(getResources().getString(R.string.Merienda).toUpperCase()) ||
                nombre.toUpperCase().equals(getResources().getString(R.string.Cena).toUpperCase())){
            this.etiqueta_nombre_ingesta_edit.setEnabled(false);
        }
    }

    //metodo onclick para el selector de la hora de la ingesta y del boton de guardar
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.boton_guardar_cambios_ingesta):
                try {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id_ingesta", this.id);
                    bundle.putString("nombre_viejo", this.nombre_viejo);
                    bundle.putString("nombre_nuevo", this.etiqueta_nombre_ingesta_edit.getText().toString());
                    bundle.putFloat("num_raciones_nuevo", Float.parseFloat(this.etiqueta_num_raciones_ingesta_edit.getText().toString()));
                    bundle.putLong("fecha_ingesta", fecha_ingesta.getTimeInMillis());
                    bundle.putInt("option", this.OPTION);

                    bundle.putBoolean("clicked_edit", true);

                    InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    Navigation.findNavController(v).navigate(R.id.nav_alimentacion, bundle);
                }
                catch(Exception e){
                    Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorDataIngesta), Toast.LENGTH_SHORT).show();
                }
                finally {
                    break;
                }

            case (R.id.etiqueta_hora_ingesta_edit):
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
                        etiqueta_hora_ingesta_edit.setText(time);
                    }
                },hours, minute, false);
                timePickerDialog.show();
                break;
        }
    }
}