package tetris;

import tetris.ui.TelaPrincipal;

import javax.swing.*;

/**
 * Ponto de entrada. Roda a tela principal no EDT (Event Dispatch Thread).
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tp = new TelaPrincipal();
            tp.iniciar();
        });
    }
}
