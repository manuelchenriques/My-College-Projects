package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IComprasRepository;
import si.isel.t43dg01.orm.Compras;
import si.isel.t43dg01.orm.ComprasPK;

import java.util.Collection;
import java.util.List;

public class ComprasRepository implements IComprasRepository {

    @Override
    public Compras findByKey(ComprasPK key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Compras compra = em.createNamedQuery("Compras.findByKey", Compras.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return compra;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Compras> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Compras> compras = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return compras;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Compras> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Compras> compras = em.createNamedQuery("Compras.findAll", Compras.class)
                    .getResultList();
            ds.validateWork();
            return compras;
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
