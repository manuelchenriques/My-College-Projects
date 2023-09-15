
from dataclasses import dataclass


@dataclass
class ProcessData:
    def __init__(self, processados: int = 0, guardados: int = 0) -> None:
        self.__nos_processados: int = processados
        self.__nos_guardados: int = guardados
        
    ## Incrementa o numero de nós processados
    def incr_processados(self):
        self.__nos_processados = self.__nos_processados + 1
    
    ## Se o numero recebido for superior ao numero de nós guardados, actualiza a variavel
    def actualizar_memorizados(self, guardados: int):
        if(self.__nos_guardados < guardados): self.__nos_guardados = guardados
        
    def print_results(self):
        print("\n---Benchmark results---\nNos processados: " + str(self.__nos_processados) + "\n" + "Nos guardados: "+ str(self.__nos_guardados) + "\n")
        
    @property
    def nos_processados(self):
        return self.__nos_processados
    
    @property
    def nos_guardados(self):
        return self.__nos_guardados