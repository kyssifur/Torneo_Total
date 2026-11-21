package controller;
import model.Equipo;
import model.voley.TorneoVoley;

public class VoleyController {
    private TorneoVoley torneo;

    public VoleyController() {
        this.torneo = new TorneoVoley("Copa Voley UNSA 2026");
    }

    public String registrarEquipo(String nombre, String rankingTxt, String fuerzaTxt) {
        int ranking = Integer.parseInt(rankingTxt);
        int fuerza = Integer.parseInt(fuerzaTxt);
        Equipo nuevo = new Equipo(nombre, ranking, fuerza);
        torneo.registrarEquipoExistente(nuevo);
        return "OK";
    }

    public String verEquipos() {
        return torneo.obtenerEquiposTexto();
    }

    public String crearLlaves() {
        return torneo.crearLlavesAleatoriasTexto();
    }
}
