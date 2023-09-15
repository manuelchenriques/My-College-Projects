from lib.pee.mec_proc.solucao import Solucao

## Permite fazer um print estruturado da solução
class VistaTrajecto:
    
    def mostrar(self, solucao: Solucao):
        if not solucao:
            print("No solution found.")
            return
        while no := solucao.remover():
            print(no.estado, "--->", no.operador)