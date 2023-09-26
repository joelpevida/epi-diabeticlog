package es.uniovi.imovil.epi_diabeticlog.Insulina.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;

@Dao
public interface InsulinaDAO {

    //metodo para insertar una insulina en la bbdd
    @Insert
    public void insertInsulina(Insulina insulina);

    //metodo para eliminar una insulina de la bbdd
    @Query("DELETE from insulina_tabla")
    public void deleteInsulinas();

    //metodo para obtener todas las insulinas de la bbdd con la fehca de medición entre las fechas introducidad
    @Query("SELECT * FROM insulina_tabla WHERE fecha_aplicacion>=:inicio_dia AND fecha_aplicacion<=:fin_dia")
    public LiveData<List<Insulina>> getInsulinas(long inicio_dia, long fin_dia);

    //metodo para eliminar una insulina de la bbdd
    @Query("DELETE from insulina_tabla WHERE :id_insulina=id")
    void deleteInsulina(int id_insulina);

    //metodo para modificar una insulina de la bbdd
    @Query("UPDATE insulina_tabla SET tipo_aplicacion=:tipo_aplicacion_nuevo,  tipo_insulina=:tipo_insulina_nuevo, num_unidades=:num_unidades_nuevo , fecha_aplicacion=:fecha_aplicacion_nueva WHERE :id_aplicacion=id")
    void modifyInsulina(String tipo_aplicacion_nuevo, String tipo_insulina_nuevo, int num_unidades_nuevo, long fecha_aplicacion_nueva, int id_aplicacion);

}