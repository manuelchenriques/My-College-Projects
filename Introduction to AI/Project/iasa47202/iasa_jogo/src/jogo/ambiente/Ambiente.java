package jogo.ambiente;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Classe representativa do ambiente do jogo e, consequentemente, responsavel pelo
 * gerenciamento dos eventos que ocorrem no ambiente.
 */
public class Ambiente {

    private Evento evento;
    private HashMap<String, Evento> eventMap;

    /**
     * Inicializa um hashmap que associa uma string a um evento
     */
    public Ambiente(){
        this.eventMap = new HashMap<>();

        eventMap.put("ANIMAL", Evento.ANIMAL);
        eventMap.put("FUGA", Evento.FUGA);
        eventMap.put("RUIDO", Evento.RUIDO);
        eventMap.put("SILENCIO", Evento.SILENCIO);
        eventMap.put("FOTOGRAFIA", Evento.FOTOGRAFIA);
        eventMap.put("TERMINAR", Evento.TERMINAR);
    }

    /**
     * @return evento actual
     */
    public Evento getEvento(){
        return evento;
    }

    /**
     * Provoca uma alteraçãp no ambiente, actualizando o evento que está a ocorrer
     * e mostrando na consola
     */
    public void evoluir(){
        gerarEvento();
        mostrar();
    }

    /**
     * Obtem um novo evento pela consola e, caso valido, actualiza
     * o estado do ambiente.
     * @return Novo evento
     */
    private Evento gerarEvento(){
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("Insert event: ");
            String input = scanner.nextLine();
            if (eventMap.containsKey(input.toUpperCase())){
                this.evento = eventMap.get(input.toUpperCase());
                break;
            }
            System.out.println("Evento invalido.");
        }
        return this.evento;
    }

    /**
     * Escreve na consola o evento que se encontra activo no momento
     */
    private void mostrar(){
        System.out.println("Event: " + evento.toString());
    }

}