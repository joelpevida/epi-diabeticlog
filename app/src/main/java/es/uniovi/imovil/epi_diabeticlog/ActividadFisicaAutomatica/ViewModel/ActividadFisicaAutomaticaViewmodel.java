package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.Model.ActividadFisicaAutomaticaRepository;
import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.Model.RegistroActividadFisicaAutomatica;


public class ActividadFisicaAutomaticaViewmodel extends AndroidViewModel {

    private ActividadFisicaAutomaticaRepository actividadFisicaAutomaticaRepository;

    private LiveData<List<RegistroActividadFisicaAutomatica>> registros;

    //constructor
    public ActividadFisicaAutomaticaViewmodel(@NonNull Application application){
        super(application);

        // inicio del dia actual
        Calendar calendar_inicio_dia = new GregorianCalendar();
        calendar_inicio_dia.set(Calendar.HOUR_OF_DAY, 0);
        calendar_inicio_dia.set(Calendar.MINUTE, 0);
        calendar_inicio_dia.set(Calendar.SECOND, 0);
        calendar_inicio_dia.set(Calendar.MILLISECOND, 0);

        // fin del dia actual
        Calendar calendar_fin_dia = new GregorianCalendar();
        calendar_fin_dia.set(Calendar.HOUR_OF_DAY, 23);
        calendar_fin_dia.set(Calendar.MINUTE, 59);
        calendar_fin_dia.set(Calendar.SECOND, 59);
        calendar_fin_dia.set(Calendar.MILLISECOND, 999);

        actividadFisicaAutomaticaRepository=new ActividadFisicaAutomaticaRepository(application, calendar_inicio_dia.getTimeInMillis(), calendar_fin_dia.getTimeInMillis());

        registros=actividadFisicaAutomaticaRepository.getRegistros();
    }

    //metodo para insertar un registro de actividad física automatica en la bbd
    public void insert(RegistroActividadFisicaAutomatica registro) {

        actividadFisicaAutomaticaRepository.insertRegistro(registro);

    }

}
