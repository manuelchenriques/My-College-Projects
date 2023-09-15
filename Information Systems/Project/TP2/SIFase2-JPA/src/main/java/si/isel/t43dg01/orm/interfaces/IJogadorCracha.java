package si.isel.t43dg01.orm.interfaces;

import si.isel.t43dg01.orm.Cracha;
import si.isel.t43dg01.orm.Jogador;
import si.isel.t43dg01.orm.JogadorCrachaPK;

import java.util.Set;

public interface IJogadorCracha {

    JogadorCrachaPK getId();
    Set<Cracha> getCrachas();
    Set<Jogador> getJogador();

    void setId(JogadorCrachaPK id);
    void setCrachas(Set<Cracha> crachas);
    void setJogador(Set<Jogador> jogador);
}