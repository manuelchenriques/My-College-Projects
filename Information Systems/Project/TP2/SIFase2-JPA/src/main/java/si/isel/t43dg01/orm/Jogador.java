package si.isel.t43dg01.orm;

import si.isel.t43dg01.orm.interfaces.IJogador;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@NamedQuery(name="Jogador.findByKey",
        query="SELECT j FROM Jogador j WHERE j.id =:key")

@NamedQuery(name="Jogador.findAll",
        query="SELECT j FROM Jogador j")
@Table(name = "jogador")

public class Jogador implements IJogador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "estado", nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "regiao")
    private Regiao regiao;

    @OneToMany(mappedBy = "jogador")
    private Set<Compras> compras;

    @ManyToMany(mappedBy = "jogadores")
    private Set<ConversaParticipantes> conversaParticipantes;

    @ManyToMany(mappedBy = "jogador")
    private Set<JogadorCracha> jogadorCrachas;

    @OneToMany(mappedBy = "jogador")
    private Set<Mensagem> mensagens;

    @OneToMany(mappedBy = "jogador")
    private Set<PartidaNormal> partidasNormais;

    @ManyToMany
    private Set<Jogador> amigos;

    public Jogador(){}

    public Jogador(String email, String username, String estado, Regiao regiao){
        this.email = email;
        this.username = username;
        this.estado = estado;
        this.regiao = regiao;
    }

    public Integer getId() {return id;}
    public String getEmail() {return email;}
    public String getUsername() {return username;}
    public String getEstado() { return estado;}
    public Regiao getRegiao() {return regiao;}

    public void setEmail(String email) {this.email = email;}
    public void setUsername(String username) {this.username = username;}
    public void setEstado(String estado) {this.estado = estado;}
    public void setRegiao(Regiao regiao) {this.regiao = regiao;}


    @Override
    public int hashCode() {
        return Objects.hash(id, email, username, estado, regiao.getNome());
    }

    @Override
    public String toString() {
        return "Jogador[" +
                "id = " + this.id +
                ", email = '" + this.email + '\'' +
                ", username = '" + this.username + '\'' +
                ", estado = '" + this.estado + '\'' +
                ", regiao = '" + this.regiao + '\'' +
                ']';
    }
}
