
from pdm.modelo.modelo_pdm import ModeloPDM
from plan.modelo.modelo_plan import ModeloPlan

## Representa o modelo de planeamento PDM. Possui os varios dados referentes ao modelo em uso
class ModeloPDMPlan(ModeloPDM):
    
    ## Inicializa uma dicionario objectivo com [estado, recompensa], com a lista de estados passada em objectivos
    ## Inicializa um dicionario transicoes com [(estado, operador), estado_sucessor], aplicando todos os operadores sobre
    ## todos os estados do modelo fornecido
    def __init__(self, modelo_plan: ModeloPlan, objectivos, rmax = 1000) -> None:
        self.__modelo_plan = modelo_plan
        self.__objectivos = dict()
        self.__transicoes = dict()
        for obj in objectivos:
            self.__objectivos[obj] = rmax
            
        operadores = self.__modelo_plan.obter_operadores()
        for estado in self.__modelo_plan.obter_estados():
            for op in operadores:
                estado_sucessor = op.aplicar(estado)
                if estado_sucessor:
                    transicao = (estado, op)
                    self.__transicoes[transicao] = estado_sucessor
                    
    ## retorna estado actual no modelo
    def obter_estado(self):
        return self.__modelo_plan.obter_estado()
    
    ## retorna lista de estados do modelo
    def obter_estados(self):
        return self.__modelo_plan.obter_estados()
    
    ## retorna lista de operadores do modelo
    def obter_acoes(self):
        return self.__modelo_plan.obter_operadores()
    
    ## retorna lista de recompensas criada na inicialização da classe
    def obter_recompensas(self):
        return self.__objectivos
    
    ## retorna lista de transicoes criada na inicialização da classe
    def obter_transicoes(self):
        return self.__transicoes