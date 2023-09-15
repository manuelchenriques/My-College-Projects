package si.isel.t43dg01.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class JogadorPartidaMultiJogadorPK implements Serializable {
    @Column(insertable = false, updatable = false)
    private Integer partida;

    @Column(insertable = false, updatable = false)
    private String jogo;

    @Column(insertable = false, updatable = false)
    private Integer jogador;

    public JogadorPartidaMultiJogadorPK() {}

    public void setPartida(Integer partida) { this.partida = partida; }
    public Integer getPartida() { return this.partida; }

    public void setJogo(String jogo) { this.jogo = jogo; }
    public String getJogo() { return this.jogo; }

    public void setJogador(Integer jogador) { this.jogador = jogador; }
    public Integer getJogador() { return this.jogador; }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof JogadorPartidaMultiJogadorPK)) {
            return false;
        }

        JogadorPartidaMultiJogadorPK otherPK = (JogadorPartidaMultiJogadorPK) other;
        return this.partida.equals(otherPK.partida) && this.jogo.equals(otherPK.jogo) && this.jogador.equals(otherPK.jogador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partida) + Objects.hash(jogo) + Objects.hash(jogador);
    }

    @Override
    public String toString() {
        return "JogadorPartidaMultiJogadorPK[" +
                "partida = " + this.partida +
                ", jogo = '" + this.jogo + '\'' +
                ", jogador = " + this.jogador +
                ']';
    }
}
