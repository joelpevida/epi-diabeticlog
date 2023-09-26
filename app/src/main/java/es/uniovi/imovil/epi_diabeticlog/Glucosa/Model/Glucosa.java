package es.uniovi.imovil.epi_diabeticlog.Glucosa.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import es.uniovi.imovil.epi_diabeticlog.PantallaInicio.View.ItemPantallaInicio;

@Entity(tableName = "glucosa_tabla")
public class Glucosa implements Parcelable, ItemPantallaInicio {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private float mgDl;
    private long fecha_medicion;
    private String uuid;

    //constructor por defecto
    public Glucosa(){}

    //constructor
    @Ignore
    public Glucosa(int id, long fecha_medicion, float mgDl) {
        this.id=id;
        this.mgDl = mgDl;
        this.fecha_medicion = fecha_medicion;

    }

    //constructor
    @Ignore
    public Glucosa(int id, long fecha_medicion, float dgMl, String uuid) {
        this.id=id;
        this.mgDl = dgMl;
        this.fecha_medicion = fecha_medicion;
        this.uuid=uuid;
    }

    //constructor
    @Ignore
    public Glucosa(long fecha_medicion, float dgMl, String uuid) {
        this.id=id;
        this.mgDl = dgMl;
        this.fecha_medicion = fecha_medicion;
        this.uuid=uuid;
    }

    //metodos interfaz Parcelable
    @Ignore
    protected Glucosa(Parcel in) {
        id = in.readInt();
        mgDl = in.readFloat();
        fecha_medicion = in.readLong();
        uuid=in.readString();
    }

    public static final Creator<Glucosa> CREATOR = new Creator<Glucosa>() {
        @Override
        public Glucosa createFromParcel(Parcel in) {
            return new Glucosa(in);
        }

        @Override
        public Glucosa[] newArray(int size) {
            return new Glucosa[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeFloat(mgDl);
        dest.writeLong(fecha_medicion);
        dest.writeString(uuid);
    }

    //getters y setters
    public String getUuid() {
        return uuid;
    }

    public int getId() {
        return id;
    }

    public float getMgDl() {
        return mgDl;
    }

    public long getFecha_medicion() {
        return fecha_medicion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMgDl(float dgMl) {
        this.mgDl = dgMl;
    }

    public void setFecha_medicion(long fecha_medicion) {
        this.fecha_medicion = fecha_medicion;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    //metodos de la interfaz ItemPantallaInicio

    //metodo que devuelve la fecha
    @Override
    public long getFechaItem() {
        return this.getFecha_medicion();
    }

    //metodo que devuelve la hora registrada en un formato amigable
    @Override
    public String getHora() {
        Calendar c=Calendar.getInstance();
        c.setTimeInMillis(this.getFechaItem());
        c.setTimeZone(TimeZone.getDefault());

        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
        String time = format.format(c.getTime());

        return time;
    }

    //metodo que retorna un string indicando el tipo de elemento, en este caso Glucosa
    @Override
    public String getTipo() {
        return "Glucosa";
    }

    //metodo que retorna un resumen de los principales atributos de la glucosa
    @Override
    public String getContent() {
        String content;
        content=String.valueOf(this.getMgDl()) + " mg/Dl";
        return content;
    }
}