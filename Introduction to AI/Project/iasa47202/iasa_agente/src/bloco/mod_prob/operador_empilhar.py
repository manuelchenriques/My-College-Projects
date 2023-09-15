
import copy
from bloco.mod_prob.estado_blocos import EstadoBloco
from bloco.mod_prob.operador_blocos import OperadorBlocos
from mod.estado import Estado

##Operador que executa um empilhamento
class OperadorEmpilhar(OperadorBlocos):
    
    ##Iniciado com o index da pilha de onde o bloco
    ## vai ser retirado e empilhado
    def __init__(self, idx) -> None:
        super().__init__(idx)
    
    ##Empilha o primeiro bloco da pilha com o indice com que foi inicializado
    ##e coloca-o no inicio da pilha de indice 0
    def aplicar(self, pilha):
        resultado = copy.deepcopy(pilha).pilhas
        if len(resultado[self.idx]) > 0:
            valor = resultado[self.idx].pop(0)
            resultado[0].insert(0, valor)
        return EstadoBloco(resultado)
    
    def __repr__(self) -> str:
        return "Empilhar(" + str(self.idx) + ")"