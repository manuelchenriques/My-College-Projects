package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.ICrachaRepository;
import si.isel.t43dg01.orm.Cracha;
import si.isel.t43dg01.orm.CrachaPK;

import java.util.Collection;
import java.util.List;

public class CrachaRepository implements ICrachaRepository {

    @Override
    public Cracha findByKey(CrachaPK key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Cracha cracha = em.createNamedQuery("Cracha.findByKey", Cracha.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return cracha;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Cracha> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Cracha> crachas = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return crachas;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Cracha> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Cracha> crachas = em.createNamedQuery("Cracha.findAll", Cracha.class)
                    .getResultList();
            ds.validateWork();
            return crachas;
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
