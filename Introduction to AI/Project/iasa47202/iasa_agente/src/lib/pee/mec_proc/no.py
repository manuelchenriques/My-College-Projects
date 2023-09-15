

## Representa um nó da arvore de busca. Possui um estado e operador, assim
## como uma referencia ao seu antecessor, caso tenha
from mod.estado import Estado
from mod.operador import Operador


class No:
    
    ## Caso tenha antecessor, a profundidade é equivalente ao incremento da profundidade
    ## do antecessor e o custo ao custo do antecessor mais o seu custo
    def __init__(self, estado: Estado, operador: Operador = None, antecessor = None) -> None:
        self._estado = estado
        self._operador = operador
        self._antecessor = antecessor
        
        if(antecessor):
            self._profundidade = antecessor._profundidade + 1
            self._custo = antecessor.custo + operador.custo(antecessor.estado, self.estado)
        else:
            self._profundidade = 0
            self._custo = 0
            
    ## Verifica se o custo é menor ao de outro nó
    def __lt__(self, no) -> bool:
        return self._custo < no.custo
    
    @property
    def profundidade(self) -> int:
        return self._profundidade

    @property
    def custo(self) -> int:
        return self._custo

    @property
    def antecessor(self):
        return self._antecessor
    
    @property
    def estado(self)-> Estado:
        return self._estado

    @property
    def operador(self) -> Operador:
        return self._operador
    
    
    