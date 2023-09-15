package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IPartidaMultiJogador;

import java.util.Objects;
import java.util.Set;

@Entity
@NamedQuery(name="PartidaMultiJogador.findByKey",
        query="SELECT pmj FROM PartidaMultiJogador pmj WHERE pmj.id =:key")

@NamedQuery(name="PartidaMultiJogador.findAll",
        query="SELECT pmj FROM PartidaMultiJogador pmj")
@Table(name = "partida_multi_jogador")
public class PartidaMultiJogador implements IPartidaMultiJogador {

    @EmbeddedId
    private PartidaMultiJogadorPK id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumns({
            @JoinColumn(name = "partida", referencedColumnName = "id"),
            @JoinColumn(name = "jogo", referencedColumnName = "jogo")
    })
    private Partida partida;

    @Column(name = "estado", nullable = false)
    private String estado;

    @OneToMany(mappedBy = "partidaMultiJogador")
    private Set<JogadorPartidaMultiJogador> jogadorPartidaMultiJogador;

    public PartidaMultiJogador(){}
    public PartidaMultiJogador(Partida partida, String estado){
        this.partida = partida;
        this.estado = estado;
    }

    @Override
    public PartidaMultiJogadorPK getId() { return this.id; }
    @Override
    public String getEstado() {return this.estado;}
    @Override
    public Partida getPartida() {return this.partida;}

    @Override
    public void setId(PartidaMultiJogadorPK id) { this.id = id; }
    @Override
    public void setEstado(String estado) {this.estado = estado;}
    @Override
    public void setPartida(Partida partida) {this.partida = partida;}

    @Override
    public int hashCode() {

        return Objects.hash(
                this.id.getPartida(),
                this.id.getJogo(),
                this.estado
        );
    }

    @Override
    public String toString() {
        return "PartidaMultiJogador[" +
                "partida = " + this.id.getPartida() +
                ", jogo = '" + this.id.getJogo() + '\'' +
                ", estado = '" + this.estado + '\'' +
                ']';
    }
}
