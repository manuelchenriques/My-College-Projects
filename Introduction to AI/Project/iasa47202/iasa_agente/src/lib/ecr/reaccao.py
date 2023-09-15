
from iasa_agente.src.lib.ecr.comportamento import Comportamento
from iasa_agente.src.lib.ecr.estimulo import Estimulo
from iasa_agente.src.lib.ecr.resposta import Resposta
from lib.sae.agente.accao import Accao

## Represente a reacção do agente a uma percepção
class Reaccao(Comportamento):
    
    ## É inicializado com um estimulo e resposta resultantes da percepção
    def __init__(self, estimulo: Estimulo, resposta: Resposta):
        self._estimulo = estimulo
        self._resposta = resposta
    
    ## Detecta intensidade do estimulo e, se for maior que nulo, activa a acção da resposta
    def activar(self, percepcao)-> Accao:
        intensidade = self._estimulo.detectar(percepcao)
        if intensidade  > 0:
            accao = self._resposta.activar(percepcao, intensidade)
            return accao
        