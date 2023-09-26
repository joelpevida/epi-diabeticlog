package es.uniovi.imovil.epi_diabeticlog.Alimentacion.View;

import es.uniovi.imovil.epi_diabeticlog.Alimentacion.Model.Ingesta;

public interface CallbacksAlimentacionActivity {

    //metodo para eliminar la ingesta seleccionada
    void removeSelected(Ingesta seleccionada);

    //metodo para modificar la ingesta seleccionada
    void modifySelected(Ingesta seleccionada);
}
