package si.isel.t43dg01.data_repositories.interfaces;

import java.util.List;

public interface IRepository<T,TCol,TK> {
    T findByKey(TK key);
    TCol find(String jpql, Object... params);
    List<T> findAll();

}