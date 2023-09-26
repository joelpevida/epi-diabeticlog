package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "registro_actividad_fisica_automatica_tabla")
public class RegistroActividadFisicaAutomatica implements Parcelable{


    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private long fecha;
    private int pasos;
    private float distancia;
    private float kcal_quemadas;
    private String uuid;

    //constructor por defecto
    public RegistroActividadFisicaAutomatica(){}

    //constructor que no se llegan a utilizar
    @Ignore
    public RegistroActividadFisicaAutomatica(int id, long fecha, int pasos, float kcal_quemadas, float distancia) {
        this.id = id;
        this.fecha = fecha;
        this.pasos = pasos;
        this.kcal_quemadas = kcal_quemadas;
        this.distancia=distancia;
    }
    //constructor que no se llegan a utilizar
    @Ignore
    public RegistroActividadFisicaAutomatica(int id, long fecha, int pasos, float kcal_quemadas, float distancia, String uuid) {
        this.id = id;
        this.fecha = fecha;
        this.pasos = pasos;
        this.kcal_quemadas = kcal_quemadas;
        this.distancia=distancia;
        this.uuid = uuid;
    }

    //getters de los atributos
    public int getId() {
        return id;
    }

    public long getFecha() {
        return fecha;
    }

    public int getPasos() {
        return pasos;
    }

    public float getKcal_quemadas() {
        return kcal_quemadas;
    }

    public String getUuid() {
        return uuid;
    }

    public float getDistancia() {
        return distancia;
    }

    //setters de los atributos
    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public void setPasos(int pasos) {
        this.pasos = pasos;
    }

    public void setKcal_quemadas(float kcal_quemadas) {
        this.kcal_quemadas = kcal_quemadas;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    //metodos para implementar la interfaz parcelable
    @Ignore
    protected RegistroActividadFisicaAutomatica(Parcel in) {
        id = in.readInt();
        fecha = in.readLong();
        pasos = in.readInt();
        kcal_quemadas = in.readFloat();
        distancia = in.readFloat();
        uuid=in.readString();

    }

    public static final Creator<RegistroActividadFisicaAutomatica> CREATOR = new Creator<RegistroActividadFisicaAutomatica>() {
        @Override
        public RegistroActividadFisicaAutomatica createFromParcel(Parcel in) {
            return new RegistroActividadFisicaAutomatica(in);
        }

        @Override
        public RegistroActividadFisicaAutomatica[] newArray(int size) {
            return new RegistroActividadFisicaAutomatica[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(fecha);
        dest.writeInt(pasos);
        dest.writeFloat(kcal_quemadas);
        dest.writeFloat(distancia);
        dest.writeString(uuid);

    }

}
