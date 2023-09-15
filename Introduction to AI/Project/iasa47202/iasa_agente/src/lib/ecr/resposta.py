
## Representa uma resposta a um estimulo
class Resposta:
    
    ## É inicializada com a acção resultante do estimulo
    def __init__(self, accao):
        self._accao = accao
    
    ## Retorna a acção já com a itensidade com que a respota foi activa
    def activar(self, percepcao, intensidade):
        self._accao.prioriade = intensidade
        return self._accao