package es.uniovi.imovil.epi_diabeticlog.Glucosa.View;

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

import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;
import es.uniovi.imovil.epi_diabeticlog.R;

public class GlucosaRecyclerViewAdapter  extends RecyclerView.Adapter<GlucosaRecyclerViewAdapter.ViewHolder>  {

    private final LayoutInflater mlayoutInflater;
    public static List<Glucosa> glucosas;
    public static CallbacksGlucosaActivity callbacksGlucosaActivity;

    //constructor
    public GlucosaRecyclerViewAdapter(Context context, Fragment fragment) {
        this.mlayoutInflater = LayoutInflater.from(context);
        if (fragment instanceof CallbacksGlucosaActivity)
            callbacksGlucosaActivity = (CallbacksGlucosaActivity) fragment;
    }

    //metodo creador del viewholder
    @Override
    public GlucosaRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_glucosa,viewGroup,false);
        return new GlucosaRecyclerViewAdapter.ViewHolder(view);
    }

    //metodo para hacer el bind del viewholder en la posicion establecida
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(GlucosaRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        Glucosa aux=glucosas.get(i);
        viewHolder.bind(aux);
    }

    //metodo que retorna el tamaño del listado de glucosas
    @Override
    public int getItemCount() {
        if(glucosas!=null)
            return glucosas.size();
        else
            return 0;
    }

    //setter para las glucosas
    public void setGlucosas(List<Glucosa> glucosas) {
        this.glucosas = glucosas;
        notifyDataSetChanged();
    }

    protected static class ViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mgDl;
        public TextView horaMedicion;

        public Button btn_modificar_glucosa;
        public Button btn_eliminar_glucosa;

        //constructor
        public ViewHolder(View itemView) {
            super(itemView);
            // Inicializar los dos TextView
            mgDl=itemView.findViewById(R.id.mgDl);
            horaMedicion=itemView.findViewById(R.id.horaMedicion);

            btn_modificar_glucosa=itemView.findViewById(R.id.btn_modificar_glucosa);
            btn_eliminar_glucosa=itemView.findViewById(R.id.btn_eliminar_glucosa);

            btn_modificar_glucosa.setOnClickListener(this);
            btn_eliminar_glucosa.setOnClickListener(this);
        }

        //metodo onclick que se ejecuta al pulsar el boton para borrar y/o modificar la glucosa
        @Override
        public void onClick(View v) {

            if(v.getId()== R.id.btn_modificar_glucosa){
                int posicion=this.getLayoutPosition();
                Glucosa seleccionada = GlucosaRecyclerViewAdapter.glucosas.get(posicion);
                callbacksGlucosaActivity.modifySelected(seleccionada);
            }
            if(v.getId()==R.id.btn_eliminar_glucosa){
                int posicion=this.getLayoutPosition();
                Glucosa seleccionada = GlucosaRecyclerViewAdapter.glucosas.get(posicion);
                callbacksGlucosaActivity.removeSelected(seleccionada);
            }
        }
        //metodo que establece los elementos de la vista con los valores de la glucosa auxiliar que recibe
        public void bind(Glucosa aux){

            mgDl.setText(String.valueOf(aux.getMgDl()));

            Calendar fecha_medicion= Calendar.getInstance();
            fecha_medicion.setTimeInMillis(aux.getFecha_medicion());

            SimpleDateFormat format= new SimpleDateFormat("k:mm a");
            String  time = format.format(fecha_medicion.getTime());
            horaMedicion.setText(time);

        }
    }
}
