package tetris.ui;

import tetris.domain.SistemaPontuacao;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de pontuação: mostra pontos, linhas e nível.
 */
public class ScorePanel extends JPanel {
    private final JLabel lblPontos = new JLabel("Pontuação: 0");
    private final JLabel lblLinhas = new JLabel("Linhas: 0");
    private final JLabel lblNivel  = new JLabel("Nível: 1");

    public ScorePanel() {
        setLayout(new GridLayout(3,1,4,4));
        setPreferredSize(new Dimension(160, 120));
        setBackground(Color.DARK_GRAY);
        lblPontos.setForeground(Color.WHITE);
        lblLinhas.setForeground(Color.WHITE);
        lblNivel.setForeground(Color.WHITE);
        lblPontos.setHorizontalAlignment(SwingConstants.CENTER);
        lblLinhas.setHorizontalAlignment(SwingConstants.CENTER);
        lblNivel.setHorizontalAlignment(SwingConstants.CENTER);
        lblPontos.setFont(new Font("Consolas", Font.BOLD, 14));
        lblLinhas.setFont(new Font("Consolas", Font.BOLD, 14));
        lblNivel.setFont(new Font("Consolas", Font.BOLD, 14));
        add(lblPontos); add(lblLinhas); add(lblNivel);
    }

    public void atualizar(SistemaPontuacao s) {
        if (s == null) return;
        lblPontos.setText("Pontuação: " + s.getPontos());
        lblLinhas.setText("Linhas: " + s.getLinhasTotais());
        lblNivel.setText("Nível: " + s.getNivel());
    }
}
