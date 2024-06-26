package EstructuraInterfaz;

import Bacterias.Bacteria;
import Bacterias.MonteCarlo;
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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

                String[] opcionesComida = {"Constante", "Linealmente", "Alterna", "Incremento/Decremento"};
                String suministroComida = (String) JOptionPane.showInputDialog(null, "Selecciona cómo quieres suministrar la comida", "Suministro de comida", JOptionPane.QUESTION_MESSAGE, null, opcionesComida, opcionesComida[0]);

                int[] dosisComida = new int[duration];
                switch (suministroComida) {
                    case "Constante":
                        int comidaInicial = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad inicial de comida"));
                        int incrementoComida = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad de comida a sumar cada día"));
                        for (int i = 0; i < duration; i++) {
                            dosisComida[i] = comidaInicial + i * incrementoComida;
                        }
                        break;

                    case "Linealmente":
                        int comidaInicialLin = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad inicial de comida"));
                        int comidaFinal = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad final de comida en el día " + duration));
                        for (int i = 0; i < duration; i++) {
                            dosisComida[i] = comidaInicialLin + i * (comidaFinal - comidaInicialLin) / (duration - 1);
                        }
                        break;

                    case "Alterna":
                        int comidaInicialAlterna = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad inicial de comida"));
                        int incrementoComidaAlterna = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad de comida a sumar en días alternos"));
                        for (int i = 0; i < duration; i++) {
                            if (i % 2 == 0) {
                                dosisComida[i] = comidaInicialAlterna + (i / 2) * incrementoComidaAlterna;
                            } else {
                                dosisComida[i] = dosisComida[i - 1];
                            }
                        }
                        break;

                    case "Incremento/Decremento":
                        int comidaInicialIncDec = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad inicial de comida"));
                        int diaLimite = Integer.parseInt(JOptionPane.showInputDialog("Introduce el día límite de incremento"));
                        int comidaDiaLimite = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad de comida en el día límite"));
                        int comidaFinalIncDec = Integer.parseInt(JOptionPane.showInputDialog("Introduce la cantidad final de comida en el día " + duration));
                        for (int i = 0; i < diaLimite; i++) {
                            dosisComida[i] = comidaInicialIncDec + i * (comidaDiaLimite - comidaInicialIncDec) / (diaLimite - 1);
                        }
                        for (int i = diaLimite; i < duration; i++) {
                            dosisComida[i] = comidaDiaLimite + (i - diaLimite) * (comidaFinalIncDec - comidaDiaLimite) / (duration - diaLimite);
                        }
                        break;
                }

                int numeroBacterias = Integer.parseInt(JOptionPane.showInputDialog("Introduce el número de bacterias"));
                double temperatura = Double.parseDouble(JOptionPane.showInputDialog("Introduce la temperatura"));
                String[] opcionesLuminosidad = {"Alta", "Media", "Baja"};
                String luminosidad = (String) JOptionPane.showInputDialog(null, "Selecciona la luminosidad", "Luminosidad", JOptionPane.QUESTION_MESSAGE, null, opcionesLuminosidad, opcionesLuminosidad[0]);


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
                        File dir = new File("src/main/java/ExperimentosGuardados/" + nombreExperimento + "/" + nombreBacteria + ".dat");
                        if (dir.delete()) {
                            System.out.println("El archivo fue eliminado exitosamente");
                        } else {
                            System.out.println("No se pudo eliminar el archivo");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró ninguna población de bacterias con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cargarExperimentosExistentes();

        JButton botonOrdenarExperimento = new JButton("Ordenar experimento");
        panelBoton.add(botonOrdenarExperimento);

        botonOrdenarExperimento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] opcionesOrdenamiento = {"Fecha", "Orden alfabético", "Número de bacterias"};
                String opcionSeleccionada = (String) JOptionPane.showInputDialog(null, "Selecciona el criterio de ordenamiento", "Ordenar experimento", JOptionPane.QUESTION_MESSAGE, null, opcionesOrdenamiento, opcionesOrdenamiento[0]);

                String[] experimentosArray = experimentos.stream().map(Experimento::getNombre).toArray(String[]::new);
                String nombreExperimento = (String) JOptionPane.showInputDialog(null, "Selecciona el experimento a ordenar", "Experimento", JOptionPane.QUESTION_MESSAGE, null, experimentosArray, experimentosArray[0]);

                Experimento experimentoSeleccionado = null;
                for (Experimento experimento : experimentos) {
                    if (experimento.getNombre().equals(nombreExperimento)) {
                        experimentoSeleccionado = experimento;
                        break;
                    }
                }

                if (experimentoSeleccionado != null) {
                    switch (opcionSeleccionada) {
                        case "Fecha":
                            experimentoSeleccionado.getBacterias().sort(Comparator.comparing(Bacteria::getFechaInicio));
                            break;
                        case "Orden alfabético":
                            experimentoSeleccionado.getBacterias().sort(Comparator.comparing(Bacteria::getNombre));
                            break;
                        case "Número de bacterias":
                            experimentoSeleccionado.getBacterias().sort(Comparator.comparing(Bacteria::getNumeroBacterias));
                            break;
                    }
                }
            }
        });

        JButton botonSimulacionMonteCarlo = new JButton("Simulación Monte Carlo");
        panelBoton.add(botonSimulacionMonteCarlo);

        botonSimulacionMonteCarlo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] experimentosArray = experimentos.stream().map(Experimento::getNombre).toArray(String[]::new);
                String nombreExperimento = (String) JOptionPane.showInputDialog(null, "Selecciona el experimento para la simulación", "Simulación Monte Carlo", JOptionPane.QUESTION_MESSAGE, null, experimentosArray, experimentosArray[0]);

                Experimento experimentoSeleccionado = null;
                for (Experimento experimento : experimentos) {
                    if (experimento.getNombre().equals(nombreExperimento)) {
                        experimentoSeleccionado = experimento;
                        break;
                    }
                }

                if (experimentoSeleccionado != null) {
                    String[] bacteriasArray = experimentoSeleccionado.getBacterias().stream().map(Bacteria::getNombre).toArray(String[]::new);
                    String nombreBacteria = (String) JOptionPane.showInputDialog(null, "Selecciona la población de bacterias para la simulación", "Simulación Monte Carlo", JOptionPane.QUESTION_MESSAGE, null, bacteriasArray, bacteriasArray[0]);

                    Bacteria bacteriaSeleccionada = null;
                    for (Bacteria bacteria : experimentoSeleccionado.getBacterias()) {
                        if (bacteria.getNombre().equals(nombreBacteria)) {
                            bacteriaSeleccionada = bacteria;
                            break;
                        }
                    }
                    if (bacteriaSeleccionada != null) {
                        final Bacteria finalBacteriaSeleccionada = bacteriaSeleccionada;
                        JFrame simulacionFrame = new JFrame("Simulación Monte Carlo - " + nombreBacteria);
                        simulacionFrame.setSize(800, 800);
                        simulacionFrame.setLocationRelativeTo(null);

                        JPanel tablero = new JPanel(new GridLayout(20, 20));
                        for (int i = 0; i < 400; i++) {
                            JPanel celda = new JPanel();
                            celda.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                            tablero.add(celda);
                        }

                        simulacionFrame.add(tablero);
                        simulacionFrame.setVisible(true);

                        MonteCarlo monteCarlo = new MonteCarlo();
                        monteCarlo.inicializarPlato(bacteriaSeleccionada);

                        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                        executor.scheduleAtFixedRate(new Runnable() {
                            int dia = 0;

                            @Override
                            public void run() {
                                monteCarlo.simularDia(finalBacteriaSeleccionada);

                                int[][] resultados = monteCarlo.getPlato();

                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < 20; i++) {
                                            for (int j = 0; j < 20; j++) {
                                                JPanel celda = (JPanel) tablero.getComponent(i * 20 + j);
                                                int numeroBacterias = resultados[i][j];
                                                if (numeroBacterias == 0) {
                                                    celda.setBackground(Color.WHITE);
                                                } else if (numeroBacterias <= 10) {
                                                    celda.setBackground(Color.GREEN);
                                                } else if (numeroBacterias <= 20) {
                                                    celda.setBackground(Color.ORANGE);
                                                } else {
                                                    celda.setBackground(Color.RED);
                                                }
                                            }
                                        }
                                    }
                                });

                                dia++;

                                if (dia >= finalBacteriaSeleccionada.getDosisComida().length) {
                                    executor.shutdown();
                                }
                            }
                        }, 0, 1, TimeUnit.SECONDS);
                    }
                }
            }
        });
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