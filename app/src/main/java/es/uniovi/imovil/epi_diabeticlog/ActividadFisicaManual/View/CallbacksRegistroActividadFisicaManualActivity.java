package es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.View;

import es.uniovi.imovil.epi_diabeticlog.ActividadFisicaManual.Model.RegistroActividadFisicaManual;

public interface CallbacksRegistroActividadFisicaManualActivity {

    //metodo para eliminar la ingesta seleccionada
    void removeSelected(RegistroActividadFisicaManual seleccionada);
    //metodo para modificar la ingesta seleccionada
    void modifySelected(RegistroActividadFisicaManual seleccionada);

}
