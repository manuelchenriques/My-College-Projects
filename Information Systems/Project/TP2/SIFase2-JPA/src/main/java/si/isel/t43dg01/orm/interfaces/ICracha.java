package si.isel.t43dg01.orm.interfaces;

import si.isel.t43dg01.orm.CrachaPK;
import si.isel.t43dg01.orm.Jogo;

public interface ICracha {

    CrachaPK getId();
    Jogo getJogo();
    String getURL();
    int getLimPontos();

    void setId(CrachaPK id);
    void setJogo(Jogo jogo);
    void setURL(String url);
    void setLimPontos(int limPontos);
}