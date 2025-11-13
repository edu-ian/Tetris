package tetris.domain;

/**
 * Sistema simples de pontuação:
 * - 10 pontos por linha removida
 * - linhas totais contadas
 * - nível = 1 + (linhasTotais / 8)  (sobe a cada 8 linhas)
 */
public class SistemaPontuacao {

    private int pontos = 0;
    private int linhasTotais = 0;

    public void adicionarLinhas(int linhas) {
        if (linhas <= 0) return;
        linhasTotais += linhas;
        pontos += linhas * 10; // 10 pontos por linha
    }

    public int getPontos() { return pontos; }
    public int getLinhasTotais() { return linhasTotais; }
    public int getNivel() { return 1 + (linhasTotais / 8); }

    public void resetar() { pontos = 0; linhasTotais = 0; }
}
