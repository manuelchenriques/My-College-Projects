

from pee.melhor_prim.procura_aa import ProcuraAA
from plan.plan_pee.calcular_heur import CalcularHeur
from plan.plan_pee.mod_prob.problema_plan import ProblemaPlan
from plan.plan_pee.planeador_pee import PlaneadorPEE
from plan.plan_pee.plano_pee import PlanoPEE
from plan.plano import Plano


class PlaneadorHeurDinamica:
    
    def __init__(self, heur_seletor: CalcularHeur) -> None:
        self.__heur_seletor = heur_seletor
        self.__mec_pee = ProcuraAA()
        
    def planear(self, modelo_plan, objectivos) -> Plano:
        if len(objectivos) > 0:
            objectivo_final = objectivos[0]
            solucao = self.__mec_pee.procurar(
                ProblemaPlan(modelo_plan, objectivo_final),
                self.__heur_seletor.heuristica(objectivo_final)
            )
            return PlanoPEE(solucao)