package es.uniovi.imovil.epi_diabeticlog.Usuario.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.Usuario.Model.UsuarioRepository;
import es.uniovi.imovil.epi_diabeticlog.Usuario.Model.Usuario;


public class UsuarioViewModel extends AndroidViewModel {

    private UsuarioRepository usuarioRepository;
    private LiveData<List<Usuario>> usuarios;

    //constructor
    public UsuarioViewModel(@NonNull Application application){
        super(application);

        usuarioRepository=new UsuarioRepository(application);
        usuarios=usuarioRepository.getUsuarios();
    }

    //getter de los usuarios en su contenedor livedata
    public LiveData<List<Usuario>> getUsuarios() {
        return this.usuarios;
    }

    //metodo para introducir el usuario que se recibe como parametro en la bbdd
    public void insert(Usuario usuario) {
        usuarioRepository.insertUsuario(usuario);
    }

    //metodo para modificar un usuario de la bbdd
    public void modifyUsuario(String nombre_nuevo, int altura_cm_nuevo, float peso_nuevo, String pin_nuevo, String uuid_usuario) {
        usuarioRepository.modifyUsuario(nombre_nuevo, altura_cm_nuevo, peso_nuevo, pin_nuevo, uuid_usuario);
    }

}
