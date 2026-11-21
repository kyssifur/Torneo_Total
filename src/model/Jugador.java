package model;
public class Jugador {

    private final String nombre;
    private String id;
    private String posicion;
    private int numeroCamiseta;
    private int ranking;
    private int fuerza;

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
        this.ranking = 0;
        this.fuerza = 0;
    }

    public Jugador(String nombre, int ranking, int fuerza) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del jugador es obligatorio");
        }
        this.nombre = nombre;
        this.id = "AUTO-" + System.currentTimeMillis();
        this.posicion = "General";
        this.numeroCamiseta = 0;
        this.ranking = ranking;
        this.fuerza = fuerza;
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

    public int getRanking() {
        return ranking;
    }

    public int getFuerza() {
        return fuerza;
    }

    public String toString() {
        return nombre + " (#" + numeroCamiseta + ", " + posicion + ")";
    }
}