from mod.estado import Estado
from mod.operador import Operador
from pdm.modelo.modelo_pdm import ModeloPDM

## Mecanismo Utilidade, responsavel por calcular a utilidade de todos os estados no modelo
class MecUtil:
    
    def __init__(self, modelo: ModeloPDM, gama, delta_max) -> None:
        self.__modelo = modelo
        self.__gama = gama
        self.__delta_max = delta_max
        self.__recompensas = modelo.obter_recompensas()
        self.__transicoes = modelo.obter_transicoes()
    
    ## Calcula a utilidade (double) de todos os estados presentes no modelo.
    ## Retorna um dicionario de (estado: Estado, utilidade: double) que contem todos os estados do modelo
    def utilidade(self):
        estados = self.__modelo.obter_estados()
        accoes = self.__modelo.obter_acoes()
        utilidades = dict((estado, 0.0) for estado in estados)
        
        while(True):
            u_ant = utilidades.copy()
            delta = 0
            
            for estado in estados:
               u_accao = []
               for op in accoes:
                   if (estado, op) in self.__transicoes: u_accao.append(self.util_accao(estado, op, u_ant))
               utilidades[estado] = max(u_accao)
               delta = max(delta, abs(utilidades[estado] - u_ant[estado]))      
            
            if delta <= self.__delta_max: break
        return utilidades
                       
    ## Calcula a utilidade de uma acção sobre um estado. A recompensa depende somente do estado, ou seja,
    ## são valorizadas as acções que como estado sucessor têm um estado com recompensa.
    def util_accao(self, estado: Estado, operador: Operador, utilidades):
        estado_sucessor = self.__transicoes[(estado, operador)]
        recompensa = 0 if estado not in self.__recompensas else self.__recompensas[estado]
        return recompensa + self.__gama * utilidades[estado_sucessor]
    
    
    