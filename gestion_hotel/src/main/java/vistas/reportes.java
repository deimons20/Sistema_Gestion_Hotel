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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class reportes extends JFrame {

    private JComboBox<String> cmbTipoReporte;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;
    
    private JButton btnGenerar, btnExportarPDF, btnExportarExcel;

    public reportes() {
        setTitle("Hotel Mapocho - Reportes y Estadísticas");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 245, 249));
        
        mainPanel.add(crearCabecera(), BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        contentPanel.add(crearPanelFiltros(), BorderLayout.NORTH);
        contentPanel.add(crearPanelTabla(), BorderLayout.CENTER);
        contentPanel.add(crearPanelExportar(), BorderLayout.SOUTH);
        
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

        JLabel lblTitulo = new JLabel("Módulo de Reportes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        panelIzquierda.add(btnVolver);
        panelIzquierda.add(lblTitulo);
        header.add(panelIzquierda, BorderLayout.WEST);

        return header;
    }

    //  Panel de filtros
    private JPanel crearPanelFiltros() {
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

        String[] opciones = {
            "Seleccionar...", 
            "Ingresos del Hotel", 
            "Habitaciones Ocupadas", 
            "Habitaciones Disponibles", 
            "Reservas"
        };
        cmbTipoReporte = new JComboBox<>(opciones);
        cmbTipoReporte.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTipoReporte.setPreferredSize(new Dimension(220, 35));
        cmbTipoReporte.setBackground(Color.WHITE);
        cmbTipoReporte.addActionListener(e -> {
            if (btnGenerar != null) btnGenerar.doClick();
        });

        txtFechaInicio = crearTextFieldMaterial("Fecha Inicio (DD/MM/AAAA)");
        txtFechaInicio.setPreferredSize(new Dimension(180, 35));
        
        txtFechaFin = crearTextFieldMaterial("Fecha Fin (DD/MM/AAAA)");
        txtFechaFin.setPreferredSize(new Dimension(180, 35));

        btnGenerar = crearBoton("Generar Reporte", new Color(59, 130, 246));
        
        btnGenerar.addActionListener(e -> {
            int opcion = cmbTipoReporte.getSelectedIndex();
            if (opcion == 0) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un tipo de reporte.");
                return;
            }
            
            modeloTabla.setRowCount(0);
            
            try {
                switch(opcion) {
                    case 1:
                        modeloTabla.setColumnIdentifiers(new String[]{"ID Pago", "Total", "Método Pago"});
                        for (Pago p : new PagoArchivo().listar()) {
                            modeloTabla.addRow(new Object[]{p.getIdPago(), "S/ " + p.getMontoTotal(), p.getMetodoPago()});
                        }
                        break;
                    case 2:
                        modeloTabla.setColumnIdentifiers(new String[]{"Nº", "Tipo", "Precio", "Estado"});
                        for (Habitacion h : new HabitacionArchivo().listar()) {
                            if (h.getEstado().equals("OCUPADA")) {
                                modeloTabla.addRow(new Object[]{h.getNumeroHabitacion(), h.getTipo(), "S/ " + h.getPrecio(), h.getEstado()});
                            }
                        }
                        break;
                    case 3:
                        modeloTabla.setColumnIdentifiers(new String[]{"Nº", "Tipo", "Precio", "Estado"});
                        for (Habitacion h : new HabitacionArchivo().listar()) {
                            if (h.getEstado().equals("LIBRE")) {
                                modeloTabla.addRow(new Object[]{h.getNumeroHabitacion(), h.getTipo(), "S/ " + h.getPrecio(), h.getEstado()});
                            }
                        }
                        break;
                    case 4:
                        modeloTabla.setColumnIdentifiers(new String[]{"Cód. Reserva", "Cliente DNI", "Habitación", "Ingreso", "Salida"});
                        for (Reserva r : new ReservaArchivo().listar()) {
                            modeloTabla.addRow(new Object[]{r.getCodigoReserva(), r.getIdCliente(), r.getNumeroHabitacion(), r.getFechaIngreso(), r.getFechaSalida()});
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

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

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

    private JPanel crearPanelExportar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setOpaque(false);

        btnExportarPDF = crearBoton("Exportar a PDF", new Color(239, 68, 68));
        btnExportarPDF.addActionListener(e -> {
            if(modeloTabla.getRowCount() == 0 || cmbTipoReporte.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "No hay datos para exportar.");
                return;
            }
            try {
                String folderPath = "C:\\Users\\rojas\\OneDrive\\Documentos\\ReporteHotel";
                File folder = new File(folderPath);
                if(!folder.exists()) folder.mkdirs();
                
                String tipo = cmbTipoReporte.getSelectedItem().toString().replace(" ", "_");
                String path = folderPath + "\\Reporte_" + tipo + ".pdf";
                
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(path));
                document.open();
                
                com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Paragraph titulo = new Paragraph("Reporte: " + cmbTipoReporte.getSelectedItem().toString() + "\n\n", titleFont);
                titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(titulo);
                
                int numCols = modeloTabla.getColumnCount();
                PdfPTable table = new PdfPTable(numCols);
                table.setWidthPercentage(100);
                
                for(int i=0; i<numCols; i++) {
                    PdfPCell c = new PdfPCell(new Phrase(tablaReportes.getColumnName(i), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                    c.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
                    table.addCell(c);
                }
                
                for(int i=0; i<modeloTabla.getRowCount(); i++) {
                    for(int j=0; j<numCols; j++) {
                        table.addCell(modeloTabla.getValueAt(i, j) != null ? modeloTabla.getValueAt(i, j).toString() : "");
                    }
                }
                document.add(table);
                document.close();
                JOptionPane.showMessageDialog(this, "Reporte PDF guardado en:\n" + path);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage());
            }
        });

        btnExportarExcel = crearBoton("Exportar a Excel", new Color(16, 185, 129));
        btnExportarExcel.addActionListener(e -> {
            if(modeloTabla.getRowCount() == 0 || cmbTipoReporte.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "No hay datos para exportar.");
                return;
            }
            try {
                String folderPath = "C:\\Users\\rojas\\OneDrive\\Documentos\\ReporteHotel";
                File folder = new File(folderPath);
                if(!folder.exists()) folder.mkdirs();
                
                String tipo = cmbTipoReporte.getSelectedItem().toString().replace(" ", "_");
                String path = folderPath + "\\Reporte_" + tipo + ".xlsx";
                
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Reporte");
                
                Row headerRow = sheet.createRow(0);
                for(int i=0; i<modeloTabla.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(tablaReportes.getColumnName(i));
                }
                
                for(int i=0; i<modeloTabla.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for(int j=0; j<modeloTabla.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object val = modeloTabla.getValueAt(i, j);
                        cell.setCellValue(val != null ? val.toString() : "");
                    }
                }
                
                for(int i=0; i<modeloTabla.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }
                
                FileOutputStream fileOut = new FileOutputStream(path);
                workbook.write(fileOut);
                fileOut.close();
                workbook.close();
                
                JOptionPane.showMessageDialog(this, "Reporte Excel guardado en:\n" + path);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar Excel: " + ex.getMessage());
            }
        });

        panel.add(btnExportarPDF);
        panel.add(btnExportarExcel);

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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }
        
        SwingUtilities.invokeLater(() -> {
            new reportes().setVisible(true);
        });
    }
}