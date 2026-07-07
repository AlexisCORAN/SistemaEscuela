/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matricula.dao;
import java.sql.Connection;
import java.util.List;
import matricula.model.Matricula;
import matricula.model.MatriculaCurso;
import shared.JdbcTemplate;
import java.util.Collections;
import shared.RowMappers;
/*
 * @author Alexis
 */

public class MatriculaDAOImpl implements IMatriculaDAO {

    private final Connection conexion;

    private static final String SELECT_MATRICULA_BASE = 
        "SELECT M.idMatricula, M.codigoMatricula, M.fechaMatricula, M.anioEscolar, M.estado AS estadoCabecera, " +
        "A.idAlumno, A.dni AS alumnoDni, A.nombres AS alumnoNombres, A.apellidos AS alumnoApellidos, A.codigoEstudiante AS alumnoCodigoEstudiante, " +
        "G.idGrado, G.nombre AS gradoNombre, G.nivel AS gradoNivel " +
        "FROM Matricula M " +
        "LEFT JOIN Alumno A ON M.idAlumno = A.idAlumno " +
        "LEFT JOIN Grado G ON M.idGrado = G.idGrado";

    public MatriculaDAOImpl(final Connection conexion) {
        this.conexion = conexion;
    }

    private void cargarDetalleCursos(final Matricula m) {
        if (conexion == null || m == null) return;
        try {
            final IMatriculaCursoDAO detalleDAO = new MatriculaCursoDAOImpl(conexion);
            final List<MatriculaCurso> detalles = detalleDAO.buscarCursosPorMatricula(m.getId());

            m.limpiarCursos();

            final boolean estadoReal = m.getActivo();
            m.setActivo(true); 
            for (final MatriculaCurso mc : detalles) {
                m.agregarCurso(mc);
            }
            m.setActivo(estadoReal); 
        } catch (final Exception e) {
            System.err.println("Error crítico al acoplar el detalle de cursos: " + e.getMessage());
        }
    }

    @Override
    public List<Matricula> listarTodos() {
        if (conexion == null) return Collections.emptyList();
        
        final List<Matricula> lista = JdbcTemplate.query(conexion, SELECT_MATRICULA_BASE, RowMappers.MATRICULA_COMPLETA_MAPPER);
        for (final Matricula m : lista) {
            cargarDetalleCursos(m);
        }
        return lista;
    }

    @Override
    public Matricula obtenerPorId(Object id) {
        if (conexion == null) return null;

        final String sql = SELECT_MATRICULA_BASE + " WHERE M.idMatricula = ?";
        final Matricula m = JdbcTemplate.queryForObject(conexion, sql, RowMappers.MATRICULA_COMPLETA_MAPPER, id);
        if (m != null) {
            cargarDetalleCursos(m);
        }
        return m;
    }

    @Override
    public boolean insertar(final Matricula entidad) {
        if (conexion == null) return false;

        final String sqlMatricula = "INSERT INTO Matricula (codigoMatricula, idAlumno, idGrado, fechaMatricula, anioEscolar, estado) " +
                                    "OUTPUT INSERTED.idMatricula " +
                                    "VALUES (?, ?, ?, ?, ?, ?)";
                                    
        try {
            final java.sql.Date fechaSql = entidad.getFechaMatricula() != null ? 
                    java.sql.Date.valueOf(entidad.getFechaMatricula()) : java.sql.Date.valueOf(java.time.LocalDate.now());

            final Integer idGenerado = JdbcTemplate.queryForObject(conexion, sqlMatricula, rs -> rs.getInt(1),
                    entidad.getCodigoMatricula(),
                    entidad.getAlumno().getId(), 
                    entidad.getGrado().getId(),
                    fechaSql,
                    entidad.getAnioEscolar(),
                    entidad.getActivo() ? "VIGENTE" : "ANULADA"
            );

            if (idGenerado == null || idGenerado <= 0) return false;
            entidad.setId(idGenerado);

            final IMatriculaCursoDAO detalleDAO = new MatriculaCursoDAOImpl(conexion);
            detalleDAO.insertarCursosDeMatricula(entidad.getId(), entidad.getCursosMatriculados());
            
            return true;
        } catch (final Exception e) {
            System.err.println("Rollback implícito - Error en inserción: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(final Matricula entidad) {
        if (conexion == null) return false;

        final String sql = "UPDATE Matricula SET idAlumno = ?, idGrado = ?, fechaMatricula = ?, anioEscolar = ?, estado = ? WHERE idMatricula = ?";
        return JdbcTemplate.update(conexion, sql,
                entidad.getAlumno().getId(),
                entidad.getGrado().getId(),
                entidad.getFechaMatricula() != null ? java.sql.Date.valueOf(entidad.getFechaMatricula()) : null,
                entidad.getAnioEscolar(),
                entidad.getActivo() ? "VIGENTE" : "ANULADA",
                entidad.getId()) > 0;
    }

    @Override
    public boolean eliminar(Object id) {
        if (conexion == null) return false;

        final String sqlDetalle = "DELETE FROM MatriculaCurso WHERE idMatricula = ?";
        JdbcTemplate.update(conexion, sqlDetalle, id);
        
        final String sqlMatricula = "DELETE FROM Matricula WHERE idMatricula = ?";
        return JdbcTemplate.update(conexion, sqlMatricula, id) > 0;
    }

    @Override
    public Matricula obtenerMatriculaActiva(String codigoEstudiante, int anioEscolar) {
        if (conexion == null) return null;

        final String sql = SELECT_MATRICULA_BASE + " WHERE (A.codigoEstudiante = ? OR A.dni = ?) AND M.anioEscolar = ? AND M.estado = 'VIGENTE'";
        final Matricula m = JdbcTemplate.queryForObject(conexion, sql, RowMappers.MATRICULA_COMPLETA_MAPPER, codigoEstudiante, codigoEstudiante, anioEscolar);
        if (m != null) {
            cargarDetalleCursos(m);
        }
        return m;
    }

    @Override
    public List<Matricula> listarPorGradoAnio(int idGrado, int anioEscolar) {
        if (conexion == null) return Collections.emptyList();

        final String sql = SELECT_MATRICULA_BASE + " WHERE M.idGrado = ? AND M.anioEscolar = ?";
        final List<Matricula> lista = JdbcTemplate.query(conexion, sql, RowMappers.MATRICULA_COMPLETA_MAPPER, idGrado, anioEscolar);
        for (final Matricula m : lista) {
            cargarDetalleCursos(m);
        }
        return lista;
    }

    @Override
    public boolean anularMatricula(Integer idMatricula) {
        if (conexion == null) return false;

        final String sql = "UPDATE Matricula SET estado = 'ANULADA' WHERE idMatricula = ?";
        return JdbcTemplate.update(conexion, sql, idMatricula) > 0;
    }

    @Override
    public String obtenerUltimoCodigo() {
        if (conexion == null) return null;

        final String sql = "SELECT TOP 1 codigoMatricula FROM Matricula ORDER BY idMatricula DESC";
        try {
            return JdbcTemplate.queryForObject(conexion, sql, (rs) -> rs.getString(1));
        } catch (final Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Matricula> buscarPorCriterioMatricula(String criterio) {
        if (conexion == null) return Collections.emptyList();

        String sql = SELECT_MATRICULA_BASE + " WHERE A.dni LIKE ? OR A.codigoEstudiante LIKE ? OR (A.nombres + ' ' + A.apellidos) LIKE ?";
        String parametro = "%" + criterio + "%";

        List<Matricula> lista = JdbcTemplate.query(conexion, sql, RowMappers.MATRICULA_COMPLETA_MAPPER, parametro, parametro, parametro);
        for (Matricula m : lista) {
            cargarDetalleCursos(m);
        }
        return lista;
    }
    
    @Override
    public List<Matricula> listarPorEstado(String estadoDb) {
        if (conexion == null) return Collections.emptyList();

        final String sql = SELECT_MATRICULA_BASE + " WHERE M.estado = ?";
        final List<Matricula> lista = JdbcTemplate.query(conexion, sql, RowMappers.MATRICULA_COMPLETA_MAPPER, estadoDb);
        for (final Matricula m : lista) {
            cargarDetalleCursos(m);
        }
        return lista;
    }
    
    @Override
    public Integer obtenerIdMatriculaCurso(String codigoMatricula, Integer idCurso) {
        if (conexion == null) return null;

        String sql = "SELECT mc.idMatriculaCurso FROM MatriculaCurso mc " +
                     "INNER JOIN Matricula mat ON mc.idMatricula = mat.idMatricula " +
                     "WHERE mat.codigoMatricula = ? AND mc.idCurso = ?";

        try {
            return shared.JdbcTemplate.queryForObject(conexion, sql, rs -> rs.getInt(1), codigoMatricula, idCurso);
        } catch (Exception e) {
            System.err.println("Error al obtener ID de MatriculaCurso: " + e.getMessage());
            return null;
        }
    }
    }