package vistas;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import persistencia.ClienteArchivo;
import persistencia.ReservaArchivo;
import modelo.Cliente;
import modelo.Reserva;

public class checkin extends JFrame {

    private JTextField txtBuscador;
    private JTextField txtCliente;
    private JTextField txtHabitacion;
    private JTextField txtFechaLlegada;
    private JButton btnBuscar, btnConfirmarCheckIn;
    private JComboBox<String> cbDuracion;
    private JTextField txtTotal;
    private double precioBaseHabitacion = 0.0;

    private String numHabInicial = "";

    public checkin() {
        this("");
    }

    public checkin(String numeroHabitacionInicial) {
        this.numHabInicial = numeroHabitacionInicial;
        setTitle("Hotel Mapocho - Realizar Check-In");
        setSize(900, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 245, 249)); 
        
        mainPanel.add(crearCabecera(), BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(30, 80, 40, 80));
        
        contentPanel.add(crearBuscadorReserva());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(crearTarjetaCheckIn());
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel crearCabecera() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 41, 59)); 
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

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

        JLabel lblTitulo = new JLabel("Procesar Ingreso (Check-In)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        panelIzquierda.add(btnVolver);
        panelIzquierda.add(lblTitulo);
        header.add(panelIzquierda, BorderLayout.WEST);

        return header;
    }

    private JPanel crearBuscadorReserva() {
        JPanel searchPanel = new JPanel(new BorderLayout(15, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        searchPanel.setMaximumSize(new Dimension(800, 60));
        
        JLabel lblIconBuscar = new JLabel("🔍");
        lblIconBuscar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        
        txtBuscador = new JTextField();
        txtBuscador.setBorder(null);
        txtBuscador.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        TextPrompt placeholder = new TextPrompt("Ingrese DNI del cliente o Código de Reserva...", txtBuscador);
        placeholder.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        
        btnBuscar = new JButton("Buscar Reserva");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setBackground(new Color(59, 130, 246));
        btnBuscar.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchPanel.add(lblIconBuscar, BorderLayout.WEST);
        searchPanel.add(txtBuscador, BorderLayout.CENTER);
        searchPanel.add(btnBuscar, BorderLayout.EAST);

        // Lógica del botón buscar
        btnBuscar.addActionListener(e -> {
            String input = txtBuscador.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un DNI o Código de Reserva");
                return;
            }

            ReservaArchivo ra = new ReservaArchivo();
            Reserva reserva = ra.buscar(input);
            ClienteArchivo ca = new ClienteArchivo();

            if (reserva != null) {
                // Es una reserva
                Cliente c = ca.buscar(String.valueOf(reserva.getIdCliente()));
                if (c != null) {
                    txtCliente.setText(c.getNombres() + " " + c.getApellidos());
                } else {
                    txtCliente.setText("Cliente ID: " + reserva.getIdCliente());
                }
                txtHabitacion.setText(String.valueOf(reserva.getNumeroHabitacion()));
                txtHabitacion.setEditable(false);
                actualizarPrecioBase(String.valueOf(reserva.getNumeroHabitacion()));
            } else {
                // Intentar buscar como DNI de cliente directo
                Cliente c = ca.buscar(input);
                if (c != null) {
                    txtCliente.setText(c.getNombres() + " " + c.getApellidos());
                    if (txtHabitacion.getText().trim().isEmpty()) {
                        txtHabitacion.setEditable(true);
                        JOptionPane.showMessageDialog(this, "Cliente encontrado (Sin reserva). Escriba el número de habitación a asignar.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Cliente encontrado (Sin reserva). La habitación seleccionada se mantendrá.");
                        actualizarPrecioBase(txtHabitacion.getText().trim());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró cliente ni reserva con ese dato. Registre al cliente primero en 'Clientes'.");
                }
            }
        });

        return searchPanel;
    }

    private JPanel crearTarjetaCheckIn() {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.setColor(new Color(226, 232, 240));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.dispose();
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel lblTituloCard = new JLabel("Confirmación de Datos");
        lblTituloCard.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTituloCard.setForeground(new Color(30, 41, 59));
        lblTituloCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos de lectura
        JPanel formPanel = new JPanel(new GridLayout(5, 1, 0, 15));
        formPanel.setOpaque(false);
        
        txtCliente = crearTextFieldMaterial("Nombre del Huésped (Autocompletado)");
        txtCliente.setEditable(false);
        
        txtHabitacion = crearTextFieldMaterial("Habitación Asignada (Autocompletado)");
        if (numHabInicial != null && !numHabInicial.isEmpty()) {
            txtHabitacion.setText(numHabInicial);
        }
        txtHabitacion.setEditable(false);
        txtHabitacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                actualizarPrecioBase(txtHabitacion.getText().trim());
            }
        });
        
        txtFechaLlegada = crearTextFieldMaterial("Fecha y Hora de Ingreso actual");
        txtFechaLlegada.setEditable(false);
        // Automáticamente pone la fecha y hora actual del sistema
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm a");
        txtFechaLlegada.setText(java.time.LocalDateTime.now().format(dtf)); 
        
        String[] opciones = {"Seleccione duración...", "Medio Día", "1 Día", "2 Días", "3 Días", "4 Días", "5 Días", "6 Días", "7 Días"};
        cbDuracion = new JComboBox<>(opciones);
        cbDuracion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cbDuracion.setBackground(Color.WHITE);
        cbDuracion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cbDuracion.addActionListener(e -> calcularTotal());
        
        txtTotal = crearTextFieldMaterial("Total a Pagar (S/)");
        txtTotal.setEditable(false);
        txtTotal.setForeground(new Color(239, 68, 68));
        txtTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        formPanel.add(txtCliente);
        formPanel.add(txtHabitacion);
        formPanel.add(txtFechaLlegada);
        formPanel.add(cbDuracion);
        formPanel.add(txtTotal);
        
        if (numHabInicial != null && !numHabInicial.isEmpty()) {
            actualizarPrecioBase(numHabInicial);
            // Autocompletar datos si hay reserva
            ReservaArchivo ra = new ReservaArchivo();
            for (Reserva r : ra.listar()) {
                if (String.valueOf(r.getNumeroHabitacion()).equals(numHabInicial)) {
                    ClienteArchivo ca = new ClienteArchivo();
                    Cliente c = ca.buscar(String.valueOf(r.getIdCliente()));
                    if (c != null) {
                        txtCliente.setText(c.getNombres() + " " + c.getApellidos());
                    } else {
                        txtCliente.setText("Cliente ID: " + r.getIdCliente());
                    }
                    txtBuscador.setText(r.getCodigoReserva()); // Setear el buscador para que se use al guardar
                    
                    try {
                        String fOut = r.getFechaSalida();
                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        java.time.LocalDate dIn = java.time.LocalDate.now(); // Usar el dia actual en lugar del fIn de la reserva
                        java.time.LocalDate dOut = java.time.LocalDate.parse(fOut, formatter);
                        long days = java.time.temporal.ChronoUnit.DAYS.between(dIn, dOut);
                        if (days >= 0 && days <= 7) {
                            cbDuracion.setSelectedIndex(days == 0 ? 1 : (int)days + 1); // Si es el mismo dia, cobra medio dia, si no, los dias enteros
                        } else if (days > 7) {
                            cbDuracion.setSelectedIndex(8);
                        }
                    } catch (Exception ex) {
                        // Ignorar
                    }
                    
                    break;
                }
            }
        }

        btnConfirmarCheckIn = new JButton("Confirmar Ingreso (Ocupar Habitación)");
        btnConfirmarCheckIn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnConfirmarCheckIn.setForeground(Color.WHITE);
        btnConfirmarCheckIn.setBackground(new Color(16, 185, 129)); // Verde éxito
        btnConfirmarCheckIn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnConfirmarCheckIn.setOpaque(true);
        btnConfirmarCheckIn.setBorderPainted(false);
        btnConfirmarCheckIn.setFocusPainted(false);
        btnConfirmarCheckIn.setBorder(new EmptyBorder(12, 30, 12, 30));
        btnConfirmarCheckIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmarCheckIn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnConfirmarCheckIn.addActionListener(e -> {
            String habitacionStr = txtHabitacion.getText().trim();
            if (txtCliente.getText().isEmpty() || habitacionStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe buscar un cliente/reserva e ingresar una habitación.");
                return;
            }

            try {
                int numHabitacion = Integer.parseInt(habitacionStr);
                persistencia.HabitacionArchivo ha = new persistencia.HabitacionArchivo();
                modelo.Habitacion h = ha.buscar(String.valueOf(numHabitacion));
                
                if (h != null) {
                    if (!h.getEstado().equals("LIBRE") && !h.getEstado().equals("RESERVADA")) {
                        JOptionPane.showMessageDialog(this, "La habitación seleccionada no está disponible.");
                        return;
                    }
                    
                    // Actualizar habitación a Ocupada
                    h.setEstado("OCUPADA");
                    ha.actualizar(h);

                    // Si no tiene código de reserva, le creamos uno ficticio o manejamos la estancia actual.
                    // Para simplificar, asumiremos que si vino directo, se puede registrar una reserva express
                    String busqueda = txtBuscador.getText().trim();
                    ReservaArchivo ra = new ReservaArchivo();
                    Reserva r = ra.buscar(busqueda);
                    
                    int idx = cbDuracion.getSelectedIndex();
                    String fechaSalida = "Por definir";
                    java.time.LocalDateTime now = java.time.LocalDateTime.now();
                    java.time.format.DateTimeFormatter dtf2 = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm a");
                    if (idx > 0) {
                        if (idx == 1) {
                            fechaSalida = now.plusHours(12).format(dtf2);
                        } else {
                            fechaSalida = now.plusDays(idx - 1).format(dtf2);
                        }
                    }
                    
                    if (r == null) {
                        // Fue un check-in directo (DNI)
                        r = new Reserva();
                        r.setCodigoReserva("RES-" + System.currentTimeMillis());
                        r.setIdCliente(Integer.parseInt(busqueda)); 
                        r.setNumeroHabitacion(numHabitacion);
                        r.setFechaIngreso(txtFechaLlegada.getText());
                        r.setFechaSalida(fechaSalida);
                        ra.registrar(r);
                    } else {
                        r.setFechaIngreso(txtFechaLlegada.getText());
                        r.setFechaSalida(fechaSalida);
                        ra.actualizar(r);
                    }
                    
                    if (idx > 0 && precioBaseHabitacion > 0) {
                        persistencia.ConsumoArchivo ca = new persistencia.ConsumoArchivo();
                        modelo.Consumo c = new modelo.Consumo();
                        c.setIdConsumo((int)(Math.random() * 900000) + 100000);
                        c.setNumeroHabitacion(numHabitacion);
                        if (idx == 1) {
                            c.setCodigoSnack("MEDIODIA");
                            c.setCantidad(1);
                            c.setSubtotal(precioBaseHabitacion / 2.0);
                        } else {
                            c.setCodigoSnack("ESTADIA");
                            c.setCantidad(idx - 1);
                            c.setSubtotal(precioBaseHabitacion * (idx - 1));
                        }
                        ca.registrar(c);
                    }

                    JOptionPane.showMessageDialog(this, "Check-In realizado con éxito. Habitación OCUPADA.");
                    new menu().setVisible(true);
                    this.dispose();

                } else {
                    JOptionPane.showMessageDialog(this, "La habitación " + numHabitacion + " no existe.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Número de habitación inválido.");
            }
        });

        cardPanel.add(lblTituloCard);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(formPanel);
        cardPanel.add(Box.createVerticalGlue());
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(btnConfirmarCheckIn);

        return cardPanel;
    }

    private void actualizarPrecioBase(String numHab) {
        if (!numHab.isEmpty()) {
            persistencia.HabitacionArchivo ha = new persistencia.HabitacionArchivo();
            modelo.Habitacion h = ha.buscar(numHab);
            if (h != null) {
                precioBaseHabitacion = h.getPrecio();
                calcularTotal();
            } else {
                precioBaseHabitacion = 0.0;
                txtTotal.setText("Total a Pagar (S/): 0.00");
            }
        }
    }

    private void calcularTotal() {
        if (cbDuracion == null || txtTotal == null) return;
        int idx = cbDuracion.getSelectedIndex();
        if (idx > 0 && precioBaseHabitacion > 0) {
            double total = 0;
            if (idx == 1) {
                total = precioBaseHabitacion / 2.0;
            } else {
                total = precioBaseHabitacion * (idx - 1);
            }
            txtTotal.setText(String.format("Total a Pagar (S/): %.2f", total));
        } else {
            txtTotal.setText("Total a Pagar (S/): 0.00");
        }
    }

    private JTextField crearTextFieldMaterial(String placeholderTexto) {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txt.setForeground(new Color(15, 23, 42));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(203, 213, 225)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        txt.setOpaque(false);
        TextPrompt placeholder = new TextPrompt(placeholderTexto, txt);
        placeholder.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        placeholder.setForeground(new Color(148, 163, 184));
        return txt;
    }

    class TextPrompt extends JLabel implements java.awt.event.FocusListener, javax.swing.event.DocumentListener {
        private JTextField component;
        public TextPrompt(String text, JTextField component) {
            this.component = component;
            setText(text);
            setFont(component.getFont());
            setForeground(Color.GRAY);
            component.addFocusListener(this);
            component.getDocument().addDocumentListener(this);
            component.setLayout(new BorderLayout());
            component.add(this);
            checkForPrompt();
        }
        private void checkForPrompt() {
            if (component.getText().isEmpty()) setVisible(true);
            else setVisible(false);
        }
        @Override public void focusGained(java.awt.event.FocusEvent e) { checkForPrompt(); }
        @Override public void focusLost(java.awt.event.FocusEvent e) { checkForPrompt(); }
        @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { checkForPrompt(); }
        @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { checkForPrompt(); }
        @Override public void changedUpdate(javax.swing.event.DocumentEvent e) {}
    }
}