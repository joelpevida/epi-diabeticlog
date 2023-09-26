package es.uniovi.imovil.epi_diabeticlog.Alimentacion.ViewModel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.AlimentacionRepository;
import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.Ingesta;

public class AlimentacionViewModel extends AndroidViewModel {


    private AlimentacionRepository alimentacionRepository;
    private LiveData<List<Ingesta>> ingestas;

    Calendar calendar_fin_dia;
    Calendar calendar_inicio_dia;

    //constructor
    public AlimentacionViewModel(@NonNull Application application){
        super(application);

        // inicio del dia
        calendar_inicio_dia = new GregorianCalendar();
        calendar_inicio_dia.set(Calendar.HOUR_OF_DAY, 00);
        calendar_inicio_dia.set(Calendar.MINUTE, 00);
        calendar_inicio_dia.set(Calendar.SECOND, 00);
        calendar_inicio_dia.set(Calendar.MILLISECOND, 00000);

        // fin del dia
        calendar_fin_dia = new GregorianCalendar();
        calendar_fin_dia.set(Calendar.HOUR_OF_DAY, 23);
        calendar_fin_dia.set(Calendar.MINUTE, 59);
        calendar_fin_dia.set(Calendar.SECOND, 59);
        calendar_fin_dia.set(Calendar.MILLISECOND, 99999);

        alimentacionRepository=new AlimentacionRepository(application, calendar_inicio_dia.getTimeInMillis(), calendar_fin_dia.getTimeInMillis());
        ingestas=alimentacionRepository.getIngestas();
    }

    //getter de las ingestas con su contenedor livedata
    public LiveData<List<Ingesta>> getIngestas() {
        return this.ingestas;
    }

    //metodo para insertar una ingesta en la bbdd
    public boolean insert(Ingesta ingesta) {
        if(!ingesta.getTipo_ingesta().equals("Extra")){
            if(nombreExistente(ingesta.getTipo_ingesta())){
                return false;
            }
        }
        alimentacionRepository.insertIngesta(ingesta);
        return true;
    }

    //metodo para modificar una ingesta concreta de la bbdd
    public boolean modifyIngesta(int id_ingesta, String nombre_nuevo, float num_raciones_nuevo, long fecha_ingesta) {
        nombre_nuevo=this.adaptName(nombre_nuevo);
        alimentacionRepository.modifyIngesta(id_ingesta, nombre_nuevo, num_raciones_nuevo, fecha_ingesta);
        return true;
    }

    //metodo para establecer el nombre de la ingesta segun criterios
    public String adaptName(String name){

        String str = name;
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        return cap;
    }

    //metodo para eliminar la ingesta seleccionada de la bbdd
    public void removeIngesta(Ingesta seleccionada) {
        alimentacionRepository.removeIngesta(seleccionada);
    }

    //metodo para informar si ya eciste una ingesta con ese nombre registrada en el dia actual
    public boolean nombreExistente(String name){

        if(this.ingestas.getValue()!=null){
            for (Ingesta aux:this.ingestas.getValue()) {
                if(aux.getTipo_ingesta().equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

}

