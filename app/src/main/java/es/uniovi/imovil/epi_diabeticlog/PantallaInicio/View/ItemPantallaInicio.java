package es.uniovi.imovil.epi_diabeticlog.PantallaInicio.View;

public interface ItemPantallaInicio {

    //metodo que devuelve la fecha del item
    long getFechaItem();

    //metodo que devuelve la hora del item en un formato amigable
    String getHora();

    //metodo que devuelve un string indicando el tipo del item
    String getTipo();

    //metodo que devuelve una cadena con las características principales del item
    String getContent();

}
