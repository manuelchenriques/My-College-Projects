from iasa_agente.src.controlo_react.controlo_react import ControloReact
from iasa_agente.src.controlo_react.reacoes.recolher import Recolher
from lib.sae.simulador import Simulador


comportamento = Recolher()
#comportamento = Explorar()
controlo = ControloReact(comportamento)

Simulador(1, controlo).executar()