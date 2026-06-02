package GUI;

//imports
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import managers.UsuarioManager;
import models.Usuario;


public class Login extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField txtContraseña;
	//cargar el usuariomanager
	private UsuarioManager usuarioManager;
	JButton btnIniciarSesion = new JButton("Iniciar Sesion");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		//aqui ahorita inicializo el usuariomanager
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 487, 479);
		setTitle("Diamond Casino - Iniciar Sesión");
		setLocationRelativeTo(null); //para que se centre la pantalla automaticamente
		contentPane = new JPanel();
		contentPane.setBackground(new Color(15,15,20)); //cambio el color de fondo
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//cargo el logo del diamante
		try {
			ImageIcon iconDiamanteOriginal = new ImageIcon(Login.class.getResource("/diamante.png"));
			Image imgEscalada = iconDiamanteOriginal.getImage().getScaledInstance(216, -1, Image.SCALE_SMOOTH);
			JLabel lblLogo = new JLabel(new ImageIcon(imgEscalada));
			lblLogo.setBounds(118,20,216,120);
			contentPane.add(lblLogo);
		}catch(Exception ex){
			System.out.println("No se encontro la imagen diamante.png");
		}
		
		//TITULO
		JLabel lblDiamond= new JLabel("DIAMOND CASINO");
		lblDiamond.setHorizontalAlignment(SwingConstants.CENTER);
		lblDiamond.setBounds(20,150,414,30);
		lblDiamond.setFont(new Font("Segoe UI Light", Font.PLAIN, 26));
		lblDiamond.setForeground(new Color(230,230,235));
		contentPane.add(lblDiamond); 
		
		//USUARIO
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(107, 202, 93, 15);
		lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblUsuario.setForeground(Color.WHITE);
		contentPane.add(lblUsuario);
		
		//CONTRASEÑA
		JLabel lblContraseña = new JLabel("Contraseña:");
		lblContraseña.setBounds(106, 257, 94, 15);
		lblContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblContraseña.setForeground(Color.WHITE);
		contentPane.add(lblContraseña);
		
		//ahora las cajas de texto pal usuario
		//USUARIO
		txtUsuario = new JTextField();
		txtUsuario.setBounds(243, 198, 140, 25);
		txtUsuario.setBackground(new Color(30,30,35));
		txtUsuario.setForeground(Color.WHITE);
		txtUsuario.setBorder(new LineBorder(new Color(192,192,192),1));
		txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		//CONTRASEÑA
		txtContraseña = new JPasswordField(); //para que no se vea la contraseña mientras la esta escribiendo
		txtContraseña.setBounds(243,253,140,25);
		txtContraseña.setBackground(new Color(30,30,35));
		txtContraseña.setForeground(Color.WHITE);
		txtContraseña.setCaretColor(Color.WHITE);
		txtContraseña.setBorder(new LineBorder(new Color(192,192,192),1));
		txtContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		contentPane.add(txtContraseña);
		
		//BOTON
		btnIniciarSesion.setBounds(162,315,150,35);
		btnIniciarSesion.setBackground(new Color(192,192,192));
		btnIniciarSesion.setForeground(Color.BLACK);
		btnIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnIniciarSesion.setFocusPainted(false);
		contentPane.add(btnIniciarSesion);
		
		
		//ACCIONES DEL BOTON
		btnIniciarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//obtenemos lo que se escribio
				String usr = txtUsuario.getText();
				String pass = new String(txtContraseña.getPassword());
				//se lo mandamos al manager
				Usuario usuarioLogueado = usuarioManager.login(usr, pass);
				//condicionales
				if(usuarioLogueado != null) {
					//si lo encontro
					JOptionPane.showMessageDialog(null, "Bienvenido al Sistema, " + usuarioLogueado.getNombre() + "\n Tu rol es: " + usuarioLogueado.getRol(),
							"Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
					//como lo encontro, destruimos esta ventana y mostramos la que sigue
					dispose();
					MainFrame menu = new MainFrame(usuarioLogueado);
					//y lo hacemos visible
					menu.setVisible(true);
				}else {
					//no encontro el usuario, o no era correcto
					JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos, intenta de nuevo", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		
	}
}
