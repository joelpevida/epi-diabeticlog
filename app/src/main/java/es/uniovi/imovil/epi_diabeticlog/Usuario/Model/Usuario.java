package es.uniovi.imovil.epi_diabeticlog.Usuario.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuario_tabla")
public class Usuario implements Parcelable {

    @PrimaryKey
    @NonNull
    private String uuid;

    String nombre;
    private int altura_cm;
    private float peso;
    private String pin;

    //constructor
    public Usuario() {
    }

    //constructor
    @Ignore
    public Usuario(String uuid, String nombre, int altura_cm, float peso, String pin) {
        this.uuid = uuid;
        this.nombre=nombre;
        this.altura_cm = altura_cm;
        this.peso = peso;
        this.pin = pin;
    }

    //getters y setters
    public String getUuid() {
        return uuid;
    }

    public String getNombre() {
        return nombre;
    }

    public int getAltura_cm() {
        return altura_cm;
    }

    public float getPeso() {
        return peso;
    }

    public String getPin() {
        return pin;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAltura_cm(int altura_cm) {
        this.altura_cm = altura_cm;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    //metodos de la interfaz parcelable
    @Ignore
    protected Usuario(Parcel in) {
        this.uuid = in.readString();
        this.nombre = in.readString();
        this.altura_cm = in.readInt();
        this.peso = in.readFloat();
        this.pin = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(nombre);
        dest.writeInt(altura_cm);
        dest.writeFloat(peso);
        dest.writeString(pin);
    }
}
