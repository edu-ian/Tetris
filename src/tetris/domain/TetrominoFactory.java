package tetris.domain;

import java.awt.Color;
import java.util.Random;

/**
 * Factory para criar Tetrominos com formas clássicas.
 * A cor é definida externamente (via CorConfig ou modo monocromático).
 */
public final class TetrominoFactory {
    private static final Random RNG = new Random();

    // Gera um tetromino aleatório em posição de spawn (x central, y negativa para spawn)
    public static Tetromino gerarAleatorio(boolean modoColorido, Color corMono) {
        TipoTetromino tipo = TipoTetromino.values()[RNG.nextInt(TipoTetromino.values().length)];
        boolean[][] forma;
        Color cor = corMono != null ? corMono : Color.LIGHT_GRAY;

        switch (tipo) {
            case I -> {
                forma = new boolean[][]{
                        {false,false,false,false},
                        {true, true, true, true},
                        {false,false,false,false},
                        {false,false,false,false}
                };
                if (modoColorido) cor = Color.CYAN;
            }
            case O -> {
                forma = new boolean[][]{
                        {true,true},
                        {true,true}
                };
                if (modoColorido) cor = Color.YELLOW;
            }
            case T -> {
                forma = new boolean[][]{
                        {false,true,false},
                        {true,true,true},
                        {false,false,false}
                };
                if (modoColorido) cor = new Color(128,0,128);
            }
            case S -> {
                forma = new boolean[][]{
                        {false,true,true},
                        {true, true,false},
                        {false,false,false}
                };
                if (modoColorido) cor = Color.GREEN;
            }
            case Z -> {
                forma = new boolean[][]{
                        {true,true,false},
                        {false,true,true},
                        {false,false,false}
                };
                if (modoColorido) cor = Color.RED;
            }
            case J -> {
                forma = new boolean[][]{
                        {true,false,false},
                        {true,true,true},
                        {false,false,false}
                };
                if (modoColorido) cor = Color.BLUE;
            }
            case L -> {
                forma = new boolean[][]{
                        {false,false,true},
                        {true, true, true},
                        {false,false,false}
                };
                if (modoColorido) cor = Color.ORANGE;
            }
            default -> {
                forma = new boolean[][]{{true}};
            }
        }

        // spawn X centralizado
        Posicao spawn = new Posicao(3, - (forma.length)); // começa um pouco acima do topo
        Tetromino t = new Tetromino(tipo, forma, spawn, cor);
        return t;
    }
}
