package view.futbol;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controller.FutbolController;
import model.Equipo;
import model.futbol.EquipoFutbol;
import model.futbol.EventoPartido;
import model.futbol.JugadorFutbol;
import model.futbol.PartidoFutbol;

/** Pestaña 2: fixture, resultados y eventos (goles y tarjetas) de cada partido. */
class PanelPartidos extends JPanel {

    /** Elemento del combo de jugadores: muestra equipo + jugador. */
    private static class ItemJugador {
        final JugadorFutbol jugador;
        final String equipo;

        ItemJugador(JugadorFutbol jugador, String equipo) {
            this.jugador = jugador;
            this.equipo = equipo;
        }

        @Override
        public String toString() {
            return equipo + " - " + jugador.getNombre() + " (#" + jugador.getNumeroCamiseta() + ")";
        }
    }

    private final FutbolController controller;

    private final JButton btnFixture = new JButton("Generar fixture");
    private final DefaultTableModel modeloPartidos = UiUtil.modeloTabla(
            "#", "Equipo A", "Goles A", "Goles B", "Equipo B", "Estado");
    private final JTable tabla = new JTable(modeloPartidos);

    // Resultado
    private final JLabel lblEquipoA = new JLabel("Equipo A");
    private final JLabel lblEquipoB = new JLabel("Equipo B");
    private final JSpinner spGolesA = UiUtil.spinner(0, 0, 99);
    private final JSpinner spGolesB = UiUtil.spinner(0, 0, 99);
    private final JButton btnResultado = new JButton("Guardar resultado");

    // Eventos
    private final JComboBox<EventoPartido.Tipo> cboTipo = new JComboBox<>(EventoPartido.Tipo.values());
    private final DefaultComboBoxModel<ItemJugador> modeloJugadores = new DefaultComboBoxModel<>();
    private final JComboBox<ItemJugador> cboJugador = new JComboBox<>(modeloJugadores);
    private final JSpinner spMinuto = UiUtil.spinner(1, 0, 130);
    private final JButton btnEvento = new JButton("Agregar evento");
    private final DefaultListModel<EventoPartido> modeloEventos = new DefaultListModel<>();
    private final JList<EventoPartido> listaEventos = new JList<>(modeloEventos);

    PanelPartidos(FutbolController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.add(btnFixture);
        barra.add(new JLabel("Formato liga: todos contra todos, una sola vuelta."));
        add(barra, BorderLayout.NORTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        UiUtil.centrar(tabla, 0, 2, 3, 5);

        JPanel inferior = new JPanel(new GridLayout(1, 2, 10, 0));
        inferior.add(crearPanelResultado());
        inferior.add(crearPanelEventos());

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tabla), inferior);
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarDetalle();
            }
        });
        btnFixture.addActionListener(e -> generarFixture());
        btnResultado.addActionListener(e -> guardarResultado());
        btnEvento.addActionListener(e -> agregarEvento());
        actualizarDetalle();
    }

    private JPanel crearPanelResultado() {
        JPanel form = UiUtil.formulario();
        form.setBorder(BorderFactory.createTitledBorder("Resultado del partido seleccionado"));
        UiUtil.fila(form, 0, lblEquipoA, spGolesA);
        UiUtil.fila(form, 1, lblEquipoB, spGolesB);
        UiUtil.filaBoton(form, 2, btnResultado);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        return panel;
    }

    private JPanel crearPanelEventos() {
        JPanel form = UiUtil.formulario();
        UiUtil.fila(form, 0, "Tipo", cboTipo);
        UiUtil.fila(form, 1, "Jugador", cboJugador);
        UiUtil.fila(form, 2, "Minuto", spMinuto);
        UiUtil.filaBoton(form, 3, btnEvento);

        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Eventos del partido (goles y tarjetas)"));
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaEventos), BorderLayout.CENTER);
        return panel;
    }

    // ---------- Acciones ----------

    private void generarFixture() {
        try {
            controller.generarFixture();
            refrescar();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtil.aviso(this, ex);
        }
    }

    private void guardarResultado() {
        try {
            controller.registrarResultado(tabla.getSelectedRow(), UiUtil.valor(spGolesA), UiUtil.valor(spGolesB));
            refrescar();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtil.aviso(this, ex);
        }
    }

    private void agregarEvento() {
        try {
            ItemJugador item = (ItemJugador) cboJugador.getSelectedItem();
            if (item == null) {
                throw new IllegalArgumentException("No hay jugadores en este partido");
            }
            controller.agregarEvento(tabla.getSelectedRow(), (EventoPartido.Tipo) cboTipo.getSelectedItem(),
                    item.jugador, UiUtil.valor(spMinuto));
            actualizarDetalle();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtil.aviso(this, ex);
        }
    }

    // ---------- Refresco de la vista ----------

    /** Vuelve a cargar los partidos desde el controlador, conservando la fila seleccionada. */
    void refrescar() {
        int seleccionada = tabla.getSelectedRow();
        modeloPartidos.setRowCount(0);
        ArrayList<PartidoFutbol> partidos = controller.getPartidos();
        for (int i = 0; i < partidos.size(); i++) {
            PartidoFutbol p = partidos.get(i);
            boolean jugado = p.isJugado();
            modeloPartidos.addRow(new Object[]{
                i + 1, p.getEquipoA().getNombre(),
                jugado ? String.valueOf(p.getGolesA()) : "-",
                jugado ? String.valueOf(p.getGolesB()) : "-",
                p.getEquipoB().getNombre(), jugado ? "Jugado" : "Pendiente"});
        }
        btnFixture.setEnabled(!controller.fixtureGenerado() && controller.getEquipos().size() >= 2);
        if (seleccionada >= 0 && seleccionada < partidos.size()) {
            tabla.setRowSelectionInterval(seleccionada, seleccionada);
        } else {
            actualizarDetalle();
        }
    }

    /** Actualiza los paneles de resultado y eventos segun el partido seleccionado. */
    private void actualizarDetalle() {
        int fila = tabla.getSelectedRow();
        modeloEventos.clear();
        modeloJugadores.removeAllElements();

        ArrayList<PartidoFutbol> partidos = controller.getPartidos();
        if (fila < 0 || fila >= partidos.size()) {
            lblEquipoA.setText("Equipo A");
            lblEquipoB.setText("Equipo B");
            spGolesA.setEnabled(false);
            spGolesB.setEnabled(false);
            btnResultado.setEnabled(false);
            btnEvento.setEnabled(false);
            return;
        }

        PartidoFutbol p = partidos.get(fila);
        lblEquipoA.setText(p.getEquipoA().getNombre());
        lblEquipoB.setText(p.getEquipoB().getNombre());
        spGolesA.setValue(p.isJugado() ? p.getGolesA() : 0);
        spGolesB.setValue(p.isJugado() ? p.getGolesB() : 0);
        spGolesA.setEnabled(!p.isJugado());
        spGolesB.setEnabled(!p.isJugado());
        btnResultado.setEnabled(!p.isJugado());

        for (Equipo equipo : new Equipo[]{p.getEquipoA(), p.getEquipoB()}) {
            if (equipo instanceof EquipoFutbol) {
                for (JugadorFutbol j : ((EquipoFutbol) equipo).getPlantel()) {
                    modeloJugadores.addElement(new ItemJugador(j, equipo.getNombre()));
                }
            }
        }
        btnEvento.setEnabled(modeloJugadores.getSize() > 0);
        for (EventoPartido ev : p.getEventos()) {
            modeloEventos.addElement(ev);
        }
    }
}