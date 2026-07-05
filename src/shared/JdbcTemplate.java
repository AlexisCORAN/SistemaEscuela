package shared;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class JdbcTemplate {

    private JdbcTemplate() {
    }

    public static <T> List<T> query(Connection connection, String sql, IRowMapper<T> mapper, Object... params) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(mapper.mapRow(rs));
                }
                return results;
            }
        } catch (SQLException ex) {
            
            throw new RuntimeException("Error ejecutando la consulta SQL: " + sql, ex);
        }
    }

    public static <T> T queryForObject(Connection connection, String sql, IRowMapper<T> mapper, Object... params) {
        List<T> results = query(connection, sql, mapper, params);
        return results.isEmpty() ? null : results.get(0);
    }

    public static int update(Connection connection, String sql, Object... params) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, params);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error ejecutando la actualización SQL: " + sql, ex);
        }
    }

    private static void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null || params.length == 0) {
            return;
        }
        
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            int index = i + 1;

            if (param == null) {
                ps.setObject(index, null);
            } else if (param instanceof LocalDate) {
                ps.setDate(index, Date.valueOf((LocalDate) param));
            } else if (param instanceof Boolean) {
                ps.setBoolean(index, (Boolean) param);
            } else if (param instanceof Integer) {
                ps.setInt(index, (Integer) param);
            } else if (param instanceof Double) {
                ps.setDouble(index, (Double) param);
            } else if (param instanceof String) {
                ps.setString(index, (String) param);
            } else {
                ps.setObject(index, param);
            }
        }
    }
    
    public static Integer ejecutarActualizacionConId(Connection connection, String sql, Object... params) {
        try (PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            setParameters(ps, params);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Error ejecutando el INSERT y obteniendo ID: " + sql, ex);
        }
    }
}