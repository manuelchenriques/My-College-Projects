package si.isel.t43dg01.data_mappers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_mappers.interfaces.IJogadorMapper;
import si.isel.t43dg01.orm.Jogador;

public class JogadorMapper implements IJogadorMapper {
    @Override
    public Integer create(Jogador entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            em.persist(entity);
            ds.validateWork();
            return entity.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Jogador read(Integer id) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Jogador jogador = em.find(Jogador.class, id);
            ds.validateWork();
            return jogador;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Integer update(Jogador entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Jogador jogador = em.find(Jogador.class, entity.getId(), LockModeType.PESSIMISTIC_WRITE);

            if(jogador == null) {
                System.out.println("Jogador not found.");
                return null;
            }

            jogador.setEmail(entity.getEmail());
            jogador.setUsername(entity.getUsername());
            jogador.setEstado(entity.getEstado());
            jogador.setRegiao(entity.getRegiao());
            ds.validateWork();
            return entity.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Integer delete(Jogador entity) {
        try(DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Jogador jogador = em.find(Jogador.class, entity.getId(), LockModeType.PESSIMISTIC_WRITE);

            if(jogador == null) {
                System.out.println("Jogador not found.");
                return null;
            }

            em.remove(jogador);
            ds.validateWork();
            return entity.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
