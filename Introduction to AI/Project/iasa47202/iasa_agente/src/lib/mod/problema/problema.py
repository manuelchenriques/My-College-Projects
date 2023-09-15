

## Representação de um problema, constituida por um estado inicial
## e um conjunto de operadores
from mod.estado import Estado
from mod.operador import Operador


class Problema:
    
    def __init__(self, estado_inicial: Estado, operadores) -> None:
        self._estado_inicial = estado_inicial
        self._operadores = operadores
    
    ## Testa se o objectivo foi concluido
    def objectivo(self, estado: Estado) -> bool:
        pass

    @property
    def estado_inicial(self) -> Estado:
        return self._estado_inicial
    
    @property
    def operadores(self):
        return self._operadores