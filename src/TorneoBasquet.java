package src;

import java.util.*;
public class TorneoBasquet extends TorneosEquipos {
    private String nombre;
    private ArrayList<equipo> equipos;
    private ArrayList<Object> grupos;
    private ArrayList<PartidoBasquet> llaves;

    public TorneoBasquet(String nombre) {
        super();
        this.nombre = nombre;
        this.equipos = new ArrayList<>();
        this.grupos = new ArrayList<>();
        this.llaves = new ArrayList<>();
    }

    public void registrarEquipoExistente(equipo equipo) {
        this.equipos.add(equipo);
    }

    public void formarEquipo(ArrayList<jugador> bolsaJugadores, String nombreEquipo) {
        equipo nuevo = new equipo(nombreEquipo, 1, 50);
        if (!bolsaJugadores.isEmpty()) {
            nuevo.addJugador(bolsaJugadores.remove(0));
        }
        this.equipos.add(nuevo);
    }

    public void crearGrupo() {
        System.out.println("Grupo creado para torneo de básquet.");
    }

    public void crearLlaves() {
        this.llaves.clear();
        if (equipos.size() < 2) {
            System.out.println("Se necesitan al menos 2 equipos.");
            return;
        }
        ArrayList<equipo> mezcla = new ArrayList<>(equipos);
        Collections.shuffle(mezcla);

        System.out.println("\n--- LLAVES DE BASQUET GENERADAS ---");
        for (int i = 0; i < mezcla.size() - 1; i += 2) {
            PartidoBasquet partido = new PartidoBasquet(mezcla.get(i), mezcla.get(i + 1));
            this.llaves.add(partido);
            System.out.println(mezcla.get(i).getNombre() + " vs " + mezcla.get(i + 1).getNombre());
        }
    }

    public ArrayList<equipo> getEquipos() {
        return this.equipos;
    }
}