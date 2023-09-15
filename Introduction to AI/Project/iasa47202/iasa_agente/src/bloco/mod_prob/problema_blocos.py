from bloco.mod_prob.estado_blocos import EstadoBloco
from mod.estado import Estado
from mod.problema.problema import Problema


##Define o problema do exercicio das pilhas de blocos
class ProblemaBloco(Problema):
    
    ## Inicializado com o estado inicial, estado final e lista de operadores
    def __init__(self, estado_inicial_pilhas: EstadoBloco, estado_final_pilhas: EstadoBloco, operadores) -> None:
        self.__estado_final = estado_final_pilhas
        super().__init__(estado_inicial_pilhas, operadores)
        
    @property
    def estado_final(self):
        return self.__estado_final
        
    ## Verifica se o estado fornecido corresponde ao estado final
    def objectivo(self, estado: Estado) -> bool:
        return self.estado_final.__eq__(estado)