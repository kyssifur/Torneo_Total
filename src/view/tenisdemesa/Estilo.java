package view.tenisdemesa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.IntFunction;

/** Colores, fuentes y componentes reutilizables para toda la interfaz. */
public final class Estilo {
    private Estilo() {}

    public static final Color AZUL = new Color(0x1B3A5C);
    public static final Color AZUL_CLARO = new Color(0x3A6EA5);
    public static final Color NARANJA = new Color(0xE8833A);
    public static final Color VERDE = new Color(0x2E9E6B);
    public static final Color ROJO = new Color(0xC8483F);
    public static final Color GRIS = new Color(0x6B7785);
    public static final Color FONDO = new Color(0xF2F4F7);
    public static final Color BORDE = new Color(0xD3D9E1);
    public static final Color VERDE_SUAVE = new Color(0xDDF3E6);

    public static final Font TITULO = new Font("SansSerif", Font.BOLD, 22);
    public static final Font SUBTITULO = new Font("SansSerif", Font.BOLD, 15);
    public static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font NEGRITA = new Font("SansSerif", Font.BOLD, 13);

    // ------------------------------------------------------------------ botones
    public static JButton boton(String texto, Color color) {
        return new BotonColor(texto, color, 9, 18);
    }

    public static JButton botonCompacto(String texto, Color color) {
        return new BotonColor(texto, color, 4, 10);
    }

    private static class BotonColor extends JButton {
        private final Color base;
        private boolean encima;

        BotonColor(String texto, Color base, int vertical, int horizontal) {
            super(texto);
            this.base = base;
            setFont(NEGRITA);
            setForeground(Color.WHITE);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(vertical, horizontal, vertical, horizontal));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { encima = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { encima = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c;
            if (!isEnabled()) c = new Color(0xDDE2E8);
            else if (getModel().isPressed()) c = base.darker().darker();
            else if (encima) c = base.darker();
            else c = base;
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ------------------------------------------------------------------ paneles
    public static JPanel tarjeta(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        return p;
    }

    public static JLabel subtitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(SUBTITULO);
        l.setForeground(AZUL);
        return l;
    }

    /** Pantalla vacía: explica qué falta y qué hacer. */
    public static JPanel mensajeVacio(String titulo, String detalle) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        JPanel caja = new JPanel();
        caja.setOpaque(false);
        caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(titulo);
        t.setFont(TITULO);
        t.setForeground(AZUL);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel d = new JLabel(detalle);
        d.setFont(NORMAL);
        d.setForeground(GRIS);
        d.setAlignmentX(Component.CENTER_ALIGNMENT);
        caja.add(t);
        caja.add(Box.createVerticalStrut(8));
        caja.add(d);
        p.add(caja);
        return p;
    }

    // ------------------------------------------------------------------ tablas
    public static DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int fila, int col) { return false; }
        };
    }

    /**
     * Tabla de solo lectura con filas alternadas.
     * @param colorFila color de resalte por fila (puede devolver null)
     * @param izquierda columnas alineadas a la izquierda (las demás van centradas)
     */
    public static JTable tabla(DefaultTableModel m, IntFunction<Color> colorFila, int... izquierda) {
        JTable t = new JTable(m) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int fila, int col) {
                Component c = super.prepareRenderer(r, fila, col);
                if (c instanceof JLabel) {
                    boolean izq = false;
                    for (int i : izquierda) if (i == col) izq = true;
                    ((JLabel) c).setHorizontalAlignment(izq ? SwingConstants.LEFT : SwingConstants.CENTER);
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                }
                if (c instanceof JComponent) ((JComponent) c).setOpaque(true);
                Color resalte = colorFila == null ? null : colorFila.apply(fila);
                if (!isRowSelected(fila)) {
                    c.setBackground(resalte != null ? resalte
                        : (fila % 2 == 0 ? Color.WHITE : new Color(0xF7F9FB)));
                    c.setForeground(Color.BLACK);
                }
                c.setFont(resalte != null ? NEGRITA : NORMAL);
                return c;
            }
        };
        t.setRowHeight(28);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setFillsViewportHeight(true);
        t.setSelectionBackground(new Color(0xCFE0F5));
        t.setSelectionForeground(Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setFont(NEGRITA);
        return t;
    }

    public static void anchoColumnas(JTable t, int... anchos) {
        for (int i = 0; i < anchos.length; i++) {
            if (anchos[i] > 0) t.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    /** Escapa texto para usarlo dentro de etiquetas HTML de Swing. */
    public static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
