from mod.estado import Estado

## Estado das pilhas de blocos
class EstadoBloco(Estado):
    
    ## Inicializado com um array de arrays, que contêm o estado inicial de cada pilha
    def __init__(self, pilhas) -> None:
        self.pilhas = pilhas
        super().__init__()
    
    def id_valor(self) -> int:
        resultado = []
        for lista in self.pilhas:
            resultado.append(tuple(lista))
        return hash(tuple(resultado))
    
    def __hash__(self) -> int:
        return super().__hash__()
    