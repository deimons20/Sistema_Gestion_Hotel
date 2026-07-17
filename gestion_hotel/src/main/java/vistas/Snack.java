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

public class Snack extends JFrame {

    private JTable tablaMenu;
    private DefaultTableModel modeloMenu;
    
    private JComboBox<String> cmbHabitaciones;
    private JTextField txtClienteAsignado;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JLabel lblTotal;
    
    private JButton btnAgregar, btnQuitar, btnRegistrar;

    public Snack() {
        setTitle("Hotel Mapocho - Punto de Venta (Snack Bar)");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 245, 249)); 
        
        mainPanel.add(crearCabecera(), BorderLayout.NORTH);
        
        // Contenedor dividido en 2 columnas
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(25, 30, 30, 30));
        
        contentPanel.add(crearPanelMenu());
        contentPanel.add(crearPanelCarrito());
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        // Datos del menú se cargarán de la base de datos
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

        JLabel lblTitulo = new JLabel("Snack Bar / Registro de Consumos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        panelIzquierda.add(btnVolver);
        panelIzquierda.add(lblTitulo);
        header.add(panelIzquierda, BorderLayout.WEST);

        return header;
    }

    // ==========================================================
    // 2. PANEL IZQUIERDO (MENÚ DE PRODUCTOS)
    // ==========================================================
    private JPanel crearPanelMenu() {
        JPanel cardPanel = crearTarjetaBase();
        
        JLabel lblTituloCard = new JLabel("Productos Disponibles");
        lblTituloCard.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloCard.setForeground(new Color(30, 41, 59));

        // Tabla del Menú
        String[] columnas = {"Cod.", "Producto", "Stock", "Precio (S/)"};
        modeloMenu = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaMenu = crearTablaCustom(modeloMenu);
        JScrollPane scrollMenu = new JScrollPane(tablaMenu);
        scrollMenu.getViewport().setBackground(Color.WHITE);
        scrollMenu.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        cargarDatosMenu(); // Llenar la tabla

        btnAgregar = crearBoton("Agregar al Carrito", new Color(59, 130, 246)); // Azul
        btnAgregar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnAgregar.addActionListener(e -> {
            int fila = tablaMenu.getSelectedRow();
            if (fila >= 0) {
                String stockStr = modeloMenu.getValueAt(fila, 2).toString();
                if (stockStr.equals("AGOTADO")) {
                    JOptionPane.showMessageDialog(this, "Este producto está agotado.");
                    return;
                }
                
                String cod = modeloMenu.getValueAt(fila, 0).toString();
                String prod = modeloMenu.getValueAt(fila, 1).toString();
                double precioUnitario = Double.parseDouble(modeloMenu.getValueAt(fila, 3).toString().replace(",", "."));
                
                boolean existe = false;
                for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
                    if (modeloCarrito.getValueAt(i, 0).toString().equals(cod)) {
                        int cantActual = Integer.parseInt(modeloCarrito.getValueAt(i, 2).toString());
                        int stockDisponible = Integer.parseInt(stockStr);
                        if (cantActual >= stockDisponible) {
                            JOptionPane.showMessageDialog(this, "No hay suficiente stock para añadir más.");
                            return;
                        }
                        cantActual++;
                        modeloCarrito.setValueAt(String.valueOf(cantActual), i, 2);
                        double nuevoSubtotal = cantActual * precioUnitario;
                        modeloCarrito.setValueAt(String.format("%.2f", nuevoSubtotal).replace(",", "."), i, 3);
                        existe = true;
                        break;
                    }
                }
                
                if (!existe) {
                    modeloCarrito.addRow(new Object[]{cod, prod, "1", String.format("%.2f", precioUnitario).replace(",", ".")});
                }
                
                calcularTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto del menú.");
            }
        });

        // --- Botones de Administración ---
        JPanel panelAdmin = new JPanel(new GridLayout(1, 3, 10, 0));
        panelAdmin.setOpaque(false);
        
        JButton btnNuevo = crearBoton("+ Nuevo", new Color(16, 185, 129)); 
        JButton btnEliminar = crearBoton("Eliminar", new Color(239, 68, 68)); 
        JButton btnEditar = crearBoton("Editar", new Color(245, 158, 11)); 
        
        btnNuevo.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del nuevo snack:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                String precioStr = JOptionPane.showInputDialog(this, "Precio (Ej: 2.50):");
                if (precioStr != null && !precioStr.trim().isEmpty()) {
                    String stockStr = JOptionPane.showInputDialog(this, "Stock Inicial:");
                    if (stockStr != null && !stockStr.trim().isEmpty()) {
                        try {
                            double precio = Double.parseDouble(precioStr.replace(",", "."));
                            int stock = Integer.parseInt(stockStr);
                            SnackArchivo sa = new SnackArchivo();
                            ArrayList<modelo.Snack> lista = sa.listar();
                            int nextId = lista.size() + 1;
                            String cod = "S" + String.format("%02d", nextId);
                            sa.registrar(new modelo.Snack(cod, nombre.trim(), stock, precio));
                            cargarDatosMenu();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Valores numéricos inválidos.");
                        }
                    }
                }
            }
        });
        
        btnEliminar.addActionListener(e -> {
            int fila = tablaMenu.getSelectedRow();
            if (fila >= 0) {
                String cod = modeloMenu.getValueAt(fila, 0).toString();
                if (JOptionPane.showConfirmDialog(this, "¿Eliminar producto " + cod + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    SnackArchivo sa = new SnackArchivo();
                    sa.eliminar(cod);
                    
                    // Re-indexar los codigos restantes para que mantengan el orden S01, S02...
                    ArrayList<modelo.Snack> listaRestante = sa.listar();
                    try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("archivos/snacks.txt"))) {
                        for (int i = 0; i < listaRestante.size(); i++) {
                            modelo.Snack s = listaRestante.get(i);
                            String nuevoCod = "S" + String.format("%02d", i + 1);
                            bw.write(nuevoCod + ";" + s.getNombre() + ";" + s.getStock() + ";" + s.getPrecio());
                            bw.newLine();
                        }
                    } catch (Exception ex) { }
                    
                    cargarDatosMenu();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
            }
        });
        
        btnEditar.addActionListener(e -> {
            int fila = tablaMenu.getSelectedRow();
            if (fila >= 0) {
                String cod = modeloMenu.getValueAt(fila, 0).toString();
                SnackArchivo sa = new SnackArchivo();
                modelo.Snack s = sa.buscar(cod);
                if (s != null) {
                    String nuevoNombre = JOptionPane.showInputDialog(this, "Nombre del producto:", s.getNombre());
                    if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                        String nuevoPrecio = JOptionPane.showInputDialog(this, "Precio:", s.getPrecio());
                        if (nuevoPrecio != null && !nuevoPrecio.trim().isEmpty()) {
                            String nuevoStock = JOptionPane.showInputDialog(this, "Stock actual:", s.getStock());
                            if (nuevoStock != null && !nuevoStock.trim().isEmpty()) {
                                try {
                                    double precio = Double.parseDouble(nuevoPrecio.replace(",", "."));
                                    int stock = Integer.parseInt(nuevoStock);
                                    s.setNombre(nuevoNombre.trim());
                                    s.setPrecio(precio);
                                    s.setStock(stock);
                                    sa.actualizar(s);
                                    cargarDatosMenu();
                                    JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(this, "Valores numéricos inválidos.");
                                }
                            }
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para editar.");
            }
        });
        
        panelAdmin.add(btnNuevo);
        panelAdmin.add(btnEditar);
        panelAdmin.add(btnEliminar);

        cardPanel.add(lblTituloCard);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(scrollMenu);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(panelAdmin);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(btnAgregar);

        return cardPanel;
    }
    
    private void cargarDatosMenu() {
        modeloMenu.setRowCount(0);
        SnackArchivo sa = new SnackArchivo();
        ArrayList<modelo.Snack> listaSnacks = sa.listar();
        if (listaSnacks.isEmpty()) {
            sa.registrar(new modelo.Snack("S01", "Agua Mineral", 50, 2.50));
            sa.registrar(new modelo.Snack("S02", "Gaseosa Cola", 40, 3.50));
            sa.registrar(new modelo.Snack("S03", "Papas Fritas", 30, 4.00));
            sa.registrar(new modelo.Snack("S04", "Galletas", 25, 2.00));
            sa.registrar(new modelo.Snack("S05", "Chocolate", 20, 5.00));
            listaSnacks = sa.listar();
        }
        for (modelo.Snack s : listaSnacks) {
            String stockDisplay = s.getStock() <= 0 ? "AGOTADO" : String.valueOf(s.getStock());
            modeloMenu.addRow(new Object[]{s.getCodigo(), s.getNombre(), stockDisplay, String.format("%.2f", s.getPrecio())});
        }
    }

    // ==========================================================
    // 3. PANEL DERECHO (CARRITO Y CUENTA)
    // ==========================================================
    private JPanel crearPanelCarrito() {
        JPanel cardPanel = crearTarjetaBase();
        
        // --- Selector de Habitación ---
        JPanel panelDestino = new JPanel(new GridLayout(2, 1, 0, 10));
        panelDestino.setOpaque(false);
        panelDestino.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // En tu lógica real, este combo box solo mostrará habitaciones Ocupadas
        cmbHabitaciones = new JComboBox<>();
        cmbHabitaciones.addItem("Seleccione Habitación...");
        
        HabitacionArchivo ha = new HabitacionArchivo();
        for (Habitacion h : ha.listar()) {
            if (h.getEstado().equals("OCUPADA")) {
                cmbHabitaciones.addItem("Habitación " + h.getNumeroHabitacion());
            }
        }
        
        cmbHabitaciones.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cmbHabitaciones.setBackground(Color.WHITE);
        
        txtClienteAsignado = new JTextField();
        txtClienteAsignado.setEditable(false);
        txtClienteAsignado.setText("Huésped: (Seleccione habitación)");
        txtClienteAsignado.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        txtClienteAsignado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(203, 213, 225)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        
        cmbHabitaciones.addActionListener(e -> {
            if (cmbHabitaciones.getSelectedIndex() > 0) {
                String habSeleccionada = cmbHabitaciones.getSelectedItem().toString();
                String numStr = habSeleccionada.replace("Habitación ", "").trim();
                
                // Buscar cliente en reservas
                ReservaArchivo ra = new ReservaArchivo();
                ClienteArchivo ca = new ClienteArchivo();
                boolean found = false;
                for (Reserva r : ra.listar()) {
                    if (r.getNumeroHabitacion() == Integer.parseInt(numStr)) {
                        Cliente c = ca.buscar(String.valueOf(r.getIdCliente()));
                        if (c != null) {
                            txtClienteAsignado.setText("Huésped: " + c.getNombres() + " " + c.getApellidos());
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) txtClienteAsignado.setText("Huésped: (Sin datos de reserva)");
                
            } else {
                txtClienteAsignado.setText("Huésped: (Seleccione habitación)");
            }
        });

        panelDestino.add(cmbHabitaciones);
        panelDestino.add(txtClienteAsignado);

        // --- Tabla del Carrito ---
        String[] colCarrito = {"Cod.", "Producto", "Cant.", "Subtotal"};
        modeloCarrito = new DefaultTableModel(colCarrito, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCarrito = crearTablaCustom(modeloCarrito);
        JScrollPane scrollCarrito = new JScrollPane(tablaCarrito);
        scrollCarrito.getViewport().setBackground(Color.WHITE);
        scrollCarrito.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        
        btnQuitar = crearBoton("Quitar Producto", new Color(239, 68, 68)); // Rojo
        btnQuitar.addActionListener(e -> {
            int fila = tablaCarrito.getSelectedRow();
            if (fila >= 0) {
                modeloCarrito.removeRow(fila);
                calcularTotal();
            }
        });

        // --- Panel Inferior (Totales y Botón Guardar) ---
        JPanel panelTotal = new JPanel(new BorderLayout());
        panelTotal.setOpaque(false);
        
        lblTotal = new JLabel("TOTAL: S/ 0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(new Color(15, 23, 42));
        
        btnRegistrar = crearBoton("Cargar a la Cuenta", new Color(249, 115, 22)); // Naranja Snack
        
        btnRegistrar.addActionListener(e -> {
            if (cmbHabitaciones.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una habitación.");
                return;
            }
            if (modeloCarrito.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "El carrito está vacío.");
                return;
            }
            
            String habStr = cmbHabitaciones.getSelectedItem().toString().replace("Habitación ", "").trim();
            int numHabitacion = Integer.parseInt(habStr);
            
            ConsumoArchivo ca = new ConsumoArchivo();
            SnackArchivo snackArch = new SnackArchivo();
            for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
                Consumo c = new Consumo();
                c.setIdConsumo((int)(System.currentTimeMillis() % Integer.MAX_VALUE) + i); // ID Aleatorio simple
                c.setNumeroHabitacion(numHabitacion);
                String codSnack = modeloCarrito.getValueAt(i, 0).toString().replace(",", ".");
                int cantidad = Integer.parseInt(modeloCarrito.getValueAt(i, 2).toString());
                
                c.setCodigoSnack(codSnack);
                c.setCantidad(cantidad);
                c.setSubtotal(Double.parseDouble(modeloCarrito.getValueAt(i, 3).toString().replace(",", ".")));
                ca.registrar(c);
                
                // Restar stock
                modelo.Snack s = snackArch.buscar(codSnack);
                if (s != null) {
                    s.setStock(Math.max(0, s.getStock() - cantidad));
                    snackArch.actualizar(s);
                }
            }
            
            JOptionPane.showMessageDialog(this, "Consumo cargado a la Habitación " + numHabitacion);
            modeloCarrito.setRowCount(0);
            cargarDatosMenu();
            calcularTotal();
            cmbHabitaciones.setSelectedIndex(0);
        });
        
        JPanel panelBotonesDerecha = new JPanel(new GridLayout(2, 1, 0, 10));
        panelBotonesDerecha.setOpaque(false);
        panelBotonesDerecha.add(btnQuitar);
        panelBotonesDerecha.add(btnRegistrar);
        
        panelTotal.add(lblTotal, BorderLayout.WEST);
        panelTotal.add(panelBotonesDerecha, BorderLayout.EAST);

        // Ensamblar Tarjeta Derecha
        cardPanel.add(panelDestino);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(scrollCarrito);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(panelTotal);

        return cardPanel;
    }

    // ==========================================================
    // MÉTODOS AUXILIARES
    // ==========================================================
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
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        return panel;
    }

    private JTable crearTablaCustom(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(35); 
        tabla.setShowGrid(false); 
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionBackground(new Color(239, 246, 255)); 
        tabla.setSelectionForeground(new Color(30, 58, 138));
        
        JTableHeader theader = tabla.getTableHeader();
        theader.setBackground(Color.WHITE);
        theader.setForeground(new Color(100, 116, 139));
        theader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        theader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        ((DefaultTableCellRenderer)theader.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        
        return tabla;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Calcula el total del carrito dinámicamente
    private void calcularTotal() {
        double total = 0.0;
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            total += Double.parseDouble(modeloCarrito.getValueAt(i, 3).toString().replace(",", "."));
        }
        lblTotal.setText(String.format("TOTAL: S/ %.2f", total));
    }


}