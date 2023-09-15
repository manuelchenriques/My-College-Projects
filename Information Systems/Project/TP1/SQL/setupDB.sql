BEGIN transaction;
	----------------------DELETE PREVIOUS MODEL---------------------
-----------------------------------------------------------------------------
----------------------            VIEWS            -------------------------
-----------------------------------------------------------------------------
DROP VIEW IF EXISTS jogadorTotalInfo;
DROP VIEW IF EXISTS JOGO_ESTATISTICAS;
DROP VIEW IF EXISTS JOGADOR_ESTATISTICAS;
-----------------------------------------------------------------------------
----------------------           TRIGGERS           -------------------------
-----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS partida_id_setter ON PARTIDA CASCADE;
DROP TRIGGER IF EXISTS apagar_jogador_view ON PARTIDA CASCADE;
DROP TRIGGER IF EXISTS partida_termina ON PARTIDA CASCADE;
DROP TRIGGER IF EXISTS insert_pontuacao_estatisticas ON PARTIDA CASCADE;
DROP TRIGGER IF EXISTS update_pontuacao_estatisticas ON PARTIDA CASCADE;
DROP TRIGGER IF EXISTS delete_pontuacao_estatisticas ON PARTIDA CASCADE;
DROP TRIGGER IF EXISTS insert_multi_pontuacao_estatisticas ON PARTIDA CASCADE;
DROP TRIGGER IF EXISTS update_multi_pontuacao_estatisticas ON PARTIDA CASCADE;
DROP TRIGGER IF EXISTS delete_multi_pontuacao_estatisticas ON PARTIDA CASCADE;

-----------------------------------------------------------------------------
----------------------           FUNCTIONS          -------------------------
-----------------------------------------------------------------------------
DROP FUNCTION IF EXISTS atualizarEstatisticas;
DROP FUNCTION IF EXISTS proximoId;
DROP FUNCTION IF EXISTS totalPontosJogador;
DROP FUNCTION IF EXISTS totalJogosJogador;
DROP FUNCTION IF EXISTS pontosJogoPorJogador;
DROP FUNCTION IF EXISTS validarCrachas;
DROP FUNCTION IF EXISTS banirView;

-----------------------------------------------------------------------------
----------------------          PROCEDURES          -------------------------
-----------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS criarJogadorLogica;
DROP PROCEDURE IF EXISTS desativarJogadorLogica;
DROP PROCEDURE IF EXISTS banirJogadorLogica;
DROP PROCEDURE IF EXISTS totalPontosJogadorLogica;
DROP PROCEDURE IF EXISTS totalJogosJogadorLogica;
DROP PROCEDURE IF EXISTS pontosJogoPorJogadorLogica;
DROP PROCEDURE IF EXISTS associarCrachaLogica;
DROP PROCEDURE IF EXISTS iniciarConversaLogica;
DROP PROCEDURE IF EXISTS juntarConversaLogica;
DROP PROCEDURE IF EXISTS enviarMensagemLogica;
DROP PROCEDURE IF EXISTS criarJogador;
DROP PROCEDURE IF EXISTS desativarJogador;
DROP PROCEDURE IF EXISTS banirJogador;
DROP PROCEDURE IF EXISTS associarCracha;
DROP PROCEDURE IF EXISTS iniciarConversa;
DROP PROCEDURE IF EXISTS juntarConversa;
DROP PROCEDURE IF EXISTS enviarMensagem;
DROP PROCEDURE IF EXISTS criarJogadorTeste;
DROP PROCEDURE IF EXISTS desativarJogadorTeste;
DROP PROCEDURE IF EXISTS banirJogadorTeste;
DROP PROCEDURE IF EXISTS totalPontosJogadorTeste;
DROP PROCEDURE IF EXISTS totalJogosJogadorTeste;
DROP PROCEDURE IF EXISTS pontosJogoPorJogadorTeste;
DROP PROCEDURE IF EXISTS associarcrachaTeste;
DROP PROCEDURE IF EXISTS iniciarconversaTeste;
DROP PROCEDURE IF EXISTS juntarconversaTeste;
DROP PROCEDURE IF EXISTS enviarmensagemTeste;
DROP PROCEDURE IF EXISTS validarCrachasTeste;
DROP PROCEDURE IF EXISTS banirViewTeste;

-----------------------------------------------------------------------------
----------------------            TABLES            -------------------------
-----------------------------------------------------------------------------
DROP TABLE IF EXISTS MENSAGEM;
DROP TABLE IF EXISTS CONVERSA_PARTICIPANTES;
DROP TABLE IF EXISTS CONVERSA;
DROP TABLE IF EXISTS AMIGOS;
DROP TABLE IF EXISTS JOGADOR_CRACHA;
DROP TABLE IF EXISTS CRACHA;
DROP TABLE IF EXISTS JOGADOR_PARTIDA_MULTI_JOGADOR;
DROP TABLE IF EXISTS PARTIDA_MULTI_JOGADOR;
DROP TABLE IF EXISTS PARTIDA_NORMAL;
DROP TABLE IF EXISTS PARTIDA;
DROP TABLE IF EXISTS COMPRAS;
DROP TABLE IF EXISTS JOGO;
DROP TABLE IF EXISTS JOGADOR;
DROP TABLE IF EXISTS REGIAO;


	--------------------------CREATE TABLES-------------------------
	--Regiao(nome)
	CREATE TABLE IF NOT EXISTS REGIAO (
		nome VARCHAR(2) NOT NULL CHECK (nome IN ('NA', 'SA', 'EU', 'AS', 'AF', 'OC')) PRIMARY KEY
	);

	--Jogador(id, email, username, estado, regiao)
	CREATE TABLE IF NOT EXISTS JOGADOR (
		id SERIAL PRIMARY KEY,
		email VARCHAR(250) NOT NULL UNIQUE,
		username VARCHAR(20) NOT NULL UNIQUE,
		estado VARCHAR(8) NOT NULL CHECK (estado IN ('Ativo', 'Inativo', 'Banido')),
		regiao VARCHAR(2) NOT NULL,
		FOREIGN KEY (regiao)
		REFERENCES REGIAO (nome) ON DELETE CASCADE ON UPDATE CASCADE
	);

	--Jogo(referencia, nome, url)
	CREATE TABLE IF NOT EXISTS JOGO (
		referencia VARCHAR(10) PRIMARY KEY,
		nome VARCHAR(25) NOT NULL UNIQUE,
		url VARCHAR(50)
	);

	--Compras(jogador, jogo, data_compra, preço)
	CREATE TABLE IF NOT EXISTS COMPRAS (
		jogador INT NOT NULL,
		jogo VARCHAR(10) NOT NULL,
		data_compra DATE NOT NULL,
		preco NUMERIC(5,2) NOT NULL,
		PRIMARY KEY (jogador, jogo),
		FOREIGN KEY (jogador)
		REFERENCES JOGADOR (id) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (jogo)
		REFERENCES JOGO (referencia) ON DELETE CASCADE ON UPDATE CASCADE
	);

	--Partida(id, jogo, regiao, tempo_inicio, tempo_fim)
	CREATE TABLE IF NOT EXISTS PARTIDA (
		id INT NOT NULL,
		jogo VARCHAR(10) NOT NULL,
		regiao VARCHAR(2) NOT NULL,
		tempo_inicio TIMESTAMP NOT NULL,
		tempo_fim TIMESTAMP,		--CAN BE NULL TO ALLOW GAMES STILL ONGOING
		PRIMARY KEY (id, jogo),
		FOREIGN KEY (jogo)
		REFERENCES JOGO (referencia) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (regiao)
		REFERENCES REGIAO (nome) ON DELETE CASCADE ON UPDATE CASCADE
	);

	--Partida_Normal(partida, jogo, jogador, dificuldade, pontuacao)
	CREATE TABLE IF NOT EXISTS PARTIDA_NORMAL(
		partida INT NOT NULL,
		jogo VARCHAR(10) NOT NULL,
		jogador INT NOT NULL,
		dificuldade SMALLINT NOT NULL CHECK (dificuldade BETWEEN 1 AND 5),
		pontuacao INT NOT NULL,
		PRIMARY KEY (partida, jogo),
		FOREIGN KEY (partida, jogo)
		REFERENCES PARTIDA (id, jogo) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (jogador)
		REFERENCES JOGADOR (id) ON DELETE CASCADE ON UPDATE CASCADE
	);

	--Partida_Multi_Jogador(partida, jogo, estado)
	CREATE TABLE IF NOT EXISTS PARTIDA_MULTI_JOGADOR(
		partida INT NOT NULL,
		jogo VARCHAR(10) NOT NULL,
		estado VARCHAR(20) NOT NULL CHECK (estado IN ('Por iniciar', 'A aguardar jogadores', 'Em curso', 'Terminada')),
		PRIMARY KEY (partida, jogo),
		FOREIGN KEY (partida, jogo)
        REFERENCES PARTIDA (id, jogo) ON DELETE CASCADE ON UPDATE CASCADE
	);

    --Jogador_Partida_MJ(partida, jogo, jogador, pontuacao)
	CREATE TABLE IF NOT EXISTS JOGADOR_PARTIDA_MULTI_JOGADOR(
		partida INT NOT NULL,
		jogo VARCHAR(10) NOT NULL,
		jogador INT NOT NULL,
		pontuacao INT NOT NULL DEFAULT 0,
		PRIMARY KEY (partida, jogo, jogador),
		FOREIGN KEY (partida, jogo)
        REFERENCES PARTIDA_MULTI_JOGADOR (partida, jogo) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (jogador)
		REFERENCES JOGADOR(id) ON DELETE CASCADE ON UPDATE CASCADE
	);

	--Cracha(nome, jogo, lim_pontos, url)
	CREATE TABLE IF NOT EXISTS CRACHA(
		nome VARCHAR(50) NOT NULL,
		jogo VARCHAR(10) NOT NULL,
		lim_pontos INT NOT NULL,
		url VARCHAR(200),
		PRIMARY KEY (nome, jogo),
		FOREIGN KEY (jogo)
		REFERENCES JOGO (referencia) ON DELETE CASCADE ON UPDATE CASCADE
	);

	--Jogador_Cracha(nome, jogo, jogador)
	CREATE TABLE IF NOT EXISTS JOGADOR_CRACHA(
		nome VARCHAR(50) NOT NULL,
		jogo VARCHAR(10) NOT NULL,
		jogador INT NOT NULL,
		PRIMARY KEY (nome, jogo, jogador),
		FOREIGN KEY (nome, jogo)
		REFERENCES CRACHA (nome, jogo) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (jogador)
		REFERENCES JOGADOR (id) ON DELETE CASCADE ON UPDATE CASCADE
	);

	--Amigos(jogador1, jogador2)
	CREATE TABLE IF NOT EXISTS AMIGOS(
		jogador1 INT,
		jogador2 INT,
		PRIMARY KEY (jogador1, jogador2),
		FOREIGN KEY (jogador1)
		REFERENCES JOGADOR (id) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (jogador2)
		REFERENCES JOGADOR (id) ON DELETE CASCADE ON UPDATE CASCADE
	);

	--Conversa(id, nome)
	CREATE TABLE IF NOT EXISTS CONVERSA(
		id SERIAL PRIMARY KEY,
		nome VARCHAR(50) NOT NULL
	);

	--Conversa_Participantes(conversa, jogador)
	CREATE TABLE IF NOT EXISTS CONVERSA_PARTICIPANTES(
		conversa INT,
		jogador INT,
		PRIMARY KEY (conversa, jogador),
		FOREIGN KEY (conversa)
		REFERENCES CONVERSA (id) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (jogador)
		REFERENCES JOGADOR (id) ON DELETE CASCADE ON UPDATE CASCADE
	);

	--Mensagem(n_ordem, data_envio, conteudo, conversa, jogador)
	CREATE TABLE IF NOT EXISTS MENSAGEM(
		n_ordem SERIAL NOT NULL,
		data_envio TIMESTAMP NOT NULL,
		conteudo VARCHAR(300) NOT NULL,
		conversa INT NOT NULL,
		jogador INT NOT NULL,
		PRIMARY KEY (n_ordem, conversa, jogador),
		FOREIGN KEY (conversa)
		REFERENCES CONVERSA (id) ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (jogador)
		REFERENCES JOGADOR (id) ON DELETE CASCADE ON UPDATE CASCADE
	);
	--------------------------CREATE VIEWS--------------------------
	
CREATE OR REPLACE VIEW jogadorTotalInfo as select jogador.id, jogador.estado, email, username,
	COUNT(DISTINCT partida.jogo) AS count_jogos_em_q_participou,
	(COUNT(DISTINCT partida_normal.partida) + COUNT(DISTINCT JOGADOR_PARTIDA_MULTI_JOGADOR.partida)) as count_partidas,
	--COALESCE is used to replace null values with to allow the addition to result in 0 and not null
	(COALESCE(SUM(partida_normal.pontuacao),0)+ COALESCE(SUM(JOGADOR_PARTIDA_MULTI_JOGADOR.pontuacao),0)) AS total_pontos
FROM jogador
LEFT JOIN compras ON jogador.id = compras.jogador
LEFT JOIN partida_normal ON jogador.id = partida_normal.jogador
LEFT JOIN JOGADOR_PARTIDA_MULTI_JOGADOR ON jogador.id = JOGADOR_PARTIDA_MULTI_JOGADOR.jogador
LEFT JOIN partida ON (partida_normal.partida = partida.id AND partida_normal.jogo = partida.jogo) OR
	(JOGADOR_PARTIDA_MULTI_JOGADOR.partida = partida.id AND JOGADOR_PARTIDA_MULTI_JOGADOR.jogo = partida.jogo)
WHERE jogador.estado != 'Banido'
group by jogador.id order by jogador.id;

CREATE OR REPLACE VIEW jogo_estatisticas AS
    SELECT j.referencia AS jogo_referencia, j.nome AS jogo_nome,
           COUNT(DISTINCT p.id) AS numero_partidas,
           COUNT(DISTINCT CASE WHEN pm.estado = 'Terminada' THEN jpm.jogador END) AS numero_jogadores,
           COALESCE(SUM(pn.pontuacao), 0) + COALESCE(SUM(jpm.pontuacao), 0) AS pontos
    FROM JOGO j
    LEFT JOIN PARTIDA p ON p.jogo = j.referencia
    LEFT JOIN PARTIDA_MULTI_JOGADOR pm ON pm.partida = p.id AND pm.jogo = p.jogo
	LEFT JOIN JOGADOR_PARTIDA_MULTI_JOGADOR jpm ON jpm.partida = pm.partida AND jpm.jogo = pm.jogo
    LEFT JOIN PARTIDA_NORMAL pn ON pn.partida = p.id AND pn.jogo = p.jogo
    GROUP BY j.referencia;

CREATE OR REPLACE VIEW jogador_estatisticas AS
    SELECT j.id AS jogador,
           COUNT(DISTINCT (p.id, p.jogo)) AS n_partidas,
           COUNT(DISTINCT c.jogo) AS n_jogos,
           COALESCE(SUM(pn.pontuacao), 0) + COALESCE(SUM(jpm.pontuacao), 0) AS pontos
    FROM JOGADOR j
    LEFT JOIN COMPRAS c ON C.jogador = j.id
    LEFT JOIN PARTIDA p ON p.jogo = c.jogo
    LEFT JOIN JOGADOR_PARTIDA_MULTI_JOGADOR jpm ON jpm.partida = p.id AND jpm.jogo = p.jogo
    LEFT JOIN PARTIDA_NORMAL pn ON pn.partida = p.id AND pn.jogo = p.jogo
    GROUP BY j.id;
	----------------------------FUNCTIONS---------------------------
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


----------------------------TRIGGER FUNCTIONS---------------------------
CREATE OR REPLACE FUNCTION proximoId() RETURNS TRIGGER AS $$
DECLARE
    max_id INT;
BEGIN
    select MAX(id) INTO max_id from partida where jogo = NEW.jogo;
    IF max_id IS NULL THEN
        NEW.id := 1;
    ELSE
        NEW.id := max_id+1;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

----------------------------TRIGGERS----------------------------
CREATE OR REPLACE TRIGGER partida_id_setter
    BEFORE INSERT ON PARTIDA
    FOR EACH ROW
        EXECUTE FUNCTION proximoId();



	---------------------------INSERT DATA--------------------------
	--Regiao(nome)
	INSERT INTO Regiao(nome)
	VALUES ('EU'),('NA'),('SA'),('AS'),('AF'),('OC');
	
	--Jogador(id, email, username, estado, regiao)
	INSERT INTO Jogador(email, username, estado, regiao)
	VALUES ('brunoaleixo@antena3.com', 'brunoaleixo', 'Ativo', 'EU'),				--01
	('renatoalexandre@antena3.com', 'renatoalexandre', 'Ativo', 'EU'),				--02
	('busto@antena3.com', 'busto', 'Ativo', 'EU'),									--03
	('bussaco@betclic.com', 'homembussaco', 'Ativo', 'EU'),							--04
	('dr.ribeiro@uncoimbra.com', 'dr.ribeiro', 'Ativo', 'EU'),						--05

	('daqui@ali.com', 'gajoqsimsr', 'Ativo', 'EU'),									--06
	('aminhavidadavaumfilmeindiano@ali.com', 'barbosa', 'Ativo', 'AS'),				--07
	
	('naoeueqsouopresidentedajunta@junta.com', 'presidentejunta', 'Ativo', 'EU'),	--08

	('omaiordaminhaaldeia@robiaes.com', 'maiordaminhaaldeia', 'Ativo', 'EU'),		--09
	('charles_coins@cmlisboa.com', 'charles_coins', 'Ativo', 'NA'),					--10
	('choninhascoins@cmlisboa.com', 'choninhas_moedas', 'Ativo', 'EU')				--11
	;
	
	--Jogo(referencia, nome, url)
	INSERT INTO Jogo(referencia, nome, url)
	VALUES ('AAAAA00001', 'DJ simulator', 'www.imgs/dj.jpg'),
	('AAAAA00002', '50 pratos', 'www.imgs/50pratoscookbook.jpg'),
	('AAAAA00003', 'jogo bue simbolico', 'www.imgs/buesimbolico.jpg'),
	('CAM0000001', 'cm simulator', 'www.imgs/cmsim.jpg'),
	('PARADOX001', 'Europa Universalis IV', 'www.euiv/mainimg.jpg'),
	('PARADOX002', 'Hearts of Iron IV', 'www.hoivmainimg.jpg'),
	('PARADOX003', 'Crusader Kings III', 'www.ck3mainimg.jpg'),
	('PARADOX004', 'Victoria III', 'www.victoria3mainimg.jpg');
	
	--Compras(jogador, jogo, data_compra, preço)
	INSERT INTO Compras(jogador, jogo, data_compra, preco)
	VALUES (2, 'AAAAA00001', '2020-01-10 19:30:00', 15.00),
	(1, 'AAAAA00002', '2020-01-10 19:30:00', 15.00),
	(2, 'AAAAA00002', '2020-01-10 19:30:00', 15.00),
	(3, 'AAAAA00002', '2020-01-10 19:30:00', 15.00),
	(4, 'AAAAA00001', '2020-01-10 19:30:00', 15.00),
	(5, 'AAAAA00001', '2020-01-10 19:30:00', 15.00),
	(2, 'AAAAA00003', '2020-01-10 16:20:00', 17.00),
	(9, 'CAM0000001', '2020-01-11 10:30:00', 25.00),
	(10, 'CAM0000001', '2020-01-11 11:20:00', 25.00),
	(6, 'PARADOX001', '2020-01-11 12:30:00', 50.00),
	(7, 'PARADOX001', '2021-02-15 12:30:00', 50.00),
	(1, 'PARADOX001', '2021-03-10 13:30:00', 50.00),
	(2, 'PARADOX001', '2022-01-13 19:30:00', 5.00),
	(3, 'PARADOX001', '2022-02-01 11:30:00', 5.00)
	;
	
	--Partida(jogo, regiao, tempo_inicio, tempo_fim)
	INSERT INTO Partida(jogo, regiao, tempo_inicio, tempo_fim)
	VALUES ('AAAAA00001', 'EU', '2023-02-07 11:00:00', '2023-04-17 11:40:00'),
	('AAAAA00002', 'EU', '2023-04-17 18:00:00', '2023-04-17 18:30:00'),
	('AAAAA00002', 'EU', '2023-04-16 23:00:00', '2023-04-17 01:40:00'),
	('AAAAA00003', 'EU', '2023-04-20 10:00:00', '2023-04-20 12:40:00'),
	('CAM0000001', 'EU', '2023-04-20 11:00:00', '2023-04-20 12:30:00'),
	('CAM0000001', 'EU', '2023-04-20 17:00:00', '2023-04-20 22:10:00'),
	('PARADOX001', 'EU', '2023-04-29 18:00:00', NULL),
	('PARADOX001', 'SA', '2023-04-29 18:30:00', NULL),
	('PARADOX001', 'EU', '2023-04-29 18:45:00', NULL)								--mp aleixo
	;
	
	--Partida_Normal(partida, jogo, jogador, dificuldade, pontuacao)
	INSERT INTO Partida_Normal(partida, jogo, jogador, dificuldade, pontuacao)
	VALUES (1, 'AAAAA00001', 2, 3, 500),
	(1, 'AAAAA00003', 2, 3, 500),
	(1, 'CAM0000001', 9, 2, 1000),
	(2, 'CAM0000001', 10, 1, 500),
	(1, 'PARADOX001', 7, 4, 5000),
	(2, 'PARADOX001', 6, 3, 1500)
	;
	
	--Partida_Multi_Jogador(partida, jogo, jogador, pontuacao, estado)
	INSERT INTO Partida_Multi_Jogador(partida, jogo, estado)
	VALUES (1, 'AAAAA00002', 'Terminada'),
	(2, 'AAAAA00002', 'Terminada'),
	(3, 'PARADOX001', 'Em curso')
	;

	--Jogador_Partida_Multi_Jogador(partida, jogo, jogador, pontuacao)
	INSERT INTO Jogador_Partida_Multi_Jogador(partida, jogo, jogador, pontuacao)
	VALUES (1, 'AAAAA00002', 1, 2500),
	(1, 'AAAAA00002', 2, 250),
	(1, 'AAAAA00002', 3, 350),
	(1, 'AAAAA00002', 4, 250),
	(2, 'AAAAA00002', 1, 1500),
	(2, 'AAAAA00002', 2, 500),
	(2, 'AAAAA00002', 3, 400),
	(2, 'AAAAA00002', 4, 250),
	(2, 'AAAAA00002', 5, 200),
	--mp aleixo eu4 ongoing
	(3, 'PARADOX001', 1, 2200),
	(3, 'PARADOX001', 2, 1500),
	(3, 'PARADOX001', 3, 2100)
	;
	
	--Cracha(nome, jogo, lim_pontos, url)
	INSERT INTO Cracha(nome, jogo, lim_pontos, url)
	VALUES ('You Stopped Racism', 'AAAAA00001', 2500, 'www.img_david_gueta_rooftop'),
	('Calhou ...', 'AAAAA00002', 1500, 'www.imgs/calhou'),
	('Calhou Arroz de Tomate com douradinhos', 'AAAAA00002', 2000, 'www.imgs/calhou_arroz')
	;
	
	--Jogador_Cracha(nome, jogo, jogador)
	--INSERT INTO Jogador_Cracha(nome, jogo, jogador)
	--VALUES ();
	
	--Jogador_Estatisticas(jogador, n_partidas, n_jogos, total_pontos)
	--INSERT INTO Jogador_Estatisticas(jogador, n_partidas, n_jogos, total_pontos)
	--VALUES ();
	
	--Jogo_Estatisticas(jogo, n_partidas, n_jogadores, pontos)
	--INSERT INTO Jogo_Estatisticas(jogo, n_partidas, n_jogadores, pontos)
	--VALUES ();
	
	--Amigos(jogador1, jogador2)
	INSERT INTO Amigos(jogador1, jogador2)
	VALUES (1, 2),
	(1, 3),
	(1, 4),
	(1, 5),
	(2, 3),
	(2, 4),
	(2, 5),
	(4, 5)
	;
	
	--Conversa(nome)
	INSERT INTO Conversa(nome)
	VALUES ('conv'),												--1
	('conv'),														--2
	('conv test');													--3
	
	--Conversa_Participantes(conversa, jogador)
	INSERT INTO Conversa_Participantes(conversa, jogador)
	VALUES (1, 1),
	(1, 2),
	(1, 3),
	(2, 1),
	(2, 2),
	(2, 3),
	(2, 4),
	(2, 5)
	;
	
	--Mensagem(data_envio, conteudo, conversa, jogador)
	INSERT INTO Mensagem(data_envio, conteudo, conversa, jogador)
	VALUES ('2023-04-17 17:45:00', 'Vamos testar o meu jogo?', 1, 1),
	('2023-04-17 17:45:20', 'Bora', 1, 2),
	('2023-04-17 17:53:00', '5m e entro', 1, 3),
	('2023-04-17 19:45:00', 'Bora Busto', 2, 1),
	('2023-04-17 19:50:00', 'Ja vou', 2, 3),
	('2023-04-17 19:50:30', 'Sempre a mesma coisa', 2, 1)
	;

commit transaction;
