package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IJogoRepository;
import si.isel.t43dg01.orm.Jogo;

import java.util.Collection;
import java.util.List;

public class JogoRepository implements IJogoRepository {

    @Override
    public Jogo findByKey(String key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Jogo jogo = em.createNamedQuery("Jogo.findByKey", Jogo.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return jogo;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Jogo> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Jogo> jogos = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return jogos;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Jogo> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Jogo> jogos = em.createNamedQuery("Jogo.findAll", Jogo.class)
                    .getResultList();
            ds.validateWork();
            return jogos;
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
