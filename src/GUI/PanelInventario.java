package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import managers.InventarioManager;
import models.Producto;


public class PanelInventario extends JPanel {

	private static final long serialVersionUID = 1L;
	private InventarioManager invManager;
	private JTextArea textAreaInventario;
	
	public PanelInventario() {
		invManager = new InventarioManager();
		setBackground(new Color(20,25,40));
		setBounds(0,0,800,720);
		setLayout(null);
		
		//aqui va la barra de herramientas de arriba (los botones)
		JPanel panelHerramientas = new JPanel();
		panelHerramientas.setBackground(new Color(15,20,35));
		panelHerramientas.setBounds(20,20,760,60);
		panelHerramientas.setLayout(null);
		add(panelHerramientas);
		
		//BOTONES
		//BUSCAR
		JButton btnBuscar = crearBotonHerramienta("BUSCAR", "/icono_buscarID.png", 10);
		panelHerramientas.add(btnBuscar);
		//RESTOCK
		JButton btnRestock = crearBotonHerramienta("RESTOCK", "/icono_restock.png", 190);
		panelHerramientas.add(btnRestock);
		//NUEVO (producto)
		JButton btnNuevo = crearBotonHerramienta("NUEVO", "/icono_agregar.png", 370);
		panelHerramientas.add(btnNuevo);
		//EDITAR (atributos de algun producto)
		JButton btnEditar = crearBotonHerramienta("EDITAR", "/icono_editar.png", 550);
		panelHerramientas.add(btnEditar);
		
		//ahora el textarea donde se va a visualizar el inventario actual
		textAreaInventario = new JTextArea();
		textAreaInventario.setEditable(false);
		textAreaInventario.setBackground(new Color(10,15,30));
		textAreaInventario.setForeground(new Color(0,255,100));
		textAreaInventario.setFont(new Font("Consolas", Font.PLAIN, 14));
		textAreaInventario.setBorder(new EmptyBorder(10,10,10,10));
		JScrollPane scrollPane = new JScrollPane(textAreaInventario);
		scrollPane.setBounds(20,100,760,580);
		scrollPane.setBorder(null);
		add(scrollPane);
		//para que se actualice el textarea cada que se hace algun cambio
		actualizarPantalla();
		
		//acciones de los botones
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = solicitarInput("Ingresa el ID del producto a buscar");
				if(id != null && !id.trim().isEmpty()) {
					Producto p = invManager.buscarProducto(id);
					if(p != null) {
						mostrarMensaje("Producto Encontrado:\n\n" + p.toString());
					}else {
						mostrarError("No se encontro ningun producto con el ID: " + id);
					}
				}
			}
		});
		
		btnRestock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = solicitarInput("Ingresa el ID del producto a hacer Restock");
				if(id != null && !id.trim().isEmpty()) {
					String cantStr = solicitarInput("¿Cuantas unidades vas a ingresar al almacen?");
					if(cantStr != null) {
						try {
							int cantidad = Integer.parseInt(cantStr);
							String resultado = invManager.hacerRestock(id, cantidad);
							//no lo pudo hacer
							if(resultado.startsWith("Error")) {
								mostrarError(resultado);
							}else {
								//se registra en el log
								managers.LogManager.registrarRestock("Sistema", id, cantidad, invManager.buscarProducto(id).getCantidad());
								//le decimos al usuario que si se pudo hacer el restock
								mostrarMensaje(resultado);
								//actualizamos en tiempo real el inventario que se muestra en el textArea
								actualizarPantalla();
							}
						}catch(Exception ex) {
							mostrarError("Ingresa un numero valido para la cantidad");
						}
					}
				}
			}
		});
		
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String id = solicitarInput("1. ID del nuevo producto:");
					//validamos que no este vacio y que no exista ya ese ID
					if(id == null || id.isEmpty()) {
						return;
					}
					if(invManager.buscarProducto(id) != null) {
						mostrarError("Ya existe un producto con ese ID en el sistema");
						return;
					}
					String nombre = solicitarInput("2. Nombre del producto:");
					if(nombre == null) {
						return;
					}
					String tipo = solicitarInput("3. Tipo (Fichas, Bebidas, Comida):");
					if(tipo == null) {
						return;
					}
					double precio = Double.parseDouble(solicitarInput("4. Precio unitario:"));
					int cantidad = Integer.parseInt(solicitarInput("5. Stock inicial (actual):"));
					int min = Integer.parseInt(solicitarInput("6. Stock minimo permitido:"));
					int max = Integer.parseInt(solicitarInput("7. Stock maximo permitido:"));
					//ya que capturamos todo, creamos ahora si el objeto
					Producto nuevo = new Producto(id,nombre,precio,cantidad,min,max,tipo,true);
					//lo agregamos al inventario
					invManager.agregarProducto(nuevo);
					//le avisamos al usuario que si quedo
					mostrarMensaje("Producto registrado con exito y guardado en archivo binario");
					actualizarPantalla();
				}catch(Exception ex) {
					mostrarError("Error en los datos ingresados, operacion cancelada");
				}
			}
		});
		
		//BOTON EDITAR
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = solicitarInput("Ingresa el ID del producto a modificar/eliminar:");
				if(id != null && !id.trim().isEmpty()) {
					Producto p = invManager.buscarProducto(id);
					if(p != null) {
						//si si lo encontro
						String[] opciones = {"Cambiar nombre", "Cambiar precio", "Cambiar maximo", "Eliminar(baja)"};
						int seleccion = JOptionPane.showOptionDialog(null, "¿Que deseas hacer con " + p.getNombre() + "?",
								"Modificar Producto", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
								new ImageIcon(new ImageIcon(PanelInventario.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)), 
								opciones, opciones[0]);
						if(seleccion == 3) {
							invManager.eliminarProducto(id);
							mostrarMensaje("Producto dado de baja exitosamente");
							actualizarPantalla();
						}else if(seleccion >= 0) {
							String nuevoValor = solicitarInput("Ingresa el nuevo valor: ");
							if(nuevoValor != null) {
								int campoReal = (seleccion == 0) ? 1 : (seleccion == 1) ? 2 : 4;
								boolean exito = invManager.modificarProducto(id, campoReal, nuevoValor);
								if(exito) {
									mostrarMensaje("Producto modificado con exito");
									//para que se vea reflejado el cambio
									actualizarPantalla();
								}else {
									mostrarError("Error al modificar, revisa que el formato sea el correcto");
								}
							}
						}
					}else {
						mostrarError("No se encontro el producto con esa ID");
					}
				}
			}
		});
	}//public
	
	private JButton crearBotonHerramienta(String texto, String rutaIcono, int x) {
		JButton btn = new JButton(texto);
		try {
			btn.setIcon(new ImageIcon(PanelInventario.class.getResource(rutaIcono)));
		}catch(Exception e) {
		}
		btn.setBounds(x,10,180,40);
		btn.setBackground(new Color(20,25,40));
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btn.setFocusPainted(false);
		return btn;
	}

	private void actualizarPantalla() {
		textAreaInventario.setText(invManager.obtenerInventarioTexto());
	}
	
	
	private String solicitarInput(String mensaje) {
		//le puse 32,32 porque redimensione la imagen a que se mostrara asi para que no se viera como comprimida
		return (String) JOptionPane.showInputDialog(this, mensaje, "Diamond Casino", JOptionPane.QUESTION_MESSAGE, new ImageIcon(new ImageIcon(PanelInventario.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)), null,"");
	}
	
	private void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Diamond Casino", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(new ImageIcon(PanelInventario.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)));
	}
	
	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(new ImageIcon(PanelInventario.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)));
	}
	
	
}//extends JPanel
