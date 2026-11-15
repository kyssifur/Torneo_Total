package model;

import java.util.ArrayList;

public class PartidoFutbol extends Partido {

    private int golesA;
    private int golesB;
    private int penalesA;
    private int penalesB;
    private boolean huboPenales;
    private final ArrayList<EventoPartido> eventos;

    public PartidoFutbol(Equipo equipoA, Equipo equipoB) {
        super(equipoA, equipoB);
        this.eventos = new ArrayList<>();
    }

    public void registrarMarcador(int golesA, int golesB) {
        if (golesA < 0 || golesB < 0) {
            throw new IllegalArgumentException("Los goles no pueden ser negativos");
        }
        this.golesA = golesA;
        this.golesB = golesB;
        this.huboPenales = false;
        this.penalesA = 0;
        this.penalesB = 0;
        marcarComoJugado();
    }

    //Solo se permite tras registrar un empate (para eliminacion directa).
    public void registrarPenales(int penalesA, int penalesB) {
        if (!isJugado() || golesA != golesB) {
            throw new IllegalStateException("Los penales solo se registran luego de un empate");
        }
        if (penalesA < 0 || penalesB < 0) {
            throw new IllegalArgumentException("Los penales no pueden ser negativos");
        }
        if (penalesA == penalesB) {
            throw new IllegalArgumentException("La tanda de penales no puede terminar empatada");
        }
        this.penalesA = penalesA;
        this.penalesB = penalesB;
        this.huboPenales = true;
    }

    //Registra un evento y actualiza las estadisticas del jugador. 
    public void agregarEvento(EventoPartido evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo");
        }
        switch (evento.getTipo()) {
            case GOL:
                evento.getJugador().registrarGol();
                break;
            case TARJETA_AMARILLA:
                evento.getJugador().registrarTarjetaAmarilla();
                break;
            case TARJETA_ROJA:
                evento.getJugador().registrarTarjetaRoja();
                break;
        }
        eventos.add(evento);
    }

    public Equipo getGanador() {
        if (!isJugado()) {
            return null;
        }
        if (golesA > golesB) return getEquipoA();
        if (golesB > golesA) return getEquipoB();
        if (huboPenales) {
            return penalesA > penalesB ? getEquipoA() : getEquipoB();
        }
        return null; // empate
    }

    public int getGolesA() {
        return golesA;
    }

    public int getGolesB() {
        return golesB;
    }

    public boolean isHuboPenales() {
        return huboPenales;
    }

    public ArrayList<EventoPartido> getEventos() {
        return new ArrayList<>(eventos);
    }

    public String toString() {
        String texto = getEquipoA() + " " + (isJugado() ? golesA : "-") + " vs "
                + (isJugado() ? golesB : "-") + " " + getEquipoB();
        if (huboPenales) {
            texto += " (pen. " + penalesA + "-" + penalesB + ")";
        }
        return texto;
    }
}

