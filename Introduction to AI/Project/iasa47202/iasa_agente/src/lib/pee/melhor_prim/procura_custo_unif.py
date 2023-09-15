

##Procura do melhor com base no custo
from pee.melhor_prim.aval.avaliador_custo_unif import AvaliadorCustoUnif
from pee.melhor_prim.procura_melhor_prim import ProcuraMelhorPrim


class ProcuraCustoUnif(ProcuraMelhorPrim):
    
    def __init__(self) -> None:
        super().__init__(AvaliadorCustoUnif())