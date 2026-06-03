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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 92, 723, 468);
		contentPane.add(scrollPane);
		
		textAreaCuentas = new JTextArea();
		textAreaCuentas.setEditable(false);
		scrollPane.setViewportView(textAreaCuentas);

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

	
	
	
}//extends JFrame
	
