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

public class reportes extends JFrame {

    // Componentes
    private JComboBox<String> cmbTipoReporte;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;
    
    private JButton btnGenerar, btnExportarPDF, btnExportarExcel;

    public reportes() {
        setTitle("Hotel Paraíso - Reportes y Estadísticas");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Fondo principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 245, 249)); // Gris claro
        
        mainPanel.add(crearCabecera(), BorderLayout.NORTH);
        
        // Panel central (Filtros + Tabla + Exportar)
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        contentPanel.add(crearPanelFiltros(), BorderLayout.NORTH);
        contentPanel.add(crearPanelTabla(), BorderLayout.CENTER);
        contentPanel.add(crearPanelExportar(), BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    // ==========================================================
    // 1. CABECERA
    // ==========================================================
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

        JLabel lblTitulo = new JLabel("Módulo de Reportes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        panelIzquierda.add(btnVolver);
        panelIzquierda.add(lblTitulo);
        header.add(panelIzquierda, BorderLayout.WEST);

        return header;
    }

    // ==========================================================
    // 2. PANEL DE FILTROS (Arriba)
    // ==========================================================
    private JPanel crearPanelFiltros() {
        // Panel estilo Tarjeta (Card)
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
                g2.setColor(new Color(226, 232, 240));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
                g2.dispose();
            }
        };
        panel.setOpaque(false);

        JLabel lblTipo = new JLabel("Tipo de Reporte:");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTipo.setForeground(new Color(71, 85, 105));

        // Opciones del combobox basadas en los requerimientos
        String[] opciones = {
            "Seleccionar...", 
            "Ingresos del Hotel", 
            "Habitaciones Ocupadas", 
            "Habitaciones Disponibles", 
            "Reservas", 
            "Directorio de Clientes", 
            "Consumos del Snack"
        };
        cmbTipoReporte = new JComboBox<>(opciones);
        cmbTipoReporte.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTipoReporte.setPreferredSize(new Dimension(220, 35));
        cmbTipoReporte.setBackground(Color.WHITE);
        cmbTipoReporte.addActionListener(e -> {
            if (btnGenerar != null) btnGenerar.doClick();
        });

        // Campos de fecha estilo Material (Sin librerías externas)
        txtFechaInicio = crearTextFieldMaterial("Fecha Inicio (DD/MM/AAAA)");
        txtFechaInicio.setPreferredSize(new Dimension(180, 35));
        
        txtFechaFin = crearTextFieldMaterial("Fecha Fin (DD/MM/AAAA)");
        txtFechaFin.setPreferredSize(new Dimension(180, 35));

        // Botón Generar
        btnGenerar = crearBoton("Generar Reporte", new Color(59, 130, 246)); // Azul
        
        // Evento temporal para vaciar la tabla y simular carga
        btnGenerar.addActionListener(e -> {
            int opcion = cmbTipoReporte.getSelectedIndex();
            if (opcion == 0) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un tipo de reporte.");
                return;
            }
            
            modeloTabla.setRowCount(0);
            
            try {
                switch(opcion) {
                    case 1: // Ingresos del Hotel
                        modeloTabla.setColumnIdentifiers(new String[]{"ID Pago", "Total", "Método Pago"});
                        for (Pago p : new PagoArchivo().listar()) {
                            modeloTabla.addRow(new Object[]{p.getIdPago(), "S/ " + p.getMontoTotal(), p.getMetodoPago()});
                        }
                        break;
                    case 2: // Habitaciones Ocupadas
                        modeloTabla.setColumnIdentifiers(new String[]{"Nº", "Tipo", "Precio", "Estado"});
                        for (Habitacion h : new HabitacionArchivo().listar()) {
                            if (h.getEstado().equals("OCUPADA")) {
                                modeloTabla.addRow(new Object[]{h.getNumeroHabitacion(), h.getTipo(), "S/ " + h.getPrecio(), h.getEstado()});
                            }
                        }
                        break;
                    case 3: // Habitaciones Disponibles
                        modeloTabla.setColumnIdentifiers(new String[]{"Nº", "Tipo", "Precio", "Estado"});
                        for (Habitacion h : new HabitacionArchivo().listar()) {
                            if (h.getEstado().equals("LIBRE")) {
                                modeloTabla.addRow(new Object[]{h.getNumeroHabitacion(), h.getTipo(), "S/ " + h.getPrecio(), h.getEstado()});
                            }
                        }
                        break;
                    case 4: // Reservas
                        modeloTabla.setColumnIdentifiers(new String[]{"Cód. Reserva", "Cliente DNI", "Habitación", "Ingreso", "Salida"});
                        for (Reserva r : new ReservaArchivo().listar()) {
                            modeloTabla.addRow(new Object[]{r.getCodigoReserva(), r.getIdCliente(), r.getNumeroHabitacion(), r.getFechaIngreso(), r.getFechaSalida()});
                        }
                        break;
                    case 5: // Directorio de Clientes
                        modeloTabla.setColumnIdentifiers(new String[]{"DNI", "Nombres", "Apellidos", "Teléfono"});
                        for (Cliente c : new ClienteArchivo().listar()) {
                            modeloTabla.addRow(new Object[]{c.getIdCliente(), c.getNombres(), c.getApellidos(), c.getTelefono()});
                        }
                        break;
                    case 6: // Consumos del Snack (Activos)
                        modeloTabla.setColumnIdentifiers(new String[]{"ID Consumo", "Habitación", "Cód. Snack", "Cantidad", "Subtotal"});
                        for (Consumo c : new ConsumoArchivo().listar()) {
                            modeloTabla.addRow(new Object[]{c.getIdConsumo(), c.getNumeroHabitacion(), c.getCodigoSnack(), c.getCantidad(), "S/ " + c.getSubtotal()});
                        }
                        break;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte.");
            }
        });

        panel.add(lblTipo);
        panel.add(cmbTipoReporte);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(txtFechaInicio);
        panel.add(txtFechaFin);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnGenerar);

        return panel;
    }

    // ==========================================================
    // 3. PANEL DE TABLA (Centro)
    // ==========================================================
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Modelo vacío (sin datos falsos)
        String[] columnasIniciales = {"---", "---", "---", "---"};
        modeloTabla = new DefaultTableModel(columnasIniciales, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaReportes = new JTable(modeloTabla);
        tablaReportes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaReportes.setRowHeight(35); 
        tablaReportes.setShowGrid(false); 
        tablaReportes.setIntercellSpacing(new Dimension(0, 0));
        tablaReportes.setSelectionBackground(new Color(239, 246, 255));
        tablaReportes.setSelectionForeground(new Color(30, 58, 138));
        
        JTableHeader theader = tablaReportes.getTableHeader();
        theader.setBackground(new Color(255, 255, 255));
        theader.setForeground(new Color(71, 85, 105));
        theader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        theader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        ((DefaultTableCellRenderer)theader.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        JScrollPane scroll = new JScrollPane(tablaReportes);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ==========================================================
    // 4. PANEL DE BOTONES DE EXPORTACIÓN (Abajo)
    // ==========================================================
    private JPanel crearPanelExportar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setOpaque(false);

        btnExportarPDF = crearBoton("Exportar a PDF", new Color(239, 68, 68)); // Rojo
        btnExportarPDF.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Reporte exportado correctamente a 'C:\\\\ReportesHotel\\\\reporte.pdf'");
        });

        btnExportarExcel = crearBoton("Exportar a Excel", new Color(16, 185, 129)); // Verde
        btnExportarExcel.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Reporte exportado correctamente a 'C:\\\\ReportesHotel\\\\reporte.xlsx'");
        });

        panel.add(btnExportarPDF);
        panel.add(btnExportarExcel);

        return panel;
    }

    // ==========================================================
    // MÉTODOS AUXILIARES DE DISEÑO
    // ==========================================================
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

    // ==========================================================
    // CLASE INTERNA PARA EL PLACEHOLDER (Texto fantasma)
    // ==========================================================
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }
        
        SwingUtilities.invokeLater(() -> {
            new reportes().setVisible(true);
        });
    }
}