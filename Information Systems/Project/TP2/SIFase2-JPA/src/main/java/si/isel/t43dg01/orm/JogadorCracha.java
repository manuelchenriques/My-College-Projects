package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IJogadorCracha;

import java.util.Objects;
import java.util.Set;

@Entity
@NamedQuery(name="JogadorCracha.findByKey",
        query="SELECT j FROM JogadorCracha j WHERE j.id =:key")
@NamedQuery(name="JogadorCracha.findAll",
        query="SELECT j FROM JogadorCracha j")
@Table(name = "jogador_cracha")
public class JogadorCracha implements IJogadorCracha {

    @EmbeddedId
    private JogadorCrachaPK id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumns({
            @JoinColumn(name = "nome", referencedColumnName = "nome"),
            @JoinColumn(name = "jogo", referencedColumnName = "jogo")
    })
    private Set<Cracha> crachas;


    @MapsId("jogador")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "jogador")
    private Set<Jogador> jogador;


    public JogadorCracha(){}
    public JogadorCracha(Set<Jogador> jogador, Set<Cracha> crachas){
        this.jogador = jogador;
        this.crachas = crachas;
    }

    @Override
    public JogadorCrachaPK getId() { return this.id; }
    @Override
    public Set<Cracha> getCrachas() {return this.crachas;}
    @Override
    public Set<Jogador> getJogador() {return this.jogador;}

    @Override
    public void setId(JogadorCrachaPK id) { this.id = id; }
    @Override
    public void setCrachas(Set<Cracha> crachas) {this.crachas = crachas;}
    @Override
    public void setJogador(Set<Jogador> jogador) {this.jogador = jogador;}

    @Override
    public int hashCode() {
        return Objects.hash(this.id.getJogador(), this.id.getNome(), this.id.getJogo());
    }

    @Override
    public String toString() {
        return "JogadorCracha[" +
                "jogador = " + this.id.getJogador() +
                ", jogo = '" + this.id.getJogo() + '\'' +
                ", nome = '" + this.id.getNome() + '\'' +
                ']';
    }
}
