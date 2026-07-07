/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matricula.dao;
import java.sql.Connection;
import java.util.List;
import matricula.model.MatriculaCurso;
import plan_estudios.model.Curso;
import shared.JdbcTemplate;
/**
 *
 * @author Alexis
 */
public class MatriculaCursoDAOImpl implements IMatriculaCursoDAO {
    private final Connection conexion;

    public MatriculaCursoDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void insertarCursosDeMatricula(int idMatricula, List<MatriculaCurso> cursos) {
        String sql = "INSERT INTO MatriculaCurso (idMatricula, idCurso, notaFinal) VALUES (?, ?, ?)";
        for (MatriculaCurso mc : cursos) {
            boolean insertado = JdbcTemplate.update(conexion, sql, idMatricula, mc.getCurso().getId(), mc.getNotaFinal()) > 0;
            if (!insertado) {
                throw new RuntimeException("No se pudo registrar el curso: " + mc.getCurso().getNombre());
            }
        }
    }

    @Override
    public List<MatriculaCurso> buscarCursosPorMatricula(int idMatricula) {
        String sql = "SELECT MC.idMatriculaCurso, MC.notaFinal, C.idCurso, C.codigoCurso, C.nombre, C.horasSemanales " +
                     "FROM MatriculaCurso MC INNER JOIN Curso C ON MC.idCurso = C.idCurso WHERE MC.idMatricula = ?";
        return JdbcTemplate.query(conexion, sql, rs -> {
            Curso c = new Curso();
            c.setId(rs.getInt("idCurso"));
            c.setCodigo(rs.getString("codigoCurso"));
            c.setNombre(rs.getString("nombre"));
            c.setHorasSemanales(rs.getInt("horasSemanales"));
            return new MatriculaCurso(rs.getInt("idMatriculaCurso"), c, rs.getDouble("notaFinal"));
        }, idMatricula);
    }

    @Override
    public List<MatriculaCurso> listarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public MatriculaCurso obtenerPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean insertar(MatriculaCurso entidad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean actualizar(MatriculaCurso entidad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
