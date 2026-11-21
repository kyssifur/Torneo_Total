package view.tenisdemesa;

import model.tenisdemesa.JugadorTT;
import model.tenisdemesa.PartidoTT;
import model.tenisdemesa.TorneoTenisDeMesa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Pestaña 2: muestra todos los grupos creados, con su tabla y sus partidos. */
public class PanelGrupos extends JPanel {
    private final VentanaPrincipal app;
    private final JPanel cuerpo = new JPanel(new BorderLayout());
    private final JLabel lblProgreso = new JLabel(" ");
    private final JProgressBar barra = new JProgressBar(0, 1);
    private final JButton btnLlaves = Estilo.boton("Ir a las llaves  ▶", Estilo.NARANJA);

    public PanelGrupos(VentanaPrincipal app) {
        this.app = app;
        setLayout(new BorderLayout(0, 12));
        setBackground(Estilo.FONDO);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel resumen = Estilo.tarjeta(new BorderLayout(14, 0));
        lblProgreso.setFont(Estilo.NEGRITA);
        resumen.add(lblProgreso, BorderLayout.WEST);
        JPanel centroBarra = new JPanel(new GridBagLayout());
        centroBarra.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        barra.setStringPainted(false);
        centroBarra.add(barra, c);
        resumen.add(centroBarra, BorderLayout.CENTER);
        resumen.add(btnLlaves, BorderLayout.EAST);
        add(resumen, BorderLayout.NORTH);

        cuerpo.setOpaque(false);
        add(cuerpo, BorderLayout.CENTER);

        btnLlaves.addActionListener(e -> app.irAPestana(2));
    }

    public void refrescar() {
        TorneoTenisDeMesa t = app.getTorneo();
        cuerpo.removeAll();

        if (t.getGrupos().isEmpty()) {
            lblProgreso.setText("Fase de grupos: sin iniciar");
            barra.setMaximum(1);
            barra.setValue(0);
            btnLlaves.setEnabled(false);
            cuerpo.add(Estilo.mensajeVacio("Todavía no hay grupos",
                "Registra al menos 4 jugadores y pulsa «Crear grupos» en la pestaña Jugadores."), BorderLayout.CENTER);
        } else {
            int total = t.getPartidosGrupo().size();
            int jugados = total - t.partidosPendientesGrupo().size();
            lblProgreso.setText("Partidos jugados: " + jugados + " de " + total);
            barra.setMaximum(total);
            barra.setValue(jugados);
            btnLlaves.setEnabled(true);
            btnLlaves.setText(jugados == total && t.getLlaves().isEmpty() ? "Hacer el sorteo  ▶" : "Ir a las llaves  ▶");
            cuerpo.add(construirGrupos(t), BorderLayout.CENTER);
        }
        cuerpo.revalidate();
        cuerpo.repaint();
    }

    private JComponent construirGrupos(TorneoTenisDeMesa t) {
        int n = t.getGrupos().size();
        int columnas = n <= 1 ? 1 : (n <= 4 ? 2 : 3);
        JPanel rejilla = new JPanel(new GridLayout(0, columnas, 14, 14));
        rejilla.setOpaque(false);
        for (int g = 0; g < n; g++) {
            rejilla.add(tarjetaGrupo(t, g, t.getGrupos().get(g)));
        }
        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setOpaque(false);
        envoltorio.add(rejilla, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(envoltorio);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        return scroll;
    }

    private JPanel tarjetaGrupo(TorneoTenisDeMesa t, int indice, ArrayList<JugadorTT> grupo) {
        List<PartidoTT> partidos = new ArrayList<>();
        for (PartidoTT p : t.getPartidosGrupo()) {
            if (grupo.contains(p.getJugadorA())) partidos.add(p);
        }
        boolean hayJuego = false;
        for (PartidoTT p : partidos) if (p.estaJugado()) hayJuego = true;
        final boolean resaltar = hayJuego;

        JPanel tarjeta = Estilo.tarjeta(new BorderLayout(0, 10));
        tarjeta.add(Estilo.subtitulo("Grupo " + (char) ('A' + indice)
            + "  (" + grupo.size() + " jugadores)"), BorderLayout.NORTH);

        // ---- tabla de posiciones
        DefaultTableModel modelo = Estilo.modelo("Pos", "Jugador", "V", "Dif. sets", "Fuerza");
        int pos = 1;
        for (JugadorTT j : t.tablaGrupo(grupo)) {
            modelo.addRow(new Object[]{pos++, j.getNombre(), victorias(t, j),
                String.format("%+d", difSets(t, j)), j.getNivelFuerza()});
        }
        JTable tabla = Estilo.tabla(modelo, fila -> (resaltar && fila < 2) ? Estilo.VERDE_SUAVE : null, 1);
        tabla.setFocusable(false);
        tabla.setRowSelectionAllowed(false);
        Estilo.anchoColumnas(tabla, 40, 170, 35, 70, 60);

        JPanel bloqueTabla = new JPanel(new BorderLayout());
        bloqueTabla.setBorder(BorderFactory.createLineBorder(Estilo.BORDE));
        bloqueTabla.add(tabla.getTableHeader(), BorderLayout.NORTH);
        bloqueTabla.add(tabla, BorderLayout.CENTER);

        // ---- partidos
        JPanel listaPartidos = new JPanel(new GridLayout(0, 1, 0, 6));
        listaPartidos.setOpaque(false);
        for (PartidoTT p : partidos) listaPartidos.add(filaPartido(p));

        JLabel nota = new JLabel(resaltar ? "Clasifican los 2 primeros (en verde)."
            : "Clasifican los 2 primeros de cada grupo.");
        nota.setFont(Estilo.NORMAL);
        nota.setForeground(Estilo.GRIS);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        bloqueTabla.setAlignmentX(Component.LEFT_ALIGNMENT);
        nota.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel titPartidos = new JLabel("Partidos");
        titPartidos.setFont(Estilo.NEGRITA);
        titPartidos.setAlignmentX(Component.LEFT_ALIGNMENT);
        listaPartidos.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(bloqueTabla);
        centro.add(Box.createVerticalStrut(4));
        centro.add(nota);
        centro.add(Box.createVerticalStrut(12));
        centro.add(titPartidos);
        centro.add(Box.createVerticalStrut(6));
        centro.add(listaPartidos);
        tarjeta.add(centro, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel filaPartido(PartidoTT p) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(new Color(0xF7F9FB));
        fila.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Estilo.BORDE),
            BorderFactory.createEmptyBorder(5, 10, 5, 6)));

        String a = Estilo.esc(p.getJugadorA().getNombre());
        String b = Estilo.esc(p.getJugadorB().getNombre());
        JLabel texto = new JLabel();
        texto.setFont(Estilo.NORMAL);
        JButton boton;
        if (p.estaJugado()) {
            boolean ganaA = p.getGanador().equals(p.getJugadorA());
            texto.setText("<html>" + (ganaA ? "<b>" + a + "</b>" : a)
                + " &nbsp;<b>" + p.getSetsA() + " - " + p.getSetsB() + "</b>&nbsp; "
                + (ganaA ? b : "<b>" + b + "</b>") + "</html>");
            boton = Estilo.botonCompacto("Jugado", Estilo.VERDE);
            boton.setEnabled(false);
        } else {
            texto.setText("<html>" + a + " &nbsp;<span style='color:#6B7785'>vs</span>&nbsp; " + b + "</html>");
            boton = Estilo.botonCompacto("Registrar", Estilo.NARANJA);
            boton.addActionListener(e -> app.registrarResultado(p));
        }
        fila.add(texto, BorderLayout.CENTER);
        fila.add(boton, BorderLayout.EAST);
        return fila;
    }

    // ------------------------------------------------------------------ estadísticas del grupo
    private int victorias(TorneoTenisDeMesa t, JugadorTT j) {
        int v = 0;
        for (PartidoTT p : t.getPartidosGrupo()) {
            if (p.estaJugado() && p.getGanador().equals(j)) v++;
        }
        return v;
    }

    private int difSets(TorneoTenisDeMesa t, JugadorTT j) {
        int d = 0;
        for (PartidoTT p : t.getPartidosGrupo()) {
            if (!p.estaJugado()) continue;
            if (p.getJugadorA().equals(j)) d += p.getSetsA() - p.getSetsB();
            else if (p.getJugadorB().equals(j)) d += p.getSetsB() - p.getSetsA();
        }
        return d;
    }
}