package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IPartida;

import java.util.Objects;

@Entity
@NamedQuery(name="Partida.findByKey",
        query="SELECT p FROM Partida p WHERE p.id =:key")

@NamedQuery(name="Partida.findAll",
        query="SELECT p FROM Partida p")
@Table(name = "partida")
public class Partida implements IPartida {

    @EmbeddedId
    private PartidaPK id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("jogo")
    @JoinColumn(name = "jogo")
    private Jogo jogo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "regiao")
    private Regiao regiao;

    @Column(name = "tempo_inicio", nullable = false)
    private String tempoInicio;

    @Column(name = "tempo_fim")
    private String tempoFim;

    @OneToOne(mappedBy = "partida")
    private PartidaMultiJogador partidaMultiJogador;

    @OneToOne(mappedBy = "partida")
    private PartidaNormal partidaNormal;

    public Partida(){}
    public Partida(PartidaPK id, Jogo jogo, Regiao regiao, String tempoInicio, String tempoFim){
        this.id = id;
        this.jogo = jogo;
        this.regiao = regiao;
        this.tempoInicio = tempoInicio;
        this.tempoFim = tempoFim;
    }

    @Override
    public PartidaPK getId() {return this.id;}
    @Override
    public Jogo getJogo() {return this.jogo;}
    @Override
    public Regiao getRegiao() {return this.regiao;}
    @Override
    public String getTempoInicio() {return this.tempoInicio;}
    @Override
    public String getTempoFim() {return this.tempoFim;}
    @Override
    public void setId(PartidaPK id) {this.id = id;}
    @Override
    public void setJogo(Jogo jogo) {this.jogo = jogo;}
    @Override
    public void setRegiao(Regiao regiao) {this.regiao = regiao;}
    @Override
    public void setTempoInicio(String tempo) {this.tempoInicio = tempo;}
    @Override
    public void setTempoFim(String tempo) {this.tempoFim = tempo;}

    @Override
    public int hashCode() {
        return Objects.hash(
                this.id,
                this.jogo.getReferencia(),
                this.regiao.getNome(),
                this.tempoInicio,
                this.tempoFim
        );
    }

    @Override
    public String toString() {
        return "Partida[" +
                "id = " + id +
                ", jogo = '" + this.jogo.getReferencia() + '\'' +
                ", regiao = '" + this.regiao.getNome() + '\'' +
                ", tempoInicio = '" + this.tempoInicio + '\'' +
                ", tempoFim = '" + this.tempoFim + '\'' +
                ']';
    }
}
