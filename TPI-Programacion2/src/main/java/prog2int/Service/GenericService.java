package prog2int.Service;

import java.util.List;

public interface GenericService<T> {
    void crear(T t) throws Exception;
    T leer(Long id) throws Exception;
    List<T> leerTodos() throws Exception;
    void actualizar(T t) throws Exception;
    void eliminar(Long id) throws Exception;
}
