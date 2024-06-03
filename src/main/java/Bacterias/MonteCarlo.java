package Bacterias;

import java.util.Random;

public class MonteCarlo {
    private int[][] plato;
    private Random random;

    public MonteCarlo() {
        this.plato = new int[20][20];
        this.random = new Random();
    }

    public int[][] getPlato() {
        return plato;
    }

    public void inicializarPlato(Bacteria bacteria) {

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                this.plato[i][j] = bacteria.getDosisComida()[0];
            }
        }


        this.plato[9][9] = bacteria.getNumeroBacterias();
        this.plato[9][10] = bacteria.getNumeroBacterias();
        this.plato[10][9] = bacteria.getNumeroBacterias();
        this.plato[10][10] = bacteria.getNumeroBacterias();
    }

    public void simularDia(Bacteria bacteria) {

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (this.plato[i][j] > 0) {
                    // La bacteria come
                    this.plato[i][j]--;


                    int direccion = this.random.nextInt(4);
                    switch (direccion) {
                        case 0:
                            if (i > 0) {
                                this.plato[i - 1][j]++;
                                this.plato[i][j]--;
                            }
                            break;
                        case 1:
                            if (i < 19) {
                                this.plato[i + 1][j]++;
                                this.plato[i][j]--;
                            }
                            break;
                        case 2:
                            if (j > 0) {
                                this.plato[i][j - 1]++;
                                this.plato[i][j]--;
                            }
                            break;
                        case 3:
                            if (j < 19) {
                                this.plato[i][j + 1]++;
                                this.plato[i][j]--;
                            }
                            break;
                    }


                    if (this.random.nextInt(10) == 0) {
                        this.plato[i][j]++;
                    }
                }
            }
        }
    }

    public void simularExperimento(Bacteria bacteria) {

        this.inicializarPlato(bacteria);
        for (int i = 0; i < bacteria.getDosisComida().length; i++) {
            this.simularDia(bacteria);
        }
    }
}