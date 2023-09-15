
from lib.mod.problema.problema import Problema
from lib.pee.mec_proc.solucao import Solucao
from lib.pee.melhor_prim.heuristica import Heuristica
from lib.pee.melhor_prim.procura_melhor_prim import ProcuraMelhorPrim


class ProcuraInformada(ProcuraMelhorPrim):
    
    def procurar(self, problema: Problema, heuristica: Heuristica) -> Solucao:
        self._heuristica = heuristica
        self._avaliador.definir_heuristica(heuristica)
        return super().procurar(problema)