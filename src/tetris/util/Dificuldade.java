package br.eduardo.tetris.util;

/**
 * Dificuldades com intervalo base do loop em ms.
 */
public enum Dificuldade {
    FACIL(800),
    MEDIO(600),
    DIFICIL(400),
    INSANO(200);

    private final int intervaloBase;

    Dificuldade(int intervaloBase) { this.intervaloBase = intervaloBase; }
    public int getIntervaloBase() { return intervaloBase; }
}
