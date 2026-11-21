package view.futbol;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controller.FutbolController;
import model.futbol.EquipoFutbol;
import model.futbol.JugadorFutbol;

/** Pestaña 1: registro de equipos y de sus jugadores. */
class PanelEquipos extends JPanel {

    private final FutbolController controller;

    // Equipos
    private final DefaultListModel<EquipoFutbol> modeloEquipos = new DefaultListModel<>();
    private final JList<EquipoFutbol> listaEquipos = new JList<>(modeloEquipos);
    private final JTextField txtEquipo = new JTextField(14);
    private final JSpinner spRanking = UiUtil.spinner(0, 0, 999);
    private final JSpinner spFuerza = UiUtil.spinner(0, 0, 100);
    private final JButton btnEquipo = new JButton("Registrar equipo");

    // Jugadores
    private final DefaultTableModel modeloPlantel = UiUtil.modeloTabla(
            "Camiseta", "Nombre", "ID", "Posición", "Titular", "Goles", "Amarillas", "Rojas");
    private final JTable tablaPlantel = new JTable(modeloPlantel);
    private final JScrollPane scrollPlantel = new JScrollPane(tablaPlantel);
    private final JTextField txtNombre = new JTextField(14);
    private final JTextField txtId = new JTextField(8);
    private final JComboBox<String> cboPosicion = new JComboBox<>(
            new String[]{"Portero", "Defensa", "Mediocampista", "Delantero"});
    private final JSpinner spCamiseta = UiUtil.spinner(1, 0, 99);
    private final JCheckBox chkTitular = new JCheckBox("Titular", true);
    private final JButton btnJugador = new JButton("Agregar jugador");

    PanelEquipos(FutbolController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelEquipos(), BorderLayout.WEST);
        add(crearPanelJugadores(), BorderLayout.CENTER);

        listaEquipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEquipos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                refrescarPlantel();
            }
        });
        btnEquipo.addActionListener(e -> registrarEquipo());
        btnJugador.addActionListener(e -> agregarJugador());
    }

    private JPanel crearPanelEquipos() {
        JPanel form = UiUtil.formulario();
        form.setBorder(BorderFactory.createTitledBorder("Nuevo equipo"));
        UiUtil.fila(form, 0, "Nombre", txtEquipo);
        UiUtil.fila(form, 1, "Ranking", spRanking);
        UiUtil.fila(form, 2, "Nivel de fuerza", spFuerza);
        UiUtil.filaBoton(form, 3, btnEquipo);

        JScrollPane scroll = new JScrollPane(listaEquipos);
        scroll.setBorder(BorderFactory.createTitledBorder("Equipos inscritos"));

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setPreferredSize(new Dimension(300, 0));
        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelJugadores() {
        JPanel form = UiUtil.formulario();
        form.setBorder(BorderFactory.createTitledBorder("Nuevo jugador (en el equipo seleccionado)"));
        UiUtil.fila(form, 0, "Nombre", txtNombre);
        UiUtil.fila(form, 1, "ID", txtId);
        UiUtil.fila(form, 2, "Posición", cboPosicion);
        UiUtil.fila(form, 3, "Camiseta", spCamiseta);
        UiUtil.fila(form, 4, "", chkTitular);
        UiUtil.filaBoton(form, 5, btnJugador);

        tablaPlantel.setRowHeight(24);
        UiUtil.centrar(tablaPlantel, 0, 4, 5, 6, 7);
        scrollPlantel.setBorder(BorderFactory.createTitledBorder("Plantel"));

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.add(form, BorderLayout.NORTH);
        panel.add(scrollPlantel, BorderLayout.CENTER);
        return panel;
    }

    // ---------- Acciones ----------

    private void registrarEquipo() {
        try {
            controller.registrarEquipo(txtEquipo.getText(), UiUtil.valor(spRanking), UiUtil.valor(spFuerza));
            txtEquipo.setText("");
            refrescar();
            listaEquipos.setSelectedIndex(modeloEquipos.size() - 1);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtil.aviso(this, ex);
        }
    }

    private void agregarJugador() {
        try {
            controller.agregarJugador(listaEquipos.getSelectedValue(), txtNombre.getText(), txtId.getText(),
                    (String) cboPosicion.getSelectedItem(), UiUtil.valor(spCamiseta), chkTitular.isSelected());
            txtNombre.setText("");
            txtId.setText("");
            spCamiseta.setValue(UiUtil.valor(spCamiseta) + 1);
            refrescarPlantel();
            txtNombre.requestFocusInWindow();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtil.aviso(this, ex);
        }
    }

    // ---------- Refresco de la vista ----------

    /** Vuelve a cargar los datos desde el controlador, conservando el equipo seleccionado. */
    void refrescar() {
        int seleccionado = listaEquipos.getSelectedIndex();
        modeloEquipos.clear();
        for (EquipoFutbol e : controller.getEquipos()) {
            modeloEquipos.addElement(e);
        }
        if (modeloEquipos.size() > 0) {
            listaEquipos.setSelectedIndex(
                    seleccionado >= 0 && seleccionado < modeloEquipos.size() ? seleccionado : 0);
        }
        boolean abierto = !controller.fixtureGenerado();
        btnEquipo.setEnabled(abierto);
        btnEquipo.setToolTipText(abierto ? null : "No se pueden agregar equipos después de generar el fixture");
        refrescarPlantel();
    }

    private void refrescarPlantel() {
        EquipoFutbol equipo = listaEquipos.getSelectedValue();
        modeloPlantel.setRowCount(0);
        btnJugador.setEnabled(equipo != null);
        if (equipo == null) {
            scrollPlantel.setBorder(BorderFactory.createTitledBorder("Plantel"));
            return;
        }
        scrollPlantel.setBorder(BorderFactory.createTitledBorder(
                "Plantel de " + equipo.getNombre() + " (" + equipo.getPlantel().size() + " jugadores)"));
        for (JugadorFutbol j : equipo.getPlantel()) {
            modeloPlantel.addRow(new Object[]{
                j.getNumeroCamiseta(), j.getNombre(), j.getId(), j.getPosicion(),
                j.isTitular() ? "Sí" : "No", j.getGoles(), j.getTarjetasAmarillas(), j.getTarjetasRojas()});
        }
    }
}