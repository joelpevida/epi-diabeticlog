package es.uniovi.imovil.epi_diabeticlog;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uniovi.imovil.epi_diabeticlog.Usuario.View.LoginActivity;

import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed;
import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;
import static com.schibsted.spain.barista.interaction.BaristaDrawerInteractions.openDrawer;
import static com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo;
import static com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.closeKeyboard;
import static com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTestUSerInterfaceLogIn {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    @Test
    public void testSignUp(){

        String nombre="Joel";

        clickOn(R.id.nombre_usuario_registro);
        writeTo(R.id.nombre_usuario_registro, nombre);
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_1);
        writeTo(R.id.digito_pin_registro_1, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_2);
        writeTo(R.id.digito_pin_registro_2, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_3);
        writeTo(R.id.digito_pin_registro_3, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_4);
        writeTo(R.id.digito_pin_registro_4, "1");
        closeKeyboard();

        clickOn(R.id.boton_registrarme);

        openDrawer();

        assertDisplayed(R.id.nombre_usuario_etiqueta, nombre);
    }

    /*
    Debe ejecutarse justo después del metodoo testSignUp, sino dara error
     */
    @Test
    public void testLogIn(){

        String nombre="Joel";

        clickOn(R.id.digito_pin_login_1);
        writeTo(R.id.digito_pin_login_1, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_login_2);
        writeTo(R.id.digito_pin_login_2, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_login_3);
        writeTo(R.id.digito_pin_login_3, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_login_4);
        writeTo(R.id.digito_pin_login_4, "1");
        closeKeyboard();

        clickOn(R.id.boton_login);

        openDrawer();

        assertDisplayed(R.id.nombre_usuario_etiqueta, nombre);
    }

    /*
    Debe ejecutarse justo después del metodoo testSignUp, sino dara error
    */
    @Test
    public void testLogInWornPIN(){

        String nombre="Joel";

        clickOn(R.id.digito_pin_login_1);
        writeTo(R.id.digito_pin_login_1, "2");
        closeKeyboard();

        clickOn(R.id.digito_pin_login_2);
        writeTo(R.id.digito_pin_login_2, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_login_3);
        writeTo(R.id.digito_pin_login_3, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_login_4);
        writeTo(R.id.digito_pin_login_4, "1");
        closeKeyboard();

        clickOn(R.id.boton_login);

        assertDisplayed("PIN");
    }

    @Test
    public void testChangeDataPerfil(){

        String nombre="Joel";

        clickOn(R.id.nombre_usuario_registro);
        writeTo(R.id.nombre_usuario_registro, nombre);
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_1);
        writeTo(R.id.digito_pin_registro_1, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_2);
        writeTo(R.id.digito_pin_registro_2, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_3);
        writeTo(R.id.digito_pin_registro_3, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_4);
        writeTo(R.id.digito_pin_registro_4, "1");
        closeKeyboard();

        clickOn(R.id.boton_registrarme);


        String nombreNuevo= "Joel Pevida";

        openDrawer();

        clickMenu(R.id.nav_perfil);

        clickOn(R.id.nombre_usuario_edit);
        writeTo(R.id.nombre_usuario_edit, nombreNuevo);
        closeKeyboard();

        clickOn(R.id.peso_usuario_edit);
        writeTo(R.id.peso_usuario_edit,"80");
        closeKeyboard();

        clickOn(R.id.altura_usuario_edit);
        writeTo(R.id.altura_usuario_edit,"180");
        closeKeyboard();

        clickOn(R.id.boton_guardar_cambios);

        openDrawer();

        assertDisplayed(R.id.nombre_usuario_etiqueta, nombreNuevo);

    }

    @Test
    public void testNavegacionPerfil(){

        String nombre="Joel";

        clickOn(R.id.nombre_usuario_registro);
        writeTo(R.id.nombre_usuario_registro, nombre);
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_1);
        writeTo(R.id.digito_pin_registro_1, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_2);
        writeTo(R.id.digito_pin_registro_2, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_3);
        writeTo(R.id.digito_pin_registro_3, "1");
        closeKeyboard();

        clickOn(R.id.digito_pin_registro_4);
        writeTo(R.id.digito_pin_registro_4, "1");
        closeKeyboard();

        clickOn(R.id.boton_registrarme);

        openDrawer();

        clickMenu(R.id.nav_perfil);

        assertDisplayed(R.string.menu_perfil);

    }

}
