package model;
import model.voley.*;
import java.util.*;

public class Grupo {
    private String id;
    private List<Equipo> equipos;
    private List<PartidoVoley> partidos;
    private TablaPosiciones tabla;

    public Grupo(String id) {
        this.id = id;
        this.equipos = new ArrayList<>();
        this.partidos = new ArrayList<>();
        this.tabla = new TablaPosiciones();
    }

    public void generarRound(PartidoVoley partido) {
        this.partidos.add(partido);
    }

    public void calcularTabla() {
        this.tabla.calcular();
    }
}
