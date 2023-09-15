


from mod.estado import Estado
from mod.operador import Operador
from deposito.mod_prob.estado_balde import EstadoBalde

## Define um operador de transferencia de água, que é definido pelo volume da transferencia
## que efectua, e o custo, que é (volume da transferencia)²
class OperadorTransferirAgua(Operador):
    
    def __init__(self, volume) -> None:
        self.__volume = volume
        self.__custo = self.__volume**2

        super().__init__()
   
   ## Aplica a transferencia de agua ao estado pretendido e retorna um novo estado com
   ## o volume actualizado. Caso a transferencia fosse resultar em um volume negativo,
   ## retorna um estado com volume 0
    def aplicar(self, estado: Estado) -> Estado:
        novo_volume = estado.volume + self.__volume
        if novo_volume < 0: novo_volume = 0
        return EstadoBalde(novo_volume)
        
    ## Retorna o custo da transferencia
    def custo(self, estado: Estado, estado_suc: Estado) -> float:
        return self.__custo
    
    ## Permite uma melhor visualização da classe da classe em caso de print
    def __repr__(self) -> str:
        return "Encheu("+ str(self.__volume) + ")"