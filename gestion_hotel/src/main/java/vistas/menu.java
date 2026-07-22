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
        setTitle("Hotel Mapocho - Menú Principal");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(crearSidebar(), BorderLayout.WEST);

        add(crearContenidoPrincipal(), BorderLayout.CENTER);
    }

    // Sidebar
    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(new Color(30, 41, 59));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 90;
                int x = (getWidth() - size) / 2;
                int y = 0;

                Shape circle = new Ellipse2D.Double(x, y, size, size);
                g2.setClip(circle);

                URL logoUrl = getClass().getResource("/img/icono-menu.png");
                if (logoUrl != null) {
                    Image logoImg = new ImageIcon(logoUrl).getImage();
                    g2.drawImage(logoImg, x, y, size, size, this);
                } else {
                    g2.setColor(new Color(203, 213, 225));
                    g2.fill(circle);
                }

                g2.setClip(null);
                g2.setColor(new Color(59, 130, 246));
                g2.setStroke(new BasicStroke(3f));
                g2.draw(circle);
                g2.dispose();
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(200, 100));
        logoPanel.setMaximumSize(new Dimension(200, 100));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNombre = new JLabel("Carlos Torres");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRol = new JLabel("ADMINISTRADOR");
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setForeground(new Color(148, 163, 184));
        lblRol.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInfo = new JLabel("<html><center><br><br>🏨 Hotel Mapocho Ica<br></center></html>");
        lblInfo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        lblInfo.setForeground(new Color(203, 213, 225));
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setBackground(new Color(239, 68, 68));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCerrarSesion.addActionListener(e -> {
            new login().setVisible(true);
            this.dispose();
        });

        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(lblNombre);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(lblRol);
        sidebar.add(lblInfo);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnCerrarSesion);

        return sidebar;
    }

    private JPanel crearContenidoPrincipal() {
        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setBackground(new Color(248, 250, 252));
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("Panel de Control");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(15, 23, 42));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM yyyy", new Locale("es", "ES"));
        String fechaActual = LocalDateTime.now().format(formatter);
        fechaActual = fechaActual.substring(0, 1).toUpperCase() + fechaActual.substring(1);
        
        JLabel lblFecha = new JLabel(fechaActual);
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFecha.setForeground(new Color(100, 116, 139));

        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(lblFecha, BorderLayout.EAST);

        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setOpaque(false);

        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setOpaque(false);

        gridPanel.add(crearCard("🏨", "Habitaciones", "Ver mapa interactivo y estado de cuartos.", new Color(59, 130, 246), "habitaciones"));
        gridPanel.add(crearCard("🛎️", "Check-In", "Registrar ingreso de nuevos huéspedes.", new Color(16, 185, 129), "checkin"));
        gridPanel.add(crearCard("🚶", "Check-Out", "Procesar salidas y pagos de cuentas.", new Color(245, 158, 11), "checkout"));
        gridPanel.add(crearCard("📅", "Reservas", "Programar llegadas y asignar cuartos.", new Color(139, 92, 246), "reservas"));
        gridPanel.add(crearCard("👥", "Clientes", "Directorio general de registro de huéspedes.", new Color(236, 72, 153), "clientes"));
        gridPanel.add(crearCard("☕", "Snack Bar", "Registrar consumos a las habitaciones.", new Color(249, 115, 22), "snack"));

        JPanel reportePanel = crearCard("📊", "Reportes y Estadísticas", "Generar reportes de ocupación, ingresos y exportar a PDF/Excel.", new Color(100, 116, 139), "reportes");
        reportePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
        reportePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        cardsContainer.add(gridPanel);
        cardsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        cardsContainer.add(reportePanel);

        mainContent.add(headerPanel, BorderLayout.NORTH);
        mainContent.add(cardsContainer, BorderLayout.CENTER);

        return mainContent;
    }

    // Tarjetas
    private JPanel crearCard(String icono, String titulo, String desc, Color colorIcono, String ventanaDestino) {
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

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        lblIcono.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        lblIcono.setForeground(colorIcono);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(30, 41, 59));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><div style='text-align: center; color: #64748b; font-size: 11px;'>" + desc + "</div></html>");
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblIcono);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblDesc);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(59, 130, 246), 2),
                        BorderFactory.createEmptyBorder(18, 18, 18, 18)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                abrirVentana(ventanaDestino);
            }
        });

        return card;
    }

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
                new Snack().setVisible(true);
                this.dispose();
                break;
            case "reportes":
                new reportes().setVisible(true);
                this.dispose();
                break;
        }
    }

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