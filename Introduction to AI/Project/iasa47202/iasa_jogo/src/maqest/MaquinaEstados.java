package maqest;

/**
 *  Maquina de estados. É constituida por um estado activo, que pode ser alterado por via de transicoes,
 *  que por sua vez são provocadas por eventos
 * @param <EV> Evento que pode ocasionar mudança estado
 * @param <AC> Acção associada a um estado
 */
public class MaquinaEstados <EV, AC> {

    private Estado<EV, AC> estado;

    /**
     * Inicialização da maquina de estados com um estado inicial
     * @param estado Estado inicial
     */
    public MaquinaEstados(Estado<EV, AC> estado){
        this.estado = estado;
    }

    /**
     * @return Estado activo
     */
    public Estado<EV, AC> getEstado(){
        return estado;
    }

    /**
     * Recebe um evento e, caso o estaco activo tenha configurada uma transicao que ocorre por via desse mesmo evento,
     * provoca a mudança de estado
     * @param evento Evento que pode provocas mudança de estado
     * @return Acção associada à transicao
     */
    public AC processar(EV evento){
        Transicao<EV, AC> transicao = estado.processar(evento);
        if (transicao != null){
            this.estado = transicao.getEstadoSucessor();
            return transicao.getAccao();
        } else return null;
    }
}
