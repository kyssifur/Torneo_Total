package model.futbol;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import model.Equipo;

/**
 * Torneo de futbol en formato liga (todos contra todos, una vuelta).
 * Formato y reglas de puntos son SUPUESTOS iniciales a confirmar con el equipo.
 * No imprime nada por consola: devuelve datos para que la vista (GUI) los muestre.
 */
public class TorneoFutbol {

    private final String nombre;
    private final ArrayList<Equipo> equipos;
    private final ArrayList<PartidoFutbol> partidos;
    private boolean fixtureGenerado;

    public TorneoFutbol(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del torneo es obligatorio");
        }
        this.nombre = nombre;
        this.equipos = new ArrayList<>();
        this.partidos = new ArrayList<>();
        this.fixtureGenerado = false;
    }

    public void registrarEquipoExistente(Equipo nuevo) {
        if (fixtureGenerado) {
            throw new IllegalStateException("No se pueden agregar equipos despues de generar el fixture");
        }
        if (nuevo == null) {
            throw new IllegalArgumentException("El equipo no puede ser nulo");
        }
        for (Equipo inscrito : equipos) {
            if (inscrito.getNombre().equalsIgnoreCase(nuevo.getNombre())) {
                throw new IllegalArgumentException("El equipo " + nuevo.getNombre() + " ya esta inscrito");
            }
        }
        equipos.add(nuevo);
    }

    /** Genera todos los partidos: cada equipo juega una vez contra los demas. */
    public void crearLlaves() {
        if (equipos.size() < 2) {
            throw new IllegalStateException("Se necesitan al menos 2 equipos");
        }
        if (fixtureGenerado) {
            throw new IllegalStateException("El fixture ya fue generado");
        }
        for (int i = 0; i < equipos.size(); i++) {
            for (int j = i + 1; j < equipos.size(); j++) {
                partidos.add(new PartidoFutbol(equipos.get(i), equipos.get(j)));
            }
        }
        fixtureGenerado = true;
    }

    /** Registra el resultado del partido en la posicion indicada del fixture. */
    public void registrarResultado(int indicePartido, int golesA, int golesB) {
        if (indicePartido < 0 || indicePartido >= partidos.size()) {
            throw new IllegalArgumentException("No existe el partido " + indicePartido);
        }
        if (golesA < 0 || golesB < 0) {
            throw new IllegalArgumentException("Los goles no pueden ser negativos");
        }
        PartidoFutbol partido = partidos.get(indicePartido);
        if (partido.isJugado()) {
            throw new IllegalStateException("Ese partido ya tiene resultado");
        }
        partido.registrarMarcador(golesA, golesB);
    }

    /** Calcula la tabla ordenada: puntos, diferencia de goles, goles a favor. */
    public ArrayList<FilaPosicionFutbol> calcularTabla() {
        HashMap<Equipo, FilaPosicionFutbol> filas = new HashMap<>();
        for (Equipo e : equipos) {
            filas.put(e, new FilaPosicionFutbol(e));
        }
        for (PartidoFutbol p : partidos) {
            if (p.isJugado()) {
                filas.get(p.getEquipoA()).registrarPartido(p.getGolesA(), p.getGolesB());
                filas.get(p.getEquipoB()).registrarPartido(p.getGolesB(), p.getGolesA());
            }
        }
        ArrayList<FilaPosicionFutbol> tabla = new ArrayList<>(filas.values());
        tabla.sort(Comparator
                .comparingInt(FilaPosicionFutbol::getPuntos).reversed()
                .thenComparing(Comparator.comparingInt(FilaPosicionFutbol::getDiferenciaGoles).reversed())
                .thenComparing(Comparator.comparingInt(FilaPosicionFutbol::getGolesFavor).reversed())
                .thenComparing(f -> f.getEquipo().getNombre()));
        return tabla;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Equipo> getEquipos() {
        return new ArrayList<>(equipos);
    }

    public ArrayList<PartidoFutbol> getPartidos() {
        return new ArrayList<>(partidos);
    }
}
