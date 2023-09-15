

## Inserção que segue a norma First In First Out
from pee.mec_proc.fronteira.fronteira import Fronteira
from pee.mec_proc.no import No


class FronteiraFIFO(Fronteira):
    
    def inserir(self, no: No):
        self._nos.append(no)