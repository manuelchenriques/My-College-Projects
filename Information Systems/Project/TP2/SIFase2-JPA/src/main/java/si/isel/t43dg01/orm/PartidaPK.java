package si.isel.t43dg01.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PartidaPK implements Serializable {
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false)
    private String jogo;

    public PartidaPK() {}

    public void setId(Integer id) { this.id = id; }
    public Integer getId() { return this.id; }

    public void setJogo(String jogo) { this.jogo = jogo; }
    public String getJogo() { return this.jogo; }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof PartidaPK)) {
            return false;
        }

        PartidaPK otherPK = (PartidaPK) other;
        return this.id.equals(otherPK.id) && this.jogo.equals(otherPK.jogo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id) + Objects.hash(jogo);
    }

    @Override
    public String toString() {
        return "PartidaPK[" +
                "id = " + this.id +
                ", jogo = '" + this.jogo + '\'' +
                ']';
    }
}
