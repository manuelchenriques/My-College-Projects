package jogo.personagem;

import jogo.ambiente.Evento;
import maqest.Estado;
import maqest.MaquinaEstados;


/**
 * Classe a quem recai o gerenciamento do estado do jogo. Aqui são definidos os varios estados e transições possiveis, além de ser onde são definidos os mecanismos para a sua
 * transição.
 */
public class Controlo {

    private MaquinaEstados<Evento, Accao> maqEst;

    /**
     * Inicialização do Controlo, onde são definidos os 4 estados possiveis e as suas respectivas transações, além de se
     * inicializar a maquina de estados. O Estado inicial é "Procurar".
     */
    public Controlo(){

        //Definir estados
        Estado<Evento, Accao> procura = new Estado<>("Procura");
        Estado<Evento, Accao> inspeccao = new Estado<>("Inspecção");
        Estado<Evento, Accao> observacao = new Estado<>("Observação");
        Estado<Evento, Accao> registo = new Estado<>("Registo");

        // Definir transições

        procura
                .transicao(Evento.ANIMAL, observacao, Accao.APROXIMAR)
                .transicao(Evento.RUIDO, inspeccao, Accao.APROXIMAR)
                .transicao(Evento.SILENCIO, procura, Accao.PROCURAR);

        inspeccao
                .transicao(Evento.ANIMAL, observacao, Accao.APROXIMAR)
                .transicao(Evento.RUIDO, inspeccao, Accao.PROCURAR)
                .transicao(Evento.SILENCIO, procura);

        observacao
                .transicao(Evento.ANIMAL, registo, Accao.OBSERVAR)
                .transicao(Evento.FUGA, inspeccao);

        registo
                .transicao(Evento.ANIMAL, registo, Accao.FOTOGRAFAR)
                .transicao(Evento.FUGA, procura)
                .transicao(Evento.FOTOGRAFIA, procura);

        maqEst = new MaquinaEstados<>(procura);
    }

    /**
     * Retorna o estado actual da maquina de estados
     * @return Estado actual
     */
    public Estado<Evento, Accao> getEstado(){
        return maqEst.getEstado();
    }

    /**
     * Ao receber o evento, envia-o para a maquina de estados de modo que esta faça as alterações de estado respectivas.
     * @param percepcao Passa o evento percepcionado
     * @return retorna a acção resultante do evento passado
     */
    public Accao processar(Percepcao percepcao){
        Evento evento = percepcao.getEvento();
        Accao accao = maqEst.processar(evento);
        mostrar();
        return accao;
    }


    /**
     * Mostra o estado actual do jogo na consola.
     */
    private void mostrar(){
        Estado<Evento, Accao> estado = maqEst.getEstado();
        System.out.println("Estado: " + estado.getNome());
    }
}
