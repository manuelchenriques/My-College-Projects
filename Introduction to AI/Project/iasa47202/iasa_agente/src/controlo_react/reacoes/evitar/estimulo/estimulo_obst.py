
from ecr.Estimulo import Estimulo
from sae.agente.percepcao import Percepcao
from sae.ambiente.direccao import Direccao
from sae.ambiente.elemento import Elemento

##Estimulo causado pela percepção de um obstaculo
class EstimuloObst(Estimulo):
    
    def __init__(self, direccao: Direccao, intensidade: float = 1) -> None:
        self.__direccao = direccao
        self.__intensidade = intensidade
        
    ##Se o estimulo for resultante de um obstaculo que se encontra logo à frente do agente
    ##gera uma intensidade equivalente à passada no constructor
    def detectar(self, percepcao: Percepcao) -> float:
        dirPer = percepcao.per_dir[self.__direccao]
        if(percepcao.contacto_obst(self.__direccao)):
            return self.__intensidade
        return 0
        