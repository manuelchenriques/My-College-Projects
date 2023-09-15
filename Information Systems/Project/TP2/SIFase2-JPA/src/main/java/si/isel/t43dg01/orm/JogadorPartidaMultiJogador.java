package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IJogadorPartidaMultiJogador;

import java.util.Objects;

@Entity
@NamedQuery(name="JogadorPartidaMultiJogador.findByKey",
        query="SELECT jpmj FROM JogadorPartidaMultiJogador jpmj WHERE jpmj.id =:key")

@NamedQuery(name="JogadorPartidaMultiJogador.findAll",
        query="SELECT jpmj FROM JogadorPartidaMultiJogador jpmj")
@Table(name = "jogador_partida_multi_jogador")
public class JogadorPartidaMultiJogador implements IJogadorPartidaMultiJogador {

    @EmbeddedId
    private JogadorPartidaMultiJogadorPK id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumns({
            @JoinColumn(name = "partida", referencedColumnName = "partida"),
            @JoinColumn(name = "jogo", referencedColumnName = "jogo")
    })
    private PartidaMultiJogador partidaMultiJogador;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "jogador")
    private Jogador jogador;

    @Column(name = "pontuacao", nullable = false)
    private Integer pontuacao;

    public JogadorPartidaMultiJogador(){}
    public JogadorPartidaMultiJogador(PartidaMultiJogador partidaMultiJogador, Jogador jogador, int pontuacao){
        this.partidaMultiJogador = partidaMultiJogador;
        this.jogador = jogador;
        this.pontuacao = pontuacao;
    }

    @Override
    public JogadorPartidaMultiJogadorPK getId() { return this.id; }
    @Override
    public Integer getPontuacao() {return this.pontuacao;}
    @Override
    public PartidaMultiJogador getPartida() {return this.partidaMultiJogador;}
    @Override
    public Jogador getJogador() {return this.jogador;}

    @Override
    public void setId(JogadorPartidaMultiJogadorPK id) { this.id = id; }
    @Override
    public void setPontuacao(Integer pontuacao) {this.pontuacao = pontuacao;}
    @Override
    public void setPartida(PartidaMultiJogador partida) {this.partidaMultiJogador = partida;}
    @Override
    public void setJogador(Jogador jogador) {this.jogador = jogador;}

    @Override
    public int hashCode() {

        return Objects.hash(
                this.getId().getPartida(),
                this.getId().getJogo(),
                this.getId().getJogador(),
                this.pontuacao
        );
    }

    @Override
    public String toString() {
        return "JogadorPartidaMultiJogador[" +
                "partida = " + this.id.getPartida() +
                ", jogo = '" + this.id.getJogo() + '\'' +
                ", jogador = " + this.id.getJogador() +
                ", pontuacao = " + this.pontuacao +
                ']';
    }
}
