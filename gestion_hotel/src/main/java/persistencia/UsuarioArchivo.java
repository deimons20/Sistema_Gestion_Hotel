package persistencia;

import interfaces.CRUD;
import java.io.*;
import java.util.ArrayList;
import modelo.Usuario;

public class UsuarioArchivo implements CRUD<Usuario> {

    private final String RUTA = "archivos/usuarios.txt";

    public UsuarioArchivo() {
        try {
            File file = new File(RUTA);
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) { }
    }

    @Override
    public ArrayList<Usuario> listar() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                Usuario u = new Usuario();
                u.setIdUsuario(Integer.parseInt(datos[0]));
                u.setUsername(datos[1]);
                u.setPassword(datos[2]);
                u.setRol(datos[3]);
                usuarios.add(u);
            }
        } catch (Exception e) { }
        return usuarios;
    }

    private boolean sobrescribirArchivo(ArrayList<Usuario> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA))) {
            for (Usuario u : lista) {
                bw.write(u.getIdUsuario() + ";" + u.getUsername() + ";" + 
                         u.getPassword() + ";" + u.getRol());
                bw.newLine();
            }
            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public boolean registrar(Usuario objeto) {
        ArrayList<Usuario> lista = listar();
        lista.add(objeto);
        return sobrescribirArchivo(lista);
    }

    @Override
    public boolean actualizar(Usuario objeto) {
        ArrayList<Usuario> lista = listar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdUsuario() == objeto.getIdUsuario()) {
                lista.set(i, objeto);
                return sobrescribirArchivo(lista);
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        ArrayList<Usuario> lista = listar();
        int idBusqueda = Integer.parseInt(id);
        lista.removeIf(u -> u.getIdUsuario() == idBusqueda);
        return sobrescribirArchivo(lista);
    }

    @Override
    public Usuario buscar(String id) {
        int idBusqueda = Integer.parseInt(id);
        return listar().stream().filter(u -> u.getIdUsuario() == idBusqueda).findFirst().orElse(null);
    }
    
    // Método extra para el Login uwu
    public Usuario validarLogin(String username, String password) {
        for (Usuario u : listar()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u; // Login exitoso
            }
        }
        return null; // Credenciales incorrectas
    }
}