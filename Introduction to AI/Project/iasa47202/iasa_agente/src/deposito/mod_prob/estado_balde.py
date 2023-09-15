

from mod.estado import Estado

## Define o estado do balde, que possui um volume
class EstadoBalde(Estado):
    
    def __init__(self, volume) -> None:
        self.__volume = volume
        
    def id_valor(self) -> int:
        return hash(self.__volume)
    
    #Definir como mostrar estado
    def __repr__(self) -> str:
        return str(self.__volume)
    
    @property
    def volume(self):
        return self.__volume