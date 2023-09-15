

from pee.mec_proc.mecanismo_procura import MecanismoProcura
from pee.mec_proc.no import No


class ProcuraGrafo (MecanismoProcura):
    
    ## Inicia a memoria e a lista dos explorados
    def _iniciar_memoria(self) -> None:
        super()._iniciar_memoria()
        self._explorados = dict()
    
    ## Memoriza um nó, adicionando-o a lista de explorados, se a função manter permitir
    def _memorizar(self, no: No) -> None:
        if self._manter(no):
            self._fronteira.inserir(no)
            self._explorados[no.estado] = no
            
      
    ## Verifica se um nó já está guardado em explorados      
    def _manter(self, no):
        return no.estado not in self._explorados