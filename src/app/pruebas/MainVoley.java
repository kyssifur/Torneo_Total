package app.pruebas;
import model.*;
import model.voley.*;

public class MainVoley { 
    public static void main(String[] args) {
        TorneoVoley miTorneo = new TorneoVoley("Copa Vóley UNSA 2026");
        Voley reglasVoley = new Voley();
        
        Jugador j1 = new Jugador("Kageyama", "V001", "Armador", 9);
        Jugador j2 = new Jugador("Hinata", "V002", "Opuesto", 10);
        Jugador j3 = new Jugador("Ushijima", "V003", "Opuesto", 1);
        Jugador j4 = new Jugador("Oikawa", "V004", "Armador", 1);

        Equipo karasuno = new Equipo("Karasuno", 2, 85);
        karasuno.addJugador(j1);
        karasuno.addJugador(j2);

        Equipo shiratorizawa = new Equipo("Shiratorizawa", 1, 95);
        shiratorizawa.addJugador(j3);

        Equipo aoba = new Equipo("Aoba Johsai", 3, 80);
        aoba.addJugador(j4);

        miTorneo.registrarEquipoExistente(karasuno);
        miTorneo.registrarEquipoExistente(shiratorizawa);
        miTorneo.registrarEquipoExistente(aoba);

        Grupo grupoA = new Grupo("Grupo A");
        miTorneo.crearGrupo(grupoA);

        PartidoVoley partido1 = new PartidoVoley(karasuno, shiratorizawa);
        
        boolean fueDecisivo = reglasVoley.esSetDecisivo(4); 
        
        partido1.registrarMarcador(3, 2); 
        grupoA.generarRound(partido1);
    }
}
