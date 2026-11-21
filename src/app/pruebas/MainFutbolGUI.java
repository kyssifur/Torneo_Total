package app.pruebas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import view.futbol.PanelFutbol;

/**
 * Prueba de la GUI de futbol por separado (no depende de Main ni de los otros deportes).
 * Si FlatLaf esta en lib/ lo usa; si no, usa Nimbus sin dar error.
 */
public class MainFutbolGUI {

    public static void main(String[] args) {
        instalarLookAndFeel();
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("TorneoTotal - Fútbol");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setContentPane(new PanelFutbol());
            ventana.pack();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }

    private static void instalarLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            return;
        } catch (Exception ex) {
            // FlatLaf no esta disponible: se intenta Nimbus
        }
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (Exception ex) {
            // se queda con el aspecto por defecto
        }
    }
}