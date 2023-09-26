package es.uniovi.imovil.epi_diabeticlog.PantallaInicio.View;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManual;
import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.Ingesta;
import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;
import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.ItemPantallaInicioBinding;


public class PantallaInicioRecyclerViewAdapter extends RecyclerView.Adapter<PantallaInicioRecyclerViewAdapter.ViewHolder>  {

    private final LayoutInflater mlayoutInflater;
    private Context context;
    public static List<ItemPantallaInicio> itemsPantallaInicio;
    private ItemPantallaInicioBinding binding;

    //constructor
    public PantallaInicioRecyclerViewAdapter(Context context, Fragment fragment) {
        this.context=context;
        this.mlayoutInflater = LayoutInflater.from(context);
    }

    //metodo creador del viewholder
    @Override
    public PantallaInicioRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        binding= ItemPantallaInicioBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new PantallaInicioRecyclerViewAdapter.ViewHolder(this.binding, this.context);
    }

    //metodo para hacer el bind del viewholder en la posicion establecida
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(PantallaInicioRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        ItemPantallaInicio aux= itemsPantallaInicio.get(i);
        viewHolder.bind(aux);
    }

    //metodo que retorna el tamaño del listado de items
    @Override
    public int getItemCount() {
        if(itemsPantallaInicio!=null)
            return itemsPantallaInicio.size();
        else
            return 0;

    }

    //metodo para eliminar el listado de elementos
    public void removeElementos(){
        if(itemsPantallaInicio!=null) {
            itemsPantallaInicio.clear();
        }
    }

    //metodo para añadir un listado de glucosas al listado de items
    public void addGlucosas(List<Glucosa> glucosas) {
        if(itemsPantallaInicio==null){
            itemsPantallaInicio=new LinkedList<>();
        }
        for (Glucosa aux : glucosas) {

                itemsPantallaInicio.add(aux);
        }
        notifyDataSetChanged();
    }

    //metodo para eliminar las glucosas del listado de items
    public void removeGlucosas() {
        if(itemsPantallaInicio!=null) {
            for(ItemPantallaInicio aux : itemsPantallaInicio){
                if(aux.getTipo().equals("Glucosa")){
                    itemsPantallaInicio.remove(aux);
                }
            }
            notifyDataSetChanged();
        }
    }

    //metodo para añadir un listado de insulinas al listado de items
    public void addInsulinas(List<Insulina> insulinas) {
        if(itemsPantallaInicio==null){
            itemsPantallaInicio=new LinkedList<>();
        }
        for (Insulina aux : insulinas) {

                itemsPantallaInicio.add(aux);

        }
        notifyDataSetChanged();
    }

    //metodo para eliminar las insulinas del listado de items
    public void removeInsulinas() {
        if(itemsPantallaInicio!=null) {
            for(ItemPantallaInicio aux : itemsPantallaInicio){
                if(aux.getTipo().equals("Insulina")){
                    itemsPantallaInicio.remove(aux);
                }
            }
            notifyDataSetChanged();
        }
    }

    //metodo para añadir un listado de registros de actividad fisica manual al listado de items
    public void addRegistros(List<RegistroActividadFisicaManual> registroActividadFisicaManuals) {
        if(itemsPantallaInicio==null){
            itemsPantallaInicio=new LinkedList<>();
        }
        for (RegistroActividadFisicaManual aux : registroActividadFisicaManuals) {

                itemsPantallaInicio.add(aux);

        }
        notifyDataSetChanged();
    }

    //metodo para eliminar las ingestas del listado de items
    public void removeRegistros() {
        if(itemsPantallaInicio!=null) {
            for(ItemPantallaInicio aux : itemsPantallaInicio){
                if(aux.getTipo().equals("Registro")){
                    itemsPantallaInicio.remove(aux);
                }
            }
            notifyDataSetChanged();
        }
    }

    //metodo para añadir un listado de ingestas al listado de items
    public void addIngestas(List<Ingesta> ingestas) {
        if(itemsPantallaInicio==null){
            itemsPantallaInicio=new LinkedList<>();
        }
        for (Ingesta aux : ingestas) {

                itemsPantallaInicio.add(aux);

        }
        notifyDataSetChanged();
    }

    //metodo para eliminar las ingestas del listado de items
    public void removeIngestas() {
        if(itemsPantallaInicio!=null) {
            for(ItemPantallaInicio aux : itemsPantallaInicio){
                if(aux.getTipo().equals("Ingesta")){
                    itemsPantallaInicio.remove(aux);
                }
            }
            notifyDataSetChanged();
        }
    }


    protected static class ViewHolder
            extends RecyclerView.ViewHolder {

        public ImageView icono;
        public TextView identificador;
        public TextView elementos_item;
        public TextView hora_item;
        private Context context;

        private ItemPantallaInicioBinding binding;

        //constructor
        public ViewHolder(ItemPantallaInicioBinding binding, Context context) {
            super(binding.getRoot());

            this.context=context;
            this.binding=binding;
            icono=this.binding.icono;
            identificador=this.binding.identificador;
            elementos_item=this.binding.elementosItem;
            hora_item=this.binding.horaItem;
        }

        //metodo que establece los elementos de la vista con los valores del item auxiliar que recibe
        public void bind(ItemPantallaInicio aux){

            RequestOptions opciones=new RequestOptions().placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher);
            if(aux.getTipo().equals("Registro")){
                identificador.setText(context.getResources().getString(R.string.ActividadFisica));
                Glide.with(binding.getRoot().getContext()).asBitmap().load(R.mipmap.icono_registro).apply(opciones).into(binding.icono);
            }
            else {
                if(aux.getTipo().equals("Glucosa")){
                    identificador.setText(context.getResources().getString(R.string.menu_glucosa));
                    Glide.with(binding.getRoot().getContext()).asBitmap().load(R.mipmap.icono_glucosa).apply(opciones).into(binding.icono);
                }
                else {
                    if(aux.getTipo().equals("Insulina")){
                        identificador.setText(context.getResources().getString(R.string.menu_insulina));
                        Glide.with(binding.getRoot().getContext()).asBitmap().load(R.mipmap.icono_insulina).apply(opciones).into(binding.icono);
                    }
                    else {
                        if(aux.getTipo().equals("Ingesta")){
                            identificador.setText(context.getResources().getString(R.string.Ingesta));
                            Glide.with(binding.getRoot().getContext()).asBitmap().load(R.mipmap.icono_ingesta).apply(opciones).into(binding.icono);
                        }
                    }
                }
            }

            elementos_item.setText(aux.getContent());

            Calendar c=Calendar.getInstance();
            c.setTimeInMillis(aux.getFechaItem());
            c.setTimeZone(TimeZone.getDefault());

            SimpleDateFormat format= new SimpleDateFormat("k:mm a");
            String  time = format.format(c.getTime());
            hora_item.setText(time);

        }
    }
}
