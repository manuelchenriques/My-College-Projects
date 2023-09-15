package si.isel.t43dg01.orm.interfaces;

import si.isel.t43dg01.orm.ComprasPK;
import si.isel.t43dg01.orm.Jogador;
import si.isel.t43dg01.orm.Jogo;

public interface ICompras {

    ComprasPK getId();
    Jogador getJogador();
    Jogo getJogo();
    float getPreco();
    String getDataCompra();

    void setId(ComprasPK id);
    void setJogador(Jogador jogador);
    void setJogo(Jogo jogo);
    void setPreco(float preco);
    void setDataCompra(String dataCompra);
}