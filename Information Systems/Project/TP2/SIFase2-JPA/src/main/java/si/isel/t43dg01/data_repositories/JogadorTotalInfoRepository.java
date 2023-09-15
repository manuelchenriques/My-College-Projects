package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IJogadorTotalInfoRepository;
import si.isel.t43dg01.orm.JogadorTotalInfo;

import java.util.Collection;
import java.util.List;

public class JogadorTotalInfoRepository implements IJogadorTotalInfoRepository {

    @Override
    public JogadorTotalInfo findByKey(Integer key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            JogadorTotalInfo jTInfo = em.createNamedQuery("JogadorTotalInfo.findByKey", JogadorTotalInfo.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return jTInfo;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<JogadorTotalInfo> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<JogadorTotalInfo> jTInfos = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return jTInfos;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<JogadorTotalInfo> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<JogadorTotalInfo> jTInfos = em.createNamedQuery("JogadorTotalInfo.findAll", JogadorTotalInfo.class)
                    .getResultList();
            ds.validateWork();
            return jTInfos;
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
