

from pee.mec_proc.solucao import Solucao

## Permite fazer um print estruturado da solução
class VistaDeposito:
    def mostrar(self, solucao: Solucao, volume_inicial):
        if not solucao:
            print("No solution found.")
            return
        
        volume_final = None
        custo = 0
        result_operators = []
        while no := solucao.remover():
            if no.operador:
                volume_final = no.estado.volume
                custo += no.operador.custo(None, None)
                result_operators.append(no.operador)
            
        print("Volume inicial:", volume_inicial)
        print("Volume final:", volume_final)
        print("Solucao:", result_operators)
        print("Dimensão:", len(result_operators) + 1)
        print("Custo", custo)