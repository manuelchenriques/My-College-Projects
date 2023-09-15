begin transaction;

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


commit;
