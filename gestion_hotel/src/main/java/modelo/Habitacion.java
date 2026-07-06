package modelo;

public class Habitacion {
    private int numeroHabitacion;
    private String tipo;
    private String estado;
    private double precio;

    public Habitacion() {
    }

    public Habitacion(int numeroHabitacion, String tipo, String estado, double precio) {
        this.numeroHabitacion = numeroHabitacion;
        this.tipo = tipo;
        this.estado = estado;
        this.precio = precio;
    }

    // Getters y Setters
    public int getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(int numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}