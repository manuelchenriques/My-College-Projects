


from pee.mec_proc.no import No
from pee.melhor_prim.aval.avaliador_heur import AvaliadorHeur


class AvaliadorSof(AvaliadorHeur):
    
    def prioridade(self, no: No):
        return super().prioridade(no)