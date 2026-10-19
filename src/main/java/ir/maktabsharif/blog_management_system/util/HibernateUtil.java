package ir.maktabsharif.blog_management_system.util;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Function;

public class HibernateUtil {
    private static final String PERSISTENCE_UNITE = "blog_management";

    private static EntityManagerFactory emf;

    private HibernateUtil(){

    }
    private static synchronized EntityManagerFactory getEmf(){
        if(emf== null){
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNITE);
        }
        return emf;
    }

    public static <T> T inTxResult(Function<EntityManager, T> operation) {
        EntityManager em = getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = operation.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
