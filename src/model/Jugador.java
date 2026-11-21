package model;

/**
 * Jugador generico, compartido por todos los deportes.
 * Constructor: (nombre, id, posicion, numeroCamiseta), el mismo orden que ya usaba el equipo.
 */
public class Jugador {

    private final String nombre;
    private final String id;
    private final String posicion;
    private final int numeroCamiseta;

    public Jugador(String nombre, String id, String posicion, int numeroCamiseta) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del jugador es obligatorio");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del jugador es obligatorio");
        }
        if (numeroCamiseta < 0) {
            throw new IllegalArgumentException("El numero de camiseta no puede ser negativo");
        }
        this.nombre = nombre;
        this.id = id;
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public String getPosicion() {
        return posicion;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    @Override
    public String toString() {
        return nombre + " (#" + numeroCamiseta + ", " + posicion + ")";
    }
}
