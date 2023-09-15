package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IRegiaoRepository;
import si.isel.t43dg01.orm.Regiao;

import java.util.Collection;
import java.util.List;

public class RegiaoRepository implements IRegiaoRepository {

    @Override
    public Regiao findByKey(String key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Regiao regiao = em.createNamedQuery("Regiao.findByKey", Regiao.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return regiao;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Regiao> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Regiao> regioes = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return regioes;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Regiao> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Regiao> regioes = em.createNamedQuery("Regiao.findAll", Regiao.class)
                    .getResultList();
            ds.validateWork();
            return regioes;
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
