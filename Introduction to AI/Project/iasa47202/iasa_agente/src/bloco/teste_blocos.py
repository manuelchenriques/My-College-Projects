

from bloco.mod_prob.estado_blocos import EstadoBloco
from bloco.mod_prob.operador_empilhar import OperadorEmpilhar
from bloco.mod_prob.operador_desempilhar import OperadorDesempilhar
from bloco.mod_prob.problema_blocos import ProblemaBloco
from pee.mec_proc.solucao import Solucao
from pee.melhor_prim.procura_custo_unif import ProcuraCustoUnif


def main():
    pilha_inicial = [
        [4,2,3,5,1],
        [],
        []
    ]
    pilha_final = [
        [1,2,3,4,5],
        [],
        []
    ]
    operadores = [
        OperadorEmpilhar(1),
        OperadorEmpilhar(2),
        OperadorDesempilhar(1),
        OperadorDesempilhar(2)
    ]
    problema = ProblemaBloco(EstadoBloco(pilha_inicial), EstadoBloco(pilha_final), operadores)
    solucao = ProcuraCustoUnif().procurar(problema)
    printSolucao(solucao)
    
def printSolucao(solucao: Solucao):
    for no in solucao._percurso:
        print(str(no.estado.pilhas) + "   " + str(no.operador))
    print("\n\n")
    print("Dimensão: " + str(len(solucao._percurso)))
    print("Custo: " + str(solucao._percurso[-1].custo))

main()