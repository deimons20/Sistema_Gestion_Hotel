package persistencia;

import interfaces.CRUD;
import java.io.*;
import java.util.ArrayList;
import modelo.Pago;

public class PagoArchivo implements CRUD<Pago> {

    private final String RUTA = "archivos/pagos.txt";

    public PagoArchivo() {
        try {
            File file = new File(RUTA);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) { }
    }

    @Override
    public ArrayList<Pago> listar() {
        ArrayList<Pago> pagos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                Pago p = new Pago();
                p.setIdPago(datos[0]);
                p.setCodigoReserva(datos[1]);
                p.setMontoTotal(Double.parseDouble(datos[2]));
                p.setMetodoPago(datos[3]);
                pagos.add(p);
            }
        } catch (Exception e) { }
        return pagos;
    }

    private boolean sobrescribirArchivo(ArrayList<Pago> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA))) {
            for (Pago p : lista) {
                bw.write(p.getIdPago() + ";" + p.getCodigoReserva() + ";" + 
                         p.getMontoTotal() + ";" + p.getMetodoPago());
                bw.newLine();
            }
            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public boolean registrar(Pago objeto) {
        ArrayList<Pago> lista = listar();
        lista.add(objeto);
        return sobrescribirArchivo(lista);
    }

    @Override
    public boolean actualizar(Pago objeto) {
        ArrayList<Pago> lista = listar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdPago().equals(objeto.getIdPago())) {
                lista.set(i, objeto);
                return sobrescribirArchivo(lista);
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        ArrayList<Pago> lista = listar();
        lista.removeIf(p -> p.getIdPago().equals(id));
        return sobrescribirArchivo(lista);
    }

    @Override
    public Pago buscar(String id) {
        return listar().stream().filter(p -> p.getIdPago().equals(id)).findFirst().orElse(null);
    }
}
