package si.isel.t43dg01.data_repositories.interfaces;

import si.isel.t43dg01.orm.ConversaParticipantes;
import si.isel.t43dg01.orm.ConversaParticipantesPK;

import java.util.Collection;

public interface IConversaParticipantesRepository extends IRepository<ConversaParticipantes, Collection<ConversaParticipantes>, ConversaParticipantesPK>{
}