package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import org.eclipse.persistence.annotations.OptimisticLocking;
import org.eclipse.persistence.annotations.OptimisticLockingType;
import si.isel.t43dg01.orm.interfaces.ICracha;

import java.util.Objects;
import java.util.Set;

@Entity
@NamedQuery(name="Cracha.findByKey",
        query="SELECT c FROM Cracha c WHERE c.id =:key")

@NamedQuery(name="Cracha.findAll",
        query="SELECT c FROM Cracha c")
@OptimisticLocking(type = OptimisticLockingType.CHANGED_COLUMNS)
@Table(name = "cracha")

public class Cracha implements ICracha {

    @EmbeddedId
    private CrachaPK id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("jogo")
    @JoinColumn(name = "jogo")
    private Jogo jogo;
    @Column(name = "lim_pontos", nullable = false)
    private int limPontos;
    @Column(name = "url", nullable = false)
    private String url;

    @ManyToMany(mappedBy = "crachas")
    private Set<JogadorCracha> jogadorCrachas;

    public Cracha(){}
    public Cracha(Jogo jogo, String url, int limPontos){
        this.jogo = jogo;
        this.limPontos = limPontos;
        this.url = url;
    }

    public Cracha(CrachaPK pk, String url, int limPontos){
        this.id = pk;
        this.limPontos = limPontos;
        this.url = url;
    }

    @Override
    public CrachaPK getId() { return this.id; }

    @Override
    public Jogo getJogo() {return this.jogo;}
    @Override
    public String getURL() {return this.url;}
    @Override
    public int getLimPontos() {return this.limPontos;}

    @Override
    public void setId(CrachaPK id) { this.id = id; }

    @Override
    public void setJogo(Jogo jogo) { this.jogo = jogo; }
    @Override
    public void setURL(String url) { this.url = url; }
    @Override
    public void setLimPontos(int limPontos) { this.limPontos = limPontos; }

    @Override
    public int hashCode() {
        return Objects.hash(jogo.getReferencia(), url, limPontos);
    }

    @Override
    public String toString() {
        return "Cracha[" +
                "nome = " + this.id.getNome() +
                ", jogo = '" + this.id.getJogo() + '\'' +
                ", lim_pontos = " + this.limPontos +
                ", url = '" + this.url + '\'' +
                ']';
    }
}
