package es.uniovi.imovil.epi_diabeticlog.Insulina.View;

import es.uniovi.imovil.epi_diabeticlog.Insulina.Model.Insulina;

public interface CallbacksInsulinaActivity {

    //metodo para eliminar la insulina seleccionada
    void removeSelected(Insulina seleccionada);
    //metodo para modificar la insulina seleccionada
    void modifySelected(Insulina seleccionada);

}

