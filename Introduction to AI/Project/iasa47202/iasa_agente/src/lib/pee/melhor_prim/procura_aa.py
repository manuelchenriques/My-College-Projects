
from lib.pee.melhor_prim.aval.avaliador import Avaliador
from lib.pee.melhor_prim.aval.avaliador_aa import AvaliadorAA
from lib.pee.melhor_prim.procura_informada import ProcuraInformada


class ProcuraAA(ProcuraInformada):
    
    def __init__(self) -> None:
        super().__init__(AvaliadorAA())