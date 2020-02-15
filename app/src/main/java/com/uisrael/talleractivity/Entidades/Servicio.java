package my.jviracocha.talleractivity.Entidades;

public class Servicio {

    private int id_Servicio;
    private String nombreServicio;
    private int costoServicio;
    private int kilometrajeTope;

    public int getId_Servicio() {
        return id_Servicio;
    }

    public void setId_Servicio(int id_Servicio) {
        this.id_Servicio = id_Servicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public int getCostoServicio() {
        return costoServicio;
    }

    public void setCostoServicio(int costoServicio) {
        this.costoServicio = costoServicio;
    }

    public int getKilometrajeTope() {
        return kilometrajeTope;
    }

    public void setKilometrajeTope(int kilometrajeTope) {
        this.kilometrajeTope = kilometrajeTope;
    }
}
