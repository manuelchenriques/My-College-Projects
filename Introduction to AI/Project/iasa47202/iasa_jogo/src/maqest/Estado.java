package maqest;

import jogo.ambiente.Evento;
import jogo.personagem.Accao;

import java.util.HashMap;

/**
 * Representa um estado da maquina de estados. É composto por um nome e uma listagem de transições possiveis.
 * @param <EV> Evento que pode ocasionar mudança estado
 * @param <AC> Acção associada a um estado
 */
public class Estado<EV, AC>{

    private String nome;
    private HashMap<EV, Transicao<EV, AC>> transicoes;

    /**
     * Inicializa a classe do estado
     * @param nome Nome do Estado
     */
    public Estado(String nome){
        this.nome = nome;
        this.transicoes = new HashMap<>();
    }

    /**
     * @return Nome do Estado
     */
    public String getNome(){
        return nome;
    }

    /**
     * Retorna a transicao, dste estado para um outro, associada ao evento passado
     * @param evento Evento que ocasiona a transicao
     * @return Transicao que seria resultante do evento passado
     */
    public Transicao<EV, AC> processar(EV evento){
        return transicoes.get(evento);
    }

    /**
     * Adiciona uma transição possivel ao Estado
     * @param evento Evento que ocasiona a transicao
     * @param estadoSucessor Estado que vai suceder após o evento
     * @return Estado com a listagem de transicoes actualizada
     */
    public Estado<EV, AC> transicao(EV evento, Estado<EV, AC> estadoSucessor){
        transicao(evento, estadoSucessor, null);
        return this;
    }

    /**
     * Adiciona uma transição possivel ao Estado
     * @param evento Evento que ocasiona a transicao
     * @param estadoSucessor Estado que vai suceder após o evento
     * @param accao Acção associada à transicao
     * @return Estado com a listagem de transicoes actualizada
     */
    public Estado<EV, AC> transicao(EV evento, Estado<EV, AC> estadoSucessor, AC accao){
        Transicao<EV, AC> novaTransicao= new Transicao<>(estadoSucessor, accao);
        this.transicoes.put(evento, novaTransicao);
        return this;
    }

}
