package si.isel.t43dg01.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MensagemPK implements Serializable {
    @Column(name = "n_ordem", updatable = false)
    private Integer nOrdem;

    @Column(updatable = false)
    private Integer conversa;

    @Column(updatable = false)
    private Integer jogador;

    public MensagemPK() {}

    public void setNOrdem(Integer nOrdem) { this.nOrdem = nOrdem; }
    public Integer getNOrdem() { return this.nOrdem; }

    public void setConversa(Integer conversa) { this.conversa = conversa; }
    public Integer getConversa() { return this.conversa; }

    public void setJogador(Integer jogador) { this.jogador = jogador; }
    public Integer getJogador() { return this.jogador; }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof MensagemPK)) {
            return false;
        }

        MensagemPK otherPK = (MensagemPK) other;
        return this.nOrdem.equals(otherPK.nOrdem) && this.conversa.equals(otherPK.conversa) && this.jogador.equals(otherPK.jogador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nOrdem) + Objects.hash(conversa) + Objects.hash(jogador);
    }

    @Override
    public String toString() {
        return "MensagemPK[" +
                "n_ordem = " + this.nOrdem +
                ", conversa = " + this.conversa +
                ", jogador = " + this.jogador +
                ']';
    }
}
