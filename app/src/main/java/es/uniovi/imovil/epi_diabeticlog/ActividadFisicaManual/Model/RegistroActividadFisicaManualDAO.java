package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManual;

@Dao
public interface RegistroActividadFisicaManualDAO {

    //metodo para insertar un registro de actividad física manual en la bbdd
    @Insert
    public void insertRegistroActividadFisicaManual(RegistroActividadFisicaManual registro);

    //metodo para eliminar todos los registros de actividad física manualde la bbdd
    @Query("DELETE from registro_actividad_fisica_manual_tabla")
    public void deleteRegistrosManuales();

    //metodo para obtener todos los registros de actividad física manual de la bbdd entre las fechas indicadas
    @Query("SELECT * FROM registro_actividad_fisica_manual_tabla WHERE fecha_inicio>=:inicio_dia AND fecha_inicio<=:fin_dia")
    public LiveData<List<RegistroActividadFisicaManual>> getRegistrosManuales(long inicio_dia, long fin_dia);

    //metodo para eliminar un registro de actividad física manual de la bbdd
    @Query("DELETE from registro_actividad_fisica_manual_tabla WHERE :id_registro=registro_actividad_fisica_manual_tabla.id")
    void deleteRegistro(int id_registro);

    //metodo para modificar un registro de actividad física manual concreto de la bbdd
    @Query("UPDATE registro_actividad_fisica_manual_tabla SET fecha_inicio=:fecha_inicio_nuevo, fecha_fin=:fecha_fin_nuevo, tipo_actividad=:tipo_actividad_nuevo, intensidad=:intensidad_nuevo , kcal_quemadas=:kcal_quemadas_nuevo WHERE :id_registro=registro_actividad_fisica_manual_tabla.id")
    void modifyRegistro(String tipo_actividad_nuevo, String intensidad_nuevo, float kcal_quemadas_nuevo, long fecha_inicio_nuevo, long fecha_fin_nuevo, int id_registro);

}
