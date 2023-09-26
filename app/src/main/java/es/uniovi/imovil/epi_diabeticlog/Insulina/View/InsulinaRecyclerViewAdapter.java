package es.uniovi.imovil.epi_diabeticlog.Insulina.View;


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

import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;
import es.uniovi.imovil.epi_diabeticlog.R;

public class InsulinaRecyclerViewAdapter
        extends RecyclerView.Adapter<InsulinaRecyclerViewAdapter.ViewHolder>  {


    private final LayoutInflater mlayoutInflater;
    public static List<Insulina> insulinas;
    public static CallbacksInsulinaActivity callbacks_insulina_activity;

    //constructor
    public InsulinaRecyclerViewAdapter(Context context, Fragment fragment) {
        this.mlayoutInflater = LayoutInflater.from(context);
        if (fragment instanceof CallbacksInsulinaActivity)
            callbacks_insulina_activity = (CallbacksInsulinaActivity) fragment;
    }

    //metodo creador del viewholder
    @Override
    public InsulinaRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_aplicacion_insulina_layout,viewGroup,false);
        return new InsulinaRecyclerViewAdapter.ViewHolder(view);
    }

    //metodo para hacer el bind del viewholder en la posicion establecida
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(InsulinaRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        Insulina aux=insulinas.get(i);
        viewHolder.bind(aux);
    }

    //metodo que retorna el tamaño del listado de insulinas
    @Override
    public int getItemCount() {
        if(insulinas!=null)
            return insulinas.size();
        else
            return 0;
    }

    //setter para las insulinas
    public void setInsulinas(List<Insulina> insulinas) {
        this.insulinas = insulinas;
        notifyDataSetChanged();
    }

    protected static class ViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tipo_aplicacion;
        public TextView tipo_insulina;
        public TextView hora;
        public TextView num_unidades;

        public Button btn_modificar_aplicacion_insulina;
        public Button btn_eliminar_aplicacion_insulina;

        //constructor
        public ViewHolder(View itemView) {
            super(itemView);
            // Inicializar los dos TextView
            tipo_aplicacion=itemView.findViewById(R.id.tipo_aplicacion_item);
            tipo_insulina=itemView.findViewById(R.id.tipo_insulina_item);
            hora=itemView.findViewById(R.id.hora_aplicacion_item);
            num_unidades=itemView.findViewById(R.id.num_unidades_item);

            btn_modificar_aplicacion_insulina=itemView.findViewById(R.id.btn_modificar_aplicacion_insulina);
            btn_eliminar_aplicacion_insulina=itemView.findViewById(R.id.btn_eliminar_aplicacion_insulina);

            btn_modificar_aplicacion_insulina.setOnClickListener(this);
            btn_eliminar_aplicacion_insulina.setOnClickListener(this);
        }

        //metodo onclick que se ejecuta al pulsar el boton para borrar y/o modificar la insulina
        @Override
        public void onClick(View v) {

            if(v.getId()== R.id.btn_modificar_aplicacion_insulina){
                int posicion=this.getLayoutPosition();
                Insulina seleccionada = InsulinaRecyclerViewAdapter.insulinas.get(posicion);
                callbacks_insulina_activity.modifySelected(seleccionada);
            }
            if(v.getId()==R.id.btn_eliminar_aplicacion_insulina){
                int posicion=this.getLayoutPosition();
                Insulina seleccionada = InsulinaRecyclerViewAdapter.insulinas.get(posicion);
                callbacks_insulina_activity.removeSelected(seleccionada);
            }
        }

        //metodo que establece los elementos de la vista con los valores de la insulina auxiliar que recibe
        public void bind(Insulina aux){

            String tipoInsulina=aux.getTipo_insulina();
            String tipoAplicacion=aux.getTipo_aplicacion();

            String lenguaje = Locale.getDefault().getLanguage();
            if(lenguaje.equals("en")){
                if(tipoInsulina.equals("Rapida")||tipoInsulina.equals("Fast")){
                    aux.setTipo_insulina("Fast");
                }
                else{
                    if(tipoInsulina.equals("Lenta")||tipoInsulina.equals("Slow")){
                        aux.setTipo_insulina("Slow");
                    }
                    else{
                        if(tipoInsulina.equals("Intermedia")||tipoInsulina.equals("Intermediate")){
                            aux.setTipo_insulina("Intermediate");
                        }
                    }
                }

                if(tipoAplicacion.equals("Normal")||tipoInsulina.equals("Normal")){
                    aux.setTipo_aplicacion("Normal");
                }
                else{
                    if(tipoInsulina.equals("Extra")||tipoInsulina.equals("Extra")){
                        aux.setTipo_aplicacion("Extra");
                    }
                }
            }
            if(lenguaje.equals("es")){
                if(tipoInsulina.equals("Rapida")||tipoInsulina.equals("Fast")){
                    aux.setTipo_insulina("Rapida");
                }
                else{
                    if(tipoInsulina.equals("Lenta")||tipoInsulina.equals("Slow")){
                        aux.setTipo_insulina("Lenta");
                    }
                    else{
                        if(tipoInsulina.equals("Intermedia")||tipoInsulina.equals("Intermediate")){
                            aux.setTipo_insulina("Intermedia");
                        }
                    }
                }

                if(tipoAplicacion.equals("Normal")||tipoInsulina.equals("Normal")){
                    aux.setTipo_aplicacion("Normal");
                }
                else{
                    if(tipoInsulina.equals("Extra")||tipoInsulina.equals("Extra")){
                        aux.setTipo_aplicacion("Extra");
                    }
                }
            }

            tipo_aplicacion.setText(aux.getTipo_aplicacion());
            tipo_insulina.setText(aux.getTipo_insulina());

            Calendar fecha_aplicacion= Calendar.getInstance();
            fecha_aplicacion.setTimeInMillis(aux.getFecha_aplicacion());

            SimpleDateFormat format= new SimpleDateFormat("k:mm a");
            String  time = format.format(fecha_aplicacion.getTime());
            hora.setText(time);

            num_unidades.setText(String.valueOf(aux.getNum_unidades()));

        }
    }
}

