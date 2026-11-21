package view.futbol;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import controller.FutbolController;

/**
 * Panel principal del modulo de futbol (reemplaza al PanelFutbol anterior).
 * Es un JPanel, no una ventana: asi el Main general lo puede meter en su propio
 * JFrame o en una pestaña junto a basquet, voley y tenis de mesa.
 */
public class PanelFutbol extends JPanel {

    private final FutbolController controller;
    private final JLabel lblTorneo = new JLabel();
    private final PanelEquipos panelEquipos;
    private final PanelPartidos panelPartidos;
    private final PanelTabla panelTabla;
    private final JTabbedPane pestanas = new JTabbedPane();

    public PanelFutbol() {
        this(new FutbolController());
    }

    public PanelFutbol(FutbolController controller) {
        this.controller = controller;
        this.panelEquipos = new PanelEquipos(controller);
        this.panelPartidos = new PanelPartidos(controller);
        this.panelTabla = new PanelTabla(controller);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 640));
        add(crearBarraSuperior(), BorderLayout.NORTH);

        pestanas.addTab("Equipos y jugadores", panelEquipos);
        pestanas.addTab("Partidos y resultados", panelPartidos);
        pestanas.addTab("Tabla de posiciones", panelTabla);
        pestanas.addChangeListener(e -> refrescarSeleccionada());
        add(pestanas, BorderLayout.CENTER);

        refrescarTodo();
    }

    private JPanel crearBarraSuperior() {
        lblTorneo.setFont(lblTorneo.getFont().deriveFont(Font.BOLD, 18f));

        JButton btnNuevo = new JButton("Nuevo torneo");
        JButton btnEjemplo = new JButton("Cargar datos de ejemplo");
        btnNuevo.addActionListener(e -> nuevoTorneo());
        btnEjemplo.addActionListener(e -> cargarEjemplo());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        botones.add(btnEjemplo);
        botones.add(btnNuevo);

        JPanel barra = new JPanel(new BorderLayout());
        barra.setBorder(BorderFactory.createEmptyBorder(10, 14, 0, 14));
        barra.add(lblTorneo, BorderLayout.WEST);
        barra.add(botones, BorderLayout.EAST);
        return barra;
    }

    private void nuevoTorneo() {
        String nombre = JOptionPane.showInputDialog(this,
                "Nombre del nuevo torneo (se perderán los datos actuales):",
                "Nuevo torneo", JOptionPane.QUESTION_MESSAGE);
        if (nombre == null) {
            return; // cancelado
        }
        try {
            controller.nuevoTorneo(nombre);
            refrescarTodo();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtil.aviso(this, ex);
        }
    }

    private void cargarEjemplo() {
        try {
            controller.cargarDatosEjemplo();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UiUtil.aviso(this, ex);
        }
        refrescarTodo();
    }

    /** Recarga todas las pestañas desde el controlador. */
    public void refrescarTodo() {
        lblTorneo.setText(controller.getNombreTorneo());
        panelEquipos.refrescar();
        panelPartidos.refrescar();
        panelTabla.refrescar();
    }

    /** Al cambiar de pestaña se recarga solo la que se va a mostrar. */
    private void refrescarSeleccionada() {
        switch (pestanas.getSelectedIndex()) {
            case 0:
                panelEquipos.refrescar();
                break;
            case 1:
                panelPartidos.refrescar();
                break;
            default:
                panelTabla.refrescar();
                break;
        }
    }
}