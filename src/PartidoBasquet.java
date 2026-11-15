package src;

public class PartidoBasquet {
    private equipo equipoA;
    private equipo equipoB;
    private int puntosA;
    private int puntosB;
    private equipo ganador;

    public PartidoBasquet(equipo equipoA, equipo equipoB) {
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