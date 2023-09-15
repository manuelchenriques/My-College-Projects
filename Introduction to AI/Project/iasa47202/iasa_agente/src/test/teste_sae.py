

#_________________________________________________
# Controlo de teste

from lib.sae.agente.controlo import Controlo
from lib.sae.simulador import Simulador


class ControloTest(Controlo):
    def processar(self, percepcao):
        pass
        

#_________________________________________________
# Activação

controlo = ControloTest()
Simulador(3, controlo).executar()