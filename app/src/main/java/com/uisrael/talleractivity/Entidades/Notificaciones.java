package my.jviracocha.talleractivity.Entidades;

public class Notificaciones {
    private  int idNotificacion;
    private String titulo;
    private String cuerpo;
    private String tema;
    private String fecha;
    private int idAdministrador;
    private String idMensage;


    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public String getIdMensage() {
        return idMensage;
    }

    public void setIdMensage(String idMensage) {
        this.idMensage = idMensage;
    }
}
