package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IJogo;

import java.util.List;
import java.util.Objects;

@Entity
@NamedQuery(name="Jogo.findByKey",
        query="SELECT j FROM Jogo j WHERE j.referencia =:key")

@NamedQuery(name="Jogo.findAll",
        query="SELECT j FROM Jogo j")

@Table(name = "jogo")

public class Jogo implements IJogo {

    @Id
    @Column(name = "referencia", nullable = false)
    private String referencia;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "url", nullable = false)
    private String url;

    @OneToMany(mappedBy = "jogo")
    private List<Compras> compras;

    @OneToMany(mappedBy = "jogo")
    private List<Partida> partidas;

    public Jogo(){}

    public Jogo(String referencia, String nome, String url){
        this.referencia = referencia;
        this.nome = nome;
        this.url = url;
    }

    public String getReferencia() {return referencia;}
    public String getNome() {return nome;}
    public String getUrl() {return url;}

    public void setReferencia(String referencia) {this.referencia = referencia;}
    public void setNome(String nome) {this.nome = nome;}
    public void setUrl(String url) {this.url = url;}


    @Override
    public int hashCode() {
        return Objects.hash(referencia, nome, url);
    }

    @Override
    public String toString() {
        return "Jogo[" +
                "referencia = '" + this.referencia + '\'' +
                "', nome = '" + this.nome + '\'' +
                ", url = '" + this.url + '\'' +
                ']';
    }
}
