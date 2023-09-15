package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.ICompras;

import java.util.Objects;

@Entity
@NamedQuery(name="Compras.findByKey",
        query="SELECT c FROM Compras c WHERE c.id =:key")

@NamedQuery(name="Compras.findAll",
        query="SELECT c FROM Compras c")
@Table(name = "compras")

public class Compras implements ICompras {

    @EmbeddedId
    private ComprasPK id;

    @ManyToOne
    @MapsId("jogador")
    @JoinColumn(name = "jogador", nullable = false)
    private Jogador jogador;

    @ManyToOne
    @MapsId("jogo")
    @JoinColumn(name = "jogo", nullable = false)
    private Jogo jogo;

    @Column(name = "preco", nullable = false)
    private float preco;

    @Column(name = "data_compra", nullable = false)
    private String dataCompra;

    public Compras(){}
    public Compras(Jogador jogador,  Jogo jogo, float preco, String dataCompra){
        this.jogador = jogador;
        this.jogo = jogo;
        this.preco = preco;
        this.dataCompra = dataCompra;
    }

    @Override
    public ComprasPK getId() { return this.id; }
    @Override
    public Jogador getJogador() {return this.jogador;}
    @Override
    public Jogo getJogo() {return this.jogo;}
    @Override
    public float getPreco() {return this.preco;}
    @Override
    public String getDataCompra() {return this.dataCompra;}
    @Override
    public void setId(ComprasPK id) { this.id = id; }
    @Override
    public void setJogador(Jogador jogador) {this.jogador = jogador;}
    @Override
    public void setJogo(Jogo jogo) {this.jogo = jogo;}
    @Override
    public void setPreco(float preco) {this.preco = preco;}
    @Override
    public void setDataCompra(String dataCompra) {this.dataCompra = dataCompra;}


    @Override
    public int hashCode() {
        return Objects.hash(this.jogador.getId(), this.jogo.getNome(), preco, dataCompra);
    }

    @Override
    public String toString() {
        return "Compras[" +
                "jogador = " + this.jogador.getId() +
                ", jogo = '" + this.jogo.getNome() + '\'' +
                ", preco = " + this.preco +
                ", data de compra = '" + this.dataCompra + '\'' +
                "]";
    }
}
