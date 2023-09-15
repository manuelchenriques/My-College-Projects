
from abc import ABC, abstractmethod
from mod.problema.problema import Problema
from pee.benchmark.proccess_data import ProcessData

from pee.mec_proc.fronteira.fronteira import Fronteira
from pee.mec_proc.no import No
from pee.mec_proc.solucao import Solucao


## Mecanismon de procura em uma arvore de pesquisa
class MecanismoProcura(ABC):
    
    ## Inicia a classe com uma Fronteira inicial
    def __init__(self, fronteira: Fronteira) -> None:
        self._fronteira = fronteira
        self.__benchmark = ProcessData()
    
    ## Inicializa a Fronteira. Verifica que a memoria está limpa
    def _iniciar_memoria(self) -> None:
        self._fronteira.iniciar()
    
    ## Memoriza nó de acordo com o tipo de procura
    @abstractmethod
    def _memorizar(self, no: No)-> None:
        pass
    
    ## Inicia a procura na arvore por um nó que resolva o problema fornecido
    def procurar(self, problema: Problema) -> Solucao:
        self._iniciar_memoria()
        no = No(problema.estado_inicial)
        self._fronteira.inserir(no)
        
        while not self._fronteira.vazia:
            no = self._fronteira.remover()
            if problema.objectivo(no.estado):
                self.__benchmark.print_results()
                return Solucao(no)
            else:
                for no_suc in self._expandir(problema, no):
                    self._memorizar(no_suc)
                    self.__benchmark.actualizar_memorizados(len(self._fronteira._nos))
        self.__benchmark.print_results()
                    

    ## Expande o nó fornecido
    def _expandir(self, problema: Problema, no: No):
        self.__benchmark.incr_processados()
        for operador in problema.operadores:
            estado_suc = operador.aplicar(no.estado)
            if estado_suc is not None:
                yield No(estado_suc, operador, no)
                
                