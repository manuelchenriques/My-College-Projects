package si.isel.t43dg01.data_mappers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_mappers.interfaces.ICrachaMapper;
import si.isel.t43dg01.orm.Cracha;
import si.isel.t43dg01.orm.CrachaPK;

public class CrachaMapper implements ICrachaMapper {
    @Override
    public CrachaPK create(Cracha entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            em.persist(entity);
            ds.validateWork();
            return entity.getId();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Cracha read(CrachaPK crachaPK) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            em.flush();

            Cracha cracha = em.find(Cracha.class, crachaPK);
            ds.validateWork();
            return cracha;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public CrachaPK update(Cracha entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Cracha cracha = em.find(Cracha.class, entity.getId(), LockModeType.PESSIMISTIC_WRITE);

            if (cracha == null) {
                System.out.println("Cracha not found.");
                return null;
            }
            cracha.setURL(entity.getURL());
            cracha.setLimPontos(entity.getLimPontos());
            ds.validateWork();
            return entity.getId();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public CrachaPK delete(Cracha entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Cracha cracha = em.find(Cracha.class, entity.getId(), LockModeType.PESSIMISTIC_WRITE);

            if(cracha == null) {
                System.out.println("Cracha not found.");
                return null;
            }

            em.remove(cracha);
            ds.validateWork();
            return entity.getId();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
