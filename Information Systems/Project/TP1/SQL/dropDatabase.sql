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

