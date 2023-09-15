package si.isel.t43dg01.data_mappers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_mappers.interfaces.IJogadorCrachaMapper;
import si.isel.t43dg01.orm.JogadorCracha;
import si.isel.t43dg01.orm.JogadorCrachaPK;

public class JogadorCrachaMapper implements IJogadorCrachaMapper {
    @Override
    public JogadorCrachaPK create(JogadorCracha entity) {
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
    public JogadorCracha read(JogadorCrachaPK jogadorCrachaPK) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            JogadorCracha jCracha = em.find(JogadorCracha.class, jogadorCrachaPK);
            ds.validateWork();
            return jCracha;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public JogadorCrachaPK update(JogadorCracha entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            JogadorCracha jCracha = em.find(JogadorCracha.class, entity.getId(), LockModeType.PESSIMISTIC_WRITE);

            if(jCracha == null) {
                System.out.println("JogadorCracha not found.");
                return null;
            }

            jCracha.setCrachas(entity.getCrachas());
            jCracha.setJogador(entity.getJogador());
            ds.validateWork();
            return entity.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public JogadorCrachaPK delete(JogadorCracha entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            JogadorCracha jCracha = em.find(JogadorCracha.class, entity.getId(), LockModeType.PESSIMISTIC_WRITE);

            if(jCracha == null) {
                System.out.println("JogadorCracha not found.");
                return null;
            }

            em.remove(jCracha);
            ds.validateWork();
            return entity.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
