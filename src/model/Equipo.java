package model;

import java.util.ArrayList;

/**
 * Equipo generico, compartido por todos los deportes.
 * Mantiene la API que ya usaban basquet y voley: Equipo(nombre, ranking, nivelFuerza),
 * getFuerza(), getRanking(), getNombre() y addJugador().
 * Las reglas propias de un deporte (por ejemplo camisetas repetidas en futbol)
 * van en una subclase, no aqui.
 */
public class Equipo {

    private final String nombre;
    private final int ranking;
    private final int nivelFuerza;
    private final ArrayList<Jugador> jugadores = new ArrayList<>();

    public Equipo(String nombre, int ranking, int nivelFuerza) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        }
        this.nombre = nombre;
        this.ranking = ranking;
        this.nivelFuerza = nivelFuerza;
    }

    public Equipo(String nombre) {
        this(nombre, 0, 0);
    }

    public void addJugador(Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo");
        }
        jugadores.add(jugador);
    }

    /** Devuelve una copia para proteger la lista interna (encapsulamiento). */
    public ArrayList<Jugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public int getFuerza() {
        return nivelFuerza;
    }

    public int getRanking() {
        return ranking;
    }

    public String getNombre() {
        return nombre;
    }

    /** Util para la GUI: un JComboBox o JList muestran el nombre del equipo. */
    @Override
    public String toString() {
        return nombre;
    }
}
