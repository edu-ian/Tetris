package tetris.domain;

import java.awt.Color;

/**
 * Partida representa o estado "alto nível" do jogo.
 * Mantém tabuleiro, pontuação e as peças (atual / próxima).
 * Fornece API usada pela UI/Engine: tick(), movimentações, reiniciar, pausa, hold, ghost.
 */
public class Partida {

    private final Tabuleiro tabuleiro;
    private final SistemaPontuacao pontuacao;

    private Tetromino atual;
    private Tetromino proximo;

    private Tetromino guardada = null;
    private boolean jaUsouHoldNoTurno = false;

    private boolean pausado = false;
    private boolean gameOver = false;

    public Partida(boolean modoColorido, Color corMono) {
        this.tabuleiro = new Tabuleiro();
        this.pontuacao = new SistemaPontuacao();
        this.atual = TetrominoFactory.gerarAleatorio(modoColorido, corMono);
        this.proximo = TetrominoFactory.gerarAleatorio(modoColorido, corMono);
    }

    // tick: avança uma unidade de tempo (gravity)
    public void tick() {
        if (pausado || gameOver) return;
        atual.moverBaixo();
        if (tabuleiro.colide(atual)) {
            atual.moverCima();
            tabuleiro.fixar(atual);
            int linhas = tabuleiro.removerLinhasCompletas();
            if (linhas > 0) pontuacao.adicionarLinhas(linhas);
            // spawn next
            atual = proximo;
            proximo = TetrominoFactory.gerarAleatorio(true, atual.getCor());
            // reset hold usage para o novo turno
            jaUsouHoldNoTurno = false;
            // if newly spawned collides => game over
            if (tabuleiro.colide(atual)) gameOver = true;
        }
    }

    // --------------------------
    // Controles usados pela UI
    // --------------------------
    public void moverEsquerda() { if (pausado || gameOver) return; atual.moverEsquerda(); if (tabuleiro.colide(atual)) atual.moverDireita(); }
    public void moverDireita()  { if (pausado || gameOver) return; atual.moverDireita(); if (tabuleiro.colide(atual)) atual.moverEsquerda(); }
    public void softDrop() { if (pausado || gameOver) return; tick(); } // softDrop usa tick() sem acelerar timer
    public void hardDrop() {
        if (pausado || gameOver) return;
        while (!tabuleiro.colide(atual)) atual.moverBaixo();
        atual.moverCima();
        tabuleiro.fixar(atual);
        int linhas = tabuleiro.removerLinhasCompletas();
        if (linhas > 0) pontuacao.adicionarLinhas(linhas);
        atual = proximo;
        proximo = TetrominoFactory.gerarAleatorio(true, atual.getCor());
        jaUsouHoldNoTurno = false;
        if (tabuleiro.colide(atual)) gameOver = true;
    }
    public void rotacionar() { if (pausado || gameOver) return; atual.rotacionar(); if (tabuleiro.colide(atual)) atual.desfazerRotacao(); }

    // --------------------------
    // HOLD (guardar peça)
    // --------------------------
    public void hold() {
        if (pausado || gameOver) return;
        if (jaUsouHoldNoTurno) return;

        if (guardada == null) {
            guardada = atual;
            atual = proximo;
            proximo = TetrominoFactory.gerarAleatorio(true, guardada.getCor());
        } else {
            Tetromino temp = atual;
            atual = guardada;
            guardada = temp;
        }
        // reposiciona a peça no topo e impede novo hold até fixar
        atual.setPosicao(new Posicao(3, - (atual.getForma().length)));
        jaUsouHoldNoTurno = true;
    }

    // retorna a ghost piece (cópia da atual que cai até a última posição válida)
    public Tetromino getGhostPiece() {
        if (atual == null) return null;
        Tetromino ghost = new Tetromino(atual.getTipo(), atual.getForma(), atual.getPosicao(), atual.getCor());
        while (!tabuleiro.colide(ghost)) ghost.moverBaixo();
        ghost.moverCima();
        return ghost;
    }

    // --------------------------
    // Reinício / limpeza / getters
    // --------------------------
    public void limpar() {
        tabuleiro.limpar();
        atual = TetrominoFactory.gerarAleatorio(true, null);
        proximo = TetrominoFactory.gerarAleatorio(true, atual.getCor());
        gameOver = false;
        pausado = false;
        jaUsouHoldNoTurno = false;
        guardada = null;
    }

    public void zerar() { pontuacao.resetar(); limpar(); }
    public void reiniciar() { zerar(); }

    public void alternarPausa() { if (!gameOver) pausado = !pausado; }

    // Getters
    public Tabuleiro getTabuleiro() { return tabuleiro; }
    public SistemaPontuacao getPontuacao() { return pontuacao; }
    public Tetromino getPecaAtual() { return atual; }
    public Tetromino getProximaPeca() { return proximo; }
    public Tetromino getPecaGuardada() { return guardada; }
    public boolean isPausado() { return pausado; }
    public boolean isGameOver() { return gameOver; }
}
