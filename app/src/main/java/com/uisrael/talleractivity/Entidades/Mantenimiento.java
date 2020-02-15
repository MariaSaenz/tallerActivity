package my.jviracocha.talleractivity.Entidades;

public class Mantenimiento {
    private  int id_mantenimiento;
    private  String nombre;
    private String observacionesMecanico;
    private  int precio;
    private  int id_mecanico;
    private  int id_citas;
    private int id_parte;


    public int getId_mantenimiento() {
        return id_mantenimiento;
    }

    public void setId_mantenimiento(int id_mantenimiento) {
        this.id_mantenimiento = id_mantenimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservacionesMecanico() {
        return observacionesMecanico;
    }

    public void setObservacionesMecanico(String observacionesMecanico) {
        this.observacionesMecanico = observacionesMecanico;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getId_mecanico() {
        return id_mecanico;
    }

    public void setId_mecanico(int id_mecanico) {
        this.id_mecanico = id_mecanico;
    }

    public int getId_citas() {
        return id_citas;
    }

    public void setId_citas(int id_citas) {
        this.id_citas = id_citas;
    }

    public int getId_parte() {
        return id_parte;
    }

    public void setId_parte(int id_parte) {
        this.id_parte = id_parte;
    }
}
