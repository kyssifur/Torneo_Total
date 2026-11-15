package src.TorneoTenisDeMesa;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contenedor de torneos individuales.
 * UML: idTorneo, fechaInicio, fechaFin; crearTorneo().
 * Composición con TorneoTenisDeMesa.
 */
public class TorneosIndividual {
    private int idTorneo;
    private Date fechaInicio;
    private Date fechaFin;
    private TorneoTenisDeMesa torneoTenisDeMesa;

    public TorneosIndividual(int idTorneo) {
        this.idTorneo = idTorneo;
    }

    /** Crea el torneo de tenis de mesa y fija la fecha de inicio. */
    public TorneoTenisDeMesa crearTorneo(String nombre) {
        this.fechaInicio = new Date();
        this.torneoTenisDeMesa = new TorneoTenisDeMesa(nombre);
        return torneoTenisDeMesa;
    }

    /** Cierra el torneo registrando la fecha de fin. */
    public void finalizarTorneo() {
        this.fechaFin = new Date();
    }

    public int getIdTorneo() { return idTorneo; }
    public void setIdTorneo(int idTorneo) { this.idTorneo = idTorneo; }
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    public TorneoTenisDeMesa getTorneoTenisDeMesa() { return torneoTenisDeMesa; }

    @Override
    public String toString() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "Torneo #" + idTorneo
            + " | inicio: " + (fechaInicio == null ? "-" : f.format(fechaInicio))
            + " | fin: " + (fechaFin == null ? "-" : f.format(fechaFin));
    }
}
