package src;

public class BasquetController {
    private TorneoBasquet torneo;

    public BasquetController(String nombreTorneo) {
        this.torneo = new TorneoBasquet(nombreTorneo);
    }

    public void handleAgregarEquipo(String nombre, int ranking, int fuerza) {
        equipo nuevoEquipo = new equipo(nombre, ranking, fuerza);
        torneo.registrarEquipoExistente(nuevoEquipo);
        System.out.println("¡Equipo '" + nombre + "' agregado al torneo de básquet con éxito!");
    }

    public void handleCrearLlaves() {
        torneo.crearLlaves();
    }

    public void mostrarEquipos() {
        System.out.println("\n--- EQUIPOS INSCRITOS EN " + torneo.getEquipos().size() + " ---");
        for (equipo e : torneo.getEquipos()) {
            System.out.println("- " + e.getNombre() + " (Fuerza: " + e.getFuerza() + ")");
        }
    }
}