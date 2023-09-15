
from abc import ABC, abstractmethod


class CalcularHeur(ABC):
    
    def __init__(self, nome) -> None:
        self.nome = nome
        print("Nome heuristica:" + str(nome))
    
    @abstractmethod
    def heuristica(self, estado_final):
        pass
