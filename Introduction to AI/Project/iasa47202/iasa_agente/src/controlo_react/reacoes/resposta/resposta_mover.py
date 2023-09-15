

# Resposta que resulta numa deslocação para um direcçao fornecida
from iasa_agente.src.lib.ecr.resposta import Resposta
from iasa_agente.src.lib.sae.agente.percepcao import Percepcao
from iasa_agente.src.lib.sae.ambiente.direccao import Direccao
from lib.sae.agente.accao import Accao

class RespostaMover(Resposta):
    
    ## Gera uma acção para a direção fornecida
    def __init__(self, direccao: Direccao):
        accao = Accao(direccao)
        self.accao = accao
    
    ## Retorna a acção criada
    def activar(self, percepcao: Percepcao, intensidade: float) -> Accao:
        return self.accao