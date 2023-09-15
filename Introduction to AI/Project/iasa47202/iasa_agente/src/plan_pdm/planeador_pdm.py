
from pdm.pdm import PDM
from plan.modelo.modelo_plan import ModeloPlan
from plan.planeador import Planeador
from plan.plano import Plano
from plan_pdm.modelo.modelo_pdm_plan import ModeloPDMPlan
from plan_pdm.plano_pdm import PlanoPDM

## Planeador que utiliza Processo de Decisão de Markov para realizar plano
class PlaneadorPDM(Planeador):
    
    ## Inicializado com gamma e delta_max, vale notar que em modelos de maior dimensão é recomendado que se aumente o valor 
    ## (mas mantendo < 1) de gamma de modo a impedir, que a partir de uma certa distancia de uma recompensa, o nivel de utilidade
    ## se torne tão baixo que deixa de ter influencia sobre o processo de decisão
    def __init__(self, gamma = 0.85, delta_max = 1) -> None:
        self.__gamma = gamma
        self.__delta_max = delta_max
    
    ## Obtem a policia e utilidade do modelo fornecido e gera um plano
    def planear(self, modelo_plan: ModeloPlan, objectivos) -> Plano:
        if len(objectivos) != 0:
            modelo = ModeloPDMPlan(modelo_plan, objectivos)
            pdm = PDM(modelo, self.__gamma, self.__delta_max)
            utilidade, politica = pdm.resolver()
            return PlanoPDM(utilidade, politica)