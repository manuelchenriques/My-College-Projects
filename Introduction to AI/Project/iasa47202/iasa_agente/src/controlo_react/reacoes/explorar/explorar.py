
import random
from iasa_agente.src.controlo_react.reacoes.resposta.resposta_mover import RespostaMover
from iasa_agente.src.lib.ecr.comportamento import Comportamento
from lib.sae.agente.accao import Accao
from lib.sae.agente.percepcao import Percepcao
from lib.sae.ambiente.direccao import Direccao


#Comportamento de explorar, onde o agente irá deslocar-se sem qualquer objectivo especifico
class Explorar(Comportamento):
    
    #Seleciona uma direção aleatoria, gera uma resposta de movimentação. Não utiliza a percepção
    #pois não necessita de tomar quaisquer decisões com base na percepção, apenas deslocar-se
    def activar(self, percepcao: Percepcao) -> Accao:
        direccao = random.choice(list(Direccao))
        return RespostaMover(direccao).activar(percepcao, 0)
        