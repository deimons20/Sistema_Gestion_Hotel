package persistencia;

import interfaces.CRUD;
import java.io.*;
import java.util.ArrayList;
import modelo.Consumo;

public class ConsumoArchivo implements CRUD<Consumo> {

    private final String RUTA = "archivos/consumos.txt";

    public ConsumoArchivo() {
        try {
            File file = new File(RUTA);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) { }
    }

    @Override
    public ArrayList<Consumo> listar() {
        ArrayList<Consumo> consumos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                Consumo c = new Consumo();
                c.setIdConsumo(Integer.parseInt(datos[0]));
                c.setNumeroHabitacion(Integer.parseInt(datos[1]));
                c.setCodigoSnack(datos[2]);
                c.setCantidad(Integer.parseInt(datos[3]));
                c.setSubtotal(Double.parseDouble(datos[4]));
                consumos.add(c);
            }
        } catch (Exception e) { }
        return consumos;
    }

    private boolean sobrescribirArchivo(ArrayList<Consumo> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA))) {
            for (Consumo c : lista) {
                bw.write(c.getIdConsumo() + ";" + c.getNumeroHabitacion() + ";" + 
                         c.getCodigoSnack() + ";" + c.getCantidad() + ";" + c.getSubtotal());
                bw.newLine();
            }
            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public boolean registrar(Consumo objeto) {
        ArrayList<Consumo> lista = listar();
        // Auto-incrementar ID si es necesario o asumir que ya viene
        lista.add(objeto);
        return sobrescribirArchivo(lista);
    }

    @Override
    public boolean actualizar(Consumo objeto) {
        ArrayList<Consumo> lista = listar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdConsumo() == objeto.getIdConsumo()) {
                lista.set(i, objeto);
                return sobrescribirArchivo(lista);
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        ArrayList<Consumo> lista = listar();
        int idBusqueda = Integer.parseInt(id);
        lista.removeIf(c -> c.getIdConsumo() == idBusqueda);
        return sobrescribirArchivo(lista);
    }

    @Override
    public Consumo buscar(String id) {
        int idBusqueda = Integer.parseInt(id);
        return listar().stream().filter(c -> c.getIdConsumo() == idBusqueda).findFirst().orElse(null);
    }
    
    // Método para obtener consumos de una habitación específica
    public ArrayList<Consumo> listarPorHabitacion(int numeroHabitacion) {
        ArrayList<Consumo> filtrado = new ArrayList<>();
        for (Consumo c : listar()) {
            if (c.getNumeroHabitacion() == numeroHabitacion) {
                filtrado.add(c);
            }
        }
        return filtrado;
    }
}
