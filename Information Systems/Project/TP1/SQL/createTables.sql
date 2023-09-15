BEGIN transaction;

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

commit transaction;
