
from mod.estado import Estado
from mod.problema.problema import Problema
from plan.modelo.modelo_plan import ModeloPlan

## Representa o problema de um planeamento
class ProblemaPlan(Problema):

    ## Incializado com o modelo de planeamento e o estado final desejado
    def __init__(self, modelo_plan: ModeloPlan, estado_final: Estado) -> None:
        self.__estado_final = estado_final
        super().__init__(modelo_plan.obter_estado(), modelo_plan.obter_operadores())
        
    ## Verifica se o estado fornecido corresponde ao estado final
    def objectivo(self, estado: Estado) -> bool:
        return estado.__eq__(self.__estado_final)