package es.uniovi.imovil.epi_diabeticlog.BaseDatos;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.Model.RegistroActividadFisicaAutomatica;
import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.Model.RegistroActividadFisicaAutomaticaDAO;
import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManual;
import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManualDAO;
import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.Ingesta;
import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.IngestaDAO;
import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;
import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.GlucosaDAO;
import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;
import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.InsulinaDAO;
import es.uniovi.imovil.epi_diabeticlog.Usuario.Model.Usuario;
import es.uniovi.imovil.epi_diabeticlog.Usuario.Model.UsuarioDAO;

@Database(entities = {RegistroActividadFisicaManual.class, Ingesta.class, Insulina.class, Glucosa.class, Usuario.class, RegistroActividadFisicaAutomatica.class}, version = 5, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    public abstract RegistroActividadFisicaManualDAO registroActividadFisicaManualDAO();
    public abstract IngestaDAO ingestaDAO();
    public abstract InsulinaDAO insulinaDAO();
    public abstract GlucosaDAO glucosaDAO();
    public abstract UsuarioDAO usuarioDAO();
    public abstract RegistroActividadFisicaAutomaticaDAO registroActividadFisicaAutomaticaDAO();

    private static DataBase INSTANCE;

    public static DataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DataBase.class) {
                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DataBase.class, "database")
                            .addCallback(sRoomDatabaseCallbackCreate)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallbackCreate = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDBAsync(INSTANCE).execute();
        }
    };

    private static class populateDBAsync extends AsyncTask<Void, Void, Void> {

        private final RegistroActividadFisicaManualDAO registroActividadFisicaManualDAO;
        private final IngestaDAO ingestaDAO;
        private final InsulinaDAO insulinaDAO;
        private final GlucosaDAO glucosaDAO;
        private final UsuarioDAO usuarioDAO;
        private final RegistroActividadFisicaAutomaticaDAO registroActividadFisicaAutomaticaDAO;

        populateDBAsync(DataBase db) {
            this.registroActividadFisicaManualDAO = db.registroActividadFisicaManualDAO();
            this.ingestaDAO=db.ingestaDAO();
            this.insulinaDAO=db.insulinaDAO();
            this.glucosaDAO=db.glucosaDAO();
            this.usuarioDAO=db.usuarioDAO();
            this.registroActividadFisicaAutomaticaDAO = db.registroActividadFisicaAutomaticaDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
