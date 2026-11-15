package model;

//Tabla de posiciones
public class FilaPosicionFutbol {

    private final Equipo equipo;
    private int jugados;
    private int ganados;
    private int empatados;
    private int perdidos;
    private int golesFavor;
    private int golesContra;

    public FilaPosicionFutbol(Equipo equipo) {
        this.equipo = equipo;
    }

    public void registrarPartido(int golesPropios, int golesRivales) {
        jugados++;
        golesFavor += golesPropios;
        golesContra += golesRivales;
        if (golesPropios > golesRivales) {
            ganados++;
        } else if (golesPropios == golesRivales) {
            empatados++;
        } else {
            perdidos++;
        }
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public int getJugados() {
        return jugados;
    }

    public int getGanados() {
        return ganados;
    }

    public int getEmpatados() {
        return empatados;
    }

    public int getPerdidos() {
        return perdidos;
    }

    public int getGolesFavor() {
        return golesFavor;
    }

    public int getGolesContra() {
        return golesContra;
    }

    public int getDiferenciaGoles() {
        return golesFavor - golesContra;
    }

    // 3 puntos por ganar y 1 por empate
    public int getPuntos() {
        return ganados * 3 + empatados;
    }
}
