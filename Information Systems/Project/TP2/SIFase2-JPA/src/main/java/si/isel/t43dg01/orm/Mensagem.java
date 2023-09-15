package si.isel.t43dg01.orm;

import jakarta.persistence.*;
import si.isel.t43dg01.orm.interfaces.IMensagem;

import java.util.Objects;

@Entity
@NamedQuery(name="Mensagem.findByKey",
        query="SELECT m FROM Mensagem m WHERE m.id =:key")

@NamedQuery(name="Mensagem.findAll",
        query="SELECT m FROM Mensagem m")
@Table(name = "mensagem")
public class Mensagem implements IMensagem {

    @EmbeddedId
    private MensagemPK id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("jogador")
    @JoinColumn(name = "jogador")
    private Jogador jogador;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("conversa")
    @JoinColumn(name = "conversa")
    private Conversa conversa;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @MapsId("nOrdem")
    @Column(name = "n_ordem")
    private int nOrdem;

    @Column(name = "data_envio")
    private String dataEnvio;

    @Column(name = "conteudo")
    private String conteudo;

    public Mensagem(){}
    public Mensagem(Jogador jogador, Conversa conversa, int nOrdem, String dataEnvio, String conteudo){
        this.jogador = jogador;
        this.conversa = conversa;
        this.nOrdem = nOrdem;
        this.dataEnvio = dataEnvio;
        this.conteudo = conteudo;
    }

    @Override
    public MensagemPK getId() { return this.id; }
    @Override
    public String getDataEnvio() {return this.dataEnvio;}
    @Override
    public String getConteudo() {return this.conteudo;}
    @Override
    public int getNOrdem() {return this.nOrdem;}
    @Override
    public Conversa getConversa() {return this.conversa;}
    @Override
    public Jogador getJogador() {return this.jogador;}

    @Override
    public void setId(MensagemPK id) { this.id = id; }
    @Override
    public void setDataEnvio(String dataEnvio) {this.dataEnvio = dataEnvio;}
    @Override
    public void setConteudo(String conteudo) {this.conteudo = conteudo;}
    @Override
    public void setNOrdem(int nOrdem) {this.nOrdem = nOrdem;}
    @Override
    public void setConversa(Conversa conversa) {this.conversa = conversa;}
    @Override
    public void setJogador(Jogador jogador) {this.jogador = jogador;}

    @Override
    public int hashCode() {
        return Objects.hash(
                this.jogador.getId(),
                this.conversa.getId(),
                this.nOrdem,
                this.dataEnvio,
                this.conteudo
        );
    }

    @Override
    public String toString() {
        return "Mensagem[" +
                "jogador = " + this.jogador.getId() +
                ", conversa = " + this.conversa.getId() +
                ", nOrdem = " + this.nOrdem +
                ", dataEnvio = '" + this.dataEnvio + '\'' +
                ", conteudo = '" + this.conteudo + '\'' +
                ']';
    }
}
