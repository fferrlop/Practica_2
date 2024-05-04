package EstructuraInterfaz;

import javax.swing.*;
import java.awt.*;

public class InterfazUsuario extends JFrame {
    public InterfazUsuario() {
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
        panelSeparacion.add(botonGuardar, BorderLayout.NORTH);

        add(panelSeparacion, BorderLayout.WEST);

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setBackground(Color.BLACK);
        add(separator, BorderLayout.CENTER);

        // Crea un nuevo panel para la parte superior de la ventana
        JPanel panelSuperior = new JPanel();
        panelSuperior.setPreferredSize(new Dimension(0, 50));
        panelSuperior.setBackground(Color.LIGHT_GRAY);
        panelSuperior.setLayout(new BorderLayout()); // Cambia a BorderLayout

        // Crea un nuevo panel para los botones y añade los botones a este panel
        JPanel panelBoton = new JPanel();
        panelBoton.setLayout(new FlowLayout()); // Usa FlowLayout
        panelBoton.setBackground(Color.LIGHT_GRAY); // Establece el color de fondo al mismo que el panelSuperior

        JButton botonGuardarNuevaPoblacion = new JButton("Guardar nueva población");
        Dimension botonSize = new Dimension(200, 30);
        botonGuardarNuevaPoblacion.setPreferredSize(botonSize); // Hace el botón más pequeño
        botonGuardarNuevaPoblacion.setMaximumSize(botonSize); // Establece el tamaño máximo del botón
        panelBoton.add(botonGuardarNuevaPoblacion);

        JButton botonEliminarPoblacion = new JButton("Eliminar población");
        botonEliminarPoblacion.setPreferredSize(botonSize); // Hace el botón más pequeño
        botonEliminarPoblacion.setMaximumSize(botonSize); // Establece el tamaño máximo del botón
        panelBoton.add(botonEliminarPoblacion);

        panelSuperior.add(panelBoton, BorderLayout.NORTH); // Añade al norte del panel

        // Añade una línea de separación al panel superior
        JPanel separatorSuperior = new JPanel();
        separatorSuperior.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1)); // Ajusta el tamaño de la línea de separación
        separatorSuperior.setBackground(Color.BLACK);
        panelSuperior.add(separatorSuperior, BorderLayout.SOUTH); // Añade al sur del panel

        // Añade el panel superior a la ventana
        add(panelSuperior, BorderLayout.NORTH);
    }

    public void mostrarVentana() {
        setVisible(true);
    }
}