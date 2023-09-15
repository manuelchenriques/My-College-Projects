package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IJogadorPartidaMultiJogadorRepository;
import si.isel.t43dg01.orm.JogadorPartidaMultiJogador;
import si.isel.t43dg01.orm.JogadorPartidaMultiJogadorPK;

import java.util.Collection;
import java.util.List;

public class JogadorPartidaMultiJogadorRepository implements IJogadorPartidaMultiJogadorRepository {

    @Override
    public JogadorPartidaMultiJogador findByKey(JogadorPartidaMultiJogadorPK key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            JogadorPartidaMultiJogador jPMJogador = em.createNamedQuery("JogadorPartidaMultiJogador.findByKey", JogadorPartidaMultiJogador.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return jPMJogador;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<JogadorPartidaMultiJogador> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<JogadorPartidaMultiJogador> jPMJogadores = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return jPMJogadores;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<JogadorPartidaMultiJogador> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<JogadorPartidaMultiJogador> jPMJogadores = em.createNamedQuery("JogadorPartidaMultiJogador.findAll", JogadorPartidaMultiJogador.class)
                    .getResultList();
            ds.validateWork();
            return jPMJogadores;
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
