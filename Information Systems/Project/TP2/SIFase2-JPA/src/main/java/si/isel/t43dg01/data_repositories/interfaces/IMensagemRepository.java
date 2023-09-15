package si.isel.t43dg01.data_repositories.interfaces;

import si.isel.t43dg01.orm.Mensagem;
import si.isel.t43dg01.orm.MensagemPK;

import java.util.Collection;

public interface IMensagemRepository extends IRepository<Mensagem, Collection<Mensagem>, MensagemPK>{
}