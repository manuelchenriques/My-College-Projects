

import copy
from bloco.mod_prob.estado_blocos import EstadoBloco
from bloco.mod_prob.operador_blocos import OperadorBlocos

##Operador que executa um desempilhamento
class OperadorDesempilhar(OperadorBlocos):
    
    ##Iniciado com o index da pilha para o qual o bloco 
    ##desempilhado vai
    def __init__(self, idx) -> None:
        super().__init__(idx)
        
    ##Desempilha o primeiro bloco da pilha de indice 0
    ##e coloca-o na pilha com o index com o qual foi inicializado
    def aplicar(self, pilha):
        resultado = copy.deepcopy(pilha).pilhas
        if len(resultado[0])  > 0:
            valor = resultado[0].pop(0)
            resultado[self.idx].insert(0, valor)
        return EstadoBloco(resultado)
    
    def __repr__(self) -> str:
        return "Desempilhar(" + str(self.idx) + ")"
        