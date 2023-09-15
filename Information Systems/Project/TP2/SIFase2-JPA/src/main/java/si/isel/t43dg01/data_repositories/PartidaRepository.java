package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IPartidaRepository;
import si.isel.t43dg01.orm.Partida;
import si.isel.t43dg01.orm.PartidaPK;

import java.util.Collection;
import java.util.List;

public class PartidaRepository implements IPartidaRepository {

    @Override
    public Partida findByKey(PartidaPK key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Partida partida = em.createNamedQuery("Partida.findByKey", Partida.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return partida;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Partida> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Partida> partidas = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return partidas;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Partida> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Partida> partidas = em.createNamedQuery("Partida.findAll", Partida.class)
                    .getResultList();
            ds.validateWork();
            return partidas;
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
