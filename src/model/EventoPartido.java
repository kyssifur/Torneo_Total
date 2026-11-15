package model;

public class EventoPartido {
    public enum Tipo { GOL, TARJETA_AMARILLA, TARJETA_ROJA }

    private final Tipo tipo;
    private final JugadorFutbol jugador;
    private final int minuto;

    public EventoPartido(Tipo tipo, JugadorFutbol jugador, int minuto) {
        if (tipo == null || jugador == null) {
            throw new IllegalArgumentException("El evento necesita tipo y jugador");
        }
        if (minuto < 0 || minuto > 130) {
            throw new IllegalArgumentException("Minuto invalido: " + minuto);
        }
        this.tipo = tipo;
        this.jugador = jugador;
        this.minuto = minuto;
    }
    public Tipo getTipo() {
        return tipo;
    }
    public JugadorFutbol getJugador() {
        return jugador;
    }
    public int getMinuto() {
        return minuto;
    }
    public String toString() {
        return minuto + "' " + tipo + " - " + jugador.getNombre();
    }
}
