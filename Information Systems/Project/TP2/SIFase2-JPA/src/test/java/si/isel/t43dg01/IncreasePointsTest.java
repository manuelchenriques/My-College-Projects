package si.isel.t43dg01;

import jakarta.persistence.EntityManager;
import si.isel.t43dg01.data_access.DataScopeImpl;
import si.isel.t43dg01.data_access.JPAContext;

import org.junit.Test;
import si.isel.t43dg01.orm.Cracha;
import si.isel.t43dg01.orm.CrachaPK;
import si.isel.t43dg01.orm.Jogo;

import static org.junit.Assert.assertNotNull;


public class IncreasePointsTest {

    @Test
    public void optimisticTest() throws Exception {
        JPAContext ctx = new JPAContext();

        Jogo testJogo = new Jogo("zzzzzxxxx1", "test jogo", "www.img_test");
        CrachaPK crachaPK = new CrachaPK();
        crachaPK.setNome("test cracha");
        crachaPK.setJogo("zzzzzxxxx1");
        Cracha testCracha = new Cracha(crachaPK, "www.imgs/test_cracha", 1000);
        testCracha.setJogo(testJogo);
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();

            em.persist(testJogo);
            em.persist(testCracha);

            ds.validateWork();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try (DataScopeImpl ds = new DataScopeImpl()) {
            assertNotNull(ctx);
            assertNotNull(ctx.getJogos().findByKey("zzzzzxxxx1"));
            assertNotNull(ctx.getCrachas().findByKey(crachaPK));

            Thread th1 = new Thread(() -> {
                try (DataScopeImpl ds1 = new DataScopeImpl()) {
                    ctx.increasePointsOptimisticLogic(testJogo.getReferencia(), testCracha.getId().getNome());
                    ds1.validateWork();
                } catch (Exception e) {
                    System.out.println("THROWS EXCEPTION");
                    System.out.println(e.getMessage());
                }
            });

            Thread th2 = new Thread(() -> {
                try (DataScopeImpl ds2 = new DataScopeImpl()) {
                    ctx.increasePointsOptimisticLogic(testJogo.getReferencia(), testCracha.getId().getNome());
                    ds2.validateWork();
                } catch (Exception e) {
                    System.out.println("THROWS EXCEPTION");
                    System.out.println(e.getMessage());
                }
            });

            th1.start();
            th2.start();

            try {
                th1.join();
                th2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ds.cancelWork();  //rollback
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (DataScopeImpl ds = new DataScopeImpl()) {
            ctx.getJogo().delete(ctx.getJogos().findByKey("zzzzzxxxx1"));    // Also deletes cracha because of CASCADE

            ds.validateWork();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pessimisticTest() throws Exception {
        JPAContext ctx = new JPAContext();

        Jogo testJogo = new Jogo("zzzzzxxxx1", "test jogo", "www.img_test");
        CrachaPK crachaPK = new CrachaPK();
        crachaPK.setNome("test cracha");
        crachaPK.setJogo("zzzzzxxxx1");
        Cracha testCracha = new Cracha(crachaPK, "www.imgs/test_cracha", 1000);
        testCracha.setJogo(testJogo);
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();

            em.persist(testJogo);
            em.persist(testCracha);

            ds.validateWork();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try (DataScopeImpl ds = new DataScopeImpl()) {
            assertNotNull(ctx);
            assertNotNull(ctx.getJogos().findByKey("zzzzzxxxx1"));
            assertNotNull(ctx.getCrachas().findByKey(crachaPK));

            Thread th1 = new Thread(() -> {
                try (DataScopeImpl ds1 = new DataScopeImpl()) {
                    ctx.increasePointsPessimisticLogic(testJogo.getReferencia(), testCracha.getId().getNome());
                    ds1.validateWork();
                } catch (Exception e) {
                    System.out.println("THROWS EXCEPTION");
                    System.out.println(e.getMessage());
                }
            });

            Thread th2 = new Thread(() -> {
                try (DataScopeImpl ds2 = new DataScopeImpl()) {
                    ctx.increasePointsPessimisticLogic(testJogo.getReferencia(), testCracha.getId().getNome());
                    ds2.validateWork();
                } catch (Exception e) {
                    System.out.println("THROWS EXCEPTION");
                    System.out.println(e.getMessage());
                }
            });

            th1.start();
            th2.start();

            try {
                th1.join();
                th2.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            ds.cancelWork();  //rollback
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (DataScopeImpl ds = new DataScopeImpl()) {
            ctx.getJogo().delete(ctx.getJogos().findByKey("zzzzzxxxx1"));  // Also deletes cracha because of CASCADE
            ds.validateWork();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
