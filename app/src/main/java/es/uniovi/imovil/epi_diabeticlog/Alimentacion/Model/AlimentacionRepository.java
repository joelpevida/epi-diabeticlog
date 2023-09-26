package es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.BaseDatos.DataBase;

public class AlimentacionRepository {

    private IngestaDAO ingestaDAO;
    private LiveData<List<Ingesta>> ingestas;
    private static long  inicio_dia;
    private static long fin_dia;

    //constructor
    public AlimentacionRepository(Application application, long inicio_dia, long fin_dia) {
        this.inicio_dia=inicio_dia;
        this.fin_dia=fin_dia;
        this.ingestaDAO = DataBase.getDatabase(application).ingestaDAO();
        this.ingestas = this.ingestaDAO.getIngestas(this.inicio_dia, this.fin_dia);
    }

    //metodo que retorna las ingestas con su contenedor livedata
    public LiveData<List<Ingesta>> getIngestas() {
        return this.ingestas;
    }

    //metodo para insertar una ingesta en la bbdd
    public void insertIngesta(Ingesta ingesta) {
        new insertAsyncTask(ingestaDAO).execute(ingesta);
    }

    //metodo para aliminar todas las ingesta en la bbdd
    public void deleteAll() {
        new AlimentacionRepository.deleteAsyncTask(ingestaDAO).execute();
    }

    //metodo para eliminar una ingesta de la bbdd
    public void removeIngesta(Ingesta seleccionada) {
        new deleteIngestaAsyncTask(ingestaDAO).execute(seleccionada);
    }

    //metodo para modificar una ingesta de la bbdd
    public void modifyIngesta(int id_ingesta, String nombre_nuevo, float num_raciones_nuevo, long fecha_ingesta) {
        WrapperIngesta wrapperIngesta=new AlimentacionRepository.WrapperIngesta(this.ingestaDAO,id_ingesta,nombre_nuevo,num_raciones_nuevo,fecha_ingesta);
        new modifyAsyncTask(wrapperIngesta).execute();
    }

    //asynktask para modificar una ingesta de la bbdd
    private static class modifyAsyncTask extends AsyncTask<AlimentacionRepository.WrapperIngesta, Void, Void> {

        private IngestaDAO myAsyncTaskDAO;
        private int id_ingesta;
        private String nombre_nuevo;
        private float num_raciones;
        private long fecha_ingesta;

        //constructor de la asynktask
        public modifyAsyncTask(AlimentacionRepository.WrapperIngesta wrapperIngesta) {
            this.myAsyncTaskDAO =wrapperIngesta.getIngestaDAO();
            this.id_ingesta=wrapperIngesta.getId();
            this.nombre_nuevo=wrapperIngesta.getNombre();
            this.num_raciones=wrapperIngesta.getNum_raciones();
            this.fecha_ingesta=wrapperIngesta.getFecha();

        }

        //metodo que modifica la ingesta de la bbdd
        @Override
        protected Void doInBackground(AlimentacionRepository.WrapperIngesta... wrapperIngestas) {
            myAsyncTaskDAO.modifyIngesta(this.id_ingesta,this.nombre_nuevo,this.num_raciones, this.fecha_ingesta);
            return null;
        }
    }

    // asynktask para eliminar una ingesta en la bbdd
    private static class deleteIngestaAsyncTask extends AsyncTask<Ingesta, Void, Void> {

        private IngestaDAO myAsyncTaskDAO;

        //constructor de la asynktask
        public deleteIngestaAsyncTask(IngestaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        //metodo que elimina la ingesta de la bbdd
        @Override
        protected Void doInBackground(Ingesta... ingestas) {
            myAsyncTaskDAO.deleteIngesta(ingestas[0].getId());
            return null;
        }
    }

    //asynktask para eliminar todas las ingestas de la bbdd
    private static class deleteAsyncTask extends AsyncTask<Ingesta, Void, Void> {

        private IngestaDAO myAsyncTaskDAO;

        //constructor de la asynktask
        public deleteAsyncTask(IngestaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        //metodo que elimina todas las ingestas de la bbdd
        @Override
        protected Void doInBackground(Ingesta... ingestas) {
            myAsyncTaskDAO.deleteIngestas();
            return null;
        }
    }

    //asynktask para insertar una ingesta en la bbdd
    private static class insertAsyncTask extends AsyncTask<Ingesta, Void, Void> {

        private IngestaDAO myAsyncTaskDAO;

        //constructor de la asynktask
        public insertAsyncTask(IngestaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        //metodo que inserta la ingesta de la bbdd
        @Override
        protected Void doInBackground(Ingesta... ingestas) {
            myAsyncTaskDAO.insertIngesta(ingestas[0]);
            return null;
        }
    }

    //clase auxiliar empleada por la asynktask para modificar una ingesta
    private class WrapperIngesta{

        private IngestaDAO ingestaDAO;
        private int id;
        private String nombre;
        private float num_raciones;
        private long fecha;

        public WrapperIngesta(IngestaDAO ingestaDAO, int id, String nombre, float num_raciones, long fecha) {
            this.ingestaDAO = ingestaDAO;
            this.id = id;
            this.nombre = nombre;
            this.num_raciones = num_raciones;
            this.fecha = fecha;
        }

        public IngestaDAO getIngestaDAO() {
            return ingestaDAO;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public float getNum_raciones() {
            return num_raciones;
        }

        public long getFecha() {
            return fecha;
        }
    }

}

