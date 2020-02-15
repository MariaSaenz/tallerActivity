package my.jviracocha.talleractivity.Entidades;

public class Cliente extends Usuario{


    private int telefono;
    private int id_cliente;
    private String direccion;
    private  String txtoken;
    private  String nombre;



    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTxtoken() {
        return txtoken;
    }

    public void setTxtoken(String txtoken) {
        this.txtoken = txtoken;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }
}
