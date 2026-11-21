package view.tenisdemesa;

import model.tenisdemesa.JugadorTT;
import model.tenisdemesa.TorneoTenisDeMesa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

/** Campos de un jugador: nombre, ID y nivel de fuerza. Se usa en el alta y en la edición. */
public class FormularioJugador extends JPanel {
    private final Supplier<TorneoTenisDeMesa> torneo;
    private final JTextField txtNombre = new JTextField(16);
    private final JTextField txtId = new JTextField(16);
    private final JSlider slFuerza = new JSlider(1, 10, 5);
    private final JLabel lblFuerza = new JLabel();
    private final JLabel lblError = new JLabel(" ");

    public FormularioJugador(Supplier<TorneoTenisDeMesa> torneo) {
        this.torneo = torneo;
        setOpaque(false);
        setLayout(new GridBagLayout());

        slFuerza.setMajorTickSpacing(1);
        slFuerza.setPaintTicks(true);
        slFuerza.setPaintLabels(true);
        slFuerza.setSnapToTicks(true);
        slFuerza.setOpaque(false);
        slFuerza.addChangeListener(e -> actualizarEtiquetaFuerza());
        lblError.setForeground(Estilo.ROJO);
        lblError.setFont(Estilo.NORMAL);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        int y = 0;
        c.insets = new Insets(0, 0, 4, 0);
        c.gridy = y++; add(etiqueta("Nombre del jugador"), c);
        c.insets = new Insets(0, 0, 12, 0);
        c.gridy = y++; add(txtNombre, c);
        c.insets = new Insets(0, 0, 4, 0);
        c.gridy = y++; add(etiqueta("ID (único)"), c);
        c.insets = new Insets(0, 0, 12, 0);
        c.gridy = y++; add(txtId, c);
        c.insets = new Insets(0, 0, 4, 0);
        c.gridy = y++; add(lblFuerza, c);
        c.gridy = y++; add(slFuerza, c);
        c.insets = new Insets(6, 0, 0, 0);
        c.gridy = y++; add(lblError, c);
        c.gridy = y; c.weighty = 1; c.fill = GridBagConstraints.VERTICAL;
        add(Box.createVerticalGlue(), c);   // empuja los campos hacia arriba

        limpiar();
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Estilo.NEGRITA);
        return l;
    }

    private void actualizarEtiquetaFuerza() {
        lblFuerza.setFont(Estilo.NEGRITA);
        lblFuerza.setText("Nivel de fuerza: " + slFuerza.getValue() + "  (1 = bajo, 10 = alto)");
    }

    /** Deja el formulario listo para un jugador nuevo, con un ID sugerido. */
    public void limpiar() {
        txtNombre.setText("");
        txtId.setText(sugerirId());
        slFuerza.setValue(5);
        actualizarEtiquetaFuerza();
        setError(null);
        txtNombre.requestFocusInWindow();
    }

    /** Carga los datos de un jugador existente para editarlos. */
    public void cargar(JugadorTT j, boolean soloNombre) {
        txtNombre.setText(j.getNombre());
        txtId.setText(j.getId());
        slFuerza.setValue(j.getNivelFuerza());
        txtId.setEnabled(!soloNombre);
        slFuerza.setEnabled(!soloNombre);
        actualizarEtiquetaFuerza();
        setError(null);
    }

    public void activar(boolean activo) {
        txtNombre.setEnabled(activo);
        txtId.setEnabled(activo);
        slFuerza.setEnabled(activo);
    }

    public void alPresionarEnter(ActionListener l) {
        txtNombre.addActionListener(l);
        txtId.addActionListener(l);
    }

    /** Valida los campos. 'excepto' es el jugador que se está editando (su propio ID no cuenta como repetido). */
    public boolean validar(JugadorTT excepto) {
        String nombre = getNombre();
        String id = getId();
        if (nombre.isEmpty()) return error("Escribe el nombre del jugador.", txtNombre);
        if (id.isEmpty()) return error("Escribe un ID para el jugador.", txtId);
        for (JugadorTT j : torneo.get().getJugadores()) {
            if (j != excepto && j.getId().equals(id)) {
                return error("Ya existe un jugador con el ID " + id + ".", txtId);
            }
        }
        setError(null);
        return true;
    }

    private boolean error(String mensaje, JComponent foco) {
        setError(mensaje);
        foco.requestFocusInWindow();
        return false;
    }

    public void setError(String mensaje) {
        lblError.setText(mensaje == null ? " " : "<html>" + Estilo.esc(mensaje) + "</html>");
    }

    /** Propone un ID libre del estilo J01, J02... */
    public String sugerirId() {
        int n = torneo.get().getJugadores().size() + 1;
        String id;
        do {
            id = String.format("J%02d", n++);
        } while (existe(id));
        return id;
    }

    private boolean existe(String id) {
        for (JugadorTT j : torneo.get().getJugadores()) if (j.getId().equals(id)) return true;
        return false;
    }

    public String getNombre() { return txtNombre.getText().trim(); }
    public String getId() { return txtId.getText().trim(); }
    public int getFuerza() { return slFuerza.getValue(); }
}