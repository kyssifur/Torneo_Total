package src;
import java.util.*;

public class TorneoVoley {
    private String nombre;
    private List<equipo> equipos;
    private List<Grupo> grupos;
    private List<PartidoVoley> llaves;

    public TorneoVoley(String nombre) {
        this.nombre = nombre;
        this.equipos = new ArrayList<>();
        this.grupos = new ArrayList<>();
        this.llaves = new ArrayList<>();
    }

    public void registrarEquipoExistente(equipo eq) {
        this.equipos.add(eq);
    }

    public void formarEquipo(String nombreEquipo, int ranking, int fuerza, List<jugador> jugadores) {
        equipo nuevoEquipo = new equipo(nombreEquipo, ranking, fuerza);
        for (jugador j : jugadores) {
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