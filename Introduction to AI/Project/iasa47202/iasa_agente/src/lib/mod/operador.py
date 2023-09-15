
from abc import ABC, abstractmethod

from mod.estado import Estado


## Classe que efectua operações sobre estados
class Operador(ABC):
    
    ## Recebe um estado e gera um outro estado
    @abstractmethod
    def aplicar(self, estado: Estado) -> Estado:
        pass
    
    ## Define o custo da mudança de um estado
    @abstractmethod
    def custo (self, estado: Estado, estado_suc: Estado) -> float:
        pass