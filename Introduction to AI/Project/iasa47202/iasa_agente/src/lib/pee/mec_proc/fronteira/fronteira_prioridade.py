
from heapq import heappop, heappush
from pee.mec_proc.fronteira.fronteira import Fronteira
from pee.mec_proc.no import No
from pee.melhor_prim.aval.avaliador import Avaliador

## Fronteira que realiza a inserção e remoção com base na prioridade de cada nó.
## A prioridade é obtida atraves do avaliador passado no constructor
class FronteiraPrioridade(Fronteira):
    
    ## Inicializado com um avaliador
    def __init__(self, avaliador: Avaliador) -> None:
        self._avaliador = avaliador
        super().__init__()
        
    ##Insere os nós em um heap, juntamente com a sua prioridade
    def inserir(self, no: No):
        heappush(self._nos, (self._avaliador.prioridade(no), no))
        
    ## Remove tuplo do heap e retorna o no
    def remover(self) -> No:
        return heappop(self._nos)[1]