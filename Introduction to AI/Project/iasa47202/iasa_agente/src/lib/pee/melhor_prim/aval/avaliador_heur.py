


from pee.melhor_prim.aval.avaliador import Avaliador
from pee.melhor_prim.heuristica import Heuristica


class AvaliadorHeur(Avaliador):
    
    def definir_heuristica(self, heuristica: Heuristica):
        self.heuristica = heuristica