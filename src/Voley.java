package src;

public class Voley extends Deporte {
    private int setsParaGanar;
    private int puntosSetNormal;
    private int puntosTieBreak;

    public Voley() {
        super("Voleibol");
        this.setsParaGanar = 3;
        this.puntosSetNormal = 25;
        this.puntosTieBreak = 15;
    }

    public boolean esSetDecisivo(int setsJugados) {
        return setsJugados == (this.setsParaGanar * 2) - 2;
    }

    public boolean evaluarGanadorSet(int puntosA, int puntosB, boolean decisivo) {
        int limite = decisivo ? this.puntosTieBreak : this.puntosSetNormal;
        return (puntosA >= limite || puntosB >= limite) && Math.abs(puntosA - puntosB) >= 2;
    }
}
