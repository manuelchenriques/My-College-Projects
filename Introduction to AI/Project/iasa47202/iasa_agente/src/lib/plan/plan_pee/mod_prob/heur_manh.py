
from mod.estado import Estado
from pee.melhor_prim.heuristica import Heuristica


class HeurManh(Heuristica):
    
    def __init__(self, estado_final) -> None:
        self.__estado_final = estado_final
    
    def h(self, estado: Estado):
        x1, y1 = estado.posicao
        x2, y2 = self.__estado_final.posicao
        
        return abs(x2-x1) + abs(y2-y1)