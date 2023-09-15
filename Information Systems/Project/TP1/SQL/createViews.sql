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

