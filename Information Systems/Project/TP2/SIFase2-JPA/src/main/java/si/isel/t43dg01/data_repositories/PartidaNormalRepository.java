package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IPartidaNormalRepository;
import si.isel.t43dg01.orm.PartidaNormal;
import si.isel.t43dg01.orm.PartidaNormalPK;

import java.util.Collection;
import java.util.List;

public class PartidaNormalRepository implements IPartidaNormalRepository {

    @Override
    public PartidaNormal findByKey(PartidaNormalPK key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            PartidaNormal pNormal = em.createNamedQuery("PartidaNormal.findByKey", PartidaNormal.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return pNormal;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<PartidaNormal> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<PartidaNormal> pNormais = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return pNormais;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<PartidaNormal> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<PartidaNormal> pNormais = em.createNamedQuery("PartidaNormal.findAll", PartidaNormal.class)
                    .getResultList();
            ds.validateWork();
            return pNormais;
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
