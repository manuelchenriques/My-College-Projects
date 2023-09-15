from mod.estado import Estado
from mod.operador import Operador
from plan.plano import Plano
from sae.vistas.vista_amb import VistaAmb


class PlanoPDM(Plano):
    
    ## inicializado com uma politica e utilidade
    def __init__(self, utilidade, politica) -> None:
        self.__utilidade = utilidade
        self.__politica = politica
        
    ## Fornecido um estado, retorna a acção que possibilita que o estado sucessor seja o com
    ## maior nivel de utilidade possivel
    def obter_accao(self, estado: Estado) -> Operador:
        if self.__politica:
            return self.__politica[estado]
        
    ## Mostra na componente visual do simulador os diferentes niveis de utilidade e politica de cada estado
    def mostrar(self, vista: VistaAmb):
        if self.__politica:
            vista.mostrar_valor(self.__utilidade)
            vista.mostrar_politica(self.__politica)