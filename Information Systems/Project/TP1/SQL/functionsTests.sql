BEGIN TRANSACTION;
------------------------------------------------------------------------------------------
---------------------------------------- TESTE D -----------------------------------------
------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE criarJogadorTeste() AS
$$
DECLARE
    jogador_id INT := 0;
BEGIN
    CALL criarJogador('johna@gmail.com', 'John Kurdo', 'NA');
    SELECT id INTO jogador_id FROM JOGADOR WHERE email = 'johna@gmail.com';
    IF jogador_id != 0 THEN
        RAISE NOTICE 'Teste D: Inserir jogador com dados bem passados: Resultado OK';
    ELSE
        RAISE NOTICE 'Teste D: Inserir jogador com dados bem passados: Resultado FAIL';
    END IF;

    CALL criarjogadorlogica('johna@gmail.com', 'John Kurdo', 'AB');
    SELECT id INTO jogador_id FROM JOGADOR WHERE email = 'johna@gmail.com';

    RAISE NOTICE 'Teste D: Falha a inserir jogador com região não existente: Resultado FAIL';

EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Teste D: Falha a inserir jogador com região não existente: Resultado OK';
        ROLLBACK;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE desativarJogadorTeste() AS
$$
DECLARE
    jogador_id INT := 0;
    jogador_estado VARCHAR(20);
BEGIN
    CALL criarjogadorlogica('johna@gmail.com', 'John Kurdo', 'NA');
    SELECT id INTO jogador_id FROM JOGADOR WHERE email = 'johna@gmail.com';

    IF jogador_id = 0 THEN
        RAISE NOTICE 'Teste D: Desativar jogador: Resultado FAIL';
    END IF;

    CALL desativarjogadorlogica(jogador_id);

    SELECT estado INTO jogador_estado FROM JOGADOR WHERE id = jogador_id;

    IF jogador_estado = 'Inativo' THEN
        RAISE NOTICE 'Teste D: Desativar jogador: Resultado OK';
    ELSE
        RAISE NOTICE 'Teste D: Desativar jogador: Resultado FAIL';
    END IF;
    ROLLBACK;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE banirJogadorTeste() AS
$$
DECLARE
    jogador_id INT := 0;
    jogador_estado VARCHAR(20);
BEGIN
    CALL criarjogadorlogica('johna@gmail.com', 'John Kurdo', 'NA');
    SELECT id INTO jogador_id FROM JOGADOR WHERE email = 'johna@gmail.com';

    IF jogador_id = 0 THEN
        RAISE NOTICE 'Teste D: Banir jogador: Resultado FAIL';
    END IF;

    CALL banirjogadorlogica(jogador_id);

    SELECT estado INTO jogador_estado FROM JOGADOR WHERE id = jogador_id;

    IF jogador_estado = 'Banido' THEN
        RAISE NOTICE 'Teste D: Banir jogador: Resultado OK';
    ELSE
        RAISE NOTICE 'Teste D: Banir jogador: Resultado FAIL';
    END IF;
    ROLLBACK;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------
---------------------------------------- TESTE E -----------------------------------------
------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE totalPontosJogadorTeste() AS
$$
DECLARE
    jogador_id INT := 0;
    pontos INT := 0;
BEGIN
    CALL criarjogadorlogica('johna@gmail.com', 'John Kurdo', 'NA');
    SELECT id INTO jogador_id FROM JOGADOR WHERE email = 'johna@gmail.com';

    IF jogador_id = 0 THEN
        RAISE NOTICE 'Teste E: Obter total de pontos de um jogador: Resultado FAIL';
    END IF;

    INSERT INTO JOGO VALUES('jdfSFJfdaf', 'Os Cavaleiros', 'https://oscavalos.com/index');
    INSERT INTO PARTIDA VALUES(1, 'jdfSFJfdaf', 'NA', '26 April 23', '27 April 23');

    INSERT INTO PARTIDA_NORMAL VALUES(1, 'jdfSFJfdaf', jogador_id, 3, 50);

    INSERT INTO PARTIDA_MULTI_JOGADOR VALUES(1, 'jdfSFJfdaf', 'Terminada');

    INSERT INTO JOGADOR_PARTIDA_MULTI_JOGADOR VALUES(1, 'jdfSFJfdaf', jogador_id, 30);

    SELECT totalpontosjogador(jogador_id) INTO pontos;

    RAISE NOTICE 'Pontos: %', pontos;

    IF pontos = 80 THEN
        RAISE NOTICE 'Teste E: Obter total de pontos de um jogador: Resultado OK';
    ELSE
        RAISE NOTICE 'Teste E: Obter total de pontos de um jogador: Resultado FAIL';
    END IF;
    ROLLBACK;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------
---------------------------------------- TESTE F -----------------------------------------
------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE totalJogosJogadorTeste() AS
$$
DECLARE
    jogador_id INT := 0;
    jogos INT := 0;
BEGIN
    CALL criarjogadorlogica('johna@gmail.com', 'John Kurdo', 'NA');
    SELECT id INTO jogador_id FROM JOGADOR WHERE email = 'johna@gmail.com';

    IF jogador_id = 0 THEN
        RAISE NOTICE 'Teste F: Obter total de jogos de um jogador: Resultado FAIL';
    END IF;

    INSERT INTO JOGO VALUES('jdfSFJfdaf', 'Os Cavaleiros', 'https://oscavalos.com/index');
    INSERT INTO JOGO VALUES('asJdfSHfdE', 'Europa Universalis', 'https://eurois.com/play');
    INSERT INTO PARTIDA VALUES(1, 'jdfSFJfdaf', 'NA', '26 April 23', '27 April 23');
    INSERT INTO PARTIDA VALUES(1, 'asJdfSHfdE', 'NA', '26 April 23', '27 April 23');

    INSERT INTO PARTIDA_NORMAL VALUES(1, 'jdfSFJfdaf', jogador_id, 3, 50);

    INSERT INTO PARTIDA_MULTI_JOGADOR VALUES(1, 'asJdfSHfdE', 'Terminada');
    INSERT INTO jogador_partida_multi_jogador VALUES(1, 'asJdfSHfdE', jogador_id, 30);

    SELECT totaljogosjogador(jogador_id) INTO jogos;

    IF jogos = 2 THEN
        RAISE NOTICE 'Teste F: Obter total de jogos de um jogador: Resultado OK';
    ELSE
        RAISE NOTICE 'Teste F: Obter total de jogos de um jogador: Resultado FAIL';
    END IF;
    ROLLBACK;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------
---------------------------------------- TESTE G -----------------------------------------
------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE pontosJogoPorJogadorTeste() AS
$$
DECLARE
    tabela REFCURSOR;
    jogador1_id INT := 0;
    jogador2_id INT := 0;
    jogador INT;
    pontos numeric;
BEGIN
    CALL criarjogadorlogica('johna@gmail.com', 'John Kurdo', 'NA');
    SELECT id INTO jogador1_id FROM JOGADOR WHERE email = 'johna@gmail.com';

    CALL criarjogadorlogica('abigail@hotmail.com', 'Abigail Kurdo', 'NA');
    SELECT id INTO jogador2_id FROM JOGADOR WHERE email = 'abigail@hotmail.com';

    IF jogador1_id = 0 OR jogador2_id = 0 THEN
        RAISE NOTICE 'Teste G: Obter total de pontos num jogo por jogador: Resultado FAIL';
    END IF;

    INSERT INTO JOGO VALUES('jdfSFJfdaf', 'Os Cavaleiros', 'https://oscavalos.com/index');
    INSERT INTO PARTIDA VALUES(1, 'jdfSFJfdaf', 'NA', '26 April 23', '27 April 23');
    INSERT INTO PARTIDA VALUES(2, 'jdfSFJfdaf', 'NA', '26 April 23', '27 April 23');
    INSERT INTO PARTIDA VALUES(3, 'jdfSFJfdaf', 'NA', '26 April 23', '27 April 23');
    INSERT INTO PARTIDA VALUES(4, 'jdfSFJfdaf', 'NA', '26 April 23', '27 April 23');
    INSERT INTO PARTIDA VALUES(5, 'jdfSFJfdaf', 'NA', '26 April 23', '27 April 23');
    INSERT INTO PARTIDA VALUES(6, 'jdfSFJfdaf', 'NA', '26 April 23', '27 April 23');

    INSERT INTO PARTIDA_NORMAL VALUES(1, 'jdfSFJfdaf', jogador1_id, 3, 50);
    INSERT INTO PARTIDA_NORMAL VALUES(3, 'jdfSFJfdaf', jogador1_id, 3, 125);
    INSERT INTO PARTIDA_MULTI_JOGADOR VALUES (5, 'jdfSFJfdaf', 'Terminada');
    INSERT INTO JOGADOR_PARTIDA_MULTI_JOGADOR VALUES (5, 'jdfSFJfdaf', jogador1_id, 25);


    INSERT INTO PARTIDA_NORMAL VALUES(2, 'jdfSFJfdaf', jogador2_id, 3, 50);
    INSERT INTO PARTIDA_MULTI_JOGADOR VALUES(4, 'jdfSFJfdaf', 'Terminada');
    INSERT INTO PARTIDA_MULTI_JOGADOR VALUES(6, 'jdfSFJfdaf', 'Terminada');
    INSERT INTO JOGADOR_PARTIDA_MULTI_JOGADOR VALUES (4, 'jdfSFJfdaf', jogador2_id, 10);
    INSERT INTO JOGADOR_PARTIDA_MULTI_JOGADOR VALUES (6, 'jdfSFJfdaf', jogador2_id, 25);

    OPEN tabela FOR SELECT * FROM pontosjogoporjogador('jdfSFJfdaf');

    FOR i IN 0..1 LOOP
            FETCH NEXT FROM tabela INTO jogador, pontos;
            --RAISE NOTICE 'JOGADOR: %', jogador;
            --RAISE NOTICE 'PONTOS: %', pontos ;

            IF jogador1_id = jogador AND pontos = 200 OR jogador2_id = jogador AND pontos = 85 THEN
                RAISE NOTICE 'Teste G: Obter total de pontos num jogo por jogador: Resultado OK';
            ELSE RAISE NOTICE 'Teste G: Obter total de pontos num jogo por jogador: Resultado FAIL';
            END IF;
        END LOOP;
    CLOSE tabela;

    ROLLBACK;
END;
$$ LANGUAGE plpgsql;


------------------------------------------------------------------------------------------
---------------------------------------- TESTE H -----------------------------------------
------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE associarcrachaTeste() AS
$$
DECLARE
    userID INT;
    gameRef VARCHAR(10);
BEGIN
    CALL criarjogadorlogica('abigail@hotmail.com', 'Abigail Kurdo', 'NA');
    SELECT id INTO userID FROM JOGADOR WHERE email = 'abigail@hotmail.com';
    INSERT INTO jogo VALUES ('ABC', 'Alphabet', 'Alphabet') RETURNING referencia into gameRef;
    INSERT INTO partida VALUES (1, gameRef, 'NA', '26 April 23', '26 April 23');
    INSERT INTO partida VALUES (2, gameRef, 'NA', '26 April 23', '26 April 23');
    INSERT INTO partida_normal VALUES (1, gameRef,userID, 3, 350);
    INSERT INTO cracha VALUES ('CRACHA1', gameRef, 100, 'ABC');
    INSERT INTO cracha VALUES ('CRACHA2', gameRef, 400, 'ABC');

    BEGIN
        CALL associarcrachalogica(userID, 'aAA', 'CRACHA1');
        RAISE NOTICE 'Teste H: Associar cracha com jogo invalido - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste H: Associar cracha com jogo invalido - OK';
    END;

    BEGIN
        CALL associarcrachalogica(userID, gameRef, 'CRACHA3');
        RAISE NOTICE 'Teste H: Associar cracha com cracha invalido - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste H: Associar cracha com cracha invalido - OK';
    END;

    BEGIN
        CALL associarcrachalogica(userID, gameRef, 'CRACHA2');
        RAISE NOTICE 'Teste H: Associar cracha com pontos insuficientes - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste H: Associar cracha com pontos insuficientes - OK';
    END;

    CALL associarcrachalogica(userID, gameRef, 'CRACHA1');
    IF (SELECT nome FROM jogador_cracha WHERE jogador_cracha.jogador = userID AND jogador_cracha.jogo = gameRef AND nome = 'CRACHA1') IS NULL THEN
        RAISE NOTICE 'Teste H: Associar cracha valido - FAIL';
    ELSE
        RAISE NOTICE 'Teste H: Associar cracha valido - OK';
    END IF;
    ROLLBACK;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------
---------------------------------------- TESTE I -----------------------------------------
------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE iniciarconversaTeste() AS
$$
DECLARE
    userID INT;
    bannedUser INT;
    idConversa INT;
BEGIN
    CALL criarjogadorlogica('abigail@hotmail.com', 'Abigail Kurdo', 'NA');
    SELECT id INTO userID FROM JOGADOR WHERE email = 'abigail@hotmail.com';
    CALL criarjogadorlogica('banned@hotmail.bad', 'Banned Player', 'NA');
    SELECT id INTO bannedUser FROM JOGADOR WHERE email = 'banned@hotmail.bad';
    UPDATE jogador SET estado = 'Banido' WHERE id = bannedUser;

    BEGIN
        CALL iniciarconversalogica(101, 'Chat1!', idConversa);
        RAISE NOTICE 'Teste I: Criar chat com jogador invalido - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste I: Criar chat com jogador inexistente - OK';
    END;

    BEGIN
        CALL iniciarconversalogica(bannedUser, 'Chat1!', idConversa);
        RAISE NOTICE 'Teste I: Criar chat com jogador banido - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste I: Criar chat com jogador banido - OK';
    END;

    CALL iniciarconversalogica(userID, 'RealChat', idConversa);
    IF idConversa IS NOT NULL AND (SELECT conversa_participantes.jogador FROM conversa_participantes WHERE conversa_participantes.jogador = userID AND conversa_participantes.conversa = idConversa) IS NOT NULL THEN
        RAISE NOTICE 'Teste I: Criar chat valido - OK';
    ELSE
        RAISE NOTICE 'Teste I: Criar chat valido - FAIL';
    END IF;

    ROLLBACK;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------
---------------------------------------- TESTE J -----------------------------------------
------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE juntarconversaTeste() AS
$$
DECLARE
    userID INT;
    bannedUser INT;
    chatID INT;
BEGIN
    CALL criarjogadorlogica('abigail@hotmail.com', 'Abigail Kurdo', 'NA');
    SELECT id INTO userID FROM JOGADOR WHERE email = 'abigail@hotmail.com';
    CALL criarjogadorlogica('banned@hotmail.bad', 'Banned Player', 'NA');
    SELECT id INTO bannedUser FROM JOGADOR WHERE email = 'banned@hotmail.bad';
    UPDATE jogador SET estado = 'Banido' WHERE id = bannedUser;
    INSERT INTO conversa VALUES (DEFAULT, 'CHAT1') RETURNING id INTO chatID;

    BEGIN
        CALL juntarconversalogica(1010101, chatID);
        RAISE NOTICE 'Teste J: Juntar a chat com jogador inexistente - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste J: Juntar a chat com jogador inexistente - OK';
    END;

    BEGIN
        CALL juntarconversalogica(bannedUser, chatID);
        RAISE NOTICE 'Teste J: Juntar a chat com jogador banido - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste J: Juntar a chat com jogador banido - OK';
    END;

    BEGIN
        CALL juntarconversalogica(userID, 101010101);
        RAISE NOTICE 'Teste J: Juntar a chat inexistente - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste J: Juntar a chat inexistente - OK';
    END;

    CALL juntarconversalogica(userID, chatID);
    IF (SELECT cp.jogador FROM conversa INNER JOIN conversa_participantes cp on conversa.id = cp.conversa WHERE cp.jogador = userID AND cp.conversa = chatID) IS NOT NULL THEN
        RAISE NOTICE 'Teste J: Juntar a chat valido - OK';
    ELSE
        RAISE NOTICE 'Teste J: Juntar a chat valido - FAIL';
    END IF;

    BEGIN
        CALL juntarconversalogica(userID, chatID);
        RAISE NOTICE 'Teste J: Juntar a chat a que já pertence - FAIL';
    EXCEPTION
        WHEN others THEN
            RAISE NOTICE 'Teste J: Juntar a chat a que já pertence - OK';
    END;
    ROLLBACK;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------
---------------------------------------- TESTE K -----------------------------------------
------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE enviarmensagemTeste() AS
$$
DECLARE
    userID INT;
    bannedUser INT;
    chatID INT;
BEGIN
    CALL criarjogadorlogica('abigail@hotmail.com', 'Abigail Kurdo', 'NA');
    SELECT id INTO userID FROM JOGADOR WHERE email = 'abigail@hotmail.com';
    CALL criarjogadorlogica('banned@hotmail.bad', 'Banned Player', 'NA');
    SELECT id INTO bannedUser FROM JOGADOR WHERE email = 'banned@hotmail.bad';
    UPDATE jogador SET estado = 'Banido' WHERE id = bannedUser;
    INSERT INTO conversa VALUES (DEFAULT, 'CHAT1') RETURNING id INTO chatID;

    BEGIN
        CALL enviarmensagemlogica(101010, chatID, 'abc');
        RAISE NOTICE 'Teste K: Enviar mensagem com jogador inexistente - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste K: Enviar mensagem com jogador inexistente - OK';
    END;

    BEGIN
        CALL enviarmensagemlogica(bannedUser, chatID, 'abc');
        RAISE NOTICE 'Teste K: Enviar mensagem com jogador banido - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste K: Enviar mensagem com jogador banido - OK';
    END;

    BEGIN
        CALL enviarmensagemlogica(userID, 1010101, 'abc');
        RAISE NOTICE 'Teste K: Enviar mensagem para chat inexistente - FAIL';
    EXCEPTION
        WHEN SQLSTATE 'P0001' THEN
            RAISE NOTICE 'Teste K: Enviar mensagem para chat inexistente - OK';
    END;

    CALL enviarmensagemlogica(userID, chatID, 'abc');
    IF (SELECT cp.jogador FROM conversa INNER JOIN conversa_participantes cp on conversa.id = cp.conversa WHERE cp.jogador = userID AND cp.conversa = chatID) IS NOT NULL
        AND (SELECT m.jogador FROM conversa INNER JOIN mensagem m on conversa.id = m.conversa WHERE m.conversa = chatID AND m.jogador = userID AND conteudo = 'abc') IS NOT NULL THEN
        RAISE NOTICE 'Teste K: Enviar mensagem valida sem pertencer ao chat - OK';
    ELSE
        RAISE NOTICE 'Teste K: Enviar mensagem valida sem pertencer ao chat - FAIL';
    END IF;

    CALL enviarmensagemlogica(userID, chatID, 'cba');
    IF (SELECT cp.jogador FROM conversa INNER JOIN conversa_participantes cp on conversa.id = cp.conversa WHERE cp.jogador = userID AND cp.conversa = chatID) IS NOT NULL
        AND (SELECT m.jogador FROM conversa INNER JOIN mensagem m on conversa.id = m.conversa WHERE m.conversa = chatID AND m.jogador = userID AND conteudo = 'cba') IS NOT NULL THEN
        RAISE NOTICE 'Teste K: Enviar mensagem valida já pertencendo ao chat - OK';
    ELSE
        RAISE NOTICE 'Teste K: Enviar mensagem valida sem já pertencendo ao chat - FAIL';
    END IF;
    ROLLBACK;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------
---------------------------------------- TESTE M -----------------------------------------
------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE validarCrachasTeste() AS
$$
DECLARE
    userID INT;
    gameRef VARCHAR(10);
    partidaId INT;
BEGIN
    CALL criarjogadorlogica('abigail@hotmail.com', 'Abigail Kurdo', 'NA');
    SELECT id INTO userID FROM JOGADOR WHERE email = 'abigail@hotmail.com';
    INSERT INTO jogo VALUES ('abc', 'Alphabet', 'Alphabet') RETURNING referencia into gameRef;
    INSERT INTO partida VALUES (1, gameRef, 'NA', '26 April 23', NULL) RETURNING id INTO partidaId;
    INSERT INTO partida_normal VALUES (1, gameRef,userID, 3, 350);
    INSERT INTO cracha VALUES ('CRACHA1', gameRef, 100, 'ABCcracha1png.com');
    INSERT INTO cracha VALUES ('CRACHA2', gameRef, 350, 'ABCcracha2png.com');
    INSERT INTO cracha VALUES ('CRACHA3', gameRef, 400, 'ABCcracha3png.com');
    UPDATE partida SET tempo_fim = '26 April 23' WHERE partida.id = partidaId AND jogo = gameRef;
    IF
            (SELECT nome FROM jogador INNER JOIN jogador_cracha jc on jogador.id = jc.jogador WHERE jc.jogador = userID AND jc.jogo = gameRef AND nome ='CRACHA1') IS NOT NULL AND
            (SELECT nome FROM jogador INNER JOIN jogador_cracha jc on jogador.id = jc.jogador WHERE jc.jogador = userID AND jc.jogo = gameRef AND nome ='CRACHA2') IS NOT NULL AND
            (SELECT nome FROM jogador INNER JOIN jogador_cracha jc on jogador.id = jc.jogador WHERE jc.jogador = userID AND jc.jogo = gameRef AND nome ='CRACHA3') IS NULL
    THEN
        RAISE NOTICE 'Teste M: Atribuição de crachas no fim do jogo - OK';
    ELSE
        RAISE NOTICE 'Teste M: Atribuição de crachas no fim do jogo - FAIL';
    END IF;
    ROLLBACK;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------------------------------------
---------------------------------------- TESTE N -----------------------------------------
------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE banirViewTeste() AS
$$
DECLARE
    userID INT;
    bannedID INT;
    state VARCHAR(8);
BEGIN
    CALL criarjogadorlogica('abigail@hotmail.com', 'Abigail Kurdo', 'NA');
    SELECT id INTO userID FROM JOGADOR WHERE email = 'abigail@hotmail.com';
    CALL criarjogadorlogica('banned@hotmail.bad', 'Banned Player', 'NA');
    SELECT id INTO bannedID FROM JOGADOR WHERE email = 'banned@hotmail.bad';

    DELETE FROM jogadortotalinfo WHERE id = bannedID;
    SELECT estado INTO state FROM jogadortotalinfo where id = userID;
    IF state != 'Ativo' THEN
        RAISE NOTICE 'Teste N: Houve afetação no estado de usarios nao envolvidos - FAIL';
    ELSE
        RAISE NOTICE 'Teste N: Houve afetação no estado de usarios nao envolvidos - OK';
    END IF;
    SELECT estado INTO state FROM jogadortotalinfo where id = bannedID;
    IF state != 'Banido' THEN
        RAISE NOTICE 'Teste N: Atribuição dos estados - FAIL';
    ELSE
        RAISE NOTICE 'Teste N: Atribuição dos estados - OK';
    END IF;
    ROLLBACK;
END;
$$ LANGUAGE plpgsql;
COMMIT;


CALL criarJogadorTeste(); --d
CALL desativarJogadorTeste(); --d
CALL banirJogadorTeste(); --d
CALL totalPontosJogadorTeste(); --e
CALL totalJogosJogadorTeste(); --f
CALL pontosJogoPorJogadorTeste(); --g
CALL associarcrachaTeste(); --h
CALL iniciarconversaTeste(); --i
CALL juntarconversaTeste(); --j
CALL enviarmensagemTeste(); --k
CALL validarCrachasTeste(); --m
CALL banirViewTeste(); --n

rollback;



