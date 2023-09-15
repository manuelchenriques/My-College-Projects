
from iasa_agente.src.controlo_react.reacoes.evitar.evitar_dir import EvitarDir
from iasa_agente.src.controlo_react.reacoes.evitar.resposta.resposta_evitar import RespostaEvitar
from iasa_agente.src.lib.ecr.hierarquia import Hierarquia
from iasa_agente.src.lib.sae.ambiente.direccao import Direccao

#Extensão de Hierarquia que representa um Comportamento Composto de desvio de uma obstaculo
class EvitarObst(Hierarquia):
    
    ##Lista de comportamentos de desvio de obstaculo em todas as direccoes
    def __init__(self):
        comportamentos = [
           EvitarDir(Direccao.NORTE, RespostaEvitar(Direccao.NORTE)),
           EvitarDir(Direccao.SUL, RespostaEvitar(Direccao.SUL)),
           EvitarDir(Direccao.ESTE, RespostaEvitar(Direccao.ESTE)),
           EvitarDir(Direccao.OESTE, RespostaEvitar(Direccao.OESTE))
           ]
        super().__init__(comportamentos)