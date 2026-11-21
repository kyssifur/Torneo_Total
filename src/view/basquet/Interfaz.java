package view.basquet;

import controller.BasquetController;
import model.basquet.PartidoBasquet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame {
    private BasquetController controller;
    
    private JTextField txtNombreEquipo, txtRankingEquipo, txtFuerzaEquipo;
    
    private JTextField txtNombreJugador, txtRankingJugador, txtFuerzaJugador;
    
    private JTextField txtNombreBolsa;
    
    private JTextArea areaResultados;

    public Interfaz() {
        controller = new BasquetController("Torneo de Básquet Interfacultades");

        setTitle("Gestión de Torneo de Básquet");
        setSize(700, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelNorte = new JPanel(new GridLayout(3, 1, 5, 5));

        JPanel panelRegistro = new JPanel(new GridLayout(4, 2, 5, 5));
        panelRegistro.setBorder(BorderFactory.createTitledBorder("1. Registrar Equipo Directo"));

        panelRegistro.add(new JLabel("Nombre del Equipo:"));
        txtNombreEquipo = new JTextField();
        panelRegistro.add(txtNombreEquipo);

        panelRegistro.add(new JLabel("Ranking:"));
        txtRankingEquipo = new JTextField();
        panelRegistro.add(txtRankingEquipo);

        panelRegistro.add(new JLabel("Nivel de Fuerza:"));
        txtFuerzaEquipo = new JTextField();
        panelRegistro.add(txtFuerzaEquipo);

        JButton btnRegistrarEquipo = new JButton("Registrar Equipo");
        panelRegistro.add(btnRegistrarEquipo);
        panelNorte.add(panelRegistro);

        JPanel panelRegJugador = new JPanel(new GridLayout(4, 2, 5, 5));
        panelRegJugador.setBorder(BorderFactory.createTitledBorder("2. Registrar Jugador (para la Bolsa)"));

        panelRegJugador.add(new JLabel("Nombre del Jugador:"));
        txtNombreJugador = new JTextField();
        panelRegJugador.add(txtNombreJugador);

        panelRegJugador.add(new JLabel("Ranking del Jugador:"));
        txtRankingJugador = new JTextField();
        panelRegJugador.add(txtRankingJugador);

        panelRegJugador.add(new JLabel("Fuerza del Jugador:"));
        txtFuerzaJugador = new JTextField();
        panelRegJugador.add(txtFuerzaJugador);

        JButton btnRegistrarJugador = new JButton("Agregar Jugador a Bolsa");
        panelRegJugador.add(btnRegistrarJugador);
        panelNorte.add(panelRegJugador);

        JPanel panelBolsa = new JPanel(new GridLayout(2, 2, 5, 5));
        panelBolsa.setBorder(BorderFactory.createTitledBorder("3. Formar Equipo desde la Bolsa de Jugadores"));
        
        panelBolsa.add(new JLabel("Nombre del Nuevo Equipo:"));
        txtNombreBolsa = new JTextField();
        panelBolsa.add(txtNombreBolsa);

        JButton btnCrearDesdeBolsa = new JButton("Generar Equipo con la Bolsa");
        panelBolsa.add(btnCrearDesdeBolsa);
        panelNorte.add(panelBolsa);

        add(panelNorte, BorderLayout.NORTH);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Estado del Torneo y Partidos"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout());
        
        JButton btnVerEquipos = new JButton("Ver Equipos Inscritos");
        JButton btnGenerarPartidos = new JButton("Generar Enfrentamientos");
        JButton btnIniciarPartido = new JButton("Iniciar Siguiente Partido");
        
        panelAcciones.add(btnVerEquipos);
        panelAcciones.add(btnGenerarPartidos);
        panelAcciones.add(btnIniciarPartido);
        
        add(panelAcciones, BorderLayout.SOUTH);

        btnRegistrarEquipo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = txtNombreEquipo.getText().trim();
                    int ranking = Integer.parseInt(txtRankingEquipo.getText().trim());
                    int fuerza = Integer.parseInt(txtFuerzaEquipo.getText().trim());

                    if (nombre.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El nombre del equipo no puede estar vacío.");
                        return;
                    }

                    controller.handleAgregarEquipo(nombre, ranking, fuerza);
                    areaResultados.append("✔ Equipo '" + nombre + "' registrado con éxito.\n");

                    txtNombreEquipo.setText("");
                    txtRankingEquipo.setText("");
                    txtFuerzaEquipo.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa números válidos en Ranking y Fuerza.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegistrarJugador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = txtNombreJugador.getText().trim();
                    int ranking = Integer.parseInt(txtRankingJugador.getText().trim());
                    int fuerza = Integer.parseInt(txtFuerzaJugador.getText().trim());

                    if (nombre.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El nombre del jugador no puede estar vacío.");
                        return;
                    }

                    controller.agregarJugadorABolsa(nombre, ranking, fuerza);
                    areaResultados.append("Jugador '" + nombre + "' agregado a la bolsa correctamente.\n");

                    txtNombreJugador.setText("");
                    txtRankingJugador.setText("");
                    txtFuerzaJugador.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingresa números válidos para el ranking y la fuerza del jugador.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCrearDesdeBolsa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreEquipo = txtNombreBolsa.getText().trim();
                if (nombreEquipo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ingresa un nombre para el equipo de la bolsa.");
                    return;
                }
                String mensaje = controller.handleFormarEquipoConJugadores(nombreEquipo);
                areaResultados.append(mensaje + "\n");
                txtNombreBolsa.setText("");
            }
        });

        btnVerEquipos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String listado = controller.obtenerEquiposTexto();
                areaResultados.append(listado);
            }
        });

        btnGenerarPartidos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String resultadoPartidos = controller.handleCrearLlaves();
                    areaResultados.append(resultadoPartidos);
                } catch (Exception ex) {
                    areaResultados.append("\n❌ Ocurrió un error al generar los enfrentamientos.\n");
                }
            }
        });

        btnIniciarPartido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controller.getTorneo().getLlaves().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Primero debes generar los enfrentamientos.");
                    return;
                }
                PartidoBasquet partidoActual = controller.getTorneo().getLlaves().get(0);
                VentanaPartido ventanaPartido = new VentanaPartido(partidoActual);
                ventanaPartido.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }
}