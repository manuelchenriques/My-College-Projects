from mod.estado import Estado
from mod.operador import Operador
from pee.mec_proc.no import No
from pee.mec_proc.solucao import Solucao
from plan.plano import Plano
from sae.vistas.vista_amb import VistaAmb

## Plano em PEE, que irá fornecer o percurso criado pelo planeador
class PlanoPEE(Plano):
    
    ## Iniciado com a solução
    def __init__(self, solucao: Solucao) -> None:
        self.__solucao = solucao
        super().__init__()

    ## Verifica se o estado fornecido corresponde ao estado actual no percurso da solução, de modo
    ## a ter a certeza que o estado se encontra no sitio certo. Se isso se verificar,retorna o operador do proximo nó
    ## no percurso da solução.
    def obter_accao(self, estado: Estado) -> Operador:
        if self.__solucao and self.__solucao.__getitem__(0).estado.__eq__(estado):
            self.__solucao.remover()
            return self.__solucao.__getitem__(0).operador
        
    ## Mostra a solução no modelo gráfico
    def mostrar(self, vista: VistaAmb):
        vista.mostrar_solucao(self.__solucao)