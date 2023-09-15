package si.isel.t43dg01.data_repositories.interfaces;

import si.isel.t43dg01.orm.Partida;
import si.isel.t43dg01.orm.PartidaPK;

import java.util.Collection;

public interface IPartidaRepository extends IRepository<Partida, Collection<Partida>, PartidaPK>{
}