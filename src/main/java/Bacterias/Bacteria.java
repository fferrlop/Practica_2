package Bacterias;

import java.time.LocalDate;

public class Bacteria {
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int numeroBacterias;
    private double temperatura;
    private String luminosidad;
    private int[] dosisComida = new int[30];

    public Bacteria(String nombre, LocalDate fechaInicio, LocalDate fechaFin, int numeroBacterias, double temperatura, String luminosidad, int comidaInicial, int diaIncremento, int comidaDiaIncremento, int comidaFinal) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numeroBacterias = numeroBacterias;
        this.temperatura = temperatura;
        this.luminosidad = luminosidad;
        calcularDosisComida(comidaInicial, diaIncremento, comidaDiaIncremento, comidaFinal);
    }

    private void calcularDosisComida(int comidaInicial, int diaIncremento, int comidaDiaIncremento, int comidaFinal) {
        int incremento = (comidaDiaIncremento - comidaInicial) / (diaIncremento - 1);
        int decremento = (comidaDiaIncremento - comidaFinal) / (30 - diaIncremento);

        for (int i = 0; i < 30; i++) {
            if (i < diaIncremento) {
                dosisComida[i] = comidaInicial + incremento * i;
            } else {
                dosisComida[i] = comidaDiaIncremento - decremento * (i - diaIncremento);
            }
        }
    }
}