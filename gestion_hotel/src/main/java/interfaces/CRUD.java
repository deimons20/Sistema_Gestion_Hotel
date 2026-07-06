package interfaces;
import java.util.ArrayList;

public interface CRUD<T> {
    boolean registrar(T objeto);
    boolean actualizar(T objeto);
    boolean eliminar(String id);
    T buscar(String id);
    ArrayList<T> listar();
}