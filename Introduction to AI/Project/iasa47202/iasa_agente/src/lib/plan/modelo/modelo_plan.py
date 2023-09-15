
from abc import ABC, abstractmethod

from mod.estado import Estado

## Interface de um Modelo de planeamento
class ModeloPlan(ABC):
    
    ## Retorna os estado actual do agente no modelo
    @abstractmethod
    def obter_estado() -> Estado:
        pass
    
    ## Retorna a lista de estados do modelo
    @abstractmethod
    def obter_estados():
        pass
    
    ## Retorna a lista de operadores do modelo
    @abstractmethod
    def obter_operadores():
        pass