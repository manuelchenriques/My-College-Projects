

## Representa uma solução da procura
from pee.mec_proc.no import No


class Solucao:
    
    ## Recebe o nó final e, a partir dos seus antecessores, 
    # constroi todo o seu percurso até ao nó inicial
    def __init__(self, no_final: No) -> None:
        self._percurso = []
        no = no_final
        while no:
            self._percurso.insert(0, no)
            no = no.antecessor
    
    ## Remove Nó do inicio do percurso da solução
    def remover(self) -> No:
        if len(self._percurso) != 0:
            return self._percurso.pop(0)
    
    ## Retorna iterator do percurso da solucao
    def __iter__(self):
        return iter(self._percurso)
        
    ## Retorna nó do percurso do index fornecido
    def __getitem__(self, index: int) -> No:
        return self._percurso[index]