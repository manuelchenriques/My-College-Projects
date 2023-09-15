


from mod.estado import Estado
from mod.operador import Operador
from mod_prob.estado_localidade import EstadoLocalidade

## Operador que representa a ligação entre dois estados. Possui um estado origem, um estado de destino e o custo da mudança de estado
class OperadorLigacao(Operador):
    
    def __init__(self, origem: str, destino: str, custo: input) -> None:
        self.__estado_origem = EstadoLocalidade(origem)
        self.__estado_destino = EstadoLocalidade(destino)
        self.__custo = custo
        
    ## Aplica a mudança de estado
    def aplicar(self, estado: Estado) -> Estado:
        if estado.__eq__(self.__estado_origem):
            return self.__estado_destino
    
    ## Retorna o valor da mudança de estado
    def custo(self, estado: Estado, estado_suc: Estado) -> float:
        if estado.__eq__(self.__estado_origem) and estado_suc.__eq__(self.__estado_destino):
            return self.__custo
        
    