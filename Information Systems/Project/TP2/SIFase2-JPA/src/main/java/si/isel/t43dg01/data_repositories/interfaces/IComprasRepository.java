package si.isel.t43dg01.data_repositories.interfaces;

import si.isel.t43dg01.orm.Compras;
import si.isel.t43dg01.orm.ComprasPK;

import java.util.Collection;

public interface IComprasRepository  extends IRepository<Compras, Collection<Compras>, ComprasPK>{
}