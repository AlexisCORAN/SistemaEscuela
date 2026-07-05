package matricula.view;

import alumnos.model.Alumno;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import main.VentanaPrincipal;
import matricula.controller.MatriculaController;
import matricula.model.Matricula;
import matricula.model.MatriculaCurso;
import plan_estudios.model.Curso;
import plan_estudios.model.Grado;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */

/**
 *
 * @author Alexis
 */
public class DialogNuevaMatricula extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DialogNuevaMatricula.class.getName());
    private final MatriculaController matriculaController;
    private final PanelMatricula panelPadre;
    private final Matricula matriculaDetalle; 
    
    private Alumno alumnoContexto = null;
    private List<Grado> gradosCargadosContexto = new ArrayList<>();

    /**
     * Creates new form DialogNuevaMatricula
     */
    public DialogNuevaMatricula(java.awt.Frame parent, boolean modal, MatriculaController matriculaController, PanelMatricula panelPadre, Matricula matriculaDetalle) {
        super(parent, modal);
        this.matriculaController = matriculaController;
        this.panelPadre = panelPadre;
        this.matriculaDetalle = matriculaDetalle; 
        initComponents();
        this.setLocationRelativeTo(parent);
        
        configurarTabla();
        
        if (this.matriculaDetalle != null) {
            configurarModoDetalle();
        } else {
            configurarModoNuevo();
        }
    }

    private void configurarTabla() {
        tablaMatricula.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        tablaMatricula.getTableHeader().setBackground(new java.awt.Color(240, 240, 240));
        tablaMatricula.getTableHeader().setForeground(new java.awt.Color(50, 50, 50));
        javax.swing.table.DefaultTableCellRenderer renderCentrado = new javax.swing.table.DefaultTableCellRenderer();
        renderCentrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        javax.swing.table.DefaultTableCellRenderer renderIzquierdo = new javax.swing.table.DefaultTableCellRenderer();
        renderIzquierdo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tablaMatricula.getColumnModel().getColumn(0).setCellRenderer(renderCentrado); 
        tablaMatricula.getColumnModel().getColumn(1).setCellRenderer(renderIzquierdo); 
        tablaMatricula.getColumnModel().getColumn(2).setCellRenderer(renderCentrado); 
        if (tablaMatricula.getTableHeader().getDefaultRenderer() instanceof javax.swing.table.DefaultTableCellRenderer) {
            ((javax.swing.table.DefaultTableCellRenderer) tablaMatricula.getTableHeader().getDefaultRenderer())
                    .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        }
    }

    private void configurarModoDetalle() {
        nombrePanel.setText("Detalle Consolidados de Matrícula: " + matriculaDetalle.getCodigoMatricula());
        if (matriculaDetalle.getAlumno() != null) {
            txtDocBusqueda.setText(matriculaDetalle.getAlumno().getDni()); 
            txtNombresEstudiante.setText(matriculaDetalle.getAlumno().getApellidos() + ", " + matriculaDetalle.getAlumno().getNombres());
        }
        txtAñoEscolar.setText(String.valueOf(matriculaDetalle.getAñoEscolar()));
        
        txtDocBusqueda.setEditable(false);
        btnBuscarAlumno.setEnabled(false);
        cboGrados.removeAllItems();
        
        if (matriculaDetalle.getGrado() != null) {
            cboGrados.addItem(matriculaDetalle.getGrado().getNombre() + " - " + matriculaDetalle.getGrado().getNivel());
        }
        cboGrados.setEnabled(false);
        btnGuardar.setVisible(false); 
        
        limpiarTablaCursos();
        if (matriculaDetalle.getCursosMatriculados() != null) {
            for (matricula.model.MatriculaCurso mc : matriculaDetalle.getCursosMatriculados()) {
                if (mc.getCurso() != null) {
                    agregarCursoATabla(mc.getCurso().getCodigo(), mc.getCurso().getNombre(), mc.getCurso().getHorasSemanales());
                }
            }
        }
    }

    private void configurarModoNuevo() {
        txtAñoEscolar.setText(String.valueOf(java.time.LocalDate.now().getYear()));
        if (this.matriculaController != null) {
            cboGrados.removeAllItems();
            cboGrados.addItem("-- Seleccione un Grado --");
            this.gradosCargadosContexto = matriculaController.obtenerGradosConCursos();
            for (Grado g : this.gradosCargadosContexto) {
                cboGrados.addItem(g.getNombre() + " - " + g.getNivel());
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelPrincipal = new javax.swing.JPanel();
        panelSuperior = new javax.swing.JPanel();
        nombrePanel = new javax.swing.JLabel();
        panelCentral = new javax.swing.JPanel();
        panelContenedor = new javax.swing.JPanel();
        panelBusquedaAlumno = new javax.swing.JPanel();
        panelRowBusqueda = new javax.swing.JPanel();
        lblDocBusqueda = new javax.swing.JLabel();
        filler12 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0));
        txtDocBusqueda = new javax.swing.JTextField();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0));
        btnBuscarAlumno = new javax.swing.JButton();
        panelRowEstudiante = new javax.swing.JPanel();
        lblEstudiante = new javax.swing.JLabel();
        filler13 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0));
        txtNombresEstudiante = new javax.swing.JTextField();
        panelSelecAcademica = new javax.swing.JPanel();
        panelRowAñoEscolar = new javax.swing.JPanel();
        lblAñoEscolar = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0));
        txtAñoEscolar = new javax.swing.JTextField();
        panelRowGrado = new javax.swing.JPanel();
        lblGradoMatricular = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0));
        cboGrados = new javax.swing.JComboBox<>();
        panelPrevCursos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMatricula = new javax.swing.JTable();
        panelInferior = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setModal(true);
        setResizable(false);

        panelPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setLayout(new java.awt.BorderLayout());

        panelSuperior.setLayout(new javax.swing.BoxLayout(panelSuperior, javax.swing.BoxLayout.X_AXIS));

        nombrePanel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nombrePanel.setText("Información de Matrícula");
        nombrePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        nombrePanel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        nombrePanel.setMaximumSize(new java.awt.Dimension(300, 30));
        nombrePanel.setMinimumSize(new java.awt.Dimension(300, 30));
        nombrePanel.setPreferredSize(new java.awt.Dimension(300, 30));
        panelSuperior.add(nombrePanel);

        panelPrincipal.add(panelSuperior, java.awt.BorderLayout.NORTH);

        panelCentral.setLayout(new java.awt.BorderLayout());

        panelContenedor.setLayout(new java.awt.BorderLayout());

        panelBusquedaAlumno.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda del Alumno", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        panelBusquedaAlumno.setLayout(new javax.swing.BoxLayout(panelBusquedaAlumno, javax.swing.BoxLayout.Y_AXIS));

        panelRowBusqueda.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
        panelRowBusqueda.setMaximumSize(new java.awt.Dimension(32767, 45));
        panelRowBusqueda.setOpaque(false);
        panelRowBusqueda.setLayout(new javax.swing.BoxLayout(panelRowBusqueda, javax.swing.BoxLayout.X_AXIS));

        lblDocBusqueda.setText("Buscar DNI / Código");
        lblDocBusqueda.setMaximumSize(new java.awt.Dimension(150, 30));
        lblDocBusqueda.setMinimumSize(new java.awt.Dimension(150, 30));
        lblDocBusqueda.setPreferredSize(new java.awt.Dimension(150, 30));
        panelRowBusqueda.add(lblDocBusqueda);
        panelRowBusqueda.add(filler12);

        txtDocBusqueda.setMaximumSize(new java.awt.Dimension(32767, 35));
        txtDocBusqueda.setMinimumSize(new java.awt.Dimension(250, 35));
        txtDocBusqueda.setPreferredSize(new java.awt.Dimension(250, 35));
        txtDocBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocBusquedaActionPerformed(evt);
            }
        });
        panelRowBusqueda.add(txtDocBusqueda);
        panelRowBusqueda.add(filler2);

        btnBuscarAlumno.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBuscarAlumno.setText("Buscar");
        btnBuscarAlumno.setMaximumSize(new java.awt.Dimension(100, 35));
        btnBuscarAlumno.setMinimumSize(new java.awt.Dimension(100, 35));
        btnBuscarAlumno.setPreferredSize(new java.awt.Dimension(100, 35));
        btnBuscarAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarAlumnoActionPerformed(evt);
            }
        });
        panelRowBusqueda.add(btnBuscarAlumno);

        panelBusquedaAlumno.add(panelRowBusqueda);

        panelRowEstudiante.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
        panelRowEstudiante.setMaximumSize(new java.awt.Dimension(32767, 45));
        panelRowEstudiante.setOpaque(false);
        panelRowEstudiante.setLayout(new javax.swing.BoxLayout(panelRowEstudiante, javax.swing.BoxLayout.X_AXIS));

        lblEstudiante.setText("Estudiante");
        lblEstudiante.setMaximumSize(new java.awt.Dimension(150, 30));
        lblEstudiante.setMinimumSize(new java.awt.Dimension(150, 30));
        lblEstudiante.setPreferredSize(new java.awt.Dimension(150, 30));
        panelRowEstudiante.add(lblEstudiante);
        panelRowEstudiante.add(filler13);

        txtNombresEstudiante.setEditable(false);
        txtNombresEstudiante.setMaximumSize(new java.awt.Dimension(32767, 35));
        txtNombresEstudiante.setMinimumSize(new java.awt.Dimension(250, 35));
        txtNombresEstudiante.setPreferredSize(new java.awt.Dimension(250, 35));
        txtNombresEstudiante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombresEstudianteActionPerformed(evt);
            }
        });
        panelRowEstudiante.add(txtNombresEstudiante);

        panelBusquedaAlumno.add(panelRowEstudiante);

        panelContenedor.add(panelBusquedaAlumno, java.awt.BorderLayout.WEST);

        panelSelecAcademica.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selección Académica\n", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        panelSelecAcademica.setLayout(new javax.swing.BoxLayout(panelSelecAcademica, javax.swing.BoxLayout.Y_AXIS));

        panelRowAñoEscolar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
        panelRowAñoEscolar.setMaximumSize(new java.awt.Dimension(32767, 45));
        panelRowAñoEscolar.setOpaque(false);
        panelRowAñoEscolar.setLayout(new javax.swing.BoxLayout(panelRowAñoEscolar, javax.swing.BoxLayout.X_AXIS));

        lblAñoEscolar.setText("Año Escolar");
        lblAñoEscolar.setMaximumSize(new java.awt.Dimension(150, 30));
        lblAñoEscolar.setMinimumSize(new java.awt.Dimension(150, 30));
        lblAñoEscolar.setPreferredSize(new java.awt.Dimension(150, 30));
        panelRowAñoEscolar.add(lblAñoEscolar);
        panelRowAñoEscolar.add(filler3);

        txtAñoEscolar.setEditable(false);
        txtAñoEscolar.setMaximumSize(new java.awt.Dimension(32767, 35));
        txtAñoEscolar.setMinimumSize(new java.awt.Dimension(250, 35));
        txtAñoEscolar.setPreferredSize(new java.awt.Dimension(250, 35));
        txtAñoEscolar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAñoEscolarActionPerformed(evt);
            }
        });
        panelRowAñoEscolar.add(txtAñoEscolar);

        panelSelecAcademica.add(panelRowAñoEscolar);

        panelRowGrado.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
        panelRowGrado.setOpaque(false);
        panelRowGrado.setLayout(new javax.swing.BoxLayout(panelRowGrado, javax.swing.BoxLayout.X_AXIS));

        lblGradoMatricular.setText("Grado a Matricular");
        lblGradoMatricular.setMaximumSize(new java.awt.Dimension(150, 30));
        lblGradoMatricular.setMinimumSize(new java.awt.Dimension(150, 30));
        lblGradoMatricular.setPreferredSize(new java.awt.Dimension(150, 30));
        panelRowGrado.add(lblGradoMatricular);
        panelRowGrado.add(filler4);

        cboGrados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione un Grado --" }));
        cboGrados.setMaximumSize(new java.awt.Dimension(32767, 35));
        cboGrados.setMinimumSize(new java.awt.Dimension(72, 35));
        cboGrados.setPreferredSize(new java.awt.Dimension(72, 35));
        cboGrados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGradosActionPerformed(evt);
            }
        });
        panelRowGrado.add(cboGrados);

        panelSelecAcademica.add(panelRowGrado);

        panelContenedor.add(panelSelecAcademica, java.awt.BorderLayout.EAST);

        panelCentral.add(panelContenedor, java.awt.BorderLayout.NORTH);

        panelPrevCursos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Previsualización de Cursos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        panelPrevCursos.setLayout(new java.awt.BorderLayout());

        tablaMatricula.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Codigo", "Curso", "Horas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaMatricula.setFillsViewportHeight(true);
        tablaMatricula.setGridColor(new java.awt.Color(225, 225, 225));
        tablaMatricula.setOpaque(false);
        tablaMatricula.setRowHeight(40);
        tablaMatricula.setShowGrid(false);
        tablaMatricula.setShowHorizontalLines(true);
        tablaMatricula.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaMatricula);
        if (tablaMatricula.getColumnModel().getColumnCount() > 0) {
            tablaMatricula.getColumnModel().getColumn(0).setResizable(false);
            tablaMatricula.getColumnModel().getColumn(1).setResizable(false);
            tablaMatricula.getColumnModel().getColumn(2).setResizable(false);
        }

        panelPrevCursos.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jScrollPane1.getViewport().setBackground(java.awt.Color.WHITE);
        jScrollPane1.setBackground(java.awt.Color.WHITE);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        panelCentral.add(panelPrevCursos, java.awt.BorderLayout.CENTER);

        panelPrincipal.add(panelCentral, java.awt.BorderLayout.CENTER);

        panelInferior.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 5, 20));
        panelInferior.setLayout(new javax.swing.BoxLayout(panelInferior, javax.swing.BoxLayout.X_AXIS));

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setMaximumSize(new java.awt.Dimension(100, 35));
        btnCancelar.setMinimumSize(new java.awt.Dimension(100, 35));
        btnCancelar.setPreferredSize(new java.awt.Dimension(100, 35));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        panelInferior.add(btnCancelar);
        panelInferior.add(filler7);

        btnGuardar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setMaximumSize(new java.awt.Dimension(100, 35));
        btnGuardar.setMinimumSize(new java.awt.Dimension(100, 35));
        btnGuardar.setPreferredSize(new java.awt.Dimension(100, 35));
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        panelInferior.add(btnGuardar);

        panelPrincipal.add(panelInferior, java.awt.BorderLayout.SOUTH);

        getContentPane().add(panelPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtAñoEscolarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAñoEscolarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAñoEscolarActionPerformed

    private void txtDocBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocBusquedaActionPerformed
        ejecutarBusquedaAlumno();
    }//GEN-LAST:event_txtDocBusquedaActionPerformed

    private void txtNombresEstudianteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombresEstudianteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombresEstudianteActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
       if (matriculaController == null) return;
        
        if (this.alumnoContexto == null) {
            mostrarMensaje("Debe seleccionar un estudiante válido antes de guardar.", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int indexGrado = cboGrados.getSelectedIndex();
        if (indexGrado <= 0) {
            mostrarMensaje("Debe seleccionar un grado académico para el estudiante.", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Grado gradoSeleccionado = gradosCargadosContexto.get(indexGrado - 1);
            int año = Integer.parseInt(txtAñoEscolar.getText().trim());
            

            Matricula m = new Matricula();
            m.setAlumno(this.alumnoContexto);
            m.setGrado(gradoSeleccionado);
            m.setAñoEscolar(año);
            m.setFechaMatricula(java.time.LocalDate.now());
            
            if (gradoSeleccionado.getCursos() != null) {
                m.setActivo(true); 
                for (Curso c : gradoSeleccionado.getCursos()) {
                    m.agregarCurso(new MatriculaCurso(c));
                }
            }

            if (matriculaController.registrarMatricula(m)) {
                mostrarMensaje("¡Matrícula registrada con éxito!", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                if (panelPadre != null) {
                    panelPadre.refrescarTabla(matriculaController.obtenerMatriculas()); 
                }
                this.dispose(); 
            } else {
                mostrarMensaje("Error al persistir la matrícula.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("El año escolar no cuenta con un formato válido.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarAlumnoActionPerformed
        ejecutarBusquedaAlumno();
    }//GEN-LAST:event_btnBuscarAlumnoActionPerformed

    private void cboGradosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGradosActionPerformed
       limpiarTablaCursos();
        int index = cboGrados.getSelectedIndex();
        
        if (index > 0 && !gradosCargadosContexto.isEmpty()) {
            Grado gradoContexto = gradosCargadosContexto.get(index - 1);
            if (gradoContexto != null && gradoContexto.getCursos() != null) {
                for (Curso c : gradoContexto.getCursos()) {
                    agregarCursoATabla(c.getCodigo(), c.getNombre(), c.getHorasSemanales());
                }
            }
        }
    }//GEN-LAST:event_cboGradosActionPerformed

    private void ejecutarBusquedaAlumno() {
        if (matriculaController == null) return;
        String criterio = txtDocBusqueda.getText().trim();
        if (criterio.isEmpty()) {
            mostrarMensaje("Debe ingresar un DNI o código válido.", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Alumno alum = matriculaController.buscarAlumnoParaMatricula(criterio);
            if (alum != null) {
                this.alumnoContexto = alum; 
                txtNombresEstudiante.setText(alum.getApellidos() + ", " + alum.getNombres());
            } else {
                this.alumnoContexto = null;
                txtNombresEstudiante.setText("");
                mostrarMensaje("Estudiante no encontrado en el sistema.", "Búsqueda", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IllegalStateException e) {
            this.alumnoContexto = null;
            txtNombresEstudiante.setText("");
            mostrarMensaje(e.getMessage(), "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void limpiarTablaCursos() {
        ((DefaultTableModel) tablaMatricula.getModel()).setRowCount(0);
    }
    
    public void agregarCursoATabla(String codigo, String nombre, int horas) {
        ((DefaultTableModel) tablaMatricula.getModel()).addRow(new Object[]{codigo, nombre, horas});
    }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "No se pudo aplicar el tema nativo del sistema", ex);
        }

        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarAlumno;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<String> cboGrados;
    private javax.swing.Box.Filler filler12;
    private javax.swing.Box.Filler filler13;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAñoEscolar;
    private javax.swing.JLabel lblDocBusqueda;
    private javax.swing.JLabel lblEstudiante;
    private javax.swing.JLabel lblGradoMatricular;
    private javax.swing.JLabel nombrePanel;
    private javax.swing.JPanel panelBusquedaAlumno;
    private javax.swing.JPanel panelCentral;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelInferior;
    private javax.swing.JPanel panelPrevCursos;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelRowAñoEscolar;
    private javax.swing.JPanel panelRowBusqueda;
    private javax.swing.JPanel panelRowEstudiante;
    private javax.swing.JPanel panelRowGrado;
    private javax.swing.JPanel panelSelecAcademica;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JTable tablaMatricula;
    private javax.swing.JTextField txtAñoEscolar;
    private javax.swing.JTextField txtDocBusqueda;
    private javax.swing.JTextField txtNombresEstudiante;
    // End of variables declaration//GEN-END:variables
}
