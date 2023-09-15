package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IConversa;

import java.util.List;
import java.util.Objects;

@Entity
@NamedQuery(name="Conversa.findByKey",
        query="SELECT c FROM Conversa c WHERE c.id =:key")

@NamedQuery(name="Conversa.findAll",
        query="SELECT c FROM Conversa c")
@Table(name = "conversa")

public class Conversa implements IConversa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "conversas")
    private List<ConversaParticipantes> conversaParticipantes;

    @OneToMany(mappedBy = "conversa")
    private List<Mensagem> mensagens;

    public Conversa(int id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public Conversa(){}

    @Override
    public int getId() {return this.id;}

    @Override
    public String getNome() {return this.nome;}

    @Override
    public void setId(int id) {this.id = id;}

    @Override
    public void setNome(String nome) {this.nome = nome;}

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.nome);
    }

    @Override
    public String toString() {
        return "Conversa[" +
                "id = " + this.id +
                ", nome = '" + this.nome + '\'' +
                ']';
    }
}
