package si.isel.t43dg01.presentation;

import si.isel.t43dg01.data_access.JPAContext;
import si.isel.t43dg01.orm.*;

import java.util.*;
import java.util.regex.Pattern;

public class JPAConsole {

    JPAContext context = new JPAContext();

    private final List<Runnable> COMMANDS = new ArrayList<>(Arrays.asList(
            null,
            this::criarJogador,
            this::desativarJogador,
            this::banirJogador,
            this::totalPontosJogador,
            this::totalJogosJogador,
            this::pontosJogoPorJogador,
            this::associarCracha,
            this::iniciarConversa,
            this::juntarConversa,
            this::enviarMensagem,
            this::jogadorTotalInfo,
            this::addBadgeNoProcedures, //2b
            this::addBadge, //2c
            this::increasePointsOptimistic,
            this::increasePointsPessimistic,
            this::end
    ));

    private final List<String> REGIONS = new ArrayList<>(Arrays.asList(
            "NA",
            "SA",
            "EU",
            "AS",
            "AF",
            "OC"
    ));

    private final Scanner input;

    public JPAConsole() {
        this.input = new Scanner(System.in);
        beginConsole();
    }

    private void beginConsole() {
        System.out.println("Welcome!");
        int userResponse;
        while (true) {
            printCommands();
            userResponse = input.nextInt();
            input.nextLine();
            if (userResponse > COMMANDS.size() || userResponse <= 0) {
                System.out.println("Not a valid operation.");
                continue;
            }
            COMMANDS.get(userResponse).run();
        }
    }

    private void criarJogador() {
        System.out.println("Email jogador?");
        String email = getEmail();

        System.out.println("Username jogador?");
        String username = getUserName();

        System.out.println("Região jogador?");
        String regiao = getRegiao();
        context.criarJogadorLogic(email, username, regiao);
        mostrarJogador();
    }

    private void desativarJogador() {
        System.out.println("ID jogador?");
        Integer id = getNum();

        Jogador jogador = context.desativarJogadorLogic(id);
        if (jogador != null)System.out.println(jogador);
    }

    private void banirJogador() {
        System.out.println("ID jogador?");
        Integer id = getNum();

        Jogador jogador = context.banirJogadorLogic(id);
        if (jogador != null)System.out.println(jogador);
    }

    private void totalPontosJogador() {
        System.out.println("ID jogador?");
        Integer id = getNum();
        List<Object[]> results = context.totalPontosJogadorLogic(id);
        if(!results.isEmpty()) {
            for (Object[] row : results) {
                System.out.println("Total pontos: " + row[0] + ".");
            }
        }
    }

    private void totalJogosJogador() {
        System.out.println("ID jogador?");
        Integer id = getNum();
        List<Object[]> results = context.totalJogosJogadorLogic(id);
        if(!results.isEmpty()) {
            for (Object[] row : results) {
                System.out.println("Total Jogos: " + row[0] + ".");
            }
        }
    }

    private void pontosJogoPorJogador() {
        System.out.println("Referência jogo?");
        String ref = getReferencia();
        List<Object[]> results = context.pontosJogoPorJogador(ref);
        if(!results.isEmpty()) {
            for (Object[] row : results) {
                System.out.printf("Jogador: %d;Pontos: %d.\n", (Integer) row[0], (Integer) row[1]);
            }
        }
    }

    private void associarCracha() {
        System.out.println("ID jogador?");
        Integer id = getNum();
        System.out.println("Referência jogo?");
        String ref = getReferencia();
        System.out.println("Nome crachá?");
        String nome = getNomeCracha();
        context.associarCrachaLogic(id, ref, nome);
        mostrarJogadorCracha();
    }

    private void iniciarConversa() {
        System.out.println("ID jogador?");
        Integer id = getNum();
        System.out.println("Nome conversa?");
        String nome = getNomeConversa();

        List<Object[]> results = context.iniciarConversaLogic(id, nome);
        if(!results.isEmpty()) {
            for (Object[] row : results) {
                System.out.printf("ID Conversa: %d.\n", (Integer) row[0]);
            }
        }
    }

    private void juntarConversa() {
        System.out.println("ID jogador?");
        Integer id = getNum();
        System.out.println("ID conversa?");
        Integer idConversa = getNum();
        context.juntarConversaLogic(id, idConversa);
        mostrarConversaParticipantes();
    }

    private void enviarMensagem() {
        System.out.println("ID jogador?");
        Integer id = getNum();
        System.out.println("ID conversa?");
        Integer idConversa = getNum();
        System.out.println("Conteúdo mensagem?");
        String msg = getMensagem();
        context.enviarMensagemLogic(id, idConversa, msg);
        mostrarMensagem();
    }

    private void jogadorTotalInfo() {
        context.jogadorTotalInfoLogic();
        mostrarJogadorTotalInfo();
    }

    private void addBadgeNoProcedures() {
        System.out.println("ID Jogador?");
        Integer idJogador = getNum();
        System.out.println("Referência jogo?");
        String refJogo = getReferencia();
        System.out.println("Nome crachá?");
        String nomeCracha = getNomeCracha();
        context.addBadgeLogicNoProcedures(idJogador, refJogo, nomeCracha);
    }

    private void addBadge() {
        System.out.println("ID Jogador?");
        Integer idJogador = getNum();
        System.out.println("Referência jogo?");
        String refJogo = getReferencia();
        System.out.println("Nome crachá?");
        String nomeCracha = getNomeCracha();
        context.addBadgeLogic(idJogador, refJogo, nomeCracha);
    }

    private void increasePointsOptimistic() {
        System.out.println("Referência jogo?");
        String refJogo = getReferencia();
        System.out.println("Nome crachá?");
        String nomeCracha = getNomeCracha();
        Cracha cracha = context.increasePointsOptimisticLogic(refJogo, nomeCracha);
        if(cracha != null){
            System.out.println(cracha);
        }
    }

    private void increasePointsPessimistic() {
        System.out.println("Referência jogo?");
        String refJogo = getReferencia();
        System.out.println("Nome crachá?");
        String nomeCracha = getNomeCracha();
        Cracha cracha = context.increasePointsPessimisticLogic(refJogo, nomeCracha);
        if(cracha != null){
            System.out.println(cracha);
        }
    }

    private void end() {
        System.out.println("Goodbye!");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            System.out.println(ie.getMessage());
        }
        System.exit(0);
    }

    private void printCommands() {
        System.out.println("Here, you can do the following:");
        System.out.println("1. Add a player");
        System.out.println("2. Deactivate a player");
        System.out.println("3. Ban a player");
        System.out.println("4. Check a player's total points");
        System.out.println("5. Check number of games a player played");
        System.out.println("6. Check all the points per player in a game");
        System.out.println("7. Add a badge to a player");
        System.out.println("8. Start a conversation");
        System.out.println("9. Join a conversation");
        System.out.println("10. Send a message to a conversation");
        System.out.println("11. Check all player's full information");
        System.out.println("12. Add a badge to a player (no procedures)");
        System.out.println("13. Add a badge to a player (different implementation)");
        System.out.println("14. Increase the number of points of a badge (with optimistic locking)");
        System.out.println("15. Increase the number of points of a badge (with a pessimistic approach)");
        System.out.println("16. Exit");
        System.out.println();
        System.out.print("Please type the number of the operation you would like to do. >> ");
    }

    private void mostrarJogador() {
        List<Jogador> allJogadores = context.getJogadores().findAll();

        for (Jogador jogador : allJogadores) {
            System.out.println(jogador);
        }
    }

    private void mostrarJogadorCracha() {
        List<JogadorCracha> allJogadorCrachas = context.getJogadorCrachas().findAll();

        for (JogadorCracha crachaJogador : allJogadorCrachas) {
            System.out.println(crachaJogador);
        }
    }

    private void mostrarConversaParticipantes() {
        List<ConversaParticipantes> allConversaParticipantes = context.getParticipantes().findAll();

        for (ConversaParticipantes conversaParticipantes : allConversaParticipantes) {
            System.out.println(conversaParticipantes);
        }
    }

    private void mostrarMensagem() {
        List<Mensagem> allMensagens = context.getMensagens().findAll();

        for (Mensagem mensagem : allMensagens) {
            System.out.println(mensagem);
        }
    }

    private void mostrarJogadorTotalInfo() {
        List<JogadorTotalInfo> jogadorInfo = context.getAllJogadorTotalInfo().findAll();

        for (JogadorTotalInfo info : jogadorInfo) {
            System.out.println(info);
        }
    }

    private Integer getNum() {
        int num = 0;
        while (num == 0) {
            try {
                num = input.nextInt();
                if (num == 0) System.out.println("0 is not a valid ID!");
            } catch (InputMismatchException ime) {
                System.out.println("Not a number!");
            }
            input.nextLine();
        }

        return num;
    }

    private String getEmail() {
        String email = "";
        do {
            if(!email.equals("")) System.out.println("Email format is not correct!");
            email = input.nextLine();
        }
        while(!Pattern.compile(".+@[^@]+\\.[^@.]+").matcher(email).matches() || email.length() > 250);
        return email;
    }

    private String getRegiao() {
        String regiao = "";
        do {
            if(!regiao.equals("")) System.out.println("That region does not exist!");
            regiao = input.nextLine();
        }
        while(!REGIONS.contains(regiao));
        return regiao;
    }

    private String getUserName() {
        String username = "";
        do {
            if(!username.equals("")) System.out.println("Username is too big!");
            username = input.nextLine();
        }
        while(username.length() > 20);
        return username;
    }

    private String getReferencia() {
        String ref = "";
        do {
            if(!ref.equals("")) System.out.println("Game reference is too big!");
            ref = input.nextLine();
        }
        while(ref.length() > 10);
        return ref;
    }

    private String getNomeCracha() {
        String nomeCracha = "";
        do {
            if(!nomeCracha.equals("")) System.out.println("Badge name is too big!");
            nomeCracha = input.nextLine();
        }
        while(nomeCracha.length() > 50);
        return nomeCracha;
    }

    private String getNomeConversa() {
        String nomeConversa = "";
        do {
            if(!nomeConversa.equals("")) System.out.println("Chat name is too big!");
            nomeConversa = input.nextLine();
        }
        while(nomeConversa.length() > 50);
        return nomeConversa;
    }

    private String getMensagem() {
        String mensagem = "";
        do {
            if(!mensagem.equals("")) System.out.println("Message is too big!");
            mensagem = input.nextLine();
        }
        while(mensagem.length() > 300);
        return mensagem;
    }
}
