package es.uniovi.imovil.epi_diabeticlog.Usuario.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.uniovi.imovil.epi_diabeticlog.MainActivity;
import es.uniovi.imovil.epi_diabeticlog.R;
import es.uniovi.imovil.epi_diabeticlog.Usuario.Model.Usuario;
import es.uniovi.imovil.epi_diabeticlog.Usuario.ViewModel.UsuarioViewModel;
import es.uniovi.imovil.epi_diabeticlog.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private ActivitySignUpBinding binding;

    private EditText usuario;
    private String nombre_usuario;

    private String uuid_string;

    private EditText digito1;
    private EditText digito2;
    private EditText digito3;
    private EditText digito4;

    private Button boton_registrarme;

    private UsuarioViewModel viewModel;

    private static final String PREFERENCES="USER_PREFERENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivitySignUpBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);

        boton_registrarme=findViewById(R.id.boton_registrarme);
        boton_registrarme.setOnClickListener(this);

        usuario=binding.nombreUsuarioRegistro;

        digito1=binding.digitoPinRegistro1;
        digito2=binding.digitoPinRegistro2;
        digito3=binding.digitoPinRegistro3;
        digito4=binding.digitoPinRegistro4;

        UUID uuid = UUID.randomUUID();
        uuid_string = uuid.toString();

        digito1.addTextChangedListener(this);
        digito2.addTextChangedListener(this);
        digito3.addTextChangedListener(this);
        digito4.addTextChangedListener(this);

    }

    //metodo on click que se ejecuta al pulsar el boton para registrarse
    @Override
    public void onClick(View v) {
        if(v.getId()==boton_registrarme.getId()) {

            try {
                String pin = "";

                String stringDigito1 = digito1.getText().toString();
                String stringDigito2 = digito2.getText().toString();
                String stringDigito3 = digito3.getText().toString();
                String stringDigito4 = digito4.getText().toString();

                pin = pin + stringDigito1;
                pin = pin + stringDigito2;
                pin = pin + stringDigito3;
                pin = pin + stringDigito4;

                nombre_usuario = usuario.getText().toString();

                if (nombre_usuario != null && !nombre_usuario.isEmpty()) {
                    if ((stringDigito1 != null && !stringDigito1.isEmpty()) && (stringDigito2 != null && !stringDigito2.isEmpty()) && (stringDigito3 != null && !stringDigito3.isEmpty()) && (stringDigito4 != null && !stringDigito4.isEmpty())) {
                        Usuario user = new Usuario(uuid_string, nombre_usuario, 0, 0, pin);
                        viewModel.insert(user);
                        this.saveUUIDPreferences();
                        this.saveNombreUsuarioPreferences();
                        this.startMainActivity();
                    }
                    else{
                        Toast.makeText(this, getResources().getString(R.string.IntroduzcaDatosSignUp), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, getResources().getString(R.string.IntroduzcaDatosSignUp), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.IntroduzcaDatosSignUp), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //metodo que inicializa MainActivity
    private void startMainActivity() {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //metodo que guarda el uuid del usuario en las shared preferences
    private void saveUUIDPreferences(){
        SharedPreferences prefs = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("UUID_usuario", uuid_string);
        prefsEditor.commit();
    }

    //metodo que guarda el nombre del usuario en las shared preferences
    private void saveNombreUsuarioPreferences(){
        SharedPreferences prefs = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("nombre_usuario", nombre_usuario);
        prefsEditor.commit();
    }

    //metodo que hace que al rellenarse una casilla del pin se pase a la siguiente
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(digito1.isFocused()&digito1.length()==1)
        {
            digito1.clearFocus();
            digito2.requestFocus();
            digito2.setCursorVisible(true);

        }
        else{
            if(digito2.isFocused()&digito2.length()==1)
            {
                digito2.clearFocus();
                digito3.requestFocus();
                digito3.setCursorVisible(true);

            }
            else {
                if(digito3.isFocused()&digito3.length()==1)
                {
                    digito3.clearFocus();
                    digito4.requestFocus();
                    digito4.setCursorVisible(true);

                }
                else{
                    if(digito4.isFocused()&digito4.length()==1)
                    {
                        digito4.clearFocus();
                        boton_registrarme.setFocusable(true);
                        boton_registrarme.setFocusableInTouchMode(true);///add this line
                        boton_registrarme.requestFocus();
                        boton_registrarme.setCursorVisible(true);

                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(boton_registrarme.getWindowToken(), 0);

                    }
                }
            }
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}