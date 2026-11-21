package model.voley;

import model.*;

import java.util.*;

public class TorneoVoley {
    private String nombre;
    private List<Equipo> equipos;
    private List<Grupo> grupos;
    private List<PartidoVoley> llaves;

    public TorneoVoley(String nombre) {
        this.nombre = nombre;
        this.equipos = new ArrayList<>();
        this.grupos = new ArrayList<>();
        this.llaves = new ArrayList<>();
    }

    public void registrarEquipoExistente(Equipo eq) {
        this.equipos.add(eq);
    }

    public void formarEquipo(String nombreEquipo, int ranking, int fuerza, List<Jugador> jugadores) {
        Equipo nuevoEquipo = new Equipo(nombreEquipo, ranking, fuerza);
        for (Jugador j : jugadores) {
            nuevoEquipo.addJugador(j);
        }
        this.equipos.add(nuevoEquipo);
    }

    public void crearGrupo(Grupo grupo) {
        this.grupos.add(grupo);
    }

    public void crearLlaves(PartidoVoley partido) {
        this.llaves.add(partido);
    }
}