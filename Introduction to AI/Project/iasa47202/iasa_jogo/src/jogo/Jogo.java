package jogo;

import jogo.ambiente.Ambiente;
import jogo.ambiente.Evento;
import jogo.personagem.Personagem;

/**
 * Classe representativa do jogo. O jogo é constituido por um ambiente e um personagem.
 */
class Jogo{

    private static Ambiente ambiente;
    private static Personagem personagem;

    /**
     * Inicializa o ambiente e personagem e, de seguida, inicia o jogo
     * @param args
     */
    public static void main(String[] args){
        ambiente = new Ambiente();
        personagem = new Personagem(ambiente);
        executar();
    }

    /**
     * Fica constantemente em execução, onde permite alterações no ambiente e a
     * respectiva acção do personagem, até que recebe um evento do tipo TERMINAR
     */
    private static void executar(){
        Evento evento = ambiente.getEvento();
        while (evento != Evento.TERMINAR){
            personagem.executar();
            ambiente.evoluir();
            evento = ambiente.getEvento();
        }
        System.out.println("Jogo terminou!");
    }
}