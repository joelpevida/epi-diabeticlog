package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.BaseDatos.DataBase;

public class ActividadFisicaManualRepository {

    private RegistroActividadFisicaManualDAO registroActividadFisicaManualDAO;
    private LiveData<List<RegistroActividadFisicaManual>> registros;

    //constructor
    public ActividadFisicaManualRepository(Application application, long inicio_dia, long fin_dia) {
        this.registroActividadFisicaManualDAO = DataBase.getDatabase(application).registroActividadFisicaManualDAO();
        this.registros = this.registroActividadFisicaManualDAO.getRegistrosManuales(inicio_dia, fin_dia);
    }

    //metodo para obtener los registros de actividad fisica manual de la base de datos con su contenedor livedata
    public LiveData<List<RegistroActividadFisicaManual>> getRegistros() {
        return this.registros;
    }

    //metodo para insertar un registro de actividad fisica manual en la bbdd
    public void insertRegistro(RegistroActividadFisicaManual registro) {
        new insertAsyncTask(registroActividadFisicaManualDAO).execute(registro);
    }

    //metodo para eliminar todos los  registros de actividad fisica manual de la bbdd
    public void deleteAll() {
        new deleteAsyncTask(registroActividadFisicaManualDAO).execute();
    }

    //metodo para modificar un registro de actividad fisica manual en la bdd
    public void modifyRegistro(String tipo_actividad_nuevo, String intensidad_nuevo, float kcal_quemadas_nuevo, long fecha_inicio_nuevo, long fecha_fin_nuevo, int id_registro) {
        WrapperRegistro wrapperRegistro=new WrapperRegistro(this.registroActividadFisicaManualDAO, tipo_actividad_nuevo, intensidad_nuevo, kcal_quemadas_nuevo, fecha_inicio_nuevo, fecha_fin_nuevo, id_registro);
        new modifyAsyncTask(wrapperRegistro).execute();
    }

    //metodo para eliminar un registro de actividad fisica manual de la bbdd
    public void removeRegistro(RegistroActividadFisicaManual seleccionada) {
        new deleteRegistroAsyncTask(registroActividadFisicaManualDAO).execute(seleccionada);
    }

    //asynktask usada para modificar un registro de actividad fisica manual en la bdd
    private static class modifyAsyncTask extends AsyncTask< WrapperRegistro, Void, Void> {

        private RegistroActividadFisicaManualDAO myAsyncTaskDAO;
        private String tipo_actividad_nuevo;
        private String intensidad_nuevo;
        private float kcal_quemadas_nuevo;
        private long fecha_inicio_nuevo;
        private long fecha_fin_nuevo;
        private int id_registro;

        public modifyAsyncTask(WrapperRegistro wrapperRegistro) {
            this.myAsyncTaskDAO =wrapperRegistro.getRegistroActividadFisicaManualDAO();
            this.tipo_actividad_nuevo=wrapperRegistro.getTipo_actividad_nuevo();
            this.intensidad_nuevo=wrapperRegistro.getIntensidad_nuevo();
            this.kcal_quemadas_nuevo=wrapperRegistro.kcal_quemadas_nuevo;
            this.fecha_inicio_nuevo=wrapperRegistro.fecha_inicio_nuevo;
            this.fecha_fin_nuevo=wrapperRegistro.fecha_fin_nuevo;
            this.id_registro=wrapperRegistro.id_registro;
        }


        @Override
        protected Void doInBackground(WrapperRegistro... wrapperRegistros) {
            myAsyncTaskDAO.modifyRegistro(this.tipo_actividad_nuevo, this.intensidad_nuevo, this.kcal_quemadas_nuevo, this.fecha_inicio_nuevo, this.fecha_fin_nuevo, this.id_registro);
            return null;
        }
    }

    //asynktask utilizada para eliminar un registro de actividad fisica manual de la bdd
    private static class deleteRegistroAsyncTask extends AsyncTask<RegistroActividadFisicaManual, Void, Void> {

        private RegistroActividadFisicaManualDAO myAsyncTaskDAO;

        public deleteRegistroAsyncTask(RegistroActividadFisicaManualDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        @Override
        protected Void doInBackground(RegistroActividadFisicaManual... registros) {
            myAsyncTaskDAO.deleteRegistro(registros[0].getId());
            return null;
        }
    }

    //asynktask empleada para insertar un un registro de actividad fisica manual en la bbdd
    private static class insertAsyncTask extends AsyncTask<RegistroActividadFisicaManual, Void, Void> {

        private RegistroActividadFisicaManualDAO myAsyncTaskDAO;

        public insertAsyncTask(RegistroActividadFisicaManualDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        @Override
        protected Void doInBackground(RegistroActividadFisicaManual... registros) {
            myAsyncTaskDAO.insertRegistroActividadFisicaManual(registros[0]);
            return null;
        }
    }

    //asybktask usada para eliminar todos los registros de actividad fisica manual de la bbdd
    private static class deleteAsyncTask extends AsyncTask<RegistroActividadFisicaManual, Void, Void> {

        private RegistroActividadFisicaManualDAO myAsyncTaskDAO;

        public deleteAsyncTask(RegistroActividadFisicaManualDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        @Override
        protected Void doInBackground(RegistroActividadFisicaManual... registros) {
            myAsyncTaskDAO.deleteRegistrosManuales();
            return null;
        }
    }

    //clase auxiliar usada por la asynktask para modificar un registro de actividad fisica manual
    private class WrapperRegistro{

        private RegistroActividadFisicaManualDAO registroActividadFisicaManualDAO;
        private String tipo_actividad_nuevo;
        private String intensidad_nuevo;
        private float kcal_quemadas_nuevo;
        private long fecha_inicio_nuevo;
        private long fecha_fin_nuevo;
        private int id_registro;

        //constructor de la clase auxiliar
        public WrapperRegistro(RegistroActividadFisicaManualDAO registroActividadFisicaManualDAO, String tipo_actividad_nuevo, String intensidad_nuevo, float kcal_quemadas_nuevo, long fecha_inicio_nuevo, long fecha_fin_nuevo, int id_registro) {
            this.registroActividadFisicaManualDAO = registroActividadFisicaManualDAO;
            this.tipo_actividad_nuevo = tipo_actividad_nuevo;
            this.intensidad_nuevo = intensidad_nuevo;
            this.kcal_quemadas_nuevo = kcal_quemadas_nuevo;
            this.fecha_inicio_nuevo = fecha_inicio_nuevo;
            this.fecha_fin_nuevo = fecha_fin_nuevo;
            this.id_registro = id_registro;
        }

        //getters de sus atributos
        public RegistroActividadFisicaManualDAO getRegistroActividadFisicaManualDAO() {
            return registroActividadFisicaManualDAO;
        }

        public String getTipo_actividad_nuevo() {
            return tipo_actividad_nuevo;
        }

        public String getIntensidad_nuevo() {
            return intensidad_nuevo;
        }

        public float getKcal_quemadas_nuevo() {
            return kcal_quemadas_nuevo;
        }

        public long getFecha_inicio_nuevo() {
            return fecha_inicio_nuevo;
        }

        public long getFecha_fin_nuevo() {
            return fecha_fin_nuevo;
        }

        public int getId_registro() {
            return id_registro;
        }

    }

}

