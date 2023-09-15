package jogo.personagem;

import jogo.ambiente.Ambiente;
import jogo.ambiente.Evento;

/**
 * Classe que representa o personagem do jogo. O personagem está ciente do ambiente e
 * do seu estado, de modo a que possa precepcionar eventos e actuar na conformidade dos mesmos
 */
public class Personagem {

    private Ambiente ambiente;
    private Controlo controlo;

    /**
     * Personagem é inserido em um ambiente e fica ciente do seu estado.
     * @param ambiente Ambiente em que o personagem é inserido
     */
    public Personagem(Ambiente ambiente){
        this.ambiente = ambiente;
        this.controlo = new Controlo();
    }

    /**
     * Personagem percepciona um evento no ambiente e em consequencia produz uma acção
     */
    public void executar(){
        Percepcao percepcao = percepcionar();
        Accao accao = controlo.processar(percepcao);
        actuar(accao);
    }

    /**
     * Personagem percepciona o evento que está a ocorrer em Ambiente
     * @return Evento percepcionado
     */
    public Percepcao percepcionar(){
        Evento evento = ambiente.getEvento();
        return new Percepcao(evento);
    }

    /**
     * Mostra na consola uma acção
     * @param accao Acção a mostrar
     */
    public void actuar(Accao accao){
        String name = accao == null ? "Nenhuma" : accao.toString();
        System.out.println("Acção: " + name);
    }
}