package si.isel.t43dg01.orm.interfaces;

public interface IJogadorTotalInfo {

    Integer getId();
    String getEstado();
    String getEmail();
    String getUsername();
    Integer getNumeroJogosParticipados();
    Integer getNumeroPartidas();
    Integer getTotalPontos();

    void setId(Integer id);
    void setEstado(String estado);
    void setEmail(String email);
    void setUsername(String username);
    void setNumeroJogosParticipados(Integer numeroJogosParticipados);
    void setNumeroPartidas(Integer numeroPartidas);
    void setTotalPontos(Integer totalPontos);
}
