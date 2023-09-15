package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IRegiao;

import java.util.List;
import java.util.Objects;

@Entity
@NamedQuery(name="Regiao.findByKey",
        query="SELECT r FROM Regiao r WHERE r.nome =:key")

@NamedQuery(name="Regiao.findAll",
        query="SELECT r FROM Regiao r")

@Table(name = "regiao")

public class Regiao implements IRegiao {

    @Id
    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "regiao")
    private List<Jogador> jogadores;

    @OneToMany(mappedBy = "regiao")
    private List<Partida> partidas;

    public Regiao() {}

    public Regiao(String nome) {this.nome = nome;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Regiao regiao = (Regiao) obj;
        return nome == regiao.nome;
    }

    @Override
    public int hashCode() {return Objects.hash(nome);}

    @Override
    public String toString() { return "Regiao[nome = '" + this.nome + "']";}
}
