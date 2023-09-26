package es.uniovi.imovil.epi_diabeticlog.Usuario.Model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.BaseDatos.DataBase;

public class UsuarioRepository {


    private UsuarioDAO usuarioDAO;
    private LiveData<List<Usuario>> usuarios;

    //constructor
    public UsuarioRepository(Application application) {
        this.usuarioDAO = DataBase.getDatabase(application).usuarioDAO();
        this.usuarios = this.usuarioDAO.getUsuarios();
    }

    //getter de los usuarios en su contenedor livedata
    public LiveData<List<Usuario>> getUsuarios() {
        return this.usuarios;
    }

    //metodo para insertar un usuario en la bbdd
    public void insertUsuario(Usuario usuario) {
        new insertAsyncTask(usuarioDAO).execute(usuario);
    }

    //metodo para eliminar un usuario de la bbdd
    public void deleteUsuario() {
        new deleteAsyncTask(usuarioDAO).execute();
    }

    //metodo para modificar un usario de la bbdd
    public void modifyUsuario(String nombre_nuevo, int altura_cm_nuevo, float peso_nuevo, String pin_nuevo, String uuid_usuario) {
        WrapperUsuario wrapperUsuario=new WrapperUsuario(this.usuarioDAO, uuid_usuario, nombre_nuevo, altura_cm_nuevo, peso_nuevo, pin_nuevo);
        new modifyAsyncTask(wrapperUsuario).execute();
    }

    //asynktask para modificar un usuario de la bbdd
    private static class modifyAsyncTask extends AsyncTask<WrapperUsuario, Void, Void> {

        private UsuarioDAO myAsyncTaskDAO;
        private String uuid;
        private String nombre;
        private int altura_cm;
        private float peso;
        private String pin;

        //constructor
        public modifyAsyncTask(WrapperUsuario wrapperUsuario) {
            this.myAsyncTaskDAO =wrapperUsuario.getUsuarioDAO();
            this.uuid=wrapperUsuario.getUuid();
            this.nombre=wrapperUsuario.getNombre();
            this.altura_cm=wrapperUsuario.getAltura_cm();
            this.peso=wrapperUsuario.getPeso();
            this.pin=wrapperUsuario.getPin();
        }

        //metodo que modifica un usuario de la bbdd
        @Override
        protected Void doInBackground(WrapperUsuario... wrapperUsuarios) {
            myAsyncTaskDAO.modifyUsuario(this.nombre, this.altura_cm, this.peso, this.pin, this.uuid);
            return null;
        }
    }

    //clase auxiliar usada por la modifyasynktask
    private class WrapperUsuario{

        private UsuarioDAO usuarioDAO;
        private String uuid;
        private String nombre;
        private int altura_cm;
        private float peso;
        private String pin;

        public WrapperUsuario(UsuarioDAO usuarioDAO, String uuid, String nombre, int altura_cm, float peso, String pin) {
            this.usuarioDAO = usuarioDAO;
            this.uuid = uuid;
            this.nombre = nombre;
            this.altura_cm = altura_cm;
            this.peso = peso;
            this.pin = pin;
        }

        public UsuarioDAO getUsuarioDAO() {
            return usuarioDAO;
        }

        public String getUuid() {
            return uuid;
        }

        public String getNombre() {
            return nombre;
        }

        public int getAltura_cm() {
            return altura_cm;
        }

        public float getPeso() {
            return peso;
        }

        public String getPin() {
            return pin;
        }
    }

    //asynktask para eliminar todos los usuarios de la bbdd
    private static class deleteAsyncTask extends AsyncTask<Usuario, Void, Void> {

        private UsuarioDAO myAsyncTaskDAO;

        //constructor
        public deleteAsyncTask(UsuarioDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        //metodo que elimina todos los usuarios de la bbdd
        @Override
        protected Void doInBackground(Usuario... usuarios) {
            myAsyncTaskDAO.deleteUsuarios();
            return null;
        }
    }

    //asynktask para introducir un usuario en la bbdd
    private static class insertAsyncTask extends AsyncTask<Usuario, Void, Void> {

        private UsuarioDAO myAsyncTaskDAO;

        //constructor
        public insertAsyncTask(UsuarioDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        //metodo que introduce un usuario en la bbdd
        @Override
        protected Void doInBackground(Usuario... usuarios) {
            myAsyncTaskDAO.insertUsuario(usuarios[0]);
            return null;
        }
    }

}
