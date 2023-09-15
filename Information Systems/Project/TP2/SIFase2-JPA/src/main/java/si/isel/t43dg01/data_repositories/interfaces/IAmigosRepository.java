package si.isel.t43dg01.data_repositories.interfaces;

import si.isel.t43dg01.orm.Amigos;
import si.isel.t43dg01.orm.AmigosPK;

import java.util.Collection;

public interface IAmigosRepository extends IRepository<Amigos, Collection<Amigos>, AmigosPK>{
}