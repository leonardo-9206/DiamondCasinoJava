package GUI;

import java.awt.Color;
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
import managers.VentasManager;
import managers.ReportesManager;
import models.CuentaCliente;

public class PanelReportes extends JPanel {

	private static final long serialVersionUID = 1L;
	private VentasManager ventasManager;
	private InventarioManager invManager;
	private JTextArea textAreaReportes;
	
	public PanelReportes() {
		invManager = new InventarioManager();
		ventasManager = new VentasManager(invManager);
		setBackground(new Color(20,25,40));
		setBounds(0,0,800,720);
		setLayout(null);
		
		//barra de herramientas
		JPanel panelHerramientas = new JPanel();
		panelHerramientas.setBackground(new Color(15,20,35));
		panelHerramientas.setBounds(20,20,760,60);
		panelHerramientas.setLayout(null);
		add(panelHerramientas);
		
		//creando los botones
		//MOSTRAR VENTAS (en el textarea)
		JButton btnMostrarVentas = new JButton("VENTAS");
		try {
			btnMostrarVentas.setIcon(new ImageIcon(PanelReportes.class.getResource("/icono_historial.png")));
		}catch(Exception ex) {
		}
		btnMostrarVentas.setBounds(10,10,145,40);
		btnMostrarVentas.setBackground(new Color(20,25,40));
		btnMostrarVentas.setForeground(Color.WHITE);
		btnMostrarVentas.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnMostrarVentas.setFocusPainted(false);
		panelHerramientas.add(btnMostrarVentas);
		
		//MOSTRAR LOG DE RESTOCK (en el textarea)
		JButton btnLogRestock = new JButton("RESTOCK");
		try {
			btnLogRestock.setIcon(new ImageIcon(PanelReportes.class.getResource("/icono_mostrarrestock.png")));
		}catch(Exception ex) {
		}
		btnLogRestock.setBounds(160,10,145,40);
		btnLogRestock.setBackground(new Color(20,25,40));
		btnLogRestock.setForeground(Color.WHITE);
		btnLogRestock.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnLogRestock.setFocusPainted(false);
		panelHerramientas.add(btnLogRestock);

		//REPORTE POR CUENTA
		JButton btnRepCuenta = new JButton("ID");
		try {
			btnRepCuenta.setIcon(new ImageIcon(PanelReportes.class.getResource("/icono_reportecliente.png")));
		}catch(Exception ex) {
		}
		btnRepCuenta.setBounds(310,10,145,40);
		btnRepCuenta.setBackground(new Color(20,25,40));
		btnRepCuenta.setForeground(Color.WHITE);
		btnRepCuenta.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnRepCuenta.setFocusPainted(false);
		panelHerramientas.add(btnRepCuenta);
		
		//REPORTE POR FECHA
		JButton btnRepFecha = new JButton("FECHA");
		try {
			btnRepCuenta.setIcon(new ImageIcon(PanelReportes.class.getResource("/icono_reportefecha.png")));
		}catch(Exception ex) {
		}
		btnRepFecha.setBounds(460,10,145,40);
		btnRepFecha.setBackground(new Color(20,25,40));
		btnRepFecha.setForeground(Color.WHITE);
		btnRepFecha.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnRepFecha.setFocusPainted(false);
		panelHerramientas.add(btnRepFecha);
		
		//REPORTE POR PRODUCTO
		JButton btnRepProducto = new JButton("PRODUCTO");
		try {
			btnRepProducto.setIcon(new ImageIcon(PanelReportes.class.getResource("/icono_reporteprod.png")));
		}catch(Exception ex) {
		}
		btnRepProducto.setBounds(610,10,145,40);
		btnRepProducto.setBackground(new Color(20,25,40));
		btnRepProducto.setForeground(Color.WHITE);
		btnRepProducto.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnRepProducto.setFocusPainted(false);
		panelHerramientas.add(btnRepProducto);
		
		//Aqui va lo del textarea
		textAreaReportes = new JTextArea();
		textAreaReportes.setEditable(false);
		textAreaReportes.setBackground(new Color(10,15,30));
		textAreaReportes.setForeground(new Color(0,255,100));
		textAreaReportes.setFont(new Font("Consolas", Font.PLAIN, 14));
		textAreaReportes.setBorder(new EmptyBorder(10,10,10,10));
		textAreaReportes.setText("Selecciona un boton para comenzar");
		JScrollPane scrollPane = new JScrollPane(textAreaReportes);
		scrollPane.setBounds(20, 100, 760, 580);
		scrollPane.setBorder(null);
		add(scrollPane);
		
		//EVENTOS DE LOS BOTONES
		//MOSTRAR VENTAS
		btnMostrarVentas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//va a mostrar en el textarea absolutamente todas las ventas
				textAreaReportes.setText(ventasManager.obtenerTodasLasVentasTexto());
			}
		});
		
		//MOSTRAR RESTOCKS
		btnLogRestock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//muestra todo el historial de todos los restocks que se han hecho
				textAreaReportes.setText(ReportesManager.leerLogRestock());
			}
		});
		
		//GENERAR REPORTE POR CUENTA
		btnRepCuenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//pedimos el ID de la cuenta
				String idCuenta = solicitarInput("Ingresa el ID de la cuenta para generar el reporte");
				if(idCuenta != null && !idCuenta.trim().isEmpty()) {
					CuentaCliente cliente = ventasManager.buscarCliente(idCuenta);
					String resultado = ReportesManager.generarReporteCliente(cliente);
					mostrarMensaje(resultado);
				}
			}
		});
		
		//GENERAR REPORTE POR FECHA
		btnRepFecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//pedimos la fecha de la cual quiere generar el reporte
				String fecha = solicitarInput("Ingresa la fecha a consultar (dd/mm/aaaa):");
				if(fecha != null && !fecha.trim().isEmpty()) {
					String resultado = ReportesManager.generarReportePorFecha(ventasManager,fecha);
					mostrarMensaje(resultado);
				}
			}
		});
		
		//GENERAR REPORTE POR PRODUCTO
		btnRepProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//pedimos el id del producto del cual quiere generar el reporte
				String idProd = solicitarInput("Ingresa el ID del producto a consultar");
				if(idProd != null && !idProd.trim().isEmpty()) {
					String resultado = ReportesManager.generarReportePorProducto(ventasManager, idProd);
					mostrarMensaje(resultado);
				}
			}
		});
		
		
	}//panelreportes
	
	private String solicitarInput(String mensaje) {
		//le puse 32,32 porque redimensione la imagen a que se mostrara asi para que no se viera como comprimida
		return (String) JOptionPane.showInputDialog(this, mensaje, "Diamond Casino", JOptionPane.QUESTION_MESSAGE, new ImageIcon(new ImageIcon(PanelReportes.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)), null,"");
	}
	
	private void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Diamond Casino", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(new ImageIcon(PanelReportes.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)));
	}
	
	//este aqui no lo usamos, pudiera perfectamente borrarlo
	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(new ImageIcon(PanelReportes.class.getResource("/diamante.png")).getImage().getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH)));
	}
	
	}// extends JPanel

