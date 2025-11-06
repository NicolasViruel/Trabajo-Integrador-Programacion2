package prog2int.Dao;

import prog2int.Config.DatabaseConnection;
import prog2int.Models.Empleado;
import prog2int.Models.Legajo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO implements GenericDAO<Empleado> {

    private final LegajoDAO legajoDAO = new LegajoDAO();

    @Override
    public void crear(Empleado emp, Connection conn) throws SQLException {
        String sql = "INSERT INTO empleado (nombre, apellido, dni, email, fecha_ingreso, area, legajo_id, eliminado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getApellido());
            ps.setString(3, emp.getDni());
            ps.setString(4, emp.getEmail());
            ps.setDate(5, emp.getFechaIngreso() != null ? Date.valueOf(emp.getFechaIngreso()) : null);
            ps.setString(6, emp.getArea());
            ps.setObject(7, emp.getLegajo() != null ? emp.getLegajo().getId() : null);
            ps.setBoolean(8, emp.isEliminado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    emp.setId(rs.getLong(1));
                }
            }
        }
    }

    @Override
    public Empleado leer(Long id) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE id = ? AND eliminado = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empleado emp = mapResultSetToEmpleado(rs);
                    Long legajoId = rs.getLong("legajo_id");
                    if (legajoId != 0) emp.setLegajo(legajoDAO.leer(legajoId));
                    return emp;
                }
            }
        }
        return null;
    }

    @Override
    public List<Empleado> leerTodos() throws SQLException {
        String sql = "SELECT * FROM empleado WHERE eliminado = FALSE";
        List<Empleado> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Empleado emp = mapResultSetToEmpleado(rs);
                Long legajoId = rs.getLong("legajo_id");
                if (legajoId != 0) emp.setLegajo(legajoDAO.leer(legajoId));
                lista.add(emp);
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Empleado emp, Connection conn) throws SQLException {
        String sql = "UPDATE empleado SET nombre=?, apellido=?, dni=?, email=?, fecha_ingreso=?, area=?, legajo_id=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getApellido());
            ps.setString(3, emp.getDni());
            ps.setString(4, emp.getEmail());
            ps.setDate(5, emp.getFechaIngreso() != null ? Date.valueOf(emp.getFechaIngreso()) : null);
            ps.setString(6, emp.getArea());
            ps.setObject(7, emp.getLegajo() != null ? emp.getLegajo().getId() : null);
            ps.setLong(8, emp.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id, Connection conn) throws SQLException {
        String sql = "UPDATE empleado SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Empleado mapResultSetToEmpleado(ResultSet rs) throws SQLException {
        Empleado emp = new Empleado();
        emp.setId(rs.getLong("id"));
        emp.setEliminado(rs.getBoolean("eliminado"));
        emp.setNombre(rs.getString("nombre"));
        emp.setApellido(rs.getString("apellido"));
        emp.setDni(rs.getString("dni"));
        emp.setEmail(rs.getString("email"));
        Date fechaIngreso = rs.getDate("fecha_ingreso");
        if (fechaIngreso != null) emp.setFechaIngreso(fechaIngreso.toLocalDate());
        emp.setArea(rs.getString("area"));
        return emp;
    }
}
