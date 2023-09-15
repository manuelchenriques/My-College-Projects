
from iasa_agente.src.lib.ecr.comportamento import Comportamento
from lib.sae.agente.accao import Accao
from lib.sae.agente.percepcao import Percepcao


## Representa um comportamento complexo, o qual é constituido por mais que um comportamento
class ComportComp(Comportamento):
    
    ## Inicializa a a classe ComportComp com uma lista de comportamentos que o constituem
    def __init__(self, comportamentos: list[Comportamento]):
        self._comportamentos = comportamentos
        
    ## Activa todos os comportamentos e guarda as acções resultantes em uma lista.
    ## De seguida retorna a acção que foi selecionada de acordo com a sua prioridade/hierarquia
    def activar(self, percepcao: Percepcao) -> Accao:
        accao_list = []

        for comportamento in self._comportamentos:
            accao = comportamento.activar(percepcao)
            if accao:
                accao_list.append(accao)
                
        if accao_list:
            return self.selecionar_accao(accao_list)
               
    ## Seleciona a accção relevante
    def selecionar_accao(self, accoes: list[Accao])-> Accao:
        pass
    