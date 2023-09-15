package si.isel.t43dg01.data_mappers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_mappers.interfaces.IRegiaoMapper;
import si.isel.t43dg01.orm.Regiao;

public class RegiaoMapper implements IRegiaoMapper {
    @Override
    public String create(Regiao entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            em.persist(entity);
            ds.validateWork();
            return entity.getNome();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Regiao read(String nome) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Regiao regiao = em.find(Regiao.class, nome);
            ds.validateWork();
            return regiao;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String update(Regiao entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Regiao regiao = em.find(Regiao.class, entity.getNome(), LockModeType.PESSIMISTIC_WRITE);

            if(regiao == null) {
                System.out.println("Regiao not found.");
                return null;
            }

            ds.validateWork();
            return entity.getNome();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String delete(Regiao entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Regiao regiao = em.find(Regiao.class, entity.getNome(), LockModeType.PESSIMISTIC_WRITE);

            if(regiao == null) {
                System.out.println("Regiao not found.");
                return null;
            }

            em.remove(regiao);
            ds.validateWork();
            return entity.getNome();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
