package si.isel.t43dg01.data_repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_repositories.interfaces.IMensagemRepository;
import si.isel.t43dg01.orm.Mensagem;
import si.isel.t43dg01.orm.MensagemPK;

import java.util.Collection;
import java.util.List;

public class MensagemRepository implements IMensagemRepository {

    @Override
    public Mensagem findByKey(MensagemPK key) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Mensagem mensagem = em.createNamedQuery("Mensagem.findByKey", Mensagem.class)
                    .setParameter("key", key)
                    .getSingleResult();
            ds.validateWork();
            return mensagem;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Mensagem> find(String jpql, Object... params) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Mensagem> mensagens = helperQueryImpl(em, jpql, params);
            ds.validateWork();
            return mensagens;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Mensagem> findAll() {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            List<Mensagem> mensagens = em.createNamedQuery("Mensagem.findAll", Mensagem.class)
                    .getResultList();
            ds.validateWork();
            return mensagens;
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
