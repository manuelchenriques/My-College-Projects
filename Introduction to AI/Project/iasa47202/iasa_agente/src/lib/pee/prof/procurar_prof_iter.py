

## Procura em profunfidade iterativa
from mod.problema.problema import Problema
from pee.mec_proc.solucao import Solucao
from pee.prof.procura_prof_lim import ProcuraProfLim


class ProcurarProfIter(ProcuraProfLim):
    
    ##Efectua a procura em profundidade limitida de maneira iterativa, indo desde a profundidade inicial(profundidade 0)
    ##até à profundidade limite. Só retorna caso encontre uma solução, ou caso não exista solução
    def procurar(self, problema: Problema, inc_prof: int = 1, limite_prof: int = 100) -> Solucao:
        procura = ProcuraProfLim(limite_prof)
        for prof in range(0, limite_prof, inc_prof):
            procura.prof_max = prof
            solucao = procura.procurar(problema)
            if solucao: return solucao
        