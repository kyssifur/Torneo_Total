package view.tenisdemesa;

import model.tenisdemesa.JugadorTT;
import model.tenisdemesa.PartidoTT;
import model.tenisdemesa.TorneoTenisDeMesa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Pestaña 4: clasificación general por puntos de ranking. */
public class PanelRanking extends JPanel {
    private final VentanaPrincipal app;
    private final DefaultTableModel modelo = Estilo.modelo("Pos", "Jugador", "ID", "Fuerza", "Puntos");
    private boolean hayPuntos = false;
    private final JTable tabla = Estilo.tabla(modelo, this::colorPodio, 1);
    private final JLabel lblCampeon = Estilo.subtitulo(" ");

    public PanelRanking(VentanaPrincipal app) {
        this.app = app;
        setLayout(new BorderLayout(0, 12));
        setBackground(Estilo.FONDO);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel tarjeta = Estilo.tarjeta(new BorderLayout(0, 10));
        tarjeta.add(lblCampeon, BorderLayout.NORTH);
        tabla.setRowSelectionAllowed(false);
        tabla.setFocusable(false);
        Estilo.anchoColumnas(tabla, 60, 300, 90, 80, 90);
        tarjeta.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JLabel nota = new JLabel("<html>Cada victoria suma " + PartidoTT.PUNTOS_VICTORIA + " puntos y cada derrota "
            + PartidoTT.PUNTOS_DERROTA + " punto, en grupos y en eliminación directa. "
            + "Desempate: nivel de fuerza y luego nombre.</html>");
        nota.setFont(Estilo.NORMAL);
        nota.setForeground(Estilo.GRIS);
        tarjeta.add(nota, BorderLayout.SOUTH);
        add(tarjeta, BorderLayout.CENTER);
    }

    private Color colorPodio(int fila) {
        if (!hayPuntos) return null;
        switch (fila) {
            case 0: return new Color(0xFFF3C4);
            case 1: return new Color(0xE9ECF0);
            case 2: return new Color(0xF6E3D3);
            default: return null;
        }
    }

    public void refrescar() {
        TorneoTenisDeMesa t = app.getTorneo();
        List<JugadorTT> orden = t.getRanking().calcular(t.getJugadores());
        hayPuntos = false;
        modelo.setRowCount(0);
        for (JugadorTT j : orden) {
            if (j.getRanking() > 0) hayPuntos = true;
            modelo.addRow(new Object[]{t.getRanking().getPosicion(j), j.getNombre(), j.getId(),
                j.getNivelFuerza(), j.getRanking()});
        }
        if (t.getCampeon() != null) {
            lblCampeon.setText("Campeón: " + t.getCampeon().getNombre());
        } else if (orden.isEmpty()) {
            lblCampeon.setText("Aún no hay jugadores registrados");
        } else {
            lblCampeon.setText(hayPuntos ? "Clasificación actual" : "Los puntos aparecerán al registrar partidos");
        }
    }
}
