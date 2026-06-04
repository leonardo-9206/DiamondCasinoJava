package GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.EmptyBorder;

import managers.InventarioManager;
import managers.VentasManager;
import models.CuentaCliente;
import models.DetalleVenta;
import models.Usuario;
import models.Venta;


public class PanelHistorial extends JPanel {

	private static final long serialVersionUID = 1L;
	private VentasManager ventasManager;
	private JTextArea textAreaHistorial;
	
	public PanelHistorial(Usuario usuarioActual) {
		ventasManager = new VentasManager(new InventarioManager());
		setBackground(new Color(20,25,40));
		setBounds(0,0,800,720);
		setLayout(null);
		
		//barra de herramientas
		JPanel panelHerramientas = new JPanel();
		panelHerramientas.setBackground(new Color(15,20,35));
		panelHerramientas.setBounds(20,20,760,60);
		panelHerramientas.setLayout(null);
		add(panelHerramientas);
		
		JLabel lblTitulo = new JLabel("MIS COMPRAS" + usuarioActual.getNombre().toUpperCase());
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setBounds(20, 15, 400, 30);
		panelHerramientas.add(lblTitulo);
		
		//textarea para que visualice sus compras
		textAreaHistorial = new JTextArea();
		textAreaHistorial.setEditable(false);
		textAreaHistorial.setBackground(new Color(10,15,30));
		textAreaHistorial.setForeground(new Color(0,255,100));
		textAreaHistorial.setFont(new Font("Consolas", Font.PLAIN, 14));
		textAreaHistorial.setBorder(new EmptyBorder(10,10,10,10));
		JScrollPane scrollPane = new JScrollPane(textAreaHistorial);
		scrollPane.setBounds(20,100,760,580);
		scrollPane.setBorder(null);
		add(scrollPane);
		

	}//panel historial
	
	//funcion para cargar todo el historial de compras del cliente
	private void cargarHistorial(String idCliente) {
		CuentaCliente cuenta = ventasManager.buscarCliente(idCliente);
		if(cuenta == null || cuenta.getHistorialVentas().isEmpty()) {
			textAreaHistorial.setText("No tienes compras registradas");
			return;
		}
		//si si tiene compras, tenemos que darle formato
		//para no estar creando un monton de strings al modificarlo
		StringBuilder sb = new StringBuilder();
		sb.append("-------------------------------------------------\n");
		sb.append("           TU HISTORIAL DE COMPRAS               \n");
		sb.append("-------------------------------------------------\n\n");
		
		for(Venta v : cuenta.getHistorialVentas()) {
			sb.append("-> Fecha: ").append(v.getFecha()).append(" | Ticket ID: ").append(v.getIdVenta()).append("\n");
			//ahora abrimos el detalle de la venta
			for(DetalleVenta d : v.getDetalles()) {
				sb.append(String.format("   - %-20s x%-5d  $%.2f\n",
						d.getProducto().getNombre(), d.getCantidad(), d.getSubtotal()));
			}
			sb.append(String.format("    TOTAL PAGADO: $%.2f\n", v.getTotal()));
			sb.append("-------------------------------------------------\n");
		}
		sb.append(String.format("\nGRAN TOTAL HISTORICO GASTADO: $%.2f\n", cuenta.calcularTotalGastado()));
		//al final nomas convertimos el stringbuilder a texto y ya lo mandamos
		textAreaHistorial.setText(sb.toString());
	}



	}//extends JPanel
