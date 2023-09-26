package es.uniovi.imovil.epi_diabeticlog.Alimentacion.View;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

import es.uniovi.imovil.epi_diabeticlog.Alimentacion.ViewModel.AlimentacionViewModel;
import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.Ingesta;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentAlimentacionBinding;

public class AlimentacionFragment extends Fragment implements View.OnClickListener, CallbacksAlimentacionActivity {

    private FragmentAlimentacionBinding binding;

    private AlimentacionRecyclerViewAdapter adapter;
    private static AlimentacionViewModel viewModel;

    private FloatingActionButton boton_add_ingesta;

    private Ingesta ingesta_creada;

    private int id_ingesta;
    private String nombre_nuevo;
    private float num_raciones_nuevo;
    private long fecha_ingesta;

    private int option;
    private boolean returned=false;

    private static final String PREFERENCES = "PREFERENCIAS";
    private static final String CHANNEL_ID = "ID_CHANNEL_ALIMENTACION";
    private AlarmManager m_alarmMgr;

    private static final String PREFERENCES_USER="USER_PREFERENCES";
    private static String UUID;

    public AlimentacionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getUUID();
        this.createNotificationChannel();
        this.inicializadasPreferences();
        this.establecerPreferences();
        this.setRecurringAlarmDesayuno(this.getContext());
        this.setRecurringAlarmMediaManana(this.getContext());
        this.setRecurringAlarmComida(this.getContext());
        this.setRecurringAlarmMerienda(this.getContext());
        this.setRecurringAlarmCena(this.getContext());
        this.restablecerPreferences(this.getContext());

        viewModel = new ViewModelProvider(this).get(AlimentacionViewModel.class);

        adapter = new AlimentacionRecyclerViewAdapter(this.getActivity(), this);
        if (getArguments() != null) {
            this.option=getArguments().getInt("option");
            returned=true;
            if(option==1){
                boolean clicked=getArguments().getBoolean("clicked_add");
                if(clicked) {
                    ingesta_creada = getArguments().getParcelable("ingesta_creada");
                    ingesta_creada.setUuid(UUID);
                }
            }else if(getArguments().getInt("option")==2){
                boolean clicked=getArguments().getBoolean("clicked_edit");
                if(clicked) {
                    nombre_nuevo = getArguments().getString("nombre_nuevo");
                    num_raciones_nuevo = getArguments().getFloat("num_raciones_nuevo");
                    fecha_ingesta = getArguments().getLong("fecha_ingesta");
                    id_ingesta = getArguments().getInt("id_ingesta");
                }
            }
        }
        else{
            returned=false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding= FragmentAlimentacionBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_alimentacion));

        RecyclerView recyclerView = (RecyclerView) binding.recyclerIngestas;

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        viewModel.getIngestas().observe(getViewLifecycleOwner(), new Observer<List<Ingesta>>() {
            @Override
            public void onChanged(@Nullable List<Ingesta> ingestas) {
                //System.out.println("ESTAMOS DENTRO DEL ONCHANGED: "+registros.get(0).getTipo_actividad()+" "+registros.get(0).getIntensidad());
                adapter.setIngestas(ingestas);
            }
        });

        onReturnToFragment();

        boton_add_ingesta = binding.botonAddIngesta;
        boton_add_ingesta.setOnClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    boton_add_ingesta.hide();
                else if (dy < 0)
                    boton_add_ingesta.show();
            }
        });

        return view;
    }

    //metodo on click que se ejcuta al pulsar el fab
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.boton_add_ingesta) {
            Navigation.findNavController(v).navigate(R.id.addIngestaFragment);
        }
    }

    //metodo que se ejecuta al ejecutar este fragmento, de forma que si se viene de añadir una ingesta, se añade a la base de datos y si se
    //viene de modificar una ingesta, se modifica en la bbdd
    protected void onReturnToFragment() {
        if(returned) {
            if (getArguments().getInt("option") == 1) {
                boolean clicked=getArguments().getBoolean("clicked_add");
                if(clicked) {
                    try {
                        if(this.ingesta_creada!=null) {
                            String Desayuno=getResources().getString(R.string.Desayuno);
                            String MediaMa=getResources().getString(R.string.Media_manana);
                            String Comida=getResources().getString(R.string.Comida);
                            String Merienda=getResources().getString(R.string.Merienda);
                            String Cena=getResources().getString(R.string.Cena);


                            SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                            if(ingesta_creada.getTipo_ingesta().equals(Desayuno)){
                                boolean registradoDesayuno=prefs.getBoolean("registrado_desayuno",false);
                                if(registradoDesayuno){
                                    Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorDesayunoRegistered), Toast.LENGTH_LONG).show();
                                }
                                else{
                                    this.viewModel.insert(this.ingesta_creada);
                                    Toast.makeText(this.getActivity(), getResources().getString(R.string.RegistradaIngesta)+ " " + ingesta_creada.getTipo_ingesta(), Toast.LENGTH_LONG).show();
                                    this.establecerComidaregistrada(ingesta_creada.getTipo_ingesta());
                                    ingesta_creada=null;
                                }
                            }
                            else{
                                if(ingesta_creada.getTipo_ingesta().equals(MediaMa)){
                                    boolean registradoMediaManana=prefs.getBoolean("registrada_mediamanana",false);
                                    if(registradoMediaManana){
                                        Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorMediaMananaRegistered), Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        this.viewModel.insert(this.ingesta_creada);
                                        Toast.makeText(this.getActivity(), getResources().getString(R.string.RegistradaIngesta)+ " " + ingesta_creada.getTipo_ingesta(), Toast.LENGTH_LONG).show();
                                        this.establecerComidaregistrada(ingesta_creada.getTipo_ingesta());
                                        ingesta_creada=null;
                                    }
                                }
                                else{
                                    if(ingesta_creada.getTipo_ingesta().equals(Comida)){
                                        boolean registradoComida=prefs.getBoolean("registrada_comida",false);
                                        if(registradoComida){
                                            Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorComidaRegistered), Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            this.viewModel.insert(this.ingesta_creada);
                                            Toast.makeText(this.getActivity(), getResources().getString(R.string.RegistradaIngesta)+ " " + ingesta_creada.getTipo_ingesta(), Toast.LENGTH_LONG).show();
                                            this.establecerComidaregistrada(ingesta_creada.getTipo_ingesta());
                                            ingesta_creada=null;
                                        }
                                    }
                                    else{
                                        if(ingesta_creada.getTipo_ingesta().equals(Merienda)){
                                            boolean registradoMerienda=prefs.getBoolean("registrada_merienda",false);
                                            if(registradoMerienda){
                                                Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorMeriendaRegistered), Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                this.viewModel.insert(this.ingesta_creada);
                                                Toast.makeText(this.getActivity(), getResources().getString(R.string.RegistradaIngesta)+ " " + ingesta_creada.getTipo_ingesta(), Toast.LENGTH_LONG).show();
                                                this.establecerComidaregistrada(ingesta_creada.getTipo_ingesta());
                                                ingesta_creada=null;
                                            }
                                        }
                                        else{
                                            if(ingesta_creada.getTipo_ingesta().equals(Cena)){
                                                boolean registradoCena=prefs.getBoolean("registrada_cena",false);
                                                if(registradoCena){
                                                    Toast.makeText(this.getActivity(), getResources().getString(R.string.ErrorCenaRegistered), Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    this.viewModel.insert(this.ingesta_creada);
                                                    Toast.makeText(this.getActivity(), getResources().getString(R.string.RegistradaIngesta)+ " " + ingesta_creada.getTipo_ingesta(), Toast.LENGTH_LONG).show();
                                                    this.establecerComidaregistrada(ingesta_creada.getTipo_ingesta());
                                                    ingesta_creada=null;
                                                }
                                            }
                                            else {

                                                this.viewModel.insert(this.ingesta_creada);
                                                Toast.makeText(this.getActivity(), getResources().getString(R.string.RegistradaIngesta)+ " " + ingesta_creada.getTipo_ingesta(), Toast.LENGTH_LONG).show();
                                                this.establecerComidaregistrada(ingesta_creada.getTipo_ingesta());
                                                ingesta_creada = null;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //codigo que se ejecuta cuando la clase editIngestaActivity termina y retorna los nuevos valores(falta definir esto)
            if (getArguments().getInt("option") == 2) {
                boolean clicked=getArguments().getBoolean("clicked_edit");
                if(clicked) {
                    try {
                        String nombre_viejo=getArguments().getString("nombre_viejo");
                        if((nombre_nuevo.equals(getContext().getResources().getString(R.string.Desayuno)) || nombre_nuevo.equals(getContext().getResources().getString(R.string.Media_manana)) || nombre_nuevo.equals(getContext().getResources().getString(R.string.Comida)) ||nombre_nuevo.equals(getContext().getResources().getString(R.string.Merienda)) || nombre_nuevo.equals(getContext().getResources().getString(R.string.Cena)))&&(!nombre_viejo.equals(nombre_nuevo)))
                        {
                            Toast.makeText(this.getActivity(), getResources().getString(R.string.NombreIngestaReservado), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            this.viewModel.modifyIngesta(this.id_ingesta, this.nombre_nuevo, this.num_raciones_nuevo, this.fecha_ingesta);
                            Toast.makeText(this.getActivity(), getResources().getString(R.string.ModificadaIngesta) + " " + nombre_nuevo, Toast.LENGTH_LONG).show();
                            this.establecerComidaregistrada(nombre_nuevo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //metodo que obtiene el uuid del usuario
    private void getUUID() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE);
        UUID=prefs.getString("UUID_usuario", "");
    }

    //metodo que elimina la ingesta seleccionada
    @Override
    public void removeSelected(Ingesta seleccionada) {
        boton_add_ingesta.show();
        this.viewModel.removeIngesta(seleccionada);
        Toast.makeText(this.getActivity(), getResources().getString(R.string.EliminadaIngesta)+ " " + seleccionada.getTipo_ingesta(), Toast.LENGTH_LONG).show();
        this.establecerComidaEliminada(seleccionada.getTipo_ingesta());
    }

    //metodo que modifica la ingesta seleccionada
    @Override
    public void modifySelected(Ingesta seleccionada) {
        Bundle bundle=new Bundle();
        bundle.putParcelable("seleccionada", seleccionada);
        this.establecerComidaEliminada(seleccionada.getTipo_ingesta());
        Navigation.findNavController(this.getView()).navigate(R.id.editIngestaFragment, bundle);
    }

    //metodo para inicializar las preferencias de usuario que se usan de control relacionadas con la notificación de las ingestas principales
    private void inicializadasPreferences() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        if(prefs.getBoolean("registradas_preferencias",false)==false){
            Log.d("inicializadsPreferences", "Aqui deberia de ser false: "+String.valueOf(prefs.getBoolean("registradas_preferencias", false)));
            prefsEditor.putBoolean("registradas_preferencias", false);
            prefsEditor.commit();
        }
    }

    //metodo para establecer las preferencias de usuario que se usan de control relacionadas con la notificación de las ingestas principales
    private void establecerPreferences() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        if(!prefs.getBoolean("registradas_preferencias", false)){
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putBoolean("registrado_desayuno", false);
            prefsEditor.putBoolean("registrada_mediamanana", false);
            prefsEditor.putBoolean("registrada_comida", false);
            prefsEditor.putBoolean("registrada_merienda", false);
            prefsEditor.putBoolean("registrada_cena", false);

            prefsEditor.putBoolean("notificado_desayuno", false);
            prefsEditor.putBoolean("notificada_mediamanana", false);
            prefsEditor.putBoolean("notificada_comida", false);
            prefsEditor.putBoolean("notificada_merienda", false);
            prefsEditor.putBoolean("notificada_cena", false);

            prefsEditor.putBoolean("registradas_preferencias", true);
            prefsEditor.commit();

        }

    }


    //metodo para indicar que una ingesta principal se ha registrado y por tanto no se notifique la necesidad de registrarlo
    private void establecerComidaregistrada(String nombre) {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        String Desayuno=getResources().getString(R.string.Desayuno);
        String MediaMa=getResources().getString(R.string.Media_manana);
        String Comida=getResources().getString(R.string.Comida);
        String Merienda=getResources().getString(R.string.Merienda);
        String Cena=getResources().getString(R.string.Cena);

        if(nombre.equals(Desayuno)){
            prefsEditor.putBoolean("registrado_desayuno", true);
            prefsEditor.commit();
        }
        else{
            if(nombre.equals(MediaMa)){
                prefsEditor.putBoolean("registrada_mediamanana", true);
                prefsEditor.commit();

            }
            else{
                if(nombre.equals(Comida)){
                    prefsEditor.putBoolean("registrada_comida", true);
                    prefsEditor.commit();

                }
                else{
                    if(nombre.equals(Merienda)){
                        prefsEditor.putBoolean("registrada_merienda", true);
                        prefsEditor.commit();
                    }
                    else{
                        if(nombre.equals(Cena)){
                            prefsEditor.putBoolean("registrada_cena", true);
                            prefsEditor.commit();

                        }
                    }
                }
            }
        }
    }

    //metodo para indicar que una ingesta principal se ha eliminado y por tanto SÍ se notifique la necesidad de registrarlo
    private void establecerComidaEliminada(String nombre) {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        String Desayuno=getResources().getString(R.string.Desayuno);
        String MediaMa=getResources().getString(R.string.Media_manana);
        String Comida=getResources().getString(R.string.Comida);
        String Merienda=getResources().getString(R.string.Merienda);
        String Cena=getResources().getString(R.string.Cena);

        if(nombre.equals(Desayuno)){
            prefsEditor.putBoolean("registrado_desayuno", false);
            prefsEditor.commit();

        }
        else{
            if(nombre.equals(MediaMa)){
                prefsEditor.putBoolean("registrada_mediamanana", false);
                prefsEditor.commit();

            }
            else{
                if(nombre.equals(Comida)){
                    prefsEditor.putBoolean("registrada_comida", false);
                    prefsEditor.commit();

                }
                else{
                    if(nombre.equals(Merienda)){
                        prefsEditor.putBoolean("registrada_merienda", false);
                        prefsEditor.commit();
                    }
                    else{
                        if(nombre.equals(Cena)){
                            prefsEditor.putBoolean("registrada_cena", false);
                            prefsEditor.commit();

                        }
                    }
                }
            }
        }
    }

    //metodo que crea el canal de notificaciones por el que se mandaran al usuario
    private void createNotificationChannel() {
        // Crea un canal de notificaciones, pero solo en API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name ="notificaciones_alimentacion";
            String description = "canal para notificaciones sobre alimentacion";
            int importance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Registra el canal con el sistema; ya no se pueden cambiar los parámetros
            NotificationManager notificationManager = this.getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //metodo que establece una alarma a las 00:00 para reiniciar las preferencias de usuario a su valor por defecto
    private void restablecerPreferences(Context context) {

        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PreferencesAlimentacionAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, (calendar.get(Calendar.DAY_OF_MONTH)+1));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);
    }

    //metodo que establece una alarma a las 11:00 para notificar si no se registro el desayuno
    private void setRecurringAlarmDesayuno(Context context) {

        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DesayunoAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);
    }

    //metodo que establece una alarma a las 13:00 para notificar si no se registro la ingesta de media mañana
    private void setRecurringAlarmMediaManana(Context context) {

        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MedimanaAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,13);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);
    }

    //metodo que establece una alarma a las 15:00 para notificar si no se registro la comida
    private void setRecurringAlarmComida(Context context) {

        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ComidaAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);
    }

    //metodo que establece una alarma a las 18:00 para notificar si no se registro la merienda
    private void setRecurringAlarmMerienda(Context context) {

        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MeriendaAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);
    }

    //metodo que establece una alarma a las 23:00 para notificar si no se registro cena
    private void setRecurringAlarmCena(Context context) {

        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CenaAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);
    }

    //broadcast que se ejecuta al lanzarse la alarma del desayuno
    public static class DesayunoAlarmReceiver extends BroadcastReceiver {

        //metodo que lanza una notificación si detecta que no se ha registrado el desayuno
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            Boolean registrado_desayuno=prefs.getBoolean("registrado_desayuno",false);

            if(!registrado_desayuno) {

                int id_notificacion=prefs.getInt("notificacion_id",0);
                addNotificacionID(context);

                this.lanzarNotificacion(context,id_notificacion);
            }
        }
        //metodo que actualzia el valor del id de la notificación
        private void addNotificacionID(Context context){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            int notificacion_id=prefs.getInt("notificacion_id",0);
            notificacion_id=notificacion_id+1;
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putInt("notificacion_id", notificacion_id);
            prefsEditor.commit();
        }

        //metodo que comprueba si se ha notificado ya al usuario sobre el desayuno y no si se ha notificado, se notifica al usuario
        public void lanzarNotificacion(Context context, int id_notificacion){
            // Constructor de notificaciones
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

            if(prefs.getBoolean("notificado_desayuno", false)==false){

                SharedPreferences.Editor prefsEditor = prefs.edit();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

                builder.setSmallIcon(R.drawable.ic_notificacion_registro);
                builder.setOngoing(false);
                builder.setAutoCancel(false);
                builder.setPriority(NotificationCompat.PRIORITY_MAX);
                builder.setVibrate(new long[] {1000,1000,1000,1000});
                Uri uriRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uriRing);
                builder.setContentTitle(context.getResources().getString(R.string.registratudesayuno));
                builder.setTicker(context.getResources().getString(R.string.importante));
                builder.setContentText(context.getResources().getString(R.string.olvidadodesayuno));

                // Lanzar la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id_notificacion, builder.build());
                prefsEditor.putBoolean("notificado_desayuno",true);
                prefsEditor.commit();
            }

        }
    }

    //broadcast que se ejecuta al lanzarse la alarma de la media mañana
    public static class MedimanaAlarmReceiver extends BroadcastReceiver {

        //metodo que lanza una notificación si detecta que no se ha registrado el pincho de media mañana
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            Boolean registrado_mediama=prefs.getBoolean("registrada_mediamanana",false);
            if(!registrado_mediama) {

                int id_notificacion=prefs.getInt("notificacion_id",0);
                addNotificacionID(context);

                this.lanzarNotificacion(context,id_notificacion);
            }
        }
        //metodo que actualzia el valor del id de la notificación
        private void addNotificacionID(Context context){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            int notificacion_id=prefs.getInt("notificacion_id",0);
            notificacion_id=notificacion_id+1;
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putInt("notificacion_id", notificacion_id);
            prefsEditor.commit();
        }
        //metodo que comprueba si se ha notificado ya al usuario sobre el pincho de media mañana y no si se ha notificado, se notifica al usuario
        public void lanzarNotificacion(Context context, int id_notificacion){
            // Constructor de notificaciones
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if(prefs.getBoolean("notificada_mediamanana", false)==false) {

                SharedPreferences.Editor prefsEditor = prefs.edit();
                // Constructor de notificaciones
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

                builder.setSmallIcon(R.drawable.ic_notificacion_registro);
                builder.setOngoing(false);
                builder.setAutoCancel(false);
                builder.setPriority(NotificationCompat.PRIORITY_MAX);
                builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
                Uri uriRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uriRing);

                builder.setContentTitle(context.getResources().getString(R.string.registratumediamanana));
                builder.setTicker(context.getResources().getString(R.string.importante));
                builder.setContentText(context.getResources().getString(R.string.olvidadomediamanana));

                // Lanzar la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id_notificacion, builder.build());

                prefsEditor.putBoolean("notificada_mediamanana",true);
                prefsEditor.commit();
            }

        }
    }

    //broadcast que se ejecuta al lanzarse la alarma de la comida
    public static class ComidaAlarmReceiver extends BroadcastReceiver {

        //metodo que lanza una notificación si detecta que no se ha registrado la comida
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            Boolean registrada_comida=prefs.getBoolean("registrada_comida",false);

            if(!registrada_comida) {

                int id_notificacion=prefs.getInt("notificacion_id",0);
                addNotificacionID(context);

                this.lanzarNotificacion(context, id_notificacion);
            }
        }
        //metodo que actualzia el valor del id de la notificación
        private void addNotificacionID(Context context){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            int notificacion_id=prefs.getInt("notificacion_id",0);
            notificacion_id=notificacion_id+1;
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putInt("notificacion_id", notificacion_id);
            prefsEditor.commit();
        }
        //metodo que comprueba si se ha notificado ya al usuario sobre la comida y no si se ha notificado, se notifica al usuario
        public void lanzarNotificacion(Context context, int id_notificacion){
            // Constructor de notificaciones
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if(prefs.getBoolean("notificada_comida", false)==false) {

                SharedPreferences.Editor prefsEditor = prefs.edit();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

                builder.setSmallIcon(R.drawable.ic_notificacion_registro);
                builder.setOngoing(false);
                builder.setAutoCancel(false);
                builder.setPriority(NotificationCompat.PRIORITY_MAX);
                builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
                Uri uriRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uriRing);

                builder.setContentTitle(context.getResources().getString(R.string.registratucomida));
                builder.setTicker(context.getResources().getString(R.string.importante));
                builder.setContentText(context.getResources().getString(R.string.olvidadocomida));

                // Lanzar la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id_notificacion, builder.build());

                prefsEditor.putBoolean("notificada_comida", true);
                prefsEditor.commit();
            }
        }
    }

    //broadcast que se ejecuta al lanzarse la alarma de la merienda
    public static class MeriendaAlarmReceiver extends BroadcastReceiver {

        //metodo que lanza una notificación si detecta que no se ha registrado la merienda
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            Boolean registrada_merienda=prefs.getBoolean("registrada_merienda",false);

            if(!registrada_merienda) {

                int id_notificacion=prefs.getInt("notificacion_id",0);
                addNotificacionID(context);

                this.lanzarNotificacion(context, id_notificacion);
            }
        }
        //metodo que actualzia el valor del id de la notificación
        private void addNotificacionID(Context context){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            int notificacion_id=prefs.getInt("notificacion_id",0);
            notificacion_id=notificacion_id+1;
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putInt("notificacion_id", notificacion_id);
            prefsEditor.commit();
        }
        //metodo que comprueba si se ha notificado ya al usuario sobre la merienda y no si se ha notificado, se notifica al usuario
        public void lanzarNotificacion(Context context, int id_notificacion){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if(prefs.getBoolean("notificada_merienda", false)==false) {

                SharedPreferences.Editor prefsEditor = prefs.edit();
                // Constructor de notificaciones
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

                builder.setSmallIcon(R.drawable.ic_notificacion_registro);
                builder.setOngoing(false);
                builder.setAutoCancel(false);
                builder.setPriority(NotificationCompat.PRIORITY_MAX);
                builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
                Uri uriRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uriRing);

                builder.setContentTitle(context.getResources().getString(R.string.registratumerienda));
                builder.setTicker(context.getResources().getString(R.string.importante));
                builder.setContentText(context.getResources().getString(R.string.olvidadomerienda));

                // Lanzar la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id_notificacion, builder.build());
                prefsEditor.putBoolean("notificada_merienda", true);
                prefsEditor.commit();
            }
        }
    }

    //broadcast que se ejecuta al lanzarse la alarma de la cena
    public static class CenaAlarmReceiver extends BroadcastReceiver {

        //metodo que lanza una notificación si detecta que no se ha registrado la cena
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            Boolean registrada_cena=prefs.getBoolean("registrada_cena",false);


            if(!registrada_cena) {

                int id_notificacion=prefs.getInt("notificacion_id",0);
                addNotificacionID(context);

                this.lanzarNotificacion(context, id_notificacion);
            }
        }

        //metodo que actualzia el valor del id de la notificación
        private void addNotificacionID(Context context){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            int notificacion_id=prefs.getInt("notificacion_id",0);
            notificacion_id=notificacion_id+1;
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putInt("notificacion_id", notificacion_id);
            prefsEditor.commit();
        }
        //metodo que comprueba si se ha notificado ya al usuario sobre la cena y no si se ha notificado, se notifica al usuario
        public void lanzarNotificacion(Context context, int id_notificacion){
            // Constructor de notificaciones
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if(prefs.getBoolean("notificada_cena", false)==false) {
                Log.d("CenaAlarmReceiver", "Si se lee esto, SE NOTIFICARA LA CENA");
                SharedPreferences.Editor prefsEditor = prefs.edit();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

                builder.setSmallIcon(R.drawable.ic_notificacion_registro);
                builder.setOngoing(false);
                builder.setAutoCancel(false);
                builder.setPriority(NotificationCompat.PRIORITY_MAX);
                builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
                Uri uriRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uriRing);

                builder.setContentTitle(context.getResources().getString(R.string.registratucena));
                builder.setTicker(context.getResources().getString(R.string.importante));
                builder.setContentText(context.getResources().getString(R.string.olvidadocena));

                // Lanzar la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id_notificacion, builder.build());
                prefsEditor.putBoolean("notificada_cena", true);
                prefsEditor.commit();
            }
        }
    }

    //broadcast que se ejecuta al lanzarse la alarma para resetear las preferencias
    public static class PreferencesAlimentacionAlarmReceiver extends BroadcastReceiver {

        //metodo que resetea las preferencias de usuario a sus valores por defecto
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

            if(prefs.getBoolean("registradas_preferencias", false)==true){
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putBoolean("registrado_desayuno", false);
                prefsEditor.putBoolean("registrada_mediamanana", false);
                prefsEditor.putBoolean("registrada_comida", false);
                prefsEditor.putBoolean("registrada_merienda", false);
                prefsEditor.putBoolean("registrada_cena", false);

                prefsEditor.putBoolean("notificado_desayuno", false);
                prefsEditor.putBoolean("notificada_mediamanana", false);
                prefsEditor.putBoolean("notificada_comida", false);
                prefsEditor.putBoolean("notificada_merienda", false);
                prefsEditor.putBoolean("notificada_cena", false);

                prefsEditor.putBoolean("registradas_preferencias", false);
                prefsEditor.commit();

            }
        }
    }
}