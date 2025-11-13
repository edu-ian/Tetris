package tetris.engine;

import tetris.domain.Partida;
import tetris.util.CorConfig;
import tetris.util.Dificuldade;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Motor do jogo: executa um Timer Swing que chama partida.tick() com intervalo
 * determinado pela dificuldade e pelo número de linhas completadas (aceleração gradual).
 *
 * Expõe métodos usados pela UI para controle (mover, rotacionar, hardDrop, softDrop, pause).
 */
public class GameEngine {

    private final Partida partida;
    private final CorConfig corConfig;
    private Dificuldade dificuldade;

    private Timer timer; // javax.swing.Timer para segurança com Swing
    private int delayAtualMs;

    public interface Listener {
        void onUpdate();
        void onGameOver();
    }
    private final Listener listener;

    public GameEngine(Partida partida, Dificuldade dificuldade, CorConfig corConfig, Listener listener) {
        this.partida = partida;
        this.dificuldade = dificuldade;
        this.corConfig = corConfig;
        this.listener = listener;
        this.delayAtualMs = dificuldade.calcularDelay(partida.getPontuacao().getLinhasTotais());
    }

    // inicia o loop do jogo
    public void iniciar() {
        if (timer != null && timer.isRunning()) return;
        delayAtualMs = dificuldade.calcularDelay(partida.getPontuacao().getLinhasTotais());
        timer = new Timer(delayAtualMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                partida.tick();
                // ajusta delay se necessário (aceleração gradual)
                int novoDelay = dificuldade.calcularDelay(partida.getPontuacao().getLinhasTotais());
                if (novoDelay != delayAtualMs) {
                    delayAtualMs = novoDelay;
                    timer.setDelay(delayAtualMs);
                }
                if (listener != null) listener.onUpdate();
                if (partida.isGameOver() && listener != null) listener.onGameOver();
            }
        });
        timer.start();
    }

    public void parar() {
        if (timer != null) timer.stop();
    }

    public void alternarPausa() {
        partida.alternarPausa();
    }

    public boolean isPausado() { return partida.isPausado(); }
    public boolean isGameOver() { return partida.isGameOver(); }

    // Delegações (UI chama esses métodos)
    public void moverEsquerda() { partida.moverEsquerda(); if (listener!=null) listener.onUpdate(); }
    public void moverDireita()  { partida.moverDireita(); if (listener!=null) listener.onUpdate(); }
    public void rotacionar()    { partida.rotacionar(); if (listener!=null) listener.onUpdate(); }
    public void softDrop()      { partida.softDrop(); if (listener!=null) listener.onUpdate(); }
    public void hardDrop()      { partida.hardDrop(); if (listener!=null) listener.onUpdate(); }

    public void reiniciar() {
        partida.reiniciar();
        delayAtualMs = dificuldade.calcularDelay(partida.getPontuacao().getLinhasTotais());
        if (timer != null) timer.setDelay(delayAtualMs);
        if (listener != null) listener.onUpdate();
    }

    public void setDificuldade(Dificuldade d) {
        this.dificuldade = d;
        delayAtualMs = d.calcularDelay(partida.getPontuacao().getLinhasTotais());
        if (timer != null) timer.setDelay(delayAtualMs);
    }

    public Partida getPartida() { return partida; }
}
