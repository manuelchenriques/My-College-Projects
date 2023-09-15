
from iasa_agente.src.lib.ecr.estimulo import Estimulo
from iasa_agente.src.lib.sae.ambiente.elemento import Elemento
from lib.sae.agente.percepcao import Percepcao
from lib.sae.ambiente.direccao import Direccao
class EstimuloAlvo(Estimulo):
    
    def __init__(self, direccao: Direccao, gama: float = 0.9) -> None:
        self.direccao = direccao
        self.gama = gama
        
    ## Se detectar um alvo na direcção, o estimulo gera uma determinada intensidade de acordo com a sua distancia
    def detectar(self, percepcao: Percepcao) -> float:
        if(percepcao.per_dir[self.direccao][0] == Elemento.ALVO):
            return self.gama ** percepcao.per_dir[self.direccao][1]
        else:
            return 0