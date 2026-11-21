package view.tenisdemesa;

import model.tenisdemesa.JugadorTT;
import model.tenisdemesa.PartidoTT;

import javax.swing.*;
import java.awt.*;

/** Ventana para registrar el marcador en sets de un partido y ver los puntos de ranking que se otorgan. */
public class DialogoResultado extends JDialog {
    private static final int N = PartidoTT.SETS_PARA_GANAR;

    private final PartidoTT partido;
    private final JSpinner spA = new JSpinner(new SpinnerNumberModel(0, 0, N, 1));
    private final JSpinner spB = new JSpinner(new SpinnerNumberModel(0, 0, N, 1));
    private final JLabel lblEstado = new JLabel(" ", SwingConstants.CENTER);
    private final JButton btnGuardar = Estilo.boton("Guardar resultado", Estilo.VERDE);
    private int[] resultado;

    private DialogoResultado(Window padre, PartidoTT partido) {
        super(padre, "Registrar resultado", ModalityType.APPLICATION_MODAL);
        this.partido = partido;

        JPanel raiz = new JPanel(new BorderLayout(0, 14));
        raiz.setBackground(Color.WHITE);
        raiz.setBorder(BorderFactory.createEmptyBorder(18, 22, 14, 22));

        JLabel info = new JLabel("Partido #" + partido.getIdPartido()
            + "  ·  Mejor de 5 sets (gana quien llegue a " + N + ")", SwingConstants.CENTER);
        info.setFont(Estilo.NORMAL);
        info.setForeground(Estilo.GRIS);
        raiz.add(info, BorderLayout.NORTH);

        JPanel marcador = new JPanel(new GridBagLayout());
        marcador.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, 12, 0, 12);
        c.gridx = 0; marcador.add(columna(partido.getJugadorA(), spA, true), c);
        JLabel vs = new JLabel("vs");
        vs.setFont(Estilo.SUBTITULO);
        vs.setForeground(Estilo.GRIS);
        c.gridx = 1; c.anchor = GridBagConstraints.CENTER; marcador.add(vs, c);
        c.gridx = 2; c.anchor = GridBagConstraints.NORTH; marcador.add(columna(partido.getJugadorB(), spB, false), c);
        raiz.add(marcador, BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout(0, 8));
        sur.setOpaque(false);
        lblEstado.setFont(Estilo.NORMAL);
        sur.add(lblEstado, BorderLayout.NORTH);

        JButton btnCancelar = Estilo.boton("Cancelar", Estilo.GRIS);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        botones.setOpaque(false);
        botones.add(btnCancelar);
        botones.add(btnGuardar);
        sur.add(botones, BorderLayout.SOUTH);
        raiz.add(sur, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> {
            resultado = new int[]{(Integer) spA.getValue(), (Integer) spB.getValue()};
            dispose();
        });
        spA.addChangeListener(e -> actualizar());
        spB.addChangeListener(e -> actualizar());

        setContentPane(raiz);
        getRootPane().setDefaultButton(btnGuardar);
        setResizable(false);
        actualizar();
        pack();
        setMinimumSize(new Dimension(440, 0));
        setLocationRelativeTo(padre);
    }

    /** Columna de un jugador: nombre, sets ganados y atajos de marcador. */
    private JPanel columna(JugadorTT jugador, JSpinner spinner, boolean esA) {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        JLabel nombre = new JLabel(jugador.getNombre());
        nombre.setFont(Estilo.SUBTITULO);
        nombre.setForeground(Estilo.AZUL);
        nombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sets = new JLabel("sets ganados");
        sets.setFont(Estilo.NORMAL);
        sets.setForeground(Estilo.GRIS);
        sets.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField campo = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        campo.setFont(new Font("SansSerif", Font.BOLD, 24));
        campo.setColumns(2);
        campo.setHorizontalAlignment(SwingConstants.CENTER);
        spinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        spinner.setMaximumSize(spinner.getPreferredSize());

        JLabel atajo = new JLabel("Victoria rápida");
        atajo.setFont(Estilo.NORMAL);
        atajo.setForeground(Estilo.GRIS);
        atajo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel atajos = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        atajos.setOpaque(false);
        for (int rival = 0; rival < N; rival++) {
            final int k = rival;
            JButton b = Estilo.botonCompacto(N + "-" + k, Estilo.AZUL_CLARO);
            b.addActionListener(e -> {
                spA.setValue(esA ? N : k);
                spB.setValue(esA ? k : N);
            });
            atajos.add(b);
        }

        col.add(nombre);
        col.add(Box.createVerticalStrut(10));
        col.add(spinner);
        col.add(Box.createVerticalStrut(2));
        col.add(sets);
        col.add(Box.createVerticalStrut(14));
        col.add(atajo);
        col.add(Box.createVerticalStrut(4));
        col.add(atajos);
        return col;
    }

    /** Valida el marcador con la misma regla del modelo y muestra el resultado esperado. */
    private void actualizar() {
        int a = (Integer) spA.getValue();
        int b = (Integer) spB.getValue();
        boolean valido = (a == N && b < N) || (b == N && a < N);
        btnGuardar.setEnabled(valido);
        if (valido) {
            JugadorTT ganador = a > b ? partido.getJugadorA() : partido.getJugadorB();
            JugadorTT perdedor = a > b ? partido.getJugadorB() : partido.getJugadorA();
            lblEstado.setForeground(Estilo.VERDE);
            lblEstado.setText("<html><div style='text-align:center'>Gana <b>" + Estilo.esc(ganador.getNombre())
                + "</b> por " + a + " - " + b + "<br>Puntos de ranking: "
                + Estilo.esc(ganador.getNombre()) + " +" + PartidoTT.PUNTOS_VICTORIA + "  ·  "
                + Estilo.esc(perdedor.getNombre()) + " +" + PartidoTT.PUNTOS_DERROTA + "</div></html>");
        } else {
            lblEstado.setForeground(Estilo.GRIS);
            lblEstado.setText("<html><div style='text-align:center'>Un jugador debe llegar a " + N
                + " sets y su rival debe tener menos de " + N + ".</div></html>");
        }
    }

    /** @return {setsA, setsB} o null si se canceló. */
    public static int[] pedir(Component padre, PartidoTT partido) {
        DialogoResultado d = new DialogoResultado(SwingUtilities.getWindowAncestor(padre), partido);
        d.setVisible(true);
        return d.resultado;
    }
}