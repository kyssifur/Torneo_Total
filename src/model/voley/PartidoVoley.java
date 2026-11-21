package model.voley;
import model.Equipo;

public class PartidoVoley {
    private Equipo equipoA;
    private Equipo equipoB;
    private int setsGanadosA;
    private int setsGanadosB;
    private Equipo ganador;

    public PartidoVoley(Equipo equipoA, Equipo equipoB) {
        this.equipoA = equipoA;
        this.equipoB = equipoB;
        this.setsGanadosA = 0;
        this.setsGanadosB = 0;
    }

    public void registrarPunto(Equipo eq) {
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