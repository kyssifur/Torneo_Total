import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Ranking del torneo.
 * UML: posiciones : HashMap  (jugador -> posición), calcular().
 */
public class Ranking {
    private HashMap<JugadorTT, Integer> posiciones;

    public Ranking() {
        this.posiciones = new HashMap<>();
    }

    /**
     * Recalcula las posiciones ordenando por puntos de ranking (desc),
     * luego nivel de fuerza (desc) y luego nombre.
     * @return jugadores ordenados de 1° a N°.
     */
    public List<JugadorTT> calcular(List<JugadorTT> jugadores) {
        List<JugadorTT> ordenados = new ArrayList<>(jugadores);
        ordenados.sort(Comparator
            .comparingInt(JugadorTT::getRanking).reversed()
            .thenComparing(Comparator.comparingInt(JugadorTT::getNivelFuerza).reversed())
            .thenComparing(JugadorTT::getNombre));
        posiciones.clear();
        for (int i = 0; i < ordenados.size(); i++) {
            posiciones.put(ordenados.get(i), i + 1);
        }
        return ordenados;
    }

    public int getPosicion(JugadorTT jugador) {
        return posiciones.getOrDefault(jugador, -1);
    }

    public HashMap<JugadorTT, Integer> getPosiciones() { return posiciones; }

    public void mostrar(List<JugadorTT> jugadores) {
        List<JugadorTT> ordenados = calcular(jugadores);
        System.out.println("---------- RANKING ----------");
        if (ordenados.isEmpty()) {
            System.out.println("(sin jugadores)");
        }
        for (JugadorTT j : ordenados) {
            System.out.printf("%2d. %-15s %3d pts%n", posiciones.get(j), j.getNombre(), j.getRanking());
        }
    }
}