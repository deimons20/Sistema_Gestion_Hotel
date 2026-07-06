package modelo;

public class Reserva {
    private String codigoReserva;
    private int idCliente;
    private int numeroHabitacion;
    private String fechaIngreso;
    private String fechaSalida;

    public Reserva() {
    }

    public Reserva(String codigoReserva, int idCliente, int numeroHabitacion, String fechaIngreso, String fechaSalida) {
        this.codigoReserva = codigoReserva;
        this.idCliente = idCliente;
        this.numeroHabitacion = numeroHabitacion;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
    }

    // Getters y Setters
    public String getCodigoReserva() { return codigoReserva; }
    public void setCodigoReserva(String codigoReserva) { this.codigoReserva = codigoReserva; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(int numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

    public String getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(String fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(String fechaSalida) { this.fechaSalida = fechaSalida; }
}