



from apresent.vista_trajecto import VistaTrajecto
from planeador.ligacao import Ligacao
from planeador.planeador_trajecto import PlaneadorTrajecto


LOC_INI = "0"
LOC_FIN = "4"
LIGACOES=[
    Ligacao("0", "1", 5),
    Ligacao("0", "2", 25),
    Ligacao("0", "7", 7),
    Ligacao("1", "3", 12),
    Ligacao("1", "6", 5),
    Ligacao("2", "7", 5),
    Ligacao("2", "8", 15),
    Ligacao("2", "4", 30),
    Ligacao("3", "2", 10),
    Ligacao("3", "5", 5),
    Ligacao("4", "3", 2),
    Ligacao("5", "4", 10),
    Ligacao("5", "6", 8),
    Ligacao("5", "1", 9),
    Ligacao("6", "3", 15),
    Ligacao("7", "8", 8),
    Ligacao("8", "4", 25),
    Ligacao("9", "6", 2),
    Ligacao("9", "10", 2),
    Ligacao("10", "4", 2)
]

def main():
    solucao = PlaneadorTrajecto().planear(LIGACOES, LOC_INI, LOC_FIN)
    
    VistaTrajecto().mostrar(solucao)
    
if __name__ == "__main__":
    main()