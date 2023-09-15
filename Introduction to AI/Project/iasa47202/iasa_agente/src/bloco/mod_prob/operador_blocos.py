from mod.estado import Estado
from mod.operador import Operador

##Classe abstrata da qual os outros operadores vão derivar
class OperadorBlocos(Operador):
    
    ##É inicializada com o index referente à operação
    def __init__(self, idx) -> None:
        self.__idx = idx
    
    def aplicar(self, pilha):
        pass
    
    ## Custo é igual ao numero da pilha-alvo do operador
    def custo(self, estado: Estado, estado_suc: Estado) -> float:
        return self.idx
    
    @property
    def idx(self):
        return self.__idx