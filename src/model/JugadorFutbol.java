package model;

public class JugadorFutbol extends Jugador {

    private int goles;
    private int tarjetasAmarillas;
    private int tarjetasRojas;
    private boolean titular;

    public JugadorFutbol(String id, String nombre, String posicion, int numeroCamiseta, boolean titular) {
        super(id, nombre, posicion, numeroCamiseta);
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

    public String toString() {
        String estadoTitular = "";
        if (titular) {
            estadoTitular = ", titular";
        }
        return super.toString() + " [goles=" + goles + ", amarillas=" + tarjetasAmarillas
                + ", rojas=" + tarjetasRojas + estadoTitular + "]";
        }
}
