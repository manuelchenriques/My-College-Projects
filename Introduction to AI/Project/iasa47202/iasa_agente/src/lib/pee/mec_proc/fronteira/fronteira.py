
from abc import ABC, abstractmethod

from pee.mec_proc.no import No

## Fronteira de exploração
class Fronteira(ABC):
    
    ## Inicia-se como vazia e inicia os listos
    def __init__(self) -> None:
        self._vazia = True
        self._nos = []
    
    ## Inicia a lista de nós, de forma a ter a certeza que estava vazia
    def iniciar(self) -> None:
        self._nos = []
    
    @abstractmethod
    def inserir(self, no: No):
        pass

    ## Remover um nó da lista de nós
    def remover(self) -> No:
        return self._nos.pop(0)

    ## Propriedade que ve se a a lista de nós está vazia
    @property
    def vazia(self) -> bool:
        return len(self._nos) == 0