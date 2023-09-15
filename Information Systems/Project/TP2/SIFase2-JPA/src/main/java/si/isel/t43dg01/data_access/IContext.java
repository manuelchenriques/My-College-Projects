package si.isel.t43dg01.data_access;

import si.isel.t43dg01.data_repositories.interfaces.*;

public interface IContext {

    IRegiaoRepository getRegioes();
    IJogoRepository getJogos();
    IJogadorRepository getJogadores();
    IComprasRepository getCompras();
    IPartidaRepository getPartidas();
    IPartidaNormalRepository getPartidasNormais();
    IPartidaMultiJogadorRepository getPartidasMultiJogador();
    IJogadorPartidaMultiJogadorRepository getJogadorPartidasMultiJogador();
    ICrachaRepository getCrachas();
    IJogadorCrachaRepository getJogadorCrachas();
    IAmigosRepository getAmigos();
    IConversaRepository getConversas();
    IConversaParticipantesRepository getParticipantes();
    IMensagemRepository getMensagens();
    IJogadorTotalInfoRepository getAllJogadorTotalInfo();
}
