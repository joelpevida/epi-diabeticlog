package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.ViewModel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.ActividadFisicaManualRepository;
import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManual;

public class ActividadFisicaViewModel extends AndroidViewModel {


    private ActividadFisicaManualRepository actividadFisicaManualRepository;
    private LiveData<List<RegistroActividadFisicaManual>> registros;

    //constructor
    public ActividadFisicaViewModel(@NonNull Application application){
        super(application);

        // inicio del dia
        Calendar calendar_inicio_dia = new GregorianCalendar();
        calendar_inicio_dia.set(Calendar.HOUR_OF_DAY, 0);
        calendar_inicio_dia.set(Calendar.MINUTE, 0);
        calendar_inicio_dia.set(Calendar.SECOND, 0);
        calendar_inicio_dia.set(Calendar.MILLISECOND, 0);

        // fin del dia
        Calendar calendar_fin_dia = new GregorianCalendar();
        calendar_fin_dia.set(Calendar.HOUR_OF_DAY, 23);
        calendar_fin_dia.set(Calendar.MINUTE, 59);
        calendar_fin_dia.set(Calendar.SECOND, 59);
        calendar_fin_dia.set(Calendar.MILLISECOND, 999);

        actividadFisicaManualRepository=new ActividadFisicaManualRepository(application, calendar_inicio_dia.getTimeInMillis(), calendar_fin_dia.getTimeInMillis());
        registros=actividadFisicaManualRepository.getRegistros();
    }

    //getter de los registros de la actividad fisica manual con su contenedor livedata
    public LiveData<List<RegistroActividadFisicaManual>> getRegistros() {
        return this.registros;
    }

    //metodo para insertar un registro de actividad fisica manual en la base de datos
    public void insert(RegistroActividadFisicaManual registro) {
        actividadFisicaManualRepository.insertRegistro(registro);
    }

    //metodo para modificar un un registro de actividad fisica manual de la base de datos
    public void modifyRegistro(String tipo_actividad_nuevo, String intensidad_nuevo, float kcal_quemadas_nuevo, long fecha_inicio_nuevo, long fecha_fin_nuevo, int id_registro) {
        actividadFisicaManualRepository.modifyRegistro(tipo_actividad_nuevo, intensidad_nuevo, kcal_quemadas_nuevo, fecha_inicio_nuevo, fecha_fin_nuevo, id_registro);
    }

    //metodo para eliminar un registro de actividad fisica manual de la base de datos
    public void removeRegistro(RegistroActividadFisicaManual seleccionada) {
        actividadFisicaManualRepository.removeRegistro(seleccionada);
    }

}

