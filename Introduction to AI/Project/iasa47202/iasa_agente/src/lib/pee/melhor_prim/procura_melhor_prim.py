
from pee.mec_proc.fronteira.fronteira_prioridade import FronteiraPrioridade
from pee.mec_proc.procura_grafo import ProcuraGrafo
from pee.melhor_prim.aval.avaliador import Avaliador

## Procura melhor primeiro
class ProcuraMelhorPrim(ProcuraGrafo):
    
    ## Inicializado com um avaliador e uma fronteira de prioridade.
    ## A prioridade vai definir qual o "melhor"
    def __init__(self, avaliador: Avaliador) -> None:
        self._avaliador = avaliador
        super().__init__(FronteiraPrioridade(self._avaliador))
    
    ## Mantem se ou já conhece o nó, ou se já conhece o no mas o caminho encontrado é mais eficiente
    def _manter(self, no):
        return super()._manter(no) or no.custo < self._explorados[no.estado].custo