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
        panelSeparacion.setPreferredSize(new Dimension(150, 0));
        panelSeparacion.setBackground(Color.LIGHT_GRAY);
        add(panelSeparacion, BorderLayout.WEST);

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setBackground(Color.BLACK);
        add(separator, BorderLayout.CENTER);
    }

    public void mostrarVentana() {
        setVisible(true);
    }
}