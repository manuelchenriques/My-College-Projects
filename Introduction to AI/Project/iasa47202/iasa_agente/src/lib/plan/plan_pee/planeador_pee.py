
from pee.mec_proc.solucao import Solucao
from pee.melhor_prim.procura_aa import ProcuraAA
from plan.plan_pee.mod_prob.heur_dist import HeurDist
from plan.plan_pee.plano_pee import PlanoPEE
from plan.planeador import Planeador
from plan.plano import Plano
from pee.melhor_prim.procura_informada import ProcuraInformada
from plan.plan_pee.mod_prob.problema_plan import ProblemaPlan

## Representa um planeador que, dado um modelo e um objectivo, planeja um percurso para lá chegar
class PlaneadorPEE(Planeador):
    
    ## Inicializado com a ProcuraInformada
    def __init__(self) -> None:
        self.__mec_pee = ProcuraAA()
        
    ## Com o modelo de planeamento e uma lista de objectivos, retorna uma plano de percurso até ao primeiro objectivo.
    ## Caso não haja percurso possivel, retorna null
    def planear(self, modelo_plan, objectivos) -> Plano:
        if len(objectivos) > 0:
            objectivo_final = objectivos[0]
            solucao = self.__mec_pee.procurar(
                ProblemaPlan(modelo_plan, objectivo_final),
                HeurDist(objectivo_final)
            )
            return PlanoPEE(solucao)