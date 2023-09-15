package si.isel.t43dg01.data_access;

import jakarta.persistence.*;

import java.sql.Connection;
import java.util.Collections;
import si.isel.t43dg01.data_mappers.*;
import si.isel.t43dg01.data_mappers.interfaces.*;
import si.isel.t43dg01.data_repositories.*;
import si.isel.t43dg01.data_repositories.interfaces.*;
import si.isel.t43dg01.orm.*;

import java.util.List;


public class JPAContext implements IContext {

    private final IRegiaoRepository _regiaoRepository;
    private final IJogadorRepository _jogadorRepository;
    private final IJogoRepository _jogoRepository;
    private final IComprasRepository _comprasRepository;
    private final IPartidaRepository _partidaRepository;
    private final IPartidaNormalRepository _partidaNormalRepository;
    private final IPartidaMultiJogadorRepository _partidaMultiJogadorRepository;
    private final IJogadorPartidaMultiJogadorRepository _jogadorPartidaMultiJogadorRepository;
    private final ICrachaRepository _crachaRepository;
    private final IJogadorCrachaRepository _jogadorCrachaRepository;
    private final IAmigosRepository _amigosRepository;
    private final IConversaRepository _conversaRepository;
    private final IConversaParticipantesRepository _conversaParticipantesRepository;
    private final IMensagemRepository _mensagemRepository;
    private final IJogadorTotalInfoRepository _jogadorTotalInfoRepository;


    private final IRegiaoMapper _regiaoMapper;
    private final IJogadorMapper _jogadorMapper;
    private final IJogoMapper _jogoMapper;
    private final IJogadorCrachaMapper _jogadorCrachaMapper;
    private final ICrachaMapper _crachaMapper;

    public JPAContext() {
        this._regiaoRepository = new RegiaoRepository();
        this._jogadorRepository = new JogadorRepository();
        this._jogoRepository = new JogoRepository();
        this._comprasRepository = new ComprasRepository();
        this._partidaRepository = new PartidaRepository();
        this._partidaNormalRepository = new PartidaNormalRepository();
        this._partidaMultiJogadorRepository = new PartidaMultiJogadorRepository();
        this._jogadorPartidaMultiJogadorRepository = new JogadorPartidaMultiJogadorRepository();
        this._crachaRepository = new CrachaRepository();
        this._jogadorCrachaRepository = new JogadorCrachaRepository();
        this._amigosRepository = new AmigosRepository();
        this._conversaRepository = new ConversaRepository();
        this._conversaParticipantesRepository = new ConversaParticipantesRepository();
        this._mensagemRepository = new MensagemRepository();
        this._jogadorTotalInfoRepository = new JogadorTotalInfoRepository();



        this._regiaoMapper = new RegiaoMapper();
        this._jogadorMapper = new JogadorMapper();
        this._jogoMapper = new JogoMapper();
        this._jogadorCrachaMapper = new JogadorCrachaMapper();
        this._crachaMapper = new CrachaMapper();
    }


    /***                REPOSITORIES                ***/

    @Override
    public IRegiaoRepository getRegioes() {return _regiaoRepository;}

    @Override
    public IJogadorRepository getJogadores(){return _jogadorRepository;}

    @Override
    public IJogoRepository getJogos(){return _jogoRepository;}

    @Override
    public IComprasRepository getCompras(){return _comprasRepository;}

    @Override
    public IPartidaRepository getPartidas(){return _partidaRepository;}

    @Override
    public IPartidaNormalRepository getPartidasNormais(){return _partidaNormalRepository;}

    @Override
    public IPartidaMultiJogadorRepository getPartidasMultiJogador(){return _partidaMultiJogadorRepository;}

    @Override
    public IJogadorPartidaMultiJogadorRepository getJogadorPartidasMultiJogador(){ return _jogadorPartidaMultiJogadorRepository; }

    @Override
    public ICrachaRepository getCrachas(){return _crachaRepository;}

    @Override
    public IJogadorCrachaRepository getJogadorCrachas(){return _jogadorCrachaRepository;}

    @Override
    public IAmigosRepository getAmigos(){return _amigosRepository;}

    @Override
    public IConversaRepository getConversas(){return _conversaRepository;}

    @Override
    public IConversaParticipantesRepository getParticipantes(){return _conversaParticipantesRepository;}

    @Override
    public IMensagemRepository getMensagens(){return _mensagemRepository;}

    @Override
    public IJogadorTotalInfoRepository getAllJogadorTotalInfo(){ return _jogadorTotalInfoRepository; }




    /***                MAPPERS                ***/

    public IRegiaoMapper getRegiao() {return _regiaoMapper;}

    public IJogadorMapper getJogador() {return _jogadorMapper;}

    public IJogoMapper getJogo() {return _jogoMapper;}

    public IJogadorCrachaMapper getJogadorCracha() { return _jogadorCrachaMapper; }

    public ICrachaMapper getCracha() { return _crachaMapper; }
    

    /***                PROCEDURES & FUNCTIONS                ***/

    public void criarJogadorLogic(String email, String username, String regiao) {
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Query query = em.createNativeQuery("CALL criarJogadorLogica(?1, ?2, ?3)");

            query.setParameter(1, email);
            query.setParameter(2, username);
            query.setParameter(3, regiao);

            query.executeUpdate();
            ds.validateWork();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Jogador desativarJogadorLogic(Integer id){
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Connection cn = em.unwrap(Connection.class);
            cn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            Query query = em.createNativeQuery("CALL desativarJogadorLogica(?1)");
            query.setParameter(1, id);
            query.executeUpdate();

            Jogador jogador = getJogador().read(id);
            ds.validateWork();
            return jogador;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Jogador banirJogadorLogic(Integer id) {
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Connection cn = em.unwrap(Connection.class);
            cn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            Query query = em.createNativeQuery("CALL banirJogadorLogica(?1)");
            query.setParameter(1, id);
            query.executeUpdate();

            Jogador jogador = getJogador().read(id);
            ds.validateWork();
            return jogador;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Object[]> totalPontosJogadorLogic(Integer id){
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            StoredProcedureQuery query = em.createStoredProcedureQuery("totalPontosJogador");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.setParameter(1, id);
            query.execute();
            ds.validateWork();
            return (List<Object[]>) query.getResultList();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Object[]> totalJogosJogadorLogic(Integer id) {
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            StoredProcedureQuery query = em.createStoredProcedureQuery("totalJogosJogador");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.setParameter(1, id);
            query.execute();
            ds.validateWork();
            return (List<Object[]>) query.getResultList();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Object[]> pontosJogoPorJogador(String ref) {
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            StoredProcedureQuery query = em.createStoredProcedureQuery("pontosJogoPorJogador");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, ref);
            query.execute();
            ds.validateWork();
            return (List<Object[]>) query.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public void associarCrachaLogic(Integer id, String ref, String nome) {
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Connection cn = em.unwrap(Connection.class);
            cn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            Query query = em.createNativeQuery("CALL associarCrachaLogica(?1, ?2, ?3)");
            query.setParameter(1, id);
            query.setParameter(2, ref);
            query.setParameter(3, nome);
            query.executeUpdate();
            ds.validateWork();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public List<Object[]> iniciarConversaLogic(Integer id, String nome) {
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            StoredProcedureQuery query = em.createStoredProcedureQuery("iniciarConversa");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.setParameter(1, id);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.setParameter(2, nome);
            query.execute();
            ds.validateWork();
            return (List<Object[]>) query.getResultList();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public void juntarConversaLogic(Integer id, Integer idConversa) {
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Connection cn = em.unwrap(Connection.class);
            cn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            Query query = em.createNativeQuery("CALL juntarConversaLogica(?1, ?2)");
            query.setParameter(1, id);
            query.setParameter(2, idConversa);
            query.executeUpdate();
            ds.validateWork();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void enviarMensagemLogic(Integer id, Integer idConversa, String msg) {
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Connection cn = em.unwrap(Connection.class);
            cn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            Query query = em.createNativeQuery("CALL enviarMensagemLogica(?1, ?2, ?3)");
            query.setParameter(1, id);
            query.setParameter(2, idConversa);
            query.setParameter(3, msg);
            query.executeUpdate();
            ds.validateWork();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /***                VIEWS                ***/

    public void jogadorTotalInfoLogic() {
        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Query query = em.createNativeQuery("CREATE OR REPLACE VIEW jogadorTotalInfo as select jogador.id, jogador.estado, email, username,\n" +
                    "\tCOUNT(DISTINCT partida.jogo) AS count_jogos_em_q_participou,\n" +
                    "\t(COUNT(DISTINCT partida_normal.partida) + COUNT(DISTINCT JOGADOR_PARTIDA_MULTI_JOGADOR.partida)) as count_partidas,\n" +
                    "\t(COALESCE(SUM(partida_normal.pontuacao),0)+ COALESCE(SUM(JOGADOR_PARTIDA_MULTI_JOGADOR.pontuacao),0)) AS total_pontos\n" +
                    "FROM jogador\n" +
                    "LEFT JOIN compras ON jogador.id = compras.jogador\n" +
                    "LEFT JOIN partida_normal ON jogador.id = partida_normal.jogador\n" +
                    "LEFT JOIN JOGADOR_PARTIDA_MULTI_JOGADOR ON jogador.id = JOGADOR_PARTIDA_MULTI_JOGADOR.jogador\n" +
                    "LEFT JOIN partida ON (partida_normal.partida = partida.id AND partida_normal.jogo = partida.jogo) OR\n" +
                    "\t(JOGADOR_PARTIDA_MULTI_JOGADOR.partida = partida.id AND JOGADOR_PARTIDA_MULTI_JOGADOR.jogo = partida.jogo)\n" +
                    "WHERE jogador.estado != 'Banido'\n" +
                    "group by jogador.id order by jogador.id;");
            query.executeUpdate();
            ds.validateWork();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /***                OTHER                ***/

    public void addBadgeLogicNoProcedures(Integer idJogador, String refJogo, String nomeCracha) {
        CrachaPK crachaPK = new CrachaPK();
        crachaPK.setJogo(refJogo);
        crachaPK.setNome(nomeCracha);

        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Jogador jogador = getJogadores().findByKey(idJogador);
            if (jogador == null) {
                System.out.println("does not exist");
                ds.cancelWork();
            }

            Query queryPointsGame = em.createNativeQuery("SELECT SUM(COALESCE(PN.pontos, 0) + COALESCE(PM.pontos, 0))::INTEGER as pontos" +
                    " FROM jogador j LEFT JOIN (SELECT pan.jogador, SUM(pan.pontuacao) as pontos FROM partida_normal pan" +
                    " WHERE pan.jogo = ?1 AND pan.jogador = ?2 GROUP BY pan.jogador, pan.jogo) PN ON pn.jogador = j.id" +
                    " LEFT JOIN (SELECT pam.jogador, SUM(pam.pontuacao) as pontos FROM jogador_partida_multi_jogador pam" +
                    " WHERE pam.jogo = ?1 AND pam.jogador = ?2 GROUP BY pam.jogador, pam.jogo) PM ON pm.jogador = j.id" +
                    " WHERE id = ?2 GROUP BY j.id;");
            queryPointsGame.setParameter(1, refJogo);
            queryPointsGame.setParameter(2, idJogador);

            Integer pointsInGame = (Integer) queryPointsGame.getResultList().get(0);

            Query queryLimPoints = em.createNativeQuery("SELECT lim_pontos FROM CRACHA WHERE nome = ?1 AND jogo = ?2;");
            queryLimPoints.setParameter(1, nomeCracha);
            queryLimPoints.setParameter(2, refJogo);

            Integer pointsLimit = (Integer) queryLimPoints.getResultList().get(0);

            Jogo jogo = getJogos().findByKey(refJogo);
            Cracha cracha = getCrachas().findByKey(crachaPK);

            if(pointsInGame>=pointsLimit){
                JogadorCracha jogadorCracha = new JogadorCracha();
                JogadorCrachaPK jogadorCrachaPK = new JogadorCrachaPK();

                jogadorCrachaPK.setJogador(jogador.getId());
                jogadorCrachaPK.setJogo(jogo.getReferencia());
                jogadorCrachaPK.setNome(cracha.getId().getNome());
                jogadorCracha.setId(jogadorCrachaPK);

                getJogadorCracha().create(jogadorCracha);
                ds.validateWork();
            }else{
                System.out.printf("O jogador com id %d não tem pontos suficientes para o cracha %s.\n", idJogador, nomeCracha);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addBadgeLogic(Integer idJogador, String refJogo, String nomeCracha) {
        CrachaPK crachaPK = new CrachaPK();
        crachaPK.setJogo(refJogo);
        crachaPK.setNome(nomeCracha);

        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            StoredProcedureQuery query = em.createStoredProcedureQuery("pontosJogoPorJogador");

            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, refJogo);
            query.execute();
            List<Object[]> results = (List<Object[]>) query.getResultList();

            Integer pontosJogador = 0;

            for (Object[] row : results) if (row[0].equals(idJogador)) {
                pontosJogador = (Integer) row[1];
                break;
            }

            Jogador jogador = getJogadores().findByKey(idJogador);
            if (jogador == null || jogador.getEstado().equals("Banido")) {
                ds.cancelWork();
                return;
            }

            Jogo jogo = getJogos().findByKey(refJogo);
            if (jogo == null) {
                ds.cancelWork();
                return;
            }

            Cracha cracha = getCrachas().findByKey(crachaPK);
            if (cracha == null) {
                ds.cancelWork();
                return;
            }

            if (pontosJogador >= cracha.getLimPontos()) {
                JogadorCracha jogadorCracha = new JogadorCracha();
                JogadorCrachaPK jogadorCrachaPK = new JogadorCrachaPK();
                jogadorCrachaPK.setJogador(jogador.getId());
                jogadorCrachaPK.setJogo(jogo.getReferencia());
                jogadorCrachaPK.setNome(cracha.getId().getNome());
                jogadorCracha.setId(jogadorCrachaPK);

                getJogadorCracha().create(jogadorCracha);
                ds.validateWork();
            } else {
                System.out.printf("O jogador com id %d não tem pontos suficientes para o cracha %s.\n", idJogador, nomeCracha);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Cracha increasePointsOptimisticLogic(String refJogo, String nomeCracha) {
        CrachaPK crachaPK = new CrachaPK();
        crachaPK.setJogo(refJogo);
        crachaPK.setNome(nomeCracha);

        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Cracha cracha = em.find(Cracha.class, crachaPK);
            cracha.setLimPontos((int) (cracha.getLimPontos() * 1.2));

            ds.validateWork();
            return cracha;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Cracha increasePointsPessimisticLogic(String refJogo, String nomeCracha) {
        CrachaPK crachaPK = new CrachaPK();
        crachaPK.setJogo(refJogo);
        crachaPK.setNome(nomeCracha);

        try (DataScopeImpl ds = new DataScopeImpl()) {
            EntityManager em = ds.getEntityManager();
            Cracha cracha = em.find(Cracha.class, crachaPK, LockModeType.PESSIMISTIC_READ);
            cracha.setLimPontos((int) (cracha.getLimPontos() * 1.2));
            ds.validateWork();
            return cracha;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

}
