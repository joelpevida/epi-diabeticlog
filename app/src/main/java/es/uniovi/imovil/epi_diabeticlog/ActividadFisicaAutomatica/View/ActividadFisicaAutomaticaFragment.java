package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.View;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.ViewModel.ActividadFisicaAutomaticaViewmodel;
import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaAutomatica.Model.RegistroActividadFisicaAutomatica;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.databinding.FragmentActividadFisicaAutomaticaBinding;


import java.util.Calendar;
import java.util.GregorianCalendar;

public class ActividadFisicaAutomaticaFragment extends Fragment implements SensorEventListener {

    private FragmentActividadFisicaAutomaticaBinding binding;

    private AlarmManager m_alarmMgrPodometro;

    private ProgressBar progressBar;
    private TextView textViewPasos;
    private TextView distanciaRecorrida;
    private TextView kcalQuemadas;
    private TextView MaxPasos;

    private SensorManager sensorManager;
    private boolean running=false;
    private float totalSteps=0;
    private float previousTotalSteps=0;
    private Sensor stepSensor;

    private static final float KCALPORPASO=(float)0.035;
    private static final float METROSPORPASO=(float)0.7;
    private static final float MAXPASOS=6000 ;

    private ActividadFisicaAutomaticaViewmodel viewmodel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewmodel = new ViewModelProvider(this).get(ActividadFisicaAutomaticaViewmodel.class);

        this.inicializadasPreferences();

        this.establecerPreferences();

        this.setRecurringAlarmSavePedometer(this.getContext());

        this.restablecerPreferences(this.getContext());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);

        binding= FragmentActividadFisicaAutomaticaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        Toolbar toolbar=(Toolbar)this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_registroactividadfisicaautomatica));

        SharedPreferences sharedPreferences=this.getContext().getSharedPreferences("myPrefrerences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        previousTotalSteps=sharedPreferences.getFloat("key1", 0);
        totalSteps=sharedPreferences.getFloat("key2", 0);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        textViewPasos = binding.textViewPasos;
        progressBar=binding.progressBarPasos;
        distanciaRecorrida=binding.distanciaRecorrida;
        kcalQuemadas=binding.KcalQuemadas;
        MaxPasos=binding.textViewMaxPasos;
        MaxPasos.setText("/"+String.valueOf((int)MAXPASOS)+" "+getResources().getString(R.string.pasos));

        progressBar.setMin(0);
        progressBar.setMax(100);

        int currentSteps=(int)totalSteps - (int)previousTotalSteps;

        this.establecerParametros(currentSteps);

        boolean ActividadFisicaPermitidoRegistro=sharedPreferences.getBoolean("ActividadFisicaPermitidoRegistro", false);
        boolean ActFisiAlmacenada=sharedPreferences.getBoolean("ActividadFisicaAlmacenada",false);
        boolean ActFisiRegistrada=sharedPreferences.getBoolean("ActividadFisicaRegistrada",true);



        if(ActividadFisicaPermitidoRegistro) {
            if (!ActFisiRegistrada) {
                if(ActFisiAlmacenada) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("USER_PREFERENCES", Context.MODE_PRIVATE);

                    String UUID=prefs.getString("UUID_usuario", "");
                    int pasos = sharedPreferences.getInt("pasos_AFR", 0);
                    float distancia = sharedPreferences.getFloat("distancia_AFR", 0);
                    float kcalQuemadas = sharedPreferences.getFloat("kcal_AFR", 0);
                    long fecha = sharedPreferences.getLong("fecha_AFR", 0);

                    RegistroActividadFisicaAutomatica registroActividadFisicaAutomatica = new RegistroActividadFisicaAutomatica();
                    registroActividadFisicaAutomatica.setUuid(UUID);
                    registroActividadFisicaAutomatica.setPasos(pasos);
                    registroActividadFisicaAutomatica.setDistancia(distancia);
                    registroActividadFisicaAutomatica.setKcal_quemadas(kcalQuemadas);
                    registroActividadFisicaAutomatica.setFecha(fecha);

                    this.viewmodel.insert(registroActividadFisicaAutomatica);

                    editor.putBoolean("ActividadFisicaRegistrada", true);
                    editor.commit();

                    progressBar.setProgress(50);

                }
            }
        }


        return view;
    }

    //metodo para actualzizar los elementos de la vista en base al total de pasos que se recibe como parámetro
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void establecerParametros(int currentSteps) {

        float kcal=currentSteps*KCALPORPASO;
        kcal = (float) (Math.round(kcal*100.0f)/100.0f);

        float distanciaRecorrida=((float)(currentSteps))*((float)METROSPORPASO);
        distanciaRecorrida = (float) (Math.round(distanciaRecorrida*100.0f)/100.0f);

        float progress=((float)currentSteps/this.MAXPASOS)*(float)100;

        if(currentSteps==0){

            this.progressBar.setVisibility(View.INVISIBLE);
            this.progressBar.setProgress(0);
            this.progressBar.setVisibility(View.VISIBLE);
        }
        else {

            if (progress >= 100) {

                progressBar.setProgress(100);
            } else {

                this.progressBar.setProgress((int) progress);
            }
        }

        this.textViewPasos.setText(String.valueOf(currentSteps));
        this.distanciaRecorrida.setText(String.valueOf(distanciaRecorrida)+" m");
        this.kcalQuemadas.setText(String.valueOf(kcal)+ " "+getResources().getString(R.string.kcals));

    }

    @Override
    public void onResume() {
        super.onResume();
        running=true;

        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(stepSensor==null){
            Toast.makeText(this.getActivity(), "NO SENSOR DETECTED ON THIS DEVICE", Toast.LENGTH_SHORT).show();
        }
        else{
            sensorManager.registerListener(ActividadFisicaAutomaticaFragment.this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }

    }

    //este metodo es necesario por implementar la interfaz SensorEventListener
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //este metodo es necesario por implementar la interfaz SensorEventListener
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent event) {

        try {
            if (running) {
                SharedPreferences sharedPreferences = this.getContext().getSharedPreferences("myPrefrerences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                totalSteps = event.values[0];
                int currentSteps = (int) totalSteps - (int) previousTotalSteps;
                editor.putFloat("key1", previousTotalSteps);
                editor.putFloat("key2", totalSteps);
                editor.commit();

                this.establecerParametros(currentSteps);

            } else {
                SharedPreferences sharedPreferences = this.getContext().getSharedPreferences("myPrefrerences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                totalSteps = event.values[0];
                editor.putFloat("key1", previousTotalSteps);
                editor.putFloat("key2", totalSteps);
                editor.commit();
            }
        }catch(Exception e){
            //en caso de error se informa en el log
            Log.d("Sensor error", "ha habido un problema con el sensor");
        }
    }

    //metodo para establecer la alarma que almacena en la base de datos el registro de actividad física automática
    private void setRecurringAlarmSavePedometer(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefrerences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        boolean PedometerAlarmReceiverSetted=sharedPreferences.getBoolean("PedometerAlarmReceiverSetted",false);

        //si no se ha fijado ya la alarma se establece a las 23:59 del dia
        if(!PedometerAlarmReceiverSetted) {

            m_alarmMgrPodometro = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ActividadFisicaAutomaticaFragment.SavePedometerAlarmReceiver.class);
            PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 0);

            m_alarmMgrPodometro.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);

            editor.putBoolean("PedometerAlarmReceiverSetted",true);
            editor.commit();
        }

    }

    //broadcast receiver que se ejecuta al saltar la alarma
    public static class SavePedometerAlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefrerences", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            boolean ActividadFisicaPermitidoRegistro = sharedPreferences.getBoolean("ActividadFisicaPermitidoRegistro", false);


            if (ActividadFisicaPermitidoRegistro) {

                boolean ActFisiRegistrada = sharedPreferences.getBoolean("ActividadFisicaRegistrada", true);
                if (!ActFisiRegistrada) {

                    boolean ActFisiAlmacenada = sharedPreferences.getBoolean("ActividadFisicaAlmacenada", true);
                    if(!ActFisiAlmacenada) {

                        float previousTotalStepsAux = sharedPreferences.getFloat("key1", 0);
                        float totalStepsAux = sharedPreferences.getFloat("key2", 0);

                        int pasos = (int) totalStepsAux - (int) previousTotalStepsAux;

                        float kcalQuemadas = (float) (pasos * KCALPORPASO);

                        float distancia = (float) (pasos * METROSPORPASO);

                        Calendar calendar = new GregorianCalendar();
                        if(calendar.get(Calendar.HOUR_OF_DAY)!=23){
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
                        }
                        calendar.set(Calendar.HOUR_OF_DAY, 12);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        editor.putInt("pasos_AFR", pasos);
                        editor.putFloat("distancia_AFR", distancia);
                        editor.putFloat("kcal_AFR", kcalQuemadas);
                        editor.putLong("fecha_AFR", calendar.getTimeInMillis());
                        editor.putBoolean("ActividadFisicaAlmacenada", true);

                        //reinicio del contador
                        previousTotalStepsAux = totalStepsAux;
                        editor.putFloat("key1", previousTotalStepsAux);
                        editor.putFloat("key2", totalStepsAux);
                        editor.commit();
                        Log.d("SavePedometerAlarmRecvr", "DATOS GUARDADOS");
                    }
                }
            }
        }
    }

    //metodo para inicializar los booleanos de controld e las preferencias
    private void inicializadasPreferences() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("myPrefrerences", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        if(prefs.getBoolean("ActividadFisicaPermitidoRegistro",false)==false){
            prefsEditor.putBoolean("ActividadFisicaPermitidoRegistro", false);
            prefsEditor.commit();
        }

    }

    //metodo para establecer los booleanos de control de las preferencias
    private void establecerPreferences() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("myPrefrerences", Context.MODE_PRIVATE);
        if(!prefs.getBoolean("ActividadFisicaPermitidoRegistro", false)){

            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putBoolean("ActividadFisicaRegistrada", false);
            prefsEditor.putBoolean("ActividadFisicaAlmacenada", false);
            prefsEditor.putBoolean("ActividadFisicaPermitidoRegistro", true);
            prefsEditor.putBoolean("PedometerAlarmReceiverResetSetted", false);
            prefsEditor.putBoolean("PedometerAlarmReceiverSetted", false);
            prefsEditor.commit();

        }

    }

    //metodo para estabolecer una a alarma para que a las 00:00 del dia se reinicien todas las preferencias de usuario
    private void restablecerPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefrerences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        boolean PedometerAlarmReceiverResetSetted=sharedPreferences.getBoolean("PedometerAlarmReceiverResetSetted",false);
        if(!PedometerAlarmReceiverResetSetted) {

            m_alarmMgrPodometro = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ActividadFisicaAutomaticaFragment.ResetPodometerPreferencesAlarmReceiver.class);
            PendingIntent m_alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, (calendar.get(Calendar.DAY_OF_MONTH)+1));
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            m_alarmMgrPodometro.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), m_alarmIntent);

            editor.putBoolean("PedometerAlarmReceiverResetSetted", true);
            editor.commit();
        }
    }

    //broadcast receiver que se ejecuta cuando la alarma anterior se ejecuta
    public static class ResetPodometerPreferencesAlarmReceiver extends BroadcastReceiver {

        //este método reinicia todas las preferencias de usuario a sus valores por defecto
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences sharedPreferences=context.getSharedPreferences("myPrefrerences", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor= sharedPreferences.edit();

            editor.putBoolean("ActividadFisicaRegistrada", false);
            editor.putBoolean("ActividadFisicaAlmacenada", false);
            editor.putBoolean("ActividadFisicaPermitidoRegistro", false);

            editor.putBoolean("PedometerAlarmReceiverResetSetted", false);
            editor.putBoolean("PedometerAlarmReceiverSetted", false);

            editor.putInt("pasos_AFR", 0);
            editor.putFloat("distancia_AFR", 0);
            editor.putFloat("kcal_AFR", 0);
            editor.putLong("fecha_AFR", 0);

            editor.commit();

        }
    }

}
