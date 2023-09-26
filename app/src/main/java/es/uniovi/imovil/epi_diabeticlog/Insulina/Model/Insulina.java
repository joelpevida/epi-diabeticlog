package es.uniovi.imovil.epi_diabeticlog.Insulina.Model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import es.uniovi.imovil.epi_diabeticlog.PantallaInicio.View.ItemPantallaInicio;

@Entity(tableName = "insulina_tabla")
public class Insulina implements Parcelable, ItemPantallaInicio {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private long fecha_aplicacion;
    private String tipo_insulina;
    private int num_unidades;
    private String tipo_aplicacion;
    private String uuid;

    //constructor
    public Insulina(){}

    //constructor
    @Ignore
    public Insulina(int id, long fecha_aplicacion, String tipo_insulina, int num_unidades, String tipo_aplicacion) {
        this.id=id;
        this.fecha_aplicacion = fecha_aplicacion;
        this.tipo_insulina = tipo_insulina;
        this.num_unidades = num_unidades;
        this.tipo_aplicacion = tipo_aplicacion;
    }

    //constructor
    @Ignore
    public Insulina(int id, long fecha_aplicacion, String tipo_insulina, int num_unidades, String tipo_aplicacion, String uuid) {
        this.id=id;
        this.fecha_aplicacion = fecha_aplicacion;
        this.tipo_insulina = tipo_insulina;
        this.num_unidades = num_unidades;
        this.tipo_aplicacion = tipo_aplicacion;
        this.uuid=uuid;
    }

    //metodos de la interfaz parcelable
    @Ignore
    protected Insulina(Parcel in) {
        id = in.readInt();
        fecha_aplicacion = in.readLong();
        tipo_insulina = in.readString();
        num_unidades = in.readInt();
        tipo_aplicacion = in.readString();
        uuid=in.readString();
    }

    public static final Creator<Insulina> CREATOR = new Creator<Insulina>() {
        @Override
        public Insulina createFromParcel(Parcel in) {
            return new Insulina(in);
        }

        @Override
        public Insulina[] newArray(int size) {
            return new Insulina[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(fecha_aplicacion);
        dest.writeString(tipo_insulina);
        dest.writeInt(num_unidades);
        dest.writeString(tipo_aplicacion);
        dest.writeString(uuid);
    }

    //getters y setters
    public String getUuid() {
        return uuid;
    }

    public int getId() {
        return id;
    }

    public long getFecha_aplicacion() {
        return fecha_aplicacion;
    }

    public String getTipo_insulina() {
        return tipo_insulina;
    }

    public int getNum_unidades() {
        return num_unidades;
    }

    public String getTipo_aplicacion() {
        return tipo_aplicacion;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha_aplicacion(long fecha_aplicacion) {
        this.fecha_aplicacion = fecha_aplicacion;
    }

    public void setTipo_insulina(String tipo_insulina) {
        this.tipo_insulina = tipo_insulina;
    }

    public void setNum_unidades(int num_unidades) {
        this.num_unidades = num_unidades;
    }

    public void setTipo_aplicacion(String tipo_aplicacion) {
        this.tipo_aplicacion = tipo_aplicacion;
    }

    //metodos de la interfaz itempantallainicio
    //metodo que devuelve la fecha de la insulina
    @Override
    public long getFechaItem() {
        return this.getFecha_aplicacion();
    }

    //metodo que devuelve la hora en un formato más amigable
    @Override
    public String getHora() {

        Calendar c=Calendar.getInstance();
        c.setTimeInMillis(this.getFechaItem());
        c.setTimeZone(TimeZone.getDefault());

        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
        String time = format.format(c.getTime());

        return time;
    }

    //metodo que devuelve el tipo del objeto, en este caso Insulina
    @Override
    public String getTipo() {
        return "Insulina";
    }

    //metodo que retorna una cadena con las características principales de la Insulina
    @Override
    public String getContent() {

        String content="";
        String lenguaje = Locale.getDefault().getLanguage();
        if(lenguaje.equals("en")){
            String tipoInsulina=this.getTipo_insulina();

            if(tipoInsulina.equals("Slow")||tipoInsulina.equals("Lenta")){
                this.tipo_insulina="Slow";
            }
            else{
                if(tipoInsulina.equals("Fast")||tipoInsulina.equals("Rápida")){
                    this.tipo_insulina="Fast";
                }
            }

            content=this.getTipo_insulina() + ", " +  this.getTipo_aplicacion().toLowerCase() + " application " + ", " + this.getNum_unidades()+" units";
        }
        if(lenguaje.equals("es")){
            String tipoInsulina=this.getTipo_insulina();

            if(tipoInsulina.equals("Slow")||tipoInsulina.equals("Lenta")){
                this.tipo_insulina="Lenta";
            }
            else{
                if(tipoInsulina.equals("Fast")||tipoInsulina.equals("Rápida")){
                    this.tipo_insulina="Rápida";
                }
            }
            content=this.getTipo_insulina() + ", " + "aplicación " + this.getTipo_aplicacion().toLowerCase() + ", " + this.getNum_unidades()+" uds.";
        }

        return content;

    }
}

