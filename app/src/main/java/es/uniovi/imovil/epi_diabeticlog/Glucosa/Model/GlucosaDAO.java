package es.uniovi.imovil.epi_diabeticlog.Glucosa.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;

@Dao
public interface GlucosaDAO {

    //metodo para insertar una glucosa en la bbdd
    @Insert
    public void insertGlucosa(Glucosa glucosa);

    //metodo para eliminar todas las glucosas en la bbdd
    @Query("DELETE from glucosa_tabla")
    public void deleteGlucosas();

    //metodo para seleccionar todas las glucosas de la bbdd con fecha entre las dos fechas introducidad
    @Query("SELECT * FROM glucosa_tabla WHERE fecha_medicion>=:inicio_dia AND fecha_medicion<=:fin_dia")
    public LiveData<List<Glucosa>> getGlucosas(long inicio_dia, long fin_dia);

    //metodo para eliminar una ingesta concreta de la bbdd
    @Query("DELETE from glucosa_tabla WHERE :id=id")
    void deleteGlucosa(int id);

    //metodo para modificar una ingesta concreta de la bbd
    @Query("UPDATE glucosa_tabla SET fecha_medicion=:fechaMedicionNuevo,  mgDl=:mgDLNuevo WHERE :idMedicion=id")
    void modifyGlucosa(long fechaMedicionNuevo, float mgDLNuevo, int idMedicion);

}
