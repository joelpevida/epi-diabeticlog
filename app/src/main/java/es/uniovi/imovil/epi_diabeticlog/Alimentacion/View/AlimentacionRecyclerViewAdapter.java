package es.uniovi.imovil.epi_diabeticlog.Alimentacion.View;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.Ingesta;
import es.uniovi.imovil.epi_diabeticlog.R;

public class AlimentacionRecyclerViewAdapter
        extends RecyclerView.Adapter<AlimentacionRecyclerViewAdapter.ViewHolder>  {

    private final LayoutInflater mlayoutInflater;
    public static List<Ingesta> ingestas;
    public static CallbacksAlimentacionActivity callbacks_alimentacion_activity;

    //constructor
    public AlimentacionRecyclerViewAdapter(Context context, Fragment fragment) {
        this.mlayoutInflater = LayoutInflater.from(context);
        if (fragment instanceof CallbacksAlimentacionActivity)
            callbacks_alimentacion_activity = (CallbacksAlimentacionActivity) fragment;
    }

    //setter para las ingestas
    public void setIngestas(List<Ingesta> ingestas) {
        this.ingestas = ingestas;
        notifyDataSetChanged();
    }

    //metodo creador del viewholder
    @Override
    public AlimentacionRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ingesta_layout,viewGroup,false);
        return new AlimentacionRecyclerViewAdapter.ViewHolder(view);
    }

    //metodo para hacer el bind del viewholder en la posicion establecida
    @Override
    public void onBindViewHolder(AlimentacionRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        // Obtener el curso Actual
        Ingesta aux=this.ingestas.get(i);
        viewHolder.bind(aux);

    }

    //metodo que retorna el tamaño del listado de ingestas
    @Override
    public int getItemCount() {
        if(ingestas!=null)
            return ingestas.size();
        else
            return 0;
    }

    protected static class ViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nombre_ingesta;
        public TextView num_raciones_ingesta;
        public Button btn_modificar_ingesta;
        public Button btn_eliminar_ingesta;
        public TextView hora_ingesta_etiqueta;

        //constructor
        public ViewHolder(View itemView) {
            super(itemView);
            // Inicializar los dos TextView
            nombre_ingesta=itemView.findViewById(R.id.nombre_ingesta_etiqueta);
            num_raciones_ingesta=itemView.findViewById(R.id.num_raciones_ingesta_etiqueta);
            btn_modificar_ingesta=itemView.findViewById(R.id.btn_modificar_ingesta);
            btn_eliminar_ingesta=itemView.findViewById(R.id.btn_eliminar_ingesta);
            hora_ingesta_etiqueta=itemView.findViewById(R.id.hora_ingesta_etiqueta);

            btn_modificar_ingesta.setOnClickListener(this);
            btn_eliminar_ingesta.setOnClickListener(this);
        }

        //metodo onclick que se ejecuta al pulsar el boton para borrar y/o modificar la ingesta
        @Override
        public void onClick(View v) {

            if(v.getId()==R.id.btn_modificar_ingesta){
                int posicion=this.getLayoutPosition();
                Ingesta seleccionada = AlimentacionRecyclerViewAdapter.ingestas.get(posicion);
                callbacks_alimentacion_activity.modifySelected(seleccionada);
            }
            if(v.getId()==R.id.btn_eliminar_ingesta){
                int posicion=this.getLayoutPosition();
                Ingesta seleccionada = AlimentacionRecyclerViewAdapter.ingestas.get(posicion);
                callbacks_alimentacion_activity.removeSelected(seleccionada);
            }
        }

        //metodo que establece los elementos de la vista con los valores de la ingesta auxiliar que recibe
        public void bind(Ingesta aux) {

            String lenguaje = Locale.getDefault().getLanguage();
            if(lenguaje.equals("en")){
                String nombreIngesta=aux.getTipo_ingesta();
                if(nombreIngesta.equals("Desayuno")||nombreIngesta.equals("Breakfast")){
                    aux.setTipo_ingesta("Breakfast");
                }
                else{
                    if(nombreIngesta.equals("Media mañana")||nombreIngesta.equals("Mid-morning snack")){
                        aux.setTipo_ingesta("Mid-morning snack");
                    }
                    else{
                        if(nombreIngesta.equals("Comida")||nombreIngesta.equals("Lunch")){
                            aux.setTipo_ingesta("Lunch");
                        }
                        else{
                            if(nombreIngesta.equals("Merienda")||nombreIngesta.equals("Mid-afternoon snack")){
                                aux.setTipo_ingesta("Mid-afternoon snack");
                            }
                            else{
                                if(nombreIngesta.equals("Cena")||nombreIngesta.equals("Dinner")){
                                    aux.setTipo_ingesta("Dinner");
                                }
                            }
                        }
                    }
                }
                num_raciones_ingesta.setText(String.valueOf(aux.getNum_raciones())+" portions");
            }
            if(lenguaje.equals("es")){
                String nombreIngesta=aux.getTipo_ingesta();
                if(nombreIngesta.equals("Desayuno")||nombreIngesta.equals("Breakfast")){
                    aux.setTipo_ingesta("Desayuno");
                }
                else{
                    if(nombreIngesta.equals("Media mañana")||nombreIngesta.equals("Mid-morning snack")){
                        aux.setTipo_ingesta("Media mañana");
                    }
                    else{
                        if(nombreIngesta.equals("Comida")||nombreIngesta.equals("Lunch")){
                            aux.setTipo_ingesta("Comida");
                        }
                        else{
                            if(nombreIngesta.equals("Merienda")||nombreIngesta.equals("Mid-afternoon snack")){
                                aux.setTipo_ingesta("Merienda");
                            }
                            else{
                                if(nombreIngesta.equals("Cena")||nombreIngesta.equals("Dinner")){
                                    aux.setTipo_ingesta("Cena");
                                }
                            }
                        }
                    }
                }
                num_raciones_ingesta.setText(String.valueOf(aux.getNum_raciones())+" raciones");
            }
            nombre_ingesta.setText(aux.getTipo_ingesta());

            Calendar fecha_ingesta= Calendar.getInstance();
            fecha_ingesta.setTimeInMillis(aux.getFecha());

            SimpleDateFormat format= new SimpleDateFormat("k:mm a");
            String time = format.format(fecha_ingesta.getTime());
            hora_ingesta_etiqueta.setText(time);
        }
    }
}
