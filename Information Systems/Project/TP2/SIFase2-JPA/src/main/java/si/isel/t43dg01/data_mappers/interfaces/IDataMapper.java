package si.isel.t43dg01.data_mappers.interfaces;

public interface IDataMapper<T, TK> {
    TK create(T entity);
    T read(TK id);
    TK update(T entity);
    TK delete(T entity);
}