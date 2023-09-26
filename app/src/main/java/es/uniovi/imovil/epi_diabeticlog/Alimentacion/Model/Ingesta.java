package es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model;

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

@Entity(tableName = "ingesta_tabla")
public class Ingesta implements Parcelable, ItemPantallaInicio {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String tipo_ingesta;
    private float num_raciones;
    private long fecha;
    private String uuid;

    //constructor por defecto
    public Ingesta(){}

    //constructor
    @Ignore
    public Ingesta(String tipo_ingesta, float num_raciones, long fecha){
        this.tipo_ingesta = tipo_ingesta;
        this.num_raciones=num_raciones;
        this.fecha=fecha;
    }
    //constructor
    @Ignore
    public Ingesta(String tipo_ingesta, float num_raciones, long fecha, String uuid){
        this.tipo_ingesta = tipo_ingesta;
        this.num_raciones=num_raciones;
        this.fecha=fecha;
        this.uuid=uuid;
    }

    //metodos de la interfaz parcelable
    @Ignore
    protected Ingesta(Parcel in){
        this.id=in.readInt();
        this.tipo_ingesta =in.readString();
        this.num_raciones=in.readFloat();
        this.fecha=in.readLong();
        this.uuid=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.id);
        dest.writeString(this.tipo_ingesta);
        dest.writeFloat(this.num_raciones);
        dest.writeLong(this.fecha);
        dest.writeString(uuid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingesta> CREATOR = new Creator<Ingesta>() {
        @Override
        public Ingesta createFromParcel(Parcel in) {
            return new Ingesta(in);
        }

        @Override
        public Ingesta[] newArray(int size) {
            return new Ingesta[size];
        }
    };

    //getters y setters
    public String getUuid() {
        return uuid;
    }

    public String getTipo_ingesta() {
        return tipo_ingesta;
    }

    public float getNum_raciones() {
        return num_raciones;
    }

    public long getFecha() {
        return fecha;
    }

    public int getId() {
        return id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNum_raciones(float num_raciones) {
        this.num_raciones = num_raciones;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public void setTipo_ingesta(String tipo_ingesta) {
        this.tipo_ingesta = tipo_ingesta;
    }

    //metodos de la interfaz itempantallainicio

    //retorna la fecha
    @Override
    public long getFechaItem() {
        return this.getFecha();
    }

    //metodo que retorna la hora en un formato amigavke
    @Override
    public String getHora() {
        Calendar c=Calendar.getInstance();
        c.setTimeInMillis(this.getFecha());
        c.setTimeZone(TimeZone.getDefault());

        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
        String time = format.format(c.getTime());

        return time;
    }

    //este método devuelve un string que identifica este elemento
    @Override
    public String getTipo() {
        return "Ingesta";
    }

    //metodo que devuelve un resumen de los elementos principales de la ingesta
    @Override
    public String getContent() {
        String content="";
        String lenguaje = Locale.getDefault().getLanguage();


        if(lenguaje.equals("es")){
            String nombreingesta=this.getTipo_ingesta();
            if(nombreingesta.equals("Breakfast")||nombreingesta.equals("Desayuno")){
                this.tipo_ingesta ="Desayuno";
            }
            else{
                if(nombreingesta.equals("Mid-morning snack")||nombreingesta.equals("Media mañana")){
                    this.tipo_ingesta ="Media mañana";
                }
                else{
                    if(nombreingesta.equals("Lunch")||nombreingesta.equals("Comida")){
                        this.tipo_ingesta ="Comida";
                    }
                    else{
                        if(nombreingesta.equals("Mid-afternoon snack")||nombreingesta.equals("Merienda")){
                            this.tipo_ingesta ="Merienda";
                        }
                        else{
                            if(nombreingesta.equals("Dinner")||nombreingesta.equals("Cena")){
                                this.tipo_ingesta ="Cena";
                            }
                        }
                    }
                }
            }

            content=this.tipo_ingesta + ", " + this.getNum_raciones() + " raciones";
        }
        if(lenguaje.equals("en")){
            String nombreingesta=this.getTipo_ingesta();

            if(nombreingesta.equals("Desayuno")){
                this.tipo_ingesta ="Breakfast";
            }
            else{
                if(nombreingesta.equals("Media mañana")){
                    this.tipo_ingesta ="Mid-morning snack";
                }
                else{
                    if(nombreingesta.equals("Comida")){
                        this.tipo_ingesta ="Lunch";
                    }
                    else{
                        if(nombreingesta.equals("Merienda")){
                            this.tipo_ingesta ="Mid-afternoon snack";
                        }
                        else{
                            if(nombreingesta.equals("Cena")){
                                this.tipo_ingesta ="Dinner";
                            }
                        }
                    }
                }
            }
            content=this.getTipo_ingesta() + ", " + this.getNum_raciones() + " portions";
        }

        return content;
    }

}

