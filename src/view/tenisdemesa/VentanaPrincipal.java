package view.tenisdemesa;

import model.tenisdemesa.PartidoTT;
import model.tenisdemesa.TorneoTenisDeMesa;
import model.tenisdemesa.TorneosIndividual;

import javax.swing.*;
import java.awt.*;

/** Ventana principal del organizador de torneos de tenis de mesa. Ejecuta esta clase para iniciar el programa. */
public class VentanaPrincipal extends JFrame {
    public static final int PESTANA_JUGADORES = 0;
    public static final int PESTANA_GRUPOS = 1;
    public static final int PESTANA_LLAVES = 2;
    public static final int PESTANA_RANKING = 3;

    private static final String NOMBRE_POR_DEFECTO = "Torneo de Tenis de Mesa";

    private TorneosIndividual contenedor;
    private TorneoTenisDeMesa torneo;
    private int numeroTorneo = 1;

    private final JTabbedPane pestanas = new JTabbedPane();
    private final JLabel lblTorneo = new JLabel(" ");
    private final PanelJugadores panelJugadores;
    private final PanelGrupos panelGrupos;
    private final PanelLlaves panelLlaves;
    private final PanelRanking panelRanking;

    public VentanaPrincipal() {
        super("Organizador de Torneos de Tenis de Mesa");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        contenedor = new TorneosIndividual(numeroTorneo);
        torneo = contenedor.crearTorneo(pedirNombre());

        panelJugadores = new PanelJugadores(this);
        panelGrupos = new PanelGrupos(this);
        panelLlaves = new PanelLlaves(this);
        panelRanking = new PanelRanking(this);

        pestanas.setFont(new Font("SansSerif", Font.BOLD, 14));
        pestanas.addTab("1. Jugadores", panelJugadores);
        pestanas.addTab("2. Grupos", panelGrupos);
        pestanas.addTab("3. Llaves y sorteo", panelLlaves);
        pestanas.addTab("4. Ranking", panelRanking);

        getContentPane().setBackground(Estilo.FONDO);
        setLayout(new BorderLayout());
        add(crearCabecera(), BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);

        refrescarTodo();
        setSize(1180, 780);
        setMinimumSize(new Dimension(960, 620));
        setLocationRelativeTo(null);
    }

    private JPanel crearCabecera() {
        JPanel cab = new JPanel(new BorderLayout(12, 0));
        cab.setBackground(Estilo.AZUL);
        cab.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel titulo = new JLabel("Organizador de Torneos de Tenis de Mesa");
        titulo.setFont(Estilo.TITULO);
        titulo.setForeground(Color.WHITE);
        lblTorneo.setFont(Estilo.NORMAL);
        lblTorneo.setForeground(new Color(0xC9D6E6));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(lblTorneo);
        cab.add(textos, BorderLayout.CENTER);

        JButton btnNuevo = Estilo.boton("Nuevo torneo", Estilo.NARANJA);
        btnNuevo.addActionListener(e -> nuevoTorneo());
        JPanel derecha = new JPanel(new GridBagLayout());
        derecha.setOpaque(false);
        derecha.add(btnNuevo);
        cab.add(derecha, BorderLayout.EAST);
        return cab;
    }

    private String pedirNombre() {
        String s = JOptionPane.showInputDialog(this, "Nombre del torneo:", NOMBRE_POR_DEFECTO);
        return (s == null || s.trim().isEmpty()) ? NOMBRE_POR_DEFECTO : s.trim();
    }

    // ------------------------------------------------------------------ acciones compartidas
    public TorneoTenisDeMesa getTorneo() { return torneo; }

    public void irAPestana(int indice) { pestanas.setSelectedIndex(indice); }

    /** Abre el diálogo de marcador y registra el resultado en el torneo. */
    public void registrarResultado(PartidoTT partido) {
        int[] r = DialogoResultado.pedir(this, partido);
        if (r == null) return;
        try {
            torneo.registrarResultado(partido, r[0], r[1]);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar el resultado",
                JOptionPane.ERROR_MESSAGE);
        }
        refrescarTodo();
    }

    public void torneoFinalizado() { contenedor.finalizarTorneo(); }

    private void nuevoTorneo() {
        int op = JOptionPane.showConfirmDialog(this,
            "Se perderán los jugadores y resultados del torneo actual.\n¿Crear un nuevo torneo?",
            "Nuevo torneo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (op != JOptionPane.YES_OPTION) return;
        String nombre = pedirNombre();
        contenedor = new TorneosIndividual(++numeroTorneo);
        torneo = contenedor.crearTorneo(nombre);
        panelJugadores.reiniciarFormulario();
        refrescarTodo();
        irAPestana(PESTANA_JUGADORES);
    }

    public void refrescarTodo() {
        panelJugadores.refrescar();
        panelGrupos.refrescar();
        panelLlaves.refrescar();
        panelRanking.refrescar();

        String fase;
        if (torneo.getCampeon() != null) fase = "Torneo finalizado";
        else if (!torneo.getLlaves().isEmpty()) fase = "Eliminación directa";
        else if (!torneo.getGrupos().isEmpty()) fase = "Fase de grupos";
        else fase = "Registro de jugadores";
        lblTorneo.setText(torneo.getNombre() + "   |   Fase: " + fase + "   |   " + contenedor);
    }

    // ------------------------------------------------------------------ main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignorada) {
                // si Nimbus no está disponible se usa el aspecto por defecto
            }
            new VentanaPrincipal().setVisible(true);
        });
    }
}
