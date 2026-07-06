package persistencia;

import interfaces.CRUD;
import java.io.*;
import java.util.ArrayList;
import modelo.Cliente;

public class ClienteArchivo implements CRUD<Cliente> {
    
    private final String RUTA = "archivos/clientes.txt";

    public ClienteArchivo() {
        verificarArchivo();
    }

    private void verificarArchivo() {
        try {
            File file = new File(RUTA);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs(); // Crea la carpeta archivos si no existe uwu
            }
            if (!file.exists()) {
                file.createNewFile(); // aqui lo que hacemos es crear un txt en caso no exista uno que coincida con la ruta
            }
        } catch (IOException e) {
            System.out.println("Error al verificar archivo clientes: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Cliente> listar() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 5) {
                    Cliente c = new Cliente();
                    c.setIdCliente(Integer.parseInt(datos[0]));
                    c.setDni(datos[1]);
                    c.setNombres(datos[2]);
                    c.setApellidos(datos[3]);
                    c.setTelefono(datos[4]);
                    clientes.add(c);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }

    private boolean sobrescribirArchivo(ArrayList<Cliente> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA))) {
            for (Cliente c : lista) {
                bw.write(c.getIdCliente() + ";" + c.getDni() + ";" + c.getNombres() + ";" + 
                         c.getApellidos() + ";" + c.getTelefono());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error al sobrescribir clientes: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean registrar(Cliente objeto) {
        ArrayList<Cliente> lista = listar();
        lista.add(objeto);
        return sobrescribirArchivo(lista);
    }

    @Override
    public boolean actualizar(Cliente objeto) {
        ArrayList<Cliente> lista = listar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdCliente() == objeto.getIdCliente()) {
                lista.set(i, objeto);
                return sobrescribirArchivo(lista);
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        ArrayList<Cliente> lista = listar();
        int idBuscado = Integer.parseInt(id);
        lista.removeIf(c -> c.getIdCliente() == idBuscado); //Aplicamos paradigma funcional(Lambda) uwu
        return sobrescribirArchivo(lista);
    }

    @Override
    public Cliente buscar(String id) {
        int idBuscado = Integer.parseInt(id);
        for (Cliente c : listar()) {
            if (c.getIdCliente() == idBuscado || c.getDni().equals(id)) {
                return c;
            }
        }
        return null;
    }
}