package model.basquet;

import model.Equipo;

public class PartidoBasquet {
    private Equipo equipoA;
    private Equipo equipoB;
    private int puntosA;
    private int puntosB;
    private Equipo ganador;

    public PartidoBasquet(Equipo equipoA, Equipo equipoB) {
        this.equipoA = equipoA;
        this.equipoB = equipoB;
        this.puntosA = 0;
        this.puntosB = 0;
    }

    public void registrarMarcador(int puntosA, int puntosB) {
        this.puntosA = puntosA;
        this.puntosB = puntosB;
        if (puntosA > puntosB) {
            this.ganador = equipoA;
        } else if (puntosB > puntosA) {
            this.ganador = equipoB;
        } else {
            this.ganador = null;
        }
    }

    public String getResumen() {
        String nombreGanador = (ganador != null) ? ganador.getNombre() : "Empate / En juego";
        return equipoA.getNombre() + " (" + puntosA + ") vs " + equipoB.getNombre() + " (" + puntosB + ") \nGanador: " + nombreGanador;
    }

    public Equipo getEquipoA() { return equipoA; }
    public Equipo getEquipoB() { return equipoB; }
    public int getPuntosA() { return puntosA; }
    public int getPuntosB() { return puntosB; }
    public Equipo getGanador() { return ganador; }
}