package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IAmigos;

import java.util.Objects;
import java.util.Set;

@Entity
@NamedQuery(name="Amigos.findByKey",
        query="SELECT a FROM Amigos a WHERE a.id =:key")
@NamedQuery(name="Amigos.findAll",
        query="SELECT a FROM Amigos a")

@Table(name = "amigos")
public class Amigos implements IAmigos {

    public Amigos(){}
    public Amigos(AmigosPK id) {
        this.id = id;
    }

    @EmbeddedId
    private AmigosPK id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumns({
            @JoinColumn(name = "jogador1", referencedColumnName = "jogador"),
            @JoinColumn(name = "jogador2", referencedColumnName = "jogador")
    })
    private Set<Jogador> jogadores;


    @Override
    public AmigosPK getId() { return this.id; }

    @Override
    public void setId(AmigosPK id) { this.id = id; }

    @Override
    public int hashCode() {
        return Objects.hash(id.hashCode());
    }

    @Override
    public String toString() {
        return "Amigos[" +
                "jogador1 = " + this.id.getJogador1() +
                ", jogador2 = " + this.id.getJogador2() +
                ']';
    }
}
