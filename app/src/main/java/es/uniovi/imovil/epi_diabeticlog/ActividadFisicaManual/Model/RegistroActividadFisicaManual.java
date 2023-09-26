package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model;


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

@Entity(tableName = "registro_actividad_fisica_manual_tabla")
public class  RegistroActividadFisicaManual implements Parcelable, ItemPantallaInicio {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private long fecha_inicio;
    private long fecha_fin;
    private String tipo_actividad;
    private String intensidad;
    private float kcal_quemadas;
    private String uuid;

    public RegistroActividadFisicaManual(){}

    //constructor
    @Ignore
    public RegistroActividadFisicaManual(int id, long fecha_inicio, long fecha_fin, String tipo_actividad, String intensidad, float kcal_quemadas) {
        this.id = id;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.tipo_actividad = tipo_actividad;
        this.intensidad = intensidad;
        this.kcal_quemadas = kcal_quemadas;
    }

    //constructor
    @Ignore
    public RegistroActividadFisicaManual(int id, long fecha_inicio, long fecha_fin, String tipo_actividad, String intensidad, float kcal_quemadas, String uuid) {
        this.id = id;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.tipo_actividad = tipo_actividad;
        this.intensidad = intensidad;
        this.kcal_quemadas = kcal_quemadas;
        this.uuid = uuid;
    }

    //getters y setters
    public String getUuid() {
        return uuid;
    }

    public int getId() {
        return id;
    }

    public long getFecha_inicio() {
        return fecha_inicio;
    }

    public long getFecha_fin() {
        return fecha_fin;
    }

    public String getTipo_actividad() {
        return tipo_actividad;
    }

    public String getIntensidad() {
        return intensidad;
    }

    public float getKcal_quemadas() {
        return kcal_quemadas;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha_inicio(long fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public void setFecha_fin(long fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public void setTipo_actividad(String tipo_actividad) {
        this.tipo_actividad = tipo_actividad;
    }

    public void setIntensidad(String intensidad) {
        this.intensidad = intensidad;
    }

    public void setKcal_quemadas(float kcal_quemadas) {
        this.kcal_quemadas = kcal_quemadas;
    }

    //metodos interfaz parcelable
    @Ignore
    protected RegistroActividadFisicaManual(Parcel in) {
        id = in.readInt();
        fecha_inicio = in.readLong();
        fecha_fin = in.readLong();
        tipo_actividad = in.readString();
        intensidad = in.readString();
        kcal_quemadas = in.readFloat();
        uuid=in.readString();
    }

    public static final Creator<RegistroActividadFisicaManual> CREATOR = new Creator<RegistroActividadFisicaManual>() {
        @Override
        public RegistroActividadFisicaManual createFromParcel(Parcel in) {
            return new RegistroActividadFisicaManual(in);
        }

        @Override
        public RegistroActividadFisicaManual[] newArray(int size) {
            return new RegistroActividadFisicaManual[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(fecha_inicio);
        dest.writeLong(fecha_fin);
        dest.writeString(tipo_actividad);
        dest.writeString(intensidad);
        dest.writeFloat(kcal_quemadas);
        dest.writeString(uuid);

    }

    //metodos interfaz itempantalla inicio
    @Override
    public long getFechaItem() {
        return this.getFecha_inicio();
    }

    //metodo que retorna la hora de registro en un formato amigable
    @Override
    public String getHora() {
        Calendar c=Calendar.getInstance();
        c.setTimeInMillis(this.getFecha_inicio());
        c.setTimeZone(TimeZone.getDefault());

        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
        String time = format.format(c.getTime());

        return time;
    }

    //este método devuelve un string que identifica este elemento
    @Override
    public String getTipo() {
        return "Registro";
    }

    //metodo que devuelve un resumen de los elementos principales del registro de actividad física manual
    @Override
    public String getContent() {
        String content="";
        String lenguaje = Locale.getDefault().getLanguage();

        if(lenguaje.equals("en")){
            String tipoActividad=this.getTipo_actividad();
            if(tipoActividad.equals("Correr")||tipoActividad.equals("Running")){
                this.tipo_actividad="Running";
            }
            else{
                if(tipoActividad.equals("Caminar")||tipoActividad.equals("Walking")){
                    this.tipo_actividad="Walking";
                }
                else{
                    if(tipoActividad.equals("Nadar")||tipoActividad.equals("Swimming")){
                        this.tipo_actividad="Swimming";
                    }
                    else{
                        if(tipoActividad.equals("Fútbol")||tipoActividad.equals("Football")){
                            this.tipo_actividad="Football";
                        }
                        else{
                            if(tipoActividad.equals("Baloncesto")||tipoActividad.equals("Basketball")){
                                this.tipo_actividad="Basketball";
                            }
                            else{
                                if(tipoActividad.equals("Gimnasio")||tipoActividad.equals("Gym")){
                                    this.tipo_actividad="Gym";
                                }
                                else{
                                    if(tipoActividad.equals("Hockey")||tipoActividad.equals("Hockey")){
                                        this.tipo_actividad="Hockey";
                                    }
                                    else{
                                        if(tipoActividad.equals("Tenis")||tipoActividad.equals("Tennis")){
                                            this.tipo_actividad="Tennis";
                                        }
                                        else{
                                            if(tipoActividad.equals("Pádel")||tipoActividad.equals("Padel")){
                                                this.tipo_actividad="Padel";
                                            }
                                            else{
                                                if(tipoActividad.equals("Ping pong")||tipoActividad.equals("Ping pong")){
                                                    this.tipo_actividad="Ping pong";
                                                }
                                                else{
                                                    if(tipoActividad.equals("Otra")||tipoActividad.equals("Other")){
                                                        this.tipo_actividad="Other";
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            String intensidad=this.getIntensidad();
            if(intensidad.equals("High")||intensidad.equals("Alta")){
                this.intensidad="High";
            }
            else{
                if(intensidad.equals("Low")||intensidad.equals("Baja")){
                    this.intensidad="Low";
                }
                else{
                    if(intensidad.equals("Medium")||intensidad.equals("Media")){
                        this.intensidad="Medium";
                    }
                }
            }

            content=this.getTipo_actividad() + ", "+ "intensity "+this.getIntensidad().toLowerCase() + ", " + this.getKcal_quemadas() + " kcal ";
        }
        if(lenguaje.equals("es")){
            String tipoActividad=this.getTipo_actividad();
            if(tipoActividad.equals("Correr")||tipoActividad.equals("Running")){
                this.tipo_actividad="Correr";
            }
            else{
                if(tipoActividad.equals("Caminar")||tipoActividad.equals("Walking")){
                    this.tipo_actividad="Caminar";
                }
                else{
                    if(tipoActividad.equals("Nadar")||tipoActividad.equals("Swimming")){
                        this.tipo_actividad="Nadar";
                    }
                    else{
                        if(tipoActividad.equals("Fútbol")||tipoActividad.equals("Football")){
                            this.tipo_actividad="Fútbol";
                        }
                        else{
                            if(tipoActividad.equals("Baloncesto")||tipoActividad.equals("Basketball")){
                                this.tipo_actividad="Baloncesto";
                            }
                            else{
                                if(tipoActividad.equals("Gimnasio")||tipoActividad.equals("Gym")){
                                    this.tipo_actividad="Gimnasio";
                                }
                                else{
                                    if(tipoActividad.equals("Hockey")||tipoActividad.equals("Hockey")){
                                        this.tipo_actividad="Hockey";
                                    }
                                    else{
                                        if(tipoActividad.equals("Tenis")||tipoActividad.equals("Tennis")){
                                            this.tipo_actividad="Tenis";
                                        }
                                        else{
                                            if(tipoActividad.equals("Pádel")||tipoActividad.equals("Padel")){
                                                this.tipo_actividad="Pádel";
                                            }
                                            else{
                                                if(tipoActividad.equals("Ping pong")||tipoActividad.equals("Ping pong")){
                                                    this.tipo_actividad="Ping pong";
                                                }
                                                else{
                                                    if(tipoActividad.equals("Otra")||tipoActividad.equals("Other")){
                                                        this.tipo_actividad="Otra";
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            String intensidad=this.getIntensidad();
            if(intensidad.equals("High")||intensidad.equals("Alta")){
                this.intensidad="Alta";
            }
            else{
                if(intensidad.equals("Low")||intensidad.equals("Baja")){
                    this.intensidad="Baja";
                }
                else{
                    if(intensidad.equals("Medium")||intensidad.equals("Media")){
                        this.intensidad="Media";
                    }
                }
            }
            content=this.getTipo_actividad() + ", "+ "intensidad "+this.getIntensidad().toLowerCase() + ", " + this.getKcal_quemadas() + " kcal ";
        }

        return content;
    }
}

