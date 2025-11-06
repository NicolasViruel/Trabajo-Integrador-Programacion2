package prog2int.Service;

import prog2int.Config.DatabaseConnection;
import prog2int.Dao.EmpleadoDAO;
import prog2int.Dao.LegajoDAO;
import prog2int.Models.Empleado;
import prog2int.Models.Legajo;

import java.sql.Connection;
import java.util.List;

public class EmpleadoService implements GenericService<Empleado> {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final LegajoDAO legajoDAO = new LegajoDAO();

    @Override
    public void crear(Empleado empleado) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Crear el legajo primero
            Legajo legajo = empleado.getLegajo();
            if (legajo != null) {
                legajoDAO.crear(legajo, conn);
                empleado.setLegajo(legajo);
            }

            // Crear el empleado
            empleadoDAO.crear(empleado, conn);

            conn.commit();
        } catch (Exception e) {
            throw new Exception("Error al crear empleado: " + e.getMessage(), e);
        }
    }

    @Override
    public Empleado leer(Long id) throws Exception {
        return empleadoDAO.leer(id);
    }

    @Override
    public List<Empleado> leerTodos() throws Exception {
        return empleadoDAO.leerTodos();
    }

    @Override
    public void actualizar(Empleado empleado) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            if (empleado.getLegajo() != null) {
                legajoDAO.actualizar(empleado.getLegajo(), conn);
            }
            empleadoDAO.actualizar(empleado, conn);
            conn.commit();
        } catch (Exception e) {
            throw new Exception("Error al actualizar empleado: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            empleadoDAO.eliminar(id, conn);
            conn.commit();
        } catch (Exception e) {
            throw new Exception("Error al eliminar empleado: " + e.getMessage(), e);
        }
    }
}
