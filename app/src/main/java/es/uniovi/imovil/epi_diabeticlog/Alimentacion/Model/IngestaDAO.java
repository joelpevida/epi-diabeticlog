package es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IngestaDAO {

    //metodo para insertar una ingesta en la bbdd
    @Insert
    void insertIngesta(Ingesta ingesta);

    //metodo para eliminar todas las ingestas de la bbdd
    @Query("DELETE from ingesta_tabla")
    void deleteIngestas();

    //metodo para obtener todas las ingestas registradas con efcha entre las fechas que recibe como parametro
    @Query("SELECT * FROM ingesta_tabla WHERE fecha>=:inicio_dia AND fecha<=:fin_dia")
    LiveData<List<Ingesta>> getIngestas(long inicio_dia, long fin_dia);

    //metodo para eliminar una ingesta concreta de la bbdd
    @Query("DELETE from ingesta_tabla WHERE :id_ingesta=ingesta_tabla.id")
    void deleteIngesta(int id_ingesta);

    //metodo para modificar una ingesta concreta de la bbdd
    @Query("UPDATE ingesta_tabla SET tipo_ingesta=:nombre_nuevo, num_raciones=:num_raciones_nuevo, fecha=:fecha_ingesta WHERE :id_ingesta=id")
    void modifyIngesta(int id_ingesta, String nombre_nuevo, float num_raciones_nuevo, long fecha_ingesta);

}
