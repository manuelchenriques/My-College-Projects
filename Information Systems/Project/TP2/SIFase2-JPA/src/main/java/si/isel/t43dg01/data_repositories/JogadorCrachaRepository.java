package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IJogadorCrachaRepository;
import si.isel.t43dg01.orm.JogadorCracha;
import si.isel.t43dg01.orm.JogadorCrachaPK;

import java.util.Collection;
import java.util.List;

public class JogadorCrachaRepository implements IJogadorCrachaRepository {

    @Override
    public JogadorCracha findByKey(JogadorCrachaPK key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            JogadorCracha jCracha = em.createNamedQuery("JogadorCracha.findByKey", JogadorCracha.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return jCracha;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<JogadorCracha> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<JogadorCracha> jCrachas = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return jCrachas;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<JogadorCracha> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<JogadorCracha> jCrachas = em.createNamedQuery("JogadorCracha.findAll", JogadorCracha.class)
                    .getResultList();
            ds.validateWork();
            return jCrachas;
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
