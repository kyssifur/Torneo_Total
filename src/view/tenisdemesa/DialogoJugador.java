package view.tenisdemesa;

import model.tenisdemesa.JugadorTT;
import model.tenisdemesa.TorneoTenisDeMesa;

import javax.swing.*;
import java.awt.*;

/** Ventana para editar los datos de un jugador. */
public class DialogoJugador extends JDialog {
    private final FormularioJugador form;
    private boolean aceptado = false;

    private DialogoJugador(Window padre, TorneoTenisDeMesa torneo, JugadorTT jugador, boolean soloNombre) {
        super(padre, "Editar jugador", ModalityType.APPLICATION_MODAL);
        form = new FormularioJugador(() -> torneo);
        form.cargar(jugador, soloNombre);

        JButton btnGuardar = Estilo.boton("Guardar cambios", Estilo.VERDE);
        JButton btnCancelar = Estilo.boton("Cancelar", Estilo.GRIS);

        btnGuardar.addActionListener(e -> {
            if (!form.validar(jugador)) return;
            jugador.setNombre(form.getNombre());
            if (!soloNombre) {
                jugador.setId(form.getId());
                jugador.setNivelFuerza(form.getFuerza());
            }
            aceptado = true;
            dispose();
        });
        btnCancelar.addActionListener(e -> dispose());
        form.alPresionarEnter(e -> btnGuardar.doClick());

        JPanel raiz = new JPanel(new BorderLayout(0, 12));
        raiz.setBackground(Color.WHITE);
        raiz.setBorder(BorderFactory.createEmptyBorder(16, 18, 14, 18));
        raiz.add(Estilo.subtitulo("Editar jugador"), BorderLayout.NORTH);
        raiz.add(form, BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setOpaque(false);
        if (soloNombre) {
            JLabel aviso = new JLabel("<html>Los grupos ya están creados: solo se puede<br>cambiar el nombre.</html>");
            aviso.setFont(Estilo.NORMAL);
            aviso.setForeground(Estilo.GRIS);
            sur.add(aviso, BorderLayout.NORTH);
        }
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        botones.setOpaque(false);
        botones.add(btnCancelar);
        botones.add(btnGuardar);
        sur.add(botones, BorderLayout.SOUTH);
        raiz.add(sur, BorderLayout.SOUTH);

        setContentPane(raiz);
        getRootPane().setDefaultButton(btnGuardar);
        setResizable(false);
        pack();
        setLocationRelativeTo(padre);
    }

    /** @return true si el usuario guardó los cambios. */
    public static boolean editar(Component padre, TorneoTenisDeMesa torneo, JugadorTT jugador, boolean soloNombre) {
        DialogoJugador d = new DialogoJugador(SwingUtilities.getWindowAncestor(padre), torneo, jugador, soloNombre);
        d.setVisible(true);
        return d.aceptado;
    }
}
