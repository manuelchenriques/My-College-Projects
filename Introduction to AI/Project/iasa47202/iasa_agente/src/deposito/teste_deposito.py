


from apresent.vista_trajecto import VistaTrajecto
from deposito.apresent.vista_deposito import VistaDeposito
from deposito.mod_prob.planeador_deposito import PleanadorDeposito


VOL_INI = 0
VOL_FIN = 9

def main():
    solucao = PleanadorDeposito().planear(VOL_INI, VOL_FIN)

    VistaDeposito().mostrar(solucao, VOL_INI)
    
if __name__ == "__main__":
    main()