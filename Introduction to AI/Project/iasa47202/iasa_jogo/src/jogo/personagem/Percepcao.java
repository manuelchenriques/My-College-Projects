package jogo.personagem;

import jogo.ambiente.Evento;

/**
 * Esta classe representa a precepção de um evento por parte do personagem
 */
public class Percepcao {

    private Evento evento;

    /**
     * @param evento Evento que foi precepcionado
     */
    public Percepcao(Evento evento){
        this.evento = evento;
    }

    /**
     * @return Evento percepcionado
     */
    public Evento getEvento(){
        return evento;
    }
}
