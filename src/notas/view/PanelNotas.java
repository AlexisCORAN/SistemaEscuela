/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package notas.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import notas.controller.RegistroBimestralController;
import plan_estudios.controller.PlanEstudiosController;
import plan_estudios.model.Curso;
import plan_estudios.model.Grado;

/**
 *
 * @author Alexis
 */
public class PanelNotas extends javax.swing.JPanel {

    private final RegistroBimestralController notasController;
    private final PlanEstudiosController planController;
    
    private List<Grado> gradosCargados = new ArrayList<>();
    private List<Curso> cursosCargados = new ArrayList<>();
    private List<Integer> idsRegistrosCargados = new ArrayList<>();

    /**
     * Creates new form PanelAlumnos
     */
    public PanelNotas() {
        this.notasController = null;
        this.planController = null;
        initComponents();
        estilizarCabecerasTablas(); 
    }
    
    public PanelNotas(RegistroBimestralController notasController, PlanEstudiosController planController) {
        this.notasController = notasController;
        this.planController = planController;
        initComponents();
        estilizarCabecerasTablas();
        if (this.notasController != null && this.planController != null) {
            cargarFiltrosGrado();
        }
    }
    
    private void cargarFiltrosGrado() {
        this.gradosCargados.clear();
        cboGradosRegistro.removeAllItems();
        cboGradosRegistro.addItem("-- Seleccione Grado --");
        cboGradoReporte.removeAllItems();
        cboGradoReporte.addItem("-- Seleccione Grado --");
        
        try {
            List<Grado> activos = planController.obtenerGradosActivos(); 
            for (Grado g : activos) {
                this.gradosCargados.add(g);
                cboGradosRegistro.addItem(g.getNombre() + " - " + g.getNivel());
                cboGradoReporte.addItem(g.getNombre() + " - " + g.getNivel());
            }
        } catch (Exception e) {
            mostrarMensaje("Error al cargar grados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarCursosPorGrado(int idGrado) {
        this.cursosCargados.clear();
        cboCursoRegistro.removeAllItems();
        cboCursoRegistro.addItem("-- Seleccione Curso --");
        cboCursoRegistro.setEnabled(true);
        btnCargarAlumnos.setEnabled(true);
        cboBimestreRegistro.setEnabled(true);

        try {
            List<Curso> todos = planController.obtenerCursos(); 
            for (Curso c : todos) {
                if (c.getGradoAsignado() != null && c.getGradoAsignado().getId() == idGrado) {
                    this.cursosCargados.add(c);
                    cboCursoRegistro.addItem(c.getNombre());
                }
            }
        } catch (Exception e) {
            mostrarMensaje("Error al cargar cursos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void estilizarCabecerasTablas() {
        javax.swing.table.JTableHeader header1 = tablaNotas.getTableHeader();
        header1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        header1.setBackground(new java.awt.Color(245, 245, 245));
        header1.setOpaque(true);
        header1.setReorderingAllowed(false);

        javax.swing.table.JTableHeader header2 = tablaNotasRiesgo.getTableHeader();
        header2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        header2.setBackground(new java.awt.Color(245, 245, 245));
        header2.setOpaque(true);
        header2.setReorderingAllowed(false);
        jScrollPane1.getViewport().setBackground(java.awt.Color.WHITE);
        jScrollPane2.getViewport().setBackground(java.awt.Color.WHITE);
    }
    
    public void recargarDatos() {
        if (notasController == null) return;
        int idxG = cboGradosRegistro.getSelectedIndex();
        int idxC = cboCursoRegistro.getSelectedIndex();
        int idxB = cboBimestreRegistro.getSelectedIndex();

        if (idxG > 0 && idxC > 0 && idxB > 0) {
            btnCargarAlumnosActionPerformed(null);
        }
    }

    public void refrescarTabla(List<Object[]> datos) {
        DefaultTableModel modelo = (DefaultTableModel) tablaNotas.getModel();
        modelo.setRowCount(0);
        idsRegistrosCargados.clear();

        btnAgregarNotas.setText("Agregar Notas");
        btnCerrarBimestre.setEnabled(false); 

        for (Object[] fila : datos) {
            idsRegistrosCargados.add((Integer) fila[0]);

            modelo.addRow(new Object[]{ 
                fila[1], 
                fila[2], 
                fila[3],
                fila[4],
                fila[5], 
                fila[6], 
                fila[7], 
                fila[8]
            });
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
        cboGradosRegistro = new javax.swing.JComboBox<>();
        panelCurso = new javax.swing.JPanel();
        lblCurso = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        cboCursoRegistro = new javax.swing.JComboBox<>();
        panelBimestre = new javax.swing.JPanel();
        lblBimestre = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        cboBimestreRegistro = new javax.swing.JComboBox<>();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(40, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        btnCerrarBimestre = new javax.swing.JButton();
        filler12 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        btnAgregarNotas = new javax.swing.JButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        btnCargarAlumnos = new javax.swing.JButton();
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(105, 0));
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaNotas = new javax.swing.JTable();
        panelReporteRiesgo = new javax.swing.JPanel();
        panelCabecera1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        lblGrado1 = new javax.swing.JLabel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        cboGradoReporte = new javax.swing.JComboBox<>();
        panelCurso1 = new javax.swing.JPanel();
        lblCurso1 = new javax.swing.JLabel();
        filler11 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        cboCursoReporte = new javax.swing.JComboBox<>();
        panelBimestre1 = new javax.swing.JPanel();
        lblBimestre1 = new javax.swing.JLabel();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        cboBimestreReporte = new javax.swing.JComboBox<>();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        btnGenerarReporte = new javax.swing.JButton();
        panelNotasRiesgo = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaNotasRiesgo = new javax.swing.JTable();
        panelInferior = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setMaximumSize(new java.awt.Dimension(200, 35));
        setMinimumSize(new java.awt.Dimension(200, 35));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(200, 35));
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

        panelCentral.setOpaque(false);
        panelCentral.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        panelRegistroNotas.setBackground(new java.awt.Color(255, 255, 255));
        panelRegistroNotas.setLayout(new java.awt.BorderLayout());

        panelCabecera.setOpaque(false);
        panelCabecera.setLayout(new javax.swing.BoxLayout(panelCabecera, javax.swing.BoxLayout.LINE_AXIS));

        panelGrado.setOpaque(false);
        panelGrado.setLayout(new javax.swing.BoxLayout(panelGrado, javax.swing.BoxLayout.LINE_AXIS));

        lblGrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGrado.setText("Grado");
        lblGrado.setMaximumSize(new java.awt.Dimension(50, 30));
        lblGrado.setMinimumSize(new java.awt.Dimension(50, 30));
        lblGrado.setPreferredSize(new java.awt.Dimension(50, 30));
        panelGrado.add(lblGrado);
        panelGrado.add(filler1);

        cboGradosRegistro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione Grado --" }));
        cboGradosRegistro.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 220, 220), 1, true));
        cboGradosRegistro.setMaximumSize(new java.awt.Dimension(150, 35));
        cboGradosRegistro.setMinimumSize(new java.awt.Dimension(150, 35));
        cboGradosRegistro.setPreferredSize(new java.awt.Dimension(150, 35));
        cboGradosRegistro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboGradosRegistroItemStateChanged(evt);
            }
        });
        cboGradosRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGradosRegistroActionPerformed(evt);
            }
        });
        panelGrado.add(cboGradosRegistro);

        panelCabecera.add(panelGrado);

        panelCurso.setOpaque(false);
        panelCurso.setLayout(new javax.swing.BoxLayout(panelCurso, javax.swing.BoxLayout.LINE_AXIS));

        lblCurso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurso.setText("Curso");
        lblCurso.setMaximumSize(new java.awt.Dimension(50, 30));
        lblCurso.setMinimumSize(new java.awt.Dimension(50, 30));
        lblCurso.setPreferredSize(new java.awt.Dimension(50, 30));
        panelCurso.add(lblCurso);
        panelCurso.add(filler3);

        cboCursoRegistro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione Curso --" }));
        cboCursoRegistro.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 220, 220), 1, true));
        cboCursoRegistro.setEnabled(false);
        cboCursoRegistro.setMaximumSize(new java.awt.Dimension(120, 35));
        cboCursoRegistro.setMinimumSize(new java.awt.Dimension(120, 35));
        cboCursoRegistro.setPreferredSize(new java.awt.Dimension(120, 35));
        cboCursoRegistro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCursoRegistroItemStateChanged(evt);
            }
        });
        cboCursoRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCursoRegistroActionPerformed(evt);
            }
        });
        panelCurso.add(cboCursoRegistro);

        panelCabecera.add(panelCurso);

        panelBimestre.setOpaque(false);
        panelBimestre.setLayout(new javax.swing.BoxLayout(panelBimestre, javax.swing.BoxLayout.LINE_AXIS));

        lblBimestre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBimestre.setText("Bimestre");
        lblBimestre.setMaximumSize(new java.awt.Dimension(70, 30));
        lblBimestre.setMinimumSize(new java.awt.Dimension(70, 30));
        lblBimestre.setPreferredSize(new java.awt.Dimension(70, 30));
        panelBimestre.add(lblBimestre);
        panelBimestre.add(filler6);

        cboBimestreRegistro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione Bimestre --", "Bimestre 1", "Bimestre 2", "Bimestre 3", "Bimestre 4" }));
        cboBimestreRegistro.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 220, 220), 1, true));
        cboBimestreRegistro.setEnabled(false);
        cboBimestreRegistro.setMaximumSize(new java.awt.Dimension(100, 35));
        cboBimestreRegistro.setMinimumSize(new java.awt.Dimension(100, 35));
        cboBimestreRegistro.setPreferredSize(new java.awt.Dimension(100, 35));
        cboBimestreRegistro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboBimestreRegistroItemStateChanged(evt);
            }
        });
        cboBimestreRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBimestreRegistroActionPerformed(evt);
            }
        });
        panelBimestre.add(cboBimestreRegistro);

        panelCabecera.add(panelBimestre);
        panelCabecera.add(filler2);

        btnCerrarBimestre.setText("Cerrar Bimestre");
        btnCerrarBimestre.setMaximumSize(new java.awt.Dimension(120, 35));
        btnCerrarBimestre.setMinimumSize(new java.awt.Dimension(120, 35));
        btnCerrarBimestre.setPreferredSize(new java.awt.Dimension(120, 35));
        btnCerrarBimestre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarBimestreActionPerformed(evt);
            }
        });
        panelCabecera.add(btnCerrarBimestre);
        panelCabecera.add(filler12);

        btnAgregarNotas.setText("Agregar Notas");
        btnAgregarNotas.setMaximumSize(new java.awt.Dimension(120, 35));
        btnAgregarNotas.setMinimumSize(new java.awt.Dimension(120, 35));
        btnAgregarNotas.setPreferredSize(new java.awt.Dimension(120, 35));
        btnAgregarNotas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarNotasMouseClicked(evt);
            }
        });
        btnAgregarNotas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarNotasActionPerformed(evt);
            }
        });
        panelCabecera.add(btnAgregarNotas);
        panelCabecera.add(filler5);

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
        panelCabecera.add(filler10);

        panelRegistroNotas.add(panelCabecera, java.awt.BorderLayout.NORTH);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setOpaque(false);

        tablaNotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Alumno", "Prom. Calificada", "Prom.Tarea académica", "Ex. Parcial", "Ex. Bimestral", "Prom. Bimestre", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaNotas.setFillsViewportHeight(true);
        tablaNotas.setGridColor(new java.awt.Color(230, 230, 230));
        tablaNotas.setOpaque(false);
        tablaNotas.setRowHeight(50);
        tablaNotas.setShowGrid(false);
        tablaNotas.setShowHorizontalLines(true);
        tablaNotas.getTableHeader().setReorderingAllowed(false);
        tablaNotas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaNotasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaNotas);
        if (tablaNotas.getColumnModel().getColumnCount() > 0) {
            tablaNotas.getColumnModel().getColumn(0).setResizable(false);
            tablaNotas.getColumnModel().getColumn(1).setResizable(false);
            tablaNotas.getColumnModel().getColumn(2).setResizable(false);
            tablaNotas.getColumnModel().getColumn(3).setResizable(false);
            tablaNotas.getColumnModel().getColumn(4).setResizable(false);
            tablaNotas.getColumnModel().getColumn(5).setResizable(false);
            tablaNotas.getColumnModel().getColumn(6).setResizable(false);
            tablaNotas.getColumnModel().getColumn(7).setResizable(false);
        }

        panelRegistroNotas.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("   Registro de Notas   ", panelRegistroNotas);

        panelReporteRiesgo.setBackground(new java.awt.Color(255, 255, 255));
        panelReporteRiesgo.setOpaque(false);
        panelReporteRiesgo.setLayout(new java.awt.BorderLayout());

        panelCabecera1.setOpaque(false);
        panelCabecera1.setLayout(new javax.swing.BoxLayout(panelCabecera1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel9.setOpaque(false);
        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        lblGrado1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGrado1.setText("Grado");
        lblGrado1.setMaximumSize(new java.awt.Dimension(50, 30));
        lblGrado1.setMinimumSize(new java.awt.Dimension(50, 30));
        lblGrado1.setPreferredSize(new java.awt.Dimension(50, 30));
        jPanel9.add(lblGrado1);
        jPanel9.add(filler7);

        cboGradoReporte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboGradoReporte.setMaximumSize(new java.awt.Dimension(70, 35));
        cboGradoReporte.setMinimumSize(new java.awt.Dimension(70, 35));
        cboGradoReporte.setPreferredSize(new java.awt.Dimension(70, 35));
        cboGradoReporte.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboGradoReporteItemStateChanged(evt);
            }
        });
        cboGradoReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGradoReporteActionPerformed(evt);
            }
        });
        jPanel9.add(cboGradoReporte);

        panelCabecera1.add(jPanel9);

        panelCurso1.setOpaque(false);
        panelCurso1.setLayout(new javax.swing.BoxLayout(panelCurso1, javax.swing.BoxLayout.LINE_AXIS));

        lblCurso1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurso1.setText("Curso");
        lblCurso1.setMaximumSize(new java.awt.Dimension(50, 30));
        lblCurso1.setMinimumSize(new java.awt.Dimension(50, 30));
        lblCurso1.setPreferredSize(new java.awt.Dimension(50, 30));
        panelCurso1.add(lblCurso1);
        panelCurso1.add(filler11);

        cboCursoReporte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCursoReporte.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 220, 220), 1, true));
        cboCursoReporte.setEnabled(false);
        cboCursoReporte.setMaximumSize(new java.awt.Dimension(70, 35));
        cboCursoReporte.setMinimumSize(new java.awt.Dimension(70, 35));
        cboCursoReporte.setPreferredSize(new java.awt.Dimension(70, 35));
        cboCursoReporte.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCursoReporteItemStateChanged(evt);
            }
        });
        cboCursoReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCursoReporteActionPerformed(evt);
            }
        });
        panelCurso1.add(cboCursoReporte);

        panelCabecera1.add(panelCurso1);

        panelBimestre1.setOpaque(false);
        panelBimestre1.setLayout(new javax.swing.BoxLayout(panelBimestre1, javax.swing.BoxLayout.LINE_AXIS));

        lblBimestre1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBimestre1.setText("Bimestre");
        lblBimestre1.setMaximumSize(new java.awt.Dimension(70, 30));
        lblBimestre1.setMinimumSize(new java.awt.Dimension(70, 30));
        lblBimestre1.setPreferredSize(new java.awt.Dimension(70, 30));
        panelBimestre1.add(lblBimestre1);
        panelBimestre1.add(filler8);

        cboBimestreReporte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione Bimestre --", "Bimestre 1", "Bimestre 2", "Bimestre 3", "Bimestre 4" }));
        cboBimestreReporte.setEnabled(false);
        cboBimestreReporte.setMaximumSize(new java.awt.Dimension(70, 35));
        cboBimestreReporte.setMinimumSize(new java.awt.Dimension(50, 35));
        cboBimestreReporte.setPreferredSize(new java.awt.Dimension(70, 35));
        cboBimestreReporte.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboBimestreReporteItemStateChanged(evt);
            }
        });
        cboBimestreReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBimestreReporteActionPerformed(evt);
            }
        });
        panelBimestre1.add(cboBimestreReporte);

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

        panelNotasRiesgo.setOpaque(false);
        panelNotasRiesgo.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane2.setOpaque(false);

        tablaNotasRiesgo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
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
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaNotasRiesgo.setFillsViewportHeight(true);
        tablaNotasRiesgo.setGridColor(new java.awt.Color(230, 230, 230));
        tablaNotasRiesgo.setRowHeight(50);
        tablaNotasRiesgo.setShowGrid(false);
        tablaNotasRiesgo.setShowHorizontalLines(true);
        tablaNotasRiesgo.getTableHeader().setReorderingAllowed(false);
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
            .addGap(0, 974, Short.MAX_VALUE)
        );
        panelInferiorLayout.setVerticalGroup(
            panelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        panelPrincipal.add(panelInferior, java.awt.BorderLayout.SOUTH);

        add(panelPrincipal, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void cboGradosRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGradosRegistroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboGradosRegistroActionPerformed

    private void cboCursoRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCursoRegistroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCursoRegistroActionPerformed

    private void cboBimestreRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBimestreRegistroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboBimestreRegistroActionPerformed

    private void cboGradoReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGradoReporteActionPerformed
        
    }//GEN-LAST:event_cboGradoReporteActionPerformed

    private void cboBimestreReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBimestreReporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboBimestreReporteActionPerformed

    private void btnCargarAlumnosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarAlumnosActionPerformed
        if (notasController == null) return;
 
        int idxG = cboGradosRegistro.getSelectedIndex();
        int idxC = cboCursoRegistro.getSelectedIndex();
        int idxB = cboBimestreRegistro.getSelectedIndex();

        if (idxG <= 0 || idxC <= 0 || idxB <= 0) {
            mostrarMensaje("Seleccione un Grado, Curso y Bimestre válidos.", "Filtros", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer idGrado = gradosCargados.get(idxG - 1).getId();
        Integer idCurso = cursosCargados.get(idxC - 1).getId();

        try {
            List<Object[]> filasProcesadas = notasController.obtenerNotasParaGrilla(idGrado, idCurso, idxB);
            refrescarTabla(filasProcesadas);
        } catch (Exception ex) {
            mostrarMensaje("Error al cargar alumnos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCargarAlumnosActionPerformed
    
    private void btnCerrarBimestreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarBimestreActionPerformed
        int fila = tablaNotas.getSelectedRow();
        if (fila == -1) {
            mostrarMensaje("Seleccione un alumno primero.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Integer idRegistro = idsRegistrosCargados.get(fila);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de cerrar este bimestre?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                notasController.cerrarBimestre(idRegistro);
                mostrarMensaje("Bimestre cerrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                btnCargarAlumnosActionPerformed(null);
            } catch (Exception e) {
                mostrarMensaje("Error al cerrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnCerrarBimestreActionPerformed

    private void btnGenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteActionPerformed
        int indexGrado = cboGradoReporte.getSelectedIndex();
    int indexBimestre = cboBimestreReporte.getSelectedIndex();

    if (indexGrado <= 0 || indexBimestre <= 0) {
        mostrarMensaje("Seleccione un Grado y un Bimestre para generar el reporte.", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    Integer idGradoReal = gradosCargados.get(indexGrado - 1).getId();
    
    try {
        List<Object[]> reporteRiesgo = notasController.obtenerReporteRiesgo(idGradoReal, indexBimestre);
        DefaultTableModel modeloRiesgo = (DefaultTableModel) tablaNotasRiesgo.getModel();
        modeloRiesgo.setRowCount(0);
        
        if (reporteRiesgo.isEmpty()) {
            mostrarMensaje("¡Excelente! No hay alumnos en riesgo académico para este grado y bimestre.", "Reporte Limpio", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (Object[] fila : reporteRiesgo) {
            modeloRiesgo.addRow(new Object[]{ fila[0], fila[1], fila[2], fila[3], "CRÍTICO" });
        }
    } catch (Exception ex) {
        mostrarMensaje("Error al generar el reporte: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnGenerarReporteActionPerformed

    private void cboGradosRegistroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboGradosRegistroItemStateChanged
            if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (cboGradosRegistro.getSelectedIndex() > 0) {
                int idGrado = gradosCargados.get(cboGradosRegistro.getSelectedIndex() - 1).getId();
                cargarCursosPorGrado(idGrado);
            } else {
                cboCursoRegistro.setEnabled(false);
                cboBimestreRegistro.setEnabled(false);
                btnCargarAlumnos.setEnabled(false);
            }
        }
    }//GEN-LAST:event_cboGradosRegistroItemStateChanged

    private void cboCursoRegistroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCursoRegistroItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCursoRegistroItemStateChanged

    private void cboBimestreRegistroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboBimestreRegistroItemStateChanged
        boolean listo = cboGradosRegistro.getSelectedIndex() > 0 && cboCursoRegistro.getSelectedIndex() > 0 && cboBimestreRegistro.getSelectedIndex() > 0;
        btnCargarAlumnos.setEnabled(listo);
    }//GEN-LAST:event_cboBimestreRegistroItemStateChanged

    private void cboGradoReporteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboGradoReporteItemStateChanged
       if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
           cboBimestreReporte.setEnabled(cboGradoReporte.getSelectedIndex() > 0);
       }
    }//GEN-LAST:event_cboGradoReporteItemStateChanged

    private void cboBimestreReporteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboBimestreReporteItemStateChanged
       boolean listo = cboGradoReporte.getSelectedIndex() > 0 && cboBimestreReporte.getSelectedIndex() > 0;
       btnGenerarReporte.setEnabled(listo);
    }//GEN-LAST:event_cboBimestreReporteItemStateChanged

    private void tablaNotasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaNotasMouseClicked
        int fila = tablaNotas.getSelectedRow();
        if (fila == -1) return;

        Object valorEstado = tablaNotas.getValueAt(fila, 7);
        String estado = (valorEstado != null) ? valorEstado.toString() : "ABIERTO";
        if ("CERRADO".equalsIgnoreCase(estado)) {
            btnAgregarNotas.setText("Ver Notas");
            btnCerrarBimestre.setEnabled(false);
        } else {
            btnAgregarNotas.setText("Agregar Notas");
            btnCerrarBimestre.setEnabled(true);
        }
    }//GEN-LAST:event_tablaNotasMouseClicked

    private void btnAgregarNotasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarNotasActionPerformed
        int fila = tablaNotas.getSelectedRow();
        if (fila == -1) {
            mostrarMensaje("Seleccione un alumno primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreAlumno = tablaNotas.getValueAt(fila, 1).toString();
        Integer idRegistro = idsRegistrosCargados.get(fila);
        String curso = cboCursoRegistro.getSelectedItem().toString();
        String bimestre = cboBimestreRegistro.getSelectedItem().toString();

        String estadoStr = tablaNotas.getValueAt(fila, 7).toString();
        boolean esActivo = "ABIERTO".equalsIgnoreCase(estadoStr);

        java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);

        DialogDetalleNotas dialog = new DialogDetalleNotas(
                (java.awt.Frame) parentWindow, true, this.notasController,
                idRegistro, nombreAlumno, curso, bimestre, esActivo
        );
        dialog.setVisible(true);
        btnCargarAlumnosActionPerformed(null);
    }//GEN-LAST:event_btnAgregarNotasActionPerformed

    private void cboCursoReporteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCursoReporteItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCursoReporteItemStateChanged

    private void cboCursoReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCursoReporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCursoReporteActionPerformed

    private void btnAgregarNotasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarNotasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregarNotasMouseClicked
    
    public javax.swing.JTable getTablaNotas() { 
        return tablaNotas; 
    }
 
    public javax.swing.JTable getTablaNotasRiesgo() { 
        return tablaNotasRiesgo;
    }
    
    public List<Integer> getIdsRegistrosCargados() {
        return Collections.unmodifiableList(idsRegistrosCargados);
    }
 
    public void mostrarMensaje(String msg, String tit, int tipo) { 
        javax.swing.JOptionPane.showMessageDialog(this, msg, tit, tipo); 
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarNotas;
    private javax.swing.JButton btnCargarAlumnos;
    private javax.swing.JButton btnCerrarBimestre;
    private javax.swing.JButton btnGenerarReporte;
    private javax.swing.JComboBox<String> cboBimestreRegistro;
    private javax.swing.JComboBox<String> cboBimestreReporte;
    private javax.swing.JComboBox<String> cboCursoRegistro;
    private javax.swing.JComboBox<String> cboCursoReporte;
    private javax.swing.JComboBox<String> cboGradoReporte;
    private javax.swing.JComboBox<String> cboGradosRegistro;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler11;
    private javax.swing.Box.Filler filler12;
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
    private javax.swing.JLabel lblCurso1;
    private javax.swing.JLabel lblGrado;
    private javax.swing.JLabel lblGrado1;
    private javax.swing.JLabel nombrePanel;
    private javax.swing.JPanel panelBimestre;
    private javax.swing.JPanel panelBimestre1;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JPanel panelCabecera1;
    private javax.swing.JPanel panelCentral;
    private javax.swing.JPanel panelCurso;
    private javax.swing.JPanel panelCurso1;
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
