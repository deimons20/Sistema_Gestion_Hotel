package vistas;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import persistencia.ClienteArchivo;
import modelo.Cliente;
import java.util.ArrayList;

public class clientes extends JFrame {

    // Componentes del formulario
    private JTextField txtBusqueda;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    
    private JTextField txtDNI;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtTelefono;
    
    private JButton btnNuevo, btnGuardar, btnEliminar;
    
    private ClienteArchivo clienteArchivo = new ClienteArchivo();
    private Cliente clienteSeleccionado = null;

    public clientes() {
        setTitle("Hotel Mapocho - Gestión de Clientes");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Fondo principal gris muy claro
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 245, 249)); 
        
        mainPanel.add(crearCabecera(), BorderLayout.NORTH);
        
        // Contenedor central dividido
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        
        contentPanel.add(crearPanelDirectorio(), BorderLayout.WEST);
        contentPanel.add(crearPanelPerfil(), BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        cargarDatosTabla();
    }
    
    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        ArrayList<Cliente> lista = clienteArchivo.listar();
        for (Cliente c : lista) {
            modeloTabla.addRow(new Object[]{c.getDni(), c.getNombres(), c.getApellidos()});
        }
    }

    // ==========================================================
    // 1. CABECERA
    // ==========================================================
    private JPanel crearCabecera() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 41, 59)); // Azul oscuro
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

        JLabel lblTitulo = new JLabel("Directorio de Huéspedes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        panelIzquierda.add(btnVolver);
        panelIzquierda.add(lblTitulo);
        header.add(panelIzquierda, BorderLayout.WEST);

        return header;
    }

    // ==========================================================
    // 2. PANEL IZQUIERDO (LISTA DE CLIENTES)
    // ==========================================================
    private JPanel crearPanelDirectorio() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(450, 0));

        // Buscador
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel lblIconBuscar = new JLabel("🔍");
        txtBusqueda = new JTextField();
        txtBusqueda.setBorder(null); // Quitar borde para que se vea limpio
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        TextPrompt placeholder = new TextPrompt("Buscar por DNI o Nombre...", txtBusqueda);
        placeholder.setForeground(new Color(148, 163, 184));
        
        searchPanel.add(lblIconBuscar, BorderLayout.WEST);
        searchPanel.add(txtBusqueda, BorderLayout.CENTER);

        // Tabla personalizada
        String[] columnas = {"DNI", "Nombres", "Apellidos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // No editable
        };
        
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaClientes.setRowHeight(35); // Filas altas y cómodas
        tablaClientes.setShowGrid(false); // Quitar cuadrícula fea
        tablaClientes.setIntercellSpacing(new Dimension(0, 0));
        tablaClientes.setSelectionBackground(new Color(239, 246, 255)); // Azul clarito al seleccionar
        tablaClientes.setSelectionForeground(new Color(30, 58, 138));
        
        // Estilo de la cabecera de la tabla
        JTableHeader theader = tablaClientes.getTableHeader();
        theader.setBackground(new Color(248, 250, 252));
        theader.setForeground(new Color(100, 116, 139));
        theader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        theader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        ((DefaultTableCellRenderer)theader.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Evento al hacer clic en una fila de la tabla
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaClientes.getSelectedRow();
                if (fila >= 0) {
                    String dniStr = modeloTabla.getValueAt(fila, 0).toString();
                    clienteSeleccionado = clienteArchivo.buscar(dniStr);
                    if (clienteSeleccionado != null) {
                        txtDNI.setText(clienteSeleccionado.getDni());
                        txtNombres.setText(clienteSeleccionado.getNombres());
                        txtApellidos.setText(clienteSeleccionado.getApellidos());
                        txtTelefono.setText(clienteSeleccionado.getTelefono());
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tablaClientes);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ==========================================================
    // 3. PANEL DERECHO (TARJETA DE PERFIL)
    // ==========================================================
    private JPanel crearPanelPerfil() {
        // Panel con bordes redondeados (Card)
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
        cardPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- AVATAR CIRCULAR GENERADO POR CÓDIGO ---
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = 90;
                int x = (getWidth() - size) / 2;
                
                // Círculo de fondo
                g2.setColor(new Color(224, 231, 255)); // Azul muy suave
                g2.fill(new Ellipse2D.Double(x, 0, size, size));
                
                // Texto (Inicial del usuario o un icono)
                g2.setColor(new Color(79, 70, 229)); // Azul índigo
                g2.setFont(new Font("Segoe UI Emoji", Font.BOLD, 40));
                FontMetrics fm = g2.getFontMetrics();
                String texto = "👤";
                int textX = x + (size - fm.stringWidth(texto)) / 2;
                int textY = (size - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(texto, textX, textY);
                
                g2.dispose();
            }
        };
        avatarPanel.setOpaque(false);
        avatarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JLabel lblTituloCard = new JLabel("Perfil del Cliente");
        lblTituloCard.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloCard.setForeground(new Color(30, 41, 59));
        lblTituloCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- CAMPOS DE TEXTO (Estilo Material Design) ---
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        formPanel.setOpaque(false);
        
        txtDNI = crearTextFieldMaterial("Número de DNI");
        txtNombres = crearTextFieldMaterial("Nombres Completos");
        txtApellidos = crearTextFieldMaterial("Apellidos");
        txtTelefono = crearTextFieldMaterial("Teléfono / Celular");
        
        aplicarFiltroNumerico(txtDNI, 8);
        aplicarFiltroNumerico(txtTelefono, 9);
        
        formPanel.add(txtDNI);
        formPanel.add(txtNombres);
        formPanel.add(txtApellidos);
        formPanel.add(txtTelefono);

        // --- BOTONES DE ACCIÓN ---
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botonesPanel.setOpaque(false);
        
        btnNuevo = crearBoton("Nuevo", new Color(100, 116, 139)); // Gris
        btnGuardar = crearBoton("Guardar Datos", new Color(16, 185, 129)); // Verde
        btnEliminar = crearBoton("Eliminar", new Color(239, 68, 68)); // Rojo
        
        // Acciones básicas de UI
        btnNuevo.addActionListener(e -> limpiarFormulario());
        
        btnGuardar.addActionListener(e -> {
            String dni = txtDNI.getText();
            String nombres = txtNombres.getText();
            String apellidos = txtApellidos.getText();
            String telefono = txtTelefono.getText();
            
            if(dni.isEmpty() || nombres.isEmpty()) {
                JOptionPane.showMessageDialog(null, "DNI y Nombres son obligatorios.");
                return;
            }
            
            if(dni.length() != 8) {
                JOptionPane.showMessageDialog(null, "El DNI debe tener exactamente 8 dígitos.");
                return;
            }
            
            if(!telefono.isEmpty() && telefono.length() != 9) {
                JOptionPane.showMessageDialog(null, "El teléfono debe tener exactamente 9 dígitos.");
                return;
            }
            
            if (clienteSeleccionado == null) {
                // Nuevo cliente
                ArrayList<Cliente> lista = clienteArchivo.listar();
                int nuevoId = lista.isEmpty() ? 1 : lista.get(lista.size() - 1).getIdCliente() + 1;
                Cliente nuevo = new Cliente(nuevoId, dni, nombres, apellidos, telefono);
                if (clienteArchivo.registrar(nuevo)) {
                    JOptionPane.showMessageDialog(null, "Cliente guardado exitosamente.");
                    limpiarFormulario();
                    cargarDatosTabla();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al guardar el cliente.");
                }
            } else {
                // Actualizar
                clienteSeleccionado.setDni(dni);
                clienteSeleccionado.setNombres(nombres);
                clienteSeleccionado.setApellidos(apellidos);
                clienteSeleccionado.setTelefono(telefono);
                if (clienteArchivo.actualizar(clienteSeleccionado)) {
                    JOptionPane.showMessageDialog(null, "Cliente actualizado exitosamente.");
                    limpiarFormulario();
                    cargarDatosTabla();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el cliente.");
                }
            }
        });
        
        btnEliminar.addActionListener(e -> {
            if (clienteSeleccionado != null) {
                int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (clienteArchivo.eliminar(String.valueOf(clienteSeleccionado.getIdCliente()))) {
                        JOptionPane.showMessageDialog(null, "Cliente eliminado.");
                        limpiarFormulario();
                        cargarDatosTabla();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un cliente de la lista.");
            }
        });
        
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnEliminar);

        // Ensamblar Tarjeta
        // cardPanel.add(avatarPanel); // Se removió el avatar a petición
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(lblTituloCard);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(formPanel);
        cardPanel.add(Box.createVerticalGlue());
        cardPanel.add(botonesPanel);

        return cardPanel;
    }

    // Método para crear Cajas de Texto estilo línea inferior (Material Design)
    private JTextField crearTextFieldMaterial(String placeholderTexto) {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setForeground(new Color(15, 23, 42));
        // Borde inferior únicamente
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(203, 213, 225)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        
        TextPrompt placeholder = new TextPrompt(placeholderTexto, txt);
        placeholder.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        placeholder.setForeground(new Color(148, 163, 184));
        
        return txt;
    }

    // Método para botones unificados
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

    private void limpiarFormulario() {
        txtDNI.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        tablaClientes.clearSelection();
        clienteSeleccionado = null;
    }

    private void aplicarFiltroNumerico(JTextField txt, int limite) {
        ((javax.swing.text.AbstractDocument) txt.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string == null) return;
                if ((fb.getDocument().getLength() + string.length()) <= limite && string.matches("\\d+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                if (text == null) return;
                if ((fb.getDocument().getLength() + text.length() - length) <= limite && text.matches("\\d+")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
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
            new clientes().setVisible(true);
        });
    }
}