package es.uniovi.imovil.epi_diabeticlog.Glucosa.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.GlucosaRepository;
import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;

public class GlucosaViewModel extends AndroidViewModel {

    private GlucosaRepository glucosaRepository;
    private LiveData<List<Glucosa>> glucosas;

    //constructor
    public GlucosaViewModel(@NonNull Application application){
        super(application);

        // inicio del dia
        Calendar calendar_inicio_dia = new GregorianCalendar();
        calendar_inicio_dia.set(Calendar.HOUR_OF_DAY, 00);
        calendar_inicio_dia.set(Calendar.MINUTE, 00);
        calendar_inicio_dia.set(Calendar.SECOND, 00);
        calendar_inicio_dia.set(Calendar.MILLISECOND, 00000);

        // fin del dia
        Calendar calendar_fin_dia = new GregorianCalendar();
        calendar_fin_dia.set(Calendar.HOUR_OF_DAY, 23);
        calendar_fin_dia.set(Calendar.MINUTE, 59);
        calendar_fin_dia.set(Calendar.SECOND, 59);
        calendar_fin_dia.set(Calendar.MILLISECOND, 99999);

        glucosaRepository=new GlucosaRepository(application, calendar_inicio_dia.getTimeInMillis(), calendar_fin_dia.getTimeInMillis());
        glucosas=glucosaRepository.getGlucosas();
    }

    //getter de las glucosas con su contenedor livedata
    public LiveData<List<Glucosa>> getGlucosas() {
        return this.glucosas;
    }

    //metodo para insertar una glucosa en la bbdd
    public void insert(Glucosa glucosa) {
        glucosaRepository.insertGlucosa(glucosa);
    }

    //metodo para modificar una glucosa de la bbdd
    public void modifyGlucosa(float mgDlNuevo, long fechaMedicionNueva, int id) {
        glucosaRepository.modifyGlucosa(mgDlNuevo, fechaMedicionNueva, id);
    }

    //metodo para eliminar una glucosa de la bbdd
    public void removeGlucosa(Glucosa seleccionada) {
        glucosaRepository.removeGlucosa(seleccionada);
    }

    //metodo para eliminar todas las glucosas de la bbdd
    public void removeGlucosas() {
        glucosaRepository.deleteAll();
    }
}
