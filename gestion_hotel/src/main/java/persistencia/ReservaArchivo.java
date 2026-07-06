package persistencia;

import interfaces.CRUD;
import java.io.*;
import java.util.ArrayList;
import modelo.Reserva;

public class ReservaArchivo implements CRUD<Reserva> {

    private final String RUTA = "archivos/reservas.txt";

    public ReservaArchivo() {
        try {
            File file = new File(RUTA);
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) { }
    }

    @Override
    public ArrayList<Reserva> listar() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                Reserva r = new Reserva();
                r.setCodigoReserva(datos[0]);
                r.setIdCliente(Integer.parseInt(datos[1]));
                r.setNumeroHabitacion(Integer.parseInt(datos[2]));
                r.setFechaIngreso(datos[3]);
                r.setFechaSalida(datos[4]);
                reservas.add(r);
            }
        } catch (Exception e) { }
        return reservas;
    }

    private boolean sobrescribirArchivo(ArrayList<Reserva> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA))) {
            for (Reserva r : lista) {
                bw.write(r.getCodigoReserva() + ";" + r.getIdCliente() + ";" + 
                         r.getNumeroHabitacion() + ";" + r.getFechaIngreso() + ";" + r.getFechaSalida());
                bw.newLine();
            }
            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public boolean registrar(Reserva objeto) {
        ArrayList<Reserva> lista = listar();
        lista.add(objeto);
        return sobrescribirArchivo(lista);
    }

    @Override
    public boolean actualizar(Reserva objeto) {
        ArrayList<Reserva> lista = listar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigoReserva().equals(objeto.getCodigoReserva())) {
                lista.set(i, objeto);
                return sobrescribirArchivo(lista);
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String codigo) {
        ArrayList<Reserva> lista = listar();
        lista.removeIf(r -> r.getCodigoReserva().equals(codigo));
        return sobrescribirArchivo(lista);
    }

    @Override
    public Reserva buscar(String codigo) {
        return listar().stream().filter(r -> r.getCodigoReserva().equals(codigo)).findFirst().orElse(null);
    }
}