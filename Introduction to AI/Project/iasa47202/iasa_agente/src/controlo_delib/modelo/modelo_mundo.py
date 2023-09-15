




from math import dist
from controlo_delib.modelo.operador_mover import OperadorMover
from mod.estado import Estado
from mod.estado_agente import EstadoAgente
from mod.operador import Operador
from plan.modelo.modelo_plan import ModeloPlan
from sae.agente.percepcao import Percepcao
from sae.ambiente.direccao import Direccao
from sae.ambiente.elemento import Elemento
from sae.vistas.vista_amb import VistaAmb

## Modelo que representativo do ambiente em que o agente está inserido
class ModeloMundo(ModeloPlan):

    def __init__(self) -> None:
        self.__estado: EstadoAgente = None
        self.__estados = []
        self.__operadores = [
            OperadorMover(self, Direccao.NORTE),
            OperadorMover(self, Direccao.SUL),
            OperadorMover(self, Direccao.ESTE),
            OperadorMover(self, Direccao.OESTE)
            ]
        self.__elementos = dict()
        self.__alterado = False

    ## Retorna o estado do agente
    def obter_estado(self) -> Estado:
        return self.__estado
    
    ## Retorna a lista de estados do ModeloMundo
    def obter_estados(self) -> list[Estado]:
        return self.__estados
    
    ## Retorna a lista de operadores
    def obter_operadores(self) -> list[Operador]:
        return self.__operadores
    
    ## Dado um estado, retorna o elemento que lhe corresponde no modelo
    def obter_elemento(self, estado: Estado) -> Elemento:
        return self.elementos.get(estado.posicao)
    
    ## Retorna a distancia entre o estado do agente e um estado fornecido
    def distancia(self,estado: EstadoAgente) -> float:
        return dist(self.__estado.posicao, estado.posicao)
    
    ## Dada uma percepção, verifica se ocurreram alterações no estado do modelo e,
    ## caso tenha, altera a flag "alterado" e actualiza todos os dados do modelo
    def actualizar(self, percepcao: Percepcao):
        self.__estado = EstadoAgente(percepcao.posicao)
        if self.elementos != percepcao.elementos:
            self.__alterado = True
            self.__elementos = percepcao.elementos
            self.__estados = []
            for pos in percepcao.posicoes:
                self.__estados.append(EstadoAgente(pos))
        else:
            self.__alterado = False
    
    ## Actualiza a componente visual do modelo
    def mostrar(self, vista: VistaAmb):
        vista.mostrar_elementos(self.elementos)
        vista.marcar_posicao(self.__estado.posicao)

    ## Getter de estados
    @property
    def elementos(self):
        return self.__elementos
    
    @property
    ## Getter de alterado
    def alterado(self):
        return self.__alterado