package EstructuraInterfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class InterfazUsuario extends JFrame {

    public InterfazUsuario() {
        setTitle("Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        ImageIcon imageIcon = new ImageIcon("src/main/resources/fondoUax.jpg");
        JLabel background = new JLabel(imageIcon);
        background.setSize(background.getPreferredSize());

        setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());

        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        background.setBounds(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);

        JButton botonDatosDinamicos = new JButton("Datos dinámicos");
        botonDatosDinamicos.setPreferredSize(new Dimension(443, 80));
        botonDatosDinamicos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton botonAnalisisOrganizacion = new JButton("Analisis y Organización de información");
        botonAnalisisOrganizacion.setPreferredSize(new Dimension(443, 80));
        botonAnalisisOrganizacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton botonMapasAsociacion = new JButton("Mapas y Asociación de Datos");
        botonMapasAsociacion.setPreferredSize(new Dimension(443, 80));
        botonMapasAsociacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton botonIndexacionVisualizacion = new JButton("Indexación y Visualización de archivos");
        botonIndexacionVisualizacion.setPreferredSize(new Dimension(443, 80));
        botonIndexacionVisualizacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0;
        buttonPanel.add(botonDatosDinamicos, gbc);

        gbc.gridy = 1;
        buttonPanel.add(botonAnalisisOrganizacion, gbc);

        gbc.gridy = 2;
        buttonPanel.add(botonMapasAsociacion, gbc);

        gbc.gridy = 3;
        buttonPanel.add(botonIndexacionVisualizacion, gbc);

        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Image originalImage = imageIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(layeredPane.getWidth(), layeredPane.getHeight(), Image.SCALE_SMOOTH);
                background.setIcon(new ImageIcon(scaledImage));
                background.setSize(layeredPane.getSize());
                buttonPanel.setBounds(layeredPane.getWidth() - 443 - 50, (layeredPane.getHeight() - buttonPanel.getPreferredSize().height) / 2, 443, buttonPanel.getPreferredSize().height);
            }
        });

        setLocationRelativeTo(null);
        add(layeredPane);

        setVisible(true);
    }
}