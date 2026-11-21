package app.pruebas;

import model.Equipo;
import model.basquet.TorneoBasquet;

public class MainBasquet {
    public static void main(String[] args) {
        System.out.println("--- TORNEO DE BÁSQUET ---");
        
        TorneoBasquet torneo = new TorneoBasquet("Torneo Universitario");

        torneo.registrarEquipoExistente(new Equipo("Ingeniería", 1, 80));
        torneo.registrarEquipoExistente(new Equipo("Derecho", 2, 75));
        torneo.registrarEquipoExistente(new Equipo("Medicina", 3, 85));
        torneo.registrarEquipoExistente(new Equipo("Administración", 4, 70));

        System.out.println("\nEquipos registrados:");
        for (Equipo e : torneo.getEquipos()) {
            System.out.println("- " + e.getNombre() + " (Fuerza: " + e.getFuerza() + ")");
        }

        System.out.println("\nGenerando enfrentamientos...");
        String resultado = torneo.crearLlaves();
        System.out.println(resultado);
    }
}