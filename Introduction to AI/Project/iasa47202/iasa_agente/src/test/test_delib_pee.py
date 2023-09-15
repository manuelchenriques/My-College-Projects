


from controlo_delib.controlo_delib import ControloDelib
from plan.plan_pee.planeador_heur_dinamica import PlaneadorHeurDinamica
from sae.simulador import Simulador
from custom_heur.calcular_dist import CalcularDist
from custom_heur.calcular_manh import CalcularManh

## Deve implementar interface CalcularHeur com a heuristica que deseja usar
heur1 = CalcularDist()
heur2 = CalcularManh()

controlo = ControloDelib(PlaneadorHeurDinamica(heur2))

Simulador(1, controlo).executar()


