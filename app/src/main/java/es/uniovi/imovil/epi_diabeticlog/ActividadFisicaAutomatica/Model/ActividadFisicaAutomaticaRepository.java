package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.Model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.BaseDatos.DataBase;

public class ActividadFisicaAutomaticaRepository {

    private RegistroActividadFisicaAutomaticaDAO registroActividadFisicaAutomaticaDAO;
    private LiveData<List<RegistroActividadFisicaAutomatica>> registros;

    //constructor
    public ActividadFisicaAutomaticaRepository(Application application, long inicio_dia, long fin_dia) {
        this.registroActividadFisicaAutomaticaDAO = DataBase.getDatabase(application).registroActividadFisicaAutomaticaDAO();
        this.registros = this.registroActividadFisicaAutomaticaDAO.getRegistroAutomatico(inicio_dia, fin_dia);
    }

    //metodo para retornar los registros en su contenedor livedata
    public LiveData<List<RegistroActividadFisicaAutomatica>> getRegistros() {
        return this.registros;
    }

    //insertar registro de actividad fisica automática en la base de datos
    public void insertRegistro(RegistroActividadFisicaAutomatica registro) {
        new ActividadFisicaAutomaticaRepository.insertAsyncTask(registroActividadFisicaAutomaticaDAO).execute(registro);
    }

    //eliminacion de todos los registro de actividad fisica automática en la base de datos
    public void deleteAll() {
        new ActividadFisicaAutomaticaRepository.deleteAsyncTask(registroActividadFisicaAutomaticaDAO).execute();
    }

    //asynktask utilizada para insertar un elemento en la tabla de los registros de actividad física automática
    private static class insertAsyncTask extends AsyncTask<RegistroActividadFisicaAutomatica, Void, Void> {


        private RegistroActividadFisicaAutomaticaDAO myAsyncTaskDAO;


        public insertAsyncTask(RegistroActividadFisicaAutomaticaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }


        @Override
        protected Void doInBackground(RegistroActividadFisicaAutomatica... registros) {
            myAsyncTaskDAO.insertRegistroActividadFisicaAutomatica(registros[0]);
            return null;
        }
    }

    //asynktask utilizada para eliminar todos lo elementos de la tabla de los registros de actividad física automática
    private static class deleteAsyncTask extends AsyncTask<RegistroActividadFisicaAutomatica, Void, Void> {


        private RegistroActividadFisicaAutomaticaDAO myAsyncTaskDAO;


        public deleteAsyncTask(RegistroActividadFisicaAutomaticaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }


        @Override
        protected Void doInBackground(RegistroActividadFisicaAutomatica... registros) {
            myAsyncTaskDAO.deleteRegistrosAutomaticas();
            return null;
        }
    }


}
