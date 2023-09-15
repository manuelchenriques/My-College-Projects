


from pee.melhor_prim.aval.avaliador_sof import AvaliadorSof
from pee.melhor_prim.procura_informada import ProcuraInformada


class ProcuraSofrega(ProcuraInformada):
    
    def __init__(self) -> None:
        super().__init__(AvaliadorSof())