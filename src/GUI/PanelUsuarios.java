package GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import managers.UsuarioManager;
import models.Admin;
import models.Usuario;
import models.Cliente;
import models.Empleado;


public class PanelUsuarios extends JPanel {

	private static final long serialVersionUID = 1L;
	private UsuarioManager usuarioManager;
	private JTextArea textAreaUsuarios;
	
	
	
	
	public PanelUsuarios() {
		usuarioManager = new UsuarioManager();
		setBackground(new Color(20,25,40));
		setBounds(0,0,800,720);
		setLayout(null);
		
		//para la barra de herramientas (botones)
		JPanel panelHerramientas = new JPanel();
		panelHerramientas.setBackground(new Color(15,20,35));
		panelHerramientas.setBounds(20,20,760,60);
		panelHerramientas.setLayout(null);
		add(panelHerramientas);
		
		//BOTONES
		//AÑADIR USUARIO
		JButton btnAnadirUsuario = new JButton("AÑADIR USUARIO");
		try {
			btnAnadirUsuario.setIcon(new ImageIcon(PanelUsuarios.class.getResource("/icono_añadirusuario.png")));
		}catch(Exception ex) {
			
		}
		btnAnadirUsuario.setBounds(10,10,180,40);
		btnAnadirUsuario.setBackground(new Color(20,25,40));
		btnAnadirUsuario.setForeground(Color.WHITE);
		btnAnadirUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnAnadirUsuario.setFocusPainted(false);
		panelHerramientas.add(btnAnadirUsuario);
		
		//ELIMINAR USUARIO
		JButton btnEliminarUsuario = new JButton("AÑADIR USUARIO");
		try {
			btnEliminarUsuario.setIcon(new ImageIcon(PanelUsuarios.class.getResource("/icono_eliminarusuario.png")));
		}catch(Exception ex) {
			
		}
		btnEliminarUsuario.setBounds(200,10,180,40);
		btnEliminarUsuario.setBackground(new Color(20,25,40));
		btnEliminarUsuario.setForeground(Color.WHITE);
		btnEliminarUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnEliminarUsuario.setFocusPainted(false);
		panelHerramientas.add(btnEliminarUsuario);
		
		//ahora el textarea para visualizar los usuarios que existen
		textAreaUsuarios = new JTextArea();
		textAreaUsuarios.setEditable(false);
		textAreaUsuarios.setBackground(new Color(10,15,30));
		textAreaUsuarios.setForeground(new Color(0,255,100));
		textAreaUsuarios.setFont(new Font("Consolas", Font.PLAIN, 14));
		textAreaUsuarios.setBorder(new EmptyBorder(10,10,10,10));
		
		JScrollPane scrollPane = new JScrollPane(textAreaUsuarios);
		scrollPane.setBounds(20,100,760,580);
		scrollPane.setBorder(null);
		add(scrollPane);
		//llamamos para que se actualice la lista de usuarios cada que se haga un cambio
		actualizarPantalla();
		
		//EVENTOS DE BOTONES
		btnAnadirUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = solicitarInput("Ingresa el nombre del nuevo usuario:");
						if(nombre == null || nombre.trim().isEmpty()) {
							return;
						}
				if(usuarioManager.existeUsuario(nombre)) {
					mostrarError("El nombre de usuario ya esta en uso");
					return;
				}
				String idUsuario = solicitarInput("Ingresa el ID para el usuario");
				if(idUsuario == null || idUsuario.trim().isEmpty()) {
					return;
				}
				if(usuarioManager.existeIdUsuario(idUsuario)) {
					mostrarError("El ID del usuario ya esta en uso");
					return;
				}
				
				String password = solicitarInput("Ingresa la contraseña para " + nombre + ":");
				if(password == null || password.trim().isEmpty()) {
					return;
				}
				String[] opciones = {"Admin", "Empleado", "Cliente"};
				int seleccion = JOptionPane.showOptionDialog(
						PanelUsuarios.this, "¿Que rol tendra el usuario?", "Seleccionar rol", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(new ImageIcon(PanelUsuarios.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32,java.awt.Image.SCALE_SMOOTH)),opciones,opciones[1] );
				if(seleccion >= 0) {
					String rol = opciones[seleccion];
					Usuario nuevoUsuario = null;
					//dependiendo del rol es el objeto que crea
					if(rol.equals("Admin")) {
						nuevoUsuario = new Admin(idUsuario, nombre, password);
					}else if(rol.equals("Empleado")){
						nuevoUsuario = new Empleado(idUsuario, nombre, password);
					}else {
						nuevoUsuario = new Cliente(idUsuario, nombre, password);
					}
					usuarioManager.agregarUsuario(nuevoUsuario);
					mostrarMensaje("Usuario añadido exitosamente con el ID:  " + idUsuario);
					actualizarPantalla();	
				}	
			}
		});
		
		btnEliminarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = solicitarInput("Ingresa el nombre del usuario a eliminar:");
				if(nombre != null && !nombre.trim().isEmpty()) {
					if(nombre.equals("admin")) {
						mostrarError("No puedes eliminar al admin del sistema");
						return;
					}
					int confirmacion = JOptionPane.showConfirmDialog(
							PanelUsuarios.this, "Estas seguro de que quieres eliminar permamentemente a " + nombre + '?' , "Confirmar Eliminacion", JOptionPane.YES_NO_OPTION);
					if(confirmacion == JOptionPane.YES_OPTION) {
						boolean borrado = usuarioManager.eliminarUsuario(nombre);
						if(borrado) {
							mostrarMensaje("Usuario elminado correctamente");
							actualizarPantalla();
						}else {
							mostrarMensaje("No se encontro ningun usuario con ese nombre");
						}
					}
				}
			}
		});
	}//panel usuarios
	
	private void actualizarPantalla() {
		textAreaUsuarios.setText(usuarioManager.obtenerUsuariosTexto());
	}
	
	
	private String solicitarInput(String mensaje) {
		//le puse 32,32 porque redimensione la imagen a que se mostrara asi para que no se viera como comprimida
		return (String) JOptionPane.showInputDialog(this, mensaje, "Diamond Casino", JOptionPane.QUESTION_MESSAGE, new ImageIcon(new ImageIcon(PanelUsuarios.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)), null,"");
	}
	
	private void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Diamond Casino", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(new ImageIcon(PanelUsuarios.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)));
	}
	
	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(new ImageIcon(PanelUsuarios.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)));
	}
	
}//extends JPanel


