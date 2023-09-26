package es.uniovi.imovil.epi_diabeticlog.Usuario.View;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.Usuario.Model.Usuario;
import es.uniovi.imovil.epi_diabeticlog.Usuario.ViewModel.UsuarioViewModel;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment implements View.OnClickListener{

    private FragmentPerfilBinding binding;

    private static final String PREFERENCES_USER="USER_PREFERENCES";
    private static String UUID;

    private UsuarioViewModel viewModel;
    private Usuario usuario;

    private EditText nombre_text;
    private EditText peso_text;
    private EditText altura_text;
    private Button boton_guardar;

    private Communication_PerfilFragment_MainActivity communication_perfilFragment_mainActivity;


    public PerfilFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding= FragmentPerfilBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_perfil));


        nombre_text=binding.nombreUsuarioEdit;
        peso_text=binding.pesoUsuarioEdit;
        altura_text=binding.alturaUsuarioEdit;
        boton_guardar=binding.botonGuardarCambios;
        boton_guardar.setOnClickListener(this);

        viewModel.getUsuarios().observe(getViewLifecycleOwner(), new Observer<List<Usuario>>() {
            @Override
            public void onChanged(@Nullable List<Usuario> usuarios) {
                establecerLayout(usuarios.get(0));
            }
        });

        return view;

    }

    //metodo que inicializa el layout del fragmento con los valores del usuario que se recibe como parametro
    private void establecerLayout(Usuario usuario) {

        this.usuario=usuario;
        nombre_text.setText(usuario.getNombre());
        peso_text.setText(String.valueOf(usuario.getPeso()));
        altura_text.setText(String.valueOf(usuario.getAltura_cm()));

    }

    //metodo con click que se ejecuta al pulsar el boton para guardar los cambios
    @Override
    public void onClick(View v) {

        try {
            int altura_nueva = Integer.valueOf(altura_text.getText().toString());
            float peso_nuevo = Float.valueOf(peso_text.getText().toString());
            String nombre_nuevo = nombre_text.getText().toString();
            if(altura_nueva!=usuario.getAltura_cm()){
                if(altura_nueva==0){
                    throw new Exception("Data missing");
                }
            }
            if(peso_nuevo!=usuario.getPeso()){
                if(peso_nuevo==0){
                    throw new Exception("Data missing");
                }
            }
            if(nombre_nuevo.isEmpty()){
                throw new Exception("Data missing");
            }
            viewModel.modifyUsuario(nombre_nuevo, altura_nueva, peso_nuevo, usuario.getPin(), usuario.getUuid());
            communication_perfilFragment_mainActivity.setNombreNavegacion(nombre_nuevo);
            Toast.makeText(this.getActivity(), getResources().getString(R.string.DatosGuardados), Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(this.getActivity(), getResources().getString(R.string.ProblemaGuardadoDatos), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (activity != null) {
            communication_perfilFragment_mainActivity = (Communication_PerfilFragment_MainActivity) activity;
        }
        else {
            throw new ClassCastException(activity.toString() +
                    "Error, no se implementa el callback");
        }
    }
}