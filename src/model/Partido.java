package model;

/**
 * Clase abstracta base para los partidos de deportes por equipos.
 * PROPUESTA: CLASE COMPARTIDA, acordar con el equipo antes de que otros la usen.
 * Permite polimorfismo: un torneo puede tratar cualquier partido como Partido.
 */
public abstract class Partido {

    private final Equipo equipoA;
    private final Equipo equipoB;
    private boolean jugado;

    protected Partido(Equipo equipoA, Equipo equipoB) {
        if (equipoA == null || equipoB == null) {
            throw new IllegalArgumentException("Un partido necesita dos equipos");
        }
        if (equipoA == equipoB) {
            throw new IllegalArgumentException("Un equipo no puede jugar contra si mismo");
        }
        this.equipoA = equipoA;
        this.equipoB = equipoB;
        this.jugado = false;
    }

    public Equipo getEquipoA() {
        return equipoA;
    }

    public Equipo getEquipoB() {
        return equipoB;
    }

    public boolean isJugado() {
        return jugado;
    }

    protected void marcarComoJugado() {
        this.jugado = true;
    }

    /** Cada deporte define como se registra su marcador. */
    public abstract void registrarMarcador(int puntosA, int puntosB);

    /** Devuelve el ganador, o null si el partido no se jugo o termino empatado. */
    public abstract Equipo getGanador();
}
