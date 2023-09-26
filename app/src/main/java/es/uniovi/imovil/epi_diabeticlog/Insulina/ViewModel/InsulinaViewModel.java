package es.uniovi.imovil.epi_diabeticlog.Insulina.ViewModel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.InsulinaRepository;
import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;

public class InsulinaViewModel extends AndroidViewModel {

    private InsulinaRepository insulinaRepository;
    private LiveData<List<Insulina>> insulinas;

    //constructor
    public InsulinaViewModel(@NonNull Application application){
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

        insulinaRepository=new InsulinaRepository(application, calendar_inicio_dia.getTimeInMillis(), calendar_fin_dia.getTimeInMillis());
        insulinas=insulinaRepository.getInsulinas();
    }

    //getter del listado de insulinas con su contenerdor livedata
    public LiveData<List<Insulina>> getInsulinas() {
        return this.insulinas;
    }

    //metodo para insertar una insulina en la bbdd
    public void insert(Insulina insulina) {
        insulinaRepository.insertInsulina(insulina);
    }

    //metodo para modificar una insulina de la bbdd
    public void modifyInsulina(String tipo_aplicacion_nuevo, String tipo_insulina_nuevo, int num_unidades_nuevo, long fecha_aplicacion_nueva, int id_aplicacion) {
        insulinaRepository.modifyInsulina(tipo_aplicacion_nuevo, tipo_insulina_nuevo, num_unidades_nuevo, fecha_aplicacion_nueva, id_aplicacion);
    }

    //metodo para eliminar una insulina de la bbdd
    public void removeInsulina(Insulina seleccionada) {
        insulinaRepository.removeInsulina(seleccionada);
    }

    //metodo para eliminar todas las insulina de la bbdd
    public void removeInsulinas() {
        insulinaRepository.deleteAll();
    }
}

