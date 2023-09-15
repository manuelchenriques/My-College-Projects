
from mod_prob.problema_plan_traj import ProblemaPlanTraj
from pee.larg.procura_largura import ProcuraLargura
from pee.mec_proc.procura_grafo import ProcuraGrafo
from pee.mec_proc.solucao import Solucao
from pee.melhor_prim.aval.avaliador_custo_unif import AvaliadorCustoUnif
from pee.melhor_prim.procura_custo_unif import ProcuraCustoUnif
from pee.melhor_prim.procura_melhor_prim import ProcuraMelhorPrim
from pee.prof.procura_prof_lim import ProcuraProfLim
from pee.prof.procura_profundiade import ProcuraProfundidade
from pee.prof.procurar_prof_iter import ProcurarProfIter
from plan_traj.planeador.ligacao import Ligacao

## Define o problema e efectua a procura de solução
class PlaneadorTrajecto:
    
    def planear(self, ligacoes: list[Ligacao], loc_inicial: str, loc_final: str)-> Solucao:
        problema = ProblemaPlanTraj(ligacoes,loc_inicial, loc_final)
        #procura = ProcuraProfLim()
        #procura = ProcuraProfundidade()
        procura = ProcuraLargura()
        #procura = ProcurarProfIter(2)
        #procura = ProcuraCustoUnif()
        #procura = ProcuraMelhorPrim(AvaliadorCustoUnif())
        return procura.procurar(problema)