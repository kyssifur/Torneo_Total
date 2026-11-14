/**
 * Partido de Tenis de Mesa al mejor de 5 sets (gana quien llegue a 3).
 * UML: jugadorA, jugadorB, setsA, setsB, ganador.
 */
public class PartidoTT {
    public static final int SETS_PARA_GANAR = 3;
    public static final int PUNTOS_VICTORIA = 3;
    public static final int PUNTOS_DERROTA = 1;

    private static int contador = 0;

    private final int idPartido;
    private JugadorTT jugadorA;
    private JugadorTT jugadorB;
    private int setsA;
    private int setsB;
    private JugadorTT ganador;
    private boolean puntosAplicados = false;

    public PartidoTT(JugadorTT jugadorA, JugadorTT jugadorB) {
        if (jugadorA.equals(jugadorB)) {
            throw new IllegalArgumentException("Un jugador no puede jugar contra sí mismo.");
        }
        this.idPartido = ++contador;
        this.jugadorA = jugadorA;
        this.jugadorB = jugadorB;
    }

    /** Registra el marcador final en sets y determina el ganador. */
    public void registrarMarcador(int setsA, int setsB) {
        if (ganador != null) {
            throw new IllegalStateException("El partido ya tiene un resultado registrado.");
        }
        boolean valido = (setsA == SETS_PARA_GANAR && setsB >= 0 && setsB < SETS_PARA_GANAR)
                      || (setsB == SETS_PARA_GANAR && setsA >= 0 && setsA < SETS_PARA_GANAR);
        if (!valido) {
            throw new IllegalArgumentException(
                "Marcador inválido: un jugador debe llegar a " + SETS_PARA_GANAR + " sets y el otro menos.");
        }
        this.setsA = setsA;
        this.setsB = setsB;
        this.ganador = (setsA > setsB) ? jugadorA : jugadorB;
    }

    /** Asigna puntos de ranking: 3 al ganador y 1 al perdedor (una sola vez). */
    public void registrarPuntos() {
        if (ganador == null) {
            throw new IllegalStateException("Primero debe registrarse el marcador.");
        }
        if (puntosAplicados) return;
        JugadorTT perdedor = (ganador.equals(jugadorA)) ? jugadorB : jugadorA;
        ganador.sumarPuntos(PUNTOS_VICTORIA);
        perdedor.sumarPuntos(PUNTOS_DERROTA);
        puntosAplicados = true;
    }

    public boolean estaJugado() { return ganador != null; }

    public int getIdPartido() { return idPartido; }
    public JugadorTT getJugadorA() { return jugadorA; }
    public JugadorTT getJugadorB() { return jugadorB; }
    public int getSetsA() { return setsA; }
    public int getSetsB() { return setsB; }
    public JugadorTT getGanador() { return ganador; }

    @Override
    public String toString() {
        String res = estaJugado()
            ? setsA + " - " + setsB + " | Ganador: " + ganador.getNombre()
            : "pendiente";
        return "#" + idPartido + " " + jugadorA.getNombre() + " vs " + jugadorB.getNombre() + " -> " + res;
    }
}