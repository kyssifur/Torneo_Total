package model.basquet;

import model.*;
import java.util.*;

public class TorneoBasquet extends TorneosEquipos {
    private String nombre;
    private ArrayList<Equipo> equipos;
    private ArrayList<Object> grupos;
    private ArrayList<PartidoBasquet> llaves;

    public TorneoBasquet(String nombre) {
        super();
        this.nombre = nombre;
        this.equipos = new ArrayList<>();
        this.grupos = new ArrayList<>();
        this.llaves = new ArrayList<>();
    }

    public void registrarEquipoExistente(Equipo equipo) {
        this.equipos.add(equipo);
    }

    public void formarEquipoDesdeBolsa(ArrayList<Jugador> bolsaJugadores, String nombreEquipo) {
        Equipo nuevo = new Equipo(nombreEquipo, equipos.size() + 1, 60);
        int jugadoresAsignados = 0;
        while (!bolsaJugadores.isEmpty() && jugadoresAsignados < 3) {
            nuevo.addJugador(bolsaJugadores.remove(0));
            jugadoresAsignados++;
        }
        this.equipos.add(nuevo);
    }

    public void crearGrupo() {
        System.out.println("Grupo creado para torneo de básquet.");
    }

    public String crearLlaves() {
        this.llaves.clear();
        if (equipos.size() < 2) {
            return "⚠ Se necesitan al menos 2 equipos inscritos para generar los enfrentamientos.\n";
        }
        
        ArrayList<Equipo> mezcla = new ArrayList<>(equipos);
        Collections.shuffle(mezcla);

        StringBuilder sb = new StringBuilder();
        sb.append("\n--- ENFRENTAMIENTOS GENERADOS ---\n");
        
        int totalEquipos = mezcla.size();
        int limite = totalEquipos - (totalEquipos % 2);

        for (int i = 0; i < limite; i += 2) {
            PartidoBasquet partido = new PartidoBasquet(mezcla.get(i), mezcla.get(i + 1));
            this.llaves.add(partido);
            sb.append("🏀 ").append(mezcla.get(i).getNombre())
              .append(" vs ").append(mezcla.get(i + 1).getNombre()).append("\n");
        }
        
        if (totalEquipos % 2 != 0) {
            Equipo equipoLibre = mezcla.get(totalEquipos - 1);
            sb.append("¡¡FELICIDADES!! Equipo ").append(equipoLibre.getNombre())
              .append(" pasa DIRECTO a la siguiente ronda por cantidad impar de equipos.\n");
        }
        
        return sb.toString();
    }

    public ArrayList<Equipo> getEquipos() {
        return this.equipos;
    }

    public ArrayList<PartidoBasquet> getLlaves() {
        return this.llaves;
    }
}