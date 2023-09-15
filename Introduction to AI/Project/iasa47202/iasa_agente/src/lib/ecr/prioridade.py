

from iasa_agente.src.lib.ecr.comport_comp import ComportComp
from sae.agente.accao import Accao


## Extensão de ComportaComp, onde a acção é selecionada com base na sua prioriade
class Prioridade(ComportComp):
    
    ## Ordena a lista de acções com base na sua prioridade e de seguida retorna a mais prioritaria
    def selecionar_accao(self, accoes: list[Accao]) -> Accao:
        return max(accoes, key=lambda x: x.prioridade)