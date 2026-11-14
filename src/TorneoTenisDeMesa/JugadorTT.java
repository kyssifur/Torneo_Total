/**
 * Jugador de Tenis de Mesa.
 * UML: nombre, id, ranking, nivelFuerza.
 */
public class JugadorTT {
    private String nombre;
    private String id;
    private int ranking;      // puntos acumulados de ranking
    private int nivelFuerza;  // 1 (bajo) a 10 (alto)

    public JugadorTT(String nombre, String id, int nivelFuerza) {
        if (nivelFuerza < 1 || nivelFuerza > 10) {
            throw new IllegalArgumentException("El nivel de fuerza debe estar entre 1 y 10.");
        }
        this.nombre = nombre;
        this.id = id;
        this.nivelFuerza = nivelFuerza;
        this.ranking = 0;
    }

    public void sumarPuntos(int puntos) { this.ranking += puntos; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getRanking() { return ranking; }
    public void setRanking(int ranking) { this.ranking = ranking; }
    public int getNivelFuerza() { return nivelFuerza; }
    public void setNivelFuerza(int nivelFuerza) { this.nivelFuerza = nivelFuerza; }

    @Override
    public boolean equals(Object o) {
        return o instanceof JugadorTT && ((JugadorTT) o).id.equals(this.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }

    @Override
    public String toString() {
        return nombre + " [" + id + "] (fuerza " + nivelFuerza + ", ranking " + ranking + " pts)";
    }
}