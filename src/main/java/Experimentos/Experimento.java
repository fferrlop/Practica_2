package Experimentos;

import Bacterias.Bacteria;

import java.io.*;
import java.time.LocalDate;
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
            PrintWriter writer = new PrintWriter(file);
            writer.println(bacteria.getNombre());
            writer.println(bacteria.getFechaInicio());
            writer.println(bacteria.getFechaFin());
            writer.println(bacteria.getNumeroBacterias());
            writer.println(bacteria.getTemperatura());
            writer.println(bacteria.getLuminosidad());
            for (int i = 0; i < 30; i++) {
                writer.println(bacteria.getDosisComida()[i]);
            }
            writer.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public Bacteria loadBacteria(String filename) {
        Bacteria bacteria = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/java/ExperimentosGuardados/" + filename));
            String nombre = reader.readLine();
            LocalDate fechaInicio = LocalDate.parse(reader.readLine());
            LocalDate fechaFin = LocalDate.parse(reader.readLine());
            int numeroBacterias = Integer.parseInt(reader.readLine());
            double temperatura = Double.parseDouble(reader.readLine());
            String luminosidad = reader.readLine();
            int[] dosisComida = new int[30];
            for (int i = 0; i < 30; i++) {
                dosisComida[i] = Integer.parseInt(reader.readLine());
            }
            bacteria = new Bacteria(nombre, fechaInicio, fechaFin, numeroBacterias, temperatura, luminosidad, dosisComida);
            reader.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        return bacteria;
    }
}