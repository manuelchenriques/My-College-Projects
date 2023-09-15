package si.isel.t43dg01.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PartidaNormalPK implements Serializable {
    @Column(insertable = false, updatable = false)
    private Integer partida;

    @Column(insertable = false, updatable = false)
    private String jogo;

    public PartidaNormalPK() {}

    public void setPartida(Integer partida) { this.partida = partida; }
    public Integer getPartida() { return this.partida; }

    public void setJogo(String jogo) { this.jogo = jogo; }
    public String getJogo() { return this.jogo; }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof PartidaNormalPK)) {
            return false;
        }

        PartidaNormalPK otherPK = (PartidaNormalPK) other;
        return this.partida.equals(otherPK.partida) && this.jogo.equals(otherPK.jogo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partida) + Objects.hash(jogo);
    }

    @Override
    public String toString() {
        return "PartidaNormalPK[" +
                "partida = " + this.partida +
                ", jogo = '" + this.jogo + '\'' +
                ']';
    }
}
