
import random
from iasa_agente.src.controlo_react.reacoes.resposta.resposta_mover import RespostaMover
from iasa_agente.src.lib.sae.agente.accao import Accao
from iasa_agente.src.lib.sae.agente.percepcao import Percepcao
from iasa_agente.src.lib.sae.ambiente.direccao import Direccao

##Resposta a uma reacção de evitarObst
class RespostaEvitar(RespostaMover):
    
    def __init__(self, dir_inicial: Direccao = Direccao.ESTE):
        super().__init__(dir_inicial)
        
    ## Se a direcção estiver livre, activa uma respostaMover para essa direcção. Caso contrario, activa
    ## para uma direcção que esteja livre. Caso não haja nenhuma direcção livre, retorna None
    def activar(self, percepcao: Percepcao, intensidade: float) -> Accao:
        if(percepcao.contacto_obst(self.accao.direccao)):
            dir_livre = self.__direccao_livre(percepcao)
            if(dir_livre):
                return RespostaMover(dir_livre).activar(percepcao, intensidade)
            else: return    
        return super().activar(percepcao, intensidade)
        
    ## Retorna, aleatoriamente, uma direcção livre
    def __direccao_livre(self, percepcao: Percepcao) -> Direccao:
        dir_livres = list(Direccao)
    
        for dir in dir_livres:
            if(percepcao.contacto_obst(dir)):
                dir_livres.remove(dir)
                
        return random.choice(dir_livres)