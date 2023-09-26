package es.uniovi.imovil.epi_diabeticlog.Insulina.View;

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

import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;
import es.uniovi.imovil.epi_diabeticlog.Insulina.ViewModel.InsulinaViewModel;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentInsulinaBinding;

public class InsulinaFragment extends Fragment implements View.OnClickListener, CallbacksInsulinaActivity {

    private FragmentInsulinaBinding binding;

    private InsulinaRecyclerViewAdapter adapter;
    private InsulinaViewModel viewModel;

    private FloatingActionButton boton_add_insulina;

    private Insulina insulina_creada;

    private String tipo_aplicacion;
    private String tipo_insulina;
    private int num_unidades;
    private long fecha_aplicacion;
    private int id_aplicacion;

    private int option;
    private boolean returned=false;

    private static final String PREFERENCES = "PREFERENCIAS";
    private static final String CHANNEL_ID = "ID_CHANNEL_INSULINA";
    private AlarmManager m_alarmMgr;

    private static final String PREFERENCES_USER="USER_PREFERENCES";
    private static String UUID;

    public InsulinaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getUUID();
        this.createNotificationChannel();
        this.inicializarNotificacionID();
        this.inicializadasPreferences();
        this.establecerPreferences();
        this.setRecurringAlarmInsulinaLenta(this.getContext());
        this.setRecurringAlarmInsulinaRapidaDesayuno(this.getContext());
        this.setRecurringAlarmInsulinaRapidaComida(this.getContext());
        this.setRecurringAlarmInsulinaRapidaCena(this.getContext());
        this.restablecerPreferences(this.getContext());

        viewModel = new ViewModelProvider(this).get(InsulinaViewModel.class);
        adapter = new InsulinaRecyclerViewAdapter(this.getActivity(), this);
        if (getArguments() != null) {
            this.option=getArguments().getInt("option");
            returned=true;
            if(option==1){
                boolean clicked=getArguments().getBoolean("clicked_add");
                if(clicked) {
                    insulina_creada = getArguments().getParcelable("insulina_creada");
                    insulina_creada.setUuid(UUID);
                }
            }else if(getArguments().getInt("option")==2){
                boolean clicked=getArguments().getBoolean("clicked_edit");
                if(clicked) {
                    tipo_aplicacion = getArguments().getString("tipo_aplicacion");
                    tipo_insulina = getArguments().getString("tipo_insulina");
                    fecha_aplicacion = getArguments().getLong("fecha_aplicacion");
                    num_unidades = getArguments().getInt("num_unidades");
                    id_aplicacion = getArguments().getInt("id_aplicacion");
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

        binding= FragmentInsulinaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        //difinimos el texto que tendrá la toolbar
        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_insulina));

        RecyclerView recyclerView = (RecyclerView)binding.recyclerAplicacionesInsulina;

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        viewModel.getInsulinas().observe(getViewLifecycleOwner(), new Observer<List<Insulina>>() {
            @Override
            public void onChanged(@Nullable List<Insulina> insulinas) {
                adapter.setInsulinas(insulinas);
            }
        });

        onReturnToFragment();

        boton_add_insulina = binding.btnAddAplicacionInsulina;
        boton_add_insulina.setOnClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    boton_add_insulina.hide();
                else if (dy < 0)
                    boton_add_insulina.show();
            }
        });

        return view;
    }

    //metodo que se ejecuta al abrir el fragmento, de forma que si se accede tras añadir una aplicación de insulina esta se insertará en la bbd y
    // si se accede tras modificar una aplicación esta se modificará en la bbdd
    private void onReturnToFragment() {
        if(returned) {
            if (getArguments().getInt("option") == 1) {
                boolean clicked=getArguments().getBoolean("clicked_add");
                if(clicked) {
                    try {
                        if(insulina_creada!=null) {
                            this.viewModel.insert(insulina_creada);
                            Toast.makeText(this.getActivity(), getResources().getString(R.string.Registrada)+": " + insulina_creada.getNum_unidades() + " " + getResources().getString(R.string.unidadesdeinsulina) +" "+ getResources().getString(R.string.tipo) + " "+ insulina_creada.getTipo_insulina() + " " +getResources().getString(R.string.aplicacion) + " "  + insulina_creada.getTipo_aplicacion(), Toast.LENGTH_LONG).show();
                            this.establecerInsulinaRegistrada(insulina_creada.getTipo_insulina(), insulina_creada.getFecha_aplicacion());
                            insulina_creada = null;
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
                        this.viewModel.modifyInsulina(this.tipo_aplicacion, this.tipo_insulina, this.num_unidades, this.fecha_aplicacion, this.id_aplicacion);
                        this.establecerInsulinaRegistrada(this.tipo_insulina, this.fecha_aplicacion);
                        Toast.makeText(this.getActivity(), getResources().getString(R.string.registradaaplicacion)+": " + tipo_aplicacion + getResources().getString(R.string.tipoinsulina) +": " + tipo_insulina + getResources().getString(R.string.numerounidades) +": " + String.valueOf(num_unidades), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //metodo para obtener el uuid del usuario
    private void getUUID() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE);
        UUID=prefs.getString("UUID_usuario", "");
    }

    //metodo que se ejecuta al pulsar el fab
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_add_aplicacion_insulina) {
            Navigation.findNavController(v).navigate(R.id.addAplicacionInsulinaFragment);
        }
    }

    //metodo que se ejecuta para eliminar la insulina seleccionada
    @Override
    public void removeSelected(Insulina seleccionada) {
        boton_add_insulina.show();
        this.viewModel.removeInsulina(seleccionada);
        Toast.makeText(this.getActivity(), getResources().getString(R.string.Eliminada)+" "+getResources().getString(R.string.AplicacionesInsulina)+": " + seleccionada.getTipo_insulina() + " "+ getResources().getString(R.string.TipoAplicacion)+": " + seleccionada.getTipo_aplicacion() + " "+getResources().getString(R.string.NumeroUnidades)+": " + seleccionada.getNum_unidades(), Toast.LENGTH_LONG).show();
        this.establecerInsulinaEliminada(seleccionada.getTipo_insulina(), seleccionada.getFecha_aplicacion());
    }

    //metodo que se ejecuta para modificar la insulina seleccionada
    @Override
    public void modifySelected(Insulina seleccionada) {
        Bundle bundle=new Bundle();
        bundle.putParcelable("seleccionada", seleccionada);
        this.establecerInsulinaEliminada(seleccionada.getTipo_insulina(), seleccionada.getFecha_aplicacion());
        Navigation.findNavController(this.getView()).navigate(R.id.editAplicacionInsulinaFragment, bundle);
    }

    //metodo para inicializar las preferencias de usuario relativas a las alarmas
    private void inicializadasPreferences() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        if(prefs.getBoolean("registradas_preferencias_insulina",false)==false){
            prefsEditor.putBoolean("registradas_preferencias_insulina", false);
            prefsEditor.commit();
        }
    }

    //metodo para establecer las preferencias de usuario relativas a las alarmas
    private void establecerPreferences() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        if(!prefs.getBoolean("registradas_preferencias_insulina", false)){
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putBoolean("registrada_insulina_lenta", false);
            prefsEditor.putBoolean("registrada_insulina_rapida_desayuno", false);
            prefsEditor.putBoolean("registrada_insulina_rapida_comida", false);
            prefsEditor.putBoolean("registrada_insulina_rapida_cena", false);

            prefsEditor.putBoolean("notificada_insulina_lenta", false);
            prefsEditor.putBoolean("notificada_insulina_rapida_desayuno", false);
            prefsEditor.putBoolean("notificada_insulina_rapida_comida", false);
            prefsEditor.putBoolean("notificada_insulina_rapida_cena", false);

            prefsEditor.putBoolean("registradas_preferencias_insulina", true);
            prefsEditor.commit();

        }

    }

    //metodo para almacenar el id de la notificación
    private void inicializarNotificacionID() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        int notificacion_id=prefs.getInt("notificacion_id",0);
        if(notificacion_id==0){
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putInt("notificacion_id", 0);
            prefsEditor.commit();
        }
    }

    //metodo para establecer la insulina registrada en las preferencias de usuario
    private void establecerInsulinaRegistrada(String tipo, long fecha_aplicacion) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        switch(tipo){
            case ("Lenta"):
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTimeInMillis(fecha_aplicacion);
                if(calendar2.get(Calendar.HOUR_OF_DAY)>=22 && calendar2.get(Calendar.HOUR_OF_DAY)<=23) {
                    prefsEditor.putBoolean("registrada_insulina_lenta", true);
                    prefsEditor.commit();
                }
                break;
            case("Rápida"):
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(fecha_aplicacion);
                if(calendar.get(Calendar.HOUR_OF_DAY)>=8 && calendar.get(Calendar.HOUR_OF_DAY)<=10){
                    prefsEditor.putBoolean("registrada_insulina_rapida_desayuno", true);
                    prefsEditor.commit();
                }
                if(calendar.get(Calendar.HOUR_OF_DAY)>=13 && calendar.get(Calendar.HOUR_OF_DAY)<=15){
                    prefsEditor.putBoolean("registrada_insulina_rapida_comida", true);
                    prefsEditor.commit();

                }
                if(calendar.get(Calendar.HOUR_OF_DAY)>=21 && calendar.get(Calendar.HOUR_OF_DAY)<=23){
                    prefsEditor.putBoolean("registrada_insulina_rapida_cena", true);
                    prefsEditor.commit();
                }
                break;
        }

    }

    //metodo para establecer una insulina como eliminada en las preferencias de usuario
    private void establecerInsulinaEliminada(String tipo, long fecha_aplicacion) {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        String Lenta=getResources().getString(R.string.Lenta);
        String Rapida=getResources().getString(R.string.Rapida);

        if(tipo.equals(Lenta)) {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(fecha_aplicacion);
            if (calendar2.get(Calendar.HOUR_OF_DAY) >= 22 && calendar2.get(Calendar.HOUR_OF_DAY) <= 23) {
                prefsEditor.putBoolean("registrada_insulina_lenta", false);
                prefsEditor.commit();

            } else {
                if (tipo.equals(Rapida)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(fecha_aplicacion);
                    if (calendar.get(Calendar.HOUR_OF_DAY) >= 8 && calendar.get(Calendar.HOUR_OF_DAY) <= 10) {
                        prefsEditor.putBoolean("registrada_insulina_rapida_desayuno", false);
                        prefsEditor.commit();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) >= 13 && calendar.get(Calendar.HOUR_OF_DAY) <= 15) {
                        prefsEditor.putBoolean("registrada_insulina_rapida_comida", false);
                        prefsEditor.commit();

                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) >= 21 && calendar.get(Calendar.HOUR_OF_DAY) <= 23) {
                        prefsEditor.putBoolean("registrada_insulina_rapida_cena", false);
                        prefsEditor.commit();
                    }
                }
            }
        }
    }

    //metodo para crear el canal de notificaciones por el que estas se mandarán al usuario
    private void createNotificationChannel() {
        // Crea un canal de notificaciones, pero solo en API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name ="notificaciones_insulina";
            String description = "canal para notificaciones sobre insulina";
            int importance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Registra el canal con el sistema; ya no se pueden cambiar los parámetros
            NotificationManager notificationManager = this.getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //metodo para establecer una alrma que reseteará las preferencias de usuario
    private void restablecerPreferences(Context context) {
        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, InsulinaFragment.PreferencesInsulinaAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        //PENSAR SI DEBERIA SUMAR UNO AL DIA
        calendar.set(Calendar.DAY_OF_MONTH, (calendar.get(Calendar.DAY_OF_MONTH)+1));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //ESTA LINEA ESTA COMENTADA PORQUE LAS 3 LINEAS DE DEBAJO DEBERIAN DE FUNCIONAR MEJOR QUE ELLA
        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);

    }

    //metodo para establecer la alarma relativa a la insulina lenta
    private void setRecurringAlarmInsulinaLenta(Context context) {

        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, InsulinaFragment.InsulinaLentaAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);

    }

    //metodo para establecer la alarma relativa a la insulina rapida relativa al desayuno
    private void setRecurringAlarmInsulinaRapidaDesayuno(Context context) {

        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, InsulinaFragment.InsulinaRapidaDesayunoAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);

    }

    //metodo para establecer la alarma relativa a la insulina rapida relativa a la comida
    private void setRecurringAlarmInsulinaRapidaComida(Context context) {

        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, InsulinaFragment.InsulinaRapidaComidaAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);

    }

    //metodo para establecer la alarma relativa a la insulina rapida relativa a la cena
    private void setRecurringAlarmInsulinaRapidaCena(Context context) {

        m_alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, InsulinaFragment.InsulinaRapidaCenaAlarmReceiver.class);
        PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        m_alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);

    }

    //broadcast receiver que se ejecuta al saltar la alarma de la insulina lenta
    public static class InsulinaLentaAlarmReceiver extends BroadcastReceiver {

        //metodo que lanza una notificacion si nos se ha registrado la insulina lenta
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            Boolean registrada_insulina_lenta=prefs.getBoolean("registrada_insulina_lenta",false);

            if(!registrada_insulina_lenta) {

                int id_notificacion=prefs.getInt("notificacion_id",0);
                addNotificacionID(context);
                this.lanzarNotificacion(context, id_notificacion);
            }
        }

        //metodo que actualiza el id de las notificaciones
        private void addNotificacionID(Context context){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            int notificacion_id=prefs.getInt("notificacion_id",0);
            notificacion_id=notificacion_id+1;
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putInt("notificacion_id", notificacion_id);
            prefsEditor.commit();
        }

        //si no se ha notificado aun al usuario se le notifica
        public void lanzarNotificacion(Context context, int id_notificacion){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if(prefs.getBoolean("notificada_insulina_lenta", false)==false) {
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
                builder.setContentTitle(context.getResources().getString(R.string.registrainsulinalenta));
                builder.setTicker(context.getResources().getString(R.string.importante));
                builder.setContentText(context.getResources().getString(R.string.olvidadoinsulinalenta));

                // Lanzar la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id_notificacion, builder.build());
                prefsEditor.putBoolean("notificada_insulina_lenta", true);
                prefsEditor.commit();
            }
        }
    }


    //broadcast receiver que se ejecuta al saltar la alarma de la insulina rapida del desayuno
    public static class InsulinaRapidaDesayunoAlarmReceiver extends BroadcastReceiver {

        //metodo que lanza una notificacion si no se ha registrado la insulina rapida del desayuno
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            Boolean registrada_insulina_rapida_desayuno=prefs.getBoolean("registrada_insulina_rapida_desayuno",false);

            if(!registrada_insulina_rapida_desayuno) {

                int id_notificacion=prefs.getInt("notificacion_id",0);
                addNotificacionID(context);
                this.lanzarNotificacion(context, id_notificacion);
            }
        }

        //metodo que actualiza el id de las noificaciones
        private void addNotificacionID(Context context){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            int notificacion_id=prefs.getInt("notificacion_id",0);
            notificacion_id=notificacion_id+1;
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putInt("notificacion_id", notificacion_id);
            prefsEditor.commit();
        }

        //si no se ha notificado aun al usuario se le notifica
        public void lanzarNotificacion(Context context, int id_notificacion){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if(prefs.getBoolean("notificada_insulina_rapida_desayuno", false)==false) {
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
                builder.setContentTitle(context.getResources().getString(R.string.registrainsulinarapidadesayuno));
                builder.setTicker(context.getResources().getString(R.string.importante));
                builder.setContentText(context.getResources().getString(R.string.olvidadoinsulinarapidadesayuno));

                // Lanzar la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id_notificacion, builder.build());
                prefsEditor.putBoolean("notificada_insulina_rapida_desayuno", true);
                prefsEditor.commit();
            }
        }
    }

    //broadcast receiver que se ejecuta al saltar la alarma de la insulina rapida de la comida
    public static class InsulinaRapidaComidaAlarmReceiver extends BroadcastReceiver {

        //metodo que lanza una notificacion si nos se ha registrado la insulina rapida de la comida
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            Boolean registrada_insulina_rapida_comida=prefs.getBoolean("registrada_insulina_rapida_comida",false);

            if(!registrada_insulina_rapida_comida) {

                int id_notificacion=prefs.getInt("notificacion_id",0);
                addNotificacionID(context);
                this.lanzarNotificacion(context, id_notificacion);
            }
        }

        //metodo que actuasliza el id de las noificaciones
        private void addNotificacionID(Context context){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            int notificacion_id=prefs.getInt("notificacion_id",0);
            notificacion_id=notificacion_id+1;
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putInt("notificacion_id", notificacion_id);
            prefsEditor.commit();
        }

        //si no se ha notificado aun al usuario se le notifica
        public void lanzarNotificacion(Context context, int id_notificacion){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if(prefs.getBoolean("notificada_insulina_rapida_comida", false)==false) {
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
                builder.setContentTitle(context.getResources().getString(R.string.registrainsulinarapidacomida));
                builder.setTicker(context.getResources().getString(R.string.importante));
                builder.setContentText(context.getResources().getString(R.string.olvidadoinsulinarapidacomida));

                // Lanzar la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id_notificacion, builder.build());

                prefsEditor.putBoolean("notificada_insulina_rapida_comida", true);
                prefsEditor.commit();
            }
        }
    }

    //broadcast receiver que se ejecuta al saltar la alarma de la insulina rapida de la cena
    public static class InsulinaRapidaCenaAlarmReceiver extends BroadcastReceiver {

        //metodo que lanza una notificacion si nos se ha registrado la insulina rapida de la cena
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            Boolean registrada_insulina_rapida=prefs.getBoolean("registrada_insulina_rapida_cena",false);

            if(!registrada_insulina_rapida) {

                int id_notificacion=prefs.getInt("notificacion_id",0);
                addNotificacionID(context);
                this.lanzarNotificacion(context, id_notificacion);
            }
        }

        //metodo que actuasliza el id de las noificaciones
        private void addNotificacionID(Context context){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            int notificacion_id=prefs.getInt("notificacion_id",0);
            notificacion_id=notificacion_id+1;
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putInt("notificacion_id", notificacion_id);
            prefsEditor.commit();
        }

        //si no se ha notificado aun al usuario se le notifica
        public void lanzarNotificacion(Context context, int id_notificacion){
            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if(prefs.getBoolean("notificada_insulina_rapida_cena", false)==false) {
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
                builder.setContentTitle(context.getResources().getString(R.string.registrainsulinarapidacena));
                builder.setTicker(context.getResources().getString(R.string.importante));
                builder.setContentText(context.getResources().getString(R.string.olvidadoinsulinarapidacena));

                // Lanzar la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id_notificacion, builder.build());

                prefsEditor.putBoolean("notificada_insulina_rapida_cena", true);
                prefsEditor.commit();
            }
        }
    }

    //broadcast receiver que se ejecuta al saltar la alarma para reestablecer las preferencias de usuario
    public static class PreferencesInsulinaAlarmReceiver extends BroadcastReceiver {

        //metodo que resetea las preferencias de usuario relativas a las insulina
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

            if(prefs.getBoolean("registradas_preferencias_insulina", false)==true){
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putBoolean("registrada_insulina_lenta", false);
                prefsEditor.putBoolean("registrada_insulina_rapida_desayuno", false);
                prefsEditor.putBoolean("registrada_insulina_rapida_comida", false);
                prefsEditor.putBoolean("registrada_insulina_rapida_cena", false);

                prefsEditor.putBoolean("notificada_insulina_lenta", false);
                prefsEditor.putBoolean("notificada_insulina_rapida_desayuno", false);
                prefsEditor.putBoolean("notificada_insulina_rapida_comida", false);
                prefsEditor.putBoolean("notificada_insulina_rapida_cena", false);

                prefsEditor.putBoolean("registradas_preferencias_insulina", false);
                prefsEditor.commit();

            }
        }
    }
}