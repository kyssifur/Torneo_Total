package model.voley;
import java.util.ArrayList;
import model.Equipo;
import model.Grupo;
import model.Jugador;

public class TorneoVoley {
    private String nombre;
    private ArrayList<Equipo> equipos;
    private ArrayList<Grupo> grupos;
    private ArrayList<PartidoVoley> llaves;

    public TorneoVoley(String nombre) {
        this.nombre = nombre;
        this.equipos = new ArrayList<Equipo>();
        this.grupos = new ArrayList<Grupo>();
        this.llaves = new ArrayList<PartidoVoley>();
    }

    public void registrarEquipoExistente(Equipo eq) {
        this.equipos.add(eq);
    }

    public void formarEquipo(String nombreEquipo, int ranking, int fuerza, ArrayList<Jugador> jugadores) {
        Equipo nuevoEquipo = new Equipo(nombreEquipo, ranking, fuerza);
        for (int i = 0; i < jugadores.size(); i++) {
            nuevoEquipo.addJugador(jugadores.get(i));
        }
        this.equipos.add(nuevoEquipo);
    }

    public void crearGrupo(Grupo grupo) {
        this.grupos.add(grupo);
    }

    public void crearLlaves(PartidoVoley partido) {
        this.llaves.add(partido);
    }

    public String obtenerEquiposTexto() {
        if (equipos.size() == 0) {
            return "No hay equipos registrados.";
        }
        String texto = "Equipos Inscritos \n";
        for (int i = 0; i < equipos.size(); i++) {
            Equipo eq = equipos.get(i);
            texto += "- " + eq.getNombre() + " Ranking: " + eq.getRanking() + " Fuerza: " + eq.getFuerza() + "\n";
        }
        return texto;
    }

    public String crearLlavesAleatoriasTexto() {
        String texto = "Llaves Generadas \n";
        ArrayList<Equipo> copia = new ArrayList<Equipo>();
        for (int i = 0; i < equipos.size(); i++) {
            copia.add(equipos.get(i));
        }

        while (copia.size() >= 2) {
            int idxA = (int) (Math.random() * copia.size());
            Equipo eqA = copia.remove(idxA);
            int idxB = (int) (Math.random() * copia.size());
            Equipo eqB = copia.remove(idxB);
            PartidoVoley partido = new PartidoVoley(eqA, eqB);
            this.llaves.add(partido);
            texto += eqA.getNombre() + " vs " + eqB.getNombre() + "\n";
        }
        return texto;
    }
}