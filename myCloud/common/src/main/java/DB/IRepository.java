package DB;

import java.util.List;

public interface IRepository<T> {
    void insert(T entity);
    void update(T entity);
    void delete(int id);
    T get(int id);
    List<T> get();
}
