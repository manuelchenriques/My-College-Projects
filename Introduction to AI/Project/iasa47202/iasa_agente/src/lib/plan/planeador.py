
from abc import ABC, abstractmethod
from plan.modelo.modelo_plan import ModeloPlan

from plan.plano import Plano

##Interface de um planeador de percurso
class Planeador(ABC):
    
    ##Planear um percurso para um objectivo, fornecidos o modelo de planeamento e lista de objectivos
    @abstractmethod
    def planear(modelo_plan: ModeloPlan, objectivos) -> Plano:
        pass