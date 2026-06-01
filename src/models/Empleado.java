package models;

public class Empleado extends Usuario {

    public Empleado(String idUsuario, String nombre, String password) {
        super(idUsuario, nombre, password, "Empleado");
    }

    @Override
    public String obtenerNivelAcceso() {
        return "Acceso Parcial: Puede realizar ventas y hacer restock de inventario.";
    }
}
