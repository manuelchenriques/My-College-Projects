package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IConversaParticipantes;

import java.util.Objects;
import java.util.Set;

@Entity
@NamedQuery(name="ConversaParticipantes.findByKey",
        query="SELECT p FROM ConversaParticipantes p WHERE p.id =:key")
@NamedQuery(name="ConversaParticipantes.findAll",
        query="SELECT c FROM ConversaParticipantes c")
@Table(name = "conversa_participantes")

public class ConversaParticipantes implements IConversaParticipantes {

    @EmbeddedId
    private ConversaParticipantesPK id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("jogador")
    @JoinColumn(name = "jogador")
    private Set<Jogador> jogadores;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("conversa")
    @JoinColumn(name = "conversa")
    private Set<Conversa> conversas;

    public ConversaParticipantes(){}
    public ConversaParticipantes(Set<Jogador> jogadores, Set<Conversa> conversas){
        this.jogadores = jogadores;
        this.conversas = conversas;
    }

    @Override
    public ConversaParticipantesPK getId() { return this.id; }
    @Override
    public Set<Conversa> getConversas() {return this.conversas;}
    @Override
    public Set<Jogador> getJogadores() {return this.jogadores;}

    @Override
    public void setId(ConversaParticipantesPK id) { this.id = id; }
    @Override
    public void setConversas(Set<Conversa> conversas) {this.conversas = conversas;}
    @Override
    public void setJogadores(Set<Jogador> jogadores) {this.jogadores = jogadores;}

    @Override
    public int hashCode() {
        return Objects.hash(this.jogadores, this.conversas);
    }

    @Override
    public String toString() {
        return "ConversaParticipantes[" +
                "jogador = " + this.id.getJogador() +
                ", conversa = " + this.id.getConversa() +
                ']';
    }
}
