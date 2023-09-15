

from controlo_delib.controlo_delib import ControloDelib
from plan_pdm.planeador_pdm import PlaneadorPDM
from sae.simulador import Simulador


controlo = ControloDelib(PlaneadorPDM(0.95, 1))

Simulador(4, controlo).executar()