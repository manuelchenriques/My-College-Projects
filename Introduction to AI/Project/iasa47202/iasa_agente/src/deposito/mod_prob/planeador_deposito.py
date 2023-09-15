

from deposito.mod_prob.estado_balde import EstadoBalde
from deposito.mod_prob.deposito_problema import DepositoProblema
from deposito.mod_prob.agua_transferencia import OperadorTransferirAgua
from pee.melhor_prim.aval.avaliador import Avaliador
from pee.melhor_prim.aval.avaliador_custo_unif import AvaliadorCustoUnif
from pee.melhor_prim.procura_custo_unif import ProcuraCustoUnif
from pee.melhor_prim.procura_melhor_prim import ProcuraMelhorPrim

## Define o problema e efectua a procura de solução
class PleanadorDeposito:
    
    def planear(self, volume_inicial, volume_final):
        
        problema = DepositoProblema(EstadoBalde(volume_inicial), EstadoBalde(volume_final))
        procura = ProcuraCustoUnif()
        return procura.procurar(problema)