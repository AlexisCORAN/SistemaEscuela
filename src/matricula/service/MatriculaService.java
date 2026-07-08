/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matricula.service;

/**
 *
 * @author Alexis
 */
import alumnos.model.Alumno;
import alumnos.service.AlumnoService;
import config.ConexionDB;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import matricula.dao.IMatriculaDAO;
import matricula.dao.MatriculaDAOImpl;
import matricula.model.Matricula;
/**
 *
 * @author Alexis
 */

public class MatriculaService {

    private final AlumnoService alumnoService;

    public MatriculaService() {
        this.alumnoService = new AlumnoService();
    }

    public List<Matricula> obtenerMatriculas() {
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);
            return matriculaDAO.listarTodos();
        } catch (final Exception e) {
            System.err.println("Error al obtener catálogo de matrículas: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Matricula> buscarMatriculas(final String criterio) {
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);

            final Matricula resultado = matriculaDAO.obtenerMatriculaActiva(criterio, java.time.LocalDate.now().getYear());
            if (resultado != null) {
                return List.of(resultado);
            }

            return matriculaDAO.buscarPorCriterioMatricula(criterio);
        } catch (final Exception e) {
            System.err.println("Error en motor de búsqueda adaptativa de matrículas: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void registrarMatricula(final Matricula m) {
        validarDatosBasicos(m);

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            conn.setAutoCommit(false);

            try {
                final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);

                generarYAsignarCodigo(m, matriculaDAO);
                verificarDuplicidad(m, matriculaDAO);

                if (!matriculaDAO.insertar(m)) {
                    throw new RuntimeException("Error al registrar la cabecera de la matrícula.");
                }

                inicializarRegistrosBimestrales(m, conn);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Error en la transacción de matrícula: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión a la base de datos: " + e.getMessage(), e);
        }
    }

    private void inicializarRegistrosBimestrales(Matricula m, Connection conn) throws SQLException {
        notas.dao.IRegistroBimestralDAO registroDAO = new notas.dao.RegistroBimestralDAOImpl(conn);
        matricula.dao.IMatriculaDAO matriculaDAO = new matricula.dao.MatriculaDAOImpl(conn);

        for (matricula.model.MatriculaCurso mc : m.getCursosMatriculados()) {

            if (mc.getId() == null || mc.getId() <= 0) {
                Integer idReal = matriculaDAO.obtenerIdMatriculaCurso(m.getCodigoMatricula(), mc.getCurso().getId());

                if (idReal == null) {
                    throw new SQLException("No se pudo recuperar el ID autogenerado para el curso: " + mc.getCurso().getNombre());
                }
                mc.setId(idReal); 
            }

            for (int i = 1; i <= 4; i++) {
                notas.model.RegistroBimestral rb = new notas.model.RegistroBimestral();
                rb.setMatriculaCurso(mc);
                rb.setBimestre(shared.Bimestre.desdeId(i)); 
                rb.setActivo(true); 

                if (!registroDAO.insertar(rb)) {
                    throw new SQLException("Fallo al insertar el bimestre " + i + " para el curso " + mc.getCurso().getNombre());
                }
            }
        }
    }

    public void anularMatricula(final Integer idMatricula) {
        if (idMatricula == null) {
            throw new IllegalArgumentException("El identificador interno de la matrícula no puede ser nulo.");
        }

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            conn.setAutoCommit(false);

            try {
                final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);
                if (!matriculaDAO.anularMatricula(idMatricula)) {
                    throw new RuntimeException("El registro no pudo ser modificado en el motor de base de datos.");
                }
                
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Error en la transacción de anulación: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión a la base de datos: " + e.getMessage(), e);
        }
    }
    
    private void generarYAsignarCodigo(Matricula matricula, IMatriculaDAO matriculaDAO) {
        final String ultimoCod = matriculaDAO.obtenerUltimoCodigo();
        int correlativo = 1;

        if (ultimoCod != null && ultimoCod.contains("-")) {
            final String[] partes = ultimoCod.split("-");
            if (partes.length == 3) {
                correlativo = Integer.parseInt(partes[2]) + 1;
            }
        }

        matricula.generarCodigoMatricula(correlativo);
    }
    
    public Matricula obtenerMatriculaPorId(final Integer idMatricula) {
        if (idMatricula == null) return null;
        
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);
            return matriculaDAO.obtenerPorId(idMatricula);
        } catch (final Exception e) {
            System.err.println("Error en negocio al obtener matrícula por ID: " + e.getMessage());
            return null;
        }
    }
    
    public List<Matricula> obtenerMatriculasPorEstado(String filtroEstado) {
        if (filtroEstado == null || filtroEstado.equalsIgnoreCase("TODOS")) {
            return obtenerMatriculas();
        }

        String estadoDb = filtroEstado.equalsIgnoreCase("VIGENTES") ? "VIGENTE" : "ANULADA";

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);
            return matriculaDAO.listarPorEstado(estadoDb);
        } catch (final Exception e) {
            System.err.println("Error en negocio al filtrar matrículas por estado: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public Alumno buscarAlumnoParaMatricula(final String criterio) {
        if (criterio == null || criterio.isBlank()) return null;

        Alumno alum = alumnoService.buscarAlumnoPorCodigo(criterio);

        if (alum == null) {
            alum = alumnoService.buscarAlumnoPorDni(criterio);
        }

        if (alum != null && !alum.isActivo()) {
            throw new IllegalStateException("El estudiante se encuentra en estado RETIRADO. No se puede matricular.");
        }

        return alum;
    }
    
    private void validarDatosBasicos(Matricula m) {
        if (m == null) {
            throw new IllegalArgumentException("La matrícula no puede ser nula.");
        }
        if (m.getAlumno() == null) {
            throw new IllegalArgumentException("Debe asociar un alumno válido a la matrícula.");
        }
    }

    private void verificarDuplicidad(Matricula m, IMatriculaDAO matriculaDAO) {
        Matricula existente = matriculaDAO.obtenerMatriculaActiva(m.getAlumno().getCodigoEstudiante(), m.getAnioEscolar());
        if (existente != null) {
            throw new IllegalStateException("El estudiante ya cuenta con una matrícula registrada para el año escolar " + m.getAnioEscolar() + ".");
        }
    }
     
}