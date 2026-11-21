package model.futbol;

import model.Jugador;

/**
 * Jugador de futbol: hereda de Jugador y agrega estadisticas propias del deporte.
 */
public class JugadorFutbol extends Jugador {

    private int goles;
    private int tarjetasAmarillas;
    private int tarjetasRojas;
    private boolean titular;

    // Mismo orden de parametros que Jugador: nombre, id, posicion, camiseta
    public JugadorFutbol(String nombre, String id, String posicion, int numeroCamiseta, boolean titular) {
        super(nombre, id, posicion, numeroCamiseta);
        this.titular = titular;
    }

    public void registrarGol() {
        goles++;
    }

    public void registrarTarjetaAmarilla() {
        tarjetasAmarillas++;
    }

    public void registrarTarjetaRoja() {
        tarjetasRojas++;
    }

    public int getGoles() {
        return goles;
    }

    public int getTarjetasAmarillas() {
        return tarjetasAmarillas;
    }

    public int getTarjetasRojas() {
        return tarjetasRojas;
    }

    public boolean isTitular() {
        return titular;
    }

    public void setTitular(boolean titular) {
        this.titular = titular;
    }

    @Override
    public String toString() {
        return super.toString() + " [goles=" + goles + ", amarillas=" + tarjetasAmarillas
                + ", rojas=" + tarjetasRojas + (titular ? ", titular" : "") + "]";
    }
}
