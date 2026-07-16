package vistas;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import persistencia.UsuarioArchivo;
import modelo.Usuario;

public class login extends JFrame {

    public login() {
        // 1. Configuración básica de la ventana
        setTitle("Hotel Paraíso - Iniciar Sesión");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla
        setResizable(false);
        
        // Crear administrador por defecto si no existen usuarios
        UsuarioArchivo ua = new UsuarioArchivo();
        if (ua.listar().isEmpty()) {
            Usuario admin = new Usuario();
            admin.setIdUsuario(1);
            admin.setUsername("admin");
            admin.setPassword("12345");
            admin.setRol("ADMINISTRADOR");
            ua.registrar(admin);
        }

        // 2. Crear el panel de fondo con la imagen
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Cargar imagen de fondo (Cuidado con el nombre exacto de tu archivo)
                URL imgUrl = getClass().getResource("/img/backgroung-login.png");
                if (imgUrl != null) {
                    Image bgImage = new ImageIcon(imgUrl).getImage();
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Color de respaldo si no encuentra la imagen
                    g.setColor(new Color(44, 62, 80));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        // Usamos GridBagLayout para centrar el cuadro de login exactamente en el medio
        backgroundPanel.setLayout(new GridBagLayout()); 

        // 3. Crear el cuadro central semitransparente (Login Box)
        JPanel loginBox = new JPanel();
        loginBox.setBackground(new Color(255, 255, 255, 220)); // Blanco con 220 de opacidad
        loginBox.setPreferredSize(new Dimension(380, 480));
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS)); // Elementos apilados hacia abajo
        loginBox.setBorder(new EmptyBorder(40, 40, 40, 40)); // Márgenes internos

        // 4. Componente personalizado para el Logo Circular
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                // Activar antialiasing para bordes suaves
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 120; // Tamaño del círculo
                int x = (getWidth() - size) / 2;
                int y = 0;

                // Crear el recorte circular
                Shape circle = new Ellipse2D.Double(x, y, size, size);
                g2.setClip(circle);

                // Cargar y dibujar el logo
                URL logoUrl = getClass().getResource("/img/logo-login.png");
                if (logoUrl != null) {
                    Image logoImg = new ImageIcon(logoUrl).getImage();
                    g2.drawImage(logoImg, x, y, size, size, this);
                } else {
                    g2.setColor(Color.GRAY);
                    g2.fill(circle);
                }
                
                // Dibujar un borde elegante alrededor del círculo (opcional)
                g2.setClip(null);
                g2.setColor(new Color(59, 130, 246)); // Borde azul
                g2.setStroke(new BasicStroke(3f));
                g2.draw(circle);
                
                g2.dispose();
            }
        };
        logoPanel.setOpaque(false); // Fondo transparente para el panel del logo
        logoPanel.setPreferredSize(new Dimension(300, 140));
        logoPanel.setMaximumSize(new Dimension(300, 140));

        // 5. Título de bienvenida
        JLabel lblTitulo = new JLabel("Bienvenido");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(51, 51, 51));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 6. Campos de texto (Usuario)
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(new Color(100, 100, 100));
        
        JTextField txtUsuario = new JTextField();
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding interno
        ));

        // 7. Campos de texto (Contraseña)
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(new Color(100, 100, 100));

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // 8. Botón de Ingresar
        JButton btnIngresar = new JButton("INGRESAR");
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setBackground(new Color(59, 130, 246)); // Azul moderno
        btnIngresar.setFocusPainted(false); // Quitar el feo borde de foco nativo de Java
        btnIngresar.setBorderPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Acción del botón (Redirección al menú principal)
        btnIngresar.addActionListener(e -> {
            String user = txtUsuario.getText();
            String pass = new String(txtPassword.getPassword());
            
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                UsuarioArchivo uaLogin = new UsuarioArchivo();
                Usuario usuarioValido = uaLogin.validarLogin(user, pass);
                
                if (usuarioValido != null) {
                    // Abrir el Menú pasándole los datos del usuario si fuera necesario en el futuro
                    menu ventanaMenu = new menu();
                    ventanaMenu.setVisible(true);
                    this.dispose(); // Cierra el Login
                } else {
                    JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 9. Ensamblar todo en el cuadro de Login
        loginBox.add(logoPanel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 10)));
        loginBox.add(lblTitulo);
        loginBox.add(Box.createRigidArea(new Dimension(0, 25))); // Espacio
        
        // Alinear etiquetas a la izquierda
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        userPanel.setOpaque(false);
        userPanel.add(lblUsuario);
        loginBox.add(userPanel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 5)));
        loginBox.add(txtUsuario);
        
        loginBox.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        passPanel.setOpaque(false);
        passPanel.add(lblPassword);
        loginBox.add(passPanel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 5)));
        loginBox.add(txtPassword);
        
        loginBox.add(Box.createRigidArea(new Dimension(0, 30)));
        loginBox.add(btnIngresar);

        // 10. Añadir el cuadro al panel de fondo y el fondo a la ventana
        backgroundPanel.add(loginBox);
        add(backgroundPanel);
    }

    // Método principal para probar la ventana directamente
    public static void main(String[] args) {
        // Cambiar el aspecto visual (Look and Feel) para que se vea más moderno en Windows
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new login().setVisible(true);
        });
    }
}