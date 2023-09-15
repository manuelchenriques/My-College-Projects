from bloco.mod_prob.estado_blocos import EstadoBloco
from bloco.mod_prob.problema_blocos import ProblemaBloco
from mod.estado import Estado
from pee.melhor_prim.heuristica import Heuristica

## Heuristica para o problema das pilhas de blocos
class HeuristicaBlocos(Heuristica):
    
    ## A heuristica é calculada com a diferença de tamanho dos estados, assim como
    ## com o numero de elementos em comum.
    def h(self, pilha: EstadoBloco, estado_final: EstadoBloco):
        heur = 0
        estado_actual = pilha[0]
        estado_final = estado_final[0]
        heur = abs(len(estado_actual) - len(estado_final))
        
        for blocoActual in estado_actual:
            for blocoFinal in estado_final:
                if blocoActual != blocoFinal:
                    heur = heur + 1
        return heur