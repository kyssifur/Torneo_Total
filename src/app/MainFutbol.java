package app;

import java.util.ArrayList;

import model.Equipo;
import model.EventoPartido;
import model.FilaPosicionFutbol;
import model.Jugador;
import model.JugadorFutbol;
import model.PartidoFutbol;
import model.TorneoFutbol;

/**
 * Prueba por consola del modulo de futbol. TEMPORAL: cuando exista la GUI,
 * esta clase se reemplaza por el Main real de la aplicacion.
 */
public class MainFutbol {

    // Posicion de cada camiseta (1 a 11): 1 portero, 4 defensas, 3 medios, 3 delanteros
    private static final String[] POSICIONES = {
        "Portero", "Defensa", "Defensa", "Defensa", "Defensa",
        "Mediocampista", "Mediocampista", "Mediocampista",
        "Delantero", "Delantero", "Delantero"
    };

    public static void main(String[] args) {
        // 1. Equipos con 11 jugadores cada uno
        Equipo sistemas = crearEquipo("Ing. Sistemas", "SIS", new String[] {
            "Francisco Mamani", "Rony Mamani", "Carlos Huaman", "Jorge Condori", "Andres Flores",
            "Miguel Ccori", "Renzo Apaza", "Sergio Choque", "Marco Vilca", "Pablo Chambi", "Daniel Ramos"
        });
        Equipo mecanica = crearEquipo("Ing. Mecánica", "MEC", new String[] {
            "Fernando Caceres", "Ricardo Paredes", "Oscar Salas", "Victor Nunez", "Hugo Cruz",
            "Alvaro Medina", "Ivan Tapia", "Bruno Valdez", "Elias Rojas", "Heber Torres", "Joel Zuniga"
        });
        Equipo derecho = crearEquipo("Derecho", "DER", new String[] {
            "Mateo Salazar", "Rodrigo Pinto", "Cristian Delgado", "Nicolas Vargas", "Samuel Ortiz",
            "Adrian Cueva", "Kevin Mendoza", "Aldo Bejar", "Franco Linares", "Piero Arce", "Luciano Neyra"
        });

        // 2. Torneo
        TorneoFutbol torneo = new TorneoFutbol("Copa Interfacultades");
        torneo.registrarEquipoExistente(sistemas);
        torneo.registrarEquipoExistente(mecanica);
        torneo.registrarEquipoExistente(derecho);
        torneo.crearLlaves();

        System.out.println("===|> FIXTURE: " + torneo.getNombre() + " <|===");
        ArrayList<PartidoFutbol> partidos = torneo.getPartidos();
        for (int i = 0; i < partidos.size(); i++) {
            System.out.println(i + ") " + partidos.get(i));
        }

        // 3. Resultados y eventos
        JugadorFutbol delanteroSis = (JugadorFutbol) sistemas.getJugadores().get(9);   // #10
        JugadorFutbol delanteroMec = (JugadorFutbol) mecanica.getJugadores().get(9);   // #10
        JugadorFutbol porteroSis = (JugadorFutbol) sistemas.getJugadores().get(0);     // #1

        torneo.registrarResultado(0, 2, 1); // Sistemas 2-1 Mecanica
        partidos.get(0).agregarEvento(new EventoPartido(EventoPartido.Tipo.GOL, delanteroSis, 15));
        partidos.get(0).agregarEvento(new EventoPartido(EventoPartido.Tipo.GOL, delanteroMec, 30));
        partidos.get(0).agregarEvento(new EventoPartido(EventoPartido.Tipo.GOL, delanteroSis, 60));
        partidos.get(0).agregarEvento(new EventoPartido(EventoPartido.Tipo.TARJETA_AMARILLA, porteroSis, 75));

        torneo.registrarResultado(1, 1, 1); // Sistemas 1-1 Derecho
        torneo.registrarResultado(2, 0, 3); // Mecanica 0-3 Derecho

        // 4. Tabla de posiciones
        System.out.println();
        System.out.println("===|| TABLA DE POSICIONES ||===");
        System.out.printf("%-14s %3s %3s %3s %3s %3s %3s %4s %4s%n",
                "Equipo", "PJ", "G", "E", "P", "GF", "GC", "DG", "Pts");
        for (FilaPosicionFutbol f : torneo.calcularTabla()) {
            System.out.printf("%-14s %3d %3d %3d %3d %3d %3d %4d %4d%n",
                    f.getEquipo().getNombre(), f.getJugados(), f.getGanados(), f.getEmpatados(),
                    f.getPerdidos(), f.getGolesFavor(), f.getGolesContra(),
                    f.getDiferenciaGoles(), f.getPuntos());
        }

        // 5. Plantel y estadisticas
        System.out.println();
        System.out.println("===|| PLANTEL DE " + sistemas.getNombre().toUpperCase() + " ||===");
        for (Jugador j : sistemas.getJugadores()) {
            System.out.println(j);
        }

        System.out.println();
        System.out.println("===|| JUGADORES CON GOLES O TARJETAS ||===");
        for (Equipo e : torneo.getEquipos()) {
            for (Jugador j : e.getJugadores()) {
                JugadorFutbol jf = (JugadorFutbol) j;
                if (jf.getGoles() > 0 || jf.getTarjetasAmarillas() > 0 || jf.getTarjetasRojas() > 0) {
                    System.out.println(e.getNombre() + " - " + jf);
                }
            }
        }

        // 6. Pruebas de validacion
        System.out.println();
        System.out.println("===|| PRUEBAS DE ERRORES ||===");
        probar("Goles negativos", () -> torneo.registrarResultado(0, -1, 2));
        probar("Partido ya jugado", () -> torneo.registrarResultado(0, 1, 0));
        TorneoFutbol torneoNuevo = new TorneoFutbol("Torneo de prueba");
        torneoNuevo.registrarEquipoExistente(sistemas);
        probar("Equipo repetido", () -> torneoNuevo.registrarEquipoExistente(sistemas));
        probar("Generar fixture con 1 solo equipo", torneoNuevo::crearLlaves);
        probar("Camiseta repetida", () -> sistemas.addJugador(
                new JugadorFutbol("SIS-99", "Pedro Nuevo", "Defensa", 9, false)));
        probar("Agregar equipo con fixture generado",
                () -> torneo.registrarEquipoExistente(new Equipo("Medicina")));
    }

    /** Crea un equipo de 11 jugadores titulares con camisetas del 1 al 11. */
    private static Equipo crearEquipo(String nombre, String prefijoId, String[] jugadores) {
        Equipo equipo = new Equipo(nombre);
        for (int i = 0; i < jugadores.length; i++) {
            String id = prefijoId + "-" + String.format("%02d", i + 1);
            equipo.addJugador(new JugadorFutbol(id, jugadores[i], POSICIONES[i], i + 1, true));
        }
        return equipo;
    }

    private static void probar(String descripcion, Runnable accion) {
        try {
            accion.run();
            System.out.println("[FALLO] " + descripcion + ": no lanzo excepcion");
        } catch (RuntimeException e) {
            System.out.println("[OK] " + descripcion + " -> " + e.getMessage());
        }
    }
}