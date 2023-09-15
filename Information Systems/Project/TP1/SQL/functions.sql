-------------------------------------------------------------------------------------------
---------------------------------------- ALÍNEA D -----------------------------------------
-------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE criarJogadorLogica(email_jogador VARCHAR(250), username_jogador VARCHAR(20), regiao_jogador VARCHAR(2)) AS $$
DECLARE
    verificar_regiao VARCHAR(2);
    id_jogador INT;
BEGIN
    SELECT nome INTO verificar_regiao FROM REGIAO WHERE nome = regiao_jogador;

    IF verificar_regiao IS NOT NULL THEN
        INSERT INTO JOGADOR VALUES(DEFAULT, email_jogador, username_jogador, 'Ativo', regiao_jogador) RETURNING id INTO id_jogador;
    ELSE
        RAISE EXCEPTION 'Região com nome % não existe', regiao_jogador;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE desativarJogadorLogica(id_jogador INT) AS $$
DECLARE
    verificar_jogador INT;
BEGIN
    SELECT id INTO verificar_jogador FROM JOGADOR WHERE id = id_jogador;

    IF verificar_jogador IS NOT NULL THEN
        UPDATE JOGADOR SET estado = 'Inativo' WHERE id = id_jogador;
    ELSE
        RAISE EXCEPTION 'Jogador com id % não existe.', id_jogador;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE banirJogadorLogica(id_jogador INT) AS $$
DECLARE
    verificar_jogador INT;
BEGIN
    SELECT id INTO verificar_jogador FROM JOGADOR WHERE id = id_jogador;

    IF verificar_jogador IS NOT NULL THEN
        UPDATE JOGADOR SET estado = 'Banido' WHERE id = id_jogador;
    ELSE
        RAISE EXCEPTION 'Jogador com id % não existe.', id_jogador;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE criarJogador(email_jogador VARCHAR(250), username_jogador VARCHAR(20), regiao_jogador VARCHAR(2)) AS $$
DECLARE
    code CHAR(5) DEFAULT '00000';
    msg TEXT;
BEGIN
    ROLLBACK;
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    BEGIN
        CALL criarJogadorLogica(email_jogador, username_jogador, regiao_jogador);
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Ocorreu algum erro';
            GET stacked DIAGNOSTICS
                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
            RAISE NOTICE 'code=%, msg=%',code,msg;
            ROLLBACK;
    END;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE desativarJogador(id_jogador INT) AS $$
DECLARE
    code CHAR(5) DEFAULT '00000';
    msg TEXT;
BEGIN
    ROLLBACK;
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    BEGIN
        CALL desativarJogadorLogica(id_jogador);
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Ocorreu algum erro';
            GET stacked DIAGNOSTICS
                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
            RAISE NOTICE 'code=%, msg=%',code,msg;
            ROLLBACK;
    END;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE banirJogador(id_jogador INT) AS $$
DECLARE
    code CHAR(5) DEFAULT '00000';
    msg TEXT;
BEGIN
    ROLLBACK;
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    BEGIN
        CALL banirJogadorLogica(id_jogador);
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Ocorreu algum erro';
            GET stacked DIAGNOSTICS
                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
            RAISE NOTICE 'code=%, msg=%',code,msg;
            ROLLBACK;
    END;
END;
$$ LANGUAGE plpgsql;

-------------------------------------------------------------------------------------------
---------------------------------------- ALÍNEA E -----------------------------------------
-------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION totalPontosJogador(id_jogador INT) RETURNS INT AS $$
DECLARE
    pontos INT;
BEGIN
    SELECT SUM(COALESCE(PN.pontos, 0) + COALESCE(PM.pontos, 0))::INTEGER as pontos INTO pontos FROM jogador j
                                                                                                        FULL OUTER JOIN (SELECT p.jogador, SUM(pontuacao) AS pontos FROM partida_normal p WHERE p.jogador = id_jogador group by p.jogador) pn ON j.id = pn.jogador
                                                                                                        FULL OUTER JOIN (SELECT jpm.jogador, SUM(jpm.pontuacao) AS pontos FROM jogador_partida_multi_jogador jpm WHERE jpm.jogador = id_jogador group by jpm.jogador) pm ON j.id = pm.jogador
    WHERE j.id = id_jogador
    group by j.id;

    RETURN pontos;
END;
$$ LANGUAGE plpgsql;

-------------------------------------------------------------------------------------------
---------------------------------------- ALÍNEA F -----------------------------------------
-------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION totalJogosJogador(id_jogador INT) RETURNS INT AS $$
DECLARE
    jogos INT;
BEGIN
    SELECT COUNT(DISTINCT partida.jogo)
    INTO jogos
    FROM jogador
             LEFT JOIN partida_normal ON jogador.id = partida_normal.jogador
             LEFT JOIN jogador_partida_multi_jogador ON jogador.id = jogador_partida_multi_jogador.jogador
             LEFT JOIN partida ON (partida_normal.partida = partida.id AND partida_normal.jogo = partida.jogo) OR
                                  (jogador_partida_multi_jogador.partida = partida.id AND jogador_partida_multi_jogador.jogo = partida.jogo)
    WHERE jogador.id =  id_jogador;

    RETURN jogos;
END;
$$ LANGUAGE plpgsql;

-------------------------------------------------------------------------------------------
---------------------------------------- ALÍNEA G -----------------------------------------
-------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION pontosJogoPorJogador(referencia_jogo VARCHAR(10)) RETURNS TABLE(jogador INT, pontos INT) AS $$
BEGIN
    RETURN QUERY SELECT id, SUM(COALESCE(PN.pontos, 0) + COALESCE(PM.pontos, 0))::INTEGER as pontos FROM jogador j
                                                                                                             LEFT JOIN (
        SELECT pan.jogador, SUM(pan.pontuacao) as pontos
        FROM partida_normal pan
        WHERE pan.jogo = referencia_jogo
        GROUP BY pan.jogador, pan.jogo
    ) PN ON pn.jogador = j.id
                                                                                                             LEFT JOIN (
        SELECT pam.jogador, SUM(pam.pontuacao) as pontos
        FROM jogador_partida_multi_jogador pam
        WHERE pam.jogo = referencia_jogo
        GROUP BY pam.jogador, pam.jogo
    ) PM ON pm.jogador = j.id
                 WHERE PN.jogador IS NOT NULL OR PM.jogador IS NOT NULL
                 GROUP BY j.id;
END;
$$ LANGUAGE plpgsql;

-------------------------------------------------------------------------------------------
---------------------------------------- ALÍNEA H -----------------------------------------
-------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE associarCrachaLogica(id_jogador INT, referencia_jogo VARCHAR(10), nome_cracha VARCHAR(50)) AS $$
DECLARE
    verificar_jogador INT;
    verificar_jogo varchar(10);
    verificar_cracha varchar(50);
    pontos_jogador numeric := 0;
    pontos_necessarios INT;
BEGIN
    SELECT id INTO verificar_jogador FROM jogador WHERE id = id_jogador AND estado != 'Banido';
    IF verificar_jogador IS NULL THEN
        RAISE EXCEPTION 'Jogador invalido.';
    end if;

    SELECT referencia INTO verificar_jogo FROM jogo WHERE referencia = referencia_jogo;
    IF verificar_jogo IS NULL THEN
        RAISE EXCEPTION 'Jogo não existe.';
    end if;

    SELECT nome INTO verificar_cracha FROM cracha WHERE nome = nome_cracha;
    IF verificar_cracha IS NULL THEN
        RAISE EXCEPTION 'Cracha não existe.';
    end if;

    SELECT pontos INTO pontos_jogador FROM pontosJogoPorJogador(referencia_jogo) WHERE jogador = id_jogador;

    SELECT lim_pontos INTO pontos_necessarios FROM CRACHA WHERE nome = nome_cracha AND jogo = referencia_jogo;

    IF pontos_jogador >= pontos_necessarios THEN
        INSERT INTO JOGADOR_CRACHA VALUES(nome_cracha, referencia_jogo, id_jogador);
    ELSE
        RAISE EXCEPTION 'O jogador com id % não tem pontos suficientes para o cracha %.', id_jogador, nome_cracha;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE associarCracha(id_jogador INT, referencia_jogo VARCHAR(10), nome_cracha VARCHAR(50)) AS $$
DECLARE
    code CHAR(5) DEFAULT '00000';
    msg TEXT;
BEGIN
    ROLLBACK;
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    BEGIN
        CALL associarCrachaLogica(id_jogador, referencia_jogo, nome_cracha);
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Ocorreu algum erro';
            GET stacked DIAGNOSTICS
                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
            RAISE NOTICE 'code=%, msg=%',code,msg;
            ROLLBACK;
    END;
END;
$$ LANGUAGE plpgsql;

-------------------------------------------------------------------------------------------
---------------------------------------- ALÍNEA I -----------------------------------------
-------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE iniciarConversaLogica(id_jogador INT, nome_conversa VARCHAR(50), INOUT idConversa INT) AS $$
DECLARE
    mensagem_inicial VARCHAR(300) := 'Juntou-se à conversa ' || nome_conversa || '.';
    id_conversa INT;
    verificar_jogador INT;
    estado_jogador VARCHAR(20);
BEGIN
    SELECT id INTO verificar_jogador FROM JOGADOR WHERE id = id_jogador;

    IF verificar_jogador IS NULL THEN
        RAISE EXCEPTION 'Jogador com id % não existe', id_jogador;
    END IF;

    SELECT estado INTO estado_jogador FROM JOGADOR WHERE id = id_jogador;

    IF estado_jogador = 'Banido' THEN
        RAISE EXCEPTION 'Jogador com id % encontra-se banido', id_jogador;
    END IF;

    INSERT INTO CONVERSA VALUES(DEFAULT, nome_conversa) RETURNING id INTO id_conversa;
    INSERT INTO CONVERSA_PARTICIPANTES VALUES(id_conversa, id_jogador);
    INSERT INTO MENSAGEM VALUES(DEFAULT, NOW(), mensagem_inicial, id_conversa, id_jogador);
    idConversa = id_conversa;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE iniciarConversa(id_jogador INT, nome_conversa VARCHAR(50), INOUT idConversa INT) AS $$
DECLARE
    code CHAR(5) DEFAULT '00000';
    msg TEXT;
BEGIN
    ROLLBACK;
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    BEGIN
        CALL iniciarConversaLogica(id_jogador, nome_conversa, idConversa);
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Ocorreu algum erro';
            GET stacked DIAGNOSTICS
                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
            RAISE NOTICE 'code=%, msg=%',code,msg;
            ROLLBACK;
    END;
END;
$$ LANGUAGE plpgsql;

-- Just to have out parameter
CREATE OR REPLACE FUNCTION iniciarConversa(id_jogador INT, nome_conversa VARCHAR(50)) RETURNS INT AS $$
DECLARE idConversa INT;
BEGIN
    CALL iniciarConversaLogica(id_jogador, nome_conversa, idConversa);
    RETURN idConversa;
END;
$$ LANGUAGE plpgsql;

-------------------------------------------------------------------------------------------
---------------------------------------- ALÍNEA J -----------------------------------------
-------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE juntarConversaLogica(id_jogador INT, id_conversa INT) AS $$
DECLARE
    mensagem_inicial VARCHAR(300) := 'Juntou-se à conversa ';
    nome_conversa VARCHAR(50);
    verificar_jogador INT;
    estado_jogador VARCHAR(20);
    verificar_conversa INT;
BEGIN
    SELECT id INTO verificar_jogador FROM JOGADOR WHERE id = id_jogador;
    IF verificar_jogador IS NULL THEN
        RAISE EXCEPTION 'Jogador com id % não existe', id_jogador;
    END IF;

    SELECT estado INTO estado_jogador FROM JOGADOR WHERE id = id_jogador;
    IF estado_jogador = 'Banido' THEN
        RAISE EXCEPTION 'Jogador com id % encontra-se banido', id_jogador;
    END IF;

    SELECT id INTO verificar_conversa FROM CONVERSA WHERE id = id_conversa;
    IF verificar_conversa IS NULL THEN
        RAISE EXCEPTION 'Conversa com id % não existe', id_conversa;
    END IF;

    SELECT id INTO verificar_conversa FROM conversa INNER JOIN conversa_participantes cp on conversa.id = cp.conversa where jogador = id_jogador AND cp.conversa = id_conversa;
    IF verificar_conversa IS NOT NULL THEN
        RAISE EXCEPTION 'Jogador com id % já se encontra na conversa %', id_jogador, id_conversa;
    END IF;

    SELECT nome INTO nome_conversa FROM CONVERSA WHERE id = id_conversa;
    INSERT INTO CONVERSA_PARTICIPANTES VALUES(id_conversa, id_jogador);
    INSERT INTO MENSAGEM VALUES(DEFAULT, NOW(), mensagem_inicial || nome_conversa || '.', id_conversa, id_jogador);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE juntarConversa(id_jogador INT, id_conversa INT) AS $$
DECLARE
    code CHAR(5) DEFAULT '00000';
    msg TEXT;
BEGIN
    ROLLBACK;
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    BEGIN
        CALL juntarConversaLogica(id_jogador, id_conversa);
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Ocorreu algum erro';
            GET stacked DIAGNOSTICS
                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
            RAISE NOTICE 'code=%, msg=%',code,msg;
            ROLLBACK;
    END;
END;
$$ LANGUAGE plpgsql;


-------------------------------------------------------------------------------------------
---------------------------------------- ALÍNEA K -----------------------------------------
-------------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE enviarMensagemLogica(id_jogador INT, id_conversa INT, conteudo_mensagem VARCHAR(300)) AS $$
DECLARE
    verificar_jogador INT;
    estado_jogador VARCHAR(20);
    verificar_conversa INT;
BEGIN
    SELECT id INTO verificar_jogador FROM JOGADOR WHERE id = id_jogador;
    IF verificar_jogador IS NULL THEN
        RAISE EXCEPTION 'Jogador com id % não existe', id_jogador;
    END IF;

    SELECT estado INTO estado_jogador FROM JOGADOR WHERE id = id_jogador;
    IF estado_jogador = 'Banido' THEN
        RAISE EXCEPTION 'Jogador com id % encontra-se banido', id_jogador;
    END IF;

    SELECT id INTO verificar_conversa FROM CONVERSA WHERE id = id_conversa;
    IF verificar_conversa IS NULL THEN
        RAISE EXCEPTION 'Conversa com id % não existe', id_conversa;
    END IF;

    SELECT id INTO verificar_conversa FROM conversa INNER JOIN conversa_participantes cp on conversa.id = cp.conversa where jogador = id_jogador AND cp.conversa = id_conversa;
    IF verificar_conversa IS NULL THEN
        INSERT INTO CONVERSA_PARTICIPANTES VALUES(id_conversa, id_jogador);
    END IF;

    INSERT INTO MENSAGEM VALUES(DEFAULT, NOW(), conteudo_mensagem, id_conversa, id_jogador);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE enviarMensagem(id_jogador INT, id_conversa INT, conteudo_mensagem VARCHAR(300)) AS $$
DECLARE
    code CHAR(5) DEFAULT '00000';
    msg TEXT;
BEGIN
    ROLLBACK;
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    BEGIN
        CALL enviarMensagemLogica(id_jogador, id_conversa, conteudo_mensagem);
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Ocorreu algum erro';
            GET stacked DIAGNOSTICS
                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
            RAISE NOTICE 'code=%, msg=%',code,msg;
            ROLLBACK;
    END;
END;
$$ LANGUAGE plpgsql;

-------------------------------------------------------------------------------------------
---------------------------------------- ALÍNEA M -----------------------------------------
-------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION validarCrachas() RETURNS TRIGGER AS $$
DECLARE
    player INT;
    nome_cracha VARCHAR(50);

    crachas CURSOR FOR
        SELECT nome
        FROM cracha
        WHERE jogo = new.jogo;

    jogadores CURSOR FOR
        SELECT jogador
        FROM partida
                 INNER JOIN jogador_partida_multi_jogador on partida.id = jogador_partida_multi_jogador.partida and partida.jogo = jogador_partida_multi_jogador.jogo
        WHERE id = new.id and partida.jogo = new.jogo;
BEGIN
    SELECT jogador INTO player FROM partida INNER JOIN partida_normal pn on partida.id = pn.partida and partida.jogo = pn.jogo WHERE pn.partida = new.id AND pn.jogo = new.jogo;
    IF (player) IS NOT NULL THEN
        OPEN crachas;
        FETCH NEXT FROM crachas INTO nome_cracha;
        WHILE (FOUND) LOOP
                BEGIN
                    CALL associarCrachaLogica(player, new.jogo, nome_cracha);
                EXCEPTION
                    WHEN SQLSTATE 'P0001' THEN
                        RAISE NOTICE 'Jogador % tem pontos insuficientes.', player;
                END;
                FETCH NEXT FROM crachas INTO nome_cracha;
            END LOOP ;
        CLOSE crachas;
    ELSE
        OPEN jogadores;
        FETCH NEXT FROM jogadores INTO player;
        WHILE (FOUND) LOOP
                OPEN crachas;
                FETCH NEXT FROM crachas INTO nome_cracha;
                WHILE (FOUND) LOOP
                        BEGIN
                            CALL associarCrachaLogica(player, new.jogo, nome_cracha);
                        EXCEPTION
                            WHEN SQLSTATE 'P0001' THEN
                                RAISE NOTICE 'Jogador % tem pontos insuficientes.', player;
                        END;
                        FETCH NEXT FROM crachas INTO nome_cracha;
                    END LOOP ;
                CLOSE crachas;
                FETCH NEXT FROM jogadores INTO player;
            END LOOP ;
        CLOSE jogadores;
    END IF;
    RETURN NEW;
END; $$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER partida_termina
    AFTER UPDATE OF tempo_fim ON PARTIDA
    FOR EACH ROW
    WHEN (OLD.tempo_fim IS NULL AND NEW.tempo_fim IS NOT NULL)
EXECUTE FUNCTION validarCrachas();

-------------------------------------------------------------------------------------------
---------------------------------------- ALÍNEA N -----------------------------------------
-------------------------------------------------------------------------------------------

/* TESTAR */

CREATE OR REPLACE FUNCTION banirView() RETURNS TRIGGER AS $$
BEGIN
    CALL banirJogadorLogica(old.id);
    RETURN NEW;
END; $$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER apagar_jogador_view
    INSTEAD OF DELETE on jogadorTotalInfo
    FOR EACH ROW
EXECUTE FUNCTION banirView();
