package DataBase.Dao;

import java.util.List;

interface Dao<T> {

    T findByCommand(String command);
    List<T> getAll();
    void insert(T t);
    void delete(T t);

}