package si.isel.t43dg01.orm.interfaces;

import si.isel.t43dg01.orm.Jogo;
import si.isel.t43dg01.orm.PartidaPK;
import si.isel.t43dg01.orm.Regiao;

public interface IPartida {

    PartidaPK getId();
    Jogo getJogo();
    Regiao getRegiao();
    String getTempoInicio();
    String getTempoFim();

    void setId(PartidaPK id);
    void setJogo(Jogo jogo);
    void setRegiao(Regiao regiao);
    void setTempoInicio(String tempo);
    void setTempoFim(String tempo);
}