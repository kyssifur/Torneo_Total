package src.TorneoTenisDeMesa;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Torneo de Tenis de Mesa: fase de grupos (todos contra todos) + llaves de eliminación directa.
 * UML: nombre, jugadores, grupos, llaves; registrarJugador(), crearGrupo(), crearLlaves().
 * Composición con Ranking y PartidoTT; agregación con JugadorTT.
 */
public class TorneoTenisDeMesa {
    public static final int MIN_JUGADORES = 4;
    public static final int TAMANO_GRUPO = 4;

    private String nombre;
    private ArrayList<JugadorTT> jugadores;
    private ArrayList<ArrayList<JugadorTT>> grupos;
    private ArrayList<PartidoTT> llaves;          // todos los partidos de eliminación directa
    private ArrayList<PartidoTT> partidosGrupo;   // partidos de la fase de grupos
    private Ranking ranking;

    private int inicioRondaActual = 0;            // índice en 'llaves' donde empieza la ronda en curso
    private ArrayList<JugadorTT> pasanPorBye = new ArrayList<>();
    private JugadorTT campeon;

    public TorneoTenisDeMesa(String nombre) {
        this.nombre = nombre;
        this.jugadores = new ArrayList<>();
        this.grupos = new ArrayList<>();
        this.llaves = new ArrayList<>();
        this.partidosGrupo = new ArrayList<>();
        this.ranking = new Ranking();
    }

    // ------------------------------------------------------------------ jugadores
    public void registrarJugador(JugadorTT jugador) {
        if (!grupos.isEmpty()) {
            throw new IllegalStateException("No se pueden registrar jugadores: los grupos ya fueron creados.");
        }
        if (jugadores.contains(jugador)) {
            throw new IllegalArgumentException("Ya existe un jugador con el id " + jugador.getId());
        }
        jugadores.add(jugador);
    }

    // ------------------------------------------------------------------ grupos
    /** Crea los grupos por "serpiente" según nivelFuerza y genera los partidos todos contra todos. */
    public void crearGrupo() {
        if (jugadores.size() < MIN_JUGADORES) {
            throw new IllegalStateException("Se necesitan al menos " + MIN_JUGADORES + " jugadores.");
        }
        if (!grupos.isEmpty()) {
            throw new IllegalStateException("Los grupos ya fueron creados.");
        }
        int numGrupos = (int) Math.ceil(jugadores.size() / (double) TAMANO_GRUPO);

        ArrayList<JugadorTT> porFuerza = new ArrayList<>(jugadores);
        porFuerza.sort(Comparator.comparingInt(JugadorTT::getNivelFuerza).reversed());

        for (int g = 0; g < numGrupos; g++) grupos.add(new ArrayList<>());

        // reparto en serpiente: 0,1,2 | 2,1,0 | 0,1,2 ...
        for (int i = 0; i < porFuerza.size(); i++) {
            int vuelta = i / numGrupos;
            int pos = i % numGrupos;
            int destino = (vuelta % 2 == 0) ? pos : numGrupos - 1 - pos;
            grupos.get(destino).add(porFuerza.get(i));
        }

        for (ArrayList<JugadorTT> grupo : grupos) {
            for (int i = 0; i < grupo.size(); i++) {
                for (int j = i + 1; j < grupo.size(); j++) {
                    partidosGrupo.add(new PartidoTT(grupo.get(i), grupo.get(j)));
                }
            }
        }
    }

    // ------------------------------------------------------------------ llaves
    /** Clasifica 2 por grupo y arma la primera ronda de eliminación directa. */
    public void crearLlaves() {
        if (grupos.isEmpty()) {
            throw new IllegalStateException("Primero debe crear los grupos.");
        }
        if (!llaves.isEmpty()) {
            throw new IllegalStateException("Las llaves ya fueron creadas.");
        }
        for (PartidoTT p : partidosGrupo) {
            if (!p.estaJugado()) {
                throw new IllegalStateException("Aún hay partidos de grupo pendientes.");
            }
        }

        ArrayList<JugadorTT> primeros = new ArrayList<>();
        ArrayList<JugadorTT> segundos = new ArrayList<>();
        for (ArrayList<JugadorTT> grupo : grupos) {
            List<JugadorTT> tabla = tablaGrupo(grupo);
            primeros.add(tabla.get(0));
            segundos.add(tabla.get(1));
        }
        ArrayList<JugadorTT> clasificados = new ArrayList<>(primeros);
        clasificados.addAll(segundos);

        armarRonda(clasificados);
    }

    /** Empareja el mejor sembrado con el peor (i vs n-1-i). Si es impar, el mejor pasa por bye. */
    private void armarRonda(List<JugadorTT> participantes) {
        inicioRondaActual = llaves.size();
        pasanPorBye.clear();
        int n = participantes.size();
        int desde = 0;
        if (n % 2 != 0) {
            pasanPorBye.add(participantes.get(0));
            desde = 1;
        }
        int i = desde, j = n - 1;
        while (i < j) {
            llaves.add(new PartidoTT(participantes.get(i), participantes.get(j)));
            i++;
            j--;
        }
    }

    /** Cuando termina la ronda actual, genera la siguiente o declara campeón. */
    public void avanzarRonda() {
        if (llaves.isEmpty()) {
            throw new IllegalStateException("Primero debe crear las llaves.");
        }
        if (campeon != null) {
            throw new IllegalStateException("El torneo ya terminó. Campeón: " + campeon.getNombre());
        }
        List<JugadorTT> avanzan = new ArrayList<>(pasanPorBye);
        for (int k = inicioRondaActual; k < llaves.size(); k++) {
            PartidoTT p = llaves.get(k);
            if (!p.estaJugado()) {
                throw new IllegalStateException("Hay partidos de la ronda actual sin resultado.");
            }
            avanzan.add(p.getGanador());
        }
        // ordenar por fuerza para mantener el sembrado
        avanzan.sort(Comparator.comparingInt(JugadorTT::getNivelFuerza).reversed());

        if (avanzan.size() == 1) {
            campeon = avanzan.get(0);
        } else {
            armarRonda(avanzan);
        }
    }

    // ------------------------------------------------------------------ resultados
    /** Registra marcador y aplica puntos de ranking; luego recalcula el ranking. */
    public void registrarResultado(PartidoTT partido, int setsA, int setsB) {
        partido.registrarMarcador(setsA, setsB);
        partido.registrarPuntos();
        ranking.calcular(jugadores);
    }

    // ------------------------------------------------------------------ tablas
    private int victorias(JugadorTT j) {
        int v = 0;
        for (PartidoTT p : partidosGrupo) {
            if (p.estaJugado() && p.getGanador().equals(j)) v++;
        }
        return v;
    }

    private int diferenciaSets(JugadorTT j) {
        int d = 0;
        for (PartidoTT p : partidosGrupo) {
            if (!p.estaJugado()) continue;
            if (p.getJugadorA().equals(j)) d += p.getSetsA() - p.getSetsB();
            else if (p.getJugadorB().equals(j)) d += p.getSetsB() - p.getSetsA();
        }
        return d;
    }

    /** Tabla de un grupo: victorias, diferencia de sets y nivel de fuerza. */
    public List<JugadorTT> tablaGrupo(List<JugadorTT> grupo) {
        List<JugadorTT> tabla = new ArrayList<>(grupo);
        tabla.sort(Comparator
            .comparingInt(this::victorias).reversed()
            .thenComparing(Comparator.comparingInt(this::diferenciaSets).reversed())
            .thenComparing(Comparator.comparingInt(JugadorTT::getNivelFuerza).reversed()));
        return tabla;
    }

    // ------------------------------------------------------------------ consola
    public void mostrarGrupos() {
        if (grupos.isEmpty()) {
            System.out.println("(los grupos aún no fueron creados)");
            return;
        }
        for (int g = 0; g < grupos.size(); g++) {
            System.out.println("=== GRUPO " + (char) ('A' + g) + " ===");
            int pos = 1;
            for (JugadorTT j : tablaGrupo(grupos.get(g))) {
                System.out.printf("  %d. %-15s V:%d  Dif.sets:%+d  (fuerza %d)%n",
                    pos++, j.getNombre(), victorias(j), diferenciaSets(j), j.getNivelFuerza());
            }
        }
        System.out.println("--- Partidos de grupo ---");
        for (PartidoTT p : partidosGrupo) System.out.println("  " + p);
    }

    public void mostrarLlaves() {
        if (llaves.isEmpty()) {
            System.out.println("(las llaves aún no fueron creadas)");
            return;
        }
        System.out.println("--- Llaves (eliminación directa) ---");
        for (int i = 0; i < llaves.size(); i++) {
            String marca = (i >= inicioRondaActual) ? "  [ronda actual] " : "  ";
            System.out.println(marca + llaves.get(i));
        }
        for (JugadorTT j : pasanPorBye) {
            System.out.println("  BYE: " + j.getNombre() + " pasa directo a la siguiente ronda");
        }
        if (campeon != null) {
            System.out.println("*** CAMPEÓN: " + campeon.getNombre() + " ***");
        }
    }

    public List<PartidoTT> partidosPendientesGrupo() {
        List<PartidoTT> res = new ArrayList<>();
        for (PartidoTT p : partidosGrupo) if (!p.estaJugado()) res.add(p);
        return res;
    }

    public List<PartidoTT> partidosPendientesLlaves() {
        List<PartidoTT> res = new ArrayList<>();
        for (int k = inicioRondaActual; k < llaves.size(); k++) {
            if (!llaves.get(k).estaJugado()) res.add(llaves.get(k));
        }
        return res;
    }

    // ------------------------------------------------------------------ getters
    public String getNombre() { return nombre; }
    public ArrayList<JugadorTT> getJugadores() { return jugadores; }
    public ArrayList<ArrayList<JugadorTT>> getGrupos() { return grupos; }
    public ArrayList<PartidoTT> getLlaves() { return llaves; }
    public ArrayList<PartidoTT> getPartidosGrupo() { return partidosGrupo; }
    public Ranking getRanking() { return ranking; }
    public JugadorTT getCampeon() { return campeon; }
}
