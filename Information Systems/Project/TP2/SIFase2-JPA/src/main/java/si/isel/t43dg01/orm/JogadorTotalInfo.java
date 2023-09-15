package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IJogadorTotalInfo;

import java.util.Objects;

@Entity
@NamedQuery(name="JogadorTotalInfo.findByKey",
        query="SELECT jti FROM JogadorTotalInfo jti WHERE jti.id=:id")

@NamedQuery(name="JogadorTotalInfo.findAll",
        query="SELECT jti FROM JogadorTotalInfo jti")
@Table(name = "jogadortotalinfo")
public class JogadorTotalInfo implements IJogadorTotalInfo {

    @Id
    private Integer id;

    private String estado;

    private String email;

    private String username;

    @Column(name = "count_jogos_em_q_participou")
    private Integer numeroJogosParticipados;

    @Column(name = "count_partidas")
    private Integer numeroPartidas;

    @Column(name = "total_pontos")
    private Integer totalPontos;

    @Override
    public Integer getId() { return this.id; }

    @Override
    public String getEstado() { return this.estado; }

    @Override
    public String getEmail() { return this.email; }

    @Override
    public String getUsername() { return this.username; }

    @Override
    public Integer getNumeroJogosParticipados() { return this.numeroJogosParticipados; }

    @Override
    public Integer getNumeroPartidas() { return this.numeroPartidas; }

    @Override
    public Integer getTotalPontos() { return this.totalPontos; }

    @Override
    public void setId(Integer id) { this.id = id; }

    @Override
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public void setEmail(String email) { this.email = email; }

    @Override
    public void setUsername(String username) { this.username = username; }

    @Override
    public void setNumeroJogosParticipados(Integer numeroJogosParticipados) { this.numeroJogosParticipados = numeroJogosParticipados; }

    @Override
    public void setNumeroPartidas(Integer numeroPartidas) { this.numeroPartidas = numeroPartidas; }

    @Override
    public void setTotalPontos(Integer totalPontos) { this.totalPontos = totalPontos; }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.estado, this.email, this.username, this.numeroJogosParticipados, this.numeroPartidas, this.totalPontos);
    }

    @Override
    public String toString() {
        return "JogadorTotalInfo[" +
                "id = " + this.id +
                ", estado = '" + this.estado + '\'' +
                ", email = '" + this.email + '\'' +
                ", username = '" + this.username + '\'' +
                ", numero_jogos_em_q_participou = " + this.numeroJogosParticipados +
                ", count_partidas = " + this.numeroPartidas +
                ", total_pontos = " + this.totalPontos +
                ']';
    }
}
