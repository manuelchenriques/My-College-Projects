from plan.plan_pee.calcular_heur import CalcularHeur
from plan.plan_pee.mod_prob.heur_dist import HeurDist


class CalcularDist(CalcularHeur):
    
    def __init__(self):
        super().__init__("Heuristica de Distancia")
        
    def heuristica(self, estado_final):
        return HeurDist(estado_final)