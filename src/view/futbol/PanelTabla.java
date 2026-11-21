package view.futbol;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.FutbolController;
import model.futbol.FilaPosicionFutbol;

/** Pestaña 3: tabla de posiciones (solo lectura). */
class PanelTabla extends JPanel {

    private final FutbolController controller;
    private final DefaultTableModel modelo = UiUtil.modeloTabla(
            "Pos", "Equipo", "PJ", "G", "E", "P", "GF", "GC", "DG", "Pts");
    private final JTable tabla = new JTable(modelo);

    PanelTabla(FutbolController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabla.setRowHeight(26);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        UiUtil.centrar(tabla, 0, 2, 3, 4, 5, 6, 7, 8, 9);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(new JLabel("Orden: puntos, diferencia de goles y goles a favor. "
                + "3 puntos por victoria y 1 por empate (a confirmar con el equipo)."), BorderLayout.SOUTH);
    }

    void refrescar() {
        modelo.setRowCount(0);
        int pos = 1;
        for (FilaPosicionFutbol f : controller.getTabla()) {
            modelo.addRow(new Object[]{
                pos++, f.getEquipo().getNombre(), f.getJugados(), f.getGanados(), f.getEmpatados(),
                f.getPerdidos(), f.getGolesFavor(), f.getGolesContra(), f.getDiferenciaGoles(), f.getPuntos()});
        }
    }
}