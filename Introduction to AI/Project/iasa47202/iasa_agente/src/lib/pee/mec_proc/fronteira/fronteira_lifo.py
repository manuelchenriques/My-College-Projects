

## Insersção que segue a norma Last In First Out
from pee.mec_proc.fronteira.fronteira import Fronteira
from pee.mec_proc.no import No


class FronteiraLIFO(Fronteira):
    
    def inserir(self, no: No):
        self._nos.insert(0, no)