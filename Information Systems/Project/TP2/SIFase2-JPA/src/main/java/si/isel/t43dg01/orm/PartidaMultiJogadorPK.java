package si.isel.t43dg01.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PartidaMultiJogadorPK implements Serializable {
    @Column(insertable = false, updatable = false)
    private Integer partida;

    @Column(insertable = false, updatable = false)
    private String jogo;

    public PartidaMultiJogadorPK() {}

    public void setPartida(Integer partida) { this.partida = partida; }
    public Integer getPartida() { return this.partida; }

    public void setJogo(String jogo) { this.jogo = jogo; }
    public String getJogo() { return this.jogo; }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof PartidaMultiJogadorPK)) {
            return false;
        }

        PartidaMultiJogadorPK otherPK = (PartidaMultiJogadorPK) other;
        return this.partida.equals(otherPK.partida) && this.jogo.equals(otherPK.jogo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partida) + Objects.hash(jogo);
    }

    @Override
    public String toString() {
        return "PartidaMultiJogadorPK[" +
                "partida = " + this.partida +
                ", jogo = '" + this.jogo + '\'' +
                ']';
    }
}
