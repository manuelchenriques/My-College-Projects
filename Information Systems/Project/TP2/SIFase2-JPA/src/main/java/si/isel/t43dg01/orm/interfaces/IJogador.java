package si.isel.t43dg01.orm.interfaces;

import si.isel.t43dg01.orm.Regiao;

public interface IJogador {

    Integer getId();
    String getEmail();
    String getUsername();
    String getEstado();
    Regiao getRegiao();

    void setEmail(String email);
    void setUsername(String username);
    void setEstado(String estado);
    void setRegiao(Regiao regiao);
}
