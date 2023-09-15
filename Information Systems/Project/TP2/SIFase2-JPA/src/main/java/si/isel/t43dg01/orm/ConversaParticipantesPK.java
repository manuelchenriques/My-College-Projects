package si.isel.t43dg01.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConversaParticipantesPK implements Serializable {
    @Column(updatable = false)
    private Integer jogador;

    @Column(updatable = false)
    private Integer conversa;

    public ConversaParticipantesPK() {}

    public void setJogador(Integer jogador) { this.jogador = jogador; }
    public Integer getJogador() { return this.jogador; }

    public void setConversa(Integer conversa) { this.conversa = conversa; }
    public Integer getConversa() { return this.conversa; }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof ConversaParticipantesPK)) {
            return false;
        }

        ConversaParticipantesPK otherPK = (ConversaParticipantesPK) other;
        return this.jogador.equals(otherPK.jogador) && this.conversa.equals(otherPK.conversa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jogador) + Objects.hash(conversa);
    }

    @Override
    public String toString() {
        return "ConversaParticipantesPK[" +
                "jogador = " + this.jogador +
                ", conversa = " + this.conversa +
                ']';
    }
}
