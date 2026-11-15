package src;

public class jugador {
    private String nombre;
    private String id;
    private String posicion;
    private int numeroCamiseta;

    public jugador(String nombre, String id, String posicion, int numeroCamiseta) {
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
}