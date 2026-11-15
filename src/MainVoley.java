package src;

public class MainVoley { 
    public static void main(String[] args) {
        TorneoVoley miTorneo = new TorneoVoley("Copa Vóley UNSA 2026");
        Voley reglasVoley = new Voley();
        
        jugador j1 = new jugador("Kageyama", "V001", "Armador", 9);
        jugador j2 = new jugador("Hinata", "V002", "Opuesto", 10);
        jugador j3 = new jugador("Ushijima", "V003", "Opuesto", 1);
        jugador j4 = new jugador("Oikawa", "V004", "Armador", 1);

        equipo karasuno = new equipo("Karasuno", 2, 85);
        karasuno.addJugador(j1);
        karasuno.addJugador(j2);

        equipo shiratorizawa = new equipo("Shiratorizawa", 1, 95);
        shiratorizawa.addJugador(j3);

        equipo aoba = new equipo("Aoba Johsai", 3, 80);
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
