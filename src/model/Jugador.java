package model;


/**
 * Jugador generico. CLASE COMPARTIDA: coordinar cambios con el equipo.
 */
public class Jugador {

    private String id;
    private String nombre;
    private String posicion;
    private int numeroCamiseta;

    public Jugador(String id, String nombre, String posicion, int numeroCamiseta) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del jugador es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del jugador es obligatorio");
        }
        if (numeroCamiseta < 0) {
            throw new IllegalArgumentException("El numero de camiseta no puede ser negativo");
        }
        this.id = id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public String toString() {
        return nombre + " (#" + numeroCamiseta + ", " + posicion + ")";
    }
}

