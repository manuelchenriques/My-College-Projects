package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IJogadorRepository;
import si.isel.t43dg01.orm.Jogador;

import java.util.Collection;
import java.util.List;

public class JogadorRepository implements IJogadorRepository {

    @Override
    public Jogador findByKey(Integer key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Jogador jogador = em.createNamedQuery("Jogador.findByKey", Jogador.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return jogador;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Jogador> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Jogador> jogadores = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return jogadores;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Jogador> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Jogador> jogadores = em.createNamedQuery("Jogador.findAll", Jogador.class)
                    .getResultList();
            ds.validateWork();
            return jogadores;
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