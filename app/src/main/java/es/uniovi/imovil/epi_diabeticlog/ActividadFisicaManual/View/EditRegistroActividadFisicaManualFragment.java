package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.View;

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
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentEditRegistroActividadFisicaManualBinding;

public class EditRegistroActividadFisicaManualFragment extends Fragment implements View.OnClickListener{

    private FragmentEditRegistroActividadFisicaManualBinding binding;

    private Spinner spinner_tipo_actividad_edit;
    private Spinner spinner_intensidad_edit;
    private TextView hora_inicio_edit;
    private TextView hora_final_edit;
    private EditText kcal_quemadas_input_edit;
    private Button boton_editar_registro_actualizar;

    private Calendar fecha_inicio_edit;
    private Calendar fecha_fin_edit;
    int id;

    private RegistroActividadFisicaManual seleccionada;
    private static final int OPTION=2;

    public EditRegistroActividadFisicaManualFragment() {
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

        binding= FragmentEditRegistroActividadFisicaManualBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_edit_registroactividadfisicamanual));

        String Alta=getResources().getString(R.string.Alta);
        String Media=getResources().getString(R.string.Media);
        String Baja=getResources().getString(R.string.Baja);

        String Correr=getResources().getString(R.string.Correr);
        String Caminar=getResources().getString(R.string.Caminar);
        String Nadar=getResources().getString(R.string.Nadar);
        String Futbol=getResources().getString(R.string.Futbol);
        String Baloncesto=getResources().getString(R.string.Baloncesto);
        String Gimnsasio=getResources().getString(R.string.Gimnsasio);
        String Hockey=getResources().getString(R.string.Hockey);
        String Tenis=getResources().getString(R.string.Tenis);
        String Padel=getResources().getString(R.string.Padel);
        String Pingpong=getResources().getString(R.string.Pingpong);
        String Otra=getResources().getString(R.string.Otra);

        if(seleccionada!=null) {
            id = seleccionada.getId();

            spinner_intensidad_edit = binding.spinnerIntensidadEdit;
            ArrayAdapter<CharSequence> arrayAdapter_intensidad = ArrayAdapter.createFromResource(this.getActivity(), R.array.tipos_intensidad, android.R.layout.simple_spinner_item);
            arrayAdapter_intensidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_intensidad_edit.setAdapter(arrayAdapter_intensidad);

            if(seleccionada.getIntensidad().equals(Alta)){
                spinner_intensidad_edit.setSelection(0);
            }
            else {
                if(seleccionada.getIntensidad().equals(Media)){
                    spinner_intensidad_edit.setSelection(1);
                }
                else {
                    if(seleccionada.getIntensidad().equals(Baja)){
                        spinner_intensidad_edit.setSelection(2);
                    }
                }
            }

            //primero seleccionamos el tipo de insulina aplicada
            spinner_tipo_actividad_edit = binding.spinnerTipoActividadEdit;
            ArrayAdapter<CharSequence> arrayAdapter_tipo_actividad = ArrayAdapter.createFromResource(this.getActivity(), R.array.tipos_actividad, android.R.layout.simple_spinner_item);
            arrayAdapter_tipo_actividad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_tipo_actividad_edit.setAdapter(arrayAdapter_tipo_actividad);

            if(seleccionada.getTipo_actividad().equals(Correr)){
                this.spinner_tipo_actividad_edit.setSelection(0);
            }

            else{
                if(seleccionada.getTipo_actividad().equals(Caminar)){
                    this.spinner_tipo_actividad_edit.setSelection(1);
                }

                else{
                    if(seleccionada.getTipo_actividad().equals(Nadar)){
                        this.spinner_tipo_actividad_edit.setSelection(2);
                    }

                    else{
                        if(seleccionada.getTipo_actividad().equals(Futbol)){
                            this.spinner_tipo_actividad_edit.setSelection(3);
                        }

                        else{
                            if(seleccionada.getTipo_actividad().equals(Baloncesto)){
                                this.spinner_tipo_actividad_edit.setSelection(4);
                            }

                            else{
                                if(seleccionada.getTipo_actividad().equals(Gimnsasio)){
                                    this.spinner_tipo_actividad_edit.setSelection(5);
                                }

                                else{
                                    if(seleccionada.getTipo_actividad().equals(Hockey)){
                                        this.spinner_tipo_actividad_edit.setSelection(6);
                                    }

                                    else{
                                        if(seleccionada.getTipo_actividad().equals(Tenis)){
                                            this.spinner_tipo_actividad_edit.setSelection(7);
                                        }

                                        else{
                                            if(seleccionada.getTipo_actividad().equals(Padel)){
                                                this.spinner_tipo_actividad_edit.setSelection(8);
                                            }

                                            else{
                                                if(seleccionada.getTipo_actividad().equals(Pingpong)){
                                                    this.spinner_tipo_actividad_edit.setSelection(9);
                                                }

                                                else{
                                                    if(seleccionada.getTipo_actividad().equals(Otra)){
                                                        this.spinner_tipo_actividad_edit.setSelection(10);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //ahora el numero de unidades aplicadas
            kcal_quemadas_input_edit = binding.kcalQuemadasInputEdit;
            kcal_quemadas_input_edit.setText(String.valueOf(seleccionada.getKcal_quemadas()));

            //inicializamos el boton de aceptar los cambios
            boton_editar_registro_actualizar = binding.botonEditarRegistroActualizar;

            //definimos la fecha en la que se produjo la aplicacion cogiendola de la seleccionada
            fecha_inicio_edit = Calendar.getInstance();
            fecha_inicio_edit.setTimeInMillis(seleccionada.getFecha_inicio());

            //definimos las fechzs
            fecha_fin_edit = Calendar.getInstance();
            fecha_fin_edit.setTimeInMillis(seleccionada.getFecha_fin());

            //le damos un formato para mostrarla en la etiqueta correspondiente
            SimpleDateFormat format = new SimpleDateFormat("k:mm a");
            String time_inicio = format.format(fecha_inicio_edit.getTime());
            String time_fin = format.format(fecha_fin_edit.getTime());

            hora_inicio_edit = binding.horaInicioEdit;
            hora_inicio_edit.setText(time_inicio);
            hora_inicio_edit.setOnClickListener(this);

            hora_final_edit = binding.horaFinalEdit;
            hora_final_edit.setText(time_fin);
            hora_final_edit.setOnClickListener(this);

            boton_editar_registro_actualizar.setOnClickListener(this);
            hora_inicio_edit.setOnClickListener(this);
            hora_final_edit.setOnClickListener(this);


        }
        return view;
    }

    //metodo on click establecido para los selectors de las horas de inicio y fin y el boton de guardado
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.boton_editar_registro_actualizar):
                try{
                    if(this.fecha_inicio_edit.before(fecha_fin_edit)) {
                        Bundle bundle = new Bundle();

                        bundle.putString("tipo_actividad", this.spinner_tipo_actividad_edit.getSelectedItem().toString());
                        bundle.putString("intensidad", this.spinner_intensidad_edit.getSelectedItem().toString());
                        bundle.putFloat("kcal_quemadas", Float.valueOf(this.kcal_quemadas_input_edit.getText().toString()));
                        bundle.putLong("fecha_inicio", this.fecha_inicio_edit.getTimeInMillis());
                        bundle.putLong("fecha_fin", this.fecha_fin_edit.getTimeInMillis());
                        bundle.putInt("id_registro", this.id);

                        bundle.putInt("option",this.OPTION);

                        bundle.putBoolean("clicked_edit",true);

                        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        Navigation.findNavController(v).navigate(R.id.nav_registroactividadfisicamanual, bundle);
                    }
                    else{
                        Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorHorasRegistro), Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorDataRegistro), Toast.LENGTH_SHORT).show();
                }
                finally {
                    break;
                }


            case (R.id.hora_inicio_edit):
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
                        fecha_inicio_edit = c;
                        SimpleDateFormat format = new SimpleDateFormat("k:mm a");
                        String time_inicio = format.format(c.getTime());
                        hora_inicio_edit.setText(time_inicio);
                    }
                }, hours, minute, false);
                timePickerDialog.show();
                break;

            case (R.id.hora_final_edit):
                Calendar calendar2 = Calendar.getInstance();
                int hours2 = calendar2.get(Calendar.HOUR_OF_DAY);
                int minute2 = calendar2.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.setTimeZone(TimeZone.getDefault());
                        fecha_fin_edit = c;
                        SimpleDateFormat format = new SimpleDateFormat("k:mm a");
                        String time_fin = format.format(c.getTime());
                        hora_final_edit.setText(time_fin);
                    }
                }, hours2, minute2, false);
                timePickerDialog2.show();
                break;
        }
    }
}