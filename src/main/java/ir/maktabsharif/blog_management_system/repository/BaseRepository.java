package ir.maktabsharif.blog_management_system.repository;

import ir.maktabsharif.blog_management_system.model.Post;

public interface BaseRepository<T > {
    void save(T t);

    void update(T t);

    boolean delete(Long id);

    T findById(Long id);


}
