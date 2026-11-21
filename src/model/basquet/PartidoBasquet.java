package model.basquet;
import model.*;

public class PartidoBasquet {
    private Equipo equipoA;
    private Equipo equipoB;
    private int puntosA;
    private int puntosB;
    private Equipo ganador;

    public PartidoBasquet(Equipo equipoA, Equipo equipoB) {
        this.equipoA = equipoA;
        this.equipoB = equipoB;
    }

    public void registrarMarcador(int puntosA, int puntosB) {
        this.puntosA = puntosA;
        this.puntosB = puntosB;
        if (puntosA > puntosB) {
            this.ganador = equipoA;
        } else if (puntosB > puntosA) {
            this.ganador = equipoB;
        } else {
            this.ganador = null; // Empate
        }
    }

    public String getResumen() {
        String nombreGanador = (ganador != null) ? ganador.getNombre() : "Empate";
        return equipoA.getNombre() + " (PUNTOS: " + puntosA + " ) vs " + equipoB.getNombre() + " (PUNTOS: " + puntosB + " ) \nGANADOR: " + nombreGanador;
    }
}