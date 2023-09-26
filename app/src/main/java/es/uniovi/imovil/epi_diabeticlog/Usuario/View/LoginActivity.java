package es.uniovi.imovil.epi_diabeticlog.Usuario.View;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProvider;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import java.util.List;

        import es.uniovi.imovil.epi_diabeticlog.MainActivity;
        import es.uniovi.imovil.epi_diabeticlog.R;
        import es.uniovi.imovil.epi_diabeticlog.Usuario.Model.Usuario;
        import es.uniovi.imovil.epi_diabeticlog.Usuario.ViewModel.UsuarioViewModel;
        import es.uniovi.imovil.epi_diabeticlog.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private ActivityLoginBinding binding;

    private UsuarioViewModel viewModel;
    private Usuario usuario;

    private EditText digito1;
    private EditText digito2;
    private EditText digito3;
    private EditText digito4;

    private Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityLoginBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        viewModel.getUsuarios().observe(this, new Observer<List<Usuario>>() {
            @Override
            public void onChanged(@Nullable List<Usuario> usuarios) {
                if(usuarios==null||usuarios.size()==0){
                    callSignUp();
                }else {
                    usuario = usuarios.get(0);
                    setContentView(view);
                }
            }
        });

        digito1=binding.digitoPinLogin1;
        digito2=binding.digitoPinLogin2;
        digito3=binding.digitoPinLogin3;
        digito4=binding.digitoPinLogin4;

        digito1.addTextChangedListener(this);
        digito2.addTextChangedListener(this);
        digito3.addTextChangedListener(this);
        digito4.addTextChangedListener(this);

        boton=binding.botonLogin;
        boton.setOnClickListener(this);

    }

    //metodo que al ser llamado lanza la actividad para que el usuario se registre
    private void callSignUp() {
        Intent intent=new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    //metodo on click que se ejecuta al pulsar el boton para hacer log in
    @Override
    public void onClick(View v) {
        if(v.getId()==boton.getId()){
            String pin = "";
            pin = pin + digito1.getText().toString();
            pin = pin + digito2.getText().toString();
            pin = pin + digito3.getText().toString();
            pin = pin + digito4.getText().toString();
            if(pin.equals(usuario.getPin())) {

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, getResources().getString(R.string.PinIncorrecto), Toast.LENGTH_SHORT).show();
            }
        }
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
                        boton.setFocusable(true);
                        boton.setFocusableInTouchMode(true);
                        boton.requestFocus();
                        boton.setCursorVisible(true);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(boton.getWindowToken(), 0);
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