package es.uniovi.imovil.epi_diabeticlog;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManual;
import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.Ingesta;
import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;
import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;

import static com.schibsted.spain.barista.assertion.BaristaClickableAssertions.assertNotClickable;
import static com.schibsted.spain.barista.assertion.BaristaEnabledAssertions.assertDisabled;
import static com.schibsted.spain.barista.assertion.BaristaEnabledAssertions.assertEnabled;
import static com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition;
import static com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListItemCount;
import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed;
import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickBack;
import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;
import static com.schibsted.spain.barista.interaction.BaristaDrawerInteractions.openDrawer;
import static com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo;
import static com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.closeKeyboard;
import static com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem;
import static com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItemChild;
import static com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu;
import static com.schibsted.spain.barista.interaction.BaristaPickerInteractions.setTimeOnPicker;
import static com.schibsted.spain.barista.interaction.BaristaSpinnerInteractions.clickSpinnerItem;


@RunWith(AndroidJUnit4.class)
public class InstrumentedTestUserInterface {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testNavegacionInicio(){

        openDrawer();

        clickMenu(R.id.nav_pantallainicio);

        assertDisplayed(R.string.menu_pantalla_inicio);

    }

    @Test
    public void testNavegacionAlimentacion(){

        openDrawer();

        clickMenu(R.id.nav_alimentacion);

        assertDisplayed(R.string.menu_alimentacion);

    }

    @Test
    public void testNavegacionActividadAutomatica(){

        openDrawer();

        clickMenu(R.id.nav_registroactividadfisicaautomatica);

        assertDisplayed(R.string.menu_registroactividadfisicaautomatica);

    }

    @Test
    public void testNavegacionActividadManual(){

        openDrawer();

        clickMenu(R.id.nav_registroactividadfisicamanual);

        assertDisplayed(R.string.menu_registroactividadfisicamanual);

    }

    @Test
    public void testNavegacionInsulina(){

        openDrawer();

        clickMenu(R.id.nav_insulina);

        assertDisplayed(R.string.menu_insulina);

    }

    @Test
    public void testNavegacionGlucosa(){

        openDrawer();

        clickMenu(R.id.nav_glucosa);

        assertDisplayed(R.string.menu_glucosa);

    }



    @Test
    public void testAddIngesta(){

        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        openDrawer();

        clickMenu(R.id.nav_alimentacion);

        clickOn(R.id.boton_add_ingesta);

        clickSpinnerItem(R.id.spinner_tipos_ingesta, 0);

        clickOn(R.id.num_raciones);
        writeTo(R.id.num_raciones, "9");
        closeKeyboard();

        clickOn(R.id.hora_ingesta);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_ingesta);

        assertDisplayedAtPosition(R.id.recycler_ingestas, 0, R.id.nombre_ingesta_etiqueta, R.string.Desayuno);
        assertDisplayedAtPosition(R.id.recycler_ingestas, 0, R.id.num_raciones_ingesta_etiqueta, "9.0" + " " + context.getResources().getString(R.string.raciones));
        assertDisplayedAtPosition(R.id.recycler_ingestas, 0, R.id.hora_ingesta_etiqueta, "9:30 AM");

        clickListItemChild(R.id.recycler_ingestas, 0, R.id.btn_eliminar_ingesta);
    }

    @Test
    public void testModifyIngesta(){

        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        openDrawer();

        clickMenu(R.id.nav_alimentacion);

        clickOn(R.id.boton_add_ingesta);

        clickSpinnerItem(R.id.spinner_tipos_ingesta, 0);

        clickOn(R.id.num_raciones);
        writeTo(R.id.num_raciones, "9");
        closeKeyboard();


        clickOn(R.id.hora_ingesta);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_ingesta);

        clickListItemChild(R.id.recycler_ingestas, 0, R.id.btn_modificar_ingesta);

        clickOn(R.id.etiqueta_num_raciones_ingesta_edit);
        writeTo(R.id.etiqueta_num_raciones_ingesta_edit, "5");
        closeKeyboard();

        clickOn(R.id.etiqueta_hora_ingesta_edit);
        setTimeOnPicker(7, 00);

        clickOn(R.id.boton_guardar_cambios_ingesta);

        assertDisplayedAtPosition(R.id.recycler_ingestas, 0, R.id.nombre_ingesta_etiqueta, R.string.Desayuno);
        assertDisplayedAtPosition(R.id.recycler_ingestas, 0, R.id.num_raciones_ingesta_etiqueta, "5.0" + " " + context.getResources().getString(R.string.raciones));
        assertDisplayedAtPosition(R.id.recycler_ingestas, 0, R.id.hora_ingesta_etiqueta, "7:00 AM");

        clickListItemChild(R.id.recycler_ingestas, 0, R.id.btn_eliminar_ingesta);
    }

    @Test
    public void testRemoveIngesta(){

        openDrawer();

        clickMenu(R.id.nav_alimentacion);

        clickOn(R.id.boton_add_ingesta);

        clickSpinnerItem(R.id.spinner_tipos_ingesta, 0);

        clickOn(R.id.num_raciones);
        writeTo(R.id.num_raciones, "9");
        closeKeyboard();

        clickOn(R.id.hora_ingesta);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_ingesta);

        clickListItemChild(R.id.recycler_ingestas, 0, R.id.btn_eliminar_ingesta);

        assertListItemCount(R.id.recycler_ingestas, 0);

    }


    @Test
    public void testModifyIngestaNombreReservado(){

        openDrawer();

        clickMenu(R.id.nav_alimentacion);

        clickOn(R.id.boton_add_ingesta);

        clickSpinnerItem(R.id.spinner_tipos_ingesta, 0);

        clickOn(R.id.num_raciones);
        writeTo(R.id.num_raciones, "9");
        closeKeyboard();

        clickOn(R.id.hora_ingesta);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_ingesta);

        clickListItemChild(R.id.recycler_ingestas, 0, R.id.btn_modificar_ingesta);

        assertDisabled(R.id.etiqueta_nombre_ingesta_edit);

        clickBack();

        clickListItemChild(R.id.recycler_ingestas, 0, R.id.btn_eliminar_ingesta);

    }

    @Test
    public void testModifyIngestaNombreLibre(){

        openDrawer();

        clickMenu(R.id.nav_alimentacion);

        clickOn(R.id.boton_add_ingesta);

        clickSpinnerItem(R.id.spinner_tipos_ingesta, 5);

        clickOn(R.id.num_raciones);
        writeTo(R.id.num_raciones, "9");
        closeKeyboard();

        clickOn(R.id.hora_ingesta);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_ingesta);

        clickListItemChild(R.id.recycler_ingestas, 0, R.id.btn_modificar_ingesta);

        assertEnabled(R.id.etiqueta_nombre_ingesta_edit);

        String comidaExtra="Comida extra";

        clickOn(R.id.etiqueta_nombre_ingesta_edit);
        writeTo(R.id.etiqueta_nombre_ingesta_edit, comidaExtra);
        closeKeyboard();

        clickOn(R.id.boton_guardar_cambios_ingesta);

        assertDisplayedAtPosition(R.id.recycler_ingestas, 0, R.id.nombre_ingesta_etiqueta, comidaExtra);

        clickListItemChild(R.id.recycler_ingestas, 0, R.id.btn_eliminar_ingesta);
    }

    @Test
    public void testRepeatAddIngestaNombreRegistrado(){

        openDrawer();

        clickMenu(R.id.nav_alimentacion);

        clickOn(R.id.boton_add_ingesta);

        clickSpinnerItem(R.id.spinner_tipos_ingesta, 0);

        clickOn(R.id.num_raciones);
        writeTo(R.id.num_raciones, "9");
        closeKeyboard();

        clickOn(R.id.hora_ingesta);

        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_ingesta);

        clickOn(R.id.boton_add_ingesta);

        clickSpinnerItem(R.id.spinner_tipos_ingesta, 0);

        clickOn(R.id.num_raciones);
        writeTo(R.id.num_raciones, "10");
        closeKeyboard();

        clickOn(R.id.hora_ingesta);

        setTimeOnPicker(10, 30);

        clickOn(R.id.boton_agregar_ingesta);

        assertListItemCount(R.id.recycler_ingestas, 1);

        clickListItemChild(R.id.recycler_ingestas, 0, R.id.btn_eliminar_ingesta);
    }

    @Test
    public void testAddIngestaNoHora(){

        openDrawer();

        clickMenu(R.id.nav_alimentacion);

        clickOn(R.id.boton_add_ingesta);

        clickSpinnerItem(R.id.spinner_tipos_ingesta, 0);

        clickOn(R.id.num_raciones);
        writeTo(R.id.num_raciones, "9");
        closeKeyboard();

        clickOn(R.id.boton_agregar_ingesta);

        clickBack();

        assertListItemCount(R.id.recycler_ingestas, 0);
    }

    @Test
    public void testAddIngestaNoRaciones(){

        openDrawer();

        clickMenu(R.id.nav_alimentacion);

        clickOn(R.id.boton_add_ingesta);

        clickSpinnerItem(R.id.spinner_tipos_ingesta, 0);

        clickOn(R.id.hora_ingesta);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_ingesta);

        clickBack();

        assertListItemCount(R.id.recycler_ingestas, 0);
    }

    @Test
    public void testAddRegistroManual(){

        openDrawer();

        clickMenu(R.id.nav_registroactividadfisicamanual);

        clickOn(R.id.boton_add_registro_act_fis_manual);

        clickSpinnerItem(R.id.spinner_tipo_actividad, 0);

        clickSpinnerItem(R.id.spinner_intensidad, 0);

        clickOn(R.id.kcal_quemadas_input);
        writeTo(R.id.kcal_quemadas_input, "220");
        closeKeyboard();

        clickOn(R.id.hora_inicio);
        setTimeOnPicker(16, 00);

        clickOn(R.id.hora_final);
        setTimeOnPicker(18, 30);

        clickOn(R.id.boton_agregar_registro);

        assertDisplayedAtPosition(R.id.recycler_registro_act_fis_manual, 0, R.id.tipo_actividad, R.string.Correr);
        assertDisplayedAtPosition(R.id.recycler_registro_act_fis_manual, 0, R.id.intensidad_actividad, R.string.Alta);
        assertDisplayedAtPosition(R.id.recycler_registro_act_fis_manual, 0, R.id.kcal_quemadas, "220.0");
        assertDisplayedAtPosition(R.id.recycler_registro_act_fis_manual, 0, R.id.hora_inicio_actividad, "16:00 PM");
        assertDisplayedAtPosition(R.id.recycler_registro_act_fis_manual, 0, R.id.hora_final_actividad, "18:30 PM");

        clickListItemChild(R.id.recycler_registro_act_fis_manual, 0, R.id.btn_eliminar_registro);
    }


    @Test
    public void testRemoveRegistroManual(){

        openDrawer();

        clickMenu(R.id.nav_registroactividadfisicamanual);

        clickOn(R.id.boton_add_registro_act_fis_manual);

        clickSpinnerItem(R.id.spinner_tipo_actividad, 0);

        clickSpinnerItem(R.id.spinner_intensidad, 0);

        clickOn(R.id.kcal_quemadas_input);
        writeTo(R.id.kcal_quemadas_input, "220");
        closeKeyboard();

        clickOn(R.id.hora_inicio);
        setTimeOnPicker(16, 00);

        clickOn(R.id.hora_final);
        setTimeOnPicker(18, 30);

        clickOn(R.id.boton_agregar_registro);

        clickListItemChild(R.id.recycler_registro_act_fis_manual, 0, R.id.btn_eliminar_registro);

        assertListItemCount(R.id.recycler_registro_act_fis_manual, 0);

    }

    @Test
    public void testModifyRegistroManual(){

        openDrawer();

        clickMenu(R.id.nav_registroactividadfisicamanual);

        clickOn(R.id.boton_add_registro_act_fis_manual);

        clickSpinnerItem(R.id.spinner_tipo_actividad, 0);

        clickSpinnerItem(R.id.spinner_intensidad, 0);

        clickOn(R.id.kcal_quemadas_input);
        writeTo(R.id.kcal_quemadas_input, "220");
        closeKeyboard();

        clickOn(R.id.hora_inicio);
        setTimeOnPicker(16, 00);

        clickOn(R.id.hora_final);
        setTimeOnPicker(18, 30);

        clickOn(R.id.boton_agregar_registro);

        clickListItemChild(R.id.recycler_registro_act_fis_manual, 0, R.id.btn_modificar_registro);

        clickSpinnerItem(R.id.spinner_tipo_actividad_edit, 1);

        clickSpinnerItem(R.id.spinner_intensidad_edit, 1);

        clickOn(R.id.kcal_quemadas_input_edit);
        writeTo(R.id.kcal_quemadas_input_edit, "90");
        closeKeyboard();

        clickOn(R.id.hora_inicio_edit);
        setTimeOnPicker(13, 00);

        clickOn(R.id.hora_final_edit);
        setTimeOnPicker(14, 30);

        clickOn(R.id.boton_editar_registro_actualizar);

        assertDisplayedAtPosition(R.id.recycler_registro_act_fis_manual, 0, R.id.tipo_actividad, R.string.Caminar);
        assertDisplayedAtPosition(R.id.recycler_registro_act_fis_manual, 0, R.id.intensidad_actividad, R.string.Media);
        assertDisplayedAtPosition(R.id.recycler_registro_act_fis_manual, 0, R.id.kcal_quemadas, "90.0");
        assertDisplayedAtPosition(R.id.recycler_registro_act_fis_manual, 0, R.id.hora_inicio_actividad, "13:00 PM");
        assertDisplayedAtPosition(R.id.recycler_registro_act_fis_manual, 0, R.id.hora_final_actividad, "14:30 PM");

        clickListItemChild(R.id.recycler_registro_act_fis_manual, 0, R.id.btn_eliminar_registro);
    }

    @Test
    public void testAddRegistroManualNoInicio(){

        openDrawer();

        clickMenu(R.id.nav_registroactividadfisicamanual);

        clickOn(R.id.boton_add_registro_act_fis_manual);

        clickSpinnerItem(R.id.spinner_tipo_actividad, 0);

        clickSpinnerItem(R.id.spinner_intensidad, 0);

        clickOn(R.id.kcal_quemadas_input);
        writeTo(R.id.kcal_quemadas_input, "220");
        closeKeyboard();

        clickOn(R.id.hora_final);
        setTimeOnPicker(18, 00);

        clickOn(R.id.boton_agregar_registro);
        clickBack();

        assertListItemCount(R.id.recycler_registro_act_fis_manual, 0);
    }

    @Test
    public void testAddRegistroManualNoFinal(){

        openDrawer();

        clickMenu(R.id.nav_registroactividadfisicamanual);

        clickOn(R.id.boton_add_registro_act_fis_manual);

        clickSpinnerItem(R.id.spinner_tipo_actividad, 0);

        clickSpinnerItem(R.id.spinner_intensidad, 0);

        clickOn(R.id.kcal_quemadas_input);
        writeTo(R.id.kcal_quemadas_input, "220");
        closeKeyboard();

        clickOn(R.id.hora_inicio);
        setTimeOnPicker(14, 00);

        clickOn(R.id.boton_agregar_registro);
        clickBack();

        assertListItemCount(R.id.recycler_registro_act_fis_manual, 0);
    }

    @Test
    public void testAddRegistroManualInicioBeforeFinal(){

        openDrawer();

        clickMenu(R.id.nav_registroactividadfisicamanual);

        clickOn(R.id.boton_add_registro_act_fis_manual);

        clickSpinnerItem(R.id.spinner_tipo_actividad, 0);

        clickSpinnerItem(R.id.spinner_intensidad, 0);

        clickOn(R.id.kcal_quemadas_input);
        writeTo(R.id.kcal_quemadas_input, "220");
        closeKeyboard();

        clickOn(R.id.hora_inicio);
        setTimeOnPicker(18, 00);

        clickOn(R.id.hora_final);
        setTimeOnPicker(14, 00);

        clickOn(R.id.boton_agregar_registro);
        clickBack();

        assertListItemCount(R.id.recycler_registro_act_fis_manual, 0);
    }

    @Test
    public void testAddRegistroManualNoCalorias(){

        openDrawer();

        clickMenu(R.id.nav_registroactividadfisicamanual);

        clickOn(R.id.boton_add_registro_act_fis_manual);

        clickSpinnerItem(R.id.spinner_tipo_actividad, 0);

        clickSpinnerItem(R.id.spinner_intensidad, 0);


        clickOn(R.id.hora_inicio);
        setTimeOnPicker(16, 00);

        clickOn(R.id.hora_final);
        setTimeOnPicker(18, 30);

        clickOn(R.id.boton_agregar_registro);

        clickBack();

        assertListItemCount(R.id.recycler_registro_act_fis_manual, 0);
    }


    @Test
    public void testAddInsulina(){

        openDrawer();

        clickMenu(R.id.nav_insulina);

        clickOn(R.id.btn_add_aplicacion_insulina);

        clickSpinnerItem(R.id.spinner_tipo_insulina, 0);

        //clickSpinnerItem(R.id.spinner_tipo_aplicacion, 0);

        clickOn(R.id.num_unidades_item);
        writeTo(R.id.num_unidades_item, "2");
        closeKeyboard();

        clickOn(R.id.hora_aplicacion);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_aplicacion);

        assertDisplayedAtPosition(R.id.recycler_aplicaciones_insulina, 0, R.id.tipo_insulina_item, R.string.Lenta);
        assertDisplayedAtPosition(R.id.recycler_aplicaciones_insulina, 0, R.id.tipo_aplicacion_item, R.string.Normal);
        assertDisplayedAtPosition(R.id.recycler_aplicaciones_insulina, 0, R.id.hora_aplicacion_item, "9:30 AM");
        assertDisplayedAtPosition(R.id.recycler_aplicaciones_insulina, 0, R.id.num_unidades_item, "2");


        clickListItemChild(R.id.recycler_aplicaciones_insulina, 0, R.id.btn_eliminar_aplicacion_insulina);
    }

    @Test
    public void testModifyInsulina(){

        openDrawer();

        clickMenu(R.id.nav_insulina);

        clickOn(R.id.btn_add_aplicacion_insulina);

        clickSpinnerItem(R.id.spinner_tipo_insulina, 0);

        clickOn(R.id.num_unidades_item);
        writeTo(R.id.num_unidades_item, "2");
        closeKeyboard();

        clickOn(R.id.hora_aplicacion);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_aplicacion);

        clickListItemChild(R.id.recycler_aplicaciones_insulina, 0, R.id.btn_modificar_aplicacion_insulina);

        clickSpinnerItem(R.id.spinner_tipo_insulina_edit, 1);

        clickOn(R.id.num_unidades_edit);
        writeTo(R.id.num_unidades_edit, "1");
        closeKeyboard();

        clickOn(R.id.hora_aplicacion_edit);
        setTimeOnPicker(6, 15);

        clickOn(R.id.boton_aceptar_cambios_aplicacion);

        assertDisplayedAtPosition(R.id.recycler_aplicaciones_insulina, 0, R.id.tipo_insulina_item, R.string.Intermedia);
        assertDisplayedAtPosition(R.id.recycler_aplicaciones_insulina, 0, R.id.tipo_aplicacion_item, R.string.Normal);
        assertDisplayedAtPosition(R.id.recycler_aplicaciones_insulina, 0, R.id.hora_aplicacion_item, "6:15 AM");
        assertDisplayedAtPosition(R.id.recycler_aplicaciones_insulina, 0, R.id.num_unidades_item, "1");


        clickListItemChild(R.id.recycler_aplicaciones_insulina, 0, R.id.btn_eliminar_aplicacion_insulina);
    }

    @Test
    public void testRemoveInsulina(){

        openDrawer();

        clickMenu(R.id.nav_insulina);

        clickOn(R.id.btn_add_aplicacion_insulina);

        clickSpinnerItem(R.id.spinner_tipo_insulina, 0);

        clickOn(R.id.num_unidades_item);
        writeTo(R.id.num_unidades_item, "2");
        closeKeyboard();

        clickOn(R.id.hora_aplicacion);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_aplicacion);

        clickListItemChild(R.id.recycler_aplicaciones_insulina, 0, R.id.btn_eliminar_aplicacion_insulina);

        assertListItemCount(R.id.recycler_aplicaciones_insulina, 0);

    }


    @Test
    public void testInsulinaNotEnabledAplicacion(){

        openDrawer();

        clickMenu(R.id.nav_insulina);

        clickOn(R.id.btn_add_aplicacion_insulina);

        clickSpinnerItem(R.id.spinner_tipo_insulina, 0);

        assertDisabled(R.id.spinner_tipo_aplicacion);

    }


    @Test
    public void testInsulinaEnabledAplicacion(){

        openDrawer();

        clickMenu(R.id.nav_insulina);

        clickOn(R.id.btn_add_aplicacion_insulina);

        clickSpinnerItem(R.id.spinner_tipo_insulina, 2);

        assertEnabled(R.id.spinner_tipo_aplicacion);

    }


    @Test
    public void testAddInsulinaNoHora(){

        openDrawer();

        clickMenu(R.id.nav_insulina);

        clickOn(R.id.btn_add_aplicacion_insulina);

        clickSpinnerItem(R.id.spinner_tipo_insulina, 0);


        clickOn(R.id.num_unidades_item);
        writeTo(R.id.num_unidades_item, "2");
        closeKeyboard();

        clickOn(R.id.boton_agregar_aplicacion);
        clickBack();

        assertListItemCount(R.id.recycler_aplicaciones_insulina, 0);
    }

    @Test
    public void testAddInsulinaNoUnidades(){

        openDrawer();

        clickMenu(R.id.nav_insulina);

        clickOn(R.id.btn_add_aplicacion_insulina);

        clickSpinnerItem(R.id.spinner_tipo_insulina, 0);


        clickOn(R.id.hora_aplicacion);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_aplicacion);
        clickBack();

        assertListItemCount(R.id.recycler_aplicaciones_insulina, 0);
    }



    @Test
    public void testAddGlucosa(){

        openDrawer();

        clickMenu(R.id.nav_glucosa);

        clickOn(R.id.boton_add_glucosa);

        clickOn(R.id.horaMedicion_input);
        setTimeOnPicker(9, 30);

        clickOn(R.id.mgDL_input);
        writeTo(R.id.mgDL_input, "180");
        closeKeyboard();

        clickOn(R.id.boton_agregar_glucosa);

        assertDisplayedAtPosition(R.id.recycler_glucosa, 0, R.id.mgDl, "180.0");
        assertDisplayedAtPosition(R.id.recycler_glucosa, 0, R.id.horaMedicion, "9:30 AM");

        clickListItemChild(R.id.recycler_glucosa, 0, R.id.btn_eliminar_glucosa);
    }

    @Test
    public void testModifyGlucosa(){

        openDrawer();

        clickMenu(R.id.nav_glucosa);

        clickOn(R.id.boton_add_glucosa);

        clickOn(R.id.horaMedicion_input);
        setTimeOnPicker(9, 30);

        clickOn(R.id.mgDL_input);
        writeTo(R.id.mgDL_input, "180");
        closeKeyboard();

        clickOn(R.id.boton_agregar_glucosa);

        clickListItemChild(R.id.recycler_glucosa, 0, R.id.btn_modificar_glucosa);

        clickOn(R.id.horaMedicion_input_edit);
        setTimeOnPicker(20, 45);

        clickOn(R.id.mgDL_input_edit);
        writeTo(R.id.mgDL_input_edit, "120");
        closeKeyboard();

        clickOn(R.id.boton_editar_glucosa);

        assertDisplayedAtPosition(R.id.recycler_glucosa, 0, R.id.mgDl, "120.0");
        assertDisplayedAtPosition(R.id.recycler_glucosa, 0, R.id.horaMedicion, "20:45 PM");

        clickListItemChild(R.id.recycler_glucosa, 0, R.id.btn_eliminar_glucosa);
    }

    @Test
    public void testRemoveGlucosa(){

        openDrawer();

        clickMenu(R.id.nav_glucosa);

        clickOn(R.id.boton_add_glucosa);

        clickOn(R.id.horaMedicion_input);
        setTimeOnPicker(9, 30);

        clickOn(R.id.mgDL_input);
        writeTo(R.id.mgDL_input, "180");
        closeKeyboard();

        clickOn(R.id.boton_agregar_glucosa);

        clickListItemChild(R.id.recycler_glucosa, 0, R.id.btn_eliminar_glucosa);

        assertListItemCount(R.id.recycler_glucosa, 0);

    }


    @Test
    public void testAddGlucosaNoHora(){

        openDrawer();

        clickMenu(R.id.nav_glucosa);

        clickOn(R.id.boton_add_glucosa);

        clickOn(R.id.mgDL_input);
        writeTo(R.id.mgDL_input, "180");
        closeKeyboard();

        clickOn(R.id.boton_agregar_glucosa);

        clickBack();

        assertListItemCount(R.id.recycler_glucosa, 0);

    }

    @Test
    public void testAddGlucosaNoMgDl(){

        openDrawer();

        clickMenu(R.id.nav_glucosa);

        clickOn(R.id.boton_add_glucosa);

        clickOn(R.id.horaMedicion_input);
        setTimeOnPicker(9, 30);

        clickOn(R.id.boton_agregar_glucosa);

        clickBack();

        assertListItemCount(R.id.recycler_glucosa, 0);


    }



    @Test
    public void testIngestaPantallaInicioRecycler(){

        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Ingesta ingestaAux= new Ingesta();
        ingestaAux.setTipo_ingesta(context.getResources().getString(R.string.Desayuno));
        ingestaAux.setNum_raciones(9);
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,8);
        c.set(Calendar.MINUTE,30);


        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
        String  time = format.format(c.getTime());



            openDrawer();
            clickMenu(R.id.nav_alimentacion);
            clickOn(R.id.boton_add_ingesta);
            clickSpinnerItem(R.id.spinner_tipos_ingesta, 0);
            clickOn(R.id.num_raciones);
            writeTo(R.id.num_raciones, "9");
            closeKeyboard();
            clickOn(R.id.hora_ingesta);
            setTimeOnPicker(8, 30);
            clickOn(R.id.boton_agregar_ingesta);


        openDrawer();
        clickMenu(R.id.nav_pantallainicio);

        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.identificador, R.string.Ingesta);
        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.elementos_item, ingestaAux.getContent());
        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.hora_item, time);


        openDrawer();
        clickMenu(R.id.nav_alimentacion);
        clickListItemChild(R.id.recycler_ingestas, 0, R.id.btn_eliminar_ingesta);

    }

    @Test
    public void testActiFisiManualPantallaInicioRecycler(){

        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();


        RegistroActividadFisicaManual registroActividadFisicaManualAux=new RegistroActividadFisicaManual();
        registroActividadFisicaManualAux.setTipo_actividad(context.getResources().getString(R.string.Correr));
        registroActividadFisicaManualAux.setIntensidad(context.getResources().getString(R.string.Alta));
        registroActividadFisicaManualAux.setKcal_quemadas(220);

        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,10);
        c.set(Calendar.MINUTE,00);

        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
        String  time = format.format(c.getTime());


        openDrawer();
        clickMenu(R.id.nav_registroactividadfisicamanual);
        clickOn(R.id.boton_add_registro_act_fis_manual);
        clickSpinnerItem(R.id.spinner_tipo_actividad, 0);
        clickSpinnerItem(R.id.spinner_intensidad, 0);
        clickOn(R.id.kcal_quemadas_input);
        writeTo(R.id.kcal_quemadas_input, "220");
        closeKeyboard();
        clickOn(R.id.hora_inicio);
        setTimeOnPicker(10, 00);
        clickOn(R.id.hora_final);
        setTimeOnPicker(12, 30);
        clickOn(R.id.boton_agregar_registro);


        openDrawer();
        clickMenu(R.id.nav_pantallainicio);

        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.identificador, R.string.ActividadFisica);
        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.elementos_item, registroActividadFisicaManualAux.getContent());
        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.hora_item, time);

        openDrawer();
        clickMenu(R.id.nav_registroactividadfisicamanual);
        clickListItemChild(R.id.recycler_registro_act_fis_manual, 0, R.id.btn_eliminar_registro);

    }

    @Test
    public void testInsulinaPantallaInicioRecycler(){

        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();


        Insulina insulinaAux=new Insulina();
        insulinaAux.setTipo_aplicacion(context.getResources().getString(R.string.Normal));
        insulinaAux.setTipo_insulina(context.getResources().getString(R.string.Lenta));
        insulinaAux.setNum_unidades(2);

        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,15);
        c.set(Calendar.MINUTE,45);

        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
        String  time = format.format(c.getTime());

        openDrawer();
        clickMenu(R.id.nav_insulina);
        clickOn(R.id.btn_add_aplicacion_insulina);
        clickSpinnerItem(R.id.spinner_tipo_insulina, 0);
        clickOn(R.id.num_unidades_item);
        writeTo(R.id.num_unidades_item, "2");
        closeKeyboard();
        clickOn(R.id.hora_aplicacion);
        setTimeOnPicker(15, 45);
        clickOn(R.id.boton_agregar_aplicacion);


        openDrawer();
        clickMenu(R.id.nav_pantallainicio);


        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.identificador, R.string.menu_insulina);
        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.elementos_item, insulinaAux.getContent());
        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.hora_item, time);


        openDrawer();
        clickMenu(R.id.nav_insulina);
        clickListItemChild(R.id.recycler_aplicaciones_insulina, 0, R.id.btn_eliminar_aplicacion_insulina);


    }

    @Test
    public void testGlucosaPantallaInicioRecycler(){

        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Glucosa glucosaAux=new Glucosa();
        glucosaAux.setMgDl(180);

        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,19);
        c.set(Calendar.MINUTE, 15);

        SimpleDateFormat format= new SimpleDateFormat("k:mm a");
        String  time = format.format(c.getTime());

        openDrawer();
        clickMenu(R.id.nav_glucosa);
        clickOn(R.id.boton_add_glucosa);
        clickOn(R.id.horaMedicion_input);
        setTimeOnPicker(19, 15);
        clickOn(R.id.mgDL_input);
        writeTo(R.id.mgDL_input, "180");
        closeKeyboard();
        clickOn(R.id.boton_agregar_glucosa);


        openDrawer();
        clickMenu(R.id.nav_pantallainicio);

        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.identificador, R.string.menu_glucosa);
        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.elementos_item, glucosaAux.getContent());
        assertDisplayedAtPosition(R.id.recycler_pantalla_inicio, 0, R.id.hora_item, time);


        openDrawer();
        clickMenu(R.id.nav_glucosa);
        clickListItemChild(R.id.recycler_glucosa, 0, R.id.btn_eliminar_glucosa);

    }

}