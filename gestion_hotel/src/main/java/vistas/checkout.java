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
import java.io.File;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class checkout extends JFrame {

    private JTextField txtBusquedaHabitacion;
    private JTextField txtCliente;
    private JTextField txtFechaIngreso;
    private JTextField txtFechaSalida;
    
    private JTable tablaCuenta;
    private DefaultTableModel modeloCuenta;
    private JLabel lblTotal;
    
    private JButton btnBuscar, btnProcesarSalida, btnImprimir;

    public checkout() {
        setTitle("Hotel Mapocho - Procesar Salida (Check-Out)");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 245, 249)); 
        
        mainPanel.add(crearCabecera(), BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(25, 30, 30, 30));
        
        contentPanel.add(crearPanelDatosHuesped());
        contentPanel.add(crearPanelEstadoCuenta());
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    // Header
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

        JLabel lblTitulo = new JLabel("Check-Out / Facturación");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        panelIzquierda.add(btnVolver);
        panelIzquierda.add(lblTitulo);
        header.add(panelIzquierda, BorderLayout.WEST);

        return header;
    }

    private JPanel crearPanelDatosHuesped() {
        JPanel cardPanel = crearTarjetaBase();
        
        JLabel lblTituloCard = new JLabel("Datos de la Estadía");
        lblTituloCard.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloCard.setForeground(new Color(30, 41, 59));

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(59, 130, 246)),
                new EmptyBorder(0, 0, 5, 0)
        ));
        
        txtBusquedaHabitacion = new JTextField();
        txtBusquedaHabitacion.setBorder(null);
        txtBusquedaHabitacion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtBusquedaHabitacion.setOpaque(false);
        TextPrompt placeholder = new TextPrompt("N° Habitación (Ej. 102)...", txtBusquedaHabitacion);
        placeholder.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        
        btnBuscar = crearBoton("Buscar", new Color(59, 130, 246));
        
        searchPanel.add(txtBusquedaHabitacion, BorderLayout.CENTER);
        searchPanel.add(btnBuscar, BorderLayout.EAST);

        JPanel formPanel = new JPanel(new GridLayout(3, 1, 0, 25));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        
        txtCliente = crearTextFieldMaterial("Nombre del Huésped");
        txtCliente.setEditable(false);
        
        txtFechaIngreso = crearTextFieldMaterial("Fecha de Ingreso");
        txtFechaIngreso.setEditable(false);
        
        txtFechaSalida = crearTextFieldMaterial("Fecha de Salida");
        txtFechaSalida.setEditable(false);
        
        formPanel.add(txtCliente);
        formPanel.add(txtFechaIngreso);
        formPanel.add(txtFechaSalida);
        
        btnBuscar.addActionListener(e -> {
            String habStr = txtBusquedaHabitacion.getText().trim();
            if (habStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un número de habitación.");
                return;
            }
            
            try {
                int numHabitacion = Integer.parseInt(habStr);
                HabitacionArchivo ha = new HabitacionArchivo();
                Habitacion h = ha.buscar(String.valueOf(numHabitacion));
                
                if (h == null || !h.getEstado().equals("OCUPADA")) {
                    JOptionPane.showMessageDialog(this, "La habitación " + numHabitacion + " no está ocupada.");
                    return;
                }
                
                ReservaArchivo ra = new ReservaArchivo();
                Reserva reserva = null;
                for (Reserva r : ra.listar()) {
                    if (r.getNumeroHabitacion() == numHabitacion) {
                        reserva = r;
                        break;
                    }
                }
                
                if (reserva != null) {
                    ClienteArchivo ca = new ClienteArchivo();
                    Cliente c = ca.buscar(String.valueOf(reserva.getIdCliente()));
                    if (c != null) {
                        txtCliente.setText(c.getNombres() + " " + c.getApellidos());
                    } else {
                        txtCliente.setText("Cliente ID: " + reserva.getIdCliente());
                    }
                    txtFechaIngreso.setText(reserva.getFechaIngreso());
                    
                    java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm a");
                    txtFechaSalida.setText(java.time.LocalDateTime.now().format(dtf));
                } else {
                    txtCliente.setText("Huésped Directo");
                    txtFechaIngreso.setText("Desconocida");
                    
                    java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm a");
                    txtFechaSalida.setText(java.time.LocalDateTime.now().format(dtf));
                }
                
                modeloCuenta.setRowCount(0);
                double total = 0.0;
                
                ConsumoArchivo consArch = new ConsumoArchivo();
                SnackArchivo sa = new SnackArchivo();
                boolean hasEstadia = false;
                
                for (Consumo cons : consArch.listarPorHabitacion(numHabitacion)) {
                    if (cons.getCodigoSnack().equals("ESTADIA")) {
                        modeloCuenta.addRow(new Object[]{"Estadía - " + h.getTipo(), cons.getCantidad() + " días", String.format("%.2f", cons.getSubtotal())});
                        hasEstadia = true;
                    } else if (cons.getCodigoSnack().equals("MEDIODIA")) {
                        modeloCuenta.addRow(new Object[]{"Estadía (Medio Día) - " + h.getTipo(), "1", String.format("%.2f", cons.getSubtotal())});
                        hasEstadia = true;
                    } else {
                        modelo.Snack s = sa.buscar(cons.getCodigoSnack());
                        String nombreSnack = (s != null) ? s.getNombre() : "Consumo " + cons.getCodigoSnack();
                        modeloCuenta.addRow(new Object[]{nombreSnack, cons.getCantidad(), String.format("%.2f", cons.getSubtotal())});
                    }
                    total += cons.getSubtotal();
                }
                
                if (!hasEstadia) {
                    modeloCuenta.addRow(new Object[]{"Estadía - " + h.getTipo(), "1", String.format("%.2f", h.getPrecio())});
                    total += h.getPrecio();
                }
                
                lblTotal.setText(String.format("TOTAL A PAGAR: S/ %.2f", total));
                lblTotal.putClientProperty("totalValue", total);
                
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Número de habitación inválido.");
            }
        });

        cardPanel.add(lblTituloCard);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(searchPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(formPanel);
        cardPanel.add(Box.createVerticalGlue());

        return cardPanel;
    }

    private JPanel crearPanelEstadoCuenta() {
        JPanel cardPanel = crearTarjetaBase();
        
        JLabel lblTituloCard = new JLabel("Estado de Cuenta");
        lblTituloCard.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloCard.setForeground(new Color(30, 41, 59));

        String[] columnas = {"Descripción", "Cant.", "Subtotal (S/)"};
        modeloCuenta = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaCuenta = new JTable(modeloCuenta);
        tablaCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCuenta.setRowHeight(35); 
        tablaCuenta.setShowGrid(false); 
        tablaCuenta.setIntercellSpacing(new Dimension(0, 0));
        tablaCuenta.setSelectionBackground(Color.WHITE);
        tablaCuenta.setSelectionForeground(Color.BLACK);
        
        JTableHeader theader = tablaCuenta.getTableHeader();
        theader.setBackground(Color.WHITE);
        theader.setForeground(new Color(100, 116, 139));
        theader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        theader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        ((DefaultTableCellRenderer)theader.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        JScrollPane scrollCuenta = new JScrollPane(tablaCuenta);
        scrollCuenta.getViewport().setBackground(Color.WHITE);
        scrollCuenta.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.setOpaque(false);
        lblTotal = new JLabel("TOTAL A PAGAR: S/ 0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(new Color(239, 68, 68)); // Rojo para deuda
        panelTotal.add(lblTotal);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setOpaque(false);
        
        btnImprimir = crearBoton("Imprimir Recibo", new Color(100, 116, 139)); // Gris
        btnImprimir.addActionListener(e -> {
            String hab = txtBusquedaHabitacion.getText().trim();
            if(hab.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Primero busque una habitación.");
                return;
            }
            if(modeloCuenta.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No hay estado de cuenta generado.");
                return;
            }
            
            try {
                String folderPath = "C:\\Users\\rojas\\OneDrive\\Documentos\\ReporteHotel";
                File folder = new File(folderPath);
                if(!folder.exists()) folder.mkdirs();
                
                String path = folderPath + "\\Recibo_Hab_" + hab + ".pdf";
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(path));
                document.open();
                
                com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Paragraph titulo = new Paragraph("Hotel Paraiso - Recibo de Check-Out\n\n", titleFont);
                titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(titulo);
                
                com.itextpdf.text.Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                document.add(new Paragraph("Habitacion: " + hab, textFont));
                document.add(new Paragraph("Huesped: " + txtCliente.getText(), textFont));
                document.add(new Paragraph("Fecha Ingreso: " + txtFechaIngreso.getText(), textFont));
                document.add(new Paragraph("Fecha Salida: " + txtFechaSalida.getText() + "\n\n", textFont));
                
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                
                String[] headers = {"Descripcion", "Cant.", "Subtotal (S/)"};
                for(String h : headers) {
                    PdfPCell c = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                    c.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    c.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                    table.addCell(c);
                }
                
                for(int i=0; i<modeloCuenta.getRowCount(); i++) {
                    table.addCell(modeloCuenta.getValueAt(i, 0).toString());
                    table.addCell(modeloCuenta.getValueAt(i, 1).toString());
                    table.addCell(modeloCuenta.getValueAt(i, 2).toString());
                }
                document.add(table);
                
                com.itextpdf.text.Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, com.itextpdf.text.BaseColor.RED);
                Paragraph total = new Paragraph("\n" + lblTotal.getText(), totalFont);
                total.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(total);
                
                document.close();
                JOptionPane.showMessageDialog(this, "Recibo generado y guardado en:\n" + path);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage());
            }
        });
        
        btnProcesarSalida = crearBoton("Procesar Salida", new Color(16, 185, 129));
        
        btnProcesarSalida.addActionListener(e -> {
            if (modeloCuenta.getRowCount() == 0 || txtBusquedaHabitacion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Primero busque una habitación válida.");
                return;
            }
            
            try {
                int numHabitacion = Integer.parseInt(txtBusquedaHabitacion.getText().trim());
                
                double totalPagado = 0.0;
                Object val = lblTotal.getClientProperty("totalValue");
                if (val != null) {
                    totalPagado = (Double) val;
                }
                
                PagoArchivo pa = new PagoArchivo();
                Pago p = new Pago();
                p.setIdPago("PAG-" + System.currentTimeMillis());
                p.setCodigoReserva("HAB-" + numHabitacion);
                p.setMontoTotal(totalPagado);
                p.setMetodoPago("Efectivo/Tarjeta");
                pa.registrar(p);
                
                HabitacionArchivo ha = new HabitacionArchivo();
                Habitacion h = ha.buscar(String.valueOf(numHabitacion));
                if (h != null) {
                    h.setEstado("LIBRE");
                    ha.actualizar(h);
                }
                
                ReservaArchivo ra = new ReservaArchivo();
                for (Reserva r : ra.listar()) {
                    if (r.getNumeroHabitacion() == numHabitacion) {
                        ra.eliminar(r.getCodigoReserva());
                    }
                }
                
                ConsumoArchivo consArch = new ConsumoArchivo();
                for (Consumo cons : consArch.listarPorHabitacion(numHabitacion)) {
                    consArch.eliminar(String.valueOf(cons.getIdConsumo()));
                }

                JOptionPane.showMessageDialog(this, "¡Check-Out procesado! Pago registrado y habitación libre nuevamente.");
                
                new menu().setVisible(true);
                this.dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al procesar salida.");
            }
        });
        
        panelBotones.add(btnImprimir);
        panelBotones.add(btnProcesarSalida);

        cardPanel.add(lblTituloCard);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(scrollCuenta);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(panelTotal);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(panelBotones);

        return cardPanel;
    }

    private JPanel crearTarjetaBase() {
        JPanel panel = new JPanel() {
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
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        return panel;
    }

    private JTextField crearTextFieldMaterial(String placeholderTexto) {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.BOLD, 15));
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

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorFondo);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
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