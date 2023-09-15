
from mod.estado import Estado
from pee.melhor_prim.heuristica import Heuristica

## Heuristica de distancia
class HeurDist(Heuristica):
    
    ## Heuristica é inicializada com um estado final
    def __init__(self, estado_final) -> None:
        self.__estado_final = estado_final
        
    ## Calcula heuristica de distancia 
    def h(self, estado: Estado):
        x, y = estado.posicao
        xDestino, yDestino = self.__estado_final.posicao
        return abs(x - xDestino) + abs(y - yDestino)