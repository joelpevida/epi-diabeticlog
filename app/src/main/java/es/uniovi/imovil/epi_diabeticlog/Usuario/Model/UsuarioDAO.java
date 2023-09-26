package es.uniovi.imovil.epi_diabeticlog.Usuario.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.Usuario.Model.Usuario;

@Dao
public interface UsuarioDAO {

    //metodo para insertar un usuario en la bbdd
    @Insert
    public void insertUsuario(Usuario usuario);

    //metodo para eliminar todos los usuarios de la bbdd
    @Query("DELETE from usuario_tabla")
    public void deleteUsuarios();

    //metodo para obtener todos los usuarios de la bbdd
    @Query("SELECT * FROM usuario_tabla")
    public LiveData<List<Usuario>> getUsuarios();

    //metodo para modificar un usuario de la bbdd
    @Query("UPDATE usuario_tabla SET nombre=:nombre_nuevo,  altura_cm=:altura_cm_nuevo, peso=:peso_nuevo , pin=:pin_nuevo WHERE uuid=:uuid_usuario")
    void modifyUsuario(String nombre_nuevo, int altura_cm_nuevo, float peso_nuevo, String pin_nuevo, String uuid_usuario);
}
