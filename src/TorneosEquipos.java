package src;

public class TorneosEquipos {
    protected int idTorneo;
    protected String fechaInicio;
    protected String fechaFin;

    public TorneosEquipos() {
    }
    
    public TorneosEquipos(int idTorneo, String fechaInicio, String fechaFin) {
        this.idTorneo = idTorneo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
}