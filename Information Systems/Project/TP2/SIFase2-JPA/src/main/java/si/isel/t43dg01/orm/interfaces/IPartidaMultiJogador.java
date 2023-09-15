package si.isel.t43dg01.orm.interfaces;

import si.isel.t43dg01.orm.*;

public interface IPartidaMultiJogador {

    PartidaMultiJogadorPK getId();
    String getEstado();
    Partida getPartida();

    void setId(PartidaMultiJogadorPK id);
    void setEstado(String estado);
    void setPartida(Partida partida);
}