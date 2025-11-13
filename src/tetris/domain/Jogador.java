package tetris.domain;

/**
 * Representa o jogador atual da partida.
 *
 * Pode ser expandido futuramente para incluir nome, ranking etc.
 */
public class Jogador {

    private String nome;

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
