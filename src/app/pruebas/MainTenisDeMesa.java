package app.pruebas;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import view.tenisdemesa.VentanaPrincipal;

public class MainTenisDeMesa {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignorada) {
            }
            new VentanaPrincipal().setVisible(true);
        });
    }
}
