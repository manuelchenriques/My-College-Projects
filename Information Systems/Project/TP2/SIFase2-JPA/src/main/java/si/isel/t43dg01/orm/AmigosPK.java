package si.isel.t43dg01.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AmigosPK implements Serializable {
    @Column(updatable = false)
    private Integer jogador1;

    @Column(updatable = false)
    private Integer jogador2;

    public AmigosPK() {}

    public void setJogador1(Integer jogador1) { this.jogador1 = jogador1; }
    public Integer getJogador1() { return this.jogador1; }

    public void setJogador2(Integer jogador2) { this.jogador2 = jogador2; }
    public Integer getJogador2() { return this.jogador2; }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof AmigosPK)) {
            return false;
        }

        AmigosPK otherPK = (AmigosPK) other;
        return this.jogador1.equals(otherPK.jogador1) && this.jogador2.equals(otherPK.jogador2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jogador1) + Objects.hash(jogador2);
    }

    @Override
    public String toString() {
        return "AmigosPK[" +
                "jogador1 = " + this.jogador1 +
                ", jogador2 = " + this.jogador2 +
                ']';
    }
}
