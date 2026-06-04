package GUI;

//imports
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import managers.FechaManager;
import managers.InventarioManager;
import managers.VentasManager;
import models.CuentaCliente;
import models.Producto;
import models.Venta;



public class PanelVentas extends JFrame {
	private static final long serialVersionUID = 1L;
	private VentasManager ventasManager;
	private InventarioManager invManager;
	private JTextArea textAreaCuentas;

	
	public PanelVentas() {
		//primero tenemos q inicializar lo de ventas
		invManager = new InventarioManager();
		ventasManager = new VentasManager(invManager);

		setBackground(new Color(20,25,40));
		setBounds(0,0,800,720);
		setLayout(null);
		
		//para la barra de herramientas
		JPanel panelHerramientas = new JPanel();
		panelHerramientas.setBackground(new Color(15,20,35));
		panelHerramientas.setBounds(20, 20, 760, 60);
		panelHerramientas.setLayout(null);
		add(panelHerramientas);
		
		//ahora agrego los botones al panel de herramientas
		//NUEVA VENTA
		JButton btnNuevaVenta = crearBotonHerramienta("NUEVA VENTA", "/icono_nuevaventa.png", 10);
		panelHerramientas.add(btnNuevaVenta);
		
		//CORTE DE CAJA
		JButton btnCorteCaja = crearBotonHerramienta("CORTE CAJA", "/icono_corte.png", 190);
		panelHerramientas.add(btnCorteCaja);
		
		
		//ESTO DE ABAJO ES EL TEXTAREA PARA VER LAS CUENTAS
		textAreaCuentas = new JTextArea();
		textAreaCuentas.setEditable(false);
		textAreaCuentas.setBackground(new Color(10,15,30));
		textAreaCuentas.setForeground(new Color(0,255,100));
		textAreaCuentas.setFont(new Font("Consolas", Font.PLAIN, 14));
		textAreaCuentas.setBorder(new EmptyBorder(10,10,10,10));
		JScrollPane scrollPane = new JScrollPane(textAreaCuentas);
		scrollPane.setBounds(20, 100, 760, 580);
		scrollPane.setBorder(null);
		add(scrollPane);
		actualizarPantalla();
		
		//acciones de botones
		btnNuevaVenta.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//pedimos el ID
				String idCliente = solicitarInput("Dame el ID del cliente para la venta: ");
				if(idCliente == null || idCliente.trim().isEmpty()) {
					return;
				}
				if(!idCliente.matches("\\d+")) {
					mostrarError("Error. El ID del cliente debe de ser solo numerico");
					return;
				}
				CuentaCliente cuenta = ventasManager.buscarCliente(idCliente);
				//si no existe ya, lo creamos
				if(cuenta == null) {
					mostrarMensaje("Cliente no encontrado, creando nueva cuenta");
					String nombre = solicitarInput("Ingresa el nombre del nuevo cliente");
					if(nombre == null || nombre.trim().isEmpty()) {
						return;
					}
					cuenta = new CuentaCliente(idCliente, nombre);
					//aqui ya creamos la cuenta
					ventasManager.agregarCliente(cuenta);
					//y ahora agregamos el usuario para que pueda loguearse
					managers.UsuarioManager uManager = new managers.UsuarioManager();
					if(!uManager.existeIdUsuario(idCliente)) {
						//le mandamos dos veces el ID para que su contraseña por defecto sea su misma ID
						models.Cliente nuevoUsuarioCliente = new models.Cliente(idCliente, nombre, idCliente);
						uManager.agregarUsuario(nuevoUsuarioCliente);
						mostrarMensaje("Se ha creado el acceso para " + nombre + " Contraseña temporal: " + idCliente);
					}
					actualizarPantalla();
				}
				//ahora si, creamos la venta con la fecha actual del sistema
				Venta nuevaVenta = new Venta(idCliente, FechaManager.getFechaActual());
				//inicia el bucle para agregar productos
				boolean seguir = true;
				while(seguir) {
					String inputNombreProd = solicitarInput("Nombre del producto a comprar (o escribe FIN para terminar)");
					if(inputNombreProd == null || inputNombreProd.equalsIgnoreCase("FIN")) {
						seguir = false;
						return;
					}
					Producto p = invManager.buscarProductoPorNombre(inputNombreProd);
					if(p == null) {
						mostrarError("No se encontro un producto con ese nombre");
					}else {
						String inputCant = solicitarInput("¿Cantidad a vender de " + p.getNombre() + "?");
						if(inputCant != null) {
							try {
								int cant = Integer.parseInt(inputCant);
								//ahora validamos que si haya ese stock disponible
								if(cant > p.getCantidad()) {
									mostrarError("Error. No puedes vender mas de lo que hay en stock (" + p.getCantidad() + ")");
								}else {
									int stockFinal = p.getCantidad() - cant;
									if(stockFinal <= p.getStockMin()) {
										mostrarMensaje("Aviso: Al vender esta cantidad, el stock quedara en: " + stockFinal + " (nivel minimo o inferior)");
									}
									//ya que validamos todo, ahora si agregamos esta cantidad y producto a la venta actual
									nuevaVenta.agregarDetalle(p, cant);
									mostrarMensaje("Se agrego " + p.getNombre() + " x" + cant + " a la venta");
								}
							}catch(Exception ex) {
								mostrarError("Cantidad invalida");
							}
						}
					}
				}
				//ya que acabe el loop de agregar productos a la venta, la procesamos, se llama al metodo que actualiza el binario y todo en el inventario
				if(!nuevaVenta.getDetalles().isEmpty()) { //solo si si se compro algo
					boolean procesado = ventasManager.procesarVenta(nuevaVenta);
					if(procesado) {
						mostrarMensaje("Venta completada \nTotal a pagar: $" + nuevaVenta.getTotal());
					}else {
						mostrarMensaje("Venta cancelada (no se agregaron productos)");
					}
				}
			}
		});
		
		//BOTON CORTE DE CAJA
		btnCorteCaja.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//pedimos confirmacion
				int resp = JOptionPane.showConfirmDialog(null, "Estas seguro de que quieres hacer el corte de caja para la fecha: " + FechaManager.getFechaActual() + "?\n Esto avanzara el dia en el sistema", 
						"CONFIRMAR CORTE", JOptionPane.YES_NO_OPTION);
				if(resp == JOptionPane.YES_OPTION) {
					//generamos entonces el reporte.txt
					String resultado = ventasManager.generarCorteCaja(FechaManager.getFechaActual());
					mostrarMensaje(resultado);
					//avanzamos el dia en uno, en el archivo local
					FechaManager.avanzarDia();
					//actualizamos la fecha en el mainframe (bueno el panel de la izq)
					MainFrame mainForm = (MainFrame) SwingUtilities.getWindowAncestor(PanelVentas.this);
					if(mainForm != null) {
						mainForm.actualizarFecha();
					}
					mostrarMensaje("Se ha actualizado el dia en uno. " + FechaManager.getFechaActual());
				}
			}
		});//btn corte caja
		
	}//public PanelVentas
	
	private JButton crearBotonHerramienta(String texto, String rutaIcono, int x) {
		JButton btn = new JButton(texto);
		try {
			btn.setIcon(new ImageIcon(PanelVentas.class.getResource(rutaIcono)));
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
		textAreaCuentas.setText(ventasManager.obtenerCuentasText());
	}
	
	
	private String solicitarInput(String mensaje) {
		//le puse 32,32 porque redimensione la imagen a que se mostrara asi para que no se viera como comprimida
		return (String) JOptionPane.showInputDialog(this, mensaje, "Diamond Casino", JOptionPane.QUESTION_MESSAGE, new ImageIcon(new ImageIcon(PanelVentas.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)), null,"");
	}
	
	private void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Diamond Casino", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(new ImageIcon(PanelVentas.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)));
	}
	
	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(new ImageIcon(PanelVentas.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)));
	}
	
}//extends JFrame
	
