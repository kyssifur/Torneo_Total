package view.futbol;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** Utilidades compartidas por los paneles de la vista de futbol. */
final class UiUtil {

    private UiUtil() {
    }

    /** Muestra el mensaje de una excepcion del modelo como aviso al usuario. */
    static void aviso(Component padre, Exception ex) {
        JOptionPane.showMessageDialog(padre, ex.getMessage(),
                "No se pudo completar la acción", JOptionPane.WARNING_MESSAGE);
    }

    static JPanel formulario() {
        return new JPanel(new GridBagLayout());
    }

    static void fila(JPanel form, int y, String etiqueta, JComponent campo) {
        fila(form, y, new JLabel(etiqueta), campo);
    }

    static void fila(JPanel form, int y, JComponent etiqueta, JComponent campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = y;
        c.insets = new Insets(3, 6, 3, 6);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        form.add(etiqueta, c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        form.add(campo, c);
    }

    static void filaBoton(JPanel form, int y, JComponent boton) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = y;
        c.gridx = 0;
        c.gridwidth = 2;
        c.insets = new Insets(6, 6, 3, 6);
        c.anchor = GridBagConstraints.EAST;
        form.add(boton, c);
    }

    static JSpinner spinner(int valor, int min, int max) {
        return new JSpinner(new SpinnerNumberModel(valor, min, max, 1));
    }

    static int valor(JSpinner spinner) {
        return ((Number) spinner.getValue()).intValue();
    }

    /** Modelo de tabla de solo lectura. */
    static DefaultTableModel modeloTabla(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    static void centrar(JTable tabla, int... columnas) {
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        for (int c : columnas) {
            tabla.getColumnModel().getColumn(c).setCellRenderer(render);
        }
    }
}