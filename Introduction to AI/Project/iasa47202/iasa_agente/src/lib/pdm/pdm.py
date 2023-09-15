
from pdm.mec_util import MecUtil
from pdm.modelo.modelo_pdm import ModeloPDM

## Classe responsavel pela realização do Procura de Decisão de Markov
class PDM:
    
    def __init__(self, modelo: ModeloPDM, gama, delta_max: int) -> None:
        self.__modelo = modelo
        self.__mec_util = MecUtil(modelo, gama, delta_max)

    ## Recebe um dicionario de (estado: Estado, utilidade: Double) com todos os estados.
    ## O metodo define, para todos os estados do modelo, a acção preferivel de modo a que o estado seguinte
    ## seja o com maior nivel de utilidade.
    def politica(self, utilidade):
        politica = dict()
        acoes = self.__modelo.obter_acoes()
        objetivos = self.__modelo.obter_recompensas()
        transicoes = self.__modelo.obter_transicoes()
        
        for estado in utilidade:
            if estado in objetivos: continue
            politica_estado = []
            for op in acoes:
                novo_estado = transicoes.get((estado, op))
                if novo_estado:
                    transacao = (op, utilidade[novo_estado])
                    politica_estado.append(transacao)
                    
            politica[estado] = max(politica_estado, key=lambda x: x[1])[0]
        return politica
    
    ## Calcula a utilidade e politica to modelo fornecido no constructor e retorna em formula de tuplo
    ## (utilidade, politica)
    def resolver(self):
        utilidade = self.__mec_util.utilidade()
        politica = self.politica(utilidade)
        return (utilidade, politica)