package ir.maktabsharif.blog_management_system.repository;



import ir.maktabsharif.blog_management_system.model.User;
import ir.maktabsharif.blog_management_system.util.HibernateUtil;

import java.util.List;

public class UserRepository implements BaseRepository<User> {
    @Override
    public void save(User user) {
        HibernateUtil.inTxResult(entityManager -> {
            entityManager.persist(user);
            return null;
        });
    }
    @Override
    public void update(User user) {
        HibernateUtil.inTxResult(entityManager -> {
            entityManager.merge(user);
            return null;
        });
    }
    @Override
    public boolean delete(Long id) {
        return HibernateUtil.inTxResult(entityManager -> {
            User user = entityManager.find(User.class, id);

            if (user == null) {
                return false;
            }

            entityManager.remove(user);
            return true;
        });
    }
    @Override
    public User findById(Long id) {
        return HibernateUtil.inTxResult(entityManager ->
                entityManager.find(User.class, id)
        );
    }

    public User findByEmail(String email) {
        return HibernateUtil.inTxResult(entityManager ->
                entityManager.createQuery(
                                "SELECT u FROM User u WHERE u.email = :email",
                                User.class
                        )
                        .setParameter("email", email)
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
        );
    }

    public List<User> findAll() {
        return HibernateUtil.inTxResult(entityManager ->
                entityManager.createQuery(
                        "SELECT u FROM User u",
                        User.class
                ).getResultList()
        );
    }
}
