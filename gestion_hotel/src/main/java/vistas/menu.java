package vistas;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class menu extends JFrame {

    public menu() {
        // 1. Configuración de la Ventana Principal
        setTitle("Hotel Paraíso - Menú Principal");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // 2. Crear y añadir la Barra Lateral (Sidebar)
        add(crearSidebar(), BorderLayout.WEST);

        // 3. Crear y añadir el Contenido Principal (Dashboard)
        add(crearContenidoPrincipal(), BorderLayout.CENTER);
    }

    // ==========================================================
    // MÉTODO PARA CREAR LA BARRA LATERAL (SIDEBAR)
    // ==========================================================
    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(new Color(30, 41, 59)); // Azul oscuro
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        // --- Perfil de Usuario con Recorte Circular ---
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 90; // Tamaño del círculo
                int x = (getWidth() - size) / 2;
                int y = 0;

                Shape circle = new Ellipse2D.Double(x, y, size, size);
                g2.setClip(circle);

                // Cargar imagen del icono del menú
                URL logoUrl = getClass().getResource("/img/icono-menu.png");
                if (logoUrl != null) {
                    Image logoImg = new ImageIcon(logoUrl).getImage();
                    g2.drawImage(logoImg, x, y, size, size, this);
                } else {
                    g2.setColor(new Color(203, 213, 225));
                    g2.fill(circle);
                }

                g2.setClip(null);
                g2.setColor(new Color(59, 130, 246)); // Borde azul del icono
                g2.setStroke(new BasicStroke(3f));
                g2.draw(circle);
                g2.dispose();
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(200, 100));
        logoPanel.setMaximumSize(new Dimension(200, 100));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Textos del Usuario ---
        JLabel lblNombre = new JLabel("Carlos Torres");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRol = new JLabel("ADMINISTRADOR");
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setForeground(new Color(148, 163, 184));
        lblRol.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Info Adicional del Hotel ---
        JLabel lblInfo = new JLabel("<html><center><br><br>🏨 Hotel Paraíso Ica<br>🟢 Sistema En Línea</center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfo.setForeground(new Color(203, 213, 225));
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Botón de Cerrar Sesión ---
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setBackground(new Color(239, 68, 68)); // Rojo
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Acción de cerrar sesión
        btnCerrarSesion.addActionListener(e -> {
            new login().setVisible(true);
            this.dispose();
        });

        // Ensamblar Sidebar
        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(lblNombre);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(lblRol);
        sidebar.add(lblInfo);
        sidebar.add(Box.createVerticalGlue()); // Empuja el botón hacia abajo
        sidebar.add(btnCerrarSesion);

        return sidebar;
    }

    // ==========================================================
    // MÉTODO PARA CREAR EL CONTENIDO PRINCIPAL
    // ==========================================================
    private JPanel crearContenidoPrincipal() {
        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setBackground(new Color(248, 250, 252)); // Gris muy claro
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- Cabecera (Título y Fecha) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("Panel de Control");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(15, 23, 42));

        // Obtener fecha actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM yyyy", new Locale("es", "ES"));
        String fechaActual = LocalDateTime.now().format(formatter);
        // Poner la primera letra en mayúscula
        fechaActual = fechaActual.substring(0, 1).toUpperCase() + fechaActual.substring(1);
        
        JLabel lblFecha = new JLabel("📅 " + fechaActual);
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFecha.setForeground(new Color(100, 116, 139));

        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(lblFecha, BorderLayout.EAST);

        // --- Cuadrícula de Tarjetas (Cards) ---
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setOpaque(false);

        // Panel para las 6 tarjetas superiores (Grid 2 filas x 3 columnas)
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setOpaque(false);

        gridPanel.add(crearCard("🏨", "Habitaciones", "Ver mapa interactivo y estado de cuartos.", new Color(59, 130, 246), "habitaciones"));
        gridPanel.add(crearCard("🛎️", "Check-In", "Registrar ingreso de nuevos huéspedes.", new Color(16, 185, 129), "checkin"));
        gridPanel.add(crearCard("🚶", "Check-Out", "Procesar salidas y pagos de cuentas.", new Color(245, 158, 11), "checkout"));
        gridPanel.add(crearCard("📅", "Reservas", "Programar llegadas y asignar cuartos.", new Color(139, 92, 246), "reservas"));
        gridPanel.add(crearCard("👥", "Clientes", "Directorio general de registro de huéspedes.", new Color(236, 72, 153), "clientes"));
        gridPanel.add(crearCard("☕", "Snack Bar", "Registrar consumos a las habitaciones.", new Color(249, 115, 22), "snack"));

        // Tarjeta grande inferior (Reportes)
        JPanel reportePanel = crearCard("📊", "Reportes y Estadísticas", "Generar reportes de ocupación, ingresos y exportar a PDF/Excel.", new Color(100, 116, 139), "reportes");
        reportePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100)); // Más ancha
        reportePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Ensamblar las tarjetas
        cardsContainer.add(gridPanel);
        cardsContainer.add(Box.createRigidArea(new Dimension(0, 20))); // Espacio
        cardsContainer.add(reportePanel);

        // Añadir cabecera y tarjetas al panel principal
        mainContent.add(headerPanel, BorderLayout.NORTH);
        mainContent.add(cardsContainer, BorderLayout.CENTER);

        return mainContent;
    }

    // ==========================================================
    // MÉTODO PARA CREAR UNA TARJETA (CARD) DINÁMICA Y CLICABLE
    // ==========================================================
    private JPanel crearCard(String icono, String titulo, String desc, Color colorIcono, String ventanaDestino) {
        // Panel personalizado para tener bordes redondeados
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Etiqueta del Icono (Emoji o Texto grande)
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcono.setForeground(colorIcono);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Etiqueta del Título
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(30, 41, 59));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Etiqueta de la Descripción
        JLabel lblDesc = new JLabel("<html><div style='text-align: center; color: #64748b; font-size: 11px;'>" + desc + "</div></html>");
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblIcono);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblDesc);

        // --- Efectos de Interacción (Hover y Clic) ---
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Efecto al pasar el mouse por encima
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(59, 130, 246), 2), // Borde azul
                        BorderFactory.createEmptyBorder(18, 18, 18, 18)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Volver a la normalidad
                card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                abrirVentana(ventanaDestino);
            }
        });

        return card;
    }

    // ==========================================================
    // MÉTODO PARA NAVEGAR A OTRAS VENTANAS
    // ==========================================================
    private void abrirVentana(String destino) {
        switch (destino) {
            case "habitaciones":
                new habitaciones().setVisible(true);
                this.dispose();
                break;
            case "clientes":
                new clientes().setVisible(true);
                this.dispose();
                break;
            case "checkin":
                new checkin().setVisible(true);
                this.dispose();
                break;
            case "checkout":
                new checkout().setVisible(true);
                this.dispose();
                break;
            case "reservas":
                new reservas().setVisible(true);
                this.dispose();
                break;
            case "snack":
                new Snack().setVisible(true); // Recuerda que tu clase se llama Snack (con S mayúscula)
                this.dispose();
                break;
            case "reportes":
                new reportes().setVisible(true);
                this.dispose();
                break;
        }
    }

    // Método principal para probar directamente
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new menu().setVisible(true);
        });
    }
}