package app;
import model.basquet.*;
import model.*;


import java.util.*;
public class Main {
public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TorneoBasquet torneo = new TorneoBasquet("Torneo de Básquet Universitario");
        ArrayList<Jugador> bolsaJugadores = new ArrayList<>();
        int opcion = 0;

        while (opcion != 4) {
            System.out.println("\n--- TORNEO BASQUET ---");
            System.out.println("1. Registrar equipo existente");
            System.out.println("2. Ver equipos inscritos");
            System.out.println("3. Crear llaves (Pseudoaleatorio)");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            if (opcion == 1) {
                System.out.print("Nombre del equipo: ");
                String nom = sc.nextLine();
                System.out.print("Ranking: ");
                int rank = sc.nextInt();
                System.out.print("Nivel de fuerza: ");
                int fuerza = sc.nextInt();
                
                Equipo e = new Equipo(nom, rank, fuerza);
                torneo.registrarEquipoExistente(e);
                System.out.println("¡Equipo registrado con éxito!");
            } else if (opcion == 2) {
                System.out.println("\n--- EQUIPOS ---");
                for (Equipo e : torneo.getEquipos()) {
                    System.out.println("- " + e.getNombre() + " (Fuerza: " + e.getFuerza() + ")");
                }
            } else if (opcion == 3) {
                torneo.crearLlaves();
            }
        }
        sc.close();
    }
}
