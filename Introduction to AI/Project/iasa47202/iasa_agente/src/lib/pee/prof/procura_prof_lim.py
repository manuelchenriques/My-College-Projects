


## Procura em profundidade limitada. Efectua procura em profundidade somente até uma certa profundidades establecida.
from mod.problema.problema import Problema
from pee.mec_proc.no import No
from pee.prof.procura_profundiade import ProcuraProfundidade


class ProcuraProfLim(ProcuraProfundidade):
    
    ## Iniciada com o limite da profundiade
    def __init__(self, prof_max: int = 100) -> None:
        self.__prof_max = prof_max
        super().__init__()
        
    ## Efectua a expansão de um nó, mas só se o nó não se encontrar no limite ou além da profundidade maxima
    def _expandir(self, problema: Problema, no: No) -> No:
        if no._profundidade < self.__prof_max:
            yield from super()._expandir(problema, no)
    
    ## Memoriza um nó, mas apenas se esse nó não for o inicio de um ciclo
    def _memorizar(self, no: No) -> None:
        if not self._ciclo(no):
            return super()._memorizar(no)
    
    ## Verifica os antecedentes do nó para ve se este nó já aparece. 
    ## Se já apareceu, e tratando-se isto de uma pesquisa em profundidade, significa que vai ocorrer um ciclo
    def _ciclo(self, no: No) ->bool:
        targetNo = no.antecessor
        while(targetNo):
            if no.estado.__eq__(targetNo.estado): return True
            targetNo = targetNo.antecessor
        return False
    
    ## Getter da propriedade que define a profundidade maxima
    @property
    def prof_max(self) -> int:
        return self.__prof_max

    ## Setter da propriedade que define a profundidade maxima
    @prof_max.setter
    def prof_max(self, valor: int):
        self.__prof_max = valor