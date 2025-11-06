package prog2int.Service;

import prog2int.Config.DatabaseConnection;
import prog2int.Dao.LegajoDAO;
import prog2int.Models.Legajo;

import java.sql.Connection;
import java.util.List;

public class LegajoService implements GenericService<Legajo> {

    private final LegajoDAO legajoDAO = new LegajoDAO();

    @Override
    public void crear(Legajo legajo) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            legajoDAO.crear(legajo, conn);
            conn.commit();
        } catch (Exception e) {
            throw new Exception("Error al crear legajo: " + e.getMessage(), e);
        }
    }

    @Override
    public Legajo leer(Long id) throws Exception {
        return legajoDAO.leer(id);
    }

    @Override
    public List<Legajo> leerTodos() throws Exception {
        return legajoDAO.leerTodos();
    }

    @Override
    public void actualizar(Legajo legajo) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            legajoDAO.actualizar(legajo, conn);
            conn.commit();
        } catch (Exception e) {
            throw new Exception("Error al actualizar legajo: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            legajoDAO.eliminar(id, conn);
            conn.commit();
        } catch (Exception e) {
            throw new Exception("Error al eliminar legajo: " + e.getMessage(), e);
        }
    }
}

