package controller;

import model.*;
import model.basquet.*;
import java.util.ArrayList;

public class BasquetController {
    private TorneoBasquet torneo;
    private ArrayList<Jugador> bolsaJugadoresGlobal;

    public BasquetController(String nombreTorneo) {
        this.torneo = new TorneoBasquet(nombreTorneo);
        this.bolsaJugadoresGlobal = new ArrayList<>();
    }

    public void agregarJugadorABolsa(String nombre, int ranking, int fuerza) {
        bolsaJugadoresGlobal.add(new Jugador(nombre, ranking, fuerza));
    }

    public void handleAgregarEquipo(String nombre, int ranking, int fuerza) {
        Equipo nuevoEquipo = new Equipo(nombre, ranking, fuerza);
        torneo.registrarEquipoExistente(nuevoEquipo);
    }

    public String handleFormarEquipoConJugadores(String nombreEquipo) {
        if (bolsaJugadoresGlobal.isEmpty()) {
            return "No hay jugadores en la bolsa. Registra jugadores primero.";
        }
        torneo.formarEquipoDesdeBolsa(bolsaJugadoresGlobal, nombreEquipo);
        return "✔ Equipo '" + nombreEquipo + "' generado con éxito desde la bolsa de jugadores.";
    }

    public String handleCrearLlaves() {
        return torneo.crearLlaves();
    }

    public String obtenerEquiposTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- EQUIPOS INSCRITOS (Total: ").append(torneo.getEquipos().size()).append(") ---\n");
        for (Equipo e : torneo.getEquipos()) {
            sb.append("- ").append(e.getNombre()).append(" (Fuerza: ").append(e.getFuerza()).append(")\n");
        }
        return sb.toString();
    }
    
    public TorneoBasquet getTorneo() {
        return torneo;
    }
}