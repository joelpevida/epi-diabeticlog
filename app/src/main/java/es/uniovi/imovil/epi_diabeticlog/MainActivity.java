package es.uniovi.imovil.epi_diabeticlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.View.RegistroActividadFisicaManualFragment;
import es.uniovi.imovil.epi_diabeticlog.Alimentacion.View.AlimentacionFragment;
import es.uniovi.imovil.epi_diabeticlog.Glucosa.View.GlucosaFragment;
import es.uniovi.imovil.epi_diabeticlog.Insulina.View.InsulinaFragment;
import es.uniovi.imovil.epi_diabeticlog.PantallaInicio.View.PantallaInicioFragment;
import es.uniovi.imovil.epi_diabeticlog.Usuario.View.Communication_PerfilFragment_MainActivity;
import es.uniovi.imovil.epi_diabeticlog.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements Communication_PerfilFragment_MainActivity{

    private ActivityMainBinding binding;

    private AppBarConfiguration mAppBarConfiguration;

    private TextView nombre_usuario_etiqueta;

    private static final String PREFERENCES_USER="USER_PREFERENCES";
    private static String nombre_usuario;

    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private PantallaInicioFragment pantallaInicioFragment;
    private RegistroActividadFisicaManualFragment registroActividadFisicaManualFragment;
    private AlimentacionFragment alimentacionFragment;
    private InsulinaFragment insulinaFragment;
    private GlucosaFragment glucosaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_pantallainicio,R.id.nav_alimentacion, R.id.nav_registroactividadfisicaautomatica, R.id.nav_registroactividadfisicamanual, R.id.nav_insulina, R.id.nav_glucosa, R.id.nav_perfil)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        SharedPreferences prefs = this.getSharedPreferences(PREFERENCES_USER, Context.MODE_PRIVATE);
        nombre_usuario=prefs.getString("nombre_usuario", "");

        fragmentManager= getSupportFragmentManager();
        pantallaInicioFragment = (PantallaInicioFragment) fragmentManager.findFragmentById(R.id.nav_pantallainicio);
        registroActividadFisicaManualFragment = (RegistroActividadFisicaManualFragment) fragmentManager.findFragmentById(R.id.nav_registroactividadfisicamanual);
        alimentacionFragment = (AlimentacionFragment) fragmentManager.findFragmentById(R.id.nav_alimentacion);
        insulinaFragment = (InsulinaFragment) fragmentManager.findFragmentById(R.id.nav_insulina);
        glucosaFragment = (GlucosaFragment) fragmentManager.findFragmentById(R.id.nav_glucosa);

        this.setNombreNavegacion(nombre_usuario);
    }

    //metodo que establece el string que se le pasa como parametro en el menu de navegación
    public void setNombreNavegacion(String nombre_usuario) {
        View headerView=navigationView.getHeaderView(0);
        nombre_usuario_etiqueta=headerView.findViewById(R.id.nombre_usuario_etiqueta);
        nombre_usuario_etiqueta.setText(nombre_usuario);
    }

    //se pone el manu establecido, en este caso vacío
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}