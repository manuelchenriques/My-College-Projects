package maqest;


/**
 * Representa a transição para um outro estado
 * @param <EV> Evento que pode ocasionar mudança estado
 * @param <AC> Acção associada a um estado
 */
public class Transicao<EV, AC>{

    private AC accao;
    private Estado<EV, AC> estadoSucessor;

    /**
     * Initialização da transição
     * @param estadoSucessor Estado que vai suceder o actual
     * @param accao Acção associada ao novo estado
     */
    public Transicao( Estado<EV, AC> estadoSucessor, AC accao){
        this.accao = accao;
        this.estadoSucessor = estadoSucessor;
    }

    /**
     * @return Estado que vai suceder o estado actual
     */
    public Estado<EV, AC> getEstadoSucessor(){
        return estadoSucessor;
    }

    /**
     * @return Acção associada ao estado
     */
    public AC getAccao(){
        return accao;
    }
}
