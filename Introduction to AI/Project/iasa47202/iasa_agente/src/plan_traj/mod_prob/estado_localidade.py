



from mod.estado import Estado

## Define um estado equivalente a uma loclização
class EstadoLocalidade(Estado):
    
    def __init__(self, localidade: str) -> None:
        self.__localidade = localidade

    def id_valor(self) -> int:
        return hash(self.localidade)
    
    #Definir como mostrar estado
    def __repr__(self) -> str:
        return self.localidade
    
    @property
    def localidade(self)-> str:
        return self.__localidade