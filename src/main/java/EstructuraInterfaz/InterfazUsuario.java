package EstructuraInterfaz;

import Bacterias.Bacteria;
import Experimentos.Experimento;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class InterfazUsuario extends JFrame {
    private List<Experimento> experimentos;
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private JList<String> listBacterias;
    private DefaultListModel<String> listModelBacterias;
    private JTree treeBacterias;
    private DefaultTreeModel treeModelBacterias;


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
                if (nombreExperimento != null && !nombreExperimento.trim().isEmpty()) {
                    Experimento experimento = new Experimento(nombreExperimento);
                    experimentos.add(experimento);
                    File dir = new File("src/main/java/ExperimentosGuardados/" + nombreExperimento);
                    dir.mkdirs();
                    listModel.addElement(nombreExperimento);
                }
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
                String startDateString = JOptionPane.showInputDialog("Introduce la fecha de inicio (formato d/MM/yyyy)");
                String endDateString = JOptionPane.showInputDialog("Introduce la fecha de fin (formato d/MM/yyyy)");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                LocalDate startDate = LocalDate.parse(startDateString, formatter);
                LocalDate endDate = LocalDate.parse(endDateString, formatter);

                if (endDate.isBefore(startDate)) {
                    JOptionPane.showMessageDialog(null, "La fecha de fin no puede ser anterior a la fecha de inicio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int duration = (int) ChronoUnit.DAYS.between(startDate, endDate);

                int numeroBacterias = Integer.parseInt(JOptionPane.showInputDialog("Introduce el número de bacterias"));
                double temperatura = Double.parseDouble(JOptionPane.showInputDialog("Introduce la temperatura"));
                String[] opcionesLuminosidad = {"Alta", "Media", "Baja"};
                String luminosidad = (String) JOptionPane.showInputDialog(null, "Selecciona la luminosidad", "Luminosidad", JOptionPane.QUESTION_MESSAGE, null, opcionesLuminosidad, opcionesLuminosidad[0]);

                int comidaInicial = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad inicial de comida"));
                int diaIncremento = Integer.parseInt(JOptionPane.showInputDialog("Introduce el día hasta el cual se debe incrementar la cantidad de comida"));
                int comidaDiaIncremento = Integer.parseInt(JOptionPane.showInputDialog("Introduce la comida del día de incremento"));
                int comidaFinal = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad final de comida en el día " + duration));

                int[] dosisComida = new int[duration];
                for (int i = 0; i < duration; i++) {
                    if (i < diaIncremento) {
                        dosisComida[i] = comidaInicial + i * (comidaDiaIncremento - comidaInicial) / diaIncremento;
                    } else {
                        dosisComida[i] = comidaDiaIncremento + (i - diaIncremento) * (comidaFinal - comidaDiaIncremento) / (duration - 1 - diaIncremento);
                    }
                }

                String[] experimentosArray = experimentos.stream().map(Experimento::getNombre).toArray(String[]::new);
                String nombreExperimento = (String) JOptionPane.showInputDialog(null, "Selecciona el experimento", "Experimento", JOptionPane.QUESTION_MESSAGE, null, experimentosArray, experimentosArray[0]);

                Bacteria bacteria = new Bacteria(nombreBacteria, startDate, endDate, numeroBacterias, temperatura, luminosidad, dosisComida);
                for (Experimento experimento : experimentos) {
                    if (experimento.getNombre().equals(nombreExperimento)) {
                        experimento.addBacteria(bacteria);
                        experimento.guardarBacteria(bacteria, nombreExperimento + "/" + nombreBacteria);
                        break;
                    }
                }
            }
        });

        panelBoton.add(botonGuardarNuevaPoblacion);

        panelSuperior.add(panelBoton, BorderLayout.NORTH);

        add(panelSuperior, BorderLayout.NORTH);

        listBacterias = new JList<>();
        listModelBacterias = new DefaultListModel<>();
        listBacterias.setModel(listModelBacterias);
        treeBacterias = new JTree();
        treeModelBacterias = new DefaultTreeModel(new DefaultMutableTreeNode());
        treeBacterias.setModel(treeModelBacterias);

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedExperiment = list.getSelectedValue();
                    for (Experimento experimento : experimentos) {
                        if (experimento.getNombre().equals(selectedExperiment)) {
                            DefaultMutableTreeNode experimentNode = new DefaultMutableTreeNode(selectedExperiment);
                            for (Bacteria bacteria : experimento.getBacterias()) {
                                DefaultMutableTreeNode bacteriaNode = new DefaultMutableTreeNode(bacteria.getNombre());
                                bacteriaNode.add(new DefaultMutableTreeNode("Fecha de inicio: " + bacteria.getFechaInicio()));
                                bacteriaNode.add(new DefaultMutableTreeNode("Fecha de fin: " + bacteria.getFechaFin()));
                                bacteriaNode.add(new DefaultMutableTreeNode("Número de bacterias: " + bacteria.getNumeroBacterias()));
                                bacteriaNode.add(new DefaultMutableTreeNode("Temperatura: " + bacteria.getTemperatura()));
                                bacteriaNode.add(new DefaultMutableTreeNode("Luminosidad: " + bacteria.getLuminosidad()));
                                int[] dosisComida = bacteria.getDosisComida();
                                for (int i = 0; i < dosisComida.length; i++) {
                                    bacteriaNode.add(new DefaultMutableTreeNode("Dosis de comida día " + (i + 1) + ": " + dosisComida[i]));
                                }
                                experimentNode.add(bacteriaNode);
                            }
                            treeModelBacterias.setRoot(experimentNode);
                            break;
                        }
                    }
                }
            }
        });

        add(new JScrollPane(treeBacterias), BorderLayout.CENTER);

        JButton botonEliminarPoblacion = new JButton("Eliminar Población");
        panelBoton.add(botonEliminarPoblacion);

        botonEliminarPoblacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] experimentosArray = experimentos.stream().map(Experimento::getNombre).toArray(String[]::new);
                String nombreExperimento = (String) JOptionPane.showInputDialog(null, "Selecciona el experimento del cual quieres eliminar una población", "Experimento", JOptionPane.QUESTION_MESSAGE, null, experimentosArray, experimentosArray[0]);

                Experimento experimentoSeleccionado = null;
                for (Experimento experimento : experimentos) {
                    if (experimento.getNombre().equals(nombreExperimento)) {
                        experimentoSeleccionado = experimento;
                        break;
                    }
                }

                if (experimentoSeleccionado != null) {
                    String[] bacteriasArray = experimentoSeleccionado.getBacterias().stream().map(Bacteria::getNombre).toArray(String[]::new);
                    String nombreBacteria = (String) JOptionPane.showInputDialog(null, "Selecciona la población de bacterias que quieres eliminar", "Bacteria", JOptionPane.QUESTION_MESSAGE, null, bacteriasArray, bacteriasArray[0]);

                    Bacteria bacteriaSeleccionada = null;
                    for (Bacteria bacteria : experimentoSeleccionado.getBacterias()) {
                        if (bacteria.getNombre().equals(nombreBacteria)) {
                            bacteriaSeleccionada = bacteria;
                            break;
                        }
                    }

                    if (bacteriaSeleccionada != null) {
                        experimentoSeleccionado.getBacterias().remove(bacteriaSeleccionada);
                        File dir = new File("src/main/java/ExperimentosGuardados/" + nombreExperimento + "/" + nombreBacteria + ".txt");
                        dir.delete();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró ninguna población de bacterias con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró ningún experimento con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        cargarExperimentosExistentes();
    }

    private void cargarExperimentosExistentes() {
        File dir = new File("src/main/java/ExperimentosGuardados/");
        File[] experimentDirectories = dir.listFiles();
        if (experimentDirectories != null) {
            for (File experimentDirectory : experimentDirectories) {
                if (experimentDirectory.isDirectory()) {
                    String experimentName = experimentDirectory.getName();
                    Experimento experimento = new Experimento(experimentName);
                    File[] bacteriaFiles = experimentDirectory.listFiles();
                    if (bacteriaFiles != null) {
                        for (File bacteriaFile : bacteriaFiles) {
                            if (bacteriaFile.isFile() && bacteriaFile.getName().endsWith(".dat")) {
                                Bacteria bacteria = loadBacteriaFromFile(experimentName + "/" + bacteriaFile.getName());
                                experimento.addBacteria(bacteria);
                            }
                        }
                    }
                    experimentos.add(experimento);
                    listModel.addElement(experimentName);
                }
            }
        }
    }

    private Bacteria loadBacteriaFromFile(String filePath) {
        Bacteria bacteria = null;
        try {
            FileInputStream fileIn = new FileInputStream("src/main/java/ExperimentosGuardados/" + filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            bacteria = (Bacteria) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("La clase Bacteria no se encontró");
            c.printStackTrace();
        }
        return bacteria;
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

//fin  :)