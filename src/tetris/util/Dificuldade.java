package tetris.util;

/**
 * Enum com 4 níveis de dificuldade. Cada nível tem delay base (ms) e aceleração por 8 linhas.
 */
public enum Dificuldade {
    FACIL(800, 20, 100),
    MEDIO(500, 25, 80),
    DIFICIL(320, 30, 60),
    INSANO(180, 35, 40);

    private final int delayBase;
    private final int aceleracaoPor8Linhas;
    private final int delayMinimo;

    Dificuldade(int delayBase, int aceleracaoPor8Linhas, int delayMinimo) {
        this.delayBase = delayBase;
        this.aceleracaoPor8Linhas = aceleracaoPor8Linhas;
        this.delayMinimo = delayMinimo;
    }

    // calcula delay atual com base no total de linhas
    public int calcularDelay(int linhasTotais) {
        int passos = linhasTotais / 8;
        int delta = passos * aceleracaoPor8Linhas;
        int delay = delayBase - delta;
        return Math.max(delayMinimo, delay);
    }
}
