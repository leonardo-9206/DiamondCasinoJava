package managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import models.Usuario;
import models.Admin;
import models.Empleado;
import models.Cliente;

public class UsuarioManager {
	private ArrayList<Usuario> listaUsuarios;
	private final String RUTA_ARCHIVO = "usuario.txt";
	
	public UsuarioManager() {
		listaUsuarios = new ArrayList<>();
		cargarDatos();
		if(listaUsuarios.isEmpty()) {
			Usuario adminPorDefecto = new Admin("1", "admin", "1234");
			agregarUsuario(adminPorDefecto);
		}
		
	}
	
	public Usuario login(String nombreIngresado, String contrasenaIngresada) {
		for(Usuario u : listaUsuarios) {
			if(u.getNombre().equals(nombreIngresado) && u.getPassword().equals(contrasenaIngresada)) {
				System.out.println("Permisos del usuario: " + u.obtenerNivelAcceso());
				return u;
			}
		}
		return null;
	}
	
	public void agregarUsuario(Usuario nuevoUsuario) {
		listaUsuarios.add(nuevoUsuario);
		guardarDatos();
	}
	
	public boolean existeUsuario(String nombreUsuario) {
		for(Usuario u : listaUsuarios) {
			if(u.getNombre().equalsIgnoreCase(nombreUsuario)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean existeIdUsuario(String idUsuario) {
		for(Usuario u : listaUsuarios) {
			if(u.getIdUsuario().equalsIgnoreCase(idUsuario)) {
				return true;
			}
			
		}
		return false;
	}
	
	public boolean eliminarUsuario(String nombreUsuario) {
		for(Usuario u : listaUsuarios) {
			if(u.getNombre().equals(nombreUsuario)) {
				listaUsuarios.remove(u);
				guardarDatos();
				return true;
			}
		}
		return false;
	}
	
	public String obtnerUsuariosTexto() {
		if(listaUsuarios.isEmpty()) {
			return "no hay usuarios registrados. ";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-15s %-20s %-15s %-15s\n", "ID USUARIO", "NOMBRE", "CONTRASEÑA", "ROL"));
		sb.append("----------------------------------------------------\n");
		
		for(Usuario u : listaUsuarios) {
			sb.append(String.format("%-15s %-20s %-15s %-15s\n", u.getIdUsuario(), u.getNombre(), u.getPassword(), u.getRol()));
		}
		return sb.toString();
	}
	
	private void guardarDatos() {
		try {
			FileWriter archivo = new FileWriter(RUTA_ARCHIVO);
			PrintWriter escritor = new PrintWriter(archivo);
			for(Usuario u : listaUsuarios) {
				String linea = u.getIdUsuario() + "," + u.getNombre() + "," + u.getPassword() + "," + u.getRol();
				escritor.println(linea);
			}
			escritor.close();
			archivo.close();
		} catch (Exception e) {
			System.out.println("Error al guardar usuarios: " + e.getMessage());
		}
	}
	
	private void cargarDatos() {
		try {
			FileReader archivo = new FileReader(RUTA_ARCHIVO);
			BufferedReader lector = new BufferedReader(archivo);
			String linea;
			
			while((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",");
				String id = datos[0];
				String nombre = datos[1];
				String pass = datos[2];
				String rol = datos[3];
				
				Usuario u;
				
				if(rol.equalsIgnoreCase("Admin")) {
					u = new Admin(id, nombre, pass);
				} else if(rol.equalsIgnoreCase("Empleado")) {
					u = new Empleado(id, nombre, pass);
				} else {
					u = new Cliente(id, nombre, pass);
				}
				
				listaUsuarios.add(u);
				
			}
			lector.close();
			archivo.close();
		} catch (Exception e) {
			System.out.println("No se encontro " + RUTA_ARCHIVO + ". Se creara el usuario admin por defecto");
		}
		
	}
	
	
	
}
