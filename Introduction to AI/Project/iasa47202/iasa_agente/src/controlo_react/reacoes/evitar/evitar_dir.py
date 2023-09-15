
from iasa_agente.src.controlo_react.reacoes.evitar.estimulo.estimulo_obst import EstimuloObst
from iasa_agente.src.controlo_react.reacoes.evitar.resposta.resposta_evitar import RespostaEvitar
from iasa_agente.src.lib.ecr.reaccao import Reaccao
from iasa_agente.src.lib.sae.ambiente.direccao import Direccao

## Reacção de desvio de uma direcção
class EvitarDir(Reaccao):
    
    ## Recebe um EstimuloObst e um RespostaEvitar
    def __init__(self, direccao: Direccao, resposta: RespostaEvitar):
        super().__init__(EstimuloObst(direccao), resposta)