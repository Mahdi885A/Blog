package ir.maktabsharif.blog_management_system.repository;

import ir.maktabsharif.blog_management_system.model.Post;
import ir.maktabsharif.blog_management_system.model.User;
import ir.maktabsharif.blog_management_system.util.HibernateUtil;

import java.util.Arrays;
import java.util.List;

public class PostRepository implements BaseRepository<Post> {

    @Override
    public void save(Post post) {
        HibernateUtil.inTxResult(entityManager -> {
                    entityManager.persist(post);

                    return null;
                }

        );

    }

    @Override
    public void update(Post post) {
        HibernateUtil.inTxResult(entityManager -> {
            entityManager.merge(post);
            return null;
        });

    }

    @Override
    public boolean delete(Long id) {
        return HibernateUtil.inTxResult(entityManager -> {
            Post post = entityManager.find(Post.class, id);

            if (post == null) {
                return false;
            }

            entityManager.remove(post);
            return true;
        });
    }


    @Override
    public Post findById(Long id) {
        return HibernateUtil.inTxResult(entityManager ->
                entityManager.find(Post.class, id)
        );
    }

    public List<Post> findAll() {
        return HibernateUtil.inTxResult(entityManager ->
                entityManager.createQuery(
                        "SELECT p FROM Post p",
                        Post.class
                ).getResultList()
        );
    }
}

