
from abc import ABC, abstractmethod


## Classe representativa de uma Estado
class Estado(ABC):
    
    ## Define identificação de um estado
    @abstractmethod
    def id_valor(self) -> int:
        pass
    
    ## Define identificação do objecto
    def __hash__(self) -> int:
        return self.id_valor().__hash__()
    
    ## Verifica se estado é identico a um outro fornecido
    def __eq__(self, __value: object) -> bool:
        return self.__hash__() == __value.__hash__()