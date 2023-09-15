package si.isel.t43dg01.orm.interfaces;

import si.isel.t43dg01.orm.Conversa;
import si.isel.t43dg01.orm.Jogador;
import si.isel.t43dg01.orm.MensagemPK;

public interface IMensagem {

    MensagemPK getId();
    String getDataEnvio();
    String getConteudo();
    int getNOrdem();
    Conversa getConversa();
    Jogador getJogador();

    void setId(MensagemPK id);
    void setDataEnvio(String dataEnvio);
    void setConteudo(String conteudo);
    void setNOrdem(int nOrdem);
    void setConversa(Conversa conversa);
    void setJogador(Jogador jogador);
}