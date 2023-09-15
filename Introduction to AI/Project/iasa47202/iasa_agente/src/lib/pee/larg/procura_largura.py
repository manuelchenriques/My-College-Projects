


from pee.mec_proc.fronteira.fronteira_fifo import FronteiraFIFO
from pee.mec_proc.procura_grafo import ProcuraGrafo

## Procura em largura
class ProcuraLargura(ProcuraGrafo):
    
    ##Inicia a classe com uma fronteira do tipo First In First Out, que vai originar um busca em largura
    def __init__(self) -> None:
        super().__init__(FronteiraFIFO())
        
    