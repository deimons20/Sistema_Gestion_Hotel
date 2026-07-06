package modelo;

public class Cliente extends Persona {
    private int idCliente;

    public Cliente() {
        super();
    }

    public Cliente(int idCliente, String dni, String nombres, String apellidos, String telefono) {
        super(dni, nombres, apellidos, telefono);
        this.idCliente = idCliente;
    }

    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
}