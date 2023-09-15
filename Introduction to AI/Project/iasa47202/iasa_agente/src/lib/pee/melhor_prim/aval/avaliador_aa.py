
from lib.pee.mec_proc.no import No
from lib.pee.melhor_prim.aval.avaliador_heur import AvaliadorHeur


class AvaliadorAA(AvaliadorHeur):
    
    def prioridade(self, no: No):
        return no.custo + self.heuristica.h(no.estado)