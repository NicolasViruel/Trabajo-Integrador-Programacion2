package prog2int.Dao;

import java.sql.Connection;
import java.util.List;

public interface GenericDAO<T> {
    void crear(T t, Connection conn) throws Exception;
    T leer(Long id) throws Exception;
    List<T> leerTodos() throws Exception;
    void actualizar(T t, Connection conn) throws Exception;
    void eliminar(Long id, Connection conn) throws Exception;
}

