package tetris.domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma peça Tetromino:
 * - tipo (I,O,T,S,Z,J,L)
 * - matriz booleana forma (linhas x colunas)
 * - posição (top-left) no tabuleiro
 * - cor (definida automaticamente ou pelo modo monocromático)
 */
public class Tetromino {

    private final TipoTetromino tipo;
    private boolean[][] forma;   // forma[r][c]
    private Posicao posicao;     // canto superior esquerdo no tabuleiro
    private Color cor;

    /**
     * Construtor principal (usado pelo projeto atual).
     */
    public Tetromino(TipoTetromino tipo, boolean[][] forma, Posicao posInicial, Color cor) {
        this.tipo = tipo;
        this.forma = copiar(forma);
        this.posicao = posInicial;
        this.cor = (cor != null ? cor : Color.LIGHT_GRAY);
    }

    /**
     * Construtor secundário (compatível com o código antigo da sua professora).
     * Qualquer chamada com 3 parâmetros cairá aqui.
     */
    public Tetromino(TipoTetromino tipo, boolean[][] forma, Posicao posInicial) {
        this(tipo, forma, posInicial, Color.LIGHT_GRAY);
    }

    // ---------------------
    // GETTERS ESSENCIAIS
    // ---------------------
    public TipoTetromino getTipo() { return tipo; }
    public boolean[][] getForma() { return copiar(forma); }
    public Posicao getPosicao() { return posicao; }
    public int getX() { return posicao.getX(); }
    public int getY() { return posicao.getY(); }
    public Color getCor() { return cor; }
    public void setCor(Color c) { if (c != null) this.cor = c; }

    // ---------------------
    // CÓPIA DO TETROMINO
    // ---------------------
    public Tetromino copiar() {
        return new Tetromino(tipo, this.getForma(), this.getPosicao(), this.getCor());
    }

    // Lista todos os blocos (x,y) relativos
    public Point[] getBlocos() {
        List<Point> list = new ArrayList<>();
        for (int r = 0; r < forma.length; r++) {
            for (int c = 0; c < forma[0].length; c++) {
                if (forma[r][c]) list.add(new Point(c, r));
            }
        }
        return list.toArray(new Point[0]);
    }

    // ---------------------
    // MOVIMENTAÇÃO
    // ---------------------
    public void mover(int dx, int dy) {
        posicao = posicao.deslocar(dx, dy);
    }

    public void moverDireita() { mover(1, 0); }
    public void moverEsquerda() { mover(-1, 0); }
    public void moverBaixo() { mover(0, 1); }
    public void moverCima() { mover(0, -1); }
    public void setPosicao(Posicao p) { posicao = p; }

    // ---------------------
    // ROTAÇÃO 90° HORÁRIO
    // ---------------------
    public void rotacionar() {
        int h = forma.length;
        int w = forma[0].length;

        boolean[][] novo = new boolean[w][h];

        for (int r = 0; r < h; r++)
            for (int c = 0; c < w; c++)
                novo[c][h - 1 - r] = forma[r][c];

        forma = novo;
    }

    /**
     * Desfaz rotação (3x horário = 1x anti-horário)
     */
    public void desfazerRotacao() {
        for (int i = 0; i < 3; i++) {
            rotacionar();
        }
    }

    // ---------------------
    // UTILITÁRIO: COPIAR MATRIZ
    // ---------------------
    private static boolean[][] copiar(boolean[][] src) {
        boolean[][] out = new boolean[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, out[i], 0, src[i].length);
        }
        return out;
    }
}
