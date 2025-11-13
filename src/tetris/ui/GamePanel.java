package tetris.ui;

import tetris.domain.Partida;
import tetris.domain.Tabuleiro;
import tetris.domain.Tetromino;
import tetris.util.CorConfig;
import tetris.engine.GameEngine;

import javax.swing.*;
import java.awt.*;

/**
 * Painel responsável por desenhar o tabuleiro, a peça atual, ghost e peças fixas.
 * Este GamePanel espera receber o GameEngine (para delegar atualizações) e a CorConfig.
 */
public class GamePanel extends JPanel {

    private static final int TAM_CELULA = 30; // tamanho em pixels de cada célula

    private final GameEngine engine;
    private final CorConfig corConfig;

    public GamePanel(GameEngine engine, CorConfig corConfig) {
        this.engine = engine;
        this.corConfig = corConfig;
        setPreferredSize(new Dimension(Tabuleiro.LARGURA * TAM_CELULA, Tabuleiro.ALTURA * TAM_CELULA));
        setBackground(corConfig.isTemaEscuro() ? Color.BLACK : new Color(0xEFEFEF));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fundo conforme tema (reaplica a cada repaint)
        g.setColor(corConfig.isTemaEscuro() ? Color.BLACK : new Color(0xEFEFEF));
        g.fillRect(0, 0, getWidth(), getHeight());

        Partida partida = engine.getPartida();
        Tabuleiro tab = partida.getTabuleiro();

        // cor da grade/linhas dependendo do tema
        Color gridColor = corConfig.isTemaEscuro() ? new Color(0x333333) : new Color(0xCCCCCC);

        // 1) Desenha células fixas do tabuleiro
        for (int y = 0; y < Tabuleiro.ALTURA; y++) {
            for (int x = 0; x < Tabuleiro.LARGURA; x++) {
                Color cor = tab.getCor(x, y);
                int px = x * TAM_CELULA;
                int py = y * TAM_CELULA;
                if (cor != null) {
                    g.setColor(cor);
                    g.fillRect(px, py, TAM_CELULA, TAM_CELULA);
                    g.setColor(cor.darker());
                    g.drawRect(px, py, TAM_CELULA, TAM_CELULA);
                } else {
                    g.setColor(gridColor);
                    g.drawRect(px, py, TAM_CELULA, TAM_CELULA);
                }
            }
        }

        // 2) Desenha ghost piece (sombra) — se existir
        Tetromino ghost = partida.getGhostPiece();
        if (ghost != null) {
            Color ghostColor = new Color(200, 200, 200, 100); // cinza translúcido
            g.setColor(ghostColor);
            for (Point p : ghost.getBlocos()) {
                int gx = (ghost.getX() + p.x) * TAM_CELULA;
                int gy = (ghost.getY() + p.y) * TAM_CELULA;
                if (gy < 0) continue; // partes acima do topo não desenhar
                g.fillRect(gx, gy, TAM_CELULA, TAM_CELULA);
            }
        }

        // 3) Desenha peça atual
        Tetromino atual = partida.getPecaAtual();
        if (atual != null) {
            Color corAtual = corConfig.isModoMonocromatico() ? corConfig.getCorMonocromatica() : atual.getCor();
            g.setColor(corAtual);
            for (Point p : atual.getBlocos()) {
                int x = (atual.getX() + p.x) * TAM_CELULA;
                int y = (atual.getY() + p.y) * TAM_CELULA;
                if (y < 0) continue; // parte acima do topo
                g.fillRect(x, y, TAM_CELULA, TAM_CELULA);
                g.setColor(corAtual.darker());
                g.drawRect(x, y, TAM_CELULA, TAM_CELULA);
                g.setColor(corAtual);
            }
        }

        // 4) Mensagens de estado (pausa / game over)
        if (engine.isPausado()) {
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(20f));
            g.drawString("PAUSADO (P)", getWidth()/2 - 70, getHeight()/2);
        }
        if (engine.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(g.getFont().deriveFont(28f));
            g.drawString("GAME OVER", getWidth()/2 - 90, getHeight()/2 - 20);
        }
    }
}
