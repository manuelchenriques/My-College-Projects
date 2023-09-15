


from pee.mec_proc.no import No
from pee.melhor_prim.aval.avaliador import Avaliador

## Atribui a prioridade ao custo do no
class AvaliadorCustoUnif(Avaliador):
    
    def prioridade(self, no: No):
        return no.custo