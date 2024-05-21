package Bacterias;
//
import java.time.LocalDate;

public class Bacteria {

    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int numeroBacterias;
    private double temperatura;
    private String luminosidad;
    private int[] dosisComida = new int[30];

    public Bacteria(String nombre, LocalDate fechaInicio, LocalDate fechaFin, int numeroBacterias, double temperatura, String luminosidad, int[] dosisComida) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numeroBacterias = numeroBacterias;
        this.temperatura = temperatura;
        this.luminosidad = luminosidad;
        this.dosisComida = dosisComida;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int getNumeroBacterias() {
        return numeroBacterias;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public String getLuminosidad() {
        return luminosidad;
    }

    public int[] getDosisComida() {
        return dosisComida;
    }
}