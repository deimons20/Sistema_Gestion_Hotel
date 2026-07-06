package persistencia;

import interfaces.CRUD;
import java.io.*;
import java.util.ArrayList;
import modelo.Habitacion;

public class HabitacionArchivo implements CRUD<Habitacion> {

    private final String RUTA = "archivos/habitaciones.txt";

    public HabitacionArchivo() {
        try {
            File file = new File(RUTA);
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) { }
    }

    @Override
    public ArrayList<Habitacion> listar() {
        ArrayList<Habitacion> habitaciones = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                Habitacion h = new Habitacion();
                h.setNumeroHabitacion(Integer.parseInt(datos[0]));
                h.setTipo(datos[1]);
                h.setEstado(datos[2]);
                h.setPrecio(Double.parseDouble(datos[3]));
                habitaciones.add(h);
            }
        } catch (Exception e) { }
        return habitaciones;
    }

    private boolean sobrescribirArchivo(ArrayList<Habitacion> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA))) {
            for (Habitacion h : lista) {
                bw.write(h.getNumeroHabitacion() + ";" + h.getTipo() + ";" + 
                         h.getEstado() + ";" + h.getPrecio());
                bw.newLine();
            }
            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public boolean registrar(Habitacion objeto) {
        ArrayList<Habitacion> lista = listar();
        lista.add(objeto);
        return sobrescribirArchivo(lista);
    }

    @Override
    public boolean actualizar(Habitacion objeto) {
        ArrayList<Habitacion> lista = listar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNumeroHabitacion() == objeto.getNumeroHabitacion()) {
                lista.set(i, objeto);
                return sobrescribirArchivo(lista);
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String numero) {
        ArrayList<Habitacion> lista = listar();
        int num = Integer.parseInt(numero);
        lista.removeIf(h -> h.getNumeroHabitacion() == num);
        return sobrescribirArchivo(lista);
    }

    @Override
    public Habitacion buscar(String numero) {
        int num = Integer.parseInt(numero);
        return listar().stream().filter(h -> h.getNumeroHabitacion() == num).findFirst().orElse(null);
    }
}