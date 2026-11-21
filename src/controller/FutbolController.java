package controller;

import java.util.ArrayList;

import model.Equipo;
import model.futbol.EquipoFutbol;
import model.futbol.EventoPartido;
import model.futbol.FilaPosicionFutbol;
import model.futbol.JugadorFutbol;
import model.futbol.PartidoFutbol;
import model.futbol.TorneoFutbol;

/**
 * Controlador del modulo de futbol (MVC).
 * La vista NUNCA toca el modelo para escribir: todo cambio pasa por aqui.
 * Las excepciones del modelo (IllegalArgumentException / IllegalStateException)
 * se dejan subir para que la vista las muestre al usuario.
 * Por ahora los datos viven en memoria; cuando integren, aqui se conectan los DAO.
 */
public class FutbolController {

    private TorneoFutbol torneo;

    public FutbolController() {
        this.torneo = new TorneoFutbol("Torneo de Fútbol");
    }

    // ---------- Torneo ----------

    /** Reemplaza el torneo actual por uno nuevo y vacio. */
    public void nuevoTorneo(String nombre) {
        this.torneo = new TorneoFutbol(nombre.trim()); // si el nombre es invalido lanza antes de reemplazar
    }

    public String getNombreTorneo() {
        return torneo.getNombre();
    }

    // ---------- Equipos y jugadores ----------

    public void registrarEquipo(String nombre, int ranking, int nivelFuerza) {
        torneo.registrarEquipoExistente(new EquipoFutbol(nombre.trim(), ranking, nivelFuerza));
    }

    public ArrayList<EquipoFutbol> getEquipos() {
        ArrayList<EquipoFutbol> lista = new ArrayList<>();
        for (Equipo e : torneo.getEquipos()) {
            if (e instanceof EquipoFutbol) {
                lista.add((EquipoFutbol) e);
            }
        }
        return lista;
    }

    public void agregarJugador(EquipoFutbol equipo, String nombre, String id, String posicion, int camiseta, boolean titular) {
        if (equipo == null) {
            throw new IllegalArgumentException("Primero selecciona un equipo");
        }
        equipo.addJugador(new JugadorFutbol(nombre.trim(), id.trim(), posicion, camiseta, titular));
    }

    // ---------- Fixture, resultados y eventos ----------

    public boolean fixtureGenerado() {
        return !torneo.getPartidos().isEmpty();
    }

    public void generarFixture() {
        torneo.crearLlaves();
    }

    public ArrayList<PartidoFutbol> getPartidos() {
        return torneo.getPartidos();
    }

    public void registrarResultado(int indicePartido, int golesA, int golesB) {
        torneo.registrarResultado(indicePartido, golesA, golesB);
    }

    public void agregarEvento(int indicePartido, EventoPartido.Tipo tipo, JugadorFutbol jugador, int minuto) {
        if (indicePartido < 0 || indicePartido >= torneo.getPartidos().size()) {
            throw new IllegalArgumentException("Selecciona un partido");
        }
        torneo.getPartidos().get(indicePartido).agregarEvento(new EventoPartido(tipo, jugador, minuto));
    }

    // ---------- Tabla ----------

    public ArrayList<FilaPosicionFutbol> getTabla() {
        return torneo.calcularTabla();
    }

    // ---------- Utilidad para probar la GUI ----------

    /** Inscribe 4 equipos con 4 jugadores cada uno. Util para demos y capturas del informe. */
    public void cargarDatosEjemplo() {
        String[] nombres = {"Leones FC", "Halcones", "Tigres", "Águilas"};
        String[] posiciones = {"Portero", "Defensa", "Mediocampista", "Delantero"};
        int[] camisetas = {1, 4, 8, 9};
        for (int t = 0; t < nombres.length; t++) {
            EquipoFutbol equipo = new EquipoFutbol(nombres[t], t + 1, 80 - t * 5);
            for (int i = 0; i < posiciones.length; i++) {
                equipo.addJugador(new JugadorFutbol(
                        posiciones[i] + " " + nombres[t], "F" + (t * 10 + i + 1),
                        posiciones[i], camisetas[i], true));
            }
            torneo.registrarEquipoExistente(equipo);
        }
    }
}