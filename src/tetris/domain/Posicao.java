package tetris.domain;

import java.io.Serializable;

/**
 * Representa uma posição (x,y) no tabuleiro.
 * Imutável — deslocamentos retornam nova instância.
 */
public final class Posicao implements Serializable {
    private final int x;
    private final int y;

    public Posicao(int x, int y) { this.x = x; this.y = y; }

    public Posicao deslocar(int dx, int dy) { return new Posicao(x + dx, y + dy); }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public String toString() { return "(" + x + "," + y + ")"; }
}
