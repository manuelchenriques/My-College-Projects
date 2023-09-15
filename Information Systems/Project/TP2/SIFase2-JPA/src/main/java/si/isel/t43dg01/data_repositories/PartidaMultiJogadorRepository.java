package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IPartidaMultiJogadorRepository;
import si.isel.t43dg01.orm.PartidaMultiJogador;
import si.isel.t43dg01.orm.PartidaMultiJogadorPK;

import java.util.Collection;
import java.util.List;

public class PartidaMultiJogadorRepository implements IPartidaMultiJogadorRepository {

    @Override
    public PartidaMultiJogador findByKey(PartidaMultiJogadorPK key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            PartidaMultiJogador pMJogador = em.createNamedQuery("PartidaMultiJogador.findByKey", PartidaMultiJogador.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return pMJogador;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<PartidaMultiJogador> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<PartidaMultiJogador> pMJogadores = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return pMJogadores;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<PartidaMultiJogador> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<PartidaMultiJogador> pMJogadores = em.createNamedQuery("PartidaMultiJogador.findAll", PartidaMultiJogador.class)
                    .getResultList();
            ds.validateWork();
            return pMJogadores;
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
