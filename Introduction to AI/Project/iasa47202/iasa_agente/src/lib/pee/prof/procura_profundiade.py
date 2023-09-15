

##Procura em Profundidade
from pee.mec_proc.fronteira.fronteira_lifo import FronteiraLIFO
from pee.mec_proc.mecanismo_procura import MecanismoProcura
from pee.mec_proc.no import No


class ProcuraProfundidade(MecanismoProcura):
    
    ##Inicia a classe com uma fronteira do tipo Last In First Out, que vai originar um busca em profundiade
    def __init__(self) -> None:
        super().__init__(FronteiraLIFO())

    ##A memorização em profundidade apenas simplesmente adiciona o nó à fronteira
    def _memorizar(self, no: No) -> None:
        self._fronteira.inserir(no)
    
