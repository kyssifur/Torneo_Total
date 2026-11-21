package view.basquet;

import model.basquet.PartidoBasquet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPartido extends JFrame {
    private PartidoBasquet partido;
    private JLabel lblMarcador, lblCronometro;
    private int segundosTranscurridos = 0;
    private Timer timer;

    public VentanaPartido(PartidoBasquet partido) {
        this.partido = partido;

        setTitle("En Vivo: " + partido.getEquipoA().getNombre() + " vs " + partido.getEquipoB().getNombre());
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelCronometro = new JPanel();
        lblCronometro = new JLabel("Tiempo: 00:00", JLabel.CENTER);
        lblCronometro.setFont(new Font("Arial", Font.BOLD, 18));
        panelCronometro.add(lblCronometro);
        add(panelCronometro, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(2, 1));
        lblMarcador = new JLabel(actualizarTextoMarcador(), JLabel.CENTER);
        lblMarcador.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCentro.add(lblMarcador);
        add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton btnPuntoA = new JButton("+ Puntos a " + partido.getEquipoA().getNombre());
        JButton btnPuntoB = new JButton("+ Puntos a " + partido.getEquipoB().getNombre());

        panelBotones.add(btnPuntoA);
        panelBotones.add(btnPuntoB);
        add(panelBotones, BorderLayout.SOUTH);

        btnPuntoA.addActionListener(e -> {
            int nuevosPuntosA = partido.getPuntosA() + 2;
            partido.registrarMarcador(nuevosPuntosA, partido.getPuntosB());
            lblMarcador.setText(actualizarTextoMarcador());
        });

        btnPuntoB.addActionListener(e -> {
            int nuevosPuntosB = partido.getPuntosB() + 2;
            partido.registrarMarcador(partido.getPuntosA(), nuevosPuntosB);
            lblMarcador.setText(actualizarTextoMarcador());
        });

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                segundosTranscurridos++;
                int minutos = segundosTranscurridos / 60;
                int segundos = segundosTranscurridos % 60;
                lblCronometro.setText(String.format("Tiempo: %02d:%02d", minutos, segundos));
            }
        });
        timer.start();
    }

    private String actualizarTextoMarcador() {
        return "<html><center><b>" + partido.getEquipoA().getNombre() + "</b>: " + partido.getPuntosA() + 
               " <br>VS<br> <b>" + partido.getEquipoB().getNombre() + "</b>: " + partido.getPuntosB() + "</center></html>";
    }
}