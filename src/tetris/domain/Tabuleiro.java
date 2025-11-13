package tetris.domain;

import java.awt.Color;
import java.awt.Point;

/**
 * Tabuleiro clássico 10x20 com armazenamento de cores das células fixadas.
 * Fornece colide(), fixar(), removerLinhasCompletas() e utilitários.
 */
public class Tabuleiro {

    public static final int LARGURA = 10;
    public static final int ALTURA = 20;

    // grid[row][col] -> row = y, col = x
    private final Color[][] grid;

    public Tabuleiro() {
        grid = new Color[ALTURA][LARGURA];
    }

    // retorna cor (null se vazio)
    public Color getCor(int x, int y) {
        if (!inBounds(x,y)) return null;
        return grid[y][x];
    }

    // define célula (usa null para limpar)
    public void definirCelula(int x, int y, Color cor) {
        if (!inBounds(x,y)) return;
        grid[y][x] = cor;
    }

    // verifica se coordenada está dentro
    public boolean inBounds(int x, int y) {
        return x >= 0 && x < LARGURA && y >= 0 && y < ALTURA;
    }

    // verifica se célula ocupada (fora do tabuleiro é considerado ocupado para colisão)
    public boolean estaOcupado(int x, int y) {
        if (!inBounds(x,y)) return true;
        return grid[y][x] != null;
    }

    // testa colisão de um tetromino com o tabuleiro/limites
    public boolean colide(Tetromino t) {
        for (Point p : t.getBlocos()) {
            int x = t.getX() + p.x;
            int y = t.getY() + p.y;
            if (y < 0) continue; // partes acima do topo não colidem com grid
            if (estaOcupado(x,y)) return true;
        }
        return false;
    }

    // fixa tetromino no grid (assume posição válida)
    public void fixar(Tetromino t) {
        for (Point p : t.getBlocos()) {
            int x = t.getX() + p.x;
            int y = t.getY() + p.y;
            if (inBounds(x,y)) grid[y][x] = t.getCor();
        }
    }

    // remove linhas completas e retorna quantas foram removidas
    public int removerLinhasCompletas() {
        int removidas = 0;
        for (int y = ALTURA - 1; y >= 0; y--) {
            boolean completa = true;
            for (int x = 0; x < LARGURA; x++) {
                if (grid[y][x] == null) { completa = false; break; }
            }
            if (completa) {
                // shift down
                for (int j = y; j > 0; j--) {
                    System.arraycopy(grid[j-1], 0, grid[j], 0, LARGURA);
                }
                // clear top
                for (int x = 0; x < LARGURA; x++) grid[0][x] = null;
                removidas++;
                y++; // recheck same row index after shift
            }
        }
        return removidas;
    }

    // limpa o tabuleiro
    public void limpar() {
        for (int y = 0; y < ALTURA; y++)
            for (int x = 0; x < LARGURA; x++)
                grid[y][x] = null;
    }

    // getters úteis
    public Color[][] getGrid() { return grid; }
    public int getLargura() { return LARGURA; }
    public int getAltura() { return ALTURA; }
}
