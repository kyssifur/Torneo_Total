package view.tenisdemesa;

import model.tenisdemesa.JugadorTT;
import model.tenisdemesa.PartidoTT;
import model.tenisdemesa.TorneoTenisDeMesa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Dibuja el cuadro de eliminación directa. Las rondas se reconstruyen a partir de la lista plana
 * de llaves del torneo: cada ronda tiene floor(participantes / 2) partidos y, si los participantes
 * son impares, uno pasa por BYE a la siguiente.
 */
public class LlavesDibujo extends JPanel {
    private static final int W = 210, H = 56, G = 64, M = 24, TOP = 56, EXTRA = 30, BYE_H = 30;

    private static class Ronda {
        String titulo;
        int total;
        final List<PartidoTT> partidos = new ArrayList<>();
        final List<JugadorTT> byes = new ArrayList<>();
    }

    private static class Caja {
        Rectangle r;
        PartidoTT partido;   // null = "por definir"
        JugadorTT bye;       // distinto de null = caja de BYE
        int ronda;
    }

    private final List<Ronda> rondas = new ArrayList<>();
    private final List<Caja> cajas = new ArrayList<>();
    private final Set<PartidoTT> pendientes = new HashSet<>();
    private Rectangle cajaCampeon;
    private JugadorTT campeon;
    private Consumer<PartidoTT> alSeleccionar;

    public LlavesDibujo() {
        setBackground(Color.WHITE);
        setToolTipText("");
        MouseAdapter raton = new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(
                    partidoPendienteEn(e.getPoint()) != null ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            }
            @Override public void mouseClicked(MouseEvent e) {
                PartidoTT p = partidoPendienteEn(e.getPoint());
                if (p != null && alSeleccionar != null) alSeleccionar.accept(p);
            }
        };
        addMouseListener(raton);
        addMouseMotionListener(raton);
    }

    public void setAlSeleccionar(Consumer<PartidoTT> accion) { this.alSeleccionar = accion; }

    // ------------------------------------------------------------------ datos y posiciones
    public void mostrar(TorneoTenisDeMesa t) {
        rondas.clear();
        cajas.clear();
        pendientes.clear();
        pendientes.addAll(t.partidosPendientesLlaves());
        campeon = t.getCampeon();
        cajaCampeon = null;

        int participantes = t.getGrupos().size() * 2;   // 2 clasificados por grupo
        List<Integer> tamanos = new ArrayList<>();
        for (int p = participantes; p > 1; ) {
            int partidos = p / 2;
            tamanos.add(partidos);
            p = partidos + (p % 2);
        }

        List<PartidoTT> llaves = t.getLlaves();
        List<JugadorTT> pool = new ArrayList<>();
        int idx = 0;
        for (int r = 0; r < tamanos.size(); r++) {
            Ronda ronda = new Ronda();
            ronda.total = tamanos.get(r);
            ronda.titulo = tituloRonda(r, tamanos.size());
            int hasta = Math.min(llaves.size(), idx + ronda.total);
            if (idx < llaves.size()) ronda.partidos.addAll(llaves.subList(idx, hasta));
            idx = hasta;

            if (r > 0) {   // el BYE es quien está en el grupo de avanzados pero no juega esta ronda
                for (JugadorTT j : pool) {
                    boolean juega = false;
                    for (PartidoTT p : ronda.partidos) {
                        if (p.getJugadorA().equals(j) || p.getJugadorB().equals(j)) juega = true;
                    }
                    if (!juega && !ronda.partidos.isEmpty()) ronda.byes.add(j);
                }
            }
            List<JugadorTT> siguiente = new ArrayList<>(ronda.byes);
            for (PartidoTT p : ronda.partidos) if (p.getGanador() != null) siguiente.add(p.getGanador());
            pool = siguiente;
            rondas.add(ronda);
        }
        calcularPosiciones();
        revalidate();
        repaint();
    }

    private static String tituloRonda(int r, int total) {
        int desdeElFinal = total - 1 - r;
        if (desdeElFinal == 0) return "Final";
        if (desdeElFinal == 1) return "Semifinales";
        if (desdeElFinal == 2) return "Cuartos de final";
        return "Ronda " + (r + 1);
    }

    private void calcularPosiciones() {
        int R = rondas.size();
        if (R == 0) {
            setPreferredSize(new Dimension(400, 200));
            return;
        }
        int areaH = rondas.get(0).total * (H + EXTRA);
        int maxByes = 0;
        for (int r = 0; r < R; r++) {
            Ronda ronda = rondas.get(r);
            int x = M + r * (W + G);
            double slot = areaH / (double) ronda.total;
            for (int i = 0; i < ronda.total; i++) {
                Caja c = new Caja();
                c.ronda = r;
                c.partido = i < ronda.partidos.size() ? ronda.partidos.get(i) : null;
                int yc = (int) (TOP + slot * (i + 0.5));
                c.r = new Rectangle(x, yc - H / 2, W, H);
                cajas.add(c);
            }
            for (int k = 0; k < ronda.byes.size(); k++) {
                Caja c = new Caja();
                c.ronda = r;
                c.bye = ronda.byes.get(k);
                c.r = new Rectangle(x, TOP + areaH + 14 + k * (BYE_H + 8), W, BYE_H);
                cajas.add(c);
            }
            maxByes = Math.max(maxByes, ronda.byes.size());
        }
        int xc = M + R * (W + G);
        cajaCampeon = new Rectangle(xc, TOP + areaH / 2 - 36, W, 72);
        int ancho = xc + W + M;
        int alto = TOP + areaH + (maxByes > 0 ? 14 + maxByes * (BYE_H + 8) : 0) + M;
        setPreferredSize(new Dimension(ancho, alto));
    }

    private PartidoTT partidoPendienteEn(Point pt) {
        for (Caja c : cajas) {
            if (c.partido != null && pendientes.contains(c.partido) && c.r.contains(pt)) return c.partido;
        }
        return null;
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        for (Caja c : cajas) {
            if (c.partido != null && c.r.contains(e.getPoint())) {
                return pendientes.contains(c.partido)
                    ? "Partido #" + c.partido.getIdPartido() + ": clic para registrar el resultado"
                    : "Partido #" + c.partido.getIdPartido();
            }
        }
        return null;
    }

    // ------------------------------------------------------------------ dibujo
    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (rondas.isEmpty()) {
            g.setFont(Estilo.NORMAL);
            g.setColor(Estilo.GRIS);
            String s = "El cuadro de llaves aparecerá aquí cuando existan los grupos.";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(s, (getWidth() - fm.stringWidth(s)) / 2, getHeight() / 2);
            g.dispose();
            return;
        }

        // títulos de columna
        g.setFont(Estilo.SUBTITULO);
        g.setColor(Estilo.AZUL);
        for (int r = 0; r < rondas.size(); r++) {
            centrado(g, rondas.get(r).titulo, M + r * (W + G), W, TOP - 22);
        }
        centrado(g, "Campeón", cajaCampeon.x, W, TOP - 22);

        // conexiones (debajo de las cajas)
        g.setStroke(new BasicStroke(2f));
        for (Caja c : cajas) {
            if (c.bye != null) conectar(g, c, c.bye);
            else if (c.partido != null && c.partido.getGanador() != null) conectar(g, c, c.partido.getGanador());
        }
        g.setStroke(new BasicStroke(1f));

        for (Caja c : cajas) {
            if (c.bye != null) dibujarBye(g, c);
            else dibujarPartido(g, c);
        }
        dibujarCampeon(g);
        g.dispose();
    }

    private void conectar(Graphics2D g, Caja origen, JugadorTT jugador) {
        Rectangle destino = null;
        int desplazamiento = 0;
        for (Caja d : cajas) {
            if (d.ronda != origen.ronda + 1) continue;
            boolean juega = d.partido != null
                && (d.partido.getJugadorA().equals(jugador) || d.partido.getJugadorB().equals(jugador));
            if (juega || (d.bye != null && d.bye.equals(jugador))) {
                destino = d.r;
                desplazamiento = (cajas.indexOf(d) % 4) * 9;   // cada destino tiene su propia vertical
            }
        }
        if (destino == null && origen.ronda == rondas.size() - 1 && jugador.equals(campeon)) {
            destino = cajaCampeon;
            desplazamiento = 18;
        }
        if (destino == null) return;
        int x1 = origen.r.x + origen.r.width, y1 = origen.r.y + origen.r.height / 2;
        int x2 = destino.x, y2 = destino.y + destino.height / 2;
        int xm = x1 + 12 + desplazamiento;
        g.setColor(Estilo.AZUL_CLARO);
        g.drawLine(x1, y1, xm, y1);
        g.drawLine(xm, y1, xm, y2);
        g.drawLine(xm, y2, x2, y2);
    }

    private void dibujarPartido(Graphics2D g, Caja c) {
        Rectangle r = c.r;
        PartidoTT p = c.partido;
        boolean pendiente = p != null && pendientes.contains(p);
        RoundRectangle2D forma = new RoundRectangle2D.Float(r.x, r.y, r.width, r.height, 10, 10);
        g.setColor(Color.WHITE);
        g.fill(forma);

        if (p == null) {
            g.setFont(Estilo.NORMAL.deriveFont(Font.ITALIC));
            g.setColor(Estilo.GRIS);
            centradoVertical(g, "Por definir", r.x, r.width, r);
        } else {
            Shape clipPrevio = g.getClip();
            g.clip(forma);
            int mitad = r.height / 2;
            for (int fila = 0; fila < 2; fila++) {
                JugadorTT j = fila == 0 ? p.getJugadorA() : p.getJugadorB();
                int sets = fila == 0 ? p.getSetsA() : p.getSetsB();
                boolean gano = p.getGanador() != null && p.getGanador().equals(j);
                Rectangle fr = new Rectangle(r.x, r.y + fila * mitad, r.width, mitad);
                if (gano) {
                    g.setColor(Estilo.VERDE_SUAVE);
                    g.fill(fr);
                }
                g.setFont(gano ? Estilo.NEGRITA : Estilo.NORMAL);
                g.setColor(p.estaJugado() && !gano ? Estilo.GRIS : Color.BLACK);
                FontMetrics fm = g.getFontMetrics();
                int base = fr.y + (fr.height - fm.getHeight()) / 2 + fm.getAscent();
                String marcador = p.estaJugado() ? String.valueOf(sets) : "-";
                int anchoMarcador = fm.stringWidth(marcador);
                g.drawString(recortar(j.getNombre(), fm, r.width - anchoMarcador - 32), r.x + 10, base);
                g.drawString(marcador, r.x + r.width - 10 - anchoMarcador, base);
            }
            g.setColor(Estilo.BORDE);
            g.drawLine(r.x, r.y + mitad, r.x + r.width, r.y + mitad);
            g.setClip(clipPrevio);
        }
        g.setStroke(new BasicStroke(pendiente ? 2.5f : 1.2f));
        g.setColor(pendiente ? Estilo.NARANJA : (p == null ? Estilo.BORDE : Estilo.GRIS));
        g.draw(forma);
        g.setStroke(new BasicStroke(1f));
    }

    private void dibujarBye(Graphics2D g, Caja c) {
        RoundRectangle2D forma = new RoundRectangle2D.Float(c.r.x, c.r.y, c.r.width, c.r.height, 10, 10);
        g.setColor(new Color(0xEAF1FA));
        g.fill(forma);
        g.setColor(Estilo.AZUL_CLARO);
        g.draw(forma);
        g.setFont(Estilo.NORMAL);
        FontMetrics fm = g.getFontMetrics();
        String texto = "BYE: " + c.bye.getNombre() + " pasa directo";
        g.setColor(Color.BLACK);
        centradoVertical(g, recortar(texto, fm, c.r.width - 16), c.r.x, c.r.width, c.r);
    }

    private void dibujarCampeon(Graphics2D g) {
        Rectangle r = cajaCampeon;
        RoundRectangle2D forma = new RoundRectangle2D.Float(r.x, r.y, r.width, r.height, 14, 14);
        boolean hay = campeon != null;
        g.setColor(hay ? new Color(0xFFF3C4) : Color.WHITE);
        g.fill(forma);
        g.setStroke(new BasicStroke(hay ? 3f : 1.2f));
        g.setColor(hay ? new Color(0xD9A400) : Estilo.BORDE);
        g.draw(forma);
        g.setStroke(new BasicStroke(1f));
        if (hay) {
            g.setFont(new Font("SansSerif", Font.BOLD, 17));
            g.setColor(Estilo.AZUL);
            FontMetrics fm = g.getFontMetrics();
            centradoVertical(g, recortar(campeon.getNombre(), fm, r.width - 20), r.x, r.width, r);
        } else {
            g.setFont(Estilo.NORMAL.deriveFont(Font.ITALIC));
            g.setColor(Estilo.GRIS);
            centradoVertical(g, "Por definir", r.x, r.width, r);
        }
    }

    // ------------------------------------------------------------------ utilidades de texto
    private void centrado(Graphics2D g, String s, int x, int ancho, int y) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, x + (ancho - fm.stringWidth(s)) / 2, y);
    }

    private void centradoVertical(Graphics2D g, String s, int x, int ancho, Rectangle caja) {
        FontMetrics fm = g.getFontMetrics();
        int base = caja.y + (caja.height - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(s, x + (ancho - fm.stringWidth(s)) / 2, base);
    }

    private String recortar(String s, FontMetrics fm, int maximo) {
        if (fm.stringWidth(s) <= maximo) return s;
        String puntos = "...";
        while (s.length() > 1 && fm.stringWidth(s + puntos) > maximo) s = s.substring(0, s.length() - 1);
        return s + puntos;
    }
}