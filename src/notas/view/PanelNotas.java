package notas.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import notas.controller.RegistroBimestralController;
import notas.model.Evaluacion;
import notas.model.RegistroBimestral;
import plan_estudios.dao.CursoDAOImpl;
import plan_estudios.dao.GradoDAOImpl;
import plan_estudios.model.Curso;
import plan_estudios.model.Grado;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author Alexis
 */
public class PanelNotas extends javax.swing.JPanel {

    private final RegistroBimestralController notasController;
    
    private List<Grado> gradosCargados = new ArrayList<>();
    private List<Curso> cursosCargados = new ArrayList<>();
    private List<Integer> idsRegistrosCargados = new ArrayList<>();

    /**
     * Creates new form PanelAlumnos
     */
    public PanelNotas() {
        this.notasController = null;
        initComponents();
        estilizarComponentes();
        cboCurso.setEnabled(false);
        cboBimestre.setEnabled(false);
        btnCargarAlumnos.setEnabled(false);
    }
    
    public PanelNotas(RegistroBimestralController notasController) {
        this.notasController = notasController;
        initComponents();
        estilizarComponentes();
        cboCurso.setEnabled(false);
        cboBimestre.setEnabled(false);
        btnCargarAlumnos.setEnabled(false);
        
        cboGrados.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                if (cboGrados.getSelectedIndex() > 0) {
                    cargarCursosPorGrado(gradosCargados.get(cboGrados.getSelectedIndex() - 1).getId());
                } else {
                    cboCurso.removeAllItems();
                    cboCurso.addItem("-- Seleccione Curso --");
                    cboCurso.setEnabled(false);
                    btnCargarAlumnos.setEnabled(false);
                }
            }
        });
 
        if (this.notasController != null) {
            cargarFiltrosGrado();
        }
    }
 

    private void cargarFiltrosGrado() {
        cboGrados.removeAllItems();
        cboGrados.addItem("-- Seleccione Grado --");
        cboGrado.removeAllItems();
        cboGrado.addItem("-- Seleccione Grado --");
        this.gradosCargados.clear();
        try {
            plan_estudios.controller.GradoController gradoController = new plan_estudios.controller.GradoController();
            List<Grado> activos = gradoController.obtenerGradosActivos();
            for (Grado g : activos) {
                this.gradosCargados.add(g);
                final String etiqueta = g.getNombre() + " - " + g.getNivel();
                cboGrados.addItem(etiqueta);
                cboGrado.addItem(etiqueta);
            }
        } catch (Exception e) {
            System.err.println("Error carga grados: " + e.getMessage());
        }
    }
 
   
    private void cargarCursosPorGrado(int idGrado) {
        cboCurso.removeAllItems();
        cboCurso.addItem("-- Seleccione Curso --");
        this.cursosCargados.clear();
        cboCurso.setEnabled(true);
        btnCargarAlumnos.setEnabled(true);
        cboBimestre.setEnabled(true);
 
        try {
            plan_estudios.controller.CursoController cursoController = new plan_estudios.controller.CursoController();
            List<Curso> todos = cursoController.obtenerCursos();
            for (Curso c : todos) {
                if (c.getGradoAsignado() != null && c.getGradoAsignado().getId() == idGrado) {
                    this.cursosCargados.add(c);
                    cboCurso.addItem(c.getNombre());
                }
            }
        } catch (Exception e) {
            System.err.println("Error carga cursos: " + e.getMessage());
        }
    }
    
    private void estilizarComponentes() {
        this.setBackground(java.awt.Color.WHITE);
        panelRegistroNotas.setBackground(java.awt.Color.WHITE);
        panelReporteRiesgo.setBackground(java.awt.Color.WHITE);
        jTabbedPane1.setBackground(java.awt.Color.WHITE);
        jTabbedPane1.setOpaque(true);
 
        java.awt.Color colorBorde = new java.awt.Color(220, 220, 220);
        javax.swing.border.Border bordeSimple = javax.swing.BorderFactory.createLineBorder(colorBorde, 1);
 
        cboGrados.setBackground(java.awt.Color.WHITE);
        cboGrados.setBorder(bordeSimple);
        cboCurso.setBackground(java.awt.Color.WHITE);
        cboCurso.setBorder(bordeSimple);
 
        jScrollPane1.getViewport().setBackground(java.awt.Color.WHITE);
        jScrollPane1.setBackground(java.awt.Color.WHITE);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        
        tablaNotas.setFillsViewportHeight(true); 
        tablaNotas.setOpaque(false);
        tablaNotas.setRowHeight(40);
        tablaNotas.setShowGrid(false);
        tablaNotas.setShowHorizontalLines(true);
        tablaNotas.setGridColor(new java.awt.Color(230, 230, 230));
 
        javax.swing.table.JTableHeader header1 = tablaNotas.getTableHeader();
        header1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        header1.setBackground(new java.awt.Color(245, 245, 245));
        header1.setOpaque(true);
        header1.setReorderingAllowed(false);
 
        jScrollPane2.getViewport().setBackground(java.awt.Color.WHITE);
        jScrollPane2.setBackground(java.awt.Color.WHITE);
        jScrollPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        
        tablaNotasRiesgo.setFillsViewportHeight(true);
        tablaNotasRiesgo.setOpaque(false);
        tablaNotasRiesgo.setRowHeight(40);
        tablaNotasRiesgo.setShowGrid(false);
        tablaNotasRiesgo.setShowHorizontalLines(true);
        tablaNotasRiesgo.setGridColor(new java.awt.Color(230, 230, 230));
 
        javax.swing.table.JTableHeader header2 = tablaNotasRiesgo.getTableHeader();
        header2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        header2.setBackground(new java.awt.Color(245, 245, 245));
        header2.setOpaque(true);
        header2.setReorderingAllowed(false);
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
        panelNombre = new javax.swing.JPanel();
        nombrePanel = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 15), new java.awt.Dimension(0, 15), new java.awt.Dimension(0, 15));
        panelCentral = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelRegistroNotas = new javax.swing.JPanel();
        panelCabecera = new javax.swing.JPanel();
        panelGrado = new javax.swing.JPanel();
        lblGrado = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        cboGrados = new javax.swing.JComboBox<>();
        panelCurso = new javax.swing.JPanel();
        lblCurso = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        cboCurso = new javax.swing.JComboBox<>();
        panelBimestre = new javax.swing.JPanel();
        lblBimestre = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        cboBimestre = new javax.swing.JComboBox<>();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(40, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        btnCargarAlumnos = new javax.swing.JButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        btnGuardarNotas = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaNotas = new javax.swing.JTable();
        panelReporteRiesgo = new javax.swing.JPanel();
        panelCabecera1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        lblGrado1 = new javax.swing.JLabel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        cboGrado = new javax.swing.JComboBox<>();
        panelBimestre1 = new javax.swing.JPanel();
        lblBimestre1 = new javax.swing.JLabel();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        cboBimestre1 = new javax.swing.JComboBox<>();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        btnGenerarReporte = new javax.swing.JButton();
        panelNotasRiesgo = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaNotasRiesgo = new javax.swing.JTable();
        panelInferior = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new java.awt.BorderLayout());

        panelPrincipal.setOpaque(false);
        panelPrincipal.setLayout(new java.awt.BorderLayout());

        panelSuperior.setOpaque(false);
        panelSuperior.setLayout(new javax.swing.BoxLayout(panelSuperior, javax.swing.BoxLayout.Y_AXIS));

        panelNombre.setMaximumSize(new java.awt.Dimension(32767, 35));
        panelNombre.setMinimumSize(new java.awt.Dimension(400, 30));
        panelNombre.setOpaque(false);
        panelNombre.setPreferredSize(new java.awt.Dimension(32767, 35));
        panelNombre.setLayout(new java.awt.BorderLayout());

        nombrePanel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        nombrePanel.setText("Gestión de Calificaciones");
        nombrePanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelNombre.add(nombrePanel, java.awt.BorderLayout.WEST);

        panelSuperior.add(panelNombre);
        panelSuperior.add(filler4);

        panelPrincipal.add(panelSuperior, java.awt.BorderLayout.NORTH);

        panelCentral.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        panelRegistroNotas.setBackground(new java.awt.Color(255, 255, 255));
        panelRegistroNotas.setLayout(new java.awt.BorderLayout());

        panelCabecera.setLayout(new javax.swing.BoxLayout(panelCabecera, javax.swing.BoxLayout.LINE_AXIS));

        panelGrado.setLayout(new javax.swing.BoxLayout(panelGrado, javax.swing.BoxLayout.LINE_AXIS));

        lblGrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGrado.setText("Grado");
        lblGrado.setMaximumSize(new java.awt.Dimension(50, 30));
        lblGrado.setMinimumSize(new java.awt.Dimension(50, 30));
        lblGrado.setPreferredSize(new java.awt.Dimension(50, 30));
        panelGrado.add(lblGrado);
        panelGrado.add(filler1);

        cboGrados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboGrados.setMaximumSize(new java.awt.Dimension(70, 35));
        cboGrados.setMinimumSize(new java.awt.Dimension(70, 35));
        cboGrados.setPreferredSize(new java.awt.Dimension(70, 35));
        cboGrados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGradosActionPerformed(evt);
            }
        });
        panelGrado.add(cboGrados);

        panelCabecera.add(panelGrado);

        panelCurso.setLayout(new javax.swing.BoxLayout(panelCurso, javax.swing.BoxLayout.LINE_AXIS));

        lblCurso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurso.setText("Curso");
        lblCurso.setMaximumSize(new java.awt.Dimension(50, 30));
        lblCurso.setMinimumSize(new java.awt.Dimension(50, 30));
        lblCurso.setPreferredSize(new java.awt.Dimension(50, 30));
        panelCurso.add(lblCurso);
        panelCurso.add(filler3);

        cboCurso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCurso.setMaximumSize(new java.awt.Dimension(70, 35));
        cboCurso.setMinimumSize(new java.awt.Dimension(70, 35));
        cboCurso.setPreferredSize(new java.awt.Dimension(70, 35));
        cboCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCursoActionPerformed(evt);
            }
        });
        panelCurso.add(cboCurso);

        panelCabecera.add(panelCurso);

        panelBimestre.setLayout(new javax.swing.BoxLayout(panelBimestre, javax.swing.BoxLayout.LINE_AXIS));

        lblBimestre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBimestre.setText("Bimestre");
        lblBimestre.setMaximumSize(new java.awt.Dimension(70, 30));
        lblBimestre.setMinimumSize(new java.awt.Dimension(70, 30));
        lblBimestre.setPreferredSize(new java.awt.Dimension(70, 30));
        panelBimestre.add(lblBimestre);
        panelBimestre.add(filler6);

        cboBimestre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboBimestre.setMaximumSize(new java.awt.Dimension(70, 35));
        cboBimestre.setMinimumSize(new java.awt.Dimension(70, 35));
        cboBimestre.setPreferredSize(new java.awt.Dimension(70, 35));
        cboBimestre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBimestreActionPerformed(evt);
            }
        });
        panelBimestre.add(cboBimestre);

        panelCabecera.add(panelBimestre);
        panelCabecera.add(filler2);

        btnCargarAlumnos.setText("Cargar Alumnos");
        btnCargarAlumnos.setMaximumSize(new java.awt.Dimension(120, 35));
        btnCargarAlumnos.setMinimumSize(new java.awt.Dimension(120, 35));
        btnCargarAlumnos.setPreferredSize(new java.awt.Dimension(120, 35));
        btnCargarAlumnos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarAlumnosActionPerformed(evt);
            }
        });
        panelCabecera.add(btnCargarAlumnos);
        panelCabecera.add(filler5);

        btnGuardarNotas.setText("Guardar Notas");
        btnGuardarNotas.setMaximumSize(new java.awt.Dimension(120, 35));
        btnGuardarNotas.setMinimumSize(new java.awt.Dimension(120, 35));
        btnGuardarNotas.setPreferredSize(new java.awt.Dimension(120, 35));
        btnGuardarNotas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarNotasActionPerformed(evt);
            }
        });
        panelCabecera.add(btnGuardarNotas);

        panelRegistroNotas.add(panelCabecera, java.awt.BorderLayout.NORTH);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tablaNotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Alumno", "P. Calificada", "Tarea Académica", "Ex. Parcial", "Ex. Bimestral", "Prom. Bimestre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaNotas.setRowHeight(35);
        tablaNotas.setShowGrid(true);
        jScrollPane1.setViewportView(tablaNotas);
        if (tablaNotas.getColumnModel().getColumnCount() > 0) {
            tablaNotas.getColumnModel().getColumn(0).setResizable(false);
            tablaNotas.getColumnModel().getColumn(1).setResizable(false);
            tablaNotas.getColumnModel().getColumn(2).setResizable(false);
            tablaNotas.getColumnModel().getColumn(3).setResizable(false);
            tablaNotas.getColumnModel().getColumn(4).setResizable(false);
            tablaNotas.getColumnModel().getColumn(5).setResizable(false);
            tablaNotas.getColumnModel().getColumn(6).setResizable(false);
        }

        panelRegistroNotas.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("   Registro de Notas   ", panelRegistroNotas);

        panelReporteRiesgo.setBackground(new java.awt.Color(255, 255, 255));
        panelReporteRiesgo.setLayout(new java.awt.BorderLayout());

        panelCabecera1.setLayout(new javax.swing.BoxLayout(panelCabecera1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        lblGrado1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGrado1.setText("Grado");
        lblGrado1.setMaximumSize(new java.awt.Dimension(50, 30));
        lblGrado1.setMinimumSize(new java.awt.Dimension(50, 30));
        lblGrado1.setPreferredSize(new java.awt.Dimension(50, 30));
        jPanel9.add(lblGrado1);
        jPanel9.add(filler7);

        cboGrado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboGrado.setMaximumSize(new java.awt.Dimension(70, 35));
        cboGrado.setMinimumSize(new java.awt.Dimension(70, 35));
        cboGrado.setPreferredSize(new java.awt.Dimension(70, 35));
        cboGrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGradoActionPerformed(evt);
            }
        });
        jPanel9.add(cboGrado);

        panelCabecera1.add(jPanel9);

        panelBimestre1.setLayout(new javax.swing.BoxLayout(panelBimestre1, javax.swing.BoxLayout.LINE_AXIS));

        lblBimestre1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBimestre1.setText("Bimestre");
        lblBimestre1.setMaximumSize(new java.awt.Dimension(70, 30));
        lblBimestre1.setMinimumSize(new java.awt.Dimension(70, 30));
        lblBimestre1.setPreferredSize(new java.awt.Dimension(70, 30));
        panelBimestre1.add(lblBimestre1);
        panelBimestre1.add(filler8);

        cboBimestre1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboBimestre1.setMaximumSize(new java.awt.Dimension(70, 35));
        cboBimestre1.setMinimumSize(new java.awt.Dimension(50, 35));
        cboBimestre1.setPreferredSize(new java.awt.Dimension(70, 35));
        cboBimestre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBimestre1ActionPerformed(evt);
            }
        });
        panelBimestre1.add(cboBimestre1);

        panelCabecera1.add(panelBimestre1);
        panelCabecera1.add(filler9);

        btnGenerarReporte.setText("Generar Reporte");
        btnGenerarReporte.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGenerarReporte.setMaximumSize(new java.awt.Dimension(120, 35));
        btnGenerarReporte.setMinimumSize(new java.awt.Dimension(120, 35));
        btnGenerarReporte.setPreferredSize(new java.awt.Dimension(120, 35));
        btnGenerarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteActionPerformed(evt);
            }
        });
        panelCabecera1.add(btnGenerarReporte);

        panelReporteRiesgo.add(panelCabecera1, java.awt.BorderLayout.NORTH);

        panelNotasRiesgo.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

        tablaNotasRiesgo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "DNI", "Nombre del Alumno", "Curso en Riesgo", "Promedio Actual", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaNotasRiesgo.setRowHeight(35);
        tablaNotasRiesgo.setShowGrid(true);
        jScrollPane2.setViewportView(tablaNotasRiesgo);
        if (tablaNotasRiesgo.getColumnModel().getColumnCount() > 0) {
            tablaNotasRiesgo.getColumnModel().getColumn(0).setResizable(false);
            tablaNotasRiesgo.getColumnModel().getColumn(1).setResizable(false);
            tablaNotasRiesgo.getColumnModel().getColumn(2).setResizable(false);
            tablaNotasRiesgo.getColumnModel().getColumn(3).setResizable(false);
            tablaNotasRiesgo.getColumnModel().getColumn(4).setResizable(false);
        }

        panelNotasRiesgo.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        panelReporteRiesgo.add(panelNotasRiesgo, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("   Reporte de Riesgo ", panelReporteRiesgo);

        panelCentral.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        panelPrincipal.add(panelCentral, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout panelInferiorLayout = new javax.swing.GroupLayout(panelInferior);
        panelInferior.setLayout(panelInferiorLayout);
        panelInferiorLayout.setHorizontalGroup(
            panelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        panelInferiorLayout.setVerticalGroup(
            panelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        panelPrincipal.add(panelInferior, java.awt.BorderLayout.SOUTH);

        add(panelPrincipal, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void cboGradosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGradosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboGradosActionPerformed

    private void cboCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCursoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCursoActionPerformed

    private void cboBimestreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBimestreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboBimestreActionPerformed

    private void cboGradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGradoActionPerformed

    }//GEN-LAST:event_cboGradoActionPerformed

    private void cboBimestre1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBimestre1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboBimestre1ActionPerformed

    private void btnCargarAlumnosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarAlumnosActionPerformed
     if (notasController == null) return;
 
        int idxG = cboGrados.getSelectedIndex();
        int idxC = cboCurso.getSelectedIndex();
        int idxB = cboBimestre.getSelectedIndex();
 
        if (idxG <= 0 || idxC <= 0 || idxB <= 0) {
            mostrarMensaje("Seleccione Grado, Curso y Bimestre.", "Filtros", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        Integer idGrado = gradosCargados.get(idxG - 1).getId();
        Integer idCurso = cursosCargados.get(idxC - 1).getId();
 
        notasController.procesarCargaAlumnos(this, idGrado, idCurso, idxB);
    }//GEN-LAST:event_btnCargarAlumnosActionPerformed
    
    private void btnGuardarNotasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarNotasActionPerformed
        if (notasController == null) return;
 
        DefaultTableModel modelo = (DefaultTableModel) tablaNotas.getModel();
        if (modelo.getRowCount() == 0) return;
 
        List<RegistroBimestral> modificados = new ArrayList<>();
        try {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                RegistroBimestral rb = new RegistroBimestral();
 
                rb.setId(idsRegistrosCargados.get(i));
                rb.setActivo(true);
 
                // FIX: antes los pesos (0.2/0.2/0.3/0.3) estaban hardcodeados
                // aquí, duplicando la misma regla de negocio que ya vive en
                // RegistroBimestralController.obtenerPesoPorTipo(). Estos
                // objetos Evaluacion son transitorios (solo se usan para
                // llevar tipo+nota al controller; el peso real que se
                // persiste sale del controller), pero mantenerlos coherentes
                // con la única fuente de verdad evita divergencias futuras
                // si el esquema de ponderación cambia.
                rb.getEvaluaciones().add(new Evaluacion(null, null, "PRACTICA",
                        shared.TipoEvaluacion.PRACTICA, Double.parseDouble(modelo.getValueAt(i, 2).toString()),
                        notasController.obtenerPesoPorTipo(shared.TipoEvaluacion.PRACTICA)));
                rb.getEvaluaciones().add(new Evaluacion(null, null, "TAREA",
                        shared.TipoEvaluacion.TAREA, Double.parseDouble(modelo.getValueAt(i, 3).toString()),
                        notasController.obtenerPesoPorTipo(shared.TipoEvaluacion.TAREA)));
                rb.getEvaluaciones().add(new Evaluacion(null, null, "PARCIAL",
                        shared.TipoEvaluacion.PARCIAL, Double.parseDouble(modelo.getValueAt(i, 4).toString()),
                        notasController.obtenerPesoPorTipo(shared.TipoEvaluacion.PARCIAL)));
                rb.getEvaluaciones().add(new Evaluacion(null, null, "BIMESTRAL",
                        shared.TipoEvaluacion.BIMESTRAL, Double.parseDouble(modelo.getValueAt(i, 5).toString()),
                        notasController.obtenerPesoPorTipo(shared.TipoEvaluacion.BIMESTRAL)));
 
                modificados.add(rb);
            }
            notasController.procesarActualizacionMasivaNotas(modificados, this);
            btnCargarAlumnosActionPerformed(null);
        } catch (Exception e)   {
            mostrarMensaje("Error al procesar el guardado masivo: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarNotasActionPerformed

    private void btnGenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteActionPerformed
        if (notasController == null) return;
 
        int indexGrado = cboGrado.getSelectedIndex();
        int indexBimestre = cboBimestre1.getSelectedIndex();
 
        if (indexGrado <= 0 || indexBimestre <= 0) {
            mostrarMensaje("Seleccione Grado y Bimestre para el reporte analítico.", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        Integer idGradoReal = gradosCargados.get(indexGrado - 1).getId();
        notasController.procesarGeneracionReporte(this, idGradoReal, indexBimestre);
    }//GEN-LAST:event_btnGenerarReporteActionPerformed
    
    public javax.swing.JTable getTablaNotas() { 
        return tablaNotas; 
    }
 
    public javax.swing.JTable getTablaNotasRiesgo() { 
        return tablaNotasRiesgo;
    }
 
    public List<Integer> getIdsRegistrosCargados() { 
        return idsRegistrosCargados; 
    }
 
    public void mostrarMensaje(String msg, String tit, int tipo) { 
        javax.swing.JOptionPane.showMessageDialog(this, msg, tit, tipo); 
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCargarAlumnos;
    private javax.swing.JButton btnGenerarReporte;
    private javax.swing.JButton btnGuardarNotas;
    private javax.swing.JComboBox<String> cboBimestre;
    private javax.swing.JComboBox<String> cboBimestre1;
    private javax.swing.JComboBox<String> cboCurso;
    private javax.swing.JComboBox<String> cboGrado;
    private javax.swing.JComboBox<String> cboGrados;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblBimestre;
    private javax.swing.JLabel lblBimestre1;
    private javax.swing.JLabel lblCurso;
    private javax.swing.JLabel lblGrado;
    private javax.swing.JLabel lblGrado1;
    private javax.swing.JLabel nombrePanel;
    private javax.swing.JPanel panelBimestre;
    private javax.swing.JPanel panelBimestre1;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JPanel panelCabecera1;
    private javax.swing.JPanel panelCentral;
    private javax.swing.JPanel panelCurso;
    private javax.swing.JPanel panelGrado;
    private javax.swing.JPanel panelInferior;
    private javax.swing.JPanel panelNombre;
    private javax.swing.JPanel panelNotasRiesgo;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelRegistroNotas;
    private javax.swing.JPanel panelReporteRiesgo;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JTable tablaNotas;
    private javax.swing.JTable tablaNotasRiesgo;
    // End of variables declaration//GEN-END:variables
}
