
from abc import ABC, abstractmethod

## Interface que modelo que contêm informações sobre o modelo em uso
class ModeloPDM(ABC):
    
    @abstractmethod
    def obter_estado():
        pass
    
    @abstractmethod
    def obter_estados():
        pass
    
    @abstractmethod
    def obter_acoes():
        pass
    
    @abstractmethod
    def obter_recompensas():
        pass
    
    @abstractmethod
    def obter_transicoes():
        pass
    
    
    
    