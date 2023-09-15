from abc import ABC, abstractmethod

from sae.agente.accao import Accao
from sae.agente.percepcao import Percepcao

##Classe abstrata de comportamento
class Comportamento(ABC):
    
    @abstractmethod
    def activar(self, percepcao: Percepcao) -> Accao:
        pass