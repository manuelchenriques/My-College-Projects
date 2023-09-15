
from controlo_delib.modelo.modelo_mundo import ModeloMundo
from sae.ambiente.elemento import Elemento

## Mecanismo de deliberação
class MecDelib:
    
    def __init__(self, modelo_mundo: ModeloMundo) -> None:
        self.__modelo_mundo = modelo_mundo
        
    ## Filtra a lista de estados do modelo_mundo, de modo a apenas ficar com os estados que
    ## representam alvos. Retorna uma lista ordenada de alvos.
    def deliberar(self):
        objectivos = []

        for estado in self.__modelo_mundo.obter_estados():
            if self.__modelo_mundo.obter_elemento(estado) == Elemento.ALVO:
                objectivos.append(estado)
        
        objectivos.sort(key=lambda x: self.__modelo_mundo.distancia(x))
        return objectivos