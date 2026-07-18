package modelo;

public class Pago {
    private String idPago;
    private String codigoReserva;
    private double montoTotal;
    private String metodoPago;

    public Pago() {
    }

    public Pago(String idPago, String codigoReserva, double montoTotal, String metodoPago) {
        this.idPago = idPago;
        this.codigoReserva = codigoReserva;
        this.montoTotal = montoTotal;
        this.metodoPago = metodoPago;
    }
    
    public void procesarPago() {
        System.out.println("Procesando pago general por: S/" + montoTotal);
    }

    public String getIdPago() { return idPago; }
    public void setIdPago(String idPago) { this.idPago = idPago; }

    public String getCodigoReserva() { return codigoReserva; }
    public void setCodigoReserva(String codigoReserva) { this.codigoReserva = codigoReserva; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}