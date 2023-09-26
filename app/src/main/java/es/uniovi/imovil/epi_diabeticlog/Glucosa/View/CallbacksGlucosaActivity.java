package es.uniovi.imovil.epi_diabeticlog.Glucosa.View;

import es.uniovi.imovil.epi_diabeticlog.Glucosa.Model.Glucosa;

public interface CallbacksGlucosaActivity {
    //metodo para eliminar la glucosa seleccionada
    void removeSelected(Glucosa seleccionada);
    //metodo para modificar la glucosa seleccionada
    void modifySelected(Glucosa seleccionada);
}
