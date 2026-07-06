package persistencia;

import interfaces.CRUD;
import java.io.*;
import java.util.ArrayList;
import modelo.Snack;

public class SnackArchivo implements CRUD<Snack> {

    private final String RUTA = "archivos/snacks.txt";

    public SnackArchivo() {
        try {
            File file = new File(RUTA);
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) { }
    }

    @Override
    public ArrayList<Snack> listar() {
        ArrayList<Snack> snacks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                Snack s = new Snack();
                s.setCodigo(datos[0]);
                s.setNombre(datos[1]);
                s.setStock(Integer.parseInt(datos[2]));
                s.setPrecio(Double.parseDouble(datos[3]));
                snacks.add(s);
            }
        } catch (Exception e) { }
        return snacks;
    }

    private boolean sobrescribirArchivo(ArrayList<Snack> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA))) {
            for (Snack s : lista) {
                bw.write(s.getCodigo() + ";" + s.getNombre() + ";" + s.getStock() + ";" + s.getPrecio());
                bw.newLine();
            }
            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public boolean registrar(Snack objeto) {
        ArrayList<Snack> lista = listar();
        lista.add(objeto);
        return sobrescribirArchivo(lista);
    }

    @Override
    public boolean actualizar(Snack objeto) {
        ArrayList<Snack> lista = listar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo().equals(objeto.getCodigo())) {
                lista.set(i, objeto);
                return sobrescribirArchivo(lista);
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String codigo) {
        ArrayList<Snack> lista = listar();
        lista.removeIf(s -> s.getCodigo().equals(codigo));
        return sobrescribirArchivo(lista);
    }

    @Override
    public Snack buscar(String codigo) {
        return listar().stream().filter(s -> s.getCodigo().equals(codigo)).findFirst().orElse(null);
    }
}