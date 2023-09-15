package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IConversaParticipantesRepository;
import si.isel.t43dg01.orm.ConversaParticipantes;
import si.isel.t43dg01.orm.ConversaParticipantesPK;

import java.util.Collection;
import java.util.List;

public class ConversaParticipantesRepository implements IConversaParticipantesRepository {

    @Override
    public ConversaParticipantes findByKey(ConversaParticipantesPK key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            ConversaParticipantes cParticipantes = em.createNamedQuery("ConversaParticipantes.findByKey", ConversaParticipantes.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return cParticipantes;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ConversaParticipantes> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<ConversaParticipantes> cParticipantes = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return cParticipantes;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<ConversaParticipantes> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<ConversaParticipantes> cParticipantes = em.createNamedQuery("ConversaParticipantes.findAll", ConversaParticipantes.class)
                    .getResultList();
            ds.validateWork();
            return cParticipantes;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    protected List helperQueryImpl(EntityManager em, String jpql, Object... params) {
        Query q = em.createQuery(jpql);

        for(int i = 0; i < params.length; ++i)
            q.setParameter(i+1, params[i]);

        return q.getResultList();
    }
}
