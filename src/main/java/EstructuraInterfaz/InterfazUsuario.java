package EstructuraInterfaz;

import Bacterias.Bacteria;
import Experimentos.Experimento;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InterfazUsuario extends JFrame {
    private List<Experimento> experimentos;
    private DefaultListModel<String> listModel;
    private JList<String> list;

    public InterfazUsuario() {
        experimentos = new ArrayList<>();
        listModel = new DefaultListModel<>();
        setTitle("Inicio");
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelSeparacion = new JPanel();
        panelSeparacion.setPreferredSize(new Dimension(220, 0));
        panelSeparacion.setBackground(Color.LIGHT_GRAY);
        panelSeparacion.setLayout(new BorderLayout());

        JButton botonGuardar = new JButton("Guardar Experimento");
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreExperimento = JOptionPane.showInputDialog("Introduce el nombre del experimento");
                Experimento experimento = new Experimento(nombreExperimento);
                experimentos.add(experimento);
                File dir = new File("src/main/java/ExperimentosGuardados/" + nombreExperimento);
                dir.mkdirs();
                listModel.addElement(nombreExperimento);
            }
        });
        panelSeparacion.add(botonGuardar, BorderLayout.NORTH);

        list = new JList<>(listModel);
        panelSeparacion.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton botonEliminar = new JButton("Eliminar Experimento");
        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreExperimento = JOptionPane.showInputDialog("Introduce el nombre del experimento a eliminar");
                boolean encontrado = false;
                for (Experimento experimento : experimentos) {
                    if (experimento.getNombre().equals(nombreExperimento)) {
                        experimentos.remove(experimento);
                        listModel.removeElement(nombreExperimento);
                        File dir = new File("src/main/java/ExperimentosGuardados/" + nombreExperimento);
                        deleteDirectory(dir);
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    JOptionPane.showMessageDialog(null, "No se encontró ningún experimento con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelSeparacion.add(botonEliminar, BorderLayout.SOUTH);

        add(panelSeparacion, BorderLayout.WEST);

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setBackground(Color.BLACK);
        add(separator, BorderLayout.CENTER);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setPreferredSize(new Dimension(0, 50));
        panelSuperior.setBackground(Color.LIGHT_GRAY);
        panelSuperior.setLayout(new BorderLayout());

        JPanel panelBoton = new JPanel();
        panelBoton.setLayout(new FlowLayout());
        panelBoton.setBackground(Color.LIGHT_GRAY);

        JButton botonGuardarNuevaPoblacion = new JButton("Guardar nueva población");
        botonGuardarNuevaPoblacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreBacteria = JOptionPane.showInputDialog("Introduce el nombre de la bacteria");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                LocalDate fechaInicio = LocalDate.parse(JOptionPane.showInputDialog("Introduce la fecha de inicio (formato: DD/MM/YYYY)"), formatter);
                LocalDate fechaFin = LocalDate.parse(JOptionPane.showInputDialog("Introduce la fecha de fin (formato: DD/MM/YYYY)"), formatter);

                int numeroBacterias = Integer.parseInt(JOptionPane.showInputDialog("Introduce el número de bacterias"));
                double temperatura = Double.parseDouble(JOptionPane.showInputDialog("Introduce la temperatura"));
                String luminosidad = JOptionPane.showInputDialog("Introduce la luminosidad");

                int comidaInicial = Integer.parseInt(JOptionPane.showInputDialog("Introduce la comida inicial"));
                int diaIncremento = Integer.parseInt(JOptionPane.showInputDialog("Introduce el día de incremento"));
                int comidaDiaIncremento = Integer.parseInt(JOptionPane.showInputDialog("Introduce la comida del día de incremento"));
                int comidaFinal = Integer.parseInt(JOptionPane.showInputDialog("Introduce la comida final"));

                String[] experimentosArray = experimentos.stream().map(Experimento::getNombre).toArray(String[]::new);
                String nombreExperimento = (String) JOptionPane.showInputDialog(null, "Selecciona el experimento", "Experimento", JOptionPane.QUESTION_MESSAGE, null, experimentosArray, experimentosArray[0]);

                Bacteria bacteria = new Bacteria(nombreBacteria, fechaInicio, fechaFin, numeroBacterias, temperatura, luminosidad, comidaInicial, diaIncremento, comidaDiaIncremento, comidaFinal);
                for (Experimento experimento : experimentos) {
                    if (experimento.getNombre().equals(nombreExperimento)) {
                        experimento.addBacteria(bacteria);
                        experimento.saveBacteria(bacteria, nombreExperimento + "/" + nombreBacteria + ".ser");
                        break;
                    }
                }
            }
        });



        Dimension botonSize = new Dimension(200, 30);
        botonGuardarNuevaPoblacion.setPreferredSize(botonSize);
        botonGuardarNuevaPoblacion.setMaximumSize(botonSize);
        panelBoton.add(botonGuardarNuevaPoblacion);

        JButton botonEliminarPoblacion = new JButton("Eliminar población");
        botonEliminarPoblacion.setPreferredSize(botonSize);
        botonEliminarPoblacion.setMaximumSize(botonSize);
        panelBoton.add(botonEliminarPoblacion);

        panelSuperior.add(panelBoton, BorderLayout.NORTH);

        JPanel separatorSuperior = new JPanel();
        separatorSuperior.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        separatorSuperior.setBackground(Color.BLACK);
        panelSuperior.add(separatorSuperior, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        cargarExperimentosExistentes();
    }

    private void cargarExperimentosExistentes() {
        File dir = new File("src/main/java/ExperimentosGuardados/");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String nombreExperimento = file.getName();
                    Experimento experimento = new Experimento(nombreExperimento);
                    experimentos.add(experimento);
                    listModel.addElement(nombreExperimento);
                }
            }
        }
    }

    private boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File child : children) {
                    boolean success = deleteDirectory(child);
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    public void mostrarVentana() {
        setVisible(true);
    }
}