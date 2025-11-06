package prog2int.Dao;

import prog2int.Config.DatabaseConnection;
import prog2int.Models.EstadoLegajo;
import prog2int.Models.Legajo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LegajoDAO implements GenericDAO<Legajo> {

    @Override
    public void crear(Legajo legajo, Connection conn) throws SQLException {
        String sql = "INSERT INTO legajo (nro_legajo, categoria, estado, fecha_alta, observaciones, eliminado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, legajo.getNroLegajo());
            ps.setString(2, legajo.getCategoria());
            ps.setString(3, legajo.getEstado().name());
            ps.setDate(4, legajo.getFechaAlta() != null ? Date.valueOf(legajo.getFechaAlta()) : null);
            ps.setString(5, legajo.getObservaciones());
            ps.setBoolean(6, legajo.isEliminado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    legajo.setId(rs.getLong(1));
                }
            }
        }
    }

    @Override
    public Legajo leer(Long id) throws SQLException {
        String sql = "SELECT * FROM legajo WHERE id = ? AND eliminado = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLegajo(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Legajo> leerTodos() throws SQLException {
        String sql = "SELECT * FROM legajo WHERE eliminado = FALSE";
        List<Legajo> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToLegajo(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Legajo legajo, Connection conn) throws SQLException {
        String sql = "UPDATE legajo SET nro_legajo=?, categoria=?, estado=?, fecha_alta=?, observaciones=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, legajo.getNroLegajo());
            ps.setString(2, legajo.getCategoria());
            ps.setString(3, legajo.getEstado().name());
            ps.setDate(4, legajo.getFechaAlta() != null ? Date.valueOf(legajo.getFechaAlta()) : null);
            ps.setString(5, legajo.getObservaciones());
            ps.setLong(6, legajo.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id, Connection conn) throws SQLException {
        String sql = "UPDATE legajo SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Legajo mapResultSetToLegajo(ResultSet rs) throws SQLException {
        Legajo legajo = new Legajo();
        legajo.setId(rs.getLong("id"));
        legajo.setEliminado(rs.getBoolean("eliminado"));
        legajo.setNroLegajo(rs.getString("nro_legajo"));
        legajo.setCategoria(rs.getString("categoria"));
        legajo.setEstado(EstadoLegajo.valueOf(rs.getString("estado")));
        Date fechaAlta = rs.getDate("fecha_alta");
        if (fechaAlta != null) legajo.setFechaAlta(fechaAlta.toLocalDate());
        legajo.setObservaciones(rs.getString("observaciones"));
        return legajo;
    }
}
