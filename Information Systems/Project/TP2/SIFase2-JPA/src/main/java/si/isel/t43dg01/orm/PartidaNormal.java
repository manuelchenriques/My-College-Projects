package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IPartidaNormal;

import java.util.Objects;

@Entity
@NamedQuery(name="PartidaNormal.findByKey",
        query="SELECT pn FROM PartidaNormal pn WHERE pn.id =:key")

@NamedQuery(name="PartidaNormal.findAll",
        query="SELECT pn FROM PartidaNormal pn")
@Table(name = "partida_normal")
public class PartidaNormal implements IPartidaNormal {

    @EmbeddedId
    private PartidaNormalPK id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumns({
        @JoinColumn(name = "partida", referencedColumnName = "id"),
        @JoinColumn(name = "jogo", referencedColumnName = "jogo")
    })
    private Partida partida;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "jogador")
    private Jogador jogador;

    @Column(name = "dificuldade", nullable = false)
    private Integer dificuldade;

    @Column(name = "pontuacao", nullable = false)
    private Integer pontuacao;

    public PartidaNormal(){}
    public PartidaNormal(Partida partida, Jogador jogador, int pontuacao, int dificuldade){
        this.partida = partida;
        this.jogador = jogador;
        this.pontuacao = pontuacao;
        this.dificuldade = dificuldade;
    }


    @Override
    public PartidaNormalPK getId() { return this.id; }
    @Override
    public Jogador getJogador() {return this.jogador;}
    @Override
    public Integer getDificuldade() {return this.dificuldade;}
    @Override
    public Integer getPontuacao() {return this.pontuacao;}
    @Override
    public Partida getPartida() {return this.partida;}

    @Override
    public void setId(PartidaNormalPK id) { this.id = id; }
    @Override
    public void setJogador(Jogador jogador) { this.jogador = jogador; }
    @Override
    public void setDificuldade(Integer dificuldade) { this.dificuldade = dificuldade; }
    @Override
    public void setPontuacao(Integer pontuacao) {this.pontuacao = pontuacao; }
    @Override
    public void setPartida(Partida partida) { this.partida = partida; }

    @Override
    public int hashCode() {

        return Objects.hash(
                this.id.getPartida(),
                this.id.getJogo(),
                this.jogador.getId(),
                this.pontuacao,
                this.dificuldade
        );
    }

    @Override
    public String toString() {
        return "PartidaNormal[" +
                "partida = " + this.partida.getId() +
                ", jogo = '" + this.id.getJogo() + '\'' +
                ", jogador = " + this.jogador.getId() +
                ", pontuacao = " + this.pontuacao +
                ", dificuldade = " + this.dificuldade +
                ']';
    }
}
