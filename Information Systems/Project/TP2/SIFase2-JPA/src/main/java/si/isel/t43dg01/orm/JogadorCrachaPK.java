package si.isel.t43dg01.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class JogadorCrachaPK implements Serializable {
    @Column
    private Integer jogador;

    @Column
    private String jogo;

    @Column
    private String nome;

    public JogadorCrachaPK() {}

    public void setJogador(Integer jogador) { this.jogador = jogador; }
    public Integer getJogador() { return this.jogador; }

    public void setJogo(String jogo) { this.jogo = jogo; }
    public String getJogo() { return this.jogo; }

    public void setNome(String nome) { this.nome = nome; }
    public String getNome() { return this.nome; }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof JogadorCrachaPK)) {
            return false;
        }

        JogadorCrachaPK otherPK = (JogadorCrachaPK) other;
        return this.jogador.equals(otherPK.jogador) && this.jogo.equals(otherPK.jogo) && this.nome.equals(otherPK.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jogador) + Objects.hash(jogo) + Objects.hash(nome);
    }

    @Override
    public String toString() {
        return "JogadorCrachaPK[" +
                "jogador = " + this.jogador +
                ", jogo = '" + this.jogo + '\'' +
                ", nome = '" + this.nome + '\'' +
                ']';
    }
}
