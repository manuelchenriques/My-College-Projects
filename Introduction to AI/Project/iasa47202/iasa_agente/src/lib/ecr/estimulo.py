from abc import ABC, abstractmethod

from lib.sae.agente.percepcao import Percepcao


## Representa um estimulo ao agente
class Estimulo(ABC):
    
    ## Usando a percepção, deteta o estimulo e retorna a itensidade
    @abstractmethod
    def detectar(self, percepcao: Percepcao) -> float:
        pass
    