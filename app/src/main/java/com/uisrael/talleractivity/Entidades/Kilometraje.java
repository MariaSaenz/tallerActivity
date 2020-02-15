package my.jviracocha.talleractivity.Entidades;

import java.util.Date;

public class Kilometraje {

    private  int id_kilometraje;
    private String fecha;
    private  int kilometraje;
    private  int id_cliente;
    private  int id_auto;


    public int getId_kilometraje() {
        return id_kilometraje;
    }

    public void setId_kilometraje(int id_kilometraje) {
        this.id_kilometraje = id_kilometraje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_auto() {
        return id_auto;
    }

    public void setId_auto(int id_auto) {
        this.id_auto = id_auto;
    }
}
