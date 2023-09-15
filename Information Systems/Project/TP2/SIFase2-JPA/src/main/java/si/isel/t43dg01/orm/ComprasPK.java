package si.isel.t43dg01.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ComprasPK implements Serializable {
    @Column(updatable = false)
    private Integer jogador;

    @Column(updatable = false)
    private String jogo;

    public ComprasPK() {}

    public void setJogador(Integer jogador) { this.jogador = jogador; }
    public Integer getJogador() { return this.jogador; }

    public void setJogo(String jogo) { this.jogo = jogo; }
    public String getJogo() { return this.jogo; }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof ComprasPK)) {
            return false;
        }

        ComprasPK otherPK = (ComprasPK) other;
        return this.jogador.equals(otherPK.jogador) && this.jogo.equals(otherPK.jogo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jogador) + Objects.hash(jogo);
    }

    @Override
    public String toString() {
        return "ComprasPK[" +
                "jogador = " + this.jogador +
                ", jogo = '" + this.jogo + '\'' +
                ']';
    }
}
