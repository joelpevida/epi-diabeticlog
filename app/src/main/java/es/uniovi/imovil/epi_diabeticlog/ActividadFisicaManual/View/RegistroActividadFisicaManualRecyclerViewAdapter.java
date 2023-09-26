package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.View;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManual;
import es.uniovi.imovil.epi_diabeticlog.R;

public class RegistroActividadFisicaManualRecyclerViewAdapter extends RecyclerView.Adapter<RegistroActividadFisicaManualRecyclerViewAdapter.ViewHolder>{

    private final LayoutInflater mlayoutInflater;
    public static List<RegistroActividadFisicaManual> registros;
    public static CallbacksRegistroActividadFisicaManualActivity callbacks_registro_actividad_fisica_manual_activity;

    //constructor
    public RegistroActividadFisicaManualRecyclerViewAdapter(Context context, Fragment fragment) {
        this.mlayoutInflater = LayoutInflater.from(context);
        if (fragment instanceof CallbacksRegistroActividadFisicaManualActivity)
            callbacks_registro_actividad_fisica_manual_activity = (CallbacksRegistroActividadFisicaManualActivity) fragment;
    }

    //setter de los registros
    public void setRegistros(List<RegistroActividadFisicaManual> registros) {
        this.registros = registros;
        notifyDataSetChanged();
    }

    //metodo creador del viewholder
    @Override
    public RegistroActividadFisicaManualRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_registro_actividad_fisica_manual,viewGroup,false);
        return new RegistroActividadFisicaManualRecyclerViewAdapter.ViewHolder(view);
    }

    //metodo para hacer el bind del viewholder en la posicion establecida
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(RegistroActividadFisicaManualRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        RegistroActividadFisicaManual aux=registros.get(i);
        viewHolder.bind(aux);
    }

    //metodo que retorna el tamaño de los registros
    @Override
    public int getItemCount() {
        if(registros!=null)
            return registros.size();
        else
            return 0;
    }

    protected class ViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tipo_actividad;
        TextView intensidad_actividad;
        TextView kcal_quemadas;
        TextView hora_inicio_actividad;
        TextView hora_final_actividad;

        public Button btn_modificar_registro;
        public Button btn_eliminar_registro;

        //metodo que establece los elementos de la vista con los valores del registro de actividad fisica manual auxiliar que recibe
        public void bind (RegistroActividadFisicaManual aux) {

            String lenguaje = Locale.getDefault().getLanguage();
            if(lenguaje.equals("en")){
                String tipoActividad=aux.getTipo_actividad();
                if(tipoActividad.equals("Correr")||tipoActividad.equals("Running")){
                    aux.setTipo_actividad("Running");
                }
                else{
                    if(tipoActividad.equals("Caminar")||tipoActividad.equals("Walking")){
                        aux.setTipo_actividad("Walking");
                    }
                    else{
                        if(tipoActividad.equals("Nadar")||tipoActividad.equals("Swimming")){
                            aux.setTipo_actividad("Swimming");
                        }
                        else{
                            if(tipoActividad.equals("Fútbol")||tipoActividad.equals("Football")){
                                aux.setTipo_actividad("Football");
                            }
                            else{
                                if(tipoActividad.equals("Baloncesto")||tipoActividad.equals("Basketball")){
                                    aux.setTipo_actividad("Basketball");
                                }
                                else{
                                    if(tipoActividad.equals("Gimnasio")||tipoActividad.equals("Gym")){
                                        aux.setTipo_actividad("Gym");
                                    }
                                    else{
                                        if(tipoActividad.equals("Hockey")||tipoActividad.equals("Hockey")){
                                            aux.setTipo_actividad("Hockey");
                                        }
                                        else{
                                            if(tipoActividad.equals("Tenis")||tipoActividad.equals("Tennis")){
                                                aux.setTipo_actividad("Tennis");
                                            }
                                            else{
                                                if(tipoActividad.equals("Pádel")||tipoActividad.equals("Padel")){
                                                    aux.setTipo_actividad("Padel");
                                                }
                                                else{
                                                    if(tipoActividad.equals("Ping pong")||tipoActividad.equals("Ping pong")){
                                                        aux.setTipo_actividad("Ping pong");
                                                    }
                                                    else{
                                                        if(tipoActividad.equals("Otra")||tipoActividad.equals("Other")){
                                                            aux.setTipo_actividad("Other");
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
                String intensidad=aux.getIntensidad();
                if(intensidad.equals("High")||intensidad.equals("Alta")){
                    aux.setIntensidad("High");
                }
                else{
                    if(intensidad.equals("Low")||intensidad.equals("Baja")){
                        aux.setIntensidad("Low");
                    }
                    else{
                        if(intensidad.equals("Medium")||intensidad.equals("Media")){
                            aux.setIntensidad("Medium");
                        }
                    }
                }

            }
            if(lenguaje.equals("es")){
                String tipoActividad=aux.getTipo_actividad();
                if(tipoActividad.equals("Correr")||tipoActividad.equals("Running")){
                    aux.setTipo_actividad("Correr");
                }
                else{
                    if(tipoActividad.equals("Caminar")||tipoActividad.equals("Walking")){
                        aux.setTipo_actividad("Caminar");
                    }
                    else{
                        if(tipoActividad.equals("Nadar")||tipoActividad.equals("Swimming")){
                            aux.setTipo_actividad("Nadar");
                        }
                        else{
                            if(tipoActividad.equals("Fútbol")||tipoActividad.equals("Football")){
                                aux.setTipo_actividad("Fútbol");
                            }
                            else{
                                if(tipoActividad.equals("Baloncesto")||tipoActividad.equals("Basketball")){
                                    aux.setTipo_actividad("Baloncesto");
                                }
                                else{
                                    if(tipoActividad.equals("Gimnasio")||tipoActividad.equals("Gym")){
                                        aux.setTipo_actividad("Gimnasio");
                                    }
                                    else{
                                        if(tipoActividad.equals("Hockey")||tipoActividad.equals("Hockey")){
                                            aux.setTipo_actividad("Hockey");
                                        }
                                        else{
                                            if(tipoActividad.equals("Tenis")||tipoActividad.equals("Tennis")){
                                                aux.setTipo_actividad("Tenis");
                                            }
                                            else{
                                                if(tipoActividad.equals("Pádel")||tipoActividad.equals("Padel")){
                                                    aux.setTipo_actividad("Pádel");
                                                }
                                                else{
                                                    if(tipoActividad.equals("Ping pong")||tipoActividad.equals("Ping pong")){
                                                        aux.setTipo_actividad("Ping pong");
                                                    }
                                                    else{
                                                        if(tipoActividad.equals("Otra")||tipoActividad.equals("Other")){
                                                            aux.setTipo_actividad("Otra");
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
                String intensidad=aux.getIntensidad();
                if(intensidad.equals("High")||intensidad.equals("Alta")){
                    aux.setIntensidad("Alta");
                }
                else{
                    if(intensidad.equals("Low")||intensidad.equals("Baja")){
                        aux.setIntensidad("Baja");
                    }
                    else{
                        if(intensidad.equals("Medium")||intensidad.equals("Media")){
                            aux.setIntensidad("Media");
                        }
                    }
                }
            }

            tipo_actividad.setText(aux.getTipo_actividad());
            intensidad_actividad.setText(aux.getIntensidad());
            kcal_quemadas.setText(String.valueOf(aux.getKcal_quemadas()));

            //hora de inicio de la actividad
            Calendar fecha_inicio= Calendar.getInstance();
            fecha_inicio.setTimeInMillis(aux.getFecha_inicio());

            SimpleDateFormat format= new SimpleDateFormat("k:mm a");
            String  time_inicio = format.format(fecha_inicio.getTime());
            hora_inicio_actividad.setText(time_inicio);

            //hora de fin de la actividad
            Calendar fecha_fin= Calendar.getInstance();
            fecha_fin.setTimeInMillis(aux.getFecha_fin());

            String  time_fin = format.format(fecha_fin.getTime());
            hora_final_actividad.setText(time_fin);

        }

        //constructor del viewholder
        public ViewHolder(View itemView) {
            super(itemView);
            // Inicializar los dos TextView
            tipo_actividad=itemView.findViewById(R.id.tipo_actividad);
            intensidad_actividad=itemView.findViewById(R.id.intensidad_actividad);
            kcal_quemadas=itemView.findViewById(R.id.kcal_quemadas);
            hora_inicio_actividad=itemView.findViewById(R.id.hora_inicio_actividad);
            hora_final_actividad=itemView.findViewById(R.id.hora_final_actividad);

            btn_modificar_registro=itemView.findViewById(R.id.btn_modificar_registro);
            btn_eliminar_registro=itemView.findViewById(R.id.btn_eliminar_registro);

            btn_modificar_registro.setOnClickListener(this);
            btn_eliminar_registro.setOnClickListener(this);
        }

        //metodo onclick que se ejecuta al pulsar el boton para borrar y/o modificar el registro de actividad fisica manual
        @Override
        public void onClick(View v) {

            if(v.getId()==R.id.btn_modificar_registro){
                int posicion=this.getLayoutPosition();
                RegistroActividadFisicaManual seleccionado = RegistroActividadFisicaManualRecyclerViewAdapter.registros.get(posicion);
                callbacks_registro_actividad_fisica_manual_activity.modifySelected(seleccionado);
            }
            if(v.getId()==R.id.btn_eliminar_registro){
                int posicion=this.getLayoutPosition();
                RegistroActividadFisicaManual seleccionado = RegistroActividadFisicaManualRecyclerViewAdapter.registros.get(posicion);
                callbacks_registro_actividad_fisica_manual_activity.removeSelected(seleccionado);
            }
        }
    }
}

