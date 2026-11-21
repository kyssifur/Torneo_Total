package view.tenisdemesa;

import model.tenisdemesa.JugadorTT;
import model.tenisdemesa.TorneoTenisDeMesa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Pestaña 1: registrar, editar y eliminar jugadores; desde aquí se crean los grupos. */
public class PanelJugadores extends JPanel {
    private final VentanaPrincipal app;
    private final FormularioJugador form;
    private final DefaultTableModel modelo = Estilo.modelo("#", "Nombre", "ID", "Fuerza");
    private final JTable tabla = Estilo.tabla(modelo, null, 1);
    private final JLabel lblContador = Estilo.subtitulo(" ");
    private final JLabel lblAviso = new JLabel(" ");

    private final JButton btnAgregar = Estilo.boton("Agregar jugador", Estilo.VERDE);
    private final JButton btnEditar = Estilo.boton("Editar", Estilo.AZUL_CLARO);
    private final JButton btnEliminar = Estilo.boton("Eliminar", Estilo.ROJO);
    private final JButton btnGrupos = Estilo.boton("Crear grupos  ▶", Estilo.NARANJA);

    private boolean bloqueado = false;

    public PanelJugadores(VentanaPrincipal app) {
        this.app = app;
        this.form = new FormularioJugador(app::getTorneo);

        setLayout(new BorderLayout(16, 0));
        setBackground(Estilo.FONDO);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // ---- izquierda: formulario de alta
        JPanel izq = Estilo.tarjeta(new BorderLayout(0, 12));
        izq.setPreferredSize(new Dimension(310, 0));
        izq.add(Estilo.subtitulo("Nuevo jugador"), BorderLayout.NORTH);
        izq.add(form, BorderLayout.CENTER);
        JPanel botonesIzq = new JPanel(new GridLayout(0, 1, 0, 8));
        botonesIzq.setOpaque(false);
        botonesIzq.add(btnAgregar);
        izq.add(botonesIzq, BorderLayout.SOUTH);
        add(izq, BorderLayout.WEST);

        // ---- derecha: lista de jugadores
        JPanel der = Estilo.tarjeta(new BorderLayout(0, 10));
        der.add(lblContador, BorderLayout.NORTH);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Estilo.anchoColumnas(tabla, 40, 260, 80, 70);
        der.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout(0, 10));
        sur.setOpaque(false);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.setOpaque(false);
        acciones.add(btnEditar);
        acciones.add(btnEliminar);
        sur.add(acciones, BorderLayout.NORTH);
        JPanel pie = new JPanel(new BorderLayout(12, 0));
        pie.setOpaque(false);
        lblAviso.setFont(Estilo.NORMAL);
        lblAviso.setForeground(Estilo.GRIS);
        pie.add(lblAviso, BorderLayout.CENTER);
        pie.add(btnGrupos, BorderLayout.EAST);
        sur.add(pie, BorderLayout.SOUTH);
        der.add(sur, BorderLayout.SOUTH);
        add(der, BorderLayout.CENTER);

        // ---- acciones
        btnAgregar.addActionListener(e -> agregar());
        form.alPresionarEnter(e -> agregar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnGrupos.addActionListener(e -> crearGrupos());
        tabla.getSelectionModel().addListSelectionListener(e -> actualizarBotones());
        tabla.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabla.getSelectedRow() >= 0) editar();
            }
        });
    }

    // ------------------------------------------------------------------ acciones
    private void agregar() {
        if (bloqueado || !form.validar(null)) return;
        try {
            app.getTorneo().registrarJugador(new JugadorTT(form.getNombre(), form.getId(), form.getFuerza()));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            form.setError(ex.getMessage());
            return;
        }
        form.limpiar();
        app.refrescarTodo();
    }


    private JugadorTT seleccionado() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : app.getTorneo().getJugadores().get(fila);
    }

    private void editar() {
        JugadorTT j = seleccionado();
        if (j == null) return;
        if (DialogoJugador.editar(this, app.getTorneo(), j, bloqueado)) {
            app.refrescarTodo();
        }
    }

    private void eliminar() {
        JugadorTT j = seleccionado();
        if (j == null || bloqueado) return;
        int op = JOptionPane.showConfirmDialog(this,
            "¿Eliminar a " + j.getNombre() + " [" + j.getId() + "] del torneo?",
            "Eliminar jugador", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (op != JOptionPane.YES_OPTION) return;
        app.getTorneo().getJugadores().remove(j);
        form.limpiar();
        app.refrescarTodo();
    }

    private void crearGrupos() {
        TorneoTenisDeMesa t = app.getTorneo();
        int n = t.getJugadores().size();
        int numGrupos = (int) Math.ceil(n / (double) TorneoTenisDeMesa.TAMANO_GRUPO);
        int op = JOptionPane.showConfirmDialog(this,
            "Se crearán " + numGrupos + " grupo(s) con los " + n + " jugadores.\n"
            + "Después no podrás agregar ni eliminar jugadores.\n\n¿Continuar?",
            "Crear grupos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op != JOptionPane.YES_OPTION) return;
        try {
            t.crearGrupo();
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pueden crear los grupos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        app.refrescarTodo();
        app.irAPestana(1);
    }

    // ------------------------------------------------------------------ estado
    public void reiniciarFormulario() { form.limpiar(); }

    public void refrescar() {
        TorneoTenisDeMesa t = app.getTorneo();
        bloqueado = !t.getGrupos().isEmpty();
        int n = t.getJugadores().size();

        int seleccion = tabla.getSelectedRow();
        modelo.setRowCount(0);
        int i = 1;
        for (JugadorTT j : t.getJugadores()) {
            modelo.addRow(new Object[]{i++, j.getNombre(), j.getId(), j.getNivelFuerza()});
        }
        if (seleccion >= 0 && seleccion < n) tabla.setRowSelectionInterval(seleccion, seleccion);

        lblContador.setText("Jugadores registrados: " + n);
        form.activar(!bloqueado);
        btnAgregar.setEnabled(!bloqueado);
        btnGrupos.setEnabled(!bloqueado && n >= TorneoTenisDeMesa.MIN_JUGADORES);

        if (bloqueado) {
            lblAviso.setText("<html>Los grupos ya están creados. Solo se pueden editar los nombres.<br>"
                + "Para cambiar jugadores, inicia un nuevo torneo.</html>");
        } else if (n < TorneoTenisDeMesa.MIN_JUGADORES) {
            lblAviso.setText("Faltan " + (TorneoTenisDeMesa.MIN_JUGADORES - n)
                + " jugador(es) para poder crear grupos (mínimo " + TorneoTenisDeMesa.MIN_JUGADORES + ").");
        } else {
            int grupos = (int) Math.ceil(n / (double) TorneoTenisDeMesa.TAMANO_GRUPO);
            lblAviso.setText("Listo: se formarán " + grupos + " grupo(s) de hasta "
                + TorneoTenisDeMesa.TAMANO_GRUPO + " jugadores.");
        }
        actualizarBotones();
    }

    private void actualizarBotones() {
        boolean haySeleccion = tabla.getSelectedRow() >= 0;
        btnEditar.setEnabled(haySeleccion);
        btnEliminar.setEnabled(haySeleccion && !bloqueado);
    }
}