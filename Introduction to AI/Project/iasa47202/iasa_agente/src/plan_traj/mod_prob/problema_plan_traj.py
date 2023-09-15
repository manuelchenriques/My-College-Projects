
from lib.mod.problema.problema import Problema
from mod.estado import Estado
from mod_prob.estado_localidade import EstadoLocalidade
from plan_traj.mod_prob.operador_ligacao import OperadorLigacao
from planeador.ligacao import Ligacao

## Define um problema de planeamento de trajecto, com o estado inicial do problema,
## estado final desejado, e ligações existentes entre os nos
class ProblemaPlanTraj(Problema):
    
    ##Inicia um Problema, tornando as ligações em OperadoresLigação que irão permitir a interação de estados
    def __init__(self, ligacoes: list[Ligacao], loc_inicial: str, loc_final: str) -> None:
        self.__estado_final = EstadoLocalidade(loc_final)
        operadores = []
        for lig in ligacoes:
            operadores.append(OperadorLigacao(lig.origem, lig.destino, lig.custo))
        super().__init__(EstadoLocalidade(loc_inicial), operadores)
    

    ##Verifica se o estado fornecido é o estado final(desejado)
    def objectivo(self, estado: Estado) -> bool:
        return self.__estado_final.__eq__(estado)