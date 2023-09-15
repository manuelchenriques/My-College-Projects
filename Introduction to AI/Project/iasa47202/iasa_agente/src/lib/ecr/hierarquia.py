
from iasa_agente.src.lib.ecr.comport_comp import ComportComp

## Extensão de ComportaComp, onde a acção é selecionada com base na sua hierarquia
class Hierarquia(ComportComp):
    
    ## Ordena a lista de acções com base na sua posição na hierarquia e de seguida retorna a do topo
    def selecionar_accao(self, accoes):
        return accoes[0]