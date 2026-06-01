package models;

public class Admin extends Usuario {
    public Admin(String idUsuario, String nombre, String password) {
        super(idUsuario, nombre, password, "Admin");
    }

    @Override
    public String obtenerNivelAcceso() {
        return "Acceso Total: Puede registrar, modificar, eliminar, ver reportes y gestionar empleados.";
    }
}
