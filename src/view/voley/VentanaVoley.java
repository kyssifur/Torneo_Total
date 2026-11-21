package view.voley;
import javax.swing.*;
import java.awt.*;
import controller.VoleyController;

public class VentanaVoley extends JFrame {
    private static final int ANCHO = 480;
    private static final int ALTO = 400;
    private VoleyController controller;
    private JTextField txtNombre;
    private JTextField txtRanking;
    private JTextField txtFuerza;
    private JButton btnRegistrar;
    private JButton btnVerEquipos;
    private JButton btnCrearLlaves;
    private JTextArea areaSalida;

    public VentanaVoley() {
        this.controller = new VoleyController();
        setTitle("Torneo de Voley");
        setSize(ANCHO, ALTO);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createContents();
        setVisible(true);
    }

    private void createContents() {
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 5, 5));
        panelCampos.add(new JLabel("Nombre del equipo:"));
        txtNombre = new JTextField(15);
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Ranking:"));
        txtRanking = new JTextField(15);
        panelCampos.add(txtRanking);

        panelCampos.add(new JLabel("Nivel de fuerza:"));
        txtFuerza = new JTextField(15);
        panelCampos.add(txtFuerza);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRegistrar = new JButton("Registrar Equipo");
        btnVerEquipos = new JButton("Ver Equipos");
        btnCrearLlaves = new JButton("Crear Llaves");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVerEquipos);
        panelBotones.add(btnCrearLlaves);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelCampos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        areaSalida = new JTextArea();
        areaSalida.setEditable(false);

        add(panelSuperior, BorderLayout.NORTH);
        add(areaSalida, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new VentanaVoley();
    }
}
