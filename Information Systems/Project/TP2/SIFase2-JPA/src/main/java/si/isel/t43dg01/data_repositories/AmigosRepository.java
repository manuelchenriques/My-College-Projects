package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IAmigosRepository;
import si.isel.t43dg01.orm.Amigos;
import si.isel.t43dg01.orm.AmigosPK;

import java.util.Collection;
import java.util.List;

public class AmigosRepository implements IAmigosRepository {

    @Override
    public Amigos findByKey(AmigosPK key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Amigos amigo = em.createNamedQuery("Amigos.findByKey", Amigos.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return amigo;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Amigos> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Amigos> amigos = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return amigos;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Amigos> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Amigos> amigos = em.createNamedQuery("Amigos.findAll", Amigos.class)
                    .getResultList();
            ds.validateWork();
            return amigos;
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
