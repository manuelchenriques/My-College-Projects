package si.isel.t43dg01.orm.interfaces;

import si.isel.t43dg01.orm.*;

public interface IJogadorPartidaMultiJogador {
    JogadorPartidaMultiJogadorPK getId();
    Integer getPontuacao();
    PartidaMultiJogador getPartida();
    Jogador getJogador();

    void setId(JogadorPartidaMultiJogadorPK id);
    void setPontuacao(Integer pontuacao);
    void setPartida(PartidaMultiJogador partida);
    void setJogador(Jogador jogador);
}
