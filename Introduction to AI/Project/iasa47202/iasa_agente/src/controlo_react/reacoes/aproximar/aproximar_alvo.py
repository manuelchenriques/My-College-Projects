
from iasa_agente.src.controlo_react.reacoes.aproximar.aproximar_dir import AproximarDir
from iasa_agente.src.controlo_react.reacoes.aproximar.estimulo.estimulo_alvo import EstimuloAlvo
from iasa_agente.src.controlo_react.reacoes.resposta.resposta_mover import RespostaMover
from iasa_agente.src.lib.ecr.prioridade import Prioridade
from iasa_agente.src.lib.sae.ambiente.direccao import Direccao


#Extensão de Prioridade que representa um Comportamento Composto de aproximação a um alvo
class AproximarAlvo(Prioridade):
    
    ##Lista de comportamentos de aproximação a alvo em todas as direccoes
    def __init__(self):
        comportamentos = [
            AproximarDir(Direccao.NORTE),
            AproximarDir(Direccao.SUL),
            AproximarDir(Direccao.ESTE),
            AproximarDir(Direccao.OESTE)
            ]
        super().__init__(comportamentos)