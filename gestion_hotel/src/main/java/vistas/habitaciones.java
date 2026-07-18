package vistas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import persistencia.HabitacionArchivo;
import modelo.Habitacion;
import java.util.ArrayList;

public class habitaciones extends JFrame {

    private JLabel lblTituloDetalle;
    private JLabel lblEstadoValor;
    private JLabel lblTipoValor;
    private JLabel lblHuespedValor;
    private JLabel lblFechaValor;
    private JButton btnAccionPrincipal;
    private JButton btnAccionSecundaria;

    private final Color COLOR_LIBRE = new Color(16, 185, 129); 
    private final Color COLOR_OCUPADA = new Color(239, 68, 68); 
    private final Color COLOR_RESERVADA = new Color(245, 158, 11); 
    private final Color COLOR_MANTENIMIENTO = new Color(100, 116, 139); 

    public habitaciones() {
        setTitle("Hotel Mapocho - Estado de Habitaciones");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL imgUrl = getClass().getResource("/img/background-habitaciones.jpg");
                if (imgUrl != null) {
                    Image bgImage = new ImageIcon(imgUrl).getImage();
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(20, 20, 20)); 
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); 

        JPanel glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setLayout(new BorderLayout(25, 25));
        
        glassPanel.setBackground(new Color(15, 23, 42, 150)); 
        
        glassPanel.setPreferredSize(new Dimension(940, 600));
        glassPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        glassPanel.add(crearCabecera(), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(25, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(crearMapaHabitaciones(), BorderLayout.CENTER);
        contentPanel.add(crearPanelDetalles(), BorderLayout.EAST);

        glassPanel.add(contentPanel, BorderLayout.CENTER);

        backgroundPanel.add(glassPanel);
        add(backgroundPanel);
        
        actualizarDetalles("101", "LIBRE", "Básica - S/ 90.00");
    }

    private JPanel crearCabecera() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 50)));
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel panelIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelIzquierda.setOpaque(false);

        JButton btnVolver = new JButton("<< Menú Principal") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(new Color(255, 255, 255, 40));
        btnVolver.setUI(new javax.swing.plaf.basic.BasicButtonUI()); 
        btnVolver.setOpaque(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnVolver.addActionListener(e -> {
            new menu().setVisible(true);
            this.dispose();
        });

        JLabel lblTitulo = new JLabel("Estado de Habitaciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);

        panelIzquierda.add(btnVolver);
        panelIzquierda.add(lblTitulo);

        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        panelLeyenda.setOpaque(false);
        panelLeyenda.add(crearItemLeyenda("Libre", COLOR_LIBRE));
        panelLeyenda.add(crearItemLeyenda("Ocupada", COLOR_OCUPADA));
        panelLeyenda.add(crearItemLeyenda("Reservada", COLOR_RESERVADA));

        header.add(panelIzquierda, BorderLayout.WEST);
        header.add(panelLeyenda, BorderLayout.EAST);

        return header;
    }

    private JPanel crearItemLeyenda(String texto, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);
        
        JPanel circulo = new JPanel();
        circulo.setBackground(color);
        circulo.setPreferredSize(new Dimension(12, 12));
        circulo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(203, 213, 225));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        item.add(circulo);
        item.add(lbl);
        return item;
    }

    private JScrollPane crearMapaHabitaciones() {
        JPanel mapaPanel = new JPanel();
        mapaPanel.setLayout(new BoxLayout(mapaPanel, BoxLayout.Y_AXIS));
        mapaPanel.setOpaque(false);

        HabitacionArchivo ha = new HabitacionArchivo();
        ArrayList<Habitacion> listaHabitaciones = ha.listar();

        if (listaHabitaciones.isEmpty()) {
            for (int i = 1; i <= 3; i++) {
                String tipo = (i == 1) ? "Básica" : (i == 2) ? "Doble" : "Suite";
                double precio = (i == 1) ? 90.00 : (i == 2) ? 160.00 : 320.00;
                for (int j = 1; j <= 5; j++) {
                    Habitacion h = new Habitacion();
                    h.setNumeroHabitacion(i * 100 + j);
                    h.setTipo(tipo);
                    h.setEstado("LIBRE");
                    h.setPrecio(precio);
                    ha.registrar(h);
                }
            }
            listaHabitaciones = ha.listar();
        }

        ArrayList<String[]> piso1 = new ArrayList<>();
        ArrayList<String[]> piso2 = new ArrayList<>();
        ArrayList<String[]> piso3 = new ArrayList<>();

        for (Habitacion h : listaHabitaciones) {
            String[] data = {String.valueOf(h.getNumeroHabitacion()), h.getEstado()};
            if (h.getNumeroHabitacion() < 200) piso1.add(data);
            else if (h.getNumeroHabitacion() < 300) piso2.add(data);
            else piso3.add(data);
        }

        mapaPanel.add(crearSeccionPiso("PISO 1 (Básicas - S/ 90.00)", piso1.toArray(new String[0][0])));
        mapaPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mapaPanel.add(crearSeccionPiso("PISO 2 (Dobles - S/ 160.00)", piso2.toArray(new String[0][0])));
        mapaPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mapaPanel.add(crearSeccionPiso("PISO 3 (Suites - S/ 320.00)", piso3.toArray(new String[0][0])));

        JScrollPane scroll = new JScrollPane(mapaPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        return scroll;
    }

    private JPanel crearSeccionPiso(String tituloPiso, String[][] habitaciones) {
        JPanel seccion = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        seccion.setOpaque(false);
        seccion.setBackground(new Color(0, 0, 0, 80)); 
        seccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(59, 130, 246)), 
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(tituloPiso);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(147, 197, 253));

        JPanel botonesPanel = new JPanel(new GridLayout(1, 5, 12, 0));
        botonesPanel.setOpaque(false);

        for (String[] hab : habitaciones) {
            String numero = hab[0];
            String estado = hab[1];
            JButton btnHabitacion = new JButton(numero);
            btnHabitacion.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnHabitacion.setForeground(Color.WHITE);
            
            btnHabitacion.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            btnHabitacion.setOpaque(true);
            btnHabitacion.setBorderPainted(false);
            btnHabitacion.setFocusPainted(false);
            
            btnHabitacion.setPreferredSize(new Dimension(0, 50));
            btnHabitacion.setCursor(new Cursor(Cursor.HAND_CURSOR));

            switch (estado) {
                case "LIBRE": btnHabitacion.setBackground(COLOR_LIBRE); break;
                case "OCUPADA": btnHabitacion.setBackground(COLOR_OCUPADA); break;
                case "RESERVADA": btnHabitacion.setBackground(COLOR_RESERVADA); break;
                case "MANTENIMIENTO": btnHabitacion.setBackground(COLOR_MANTENIMIENTO); break;
            }

            btnHabitacion.addActionListener((ActionEvent e) -> {
                String tipoHab = tituloPiso.substring(8, tituloPiso.indexOf(")"));
                actualizarDetalles(numero, estado, tipoHab);
            });

            botonesPanel.add(btnHabitacion);
        }

        seccion.add(lblTitulo, BorderLayout.NORTH);
        seccion.add(botonesPanel, BorderLayout.CENTER);
        return seccion;
    }

    private JPanel crearPanelDetalles() {
        JPanel detallesPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        detallesPanel.setOpaque(false);
        detallesPanel.setLayout(new BoxLayout(detallesPanel, BoxLayout.Y_AXIS));
        detallesPanel.setBackground(new Color(0, 0, 0, 100)); 
        detallesPanel.setPreferredSize(new Dimension(280, 0));
        detallesPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 20), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        lblTituloDetalle = new JLabel("Habitación ---");
        lblTituloDetalle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTituloDetalle.setForeground(Color.WHITE);
        lblTituloDetalle.setAlignmentX(Component.CENTER_ALIGNMENT);

        detallesPanel.add(lblTituloDetalle);
        detallesPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        lblEstadoValor = agregarFilaInfo(detallesPanel, "ESTADO ACTUAL", "---");
        lblTipoValor = agregarFilaInfo(detallesPanel, "TIPO Y TARIFA", "---");
        lblHuespedValor = agregarFilaInfo(detallesPanel, "HUÉSPED ASIGNADO", "---");
        lblFechaValor = agregarFilaInfo(detallesPanel, "FECHA SALIDA", "---");

        detallesPanel.add(Box.createVerticalGlue()); 

        btnAccionPrincipal = new JButton("Acción");
        btnAccionPrincipal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnAccionPrincipal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAccionPrincipal.setForeground(Color.WHITE);
        btnAccionPrincipal.setBackground(new Color(59, 130, 246));
        
        btnAccionPrincipal.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnAccionPrincipal.setOpaque(true);
        btnAccionPrincipal.setBorderPainted(false);
        btnAccionPrincipal.setFocusPainted(false);
        
        btnAccionPrincipal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAccionPrincipal.setAlignmentX(Component.CENTER_ALIGNMENT);

        detallesPanel.add(btnAccionPrincipal);
        detallesPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        btnAccionSecundaria = new JButton("Acción 2");
        btnAccionSecundaria.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btnAccionSecundaria.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAccionSecundaria.setForeground(Color.WHITE);
        btnAccionSecundaria.setBackground(new Color(100, 116, 139));
        btnAccionSecundaria.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnAccionSecundaria.setOpaque(true);
        btnAccionSecundaria.setBorderPainted(false);
        btnAccionSecundaria.setFocusPainted(false);
        btnAccionSecundaria.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAccionSecundaria.setAlignmentX(Component.CENTER_ALIGNMENT);

        detallesPanel.add(btnAccionSecundaria);

        return detallesPanel;
    }

    private JLabel agregarFilaInfo(JPanel panel, String titulo, String valorInicial) {
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitulo.setForeground(new Color(148, 163, 184)); 
        
        JLabel lblValor = new JLabel(valorInicial);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblValor.setForeground(Color.WHITE);

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        fila.setOpaque(false);
        fila.setLayout(new BoxLayout(fila, BoxLayout.Y_AXIS));
        fila.add(lblTitulo);
        fila.add(lblValor);
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(fila);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        return lblValor;
    }

    private void actualizarDetalles(String numero, String estado, String tipo) {
        lblTituloDetalle.setText("Habitación " + numero);
        lblTipoValor.setText(tipo);

        if (estado.equals("LIBRE")) {
            lblEstadoValor.setText("DISPONIBLE");
            lblEstadoValor.setForeground(COLOR_LIBRE);
            lblHuespedValor.setText("Ninguno");
            lblFechaValor.setText("---");
            
            btnAccionPrincipal.setText("Realizar Check-In");
            btnAccionPrincipal.setBackground(COLOR_LIBRE);
            btnAccionPrincipal.setEnabled(true);
            btnAccionPrincipal.setVisible(true);
            
            quitarEventos(btnAccionPrincipal);
            btnAccionPrincipal.addActionListener(e -> {
                new checkin(numero).setVisible(true);
                this.dispose();
            });

            btnAccionSecundaria.setText("Poner en Mantenimiento");
            btnAccionSecundaria.setVisible(true);
            quitarEventos(btnAccionSecundaria);
            btnAccionSecundaria.addActionListener(e -> cambiarEstadoMantenimiento(numero, "MANTENIMIENTO"));

        } else if (estado.equals("OCUPADA")) {
            lblEstadoValor.setText("OCUPADA");
            lblEstadoValor.setForeground(COLOR_OCUPADA);
            
            persistencia.ReservaArchivo ra = new persistencia.ReservaArchivo();
            modelo.Reserva reservaActiva = null;
            for (modelo.Reserva r : ra.listar()) {
                if (r.getNumeroHabitacion() == Integer.parseInt(numero)) {
                    reservaActiva = r;
                }
            }
            if (reservaActiva != null) {
                persistencia.ClienteArchivo ca = new persistencia.ClienteArchivo();
                modelo.Cliente c = ca.buscar(String.valueOf(reservaActiva.getIdCliente()));
                if (c != null) {
                    lblHuespedValor.setText(c.getNombres() + " " + c.getApellidos());
                } else {
                    lblHuespedValor.setText("Cliente ID: " + reservaActiva.getIdCliente());
                }
                lblFechaValor.setText(reservaActiva.getFechaSalida() != null ? reservaActiva.getFechaSalida() : "Consulte Checkout");
            } else {
                lblHuespedValor.setText("Consultar Sistema");
                lblFechaValor.setText("Consulte Checkout");
            }
            
            btnAccionPrincipal.setText("Procesar Check-Out");
            btnAccionPrincipal.setBackground(new Color(59, 130, 246)); 
            btnAccionPrincipal.setEnabled(true);
            btnAccionPrincipal.setVisible(true);

            quitarEventos(btnAccionPrincipal);
            btnAccionPrincipal.addActionListener(e -> {
                new checkout().setVisible(true);
                this.dispose();
            });

            btnAccionSecundaria.setText("Extender Estadía");
            btnAccionSecundaria.setVisible(true);
            btnAccionSecundaria.setBackground(new Color(139, 92, 246)); 
            quitarEventos(btnAccionSecundaria);
            final modelo.Reserva rFinal = reservaActiva;
            btnAccionSecundaria.addActionListener(e -> {
                if (rFinal != null) {
                    String input = JOptionPane.showInputDialog(this, "¿Cuántos días adicionales desea quedarse?");
                    if (input != null && !input.trim().isEmpty()) {
                        try {
                            int dias = Integer.parseInt(input.trim());
                            if (dias > 0) {
                                String fOutStr = rFinal.getFechaSalida();
                                String fechaBase = fOutStr.substring(0, 10);
                                java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                java.time.LocalDate dOut = java.time.LocalDate.parse(fechaBase, fmt);
                                dOut = dOut.plusDays(dias);
                                String nuevaFecha = dOut.format(fmt);
                                
                                if (fOutStr.length() > 10) {
                                    nuevaFecha += fOutStr.substring(10);
                                } else {
                                    nuevaFecha += " - 12:00 m.";
                                }
                                
                                rFinal.setFechaSalida(nuevaFecha);
                                persistencia.ReservaArchivo ra2 = new persistencia.ReservaArchivo();
                                ra2.actualizar(rFinal);
                                
                                persistencia.HabitacionArchivo ha2 = new persistencia.HabitacionArchivo();
                                modelo.Habitacion h2 = ha2.buscar(numero);
                                double precioBase = h2 != null ? h2.getPrecio() : 0.0;
                                
                                if (precioBase > 0) {
                                    persistencia.ConsumoArchivo ca2 = new persistencia.ConsumoArchivo();
                                    modelo.Consumo c2 = new modelo.Consumo();
                                    c2.setIdConsumo((int)(Math.random() * 900000) + 100000);
                                    c2.setNumeroHabitacion(Integer.parseInt(numero));
                                    c2.setCodigoSnack("ESTADIA_EXT");
                                    c2.setCantidad(dias);
                                    c2.setSubtotal(precioBase * dias);
                                    ca2.registrar(c2);
                                }
                                
                                JOptionPane.showMessageDialog(this, "Estadía extendida por " + dias + " días.\\nSe ha cargado el costo a la habitación automáticamente.");
                                new habitaciones().setVisible(true);
                                this.dispose();
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Número de días inválido o error en la fecha.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró la reserva activa para extender.");
                }
            });

        } else if (estado.equals("RESERVADA")) {
            lblEstadoValor.setText("RESERVADA");
            lblEstadoValor.setForeground(COLOR_RESERVADA);
            lblHuespedValor.setText("Reservado");
            lblFechaValor.setText("Consultar Reservas");
            
            btnAccionPrincipal.setText("Confirmar Check-In");
            btnAccionPrincipal.setBackground(COLOR_RESERVADA);
            btnAccionPrincipal.setEnabled(true);
            btnAccionPrincipal.setVisible(true);
            
            quitarEventos(btnAccionPrincipal);
            btnAccionPrincipal.addActionListener(e -> {
                new checkin(numero).setVisible(true);
                this.dispose();
            });
            
            btnAccionSecundaria.setVisible(false);

        } else {
            lblEstadoValor.setText("MANTENIMIENTO");
            lblEstadoValor.setForeground(COLOR_MANTENIMIENTO);
            lblHuespedValor.setText("Personal de limpieza");
            lblFechaValor.setText("---");
            
            btnAccionPrincipal.setText("Finalizar Mantenimiento");
            btnAccionPrincipal.setBackground(COLOR_LIBRE);
            btnAccionPrincipal.setEnabled(true);
            btnAccionPrincipal.setVisible(true);
            
            quitarEventos(btnAccionPrincipal);
            btnAccionPrincipal.addActionListener(e -> cambiarEstadoMantenimiento(numero, "LIBRE"));
            
            btnAccionSecundaria.setVisible(false);
        }
    }

    private void cambiarEstadoMantenimiento(String numero, String nuevoEstado) {
        HabitacionArchivo ha = new HabitacionArchivo();
        Habitacion h = ha.buscar(numero);
        if (h != null) {
            h.setEstado(nuevoEstado);
            ha.actualizar(h);
            new habitaciones().setVisible(true);
            this.dispose();
        }
    }

    private void quitarEventos(JButton btn) {
        for (java.awt.event.ActionListener al : btn.getActionListeners()) {
            btn.removeActionListener(al);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }
        
        SwingUtilities.invokeLater(() -> {
            new habitaciones().setVisible(true);
        });
    }
}