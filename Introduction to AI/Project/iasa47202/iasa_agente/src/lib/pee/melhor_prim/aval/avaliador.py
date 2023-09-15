
from abc import ABC, abstractmethod
from lib.pee.mec_proc.no import No

class Avaliador(ABC):
    
    ## Recebe um nó e averigua a sua prioridade    
    @abstractmethod
    def prioridade(self, no: No):
        pass