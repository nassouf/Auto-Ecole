package org.example.service.myInterfaces;

import java.util.List;

public interface DBservices<T> {
    public <T> List<T> getAll();
    public <T> T getById(Integer id);
    public boolean create(T obj);
    public boolean update(Integer id, T obj);
    public boolean delete(Integer id);
}
