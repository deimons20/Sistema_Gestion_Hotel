package vistas;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import persistencia.*;
import modelo.*;
import java.util.ArrayList;

public class reservas extends JFrame {

    private JTextField txtDNICliente;
    private JComboBox<String> cmbNumHabitacion;
    private JTextField txtFechaIngreso;
    private JTextField txtFechaSalida;
    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    
    private JButton btnGuardar, btnCancelar, btnIrCheckIn;

    public reservas() {
        setTitle("Hotel Paraíso - Gestión de Reservas");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 245, 249)); 
        
        mainPanel.add(crearCabecera(), BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout(25, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(25, 30, 30, 30));
        
        contentPanel.add(crearPanelFormulario(), BorderLayout.WEST);
        contentPanel.add(crearPanelTabla(), BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        if (modeloTabla == null) return;
        modeloTabla.setRowCount(0);
        ReservaArchivo ra = new ReservaArchivo();
        for (Reserva r : ra.listar()) {
            modeloTabla.addRow(new Object[]{
                r.getCodigoReserva(),
                r.getIdCliente(),
                "Hab. " + r.getNumeroHabitacion(),
                r.getFechaIngreso(),
                r.getFechaSalida()
            });
        }
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

        JLabel lblTitulo = new JLabel("Gestión de Reservas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        panelIzquierda.add(btnVolver);
        panelIzquierda.add(lblTitulo);
        header.add(panelIzquierda, BorderLayout.WEST);

        return header;
    }

    private JPanel crearPanelFormulario() {
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
        cardPanel.setPreferredSize(new Dimension(320, 0));
        cardPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblTituloCard = new JLabel("Nueva Reserva");
        lblTituloCard.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloCard.setForeground(new Color(30, 41, 59));
        lblTituloCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 0, 20));
        formPanel.setOpaque(false);
        
        txtDNICliente = crearTextFieldMaterial("DNI del Cliente");
        
        cmbNumHabitacion = new JComboBox<>();
        cmbNumHabitacion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbNumHabitacion.setBackground(Color.WHITE);
        cmbNumHabitacion.addItem("Seleccione Habitación...");
        try {
            for (Habitacion h : new HabitacionArchivo().listar()) {
                if (h.getEstado().equals("LIBRE")) {
                    String piso = "Piso " + String.valueOf(h.getNumeroHabitacion()).charAt(0);
                    cmbNumHabitacion.addItem(h.getNumeroHabitacion() + " - S/ " + h.getPrecio() + " (" + piso + ")");
                }
            }
        } catch (Exception ex) {}

        txtFechaIngreso = crearTextFieldMaterial("Fecha Llegada (DD/MM/AAAA)");
        txtFechaSalida = crearTextFieldMaterial("Fecha Salida (DD/MM/AAAA)");
        
        formPanel.add(txtDNICliente);
        formPanel.add(cmbNumHabitacion);
        formPanel.add(txtFechaIngreso);
        formPanel.add(txtFechaSalida);

        btnGuardar = crearBoton("Guardar Reserva", new Color(139, 92, 246)); // Morado
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        btnGuardar.addActionListener(e -> {
            try {
                String dniStr = txtDNICliente.getText().trim();
                
                String habStr = "";
                if (cmbNumHabitacion.getSelectedIndex() > 0) {
                    habStr = cmbNumHabitacion.getSelectedItem().toString().split(" ")[0]; // Obtener solo el número
                }
                
                String fecIn = txtFechaIngreso.getText().trim();
                String fecOut = txtFechaSalida.getText().trim();
                
                if (dniStr.isEmpty() || habStr.isEmpty() || fecIn.isEmpty() || fecOut.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                    return;
                }
                
                int dni = Integer.parseInt(dniStr);
                int numHab = Integer.parseInt(habStr);
                
                HabitacionArchivo ha = new HabitacionArchivo();
                Habitacion h = ha.buscar(String.valueOf(numHab));
                if (h == null) {
                    JOptionPane.showMessageDialog(this, "La habitación no existe.");
                    return;
                }
                
                if (!h.getEstado().equals("LIBRE")) {
                    JOptionPane.showMessageDialog(this, "La habitación no está disponible (Estado: " + h.getEstado() + ").");
                    return;
                }
                
                Reserva r = new Reserva();
                r.setCodigoReserva("RES-" + System.currentTimeMillis());
                r.setIdCliente(dni);
                r.setNumeroHabitacion(numHab);
                r.setFechaIngreso(fecIn);
                r.setFechaSalida(fecOut);
                
                ReservaArchivo ra = new ReservaArchivo();
                ra.registrar(r);
                
                h.setEstado("RESERVADA");
                ha.actualizar(h);
                
                JOptionPane.showMessageDialog(this, "Reserva registrada con éxito.");
                txtDNICliente.setText("");
                cmbNumHabitacion.setSelectedIndex(0);
                txtFechaIngreso.setText("");
                txtFechaSalida.setText("");
                cargarDatosTabla();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos (DNI y Habitación deben ser números).");
            }
        });

        cardPanel.add(lblTituloCard);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(formPanel);
        cardPanel.add(Box.createVerticalGlue());
        cardPanel.add(btnGuardar);

        return cardPanel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);

        String[] columnas = {"Cod. Reserva", "DNI Cliente", "Habitación", "F. Llegada", "F. Salida"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaReservas = new JTable(modeloTabla);
        tablaReservas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaReservas.setRowHeight(35); 
        tablaReservas.setShowGrid(false); 
        tablaReservas.setIntercellSpacing(new Dimension(0, 0));
        tablaReservas.setSelectionBackground(new Color(239, 246, 255)); 
        tablaReservas.setSelectionForeground(new Color(30, 58, 138));
        
        JTableHeader theader = tablaReservas.getTableHeader();
        theader.setBackground(Color.WHITE);
        theader.setForeground(new Color(100, 116, 139));
        theader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        theader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        ((DefaultTableCellRenderer)theader.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        JScrollPane scroll = new JScrollPane(tablaReservas);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        // Panel de acciones inferior (Flujo UX)
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelAcciones.setOpaque(false);
        
        btnCancelar = crearBoton("Cancelar Reserva", new Color(239, 68, 68)); // Rojo
        btnIrCheckIn = crearBoton("Llegó el cliente ➔ Ir a Check-In", new Color(16, 185, 129)); // Verde
        
        btnCancelar.addActionListener(e -> {
            int fila = tablaReservas.getSelectedRow();
            if (fila >= 0) {
                String cod = modeloTabla.getValueAt(fila, 0).toString();
                String habStr = modeloTabla.getValueAt(fila, 2).toString().replace("Hab. ", "").trim();
                
                ReservaArchivo ra = new ReservaArchivo();
                ra.eliminar(cod);
                
                try {
                    int numHab = Integer.parseInt(habStr);
                    HabitacionArchivo ha = new HabitacionArchivo();
                    Habitacion h = ha.buscar(String.valueOf(numHab));
                    if (h != null && h.getEstado().equals("RESERVADA")) {
                        h.setEstado("LIBRE");
                        ha.actualizar(h);
                    }
                } catch (Exception ex) {}
                
                JOptionPane.showMessageDialog(this, "Reserva cancelada.");
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla.");
            }
        });
        
        btnIrCheckIn.addActionListener(e -> {
            new checkin().setVisible(true);
            this.dispose();
        });

        panelAcciones.add(btnCancelar);
        panelAcciones.add(btnIrCheckIn);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelAcciones, BorderLayout.SOUTH);

        return panel;
    }

    private JTextField crearTextFieldMaterial(String placeholderTexto) {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setForeground(new Color(15, 23, 42));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(203, 213, 225)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        txt.setOpaque(false);
        TextPrompt placeholder = new TextPrompt(placeholderTexto, txt);
        placeholder.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        placeholder.setForeground(new Color(148, 163, 184));
        return txt;
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorFondo);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
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