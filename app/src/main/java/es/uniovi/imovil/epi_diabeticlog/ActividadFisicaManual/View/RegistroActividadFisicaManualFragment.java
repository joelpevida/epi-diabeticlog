package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManual;
import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.ViewModel.ActividadFisicaViewModel;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentRegistroActividadFisicaManualBinding;

public class RegistroActividadFisicaManualFragment extends Fragment implements View.OnClickListener, CallbacksRegistroActividadFisicaManualActivity {

    private RegistroActividadFisicaManualRecyclerViewAdapter adapter;
    private ActividadFisicaViewModel viewModel;

    private FragmentRegistroActividadFisicaManualBinding binding;

    private FloatingActionButton boton_add_registro;

    private RegistroActividadFisicaManual registro_creado;

    private String tipo_actividad;
    private String intensidad;
    private float kcal_quemadas;
    private long fecha_inicio;
    private long fecha_fin;
    private int id_registro;

    private int option;
    private boolean returned=false;

    private static final String PREFERENCES_USER="USER_PREFERENCES";
    private static String UUID;

    public RegistroActividadFisicaManualFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getUUID();
        viewModel = new ViewModelProvider(this).get(ActividadFisicaViewModel.class);
        adapter = new RegistroActividadFisicaManualRecyclerViewAdapter(this.getActivity(), this);
        if (getArguments() != null) {
            this.option=getArguments().getInt("option");
            returned=true;
            if(option==1){
                boolean clicked=getArguments().getBoolean("clicked_add");
                if(clicked) {
                    registro_creado = getArguments().getParcelable("registro_creado");
                    registro_creado.setUuid(UUID);
                }

            }else if(getArguments().getInt("option")==2){
                boolean clicked=getArguments().getBoolean("clicked_edit");
                if(clicked) {
                    tipo_actividad = getArguments().getString("tipo_actividad");
                    intensidad = getArguments().getString("intensidad");
                    kcal_quemadas = getArguments().getFloat("kcal_quemadas");
                    fecha_inicio = getArguments().getLong("fecha_inicio");
                    fecha_fin = getArguments().getLong("fecha_fin");
                    id_registro = getArguments().getInt("id_registro");
                }
            }
        }
        else{
            returned=false;
        }
    }

    //metodo que obtiene el uuid del usuario
    private void getUUID() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE);
        UUID=prefs.getString("UUID_usuario", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding= FragmentRegistroActividadFisicaManualBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_registroactividadfisicamanual));

        RecyclerView recyclerView = (RecyclerView) binding.recyclerRegistroActFisManual;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        viewModel.getRegistros().observe(getViewLifecycleOwner(), new Observer<List<RegistroActividadFisicaManual>>() {
            @Override
            public void onChanged(@Nullable List<RegistroActividadFisicaManual> registros) {
                adapter.setRegistros(registros);
            }
        });

        onReturnToFragment();

        boton_add_registro = binding.botonAddRegistroActFisManual;
        boton_add_registro.setOnClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    boton_add_registro.hide();
                else if (dy < 0)
                    boton_add_registro.show();
            }
        });

        return view;
    }

    //metodo onclick del fab para
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.boton_add_registro_act_fis_manual) {
            Navigation.findNavController(v).navigate(R.id.addRegistroActividadFisicaManualFragment);
        }
    }

    //metodo que se ejecuta al volver al fragmento del listado, si se retorna de añadir un registro de actividad fisica manual entoinces se guarda
    //si se retorna de editar un registro de actividad fisica manual entonces se modifica este
    protected void onReturnToFragment() {
        if(returned) {
            if (getArguments().getInt("option") == 1) {
                boolean clicked=getArguments().getBoolean("clicked_add");
                if(clicked) {
                    try {
                        if (registro_creado!=null) {
                            this.viewModel.insert(this.registro_creado);
                            Toast.makeText(this.getActivity(), "Registrado: " + registro_creado.getTipo_actividad() + " Intensidad: " + registro_creado.getIntensidad() + " KCAL quemadas: " + String.valueOf(registro_creado.getKcal_quemadas()), Toast.LENGTH_LONG).show();
                            registro_creado=null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (getArguments().getInt("option") == 2) {
                boolean clicked=getArguments().getBoolean("clicked_edit");
                if(clicked) {
                    try {
                        this.viewModel.modifyRegistro(this.tipo_actividad, this.intensidad, this.kcal_quemadas, this.fecha_inicio, this.fecha_fin, this.id_registro);
                        Toast.makeText(this.getActivity(), getResources().getString(R.string.Registrada) +": " + tipo_actividad + getResources().getString(R.string.Intensidad) +": " + intensidad + getResources().getString(R.string.KcalQuemadas) +": " + String.valueOf(kcal_quemadas) , Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //metodo usado para eliminar un registro de actividad fisica manual de la bbdd
    @Override
    public void removeSelected(RegistroActividadFisicaManual seleccionada) {
        boton_add_registro.show();
        this.viewModel.removeRegistro(seleccionada);
        Toast.makeText(this.getActivity(), getResources().getString(R.string.Eliminada) +": " + seleccionada.getTipo_actividad() + getResources().getString(R.string.Intensidad) +": " + seleccionada.getIntensidad() + getResources().getString(R.string.KcalQuemadas) +": " + String.valueOf(seleccionada.getKcal_quemadas()) , Toast.LENGTH_LONG).show();
    }

    //metodo para modificar un registro de actividad fisica manual de la bbdd
    @Override
    public void modifySelected(RegistroActividadFisicaManual seleccionado) {
        Bundle bundle=new Bundle();
        bundle.putParcelable("seleccionada", seleccionado);
        Navigation.findNavController(this.getView()).navigate(R.id.editRegistroActividadFisicaManualFragment, bundle);

    }

}