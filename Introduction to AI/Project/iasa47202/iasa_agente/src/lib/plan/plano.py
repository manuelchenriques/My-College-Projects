
from abc import ABC, abstractmethod
from mod.estado import Estado
from mod.operador import Operador
from sae.vistas.vista_amb import VistaAmb

## Representa um plano com um percurso até ao no final
class Plano(ABC):
    
    @abstractmethod
    def obter_accao(self, estado: Estado) -> Operador:
        pass

    @abstractmethod
    def mostrar(self, vista: VistaAmb ):
        pass