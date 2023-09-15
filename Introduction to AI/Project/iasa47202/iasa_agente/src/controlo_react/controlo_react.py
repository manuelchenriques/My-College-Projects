



from ecr.Comportamento import Comportamento
from sae.agente.controlo import Controlo

## Controlo Reactivo
class ControloReact(Controlo):
    
    ## Recebe um comportamento, que define o comportamento geral do agente
    def __init__(self, comportamento: Comportamento):
        self.__comportamento = comportamento
        self.mostrar_per_dir = True
    
    ## Faz o processamento de uma precepção de acordo com o comportamento fornecido
    def processar(self, percepcao):
        return self.__comportamento.activar(percepcao)