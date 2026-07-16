package modelo;

public class Consumo {
    private int idConsumo;
    private int numeroHabitacion;
    private String codigoSnack;
    private int cantidad;
    private double subtotal;

    public Consumo() {
    }

    public Consumo(int idConsumo, int numeroHabitacion, String codigoSnack, int cantidad, double subtotal) {
        this.idConsumo = idConsumo;
        this.numeroHabitacion = numeroHabitacion;
        this.codigoSnack = codigoSnack;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getIdConsumo() { return idConsumo; }
    public void setIdConsumo(int idConsumo) { this.idConsumo = idConsumo; }

    public int getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(int numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

    public String getCodigoSnack() { return codigoSnack; }
    public void setCodigoSnack(String codigoSnack) { this.codigoSnack = codigoSnack; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
