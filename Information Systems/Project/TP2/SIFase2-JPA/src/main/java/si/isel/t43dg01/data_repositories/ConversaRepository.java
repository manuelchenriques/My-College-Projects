package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IConversaRepository;
import si.isel.t43dg01.orm.Conversa;

import java.util.Collection;
import java.util.List;

public class ConversaRepository implements IConversaRepository {

    @Override
    public Conversa findByKey(Integer key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Conversa conversa = em.createNamedQuery("Conversa.findByKey", Conversa.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return conversa;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Conversa> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Conversa> conversas = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return conversas;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Conversa> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Conversa> conversas = em.createNamedQuery("Conversa.findAll", Conversa.class)
                    .getResultList();
            ds.validateWork();
            return conversas;
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
