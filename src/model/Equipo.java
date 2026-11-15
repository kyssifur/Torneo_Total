package model;


import java.util.ArrayList;

/**
 * Equipo con su plantel de jugadores. CLASE COMPARTIDA: coordinar cambios con el equipo.
 */
public class Equipo {

    private final String nombre;
    private int ranking;
    private int nivelFuerza;
    private final ArrayList<Jugador> jugadores;

    public Equipo(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        }
        this.nombre = nombre;
        this.jugadores = new ArrayList<>();
    }

    public void addJugador(Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo");
        }
        for (Jugador j : jugadores) {
            if (j.getId().equals(jugador.getId())) {
                throw new IllegalArgumentException("Ya existe un jugador con id " + jugador.getId());
            }
            if (j.getNumeroCamiseta() == jugador.getNumeroCamiseta()) {
                throw new IllegalArgumentException("El numero de camiseta " + jugador.getNumeroCamiseta() + " ya esta en uso");
            }
        }
        jugadores.add(jugador);
    }

    public String getNombre() {
        return nombre;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getNivelFuerza() {
        return nivelFuerza;
    }

    public void setNivelFuerza(int nivelFuerza) {
        this.nivelFuerza = nivelFuerza;
    }

    /** Devuelve una copia para proteger la lista interna (encapsulamiento). */
    public ArrayList<Jugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public Jugador buscarJugadorPorId(String id) {
        for (Jugador j : jugadores) {
            if (j.getId().equals(id)) {
                return j;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipo)) return false;
        Equipo otro = (Equipo) o;
        return nombre.equalsIgnoreCase(otro.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
