from lib.mod.estado import Estado

class EstadoAgente(Estado):
    
    def __init__(self, posicao) -> None:
        self.__posicao = posicao
        
    def id_valor(self) -> int:
        return hash(self.posicao)

    @property
    def posicao(self):
        return self.__posicao
    