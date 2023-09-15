from dataclasses import dataclass

#Define uma ligação, com uma origem, destino e custo
@dataclass
class Ligacao:
    origem: str
    destino: str
    custo: int