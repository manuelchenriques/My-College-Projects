package si.isel.t43dg01.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CrachaPK implements Serializable {
    @Column(updatable = false)
    private String nome;

    @Column(updatable = false)
    private String jogo;

    public CrachaPK() {}

    public void setNome(String nome) { this.nome = nome; }
    public String getNome() { return this.nome; }

    public void setJogo(String jogo) { this.jogo = jogo; }
    public String getJogo() { return this.jogo; }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof CrachaPK)) {
            return false;
        }

        CrachaPK otherPK = (CrachaPK) other;
        return this.nome.equals(otherPK.nome) && this.jogo.equals(otherPK.jogo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome) + Objects.hash(jogo);
    }

    @Override
    public String toString() {
        return "CrachaPK[" +
                "nome = '" + this.nome + '\'' +
                ", jogo = '" + this.jogo + '\'' +
                ']';
    }
}
