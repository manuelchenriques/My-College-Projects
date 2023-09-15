package si.isel.t43dg01.orm.interfaces;

import si.isel.t43dg01.orm.Conversa;
import si.isel.t43dg01.orm.ConversaParticipantesPK;
import si.isel.t43dg01.orm.Jogador;

import java.util.Set;

public interface IConversaParticipantes {

    ConversaParticipantesPK getId();
    Set<Conversa> getConversas();
    Set<Jogador> getJogadores();

    void setId(ConversaParticipantesPK id);
    void setConversas(Set<Conversa> conversas);
    void setJogadores(Set<Jogador> jogadores);
}