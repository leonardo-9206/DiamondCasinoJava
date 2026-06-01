package models;

public class Cliente extends Usuario {
    public Cliente(String idUsuario, String nombre, String password) {
        super(idUsuario, nombre, password, "Cliente");
    }

    @Override
    public String obtenerNivelAcceso() {
        return "Acceso de Cliente: Solo puede ver su historial de compras y su cuenta personal.";
    }
}
