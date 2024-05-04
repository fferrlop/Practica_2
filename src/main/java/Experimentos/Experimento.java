package Experimentos;

import Bacterias.Bacteria;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Experimento {
    private String nombre;
    private List<Bacteria> bacterias;

    public Experimento(String nombre) {
        this.nombre = nombre;
        this.bacterias = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void addBacteria(Bacteria bacteria) {
        bacterias.add(bacteria);
    }

    public List<Bacteria> getBacterias() {
        return bacterias;
    }

    public void saveBacteria(Bacteria bacteria, String filename) {
        try {
            File file = new File("src/main/java/ExperimentosGuardados/" + filename);
            file.getParentFile().mkdirs();
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(bacteria);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
