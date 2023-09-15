from mod.estado import Estado
from plan.plan_pee.calcular_heur import CalcularHeur
from plan.plan_pee.mod_prob.heur_manh import HeurManh


class CalcularManh(CalcularHeur):
    
    def __init__(self):
        super().__init__("Heuristica de Distancia")
        
    def heuristica(self, estado_final):
        return HeurManh(estado_final)