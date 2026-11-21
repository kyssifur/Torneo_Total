package view.tenisdemesa;

import model.tenisdemesa.PartidoTT;
import model.tenisdemesa.TorneoTenisDeMesa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Pestaña 3: sorteo de llaves, cuadro de eliminación directa y registro de resultados de la ronda. */
public class PanelLlaves extends JPanel {
    private final VentanaPrincipal app;
    private final LlavesDibujo dibujo = new LlavesDibujo();
    private final JLabel lblEstado = new JLabel(" ");
    private final JPanel barraEstado = Estilo.tarjeta(new BorderLayout(12, 0));
    private final JButton btnSorteo = Estilo.boton("Realizar sorteo", Estilo.NARANJA);
    private final JButton btnAvanzar = Estilo.boton("Avanzar ronda  ▶", Estilo.AZUL_CLARO);

    private final DefaultListModel<PartidoTT> modeloPendientes = new DefaultListModel<>();
    private final JList<PartidoTT> listaPendientes = new JList<>(modeloPendientes);
    private final JLabel lblPendientes = Estilo.subtitulo("Partidos pendientes");
    private final JButton btnRegistrar = Estilo.boton("Registrar resultado", Estilo.VERDE);

    public PanelLlaves(VentanaPrincipal app) {
        this.app = app;
        setLayout(new BorderLayout(14, 12));
        setBackground(Estilo.FONDO);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // ---- barra superior
        lblEstado.setFont(Estilo.NEGRITA);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setOpaque(false);
        botones.add(btnSorteo);
        botones.add(btnAvanzar);
        barraEstado.add(lblEstado, BorderLayout.CENTER);
        barraEstado.add(botones, BorderLayout.EAST);
        add(barraEstado, BorderLayout.NORTH);

        // ---- cuadro de llaves
        JScrollPane scroll = new JScrollPane(dibujo);
        scroll.setBorder(BorderFactory.createLineBorder(Estilo.BORDE));
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.getHorizontalScrollBar().setUnitIncrement(18);
        add(scroll, BorderLayout.CENTER);

        // ---- partidos pendientes de la ronda
        JPanel derecha = Estilo.tarjeta(new BorderLayout(0, 10));
        derecha.setPreferredSize(new Dimension(280, 0));
        derecha.add(lblPendientes, BorderLayout.NORTH);
        listaPendientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPendientes.setFixedCellHeight(34);
        listaPendientes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean sel, boolean foco) {
                JLabel c = (JLabel) super.getListCellRendererComponent(l, v, i, sel, foco);
                PartidoTT p = (PartidoTT) v;
                c.setText(p.getJugadorA().getNombre() + "  vs  " + p.getJugadorB().getNombre());
                c.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        });
        derecha.add(new JScrollPane(listaPendientes), BorderLayout.CENTER);
        JLabel ayuda = new JLabel("<html>También puedes hacer clic en un partido del cuadro (borde naranja).</html>");
        ayuda.setFont(Estilo.NORMAL);
        ayuda.setForeground(Estilo.GRIS);
        JPanel sur = new JPanel(new BorderLayout(0, 8));
        sur.setOpaque(false);
        sur.add(ayuda, BorderLayout.NORTH);
        sur.add(btnRegistrar, BorderLayout.SOUTH);
        derecha.add(sur, BorderLayout.SOUTH);
        add(derecha, BorderLayout.EAST);

        // ---- acciones
        dibujo.setAlSeleccionar(app::registrarResultado);
        btnRegistrar.addActionListener(e -> registrarSeleccionado());
        listaPendientes.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) registrarSeleccionado();
            }
        });
        btnSorteo.addActionListener(e -> realizarSorteo());
        btnAvanzar.addActionListener(e -> avanzarRonda());
    }

    private void registrarSeleccionado() {
        PartidoTT p = listaPendientes.getSelectedValue();
        if (p == null && !modeloPendientes.isEmpty()) p = modeloPendientes.get(0);
        if (p != null) app.registrarResultado(p);
    }

    private void realizarSorteo() {
        TorneoTenisDeMesa t = app.getTorneo();
        try {
            t.crearLlaves();
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se puede hacer el sorteo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        app.refrescarTodo();
        JOptionPane.showMessageDialog(this,
            "Sorteo realizado.\nClasificaron " + (t.getGrupos().size() * 2) + " jugadores (2 por grupo).\n"
            + "Partidos de la primera ronda: " + t.partidosPendientesLlaves().size() + ".",
            "Sorteo de llaves", JOptionPane.INFORMATION_MESSAGE);
    }

    private void avanzarRonda() {
        TorneoTenisDeMesa t = app.getTorneo();
        try {
            t.avanzarRonda();
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se puede avanzar",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (t.getCampeon() != null) app.torneoFinalizado();
        app.refrescarTodo();
        if (t.getCampeon() != null) {
            JOptionPane.showMessageDialog(this, "El campeón del torneo es\n" + t.getCampeon().getNombre() + "!",
                "Torneo finalizado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refrescar() {
        TorneoTenisDeMesa t = app.getTorneo();
        boolean hayGrupos = !t.getGrupos().isEmpty();
        boolean hayLlaves = !t.getLlaves().isEmpty();
        int pendientesGrupo = t.partidosPendientesGrupo().size();

        dibujo.mostrar(t);
        modeloPendientes.clear();
        for (PartidoTT p : t.partidosPendientesLlaves()) modeloPendientes.addElement(p);
        if (!modeloPendientes.isEmpty()) listaPendientes.setSelectedIndex(0);
        lblPendientes.setText("Partidos pendientes (" + modeloPendientes.size() + ")");

        btnSorteo.setEnabled(hayGrupos && !hayLlaves && pendientesGrupo == 0);
        btnAvanzar.setEnabled(hayLlaves && t.getCampeon() == null && modeloPendientes.isEmpty());
        btnRegistrar.setEnabled(!modeloPendientes.isEmpty());

        barraEstado.setBackground(Color.WHITE);
        lblEstado.setForeground(Color.BLACK);
        if (t.getCampeon() != null) {
            barraEstado.setBackground(new Color(0xFFF3C4));
            lblEstado.setText("Campeón del torneo: " + t.getCampeon().getNombre());
        } else if (!hayGrupos) {
            lblEstado.setText("Primero crea los grupos en la pestaña Jugadores.");
        } else if (!hayLlaves && pendientesGrupo > 0) {
            lblEstado.setText("Faltan " + pendientesGrupo + " partido(s) de grupo por jugar antes de hacer el sorteo.");
        } else if (!hayLlaves) {
            lblEstado.setText("Fase de grupos terminada. Pulsa «Realizar sorteo» para armar las llaves.");
        } else if (!modeloPendientes.isEmpty()) {
            lblEstado.setText("Ronda en curso: faltan " + modeloPendientes.size() + " resultado(s).");
        } else {
            lblEstado.setText("Ronda terminada. Pulsa «Avanzar ronda» para continuar.");
        }
    }
}
