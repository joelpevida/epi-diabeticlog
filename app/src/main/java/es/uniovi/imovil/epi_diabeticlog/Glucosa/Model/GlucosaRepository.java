package es.uniovi.imovil.epi_diabeticlog.Glucosa.Model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.BaseDatos.DataBase;

public class GlucosaRepository {

    private GlucosaDAO glucosaDAO;
    private LiveData<List<Glucosa>> glucosas;

    //constructor
    public GlucosaRepository(Application application, long inicio_dia, long fin_dia) {
        this.glucosaDAO = DataBase.getDatabase(application).glucosaDAO();
        this.glucosas = this.glucosaDAO.getGlucosas(inicio_dia, fin_dia);
    }

    //getter de las glucosas con su contenedor livedata
    public LiveData<List<Glucosa>> getGlucosas() {
        return this.glucosas;
    }

    //metodo para insertar una glucosa en la bbd
    public void insertGlucosa(Glucosa glucosa) {
        new insertAsyncTask(glucosaDAO).execute(glucosa);
    }

    //metodo apra eliminar todas las glucosas de la bbdd
    public void deleteAll() {
        new deleteAsyncTask(glucosaDAO).execute();
    }

    //metodo para eliminar una glucosa de la bbdd
    public void removeGlucosa(Glucosa seleccionada) {
        new deleteInsulinaAsyncTask(glucosaDAO).execute(seleccionada);
    }

    //metodo para modificar una glucosa de la bbdd
    public void modifyGlucosa(float mgDLNuevo,  long fechaMedicionNueva, int id) {
        WrapperGlucosa wrapperGlucosa=new WrapperGlucosa(this.glucosaDAO, id, fechaMedicionNueva, mgDLNuevo);
        new modifyAsyncTask(wrapperGlucosa).execute();
    }

    //asynktask para modificar una glucosa de la bbdd
    private static class modifyAsyncTask extends AsyncTask<WrapperGlucosa, Void, Void> {

        private GlucosaDAO myAsyncTaskDAO;
        private int id;
        private float mgDL;
        private long fechaMedicion;


        //constructor de la asynktask
        public modifyAsyncTask(WrapperGlucosa wrapperGlucosa) {
            this.myAsyncTaskDAO =wrapperGlucosa.getGlucosaDAO();
            this.mgDL=wrapperGlucosa.getMgDL();
            this.fechaMedicion=wrapperGlucosa.getFechaMedicion();
            this.id=wrapperGlucosa.getId();
        }

        //metodo para modificar una glucosa en la bbdd
        @Override
        protected Void doInBackground(WrapperGlucosa... wrapperGlucosas) {
            myAsyncTaskDAO.modifyGlucosa(this.fechaMedicion, this.mgDL, this.id);
            return null;
        }
    }

    //clase auxiliar usada por la modifyaskynktask
    private class WrapperGlucosa{

        private GlucosaDAO glucosaDAO;
        private int id;
        private float mgDL;
        private long fechaMedicion;


        public WrapperGlucosa(GlucosaDAO glucosaDAO, int id, long fechaMedicion, float mgDL) {
            this.glucosaDAO = glucosaDAO;
            this.id = id;
            this.mgDL=mgDL;
            this.fechaMedicion=fechaMedicion;

        }

        public GlucosaDAO getGlucosaDAO() {
            return glucosaDAO;
        }

        public int getId() {
            return id;
        }

        public float getMgDL() {
            return mgDL;
        }

        public long getFechaMedicion() {
            return fechaMedicion;
        }
    }

    //asynktask para eliminar una glucosa de la bbdd
    private static class deleteInsulinaAsyncTask extends AsyncTask<Glucosa, Void, Void> {

        private GlucosaDAO myAsyncTaskDAO;

        //constructor de la asynktask
        public deleteInsulinaAsyncTask(GlucosaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        //metodo para eliminar una glucosa de la bbdd
        @Override
        protected Void doInBackground(Glucosa... glucosas) {
            myAsyncTaskDAO.deleteGlucosa(glucosas[0].getId());
            return null;
        }
    }

    //asynktask para eliminar todas las glucosas de la bbdd
    private static class deleteAsyncTask extends AsyncTask<Glucosa, Void, Void> {

        private GlucosaDAO myAsyncTaskDAO;

        //constructor de la asynktask
        public deleteAsyncTask(GlucosaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        //metodo para eliminar todas las glucosas de la bbdd
        @Override
        protected Void doInBackground(Glucosa... glucosas) {
            myAsyncTaskDAO.deleteGlucosas();
            return null;
        }
    }

    //asynktask para insertar una glucosa en la bbdd
    private static class insertAsyncTask extends AsyncTask<Glucosa, Void, Void> {

        private GlucosaDAO myAsyncTaskDAO;

        //constructor de la asynktask
        public insertAsyncTask(GlucosaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        //metodo para insertar una glucosa en la bbdd
        @Override
        protected Void doInBackground(Glucosa... glucosas) {
            myAsyncTaskDAO.insertGlucosa(glucosas[0]);
            return null;
        }
    }

}
