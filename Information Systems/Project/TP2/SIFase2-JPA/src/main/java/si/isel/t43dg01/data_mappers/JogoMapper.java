package si.isel.t43dg01.data_mappers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_mappers.interfaces.IJogoMapper;
import si.isel.t43dg01.orm.Jogo;


public class JogoMapper implements IJogoMapper {
    @Override
    public String create(Jogo entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            em.persist(entity);
            ds.validateWork();
            return entity.getReferencia();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Jogo read(String referencia) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Jogo jogo = em.find(Jogo.class, referencia);
            ds.validateWork();
            return jogo;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String update(Jogo entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Jogo jogo = em.find(Jogo.class, entity.getReferencia(), LockModeType.PESSIMISTIC_WRITE);

            if(jogo == null) {
                System.out.println("Jogo not found.");
                return null;
            }

            jogo.setNome(entity.getNome());
            jogo.setUrl(entity.getUrl());
            ds.validateWork();
            return entity.getReferencia();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String delete(Jogo entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Jogo jogo = em.find(Jogo.class, entity.getReferencia(), LockModeType.PESSIMISTIC_WRITE);

            if(jogo == null) {
                System.out.println("Jogo not found.");
                return null;
            }

            em.remove(jogo);
            ds.validateWork();
            return entity.getReferencia();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
