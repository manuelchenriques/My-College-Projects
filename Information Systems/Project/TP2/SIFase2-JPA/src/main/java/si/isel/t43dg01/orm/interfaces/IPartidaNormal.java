package si.isel.t43dg01.orm.interfaces;

import si.isel.t43dg01.orm.*;

public interface IPartidaNormal {

    PartidaNormalPK getId();
    Jogador getJogador();
    Integer getDificuldade();
    Integer getPontuacao();
    Partida getPartida();

    void setId(PartidaNormalPK id);
    void setJogador(Jogador jogador);
    void setDificuldade(Integer dificuldade);
    void setPontuacao(Integer pontuacao);
    void setPartida(Partida partida);
}