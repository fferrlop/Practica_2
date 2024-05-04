package Experimentos;

import Bacterias.Bacteria;
import java.util.ArrayList;
import java.util.List;

public class Experimento {
    private String nombre;
    private List<Bacteria> bacterias;

    public Experimento(String nombre) {
        this.nombre = nombre;
        this.bacterias = new ArrayList<>();
    }

    public void addBacteria(Bacteria bacteria) {
        bacterias.add(bacteria);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Bacteria> getBacterias() {
        return bacterias;
    }
}