
from iasa_agente.src.controlo_react.reacoes.aproximar.estimulo.estimulo_alvo import EstimuloAlvo
from iasa_agente.src.controlo_react.reacoes.resposta.resposta_mover import RespostaMover
from iasa_agente.src.lib.ecr.reaccao import Reaccao
from lib.sae.agente.accao import Accao
from lib.sae.ambiente.direccao import Direccao

## Reacção de aproximação em uma direcção
class AproximarDir(Reaccao):
    
    ## Recebe um EstimuloAlvo e um RespostaMover
    def __init__(self, direccao):
        super().__init__(EstimuloAlvo(direccao), RespostaMover(direccao))