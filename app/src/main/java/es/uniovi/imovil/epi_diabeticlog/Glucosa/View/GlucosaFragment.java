package es.uniovi.imovil.epi_diabeticlog.Glucosa.View;

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

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;
import es.uniovi.imovil.epi_diabeticlog.Glucosa.ViewModel.GlucosaViewModel;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentGlucosaBinding;

public class GlucosaFragment extends Fragment implements View.OnClickListener, CallbacksGlucosaActivity {

    private FragmentGlucosaBinding binding;

    private GlucosaRecyclerViewAdapter adapter;
    private GlucosaViewModel viewModel;

    private FloatingActionButton botonAddGlucosa;

    private Glucosa glucosaCreada;

    private float mgDL;
    private long fechaMedicion;
    private int id;

    private int option;
    private boolean returned=false;

    private static final String PREFERENCES_USER="USER_PREFERENCES";
    private static String UUID;

    public GlucosaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getUUID();
        viewModel = new ViewModelProvider(this).get(GlucosaViewModel.class);
        adapter = new GlucosaRecyclerViewAdapter(this.getActivity(), this);
        if (getArguments() != null) {
            this.option=getArguments().getInt("option");
            returned=true;
            if(option==1){
                boolean clicked=getArguments().getBoolean("clicked_add");
                if(clicked) {
                    glucosaCreada = getArguments().getParcelable("glucosa_creada");
                    glucosaCreada.setUuid(UUID);
                }
            }else if(getArguments().getInt("option")==2){
                boolean clicked_edit=getArguments().getBoolean("clicked_edit");
                if(clicked_edit) {
                    this.mgDL = getArguments().getFloat("mgDL");
                    this.fechaMedicion = getArguments().getLong("fechaMedicion");
                    this.id = getArguments().getInt("id");

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

        binding= FragmentGlucosaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_glucosa));

        RecyclerView recyclerView = (RecyclerView) binding.recyclerGlucosa;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        viewModel.getGlucosas().observe(getViewLifecycleOwner(), new Observer<List<Glucosa>>() {
            @Override
            public void onChanged(@Nullable List<Glucosa> glucosas) {

                adapter.setGlucosas(glucosas);
            }
        });

        onReturnToFragment();

        botonAddGlucosa = binding.botonAddGlucosa;
        botonAddGlucosa.setOnClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    botonAddGlucosa.hide();
                else if (dy < 0)
                    botonAddGlucosa.show();
            }
        });

        return view;
    }

    //metodo on click que se ejecuta al pulsar el fab
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.boton_add_glucosa) {
            Navigation.findNavController(v).navigate(R.id.addGlucosaFragment);
        }
    }

    //metodo que se ejecuta al lanzar el fragmento de fomra que si se viene de añadir una actividad esta se guarda en la bbdd y si se viene de modificar una actividad se modifica esta en la bbdd
    protected void onReturnToFragment() {
        if(returned) {
            if (getArguments().getInt("option") == 1) {
                boolean clicked=getArguments().getBoolean("clicked_add");
                if(clicked) {
                    try {
                        if(glucosaCreada!=null) {
                            this.viewModel.insert(this.glucosaCreada);
                            Toast.makeText(this.getActivity(), getResources().getString(R.string.MedicionGlucosaCreada) +": " + glucosaCreada.getMgDl() + " mg/dl", Toast.LENGTH_LONG).show();
                            glucosaCreada = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //codigo que se ejecuta cuando la clase editIngestaActivity termina y retorna los nuevos valores(falta definir esto)
            if (getArguments().getInt("option") == 2) {
                boolean clicked=getArguments().getBoolean("clicked_edit");
                if(clicked) {
                    try {
                        this.viewModel.modifyGlucosa(this.mgDL, this.fechaMedicion, this.id);
                        Toast.makeText(this.getActivity(), getResources().getString(R.string.MedicionGlucosaCreada) +": " + this.mgDL + " mg/dl" , Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //metodo para eliminar la glucosa seleccionada de la bbdd
    @Override
    public void removeSelected(Glucosa seleccionada) {
        botonAddGlucosa.show();
        this.viewModel.removeGlucosa(seleccionada);
        Toast.makeText(this.getActivity(), getResources().getString(R.string.Eliminada)+" "+getResources().getString(R.string.menu_glucosa).toLowerCase() +": " + seleccionada.getMgDl() +"mg/dl", Toast.LENGTH_LONG).show();
    }

    //metodo para modificar la glucosa seleccionada de la bbdd
    @Override
    public void modifySelected(Glucosa seleccionado) {
        Bundle bundle=new Bundle();
        bundle.putParcelable("seleccionada", seleccionado);
        Navigation.findNavController(this.getView()).navigate(R.id.editGlucosaFragment, bundle);

    }

}