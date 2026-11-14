package src;

public class PartidoVoley {
    private equipo equipoA;
    private equipo equipoB;
    private int setsGanadosA;
    private int setsGanadosB;
    private equipo ganador;

    public PartidoVoley(equipo equipoA, equipo equipoB) {
        this.equipoA = equipoA;
        this.equipoB = equipoB;
        this.setsGanadosA = 0;
        this.setsGanadosB = 0;
    }

    public void registrarPunto(equipo eq) {
    }

    public void registrarMarcador(int setsA, int setsB) {
        this.setsGanadosA = setsA;
        this.setsGanadosB = setsB;
        if (this.setsGanadosA >= 3) {
            this.ganador = equipoA;
        } else if (this.setsGanadosB >= 3) {
            this.ganador = equipoB;
        }
    }
}
