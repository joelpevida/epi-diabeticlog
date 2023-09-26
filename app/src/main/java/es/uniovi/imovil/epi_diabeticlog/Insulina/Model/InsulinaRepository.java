package es.uniovi.imovil.epi_diabeticlog.Insulina.Model;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.BaseDatos.DataBase;

public class InsulinaRepository {


    private InsulinaDAO insulinaDAO;
    private LiveData<List<Insulina>> insulinas;

    //constructor
    public InsulinaRepository(Application application, long inicio_dia, long fin_dia) {
        this.insulinaDAO = DataBase.getDatabase(application).insulinaDAO();
        this.insulinas = this.insulinaDAO.getInsulinas(inicio_dia, fin_dia);
    }

    //getter de las insulinas con su contenedor livedata
    public LiveData<List<Insulina>> getInsulinas() {
        return this.insulinas;
    }

    //metodo para insertar una insulina en la bbdd
    public void insertInsulina(Insulina insulina) {
        new insertAsyncTask(insulinaDAO).execute(insulina);
    }

    //metodo apra eliminar todas las insulinas de la bbdd
    public void deleteAll() {
        new deleteAsyncTask(insulinaDAO).execute();
    }

    //metodo para eliminar una insulina de la bbdd
    public void removeInsulina(Insulina seleccionada) {
        new deleteInsulinaAsyncTask(insulinaDAO).execute(seleccionada);
    }

    //metodo para modificar una insulina de la bbdd
    public void modifyInsulina(String tipo_aplicacion_nuevo, String tipo_insulina_nuevo, int num_unidades_nuevo, long fecha_aplicacion_nueva, int id_aplicacion) {
        WrapperInsulina wrapperInsulina=new WrapperInsulina(insulinaDAO, id_aplicacion, fecha_aplicacion_nueva, tipo_insulina_nuevo, num_unidades_nuevo, tipo_aplicacion_nuevo);
        new modifyAsyncTask(wrapperInsulina).execute();
    }

    //asynktask para modificar una insulina de la bbdd
    private static class modifyAsyncTask extends AsyncTask<WrapperInsulina, Void, Void> {

        private InsulinaDAO myAsyncTaskDAO;
        private int id;
        private long fecha_aplicacion;
        private String tipo_insulina;
        private int num_unidades;
        private String tipo_aplicacion;

        //constructor de la asynktask
        public modifyAsyncTask(WrapperInsulina wrapperRegistro) {
            this.myAsyncTaskDAO =wrapperRegistro.getInsulinaDAO();
            this.num_unidades=wrapperRegistro.getNum_unidades();
            this.tipo_insulina=wrapperRegistro.getTipo_insulina();
            this.tipo_aplicacion=wrapperRegistro.getTipo_aplicacion();
            this.fecha_aplicacion=wrapperRegistro.getFecha_aplicacion();
            this.id=wrapperRegistro.getId();
        }

        //metodo para modificar una insulina de la bbdd
        @Override
        protected Void doInBackground(WrapperInsulina... wrapperInsulinas) {
            myAsyncTaskDAO.modifyInsulina(this.tipo_aplicacion, this.tipo_insulina, this.num_unidades, this.fecha_aplicacion, this.id);
            return null;
        }
    }

    //asynktask para eliminar una insulina de la bbdd
    private static class deleteInsulinaAsyncTask extends AsyncTask<Insulina, Void, Void> {

        private InsulinaDAO myAsyncTaskDAO;

        //constructor de la asynktask
        public deleteInsulinaAsyncTask(InsulinaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }
        //metodo para eliminar una insulina de la bbdd
        @Override
        protected Void doInBackground(Insulina... insulinas) {
            myAsyncTaskDAO.deleteInsulina(insulinas[0].getId());
            return null;
        }
    }

    //asynktask para eliminar todas las insulinas de la bbdd
    private static class deleteAsyncTask extends AsyncTask<Insulina, Void, Void> {

        private InsulinaDAO myAsyncTaskDAO;

        //constructor de la asynktask
        public deleteAsyncTask(InsulinaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        //metodo para eliminar todas las insulinas de la bbdd
        @Override
        protected Void doInBackground(Insulina... insulinas) {
            myAsyncTaskDAO.deleteInsulinas();
            return null;
        }
    }

    //asynktask para insertar una insulina en la bbdd
    private static class insertAsyncTask extends AsyncTask<Insulina, Void, Void> {

        private InsulinaDAO myAsyncTaskDAO;

        //constructor de la asynktask
        public insertAsyncTask(InsulinaDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        //metodo para insertar una insulina en la bbdd
        @Override
        protected Void doInBackground(Insulina... insulinas) {
            myAsyncTaskDAO.insertInsulina(insulinas[0]);
            return null;
        }
    }

    //clase auxiliar usada por la asynktask para modificar una insulina de la bbdd
    private class WrapperInsulina{

        private InsulinaDAO insulinaDAO;
        private int id;
        private long fecha_aplicacion;
        private String tipo_insulina;
        private int num_unidades;
        private String tipo_aplicacion;

        public WrapperInsulina(InsulinaDAO insulinaDAO, int id, long fecha_aplicacion, String tipo_insulina, int num_unidades, String tipo_aplicacion) {
            this.insulinaDAO = insulinaDAO;
            this.id = id;
            this.fecha_aplicacion = fecha_aplicacion;
            this.tipo_insulina = tipo_insulina;
            this.num_unidades = num_unidades;
            this.tipo_aplicacion = tipo_aplicacion;
        }

        public InsulinaDAO getInsulinaDAO() {
            return insulinaDAO;
        }

        public int getId() {
            return id;
        }

        public long getFecha_aplicacion() {
            return fecha_aplicacion;
        }

        public String getTipo_insulina() {
            return tipo_insulina;
        }

        public int getNum_unidades() {
            return num_unidades;
        }

        public String getTipo_aplicacion() {
            return tipo_aplicacion;
        }
    }

}

