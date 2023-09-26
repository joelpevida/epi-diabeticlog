package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.Model.RegistroActividadFisicaAutomatica;

@Dao
public interface RegistroActividadFisicaAutomaticaDAO {

    //metodo para insertar un elemento en la bbdd
    @Insert
    public void insertRegistroActividadFisicaAutomatica(RegistroActividadFisicaAutomatica registro);

    //metodo para eliminar todos los elementos de la bbdd
    @Query("DELETE from registro_actividad_fisica_automatica_tabla")
    public void deleteRegistrosAutomaticas();

    //metodo para obtener un elemento en la bbdd entre unas fechas determinadas
    @Query("SELECT * FROM registro_actividad_fisica_automatica_tabla WHERE fecha>=:inicio_dia AND fecha<=:fin_dia")
    public LiveData<List<RegistroActividadFisicaAutomatica>> getRegistroAutomatico(long inicio_dia, long fin_dia);

}
