
from iasa_agente.src.controlo_react.reacoes.aproximar.aproximar_alvo import AproximarAlvo
from iasa_agente.src.controlo_react.reacoes.evitar.evitar_obst import EvitarObst
from iasa_agente.src.controlo_react.reacoes.explorar.explorar import Explorar
from iasa_agente.src.lib.ecr.hierarquia import Hierarquia

## Comportamento Composto, que extende de hierarquia, com uma hierarquia de comportamento
class Recolher(Hierarquia):
    
    def __init__(self):
        comportamentos = [
            AproximarAlvo(),
            EvitarObst(),
            Explorar()
        ]
        super().__init__(comportamentos)