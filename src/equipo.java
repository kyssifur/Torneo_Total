package src;
import java.util.*;
public class equipo {
    private String nombre;
    private int ranking;
    private int nivelFuerza;
    private ArrayList<jugador> jugadores;

    public equipo(String nombre, int ranking, int nivelFuerza) {
        this.nombre = nombre;
        this.ranking = ranking;
        this.nivelFuerza = nivelFuerza;
        this.jugadores = new ArrayList<>();
    }

    public void addJugador(jugador jugador) {
        this.jugadores.add(jugador);
    }

    public int getFuerza() {
        return this.nivelFuerza;
    }

    public int getRanking() {
        return this.ranking;
    }

    public String getNombre() {
        return this.nombre;
    }
}