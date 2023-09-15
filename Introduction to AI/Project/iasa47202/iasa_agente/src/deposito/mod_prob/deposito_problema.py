

from deposito.mod_prob.estado_balde import EstadoBalde
from mod.estado import Estado
from mod.problema.problema import Problema
from deposito.mod_prob.agua_transferencia import OperadorTransferirAgua

## Define um problema de deposito de agua. É definido pelo volume inicial do balde e volume desejado, e define
## os operadores de transferencia 2, -2, 3, -3
class DepositoProblema(Problema):
    
    def __init__(self, estado_inicial: EstadoBalde, estado_final: EstadoBalde) -> None:
        self.__estado_final = estado_final
        operadores = [
            OperadorTransferirAgua(2),
            OperadorTransferirAgua(-2),
            OperadorTransferirAgua(+3),
            OperadorTransferirAgua(-3)     
        ]
        
        super().__init__(estado_inicial, operadores)
        
    ## Dado um estado, verifica se o estado fornecido resolve o problema
    def objectivo(self, estado: Estado) -> bool:
        return self.__estado_final.__eq__(estado)