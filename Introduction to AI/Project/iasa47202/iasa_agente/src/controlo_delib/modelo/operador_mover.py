
import math
from lib.mod.operador import Operador
from mod.estado import Estado
from mod.estado_agente import EstadoAgente
from sae.ambiente.direccao import Direccao
from lib.sae.agente.accao import Accao

##Operador responsavel pela mudança de Estado (posição) do agente
class OperadorMover(Operador):
    
    def __init__(self, modelo_mundo, direccao: Direccao) -> None:
        self.modelo_mundo = modelo_mundo
        self.accao = Accao(direccao)
        self.ang = self.accao.ang
    
    ## Aplica a mudança de estado de acordo com a acção do Operador
    def aplicar(self, estado: EstadoAgente) -> Estado:
        x, y = estado.posicao
        dx = round(self.accao.passo * math.cos(self.ang))
        dy = -round(self.accao.passo * math.sin(self.ang))
        novo_estado = EstadoAgente((x+ dx, y + dy))
        if novo_estado in self.modelo_mundo.obter_estados():
            return novo_estado
    
    ##Retorna o custo da alteração de um estado para outro
    def custo(self, estado: Estado, estado_suc: Estado) -> float:
        return max(1,math.dist(estado.posicao,estado_suc.posicao))