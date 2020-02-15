package my.jviracocha.talleractivity.Entidades;

public class Parte {

    private  int id_parte;
    private  String nombre;
    private  int costo;
    private  int vencimientoKM;
    private  int id_auto;

    private boolean select;

    public int getId_parte() {
        return id_parte;
    }

    public void setId_parte(int id_parte) {
        this.id_parte = id_parte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public int getVencimientoKM() {
        return vencimientoKM;
    }

    public void setVencimientoKM(int vencimientoKM) {
        this.vencimientoKM = vencimientoKM;
    }

    public int getId_auto() {
        return id_auto;
    }

    public void setId_auto(int id_auto) {
        this.id_auto = id_auto;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
