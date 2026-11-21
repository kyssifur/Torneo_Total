package model.futbol;

import java.util.ArrayList;

import model.Equipo;
import model.Jugador;

/**
 * Equipo de futbol: hereda de Equipo y agrega las reglas del deporte
 * (solo JugadorFutbol, sin ids ni camisetas repetidos).
 */
public class EquipoFutbol extends Equipo {

    public EquipoFutbol(String nombre) {
        super(nombre);
    }

    public EquipoFutbol(String nombre, int ranking, int nivelFuerza) {
        super(nombre, ranking, nivelFuerza);
    }

    @Override
    public void addJugador(Jugador j) {
        if (!(j instanceof JugadorFutbol)) {
            throw new IllegalArgumentException("Un equipo de futbol solo acepta JugadorFutbol");
        }
        JugadorFutbol nuevo = (JugadorFutbol) j;
        for (Jugador p : getJugadores()) {
            if (p.getId().equals(nuevo.getId())) {
                throw new IllegalArgumentException("Ya existe un jugador con id " + nuevo.getId());
            }
            if (p.getNumeroCamiseta() == nuevo.getNumeroCamiseta()) {
                throw new IllegalArgumentException("El numero de camiseta " + nuevo.getNumeroCamiseta() + " ya esta en uso");
            }
        }
        super.addJugador(nuevo);
    }

    /** Plantel tipado como JugadorFutbol (copia de la lista interna). */
    public ArrayList<JugadorFutbol> getPlantel() {
        ArrayList<JugadorFutbol> plantel = new ArrayList<>();
        for (Jugador j : getJugadores()) {
            plantel.add((JugadorFutbol) j);
        }
        return plantel;
    }
}
