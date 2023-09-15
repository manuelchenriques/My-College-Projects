
from controlo_delib.mec_delib import MecDelib
from controlo_delib.modelo.modelo_mundo import ModeloMundo
from plan.planeador import Planeador
from sae.agente.accao import Accao
from sae.agente.controlo import Controlo
from sae.agente.percepcao import Percepcao

## Controlo Deliberativo
class ControloDelib(Controlo):
    
    def __init__(self, planeador: Planeador) -> None:
        self.__planeador = planeador
        self.__objectivos = []
        self.__modelo_mundo = ModeloMundo()
        self.__mec_delib = MecDelib(self.__modelo_mundo)
        super().__init__()
    
    ## Processa um ciclo de deliberação
    def processar(self, percepcao: Percepcao)-> Accao:
        self.__assimilar(percepcao)
        if self.__reconsiderar():
            self.__deliberar()
            self.__planear()
        return self.__executar()
    
    ## Dada uma percepção, actualiza o modelo_mundo caso haja alterações no estado do ambiente
    def __assimilar(self, percepcao: Percepcao):
        self.__modelo_mundo.actualizar(percepcao)
    
    ## Verifica se houve alterações no modelo mundo de modo que,
    ## caso haja, actualizar os objectivos
    def __reconsiderar(self) -> bool:
        return self.__modelo_mundo.alterado or self.__plano is None
    
    ## Actualiza a lista de objectivos com a lista de objectivos gerada pelp
    ## mecanismo de deliberação
    def __deliberar(self):
        self.__objectivos = self.__mec_delib.deliberar()
    
    ## Através do planejador, constroi um plano de um percurso até um dos objectivos
    def __planear(self):
        self.__plano = self.__planeador.planear(self.__modelo_mundo, self.__objectivos)
    
    ## Caso tenha um plano de percurso, retorna a acção para a proxima posição do percurso do plano
    def __executar(self)-> Accao:
        if self.__plano and self.__objectivos:
            estado = self.__modelo_mundo.obter_estado()
            operador = self.__plano.obter_accao(estado)
            if operador:
                self.__mostrar()
                return operador.accao
    
    ## Actualiza o modelo visual com o estado actual de deliberação
    def __mostrar(self):
        self.vista.limpar()
        self.__modelo_mundo.mostrar(self.vista)
        if self.__plano:
            self.__plano.mostrar(self.vista)
        if self.__objectivos:
            self.vista.mostrar_estados(self.__objectivos)